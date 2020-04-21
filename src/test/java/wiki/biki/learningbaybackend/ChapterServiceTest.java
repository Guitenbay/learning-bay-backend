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

    private final String uri            = EntityConfig.CHAPTER_PREFIX + testId;

    @Test
    @Order(0)
    void getNotSuchChapter() {
        Chapter chapter = chapterService.getChapterByUri(uri);
        Assertions.assertNull(chapter);
    }

    @Test
    @Order(1)
    void insertChapter() {
        Chapter chapter = new Chapter();
        chapter.setId(testId);
        chapter.setSequence(testSequence);
        chapter.setTitle(testTitle);
        chapter.setCourseUri(testCourseUri);
        Assertions.assertTrue(chapterService.insert(chapter));
    }

    @Test
    @Order(2)
    void getChapterByUri() {
        Chapter chapter = chapterService.getChapterByUri(uri);
        Assertions.assertNotNull(chapter);
        Assertions.assertEquals(testSequence, chapter.getSequence());
        Assertions.assertEquals(testTitle, chapter.getTitle());
        Assertions.assertEquals(testCourseUri, chapter.getCourseUri());
    }

    @Test
    @Order(3)
    void updateChapter() {
        Chapter chapter = chapterService.getChapterByUri(uri);
        int updateSequence = testSequence + 1;
        String updateTitle = testTitle + "change";
        String updateCourseUri = testCourseUri + "change";
        chapter.setId(testId);
        chapter.setSequence(updateSequence);
        chapter.setTitle(updateTitle);
        chapter.setCourseUri(updateCourseUri);
        Assertions.assertTrue(chapterService.update(chapter));
        chapter = chapterService.getChapterByUri(uri);
        Assertions.assertEquals(updateSequence, chapter.getSequence());
        Assertions.assertEquals(updateTitle, chapter.getTitle());
        Assertions.assertEquals(updateCourseUri, chapter.getCourseUri());
    }

    @Test
    @Order(4)
    void deleteChapter() {
        Assertions.assertTrue(chapterService.delete(uri));
        Assertions.assertNull(chapterService.getChapterByUri(uri));
    }

}
