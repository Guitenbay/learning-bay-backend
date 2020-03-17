package wiki.biki.learningbaybackend.model;

import wiki.biki.learningbaybackend.fuseki.FusekiProperty;

public class MediaMaterial {
    private String id;
    private String uri;
    @FusekiProperty("date")
    private String date;
    @FusekiProperty("title")
    private String title;
    @FusekiProperty("creator")
    private String creator;
    @FusekiProperty("type")
    private String type;
    @FusekiProperty("filename")
    private String filename;
    @FusekiProperty("description")
    private String description;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
