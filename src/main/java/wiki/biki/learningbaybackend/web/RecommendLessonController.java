package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.apache.jena.rdf.model.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.KElement;
import wiki.biki.learningbaybackend.model.Section;
import wiki.biki.learningbaybackend.model.UserKnowledgeState;
import wiki.biki.learningbaybackend.service.KElementService;
import wiki.biki.learningbaybackend.service.SectionService;
import wiki.biki.learningbaybackend.service.UserKnowledgeStateService;
import wiki.biki.learningbaybackend.service.impl.KElementServiceImpl;
import wiki.biki.learningbaybackend.service.impl.SectionServiceImpl;
import wiki.biki.learningbaybackend.service.impl.UserKnowledgeStateServiceImpl;

import java.util.*;

@RestController
@RequestMapping(value = "/fuseki/recommend")
public class RecommendLessonController {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.KE, PrefixConfig.CHAPTER, PrefixConfig.LESSON, PrefixConfig.SECTION})
    );
    private UserKnowledgeStateService userKnowledgeStateService = new UserKnowledgeStateServiceImpl();
    private KElementService kElementService = new KElementServiceImpl();
    private SectionService sectionService = new SectionServiceImpl();

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
        // 确定推荐的知识元
        ArrayList<String> reasonableNextKElements = new ArrayList<>();
        probableNextKElements.forEach(probableNextKElement -> {
            // 若该知识元已经学习，则返回
            if (knowledgeStateMap.containsKey(probableNextKElement.getUri())) return;
            // 若该知识元未学习
            ArrayList<String> prevKElements = probableNextKElement.getPreviousList();
            if (prevKElements != null) {
                boolean result = true;
                // 判断是否前提知识元都满足要求
                for (String prevKElement : prevKElements) {
                    if (!knowledgeStateMap.containsKey(prevKElement)
                        || knowledgeStateMap.get(prevKElement) <= 0) {
                        result = false;
                    }
                    if (!result) break;
                }
                if (result) reasonableNextKElements.add(probableNextKElement.getUri());
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
        for (String inferenceNextKElement : reasonableNextKElements) {
            builder = builder.append(String.format("<%s> ", inferenceNextKElement));
        }
        ArrayList<Map<String, String>> lessons = LearningBayBackendApplication.fusekiApp.querySelectAsEntities(builder.append("}").toString());
        ArrayList<String> existed = new ArrayList<>();
        ArrayList<Map<String, String>> uniqueLessons = new ArrayList<>();
        for (Map<String, String> lesson : lessons) {
            String lessonUri = lesson.get("uri");
            if (!existed.contains(lessonUri)) {
                existed.add(lessonUri);
                // 判断 lesson 内的所有 section 对应的知识元的前置条件都成立
                if (couldRecommend(lessonUri, reasonableNextKElements, knowledgeStateMap)) {
                    uniqueLessons.add(lesson);
                }
            }
        }
        json.put("res", true);
        json.put("data", uniqueLessons);
        return json.toJSONString();
    }

    private boolean couldRecommend(String lessonUri, List<String> inferenceNextKElements, Map<String, Integer> knowledgeStateMap) {
        List<Section> sections = sectionService.getSectionListByLessonUri(lessonUri);
        sections.sort(Comparator.comparingInt(Section::getSequence));
        List<String> prevSectionsKElementUris = new ArrayList<>();
        for (Section section : sections) {
            String kElementUri = section.getUri();
            // 若当前 section 对应的 kElement 不是推理出来的下阶段学习知识元，
            // 且 kElement 的前置知识元也都不是本课程之前 section 的知识元或者已掌握知识元
            if (!inferenceNextKElements.contains(kElementUri)) {
                boolean result = true;
                List<String> prevKElements = kElementService.getKElementByUri(kElementUri).getPreviousList();
                if (prevKElements == null) prevKElements = new ArrayList<>();
                for (String prevKElement : prevKElements) {
                    // kElement 的前置知识元都不是本课程之前 section 的知识元或者已掌握知识元
                    if (!prevSectionsKElementUris.contains(prevKElement)
                            && (!knowledgeStateMap.containsKey(prevKElement)
                                || knowledgeStateMap.get(prevKElement) <= 0)) {
                        result = false;
                    }
                    if (!result) break;
                }
                if (!result) return false;
                else {
                    // section 可以学习，则加入 可学习 Section 对应的 KElement 数组中
                    prevSectionsKElementUris.add(kElementUri);
                }
            } else {
                // section 可以学习，则加入 可学习 Section 对应的 KElement 数组中
                prevSectionsKElementUris.add(kElementUri);
            }
        }
        return true;
    }
}
