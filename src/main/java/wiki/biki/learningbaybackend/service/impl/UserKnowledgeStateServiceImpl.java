package wiki.biki.learningbaybackend.service.impl;

import wiki.biki.learningbaybackend.model.UserKnowledgeState;
import wiki.biki.learningbaybackend.service.UserKnowledgeStateService;

import java.util.ArrayList;

public class UserKnowledgeStateServiceImpl implements UserKnowledgeStateService {
    @Override
    public ArrayList<UserKnowledgeState> getStateListByUri(String uri) {
        return null;
    }

    @Override
    public boolean isExist(String uri) {
        return false;
    }

    @Override
    public boolean insert(UserKnowledgeState user) {
        return false;
    }

    @Override
    public boolean update(UserKnowledgeState user) {
        return false;
    }

    @Override
    public boolean delete(String uri) {
        return false;
    }
}
