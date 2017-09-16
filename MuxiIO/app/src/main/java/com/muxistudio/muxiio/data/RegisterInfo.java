package com.muxistudio.muxiio.data;

/**
 * Created by kolibreath on 17-8-7.
 */

public class RegisterInfo {

    /**
     * email : 734780179@qq.com
     * username : szypride666
     * password : fffffffff
     */

    //post register info

    private String email;
    private String username;
    private String password;

    public RegisterInfo(String email, String username, String password){
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
