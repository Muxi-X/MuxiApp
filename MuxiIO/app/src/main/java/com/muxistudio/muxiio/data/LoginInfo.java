package com.muxistudio.muxiio.data;

/**
 * Created by kolibreath on 17-8-7.
 */

public class LoginInfo {
    /**
     * email : 111
     * password : !
     */

    //post data

    public LoginInfo(String username, String password){
        this.username = username;
        this.password = password;
    }

    private String username;
    private String password;

    public String getEmail() {
        return username;
    }

    public void setEmail(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
