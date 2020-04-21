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

    private final String uri = EntityConfig.K_ELEMENT_PREFIX + testId;

    @Test
    @Order(0)
    void getNotSuchKElement() {
        KElement kElement = kElementService.getKElementByUri(uri);
        Assertions.assertNull(kElement);
    }
    
    @Test
    @Order(1)
    void insertKElement() {
        KElement kElement = new KElement();
        kElement.setId(testId);
        kElement.setName(testName);
        kElement.setDescription(testDes);
        kElement.setCreator(testCreator);
        kElement.setDate(testDate);
        kElement.setPreviousList(testPreviousList);
        Assertions.assertTrue(kElementService.insert(kElement));
    }

    @Test
    @Order(2)
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
    @Order(3)
    void updateKElement() {
        KElement kElement = kElementService.getKElementByUri(uri);
        String updateName       = testName + "change";
        String updateDes        = testDes + "change";
        String updateCreator    = testCreator + "change";
        String updateDate       = testDate + "change";
        testPreviousList.add(EntityConfig.K_ELEMENT_PREFIX + "kElementId");
        ArrayList<String> updatePreviousList = testPreviousList;
        kElement.setId(testId);
        kElement.setName(updateName);
        kElement.setDescription(updateDes);
        kElement.setCreator(updateCreator);
        kElement.setDate(updateDate);
        kElement.setPreviousList(updatePreviousList);
        Assertions.assertTrue(kElementService.update(kElement));
        kElement = kElementService.getKElementByUri(uri);
        Assertions.assertEquals(updateName, kElement.getName());
        Assertions.assertEquals(updateDes, kElement.getDescription());
        Assertions.assertEquals(updateCreator, kElement.getCreator());
        Assertions.assertEquals(updateDate, kElement.getDate());
        Assertions.assertEquals(updatePreviousList, kElement.getPreviousList());
    }

    @Test
    @Order(4)
    void deleteKElement() {
        Assertions.assertTrue(kElementService.delete(uri));
        Assertions.assertNull(kElementService.getKElementByUri(uri));
    }

}
