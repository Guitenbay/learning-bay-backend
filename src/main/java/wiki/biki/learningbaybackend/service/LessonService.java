package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.Lesson;

import java.util.ArrayList;

public interface LessonService {
    ArrayList<Lesson> getLessonList();
    Lesson getLessonByUri(String uri);
    boolean isExist(String uri);
    boolean insert(Lesson lesson);
    boolean update(Lesson lesson);
    boolean delete(String uri);
}
