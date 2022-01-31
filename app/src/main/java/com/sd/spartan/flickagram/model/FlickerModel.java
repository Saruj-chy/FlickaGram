package com.sd.spartan.flickagram.model;

public class FlickerModel {
    String id, owner, secret, server, farm, title, ispublic,
            datetaken, datetakengranularity, datetakenunknown, url_h, height_h, width_h;

    public FlickerModel(String id, String owner, String secret, String server, String farm, String title, String ispublic, String datetaken, String datetakengranularity, String datetakenunknown, String url_h, String height_h, String width_h) {

        this.id = id;
        this.owner = owner;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
        this.title = title;
        this.ispublic = ispublic;
        this.datetaken = datetaken;
        this.datetakengranularity = datetakengranularity;
        this.datetakenunknown = datetakenunknown;
        this.url_h = url_h;
        this.height_h = height_h;
        this.width_h = width_h;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public String getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public String getIspublic() {
        return ispublic;
    }

    public String getDatetaken() {
        return datetaken;
    }

    public String getDatetakengranularity() {
        return datetakengranularity;
    }

    public String getDatetakenunknown() {
        return datetakenunknown;
    }

    public String getUrl_h() {
        return url_h;
    }

    public String getHeight_h() {
        return height_h;
    }

    public String getWidth_h() {
        return width_h;
    }


}
