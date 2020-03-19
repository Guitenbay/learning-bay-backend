package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.Chapter;

import java.util.ArrayList;

public interface ChapterService {
    ArrayList<Chapter> getChapterList();
    ArrayList<Chapter> getChapterListByCourseUri(String uri);
    Chapter getChapterByUri(String uri);
    boolean isExist(String uri);
    boolean insert(Chapter chapter);
    boolean update(Chapter chapter);
    boolean delete(String uri);
}
