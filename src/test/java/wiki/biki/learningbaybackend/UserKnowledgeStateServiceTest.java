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
    private static UserService userService = new UserServiceImpl();
    private UserKnowledgeStateService userKnowledgeStateService = new UserKnowledgeStateServiceImpl();

    private static final String testUserId = "testUserId";
    private static final String userUri = GraphConfig.USER_GRAPH_PREFIX + testUserId;

    private final String testKElementUri = EntityConfig.K_ELEMENT_PREFIX + "testKElementId";
    private final int testState = 1;

    private final int updateState = testState + 1;

    private UserKnowledgeState testUserKnowledgeState = new UserKnowledgeState(testKElementUri, testState);
    private UserKnowledgeState updateUserKnowledgeState = new UserKnowledgeState(testKElementUri, updateState);
    @BeforeAll
    static void beforeAll() {
        // 创建 用户
        User user = new User();
        user.setId(testUserId);
        user.setUsername("test user");
        user.setPassword("test user");
        Assertions.assertTrue(userService.insert(user));
    }
    @AfterAll
    static void afterAll() {
        // 删除 用户
        Assertions.assertTrue(userService.delete(userUri));
    }

    @Test
    @Order(0)
    void getNotSuchUserKnowledgeStateByUri() {
        Assertions.assertNotEquals(testState, userKnowledgeStateService.getState(userUri, testKElementUri));
    }

    @Test
    @Order(1)
    void doNotExistUserKnowledgeState() {
        Assertions.assertFalse(userKnowledgeStateService.isExist(userUri, testKElementUri));
    }

    @Test
    @Order(2)
    void insertUserKnowledgeStateWithoutCheckingExist() {
        Assertions.assertTrue(userKnowledgeStateService.insertWithoutCheckingExist(userUri, testUserKnowledgeState));
    }

    @Test
    @Order(3)
    void existUserKnowledgeState() {
        Assertions.assertTrue(userKnowledgeStateService.isExist(userUri, testKElementUri));
    }

    @Test
    @Order(4)
    void getUserKnowledgeStateByUri() {
        Assertions.assertEquals(testState, userKnowledgeStateService.getState(userUri, testKElementUri));
    }

    @Test
    @Order(5)
    void updateUserKnowledgeStateWithoutCheckingExist() {
        Assertions.assertTrue(userKnowledgeStateService.updateWithoutCheckingExist(userUri, updateUserKnowledgeState));
        Assertions.assertEquals(updateState, userKnowledgeStateService.getState(userUri, testKElementUri));
    }

    @Test
    @Order(6)
    void deleteUserKnowledgeStateWithoutCheckingExist() {
        Assertions.assertTrue(userKnowledgeStateService.deleteWithoutCheckingExist(userUri, testKElementUri));
        Assertions.assertEquals(-1, userKnowledgeStateService.getState(userUri, testKElementUri));
    }

}
