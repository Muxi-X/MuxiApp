package com.muxistudio.muxiio.data;

/**
 * Created by kolibreath on 17-8-7.
 */

public class AfterAddShare {
    /**
     * share : 1
     * title : 1
     * tag : !
     * author_id : 1
     * id : 2
     */

    private String share;
    private String title;
    private String tag;
    private int author_id;
    private int id;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
