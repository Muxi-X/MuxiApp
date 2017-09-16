package com.muxistudio.muxiio.data;

import android.support.annotation.NonNull;

/**
 * Created by kolibreath on 17-8-7.
 */

public class Message implements Comparable<Message>{

    public String messengerAvatar;
    public String messengerDate;
    public String messengerUsername;
    public String messengerTitle;
    public String messengerContent;
    public int shareId;
    public Message(String messengerAvatar,
                   String messengerDate,String messengerUsername,
                   String messengerTitle,
                   String messengerContent,
                   int shareId){
        this.messengerAvatar = messengerAvatar;
        this.messengerDate = messengerDate;
        this.messengerUsername = messengerUsername;
        this.messengerTitle = messengerTitle;
        this.messengerContent = messengerContent;
        this.shareId = shareId;
    }

    @Override
    public int compareTo(@NonNull Message message) {
        return (- this.calculate(this.messengerDate) + message.calculate(message.messengerDate));
    }

    private int calculate(String date){
        String month = date.substring(0,1);
        String day = date.substring(3,4);
        return Integer.parseInt(month) + Integer.parseInt(day);
    }
}
