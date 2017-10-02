package com.muxistudio.muxiio.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.muxistudio.muxiio.App;
import com.muxistudio.muxiio.model.ACache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kolibreath on 17-9-9.
 */

public class    CacheUtils {
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
    public static <T>void saveListCache(String key, List<T>list){
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
    public static void saveBitmapCache(String key, Bitmap bitmap){
        acache.put(key,bitmap);
    }
    public static Bitmap readBitmapCache(String key){
        Bitmap bitmap = acache.getAsBitmap(key);
        if(bitmap==null)
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        int options = 100;
        while(outputStream.toByteArray().length/1024>32&&options>=50){
            outputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG,options,outputStream);
            options -= 10;
        }
        ByteArrayInputStream inputStream  = new ByteArrayInputStream
                (outputStream.toByteArray());
        bitmap = BitmapFactory.decodeStream(inputStream,null,null);
        return bitmap;
    }
    public static void removeBitmapCache(String key){
        acache.remove(key);
    }
}
