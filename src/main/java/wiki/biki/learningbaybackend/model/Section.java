package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

public class Section {
    private String id;
    private String uri;
    @FusekiProperty("title")
    private String title;
    @FusekiProperty("content")
    private String content;
    @FusekiProperty("correspondKE")
    private String kElementURi;
    @FusekiProperty("correspondCQ")
    private String codeQuestionUri;

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

    public String getCodeQuestionUri() {
        return codeQuestionUri;
    }

    public void setCodeQuestionUri(String codeQuestionUri) {
        this.codeQuestionUri = codeQuestionUri;
    }
}
