package wiki.biki.learningbaybackend;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import wiki.biki.learningbaybackend.fuseki.EntityConfig;
import wiki.biki.learningbaybackend.model.CodeQuestion;
import wiki.biki.learningbaybackend.service.CodeQuestionService;
import wiki.biki.learningbaybackend.service.impl.CodeQuestionServiceImpl;

import java.util.ArrayList;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CodeQuestionServiceTest {
    private CodeQuestionService codeQuestionService = new CodeQuestionServiceImpl();

    private final String testId         = "testCQId";
    private final String testDate       = "2020-4-20";
    private final String testTitle      = "test code question";
    private final String testCreator    = "tester";
    private final String testCode       = "class A {}";
    private final String testContent    = "code content";
    private final String testSetFilename= "test.txt";
    private final ArrayList<String> testKElementUris = new ArrayList<>();
    private final String testCourseUri  = EntityConfig.COURSE_PREFIX + "testCourseUri";

    private final String updateDate       = testDate + "change";
    private final String updateTitle      = testTitle + "change";
    private final String updateCreator    = testCreator + "change";
    private final String updateCode       = testCode + "change";
    private final String updateContent    = testContent + "change";
    private final String updateSetFilename= testSetFilename + "change";
    private final String updateCourseUri  = testCourseUri + "change";
    private final ArrayList<String> updateKElementUris = new ArrayList<>();

    private final String uri            = EntityConfig.CQ_PREFIX + testId;

    private CodeQuestion testCodeQuestion;
    private CodeQuestion updateCodeQuestion;
    {
        testCodeQuestion = new CodeQuestion();
        testCodeQuestion.setId(testId);
        testCodeQuestion.setDate(testDate);
        testCodeQuestion.setTitle(testTitle);
        testCodeQuestion.setCreator(testCreator);
        testCodeQuestion.setCode(testCode);
        testCodeQuestion.setContent(testContent);
        testCodeQuestion.setTestSetFilename(testSetFilename);
        testCodeQuestion.setkElementUris(testKElementUris);
        testCodeQuestion.setCourseUri(testCourseUri);

        updateKElementUris.add(EntityConfig.K_ELEMENT_PREFIX + "kElementId");
        updateCodeQuestion = new CodeQuestion();
        updateCodeQuestion.setId(testId);
        updateCodeQuestion.setDate(updateDate);
        updateCodeQuestion.setTitle(updateTitle);
        updateCodeQuestion.setCreator(updateCreator);
        updateCodeQuestion.setCode(updateCode);
        updateCodeQuestion.setContent(updateContent);
        updateCodeQuestion.setTestSetFilename(updateSetFilename);
        updateCodeQuestion.setkElementUris(updateKElementUris);
        updateCodeQuestion.setCourseUri(updateCourseUri);
    }
    @Test
    @Order(0)
    void getNoSuchCodeQuestion() {
        CodeQuestion codeQuestion = codeQuestionService.getCodeQuestionByUri(uri);
        Assertions.assertNull(codeQuestion);
    }

    @Test
    @Order(1)
    void updateNoSuchCodeQuestion() {
        Assertions.assertFalse(codeQuestionService.update(updateCodeQuestion));
    }

    @Test
    @Order(2)
    void deleteNoSuchCodeQuestion() {
        Assertions.assertFalse(codeQuestionService.delete(uri));
    }

    @Test
    @Order(3)
    void insertCodeQuestion() {
        Assertions.assertTrue(codeQuestionService.insert(testCodeQuestion));
    }

    @Test
    @Order(4)
    void insertDuplicateChapter() {
        Assertions.assertFalse(codeQuestionService.insert(testCodeQuestion));
    }

    @Test
    @Order(5)
    void getCodeQuestionByUri() {
        CodeQuestion codeQuestion = codeQuestionService.getCodeQuestionByUri(uri);
        Assertions.assertNotNull(codeQuestion);
        Assertions.assertEquals(testDate, codeQuestion.getDate());
        Assertions.assertEquals(testTitle, codeQuestion.getTitle());
        Assertions.assertEquals(testCreator, codeQuestion.getCreator());
        Assertions.assertEquals(testCode, codeQuestion.getCode());
        Assertions.assertEquals(testContent, codeQuestion.getContent());
        Assertions.assertEquals(testSetFilename, codeQuestion.getTestSetFilename());
        // 传入为 [], 取出为 null
        Assertions.assertNull(codeQuestion.getkElementUris());
        Assertions.assertEquals(testCourseUri, codeQuestion.getCourseUri());
    }

    @Test
    @Order(6)
    void updateCodeQuestion() {
        Assertions.assertTrue(codeQuestionService.update(updateCodeQuestion));
        CodeQuestion codeQuestion = codeQuestionService.getCodeQuestionByUri(uri);
        Assertions.assertEquals(updateDate, codeQuestion.getDate());
        Assertions.assertEquals(updateTitle, codeQuestion.getTitle());
        Assertions.assertEquals(updateCreator, codeQuestion.getCreator());
        Assertions.assertEquals(updateCode, codeQuestion.getCode());
        Assertions.assertEquals(updateContent, codeQuestion.getContent());
        Assertions.assertEquals(updateSetFilename, codeQuestion.getTestSetFilename());
        Assertions.assertEquals(updateKElementUris, codeQuestion.getkElementUris());
        Assertions.assertEquals(updateCourseUri, codeQuestion.getCourseUri());
    }

    @Test
    @Order(7)
    void deleteCodeQuestion() {
        Assertions.assertTrue(codeQuestionService.delete(uri));
        Assertions.assertNull(codeQuestionService.getCodeQuestionByUri(uri));
    }

}
