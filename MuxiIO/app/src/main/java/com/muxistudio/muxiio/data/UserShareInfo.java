package com.muxistudio.muxiio.data;

import android.support.annotation.NonNull;

/**
 * Created by kolibreath on 17-8-27.
 */

public class UserShareInfo implements Comparable{

    public int userShareId;
    public int userShareTotal;

    public UserShareInfo(int userShareId,int userShareTotal){
        this.userShareId = userShareId;
        this.userShareTotal = userShareTotal;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return this.userShareId - ((UserShareInfo) o).userShareId;
    }
}
