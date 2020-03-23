package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.User;

public interface UserService {

    String getUserUri(User user);
    boolean isExist(String uri);
    boolean insert(User user);
    boolean update(User user);
    boolean delete(String uri);

}
