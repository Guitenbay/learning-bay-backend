package wiki.biki.learningbaybackend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import wiki.biki.learningbaybackend.model.User;

public interface UserService {

    String getUserUri(User user, PasswordEncoder passwordEncoder);
    boolean isExist(String username);
    boolean insert(User user);
    boolean update(User user);
    boolean delete(String uri);

}
