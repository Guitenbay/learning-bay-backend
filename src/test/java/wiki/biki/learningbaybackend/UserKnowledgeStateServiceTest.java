package wiki.biki.learningbaybackend;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import wiki.biki.learningbaybackend.fuseki.EntityConfig;
import wiki.biki.learningbaybackend.fuseki.GraphConfig;
import wiki.biki.learningbaybackend.model.UserKnowledgeState;
import wiki.biki.learningbaybackend.model.User;
import wiki.biki.learningbaybackend.service.UserKnowledgeStateService;
import wiki.biki.learningbaybackend.service.UserService;
import wiki.biki.learningbaybackend.service.impl.UserKnowledgeStateServiceImpl;
import wiki.biki.learningbaybackend.service.impl.UserServiceImpl;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserKnowledgeStateServiceTest {
    private UserService userService = new UserServiceImpl();
    private UserKnowledgeStateService userKnowledgeStateService = new UserKnowledgeStateServiceImpl();

    private final String testUserId = "testUserId";

    private final String userUri = GraphConfig.USER_GRAPH_PREFIX + testUserId;
    
    private final String testKElementUri = EntityConfig.K_ELEMENT_PREFIX + "testKElementId";
    private final int testState = 1;

    @Test
    @Order(1)
    void insertUserKnowledgeState() {
        // 创建 用户
        User user = new User();
        user.setId(testUserId);
        user.setUsername("test user");
        user.setPassword("test user");
        Assertions.assertTrue(userService.insert(user));

        UserKnowledgeState userKnowledgeState = new UserKnowledgeState(testKElementUri, testState);
        Assertions.assertFalse(userKnowledgeStateService.isExist(userUri, testKElementUri));
        Assertions.assertTrue(userKnowledgeStateService.insertWithoutCheckingExist(userUri, userKnowledgeState));
    }

    @Test
    @Order(2)
    void getUserKnowledgeStateByUri() {
        Assertions.assertEquals(testState, userKnowledgeStateService.getState(userUri, testKElementUri));
    }

    @Test
    @Order(3)
    void updateUserKnowledgeState() {
        int updateState = testState + 1;
        UserKnowledgeState userKnowledgeState = new UserKnowledgeState(testKElementUri, updateState);
        Assertions.assertTrue(userKnowledgeStateService.isExist(userUri, testKElementUri));
        Assertions.assertTrue(userKnowledgeStateService.updateWithoutCheckingExist(userUri, userKnowledgeState));
        Assertions.assertEquals(updateState, userKnowledgeStateService.getState(userUri, testKElementUri));
    }

    @Test
    @Order(4)
    void deleteUserKnowledgeState() {
        Assertions.assertTrue(userKnowledgeStateService.deleteWithoutCheckingExist(userUri, testKElementUri));
        Assertions.assertEquals(-1, userKnowledgeStateService.getState(userUri, testKElementUri));
        // 删除 用户
        Assertions.assertTrue(userService.delete(userUri));
    }

}
