package wiki.biki.learningbaybackend.service;

import wiki.biki.learningbaybackend.model.UserKnowledgeState;

import java.util.ArrayList;

public interface UserKnowledgeStateService {

    ArrayList<UserKnowledgeState> getStateListByUserUri(String uri);
    int getState(String userUri, String uri);
    boolean isExist(String userUri, String uri);
    boolean insertWithoutCheckingExist(String userUri, UserKnowledgeState state);
    boolean updateWithoutCheckingExist(String userUri, UserKnowledgeState state);
    boolean deleteWithoutCheckingExist(String userUri, String uri);

}
