package wiki.biki.learningbaybackend.service.impl;

import org.apache.jena.rdf.model.Model;
import wiki.biki.learningbaybackend.LearningBayBackendApplication;
import wiki.biki.learningbaybackend.fuseki.*;
import wiki.biki.learningbaybackend.model.Course;
import wiki.biki.learningbaybackend.service.CourseService;

import java.util.ArrayList;

public class CourseServiceImpl implements CourseService {
    private FusekiSPARQLStringBuilderFactory factory = new FusekiSPARQLStringBuilderFactory(
            PrefixConfig.getPrefix(new String[]{PrefixConfig.COURSE})
    );
    @Override
    public ArrayList<Course> getCourseList() {
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .append(" ?x ").startWhere().where("?x rdf:type :Course").endWhere().toString());
        return FusekiUtils.createEntityListFromModel(Course.class, model);
    }

    @Override
    public Course getCourseByUri(String uri) {
        Course course = null;
        Model model = LearningBayBackendApplication.fusekiApp.queryDescribe(factory.build().set(SPARQLType.DESCRIBE)
                .describe(uri).toString());
        try {
            course = FusekiUtils.createEntityFromModel(Course.class, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public boolean isExist(String uri) {
        return LearningBayBackendApplication.fusekiApp.ask(factory.build().set(SPARQLType.ASK)
                .startWhere()
                .where(String.format("<%s> rdf:type :Course.", uri))
                .endWhere().toString());
    }

    private boolean insertData(String uri, String title, ArrayList<String> codeQuestionUris, ArrayList<String> chapterUris) {
        FusekiSPARQLStringBuilder courseBuilder = factory.build().set(SPARQLType.INSERT)
                .startInsert()
                .to(uri).insertClass("rdf:type", ":Course")
                .insert("course:title", title);
        for (String codeQuestionUri: codeQuestionUris) {
            courseBuilder = courseBuilder.insert("course:contains", codeQuestionUri);
        }
        for (String chapterUri: chapterUris) {
            courseBuilder = courseBuilder.insert("course:hasChapter", chapterUri);
        }
        return LearningBayBackendApplication.fusekiApp.insert(courseBuilder
                .endInsert().toString());
    }

    @Override
    public boolean insert(Course course) {
        String uri = EntityConfig.COURSE_PREFIX + course.getId();
        if (isExist(uri)) return false;
        return insertData(uri, course.getTitle(), course.getCodeQuestionUris(), course.getChapterUris());
    }

    @Override
    public boolean update(Course course) {
        String uri = EntityConfig.COURSE_PREFIX + course.getId();
        boolean delete = this.delete(uri);
        if (!delete) return false;
        return insertData(uri, course.getTitle(), course.getCodeQuestionUris(), course.getChapterUris());
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
