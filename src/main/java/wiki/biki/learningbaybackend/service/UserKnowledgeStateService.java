package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.UserKnowledgeState;

import java.util.ArrayList;

public interface UserKnowledgeStateService {

    ArrayList<UserKnowledgeState> getStateListByUri(String uri);
    boolean isExist(String uri);
    boolean insert(UserKnowledgeState user);
    boolean update(UserKnowledgeState user);
    boolean delete(String uri);

}
