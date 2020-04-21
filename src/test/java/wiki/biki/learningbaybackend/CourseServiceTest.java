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

    private final String uri = EntityConfig.COURSE_PREFIX + testId;

    @Test
    @Order(0)
    void getNotSuchCourse() {
        Course course = courseService.getCourseByUri(uri);
        Assertions.assertNull(course);
    }
    
    @Test
    @Order(1)
    void insertCourse() {
        Course course = new Course();
        course.setId(testId);
        course.setTitle(testTitle);
        Assertions.assertTrue(courseService.insert(course));
    }

    @Test
    @Order(2)
    void getCourseByUri() {
        Course course = courseService.getCourseByUri(uri);
        System.out.println(course.getUri());
        System.out.println(course.getTitle());
        Assertions.assertNotNull(course);
        Assertions.assertEquals(testTitle, course.getTitle());
    }

    @Test
    @Order(3)
    void updateCourse() {
        Course course = courseService.getCourseByUri(uri);
        String updateTitle = testTitle + "change";
        course.setId(testId);
        course.setTitle(updateTitle);
        Assertions.assertTrue(courseService.update(course));
        course = courseService.getCourseByUri(uri);
        Assertions.assertEquals(updateTitle, course.getTitle());
    }

    @Test
    @Order(4)
    void deleteCourse() {
        Assertions.assertTrue(courseService.delete(uri));
        Assertions.assertNull(courseService.getCourseByUri(uri));
    }

}
