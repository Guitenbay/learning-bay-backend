package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.StmtIterator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.KElement;
import wiki.biki.learningbaybackend.model.Lesson;
import wiki.biki.learningbaybackend.model.UserKnowledgeState;
import wiki.biki.learningbaybackend.service.UserKnowledgeStateService;
import wiki.biki.learningbaybackend.service.impl.UserKnowledgeStateServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/fuseki/recommend")
public class RecommendLessonController {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.KE, PrefixConfig.CHAPTER, PrefixConfig.LESSON, PrefixConfig.SECTION})
    );
    private UserKnowledgeStateService userKnowledgeStateService = new UserKnowledgeStateServiceImpl();

    /**
     * recommend the lessons which need to review
     * @param uri: user's uri
     * @param courseUri
     * @return
     */
    @GetMapping("/review")
    public String getReviewRecommends(String uri, String courseUri) {
        JSONObject json = new JSONObject();
        if (uri == null) {
            json.put("res", false);
            return json.toJSONString();
        }
        ArrayList<Map<String, String>> lessons = LearningBayBackendApplication.fusekiApp.querySelectAsEntities(factory.build().set(SPARQLType.SELECT)
                .startSelect()
                .select("?uri").select("?title").select("?chapterUri")
                .endSelect()
                .startWhere()
                .where("?section rdf:type :Section;").where("section:belongs ?uri;").where("section:correspondKE ?review.")
                .where("?uri rdf:type :Lesson;").where("lesson:title ?title;").where("lesson:belongs ?chapterUri.")
                .where("?chapterUri rdf:type :Chapter;").where(String.format("chapter:belongs <%s>.", courseUri))
                .graph(String.format("<%s>", uri)).where("?review rdf:type :KElement;").where("ke:state ?state FILTER (?state = 0)")
                .endWhere()
                .toString());
        ArrayList<String> existed = new ArrayList<>();
        ArrayList<Map<String, String>> uniqueLessons = new ArrayList<>();
        for (Map<String, String> lesson : lessons) {
            String lessonUri = lesson.get("uri");
            if (!existed.contains(lessonUri)) {
                existed.add(lessonUri);
                uniqueLessons.add(lesson);
            }
        }
        json.put("res", true);
        json.put("data", uniqueLessons);
        return json.toJSONString();
    }
    /**
     * recommend the next lessons
     * @param uri: user's uri
     * @param courseUri
     * @return
     */
    @GetMapping
    public String getRecommends(String uri, String courseUri) {
        JSONObject json = new JSONObject();
        if (uri == null) {
            json.put("res", false);
            return json.toJSONString();
        }
        Model probableNextKElementModel = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .append(" ?last ")
                .startWhere()
                .where("?chapterUri rdf:type :Chapter;").where(String.format("chapter:belongs <%s>.", courseUri))
                .where("?lesson rdf:type :Lesson;").where("lesson:belongs ?chapterUri.")
                .where("?section rdf:type :Section;").where("section:belongs ?lesson;").where("section:correspondKE ?last.")
                .startUnion()
                .graph(GraphConfig.KE_GRAPH).where("?last rdf:type :KElement;").where("ke:previous ?prev.")
                .union()
                .graph(String.format("<%s>", uri)).where("?prev rdf:type :KElement;").where("ke:state ?state FILTER (?state > 0)")
                .endUnion().endWhere().toString());
        Map<String, Integer> knowledgeStateMap = new HashMap<>();
        for (UserKnowledgeState userKnowledgeState : userKnowledgeStateService.getStateListByUserUri(uri)) {
            knowledgeStateMap.put(userKnowledgeState.getUri(), userKnowledgeState.getState());
        }
        ArrayList<KElement> probableNextKElements = FusekiUtils.createEntityListFromModel(KElement.class, probableNextKElementModel);
        // 确定推荐的课程
        ArrayList<String> inferenceNextKElements = new ArrayList<>();
        probableNextKElements.forEach(probableNextKElement -> {
            // 若该知识点已经学习，则返回
            if (knowledgeStateMap.containsKey(probableNextKElement.getUri())) return;
            // 若该知识点未学习
            ArrayList<String> prevKElements = probableNextKElement.getPreviousList();
            if (prevKElements != null) {
                boolean result = true;
                // 判断是否前提知识点都满足要求
                for (String prevKElement : prevKElements) {
                    if (!knowledgeStateMap.containsKey(prevKElement)) {
                        result = false;
                        continue;
                    }
                    if (knowledgeStateMap.get(prevKElement) <= 0) {
                        result = false;
                    }
                }
                if (result) inferenceNextKElements.add(probableNextKElement.getUri());
            }
        });

        FusekiSPARQLStringBuilder builder = factory.build().set(SPARQLType.SELECT)
                .startSelect()
                .select("?uri").select("?title").select("?chapterUri")
                .endSelect()
                .startWhere()
                .where("?uri rdf:type :Lesson;").where("lesson:title ?title;").where("lesson:belongs ?chapterUri.")
                .where("?chapterUri rdf:type :Chapter;").where(String.format("chapter:belongs <%s>.", courseUri))
                .where("?section rdf:type :Section;").where("section:belongs ?uri;").where("section:correspondKE ?kElement.")
                .endWhere()
                .append("VALUES ?kElement {");
        for (String inferenceNextKElement : inferenceNextKElements) {
            builder = builder.append(String.format("<%s> ", inferenceNextKElement));
        }
        ArrayList<Map<String, String>> lessons = LearningBayBackendApplication.fusekiApp.querySelectAsEntities(builder.append("}").toString());
//        ArrayList<Lesson> lessons = FusekiUtils.createEntityListFromModel(Lesson.class, lessonModel);
        ArrayList<String> existed = new ArrayList<>();
        ArrayList<Map<String, String>> uniqueLessons = new ArrayList<>();
        for (Map<String, String> lesson : lessons) {
            String lessonUri = lesson.get("uri");
            if (!existed.contains(lessonUri)) {
                existed.add(lessonUri);
                uniqueLessons.add(lesson);
            }
        }
        json.put("res", true);
        json.put("data", uniqueLessons);
        return json.toJSONString();
    }
}
