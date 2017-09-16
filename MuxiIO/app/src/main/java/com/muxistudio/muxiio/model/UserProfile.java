package com.muxistudio.muxiio.model;

/**
 * Created by kolibreath on 17-8-12.
 */

public class UserProfile {
    /**
     * avatar_url : http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif
     * birthday : null
     * email : 1
     * flickr : null
     * github : null
     * group : null
     * hometown : null
     * id : 1
     * info : null
     * personal_blog : null
     * timejoin : null
     * timeleft : null
     * username : 1
     * weibo : null
     * zhihu : null
     */

    //return from user profile

    private String avatar_url;
    private String birthday;
    private String email;
    private String flickr;
    private String github;
    private String group;
    private String hometown;
    private int id;
    private String info;
    private String personal_blog;
    private String timejoin;
    private String timeleft;
    private String username;
    private String weibo;
    private String zhihu;

    public UserProfile(String avatar_url, String birthday, String email,
                       String flickr, String github, String group,
                       String hometown, int id, String info,
                       String personal_blog, String timejoin, String timeleft,
                       String username, String weibo, String zhihu) {
        this.avatar_url = avatar_url;
        this.birthday = birthday;
        this.email = email;
        this.flickr = flickr;
        this.github = github;
        this.group = group;
        this.hometown = hometown;
        this.id = id;
        this.info = info;
        this.personal_blog = personal_blog;
        this.timejoin = timejoin;
        this.timeleft = timeleft;
        this.username = username;
        this.weibo = weibo;
        this.zhihu = zhihu;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlickr() {
        return flickr;
    }

    public void setFlickr(String flickr) {
        this.flickr = flickr;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPersonal_blog() {
        return personal_blog;
    }

    public void setPersonal_blog(String personal_blog) {
        this.personal_blog = personal_blog;
    }

    public String getTimejoin() {
        return timejoin;
    }

    public void setTimejoin(String timejoin) {
        this.timejoin = timejoin;
    }

    public String getTimeleft() {
        return timeleft;
    }

    public void setTimeleft(String timeleft) {
        this.timeleft = timeleft;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getZhihu() {
        return zhihu;
    }

    public void setZhihu(String zhihu) {
        this.zhihu = zhihu;
    }





}
