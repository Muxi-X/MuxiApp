package com.muxistudio.muxiio.model;

/**
 * Created by kolibreath on 17-8-7.
 */

public class AddShare {
    //post to add share

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    private String share;
    private String tags;


    public AddShare(String title, String share, String tags){
        this.tags = tags;
        this.title =title;
        this.share = share;
    }
}
