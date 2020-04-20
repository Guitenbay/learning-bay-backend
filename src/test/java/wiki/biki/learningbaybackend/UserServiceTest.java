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

    private final String uri = GraphConfig.USER_GRAPH_PREFIX + testId;

    @Test
    @Order(1)
    void insertUser() {
        User user = new User();
        user.setId(testId);
        user.setUsername(testUsername);
        user.setPassword(testPassword);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Assertions.assertTrue(userService.insert(user));
    }

    @Test
    @Order(2)
    void getUserUriByUsernameAndPassword() {
        User user = new User();
        user.setUsername(testUsername);
        user.setPassword(testPassword);
        Assertions.assertEquals(uri, userService.getUserUri(user, passwordEncoder));
    }

    @Test
    @Order(3)
    void updateUser() {
        String updateUsername = testUsername + "change";
        String updatePassword = testPassword + "change";

        User user = new User();
        user.setId(testId);
        user.setUsername(updateUsername);
        user.setPassword(updatePassword);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Assertions.assertTrue(userService.update(user));
        user.setPassword(updatePassword);
        Assertions.assertEquals(uri, userService.getUserUri(user, passwordEncoder));
    }

    @Test
    @Order(4)
    void deleteUser() {
        Assertions.assertTrue(userService.delete(uri));
    }

}
