package com.muxistudio.muxiio.model;

public class ProfileInfo {

    private String info;
    private String birthday;
    private String hometown;
    private String group;
    private String timejoin;
    private String timeleft;
    private String avatar_url;
    private String personal_blog;
    private String github;
    private String flickr;
    private String weibo;
    private String zhihu;

    public ProfileInfo(String info, String birthday, String hometown, String group,
                       String timejoin, String timeleft, String avatar_url, String personal_blog,
                       String github, String flickr, String weibo, String zhihu) {
        this.info = info;
        this.birthday = birthday;
        this.hometown = hometown;
        this.group = group;
        this.timejoin = timejoin;
        this.timeleft = timeleft;
        this.avatar_url = avatar_url;
        this.personal_blog = personal_blog;
        this.github = github;
        this.flickr = flickr;
        this.weibo = weibo;
        this.zhihu = zhihu;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getPersonal_blog() {
        return personal_blog;
    }

    public void setPersonal_blog(String personal_blog) {
        this.personal_blog = personal_blog;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getFlickr() {
        return flickr;
    }

    public void setFlickr(String flickr) {
        this.flickr = flickr;
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
