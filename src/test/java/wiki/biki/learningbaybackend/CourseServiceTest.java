package wiki.biki.learningbaybackend;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import wiki.biki.learningbaybackend.fuseki.EntityConfig;
import wiki.biki.learningbaybackend.model.Course;
import wiki.biki.learningbaybackend.service.CourseService;
import wiki.biki.learningbaybackend.service.impl.CourseServiceImpl;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourseServiceTest {
    private CourseService courseService = new CourseServiceImpl();

    private final String testId     = "testCourseId";
    private final String testTitle  = "test course";

    private final String updateTitle = testTitle + "change";

    private final String uri = EntityConfig.COURSE_PREFIX + testId;

    private Course testCourse;
    private Course updateCourse;
    {
        testCourse = new Course();
        testCourse.setId(testId);
        testCourse.setTitle(testTitle);

        updateCourse = new Course();
        updateCourse.setId(testId);
        updateCourse.setTitle(updateTitle);
    }
    @Test
    @Order(0)
    void getNoSuchCourse() {
        Course course = courseService.getCourseByUri(uri);
        Assertions.assertNull(course);
    }

    @Test
    @Order(1)
    void updateNoSuchCourse() {
        Assertions.assertFalse(courseService.update(updateCourse));
    }

    @Test
    @Order(2)
    void deleteNoSuchCourse() {
        Assertions.assertFalse(courseService.delete(uri));
    }
    
    @Test
    @Order(3)
    void insertCourse() {
        Assertions.assertTrue(courseService.insert(testCourse));
    }

    @Test
    @Order(4)
    void insertDuplicateCourse() {
        Assertions.assertFalse(courseService.insert(testCourse));
    }

    @Test
    @Order(5)
    void getCourseByUri() {
        Course course = courseService.getCourseByUri(uri);
        Assertions.assertNotNull(course);
        Assertions.assertEquals(testTitle, course.getTitle());
    }

    @Test
    @Order(6)
    void updateCourse() {
        Assertions.assertTrue(courseService.update(updateCourse));
        Course course = courseService.getCourseByUri(uri);
        Assertions.assertEquals(updateTitle, course.getTitle());
    }

    @Test
    @Order(7)
    void deleteCourse() {
        Assertions.assertTrue(courseService.delete(uri));
        Assertions.assertNull(courseService.getCourseByUri(uri));
    }

}
