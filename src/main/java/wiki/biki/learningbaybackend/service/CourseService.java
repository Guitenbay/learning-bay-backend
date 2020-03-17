package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.Course;

import java.util.ArrayList;

public interface CourseService {
    ArrayList<Course> getCourseList();
    Course getCourseByUri(String uri);
    boolean isExist(String uri);
    boolean insert(Course course);
    boolean update(Course course);
    boolean delete(String uri);
}
