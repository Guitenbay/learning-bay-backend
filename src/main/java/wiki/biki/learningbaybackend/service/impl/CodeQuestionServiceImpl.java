package wiki.biki.learningbaybackend.service.impl;

import org.apache.jena.rdf.model.Model;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.CodeQuestion;
import wiki.biki.learningbaybackend.service.CodeQuestionService;

import java.util.ArrayList;

public class CodeQuestionServiceImpl implements CodeQuestionService {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.RESOURCE, PrefixConfig.CQ})
    );
    @Override
    public ArrayList<CodeQuestion> getCodeQuestionList() {
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .append(" ?x ").startWhere().where("?x rdf:type :CodeQuestion").endWhere().toString());
        return FusekiUtils.createEntityListFromModel(CodeQuestion.class, model);
    }

    @Override
    public CodeQuestion getCodeQuestionByUri(String uri) {
        CodeQuestion codeQuestion = null;
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .describe(uri).toString());
        try {
            codeQuestion = FusekiUtils.createEntityFromModel(CodeQuestion.class, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codeQuestion;
    }

    @Override
    public boolean isExist(String uri) {
        return LearningBayBackendApplication.fusekiApp.ask(factory.build().set(SPARQLType.ASK)
                .startWhere()
                .where(String.format("<%s> rdf:type :CodeQuestion.", uri))
                .endWhere().toString());
    }

    private boolean insertData(String uri, String title, String date, String creator,
                               String code, String content, String testSetFilename,
                               ArrayList<String> kElementUris) {
        FusekiSPARQLStringBuilder chapterBuilder = factory.build().set(SPARQLType.INSERT)
                .startInsert()
                .to(uri).insertClass("rdf:type", ":CodeQuestion")
                .insert("resource:title", title).insert("resource:date", date)
                .insert("resource:creator", creator).insert("cq:code", code)
                .insert("cq:content", content).insert("cq:testSetFilename", testSetFilename);
        for (String kElementUri: kElementUris) {
            chapterBuilder = chapterBuilder.insert("cq:hasKElement", kElementUri);
        }
        return LearningBayBackendApplication.fusekiApp.insert(chapterBuilder
                .endInsert().toString());
    }

    @Override
    public boolean insert(CodeQuestion codeQuestion) {
        String uri = EntityConfig.CQ_PREFIX + codeQuestion.getId();
        if (isExist(uri)) return false;
        return insertData(uri, codeQuestion.getTitle(), codeQuestion.getDate(), codeQuestion.getCreator(),
                codeQuestion.getCode(), codeQuestion.getContent(), codeQuestion.getTestSetFilename(),
                codeQuestion.getkElementUris());
    }

    @Override
    public boolean update(CodeQuestion codeQuestion) {
        String uri = EntityConfig.CQ_PREFIX + codeQuestion.getId();
        boolean delete = this.delete(uri);
        if (!delete) return false;
        return insertData(uri, codeQuestion.getTitle(), codeQuestion.getDate(), codeQuestion.getCreator(),
                codeQuestion.getCode(), codeQuestion.getContent(), codeQuestion.getTestSetFilename(),
                codeQuestion.getkElementUris());
    }

    @Override
    public boolean delete(String uri) {
        if (!isExist(uri)) return false;
        else return LearningBayBackendApplication.fusekiApp.delete(factory.build().set(SPARQLType.DELETE)
                .startWhere()
                .where(String.format("<%s> ?property ?object.", uri))
                .endWhere().toString());
    }
}
