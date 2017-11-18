package com.muxistudio.muxiio.data;

import android.support.annotation.NonNull;

import com.muxistudio.muxiio.utils.MyTextUtils;

import java.io.Serializable;

/**
 * Created by kolibreath on 17-11-17.
 */

public class SharesBean implements Comparable,Serializable{
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


    @Override
    public int compareTo(@NonNull Object o) {
        return MyTextUtils.getCompareBasis(this.getDate(),((SharesBean)o).getDate());
    }
}
