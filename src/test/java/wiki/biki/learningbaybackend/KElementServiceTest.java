package wiki.biki.learningbaybackend;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import wiki.biki.learningbaybackend.fuseki.EntityConfig;
import wiki.biki.learningbaybackend.model.KElement;
import wiki.biki.learningbaybackend.service.KElementService;
import wiki.biki.learningbaybackend.service.impl.KElementServiceImpl;

import java.util.ArrayList;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KElementServiceTest {
    private KElementService kElementService = new KElementServiceImpl();

    private final String testId         = "testKElementId";
    private final String testName       = "test kElement";
    private final String testDes        = "test kElement Description";
    private final String testCreator    = "tester";
    private final String testDate       = "2020-04-20";
    private final ArrayList<String> testPreviousList = new ArrayList<>();

    private final String updateName       = testName + "change";
    private final String updateDes        = testDes + "change";
    private final String updateCreator    = testCreator + "change";
    private final String updateDate       = testDate + "change";
    private final ArrayList<String> updatePreviousList = new ArrayList<>();

    private final String uri = EntityConfig.K_ELEMENT_PREFIX + testId;

    private KElement testKElement;
    private KElement updateKElement;
    {
        testKElement = new KElement();
        testKElement.setId(testId);
        testKElement.setName(testName);
        testKElement.setDescription(testDes);
        testKElement.setCreator(testCreator);
        testKElement.setDate(testDate);
        testKElement.setPreviousList(testPreviousList);

        updatePreviousList.add(EntityConfig.K_ELEMENT_PREFIX + "kElementId");
        updateKElement = new KElement();
        updateKElement.setId(testId);
        updateKElement.setName(updateName);
        updateKElement.setDescription(updateDes);
        updateKElement.setCreator(updateCreator);
        updateKElement.setDate(updateDate);
        updateKElement.setPreviousList(updatePreviousList);
    }
    @Test
    @Order(0)
    void getNoSuchKElement() {
        KElement kElement = kElementService.getKElementByUri(uri);
        Assertions.assertNull(kElement);
    }

    @Test
    @Order(1)
    void updateNoSuchKElement() {
        Assertions.assertFalse(kElementService.update(updateKElement));
    }

    @Test
    @Order(2)
    void deleteNoSuchKElement() {
        Assertions.assertFalse(kElementService.delete(uri));
    }
    
    @Test
    @Order(3)
    void insertKElement() {
        Assertions.assertTrue(kElementService.insert(testKElement));
    }

    @Test
    @Order(4)
    void insertDuplicateKElement() {
        Assertions.assertFalse(kElementService.insert(testKElement));
    }

    @Test
    @Order(5)
    void getKElementByUri() {
        KElement kElement = kElementService.getKElementByUri(uri);
        Assertions.assertNotNull(kElement);
        Assertions.assertEquals(testName, kElement.getName());
        Assertions.assertEquals(testDes, kElement.getDescription());
        Assertions.assertEquals(testCreator, kElement.getCreator());
        Assertions.assertEquals(testDate, kElement.getDate());
        // 传入为 [], 取出为 null
        Assertions.assertNull(kElement.getPreviousList());
    }

    @Test
    @Order(6)
    void updateKElement() {
        Assertions.assertTrue(kElementService.update(updateKElement));
        KElement kElement = kElementService.getKElementByUri(uri);
        Assertions.assertEquals(updateName, kElement.getName());
        Assertions.assertEquals(updateDes, kElement.getDescription());
        Assertions.assertEquals(updateCreator, kElement.getCreator());
        Assertions.assertEquals(updateDate, kElement.getDate());
        Assertions.assertEquals(updatePreviousList, kElement.getPreviousList());
    }

    @Test
    @Order(7)
    void deleteKElement() {
        Assertions.assertTrue(kElementService.delete(uri));
        Assertions.assertNull(kElementService.getKElementByUri(uri));
    }

}
