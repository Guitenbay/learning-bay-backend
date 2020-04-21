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

    private final int updateSequence      = testSequence + 1;
    private final String updateTitle      = testTitle + "change";
    private final String updateContent    = testContent + "change";
    private final String updateKElementUri= testKElementUri + "change";
    private final String updateLessonUri  = testLessonUri + "change";

    private final String uri = EntityConfig.SECTION_PREFIX + testId;

    private Section testSection;
    private Section updateSection;
    {
        testSection = new Section();
        testSection.setId(testId);
        testSection.setSequence(testSequence);
        testSection.setTitle(testTitle);
        testSection.setContent(testContent);
        testSection.setkElementUri(testKElementUri);
        testSection.setLessonUri(testLessonUri);

        updateSection = new Section();
        updateSection.setId(testId);
        updateSection.setSequence(updateSequence);
        updateSection.setTitle(updateTitle);
        updateSection.setContent(updateContent);
        updateSection.setkElementUri(updateKElementUri);
        updateSection.setLessonUri(updateLessonUri);
    }
    @Test
    @Order(0)
    void getNoSuchSection() {
        Section section = sectionService.getSectionByUri(uri);
        Assertions.assertNull(section);
    }

    @Test
    @Order(1)
    void updateNoSuchSection() {
        Assertions.assertFalse(sectionService.update(updateSection));
    }

    @Test
    @Order(2)
    void deleteNoSuchSection() {
        Assertions.assertFalse(sectionService.delete(uri));
    }
    
    @Test
    @Order(3)
    void insertSection() {
        Assertions.assertTrue(sectionService.insert(testSection));
    }

    @Test
    @Order(4)
    void insertDuplicateSection() {
        Assertions.assertFalse(sectionService.insert(testSection));
    }

    @Test
    @Order(5)
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
    @Order(6)
    void updateSection() {
        Assertions.assertTrue(sectionService.update(updateSection));
        Section section = sectionService.getSectionByUri(uri);
        Assertions.assertEquals(updateSequence, section.getSequence());
        Assertions.assertEquals(updateTitle, section.getTitle());
        Assertions.assertEquals(updateContent, section.getContent());
        Assertions.assertEquals(updateKElementUri, section.getkElementUri());
        Assertions.assertEquals(updateLessonUri, section.getLessonUri());
    }

    @Test
    @Order(7)
    void deleteSection() {
        Assertions.assertTrue(sectionService.delete(uri));
        Assertions.assertNull(sectionService.getSectionByUri(uri));
    }

}
