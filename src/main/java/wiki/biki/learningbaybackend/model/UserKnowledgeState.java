package wiki.biki.learningbaybackend.model;

public class UserKnowledgeState {
    private String kElementUri;
    private int state;

    public String getkElementUri() {
        return kElementUri;
    }

    public void setkElementUri(String kElementUri) {
        this.kElementUri = kElementUri;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
