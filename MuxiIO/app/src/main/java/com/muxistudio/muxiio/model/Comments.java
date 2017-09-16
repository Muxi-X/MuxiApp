package com.muxistudio.muxiio.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by kolibreath on 17-8-9.
 */

public class Comments implements Comparable<Comments>{

    /**
     * comment : [{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"123","date":"Fri, 11 Aug 2017 14:06:09 GMT","username":"2768"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"123","date":"Fri, 11 Aug 2017 12:44:08 GMT","username":"2768"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"123","date":"Fri, 11 Aug 2017 12:44:07 GMT","username":"2768"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"123","date":"Fri, 11 Aug 2017 12:43:56 GMT","username":"2768"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"123","date":"Fri, 11 Aug 2017 12:11:20 GMT","username":"2768"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"jxud","date":"Thu, 10 Aug 2017 01:51:21 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"duxj","date":"Thu, 10 Aug 2017 01:51:03 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"duxj","date":"Thu, 10 Aug 2017 01:49:39 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"jxud","date":"Thu, 10 Aug 2017 01:49:23 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"duxj","date":"Thu, 10 Aug 2017 01:47:12 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"duxj","date":"Thu, 10 Aug 2017 01:47:11 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"jxud","date":"Thu, 10 Aug 2017 01:47:07 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"duxj","date":"Thu, 10 Aug 2017 01:47:06 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"duxj","date":"Thu, 10 Aug 2017 01:45:00 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"?ko uoy era","date":"Thu, 10 Aug 2017 01:41:09 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"sssss","date":"Wed, 09 Aug 2017 15:03:34 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"sssss","date":"Wed, 09 Aug 2017 15:03:33 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"sss","date":"Wed, 09 Aug 2017 14:58:56 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"12","date":"Wed, 09 Aug 2017 14:50:16 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"123","date":"Wed, 09 Aug 2017 14:45:39 GMT","username":"szy"},{"avatar":"http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif","comment":"123","date":"Wed, 09 Aug 2017 14:43:38 GMT","username":"2768"}]
     * comment_num : 21
     */

    private int comment_num;
    private List<CommentBean> comment;

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public List<CommentBean> getComment() {
        return comment;
    }

    public void setComment(List<CommentBean> comment) {
        this.comment = comment;
    }

    @Override
    public int compareTo(@NonNull Comments comments) {
        return 0;
    }

    private int calculate(String date){
        String month = date.substring(0,1);
        String day = date.substring(2,3);
        return Integer.parseInt(month) + Integer.parseInt(day);
    }



    public static class CommentBean {
        /**
         * avatar : http://7xrvvt.com1.z0.glb.clouddn.com/shakedog.gif
         * comment : 123
         * date : Fri, 11 Aug 2017 14:06:09 GMT
         * username : 2768
         */

        private String avatar;
        private String comment;
        private String date;
        private String username;

        public CommentBean(String avatar,String comment,String date,String username){
            this.avatar = avatar;
            this.date = date;
            this.username = username;
            this.comment = comment;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }


    }
}
