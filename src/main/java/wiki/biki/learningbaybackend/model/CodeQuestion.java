package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

import java.util.ArrayList;

public class CodeQuestion {
    private String id;
    private String uri;
    @FusekiProperty("date")
    private String date;
    @FusekiProperty("title")
    private String title;
    @FusekiProperty("creator")
    private String creator;
    @FusekiProperty("code")
    private String code;
    @FusekiProperty("content")
    private String content;
    @FusekiProperty("testSetFilename")
    private String testSetFilename;
    @FusekiProperty("hasKElement")
    private ArrayList<String> kElementUris;
    @FusekiProperty("belongs")
    private String courseUri;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTestSetFilename() {
        return testSetFilename;
    }

    public void setTestSetFilename(String testSetFilename) {
        this.testSetFilename = testSetFilename;
    }

    public ArrayList<String> getkElementUris() {
        return kElementUris;
    }

    public void setkElementUris(ArrayList<String> kElementUris) {
        this.kElementUris = kElementUris;
    }

    public String getCourseUri() {
        return courseUri;
    }

    public void setCourseUri(String courseUri) {
        this.courseUri = courseUri;
    }
}
