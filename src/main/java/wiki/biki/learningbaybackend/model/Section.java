package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

public class Section {
    private String id;
    private String uri;
    @FusekiProperty("sequence")
    private int sequence;
    @FusekiProperty("title")
    private String title;
    @FusekiProperty("content")
    private String content;
    @FusekiProperty("correspondKE")
    private String kElementURi;
    @FusekiProperty("belongs")
    private String lessonUri;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getkElementURi() {
        return kElementURi;
    }

    public void setkElementURi(String kElementURi) {
        this.kElementURi = kElementURi;
    }

    public String getLessonUri() {
        return lessonUri;
    }

    public void setLessonUri(String lessonUri) {
        this.lessonUri = lessonUri;
    }
}
