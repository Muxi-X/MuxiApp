package com.muxistudio.muxiio.utils;

import com.muxistudio.muxiio.data.SharesBean;

import java.util.List;

/**
 * Created by kolibreath on 17-9-13.
 */

public class ListUtils {
    public static<T> void add(List<T> source,List<T> target){
        for(int i=0;i<source.size();i++){
            target.add(source.get(i));
        }
    }
    public static boolean equals(List<SharesBean> list1, List<SharesBean>list2){
        int size1 = list1.size();
        int size2 = list2.size();
        if(size1!=size2)
            return false;
        for(int i=0;i<size1&&i<size2;i++){
            if(list1.get(i).getId()!=list2.get(i).getId()){
                return false;
            }
        }
        return true;
    }
}
