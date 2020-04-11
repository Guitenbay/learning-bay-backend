package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

public class UserKnowledgeState {
    private String uri;
    /**
     * state :
     *  0   未掌握
     *  1   理解
     *  2   应用缓冲
     *  3   应用
     */
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
