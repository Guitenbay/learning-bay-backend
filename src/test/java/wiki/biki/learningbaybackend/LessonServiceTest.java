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

    private final int updateSequence = testSequence + 1;
    private final String updateTitle      = testTitle + "change";
    private final String updateMediaUri   = testMediaUri + "change";
    private final String updateCQUri      = testCQUri + "change";
    private final String updateChapterUri = testChapterUri + "change";

    private final String uri = EntityConfig.LESSON_PREFIX + testId;

    private Lesson testLesson;
    private Lesson updateLesson;
    {
        testLesson = new Lesson();
        testLesson.setId(testId);
        testLesson.setSequence(testSequence);
        testLesson.setTitle(testTitle);
        testLesson.setMediaUri(testMediaUri);
        testLesson.setCodeQuestionUri(testCQUri);
        testLesson.setChapterUri(testChapterUri);

        updateLesson = new Lesson();
        updateLesson.setId(testId);
        updateLesson.setSequence(updateSequence);
        updateLesson.setTitle(updateTitle);
        updateLesson.setMediaUri(updateMediaUri);
        updateLesson.setCodeQuestionUri(updateCQUri);
        updateLesson.setChapterUri(updateChapterUri);
    }
    @Test
    @Order(0)
    void getNoSuchLesson() {
        Lesson lesson = lessonService.getLessonByUri(uri);
        Assertions.assertNull(lesson);
    }

    @Test
    @Order(1)
    void updateNoSuchLesson() {
        Assertions.assertFalse(lessonService.update(updateLesson));
    }

    @Test
    @Order(2)
    void deleteNoSuchLesson() {
        Assertions.assertFalse(lessonService.delete(uri));
    }

    @Test
    @Order(3)
    void insertLesson() {
        Assertions.assertTrue(lessonService.insert(testLesson));
    }

    @Test
    @Order(4)
    void insertDuplicateLesson() {
        Assertions.assertFalse(lessonService.insert(testLesson));
    }

    @Test
    @Order(5)
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
    @Order(6)
    void updateLesson() {
        Assertions.assertTrue(lessonService.update(updateLesson));
        Lesson lesson = lessonService.getLessonByUri(uri);
        Assertions.assertEquals(updateSequence, lesson.getSequence());
        Assertions.assertEquals(updateTitle, lesson.getTitle());
        Assertions.assertEquals(updateMediaUri, lesson.getMediaUri());
        Assertions.assertEquals(updateCQUri, lesson.getCodeQuestionUri());
        Assertions.assertEquals(updateChapterUri, lesson.getChapterUri());
    }

    @Test
    @Order(7)
    void deleteLesson() {
        Assertions.assertTrue(lessonService.delete(uri));
        Assertions.assertNull(lessonService.getLessonByUri(uri));
    }

}
