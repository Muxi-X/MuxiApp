package com.muxistudio.muxiio.model;

/**
 * Created by kolibreath on 17-8-21.
 */

public class AvatarUrl {

    /**
     * avatar : text
     */

    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public AvatarUrl(String avatar){
        this.avatar = avatar;
    }
}
