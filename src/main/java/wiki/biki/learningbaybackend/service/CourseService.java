package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.Course;

import java.util.ArrayList;
import java.util.Map;

public interface CourseService {
    ArrayList<Course> getCourseList();
    ArrayList<Map<String, String>> getCourseEntities();
    Course getCourseByUri(String uri);
    boolean isExist(String uri);
    boolean insert(Course course);
    boolean update(Course course);
    boolean delete(String uri);
}
