package com.muxistudio.muxiio.model;

import com.muxistudio.muxiio.data.SharesBean;

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


}