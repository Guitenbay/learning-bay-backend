package wiki.biki.learningbaybackend;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import wiki.biki.learningbaybackend.fuseki.EntityConfig;
import wiki.biki.learningbaybackend.model.MediaMaterial;
import wiki.biki.learningbaybackend.service.MediaMaterialService;
import wiki.biki.learningbaybackend.service.impl.MediaMaterialServiceImpl;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MediaMaterialServiceTest {
    private MediaMaterialService mediaMaterialService = new MediaMaterialServiceImpl();

    private final String testId         = "testMediaMaterialId";
    private final String testDate       = "2020-04-20";
    private final String testTitle      = "test mediaMaterial";
    private final String testCreator    = "tester";
    private final String testMediaType = "test type";
    private final String testFilename   = "test";
    private final String testDes        = "test description";

    private final String updateDate       = testDate + "change";
    private final String updateTitle      = testTitle + "change";
    private final String updateCreator    = testCreator + "change";
    private final String updateMediaType  = testMediaType + "change";
    private final String updateFilename   = testFilename + "change";
    private final String updateDes        = testDes + "change";
    
    private final String uri = EntityConfig.MM_PREFIX + testId;

    private MediaMaterial testMediaMaterial;
    private MediaMaterial updateMediaMaterial;
    {
        testMediaMaterial = new MediaMaterial();
        testMediaMaterial.setId(testId);
        testMediaMaterial.setDate(testDate);
        testMediaMaterial.setTitle(testTitle);
        testMediaMaterial.setCreator(testCreator);
        testMediaMaterial.setType(testMediaType);
        testMediaMaterial.setFilename(testFilename);
        testMediaMaterial.setDescription(testDes);

        updateMediaMaterial = new MediaMaterial();
        updateMediaMaterial.setId(testId);
        updateMediaMaterial.setDate(updateDate);
        updateMediaMaterial.setTitle(updateTitle);
        updateMediaMaterial.setCreator(updateCreator);
        updateMediaMaterial.setType(updateMediaType);
        updateMediaMaterial.setFilename(updateFilename);
        updateMediaMaterial.setDescription(updateDes);
    }
    @Test
    @Order(0)
    void getNoSuchMediaMaterial() {
        MediaMaterial mediaMaterial = mediaMaterialService.getMediaMaterialByUri(uri);
        Assertions.assertNull(mediaMaterial);
    }

    @Test
    @Order(1)
    void updateNoSuchMediaMaterial() {
        Assertions.assertFalse(mediaMaterialService.update(updateMediaMaterial));
    }

    @Test
    @Order(2)
    void deleteNoSuchMediaMaterial() {
        Assertions.assertFalse(mediaMaterialService.delete(uri));
    }

    @Test
    @Order(3)
    void insertMediaMaterial() {
        Assertions.assertTrue(mediaMaterialService.insert(testMediaMaterial));
    }

    @Test
    @Order(4)
    void insertDuplicateMediaMaterial() {
        Assertions.assertFalse(mediaMaterialService.insert(testMediaMaterial));
    }

    @Test
    @Order(5)
    void getMediaMaterialByUri() {
        MediaMaterial mediaMaterial = mediaMaterialService.getMediaMaterialByUri(uri);
        Assertions.assertNotNull(mediaMaterial);
        Assertions.assertEquals(testDate, mediaMaterial.getDate());
        Assertions.assertEquals(testTitle, mediaMaterial.getTitle());
        Assertions.assertEquals(testCreator, mediaMaterial.getCreator());
        Assertions.assertEquals(testMediaType, mediaMaterial.getType());
        Assertions.assertEquals(testFilename, mediaMaterial.getFilename());
        Assertions.assertEquals(testDes, mediaMaterial.getDescription());
    }

    @Test
    @Order(6)
    void updateMediaMaterial() {
        Assertions.assertTrue(mediaMaterialService.update(updateMediaMaterial));
        MediaMaterial mediaMaterial = mediaMaterialService.getMediaMaterialByUri(uri);
        Assertions.assertEquals(updateDate, mediaMaterial.getDate());
        Assertions.assertEquals(updateTitle, mediaMaterial.getTitle());
        Assertions.assertEquals(updateCreator, mediaMaterial.getCreator());
        Assertions.assertEquals(updateMediaType, mediaMaterial.getType());
        Assertions.assertEquals(updateFilename, mediaMaterial.getFilename());
        Assertions.assertEquals(updateDes, mediaMaterial.getDescription());
    }

    @Test
    @Order(7)
    void deleteMediaMaterial() {
        Assertions.assertTrue(mediaMaterialService.delete(uri));
        Assertions.assertNull(mediaMaterialService.getMediaMaterialByUri(uri));
    }

}
