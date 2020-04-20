package wiki.biki.learningbaybackend;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import wiki.biki.learningbaybackend.fuseki.EntityConfig;
import wiki.biki.learningbaybackend.model.Section;
import wiki.biki.learningbaybackend.service.SectionService;
import wiki.biki.learningbaybackend.service.impl.SectionServiceImpl;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SectionServiceTest {
    private SectionService sectionService = new SectionServiceImpl();

    private final String testId         = "testSectionId";
    private final int testSequence      = 1;
    private final String testTitle      = "test section";
    private final String testContent    = "test content";
    private final String testKElementUri= EntityConfig.K_ELEMENT_PREFIX + "KElementId";
    private final String testLessonUri  = EntityConfig.LESSON_PREFIX + "testLessonUri";

    private final String uri = EntityConfig.SECTION_PREFIX + testId;
    
    @Test
    @Order(1)
    void insertSection() {
        Section section = new Section();
        section.setId(testId);
        section.setSequence(testSequence);
        section.setTitle(testTitle);
        section.setContent(testContent);
        section.setkElementUri(testKElementUri);
        section.setLessonUri(testLessonUri);
        Assertions.assertTrue(sectionService.insert(section));
    }

    @Test
    @Order(2)
    void getSectionByUri() {
        Section section = sectionService.getSectionByUri(uri);
        Assertions.assertNotNull(section);
        Assertions.assertEquals(testSequence, section.getSequence());
        Assertions.assertEquals(testTitle, section.getTitle());
        Assertions.assertEquals(testContent, section.getContent());
        Assertions.assertEquals(testKElementUri, section.getkElementUri());
        Assertions.assertEquals(testLessonUri, section.getLessonUri());
    }

    @Test
    @Order(3)
    void updateSection() {
        Section section = sectionService.getSectionByUri(uri);
        int updateSequence      = testSequence + 1;
        String updateTitle      = testTitle + "change";
        String updateContent    = testContent + "change";
        String updateKElementUri= testKElementUri + "change";
        String updateLessonUri  = testLessonUri + "change";
        section.setId(testId);
        section.setSequence(updateSequence);
        section.setTitle(updateTitle);
        section.setContent(updateContent);
        section.setkElementUri(updateKElementUri);
        section.setLessonUri(updateLessonUri);
        Assertions.assertTrue(sectionService.update(section));
        section = sectionService.getSectionByUri(uri);
        Assertions.assertEquals(updateSequence, section.getSequence());
        Assertions.assertEquals(updateTitle, section.getTitle());
        Assertions.assertEquals(updateContent, section.getContent());
        Assertions.assertEquals(updateKElementUri, section.getkElementUri());
        Assertions.assertEquals(updateLessonUri, section.getLessonUri());
    }

    @Test
    @Order(4)
    void deleteSection() {
        Assertions.assertTrue(sectionService.delete(uri));
        Assertions.assertNull(sectionService.getSectionByUri(uri));
    }

}
