package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

import java.util.ArrayList;

public class Lesson {
    private String id;
    private String uri;
    @FusekiProperty("title")
    private String title;
    @FusekiProperty("hasMedia")
    private String mediaUri;
    @FusekiProperty("hasSection")
    private ArrayList<String> sectionUris;

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

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public ArrayList<String> getSectionUris() {
        return sectionUris;
    }

    public void setSectionUris(ArrayList<String> sectionUris) {
        this.sectionUris = sectionUris;
    }
}
