package wiki.biki.learningbaybackend.model;

import java.util.ArrayList;

public class UserKnowledgeStateList {
    private String userUri;
    private ArrayList<UserKnowledgeState> knowledgeStates;

    public String getUserUri() {
        return userUri;
    }

    public void setUserUri(String userUri) {
        this.userUri = userUri;
    }

    public ArrayList<UserKnowledgeState> getKnowledgeStates() {
        return knowledgeStates;
    }

    public void setKnowledgeStates(ArrayList<UserKnowledgeState> knowledgeStates) {
        this.knowledgeStates = knowledgeStates;
    }
}
