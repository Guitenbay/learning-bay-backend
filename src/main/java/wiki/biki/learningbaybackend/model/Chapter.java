package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

import java.util.ArrayList;

public class Chapter {
    private String id;
    private String uri;
    @FusekiProperty("title")
    private String title;
    @FusekiProperty("hasLesson")
    private ArrayList<String> lessonUris;

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

    public ArrayList<String> getLessonUris() {
        return lessonUris;
    }

    public void setLessonUris(ArrayList<String> lessonUris) {
        this.lessonUris = lessonUris;
    }
}
