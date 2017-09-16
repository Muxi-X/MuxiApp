package com.muxistudio.muxiio.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kolibreath on 17-8-7.
 */

public class ShareList{
    /**
     * count : 7
     * page : 1
     * pages_count : 1
     * shares : [{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"/api/v2.0/1/comments/","date":"Sat, 12 Aug 2017 13:09:33 GMT","id":1,"read_num":0,"share":"456","tag":"frontend","title":"123","user_id":1,"username":"我"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"/api/v2.0/2/comments/","date":"Sat, 12 Aug 2017 13:11:54 GMT","id":2,"read_num":1,"share":"456","tag":"frontend","title":"123","user_id":1,"username":"我"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"/api/v2.0/3/comments/","date":"Sat, 12 Aug 2017 13:13:32 GMT","id":3,"read_num":1,"share":"456","tag":"frontend","title":"123","user_id":1,"username":"我"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"/api/v2.0/4/comments/","date":"Mon, 14 Aug 2017 07:04:15 GMT","id":4,"read_num":0,"share":"111","tag":"frontend","title":"11111111111111111","user_id":3,"username":""},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"/api/v2.0/5/comments/","date":"Mon, 14 Aug 2017 07:55:13 GMT","id":5,"read_num":0,"share":"111","tag":"frontend","title":"2","user_id":3,"username":""},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"/api/v2.0/11/comments/","date":"Thu, 17 Aug 2017 01:40:00 GMT","id":11,"read_num":0,"share":"1","tag":"frontend","title":"2","user_id":9,"username":"4"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"/api/v2.0/12/comments/","date":"Thu, 17 Aug 2017 01:43:11 GMT","id":12,"read_num":0,"share":"1","tag":"frontend","title":"2","user_id":9,"username":"4"}]
     */

    private int share_num;
    private int count;
    private int page;
    private int pages_count;
    private List<SharesBean> shares;

    public int getShare_num() {
        return share_num;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages_count() {
        return pages_count;
    }

    public void setPages_count(int pages_count) {
        this.pages_count = pages_count;
    }

    public List<SharesBean> getShares() {
        return shares;
    }

    public void setShares(List<SharesBean> shares) {
        this.shares = shares;
    }

    public static class SharesBean implements Serializable {
        private String avatar;
        private String comment;
        private String date;
        private int id;
        private int read_num;
        private String share;
        private String tag;
        private String title;
        private int user_id;
        private String username;

        public SharesBean(String avatar, String comment, String date, int id, int read_num,
                          String share, String tag, String title, int user_id, String username) {
            this.avatar = avatar;
            this.comment = comment;
            this.id = id;
            this.read_num = read_num;
            this.share = share;
            this.tag = tag;
            this.title = title;
            this.user_id = user_id;
            this.username = username;
            this.date = date;
        }


        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRead_num() {
            return read_num;
        }

        public void setRead_num(int read_num) {
            this.read_num = read_num;
        }

        public String getShare() {
            return share;
        }

        public void setShare(String share) {
            this.share = share;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}