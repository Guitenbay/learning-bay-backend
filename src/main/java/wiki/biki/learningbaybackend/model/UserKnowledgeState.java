package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

public class UserKnowledgeState {
    private String uri;
    @FusekiProperty("state")
    private int state;

    public UserKnowledgeState() {}
    public UserKnowledgeState(String uri, int state) {
        this.uri = uri;
        this.state = state;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
