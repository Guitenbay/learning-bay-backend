package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

import java.util.ArrayList;

public class Course {
    private String id;
    private String uri;
    @FusekiProperty("title")
    private String title;
//    @FusekiProperty("contains")
//    private ArrayList<String> codeQuestionUris;
//    @FusekiProperty("hasChapter")
//    private ArrayList<String> chapterUris;

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
}
