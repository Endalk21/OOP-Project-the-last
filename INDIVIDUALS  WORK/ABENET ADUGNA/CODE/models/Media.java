package models;

public abstract class Media {
    protected String url;
    protected String type;

    public Media(String url, String type) {
        this.url = url;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }
}
