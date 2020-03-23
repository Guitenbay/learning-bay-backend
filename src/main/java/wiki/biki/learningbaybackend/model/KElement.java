package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

import java.util.ArrayList;

public class KElement {
    private String id;
    private String uri;
    @FusekiProperty("name")
    private String name;
    @FusekiProperty("description")
    private String description;
    @FusekiProperty("creator")
    private String creator;
    @FusekiProperty("date")
    private String date;
    @FusekiProperty("next")
    private ArrayList<String> nextList;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getNextList() {
        return nextList;
    }

    public void setNextList(ArrayList<String> nextList) {
        this.nextList = nextList;
    }
}
