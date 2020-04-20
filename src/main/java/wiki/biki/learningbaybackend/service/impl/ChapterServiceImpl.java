package wiki.biki.learningbaybackend.service.impl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.Chapter;
import wiki.biki.learningbaybackend.service.ChapterService;

import java.util.ArrayList;
import java.util.Map;

public class ChapterServiceImpl implements ChapterService {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.CHAPTER})
    );
    @Override
    public ArrayList<Chapter> getChapterList() {
//        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
//                .append(" ?x ").startWhere().where("?x rdf:type :Chapter").endWhere().toString());
//        return FusekiUtils.createEntityListFromModel(Chapter.class, model);
        Map<String, ArrayList<RDFNode>> map = LearningBayBackendApplication.fusekiApp.querySelectList(factory.build().set(SPARQLType.SELECT)
                .startSelect().select("?uri").select("?title").select("?sequence").endSelect()
                .startWhere().where("?uri rdf:type :Chapter;").where("chapter:title ?title;").where("chapter:sequence ?sequence.").endWhere().toString());
        return FusekiUtils.createEntityListBySelectMap(Chapter.class, map);
    }

    @Override
    public ArrayList<Chapter> getChapterListByCourseUri(String uri) {
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .append(" ?x ").startWhere().where("?x rdf:type :Chapter;").where(String.format("chapter:belongs <%s>.", uri)).endWhere().toString());
        return FusekiUtils.createEntityListFromModel(Chapter.class, model);
    }

    @Override
    public Chapter getChapterByUri(String uri) {
        Chapter chapter = null;
        if (!isExist(uri)) return null;
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .describe(uri).toString());
        try {
            chapter = FusekiUtils.createEntityFromModel(Chapter.class, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chapter;
    }

    @Override
    public boolean isExist(String uri) {
        return LearningBayBackendApplication.fusekiApp.ask(factory.build().set(SPARQLType.ASK)
                .startWhere()
                .where(String.format("<%s> rdf:type :Chapter.", uri))
                .endWhere().toString());
    }

    private boolean insertData(String uri, int sequence, String title, String courseUri) {
        FusekiSPARQLStringBuilder chapterBuilder = factory.build().set(SPARQLType.INSERT)
                .startInsert()
                .to(uri).insertClass("rdf:type", ":Chapter")
                .insert("chapter:sequence", sequence)
                .insert("chapter:title", title).insertUri("chapter:belongs", courseUri);
        return LearningBayBackendApplication.fusekiApp.insert(chapterBuilder
                .endInsert().toString());
    }

    @Override
    public boolean insert(Chapter chapter) {
        String uri = EntityConfig.CHAPTER_PREFIX + chapter.getId();
        if (isExist(uri)) return false;
        return insertData(uri, chapter.getSequence(), chapter.getTitle(), chapter.getCourseUri());
    }

    @Override
    public boolean update(Chapter chapter) {
        String uri = EntityConfig.CHAPTER_PREFIX + chapter.getId();
        boolean delete = this.delete(uri);
        if (!delete) return false;
        return insertData(uri, chapter.getSequence(), chapter.getTitle(), chapter.getCourseUri());
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
