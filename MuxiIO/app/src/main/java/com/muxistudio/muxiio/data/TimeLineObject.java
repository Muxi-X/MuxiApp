package com.muxistudio.muxiio.data;

import java.util.List;

/**
 * Created by kolibreath on 17-7-30.
 */

public class TimeLineObject {

    //retrieve from serve as a list !

    public TimeLineObject(){

    }
    /**
     * page : 1
     * pages_count : 2
     * share : [{"avatar":null,"comment":"/api/v2.0/8/comments/","date":"Wed, 02 Aug 2017 09:47:35 GMT","id":8,"share":"124","title":null,"username":"1"},{"avatar":null,"comment":"/api/v2.0/7/comments/","date":"Wed, 02 Aug 2017 09:46:56 GMT","id":7,"share":"124","title":null,"username":"1"},{"avatar":null,"comment":"/api/v2.0/6/comments/","date":"Wed, 02 Aug 2017 09:25:28 GMT","id":6,"share":"124","title":null,"username":"1"},{"avatar":null,"comment":"/api/v2.0/5/comments/","date":"Wed, 02 Aug 2017 08:58:43 GMT","id":5,"share":"124","title":null,"username":"1"},{"avatar":null,"comment":"/api/v2.0/4/comments/","date":"Wed, 02 Aug 2017 08:55:02 GMT","id":4,"share":"124","title":null,"username":"1"}]
     */

    private int page;
    private int pages_count;
    private List<ShareBean> share;

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

    public List<ShareBean> getShare() {
        return share;
    }

    public void setShare(List<ShareBean> share) {
        this.share = share;
    }

    public static class ShareBean {
        /**
         * avatar : null
         * comment : /api/v2.0/8/comments/
         * date : Wed, 02 Aug 2017 09:47:35 GMT
         * id : 8
         * share : 124
         * title : null
         * username : 1
         */

        private Object avatar;
        private String comment;
        private String date;
        private int id;
        private String share;
        private Object title;
        private String username;

        public Object getAvatar() {
            return avatar;
        }

        public void setAvatar(Object avatar) {
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

        public String getShare() {
            return share;
        }

        public void setShare(String share) {
            this.share = share;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
