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
    
    private final String uri = EntityConfig.MM_PREFIX + testId;

    @Test
    @Order(1)
    void insertMediaMaterial() {
        MediaMaterial mediaMaterial = new MediaMaterial();
        mediaMaterial.setId(testId);
        mediaMaterial.setDate(testDate);
        mediaMaterial.setTitle(testTitle);
        mediaMaterial.setCreator(testCreator);
        mediaMaterial.setType(testMediaType);
        mediaMaterial.setFilename(testFilename);
        mediaMaterial.setDescription(testDes);
        Assertions.assertTrue(mediaMaterialService.insert(mediaMaterial));
    }

    @Test
    @Order(2)
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
    @Order(3)
    void updateMediaMaterial() {
        MediaMaterial mediaMaterial = mediaMaterialService.getMediaMaterialByUri(uri);
        String updateDate       = testDate + "change";
        String updateTitle      = testTitle + "change";
        String updateCreator    = testCreator + "change";
        String updateMediaType  = testMediaType + "change";
        String updateFilename   = testFilename + "change";
        String updateDes        = testDes + "change";
        mediaMaterial.setId(testId);
        mediaMaterial.setDate(updateDate);
        mediaMaterial.setTitle(updateTitle);
        mediaMaterial.setCreator(updateCreator);
        mediaMaterial.setType(updateMediaType);
        mediaMaterial.setFilename(updateFilename);
        mediaMaterial.setDescription(updateDes);
        Assertions.assertTrue(mediaMaterialService.update(mediaMaterial));
        mediaMaterial = mediaMaterialService.getMediaMaterialByUri(uri);
        Assertions.assertEquals(updateDate, mediaMaterial.getDate());
        Assertions.assertEquals(updateTitle, mediaMaterial.getTitle());
        Assertions.assertEquals(updateCreator, mediaMaterial.getCreator());
        Assertions.assertEquals(updateMediaType, mediaMaterial.getType());
        Assertions.assertEquals(updateFilename, mediaMaterial.getFilename());
        Assertions.assertEquals(updateDes, mediaMaterial.getDescription());
    }

    @Test
    @Order(4)
    void deleteMediaMaterial() {
        Assertions.assertTrue(mediaMaterialService.delete(uri));
        Assertions.assertNull(mediaMaterialService.getMediaMaterialByUri(uri));
    }

}
