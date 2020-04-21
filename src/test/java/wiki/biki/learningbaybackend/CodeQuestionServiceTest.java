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

    private final String uri            = EntityConfig.CQ_PREFIX + testId;

    @Test
    @Order(0)
    void getNotSuchCodeQuestion() {
        CodeQuestion codeQuestion = codeQuestionService.getCodeQuestionByUri(uri);
        Assertions.assertNull(codeQuestion);
    }

    @Test
    @Order(1)
    void insertCodeQuestion() {
        CodeQuestion codeQuestion = new CodeQuestion();
        codeQuestion.setId(testId);
        codeQuestion.setDate(testDate);
        codeQuestion.setTitle(testTitle);
        codeQuestion.setCreator(testCreator);
        codeQuestion.setCode(testCode);
        codeQuestion.setContent(testContent);
        codeQuestion.setTestSetFilename(testSetFilename);
        codeQuestion.setkElementUris(testKElementUris);
        codeQuestion.setCourseUri(testCourseUri);
        Assertions.assertTrue(codeQuestionService.insert(codeQuestion));
    }

    @Test
    @Order(2)
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
    @Order(3)
    void updateCodeQuestion() {
        CodeQuestion codeQuestion = codeQuestionService.getCodeQuestionByUri(uri);
        String updateDate       = testDate + "change";
        String updateTitle      = testTitle + "change";
        String updateCreator    = testCreator + "change";
        String updateCode       = testCode + "change";
        String updateContent    = testContent + "change";
        String updateSetFilename= testSetFilename + "change";
        testKElementUris.add(EntityConfig.K_ELEMENT_PREFIX + "kElementId");
        ArrayList<String> updateKElementUris = testKElementUris;
        String updateCourseUri  = testCourseUri + "change";
        codeQuestion.setId(testId);
        codeQuestion.setDate(updateDate);
        codeQuestion.setTitle(updateTitle);
        codeQuestion.setCreator(updateCreator);
        codeQuestion.setCode(updateCode);
        codeQuestion.setContent(updateContent);
        codeQuestion.setTestSetFilename(updateSetFilename);
        codeQuestion.setkElementUris(updateKElementUris);
        codeQuestion.setCourseUri(updateCourseUri);
        Assertions.assertTrue(codeQuestionService.update(codeQuestion));
        codeQuestion = codeQuestionService.getCodeQuestionByUri(uri);
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
    @Order(4)
    void deleteCodeQuestion() {
        Assertions.assertTrue(codeQuestionService.delete(uri));
        Assertions.assertNull(codeQuestionService.getCodeQuestionByUri(uri));
    }

}
