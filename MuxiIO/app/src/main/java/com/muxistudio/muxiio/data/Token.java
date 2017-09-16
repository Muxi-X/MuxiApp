package com.muxistudio.muxiio.data;

/**
 * Created by kolibreath on 17-8-7.
 */

public class Token {
    /**
     * username
     * avatar : http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif
     *  "user_id": 2
     */

    private String avatar;
    private String token;
    private int user_id;
   // private int auth_user_id;

    public String getToken(){
        return token;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

   // public int getAuth_user_id() {
        //return auth_user_id;

    //public void setAuth_user_id(int auth_user_id) {
      //  this.auth_user_id = auth_user_id;
   // }
}
