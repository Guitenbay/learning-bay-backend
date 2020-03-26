package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.UserKnowledgeState;

import java.util.ArrayList;

public interface UserKnowledgeStateService {

    ArrayList<UserKnowledgeState> getStateListByUserUri(String uri);
    int getState(String userUri, String uri);
    boolean isExist(String userUri, String uri);
    boolean insert(String userUri, UserKnowledgeState state);
    boolean update(String userUri, UserKnowledgeState state);
    boolean delete(String userUri, String uri);

}
