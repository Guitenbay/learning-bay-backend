package wiki.biki.learningbaybackend.service.impl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.FusekiSPARQLStringBuilderFactory;
import wiki.biki.learningbaybackend.fuseki.FusekiUtils;
import wiki.biki.learningbaybackend.fuseki.PrefixConfig;
import wiki.biki.learningbaybackend.fuseki.SPARQLType;
import wiki.biki.learningbaybackend.model.UserKnowledgeState;
import wiki.biki.learningbaybackend.service.UserKnowledgeStateService;

import java.util.ArrayList;

public class UserKnowledgeStateServiceImpl implements UserKnowledgeStateService {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.KE})
    );
    /**
     *
     * @param uri: userUri
     * @return
     */
    @Override
    public ArrayList<UserKnowledgeState> getStateListByUserUri(String uri) {
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .append(" ?x ")
                .append(String.format("FROM <%s>", uri))
                .startWhere()
                .where("?x rdf:type :KElement")
                .endWhere().toString());
        return FusekiUtils.createEntityListFromModel(UserKnowledgeState.class, model);
    }

    @Override
    public int getState(String userUri, String uri) {
        if (!isExist(userUri, uri)) return -1;
        RDFNode state = LearningBayBackendApplication.fusekiApp.querySelect(factory.build().set(SPARQLType.SELECT)
                .startSelect().select("?state").endSelect()
                .append(String.format("FROM <%s>", userUri))
                .startWhere()
                .where(String.format("<%s> rdf:type :KElement;", uri))
                .where("ke:state ?state")
                .endWhere().toString()).get("state");
        return Integer.parseInt(FusekiUtils.getObjectValue(state));
    }

    /**
     *
     * @param uri: kElementUri
     * @return
     */
    @Override
    public boolean isExist(String userUri, String uri) {
        return LearningBayBackendApplication.fusekiApp.ask(factory.build().set(SPARQLType.ASK)
                .startWhere().graph(String.format("<%s>", userUri))
                .where(String.format("<%s> rdf:type :KElement", uri))
                .endWhere().toString());
    }

    private boolean insertData(String userUri, String kElementUri, int state) {
        return LearningBayBackendApplication.fusekiApp.insert(factory.build().set(SPARQLType.INSERT)
                .startInsert().graph(String.format("<%s>", userUri))
                .to(kElementUri).insertClass("rdf:type", ":KElement").insert("ke:state", state)
                .endInsert().toString());
    }

    @Override
    public boolean insertWithoutCheckingExist(String userUri, UserKnowledgeState kState) {
        // 使用时会先调用 isExist 判断，所以不用再判断
//        if (isExist(userUri, kState.getUri())) return false;
        return insertData(userUri, kState.getUri(), kState.getState());
    }

    @Override
    public boolean updateWithoutCheckingExist(String userUri, UserKnowledgeState kState) {
        boolean delete = this.deleteWithoutCheckingExist(userUri, kState.getUri());
        if (!delete) return false;
        return insertData(userUri, kState.getUri(), kState.getState());
    }

    @Override
    public boolean deleteWithoutCheckingExist(String userUri, String uri) {
        // 使用时会先调用 isExist 判断，所以不用再判断
//        if (!isExist(userUri, uri)) return false;
        return LearningBayBackendApplication.fusekiApp.delete(factory.build().set(SPARQLType.DELETE)
                .startWhere().graph(String.format("<%s>", userUri))
                .where(String.format("<%s> ?property ?object.", uri))
                .endWhere().toString());
    }


}
