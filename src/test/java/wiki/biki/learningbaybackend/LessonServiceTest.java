package wiki.biki.learningbaybackend;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import wiki.biki.learningbaybackend.fuseki.EntityConfig;
import wiki.biki.learningbaybackend.model.Lesson;
import wiki.biki.learningbaybackend.service.LessonService;
import wiki.biki.learningbaybackend.service.impl.LessonServiceImpl;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LessonServiceTest {
    private LessonService lessonService = new LessonServiceImpl();

    private final String testId         = "testLessonId";
    private final int testSequence      = 1;
    private final String testTitle      = "test lesson";
    private final String testMediaUri   = EntityConfig.MM_PREFIX + "testMediaUri";
    private final String testCQUri      = EntityConfig.CQ_PREFIX + "testCQId";
    private final String testChapterUri = EntityConfig.CHAPTER_PREFIX + "testChapterUri";

    private final String uri = EntityConfig.LESSON_PREFIX + testId;

    @Test
    @Order(0)
    void getNotSuchLesson() {
        Lesson lesson = lessonService.getLessonByUri(uri);
        Assertions.assertNull(lesson);
    }

    @Test
    @Order(1)
    void insertLesson() {
        Lesson lesson = new Lesson();
        lesson.setId(testId);
        lesson.setSequence(testSequence);
        lesson.setTitle(testTitle);
        lesson.setMediaUri(testMediaUri);
        lesson.setCodeQuestionUri(testCQUri);
        lesson.setChapterUri(testChapterUri);
        Assertions.assertTrue(lessonService.insert(lesson));
    }

    @Test
    @Order(2)
    void getLessonByUri() {
        Lesson lesson = lessonService.getLessonByUri(uri);
        Assertions.assertNotNull(lesson);
        Assertions.assertEquals(testSequence, lesson.getSequence());
        Assertions.assertEquals(testTitle, lesson.getTitle());
        Assertions.assertEquals(testMediaUri, lesson.getMediaUri());
        Assertions.assertEquals(testCQUri, lesson.getCodeQuestionUri());
        Assertions.assertEquals(testChapterUri, lesson.getChapterUri());
    }

    @Test
    @Order(3)
    void updateLesson() {
        Lesson lesson = lessonService.getLessonByUri(uri);
        int updateSequence = testSequence + 1;
        String updateTitle      = testTitle + "change";
        String updateMediaUri   = testMediaUri + "change";
        String updateCQUri      = testCQUri + "change";
        String updateChapterUri = testChapterUri + "change";
        lesson.setId(testId);
        lesson.setSequence(updateSequence);
        lesson.setTitle(updateTitle);
        lesson.setMediaUri(updateMediaUri);
        lesson.setCodeQuestionUri(updateCQUri);
        lesson.setChapterUri(updateChapterUri);
        Assertions.assertTrue(lessonService.update(lesson));
        lesson = lessonService.getLessonByUri(uri);
        Assertions.assertEquals(updateSequence, lesson.getSequence());
        Assertions.assertEquals(updateTitle, lesson.getTitle());
        Assertions.assertEquals(updateMediaUri, lesson.getMediaUri());
        Assertions.assertEquals(updateCQUri, lesson.getCodeQuestionUri());
        Assertions.assertEquals(updateChapterUri, lesson.getChapterUri());
    }

    @Test
    @Order(4)
    void deleteLesson() {
        Assertions.assertTrue(lessonService.delete(uri));
        Assertions.assertNull(lessonService.getLessonByUri(uri));
    }

}
