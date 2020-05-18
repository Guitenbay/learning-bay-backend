package wiki.biki.learningbaybackend.web;

import com.alibaba.fastjson.JSONObject;
import org.apache.jena.rdf.model.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.KElement;
import wiki.biki.learningbaybackend.service.UserKnowledgeStateService;
import wiki.biki.learningbaybackend.service.impl.UserKnowledgeStateServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fuseki/user/knowledge/graph")
public class KnowledgeStateGraphController {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.KE, PrefixConfig.COURSE, PrefixConfig.CHAPTER, PrefixConfig.LESSON, PrefixConfig.SECTION})
    );
    private UserKnowledgeStateService userKnowledgeStateService = new UserKnowledgeStateServiceImpl();
    @GetMapping
    public String getUserKnowledgeStatesAsGraph(String userUri, String courseUri) {
        Model kElementModel = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .append(" ?x ")
                .startWhere()
                .where("?section section:correspondKE ?x;").where("rdf:type :Section;").where("section:belongs ?lesson.")
                .where("?lesson rdf:type :Lesson;").where("lesson:belongs ?chapter.")
                .where("?chapter rdf:type :Chapter;").where(String.format("chapter:belongs <%s>.", courseUri))
                .graph(GraphConfig.KE_GRAPH).where("?x rdf:type :KElement")
                .endWhere().toString());
        ArrayList<KElement> kElements = FusekiUtils.createEntityListFromModel(KElement.class, kElementModel);
        ArrayList<Map<String, String>> nodes = new ArrayList<>();
        ArrayList<Map<String, String>> edges = new ArrayList<>();
        kElements.forEach(kElement -> {
            Map<String, String> node = new HashMap<>();
            node.put("id", kElement.getUri());
//            System.out.println(userKnowledgeStateService.getState(userUri, kElement.getUri()));
            if (userKnowledgeStateService.isExist(userUri, kElement.getUri())) {
                int state = userKnowledgeStateService.getState(userUri, kElement.getUri());
                node.put("label", kElement.getName() + ':' + state);
                if (state > 0) node.put("cluster", "grasp" + ':' + state);
            } else {
                node.put("label", kElement.getName());
            }
            if (!node.containsKey("cluster")) node.put("cluster", "none");
            ArrayList<String> prevList = kElement.getPreviousList();
            if (prevList != null) {
                for (String prev : prevList) {
                    Map<String, String> edge = new HashMap<>();
                    edge.put("source", prev);
                    edge.put("target", kElement.getUri());
                    edges.add(edge);
                }
            }
            nodes.add(node);
        });
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("nodes", nodes);
        data.put("edges", edges);
        json.put("res", true);
        json.put("data", data);
        return json.toJSONString();
    }

}
