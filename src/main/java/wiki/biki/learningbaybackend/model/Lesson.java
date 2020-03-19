package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

import java.util.ArrayList;

public class Lesson {
    private String id;
    private String uri;
    @FusekiProperty("sequence")
    private int sequence;
    @FusekiProperty("title")
    private String title;
    @FusekiProperty("hasMedia")
    private String mediaUri;
    @FusekiProperty("belongs")
    private String chapterUri;
//    @FusekiProperty("hasSection")
//    private ArrayList<String> sectionUris;

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

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public String getChapterUri() {
        return chapterUri;
    }

    public void setChapterUri(String chapterUri) {
        this.chapterUri = chapterUri;
    }
}
