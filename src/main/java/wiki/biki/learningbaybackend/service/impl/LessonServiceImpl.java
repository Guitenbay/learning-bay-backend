package wiki.biki.learningbaybackend.service.impl;

import org.apache.jena.rdf.model.Model;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.Lesson;
import wiki.biki.learningbaybackend.service.LessonService;

import java.util.ArrayList;

public class LessonServiceImpl implements LessonService {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.LESSON})
    );
    @Override
    public ArrayList<Lesson> getLessonList() {
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .append(" ?x ").startWhere().where("?x rdf:type :Lesson").endWhere().toString());
        return FusekiUtils.createEntityListFromModel(Lesson.class, model);
    }

    @Override
    public Lesson getLessonByUri(String uri) {
        Lesson lesson = null;
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .describe(uri).toString());
        try {
            lesson = FusekiUtils.createEntityFromModel(Lesson.class, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lesson;
    }

    @Override
    public boolean isExist(String uri) {
        return LearningBayBackendApplication.fusekiApp.ask(factory.build().set(SPARQLType.ASK)
                .startWhere()
                .where(String.format("<%s> rdf:type :Lesson.", uri))
                .endWhere().toString());
    }

    private boolean insertData(String uri, String title, String mediaUri, ArrayList<String> sectionUris) {
        FusekiSPARQLStringBuilder lessonBuilder = factory.build().set(SPARQLType.INSERT)
                .startInsert()
                .to(uri).insertClass("rdf:type", ":Lesson")
                .insert("lesson:title", title).insert("lesson:hasMedia", mediaUri);
        for (String sectionUri: sectionUris) {
            lessonBuilder = lessonBuilder.insert("lesson:hasSection", sectionUri);
        }
        return LearningBayBackendApplication.fusekiApp.insert(lessonBuilder
                .endInsert().toString());
    }

    @Override
    public boolean insert(Lesson lesson) {
        String uri = EntityConfig.LESSON_PREFIX + lesson.getId();
        if (isExist(uri)) return false;
        return insertData(uri, lesson.getTitle(), lesson.getMediaUri(), lesson.getSectionUris());
    }

    @Override
    public boolean update(Lesson lesson) {
        String uri = EntityConfig.LESSON_PREFIX + lesson.getId();
        boolean delete = this.delete(uri);
        if (!delete) return false;
        return insertData(uri, lesson.getTitle(), lesson.getMediaUri(), lesson.getSectionUris());
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
