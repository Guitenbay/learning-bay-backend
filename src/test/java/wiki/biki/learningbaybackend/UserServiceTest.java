package wiki.biki.learningbaybackend;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import wiki.biki.learningbaybackend.fuseki.EntityConfig;
import wiki.biki.learningbaybackend.fuseki.GraphConfig;
import wiki.biki.learningbaybackend.model.User;
import wiki.biki.learningbaybackend.service.UserService;
import wiki.biki.learningbaybackend.service.impl.UserServiceImpl;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    private UserService userService = new UserServiceImpl();
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final String testId = "testUserId";
    private final String testUsername = "test user";
    private final String testPassword = "test user";

    private final String updateUsername = testUsername + "change";
    private final String updatePassword = testPassword + "change";

    private final String uri = GraphConfig.USER_GRAPH_PREFIX + testId;

    private User testUser;
    private User updateUser;
    {
        testUser = new User();
        testUser.setId(testId);
        testUser.setUsername(testUsername);
        testUser.setPassword(passwordEncoder.encode(testPassword));

        updateUser = new User();
        updateUser.setId(testId);
        updateUser.setUsername(updateUsername);
        updateUser.setPassword(passwordEncoder.encode(updatePassword));
    }
    @Test
    @Order(0)
    void getNotSuchUserUriByUsernameAndPassword() {
        User user = new User();
        user.setUsername(testUsername);
        user.setPassword(testPassword);
        Assertions.assertNull(userService.getUserUri(user, passwordEncoder));
    }

    @Test
    @Order(1)
    void updateNoSuchUser() {
        Assertions.assertFalse(userService.update(updateUser));
    }

    @Test
    @Order(2)
    void deleteNoSuchUser() {
        Assertions.assertFalse(userService.delete(uri));
    }

    @Test
    @Order(3)
    void insertUser() {
        Assertions.assertTrue(userService.insert(testUser));
    }

    @Test
    @Order(4)
    void insertDuplicateUser() {
        Assertions.assertFalse(userService.insert(testUser));
    }

    @Test
    @Order(5)
    void getUserUriByUsernameAndPassword() {
        User user = new User();
        user.setUsername(testUsername);
        user.setPassword(testPassword);
        Assertions.assertEquals(uri, userService.getUserUri(user, passwordEncoder));
    }

    @Test
    @Order(6)
    void updateUser() {
        Assertions.assertTrue(userService.update(updateUser));
        updateUser.setPassword(updatePassword);
        Assertions.assertEquals(uri, userService.getUserUri(updateUser, passwordEncoder));
    }

    @Test
    @Order(7)
    void deleteUser() {
        Assertions.assertTrue(userService.delete(uri));
    }

}
