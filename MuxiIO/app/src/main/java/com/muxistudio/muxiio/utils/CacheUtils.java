package com.muxistudio.muxiio.utils;

import android.graphics.Bitmap;

import com.muxistudio.muxiio.App;
import com.muxistudio.muxiio.model.ACache;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kolibreath on 17-9-9.
 */

public class CacheUtils {
    //这个key是专门的储存share object的keyH
    public static final String SHARE_TEMPLIST_KEY = "MuxistudioTempList";
    public static final String SHARE_LIST_KEY = "MuxistudioShareList";
    public static final String BITMAP_KEY = "MuxistudioBitmap";
    public static ACache acache = ACache.get(App.getContext());
    public static <T> void removeItem(List<T> list,String key,int index ){
        list.remove(index);
        acache.remove(key+index);
    }
    public static <T>void clearItems(String key,List<T> list){
        for(int i=0;i<list.size();i++){
            list.remove(i);
            acache.remove(key+i);
        }
        acache.clear();
    }
    public static <T>void storeListCache(String key,List<T>list){
        for(int i=0;i<list.size();i++){
            acache.put(key+i, (Serializable) list.get(i));
        }
    }
    public static<T> List<T> readListCache(String key,int length) throws FileNotFoundException {
        List<T> list = new LinkedList<>();
        for(int i=0;i<length;i++){
            T t =(T) acache.getAsObject(key+i);
            if(t!=null){
                list.add(t);
            }
        }
        return list;
    }
    public static void storeBitmapCache(String key,Bitmap bitmap){
        acache.put(key,bitmap);
    }
    public static Bitmap readBitmapCache(String key){
        Bitmap bitmap = acache.getAsBitmap(key);
        if(bitmap==null)
            return null;
        return bitmap;
    }
    public static void removeBitmapCache(String key,Bitmap bitmap){
        acache.remove(key);
    }
}
