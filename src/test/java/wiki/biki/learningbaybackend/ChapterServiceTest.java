package wiki.biki.learningbaybackend;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import wiki.biki.learningbaybackend.fuseki.EntityConfig;
import wiki.biki.learningbaybackend.model.Chapter;
import wiki.biki.learningbaybackend.service.ChapterService;
import wiki.biki.learningbaybackend.service.impl.ChapterServiceImpl;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChapterServiceTest {
    private ChapterService chapterService = new ChapterServiceImpl();

    private final String testId         = "testChapterId";
    private final int testSequence      = 1;
    private final String testTitle      = "test chapter";
    private final String testCourseUri  = EntityConfig.COURSE_PREFIX + "testCourseUri";

    private final int updateSequence = testSequence + 1;
    private final String updateTitle = testTitle + "change";
    private final String updateCourseUri = testCourseUri + "change";

    private final String uri            = EntityConfig.CHAPTER_PREFIX + testId;

    private Chapter testChapter;
    private Chapter updateChapter;
    {
        testChapter = new Chapter();
        testChapter.setId(testId);
        testChapter.setSequence(testSequence);
        testChapter.setTitle(testTitle);
        testChapter.setCourseUri(testCourseUri);

        updateChapter = new Chapter();
        updateChapter.setId(testId);
        updateChapter.setSequence(updateSequence);
        updateChapter.setTitle(updateTitle);
        updateChapter.setCourseUri(updateCourseUri);
    }

    @Test
    @Order(0)
    void getNoSuchChapter() {
        Chapter chapter = chapterService.getChapterByUri(uri);
        Assertions.assertNull(chapter);
    }

    @Test
    @Order(1)
    void updateNoSuchChapter() {
        Assertions.assertFalse(chapterService.update(updateChapter));
    }

    @Test
    @Order(2)
    void deleteNoSuchChapter() {
        Assertions.assertFalse(chapterService.delete(uri));
    }

    @Test
    @Order(3)
    void insertChapter() {
        Assertions.assertTrue(chapterService.insert(testChapter));
    }

    @Test
    @Order(4)
    void insertDuplicateChapter() {
        Assertions.assertFalse(chapterService.insert(testChapter));
    }

    @Test
    @Order(5)
    void getChapterByUri() {
        Chapter chapter = chapterService.getChapterByUri(uri);
        Assertions.assertNotNull(chapter);
        Assertions.assertEquals(testSequence, chapter.getSequence());
        Assertions.assertEquals(testTitle, chapter.getTitle());
        Assertions.assertEquals(testCourseUri, chapter.getCourseUri());
    }

    @Test
    @Order(6)
    void updateChapter() {
        Assertions.assertTrue(chapterService.update(updateChapter));
        Chapter chapter = chapterService.getChapterByUri(uri);
        Assertions.assertEquals(updateSequence, chapter.getSequence());
        Assertions.assertEquals(updateTitle, chapter.getTitle());
        Assertions.assertEquals(updateCourseUri, chapter.getCourseUri());
    }

    @Test
    @Order(7)
    void deleteChapter() {
        Assertions.assertTrue(chapterService.delete(uri));
        Assertions.assertNull(chapterService.getChapterByUri(uri));
    }

}
