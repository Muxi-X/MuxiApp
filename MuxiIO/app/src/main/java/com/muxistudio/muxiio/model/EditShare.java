package com.muxistudio.muxiio.model;

/**
 * Created by kolibreath on 17-8-7.
 */

public class EditShare {

    private String share;
    private String title;

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EditShare(String share,String title){
        this.share = share;
        this.title = title;
    }
}
