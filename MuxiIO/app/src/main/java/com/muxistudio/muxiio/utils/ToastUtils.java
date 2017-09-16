package com.muxistudio.muxiio.utils;

import android.content.Context;
import android.widget.Toast;

import com.muxistudio.muxiio.App;

/**
 * Created by kolibreath on 17-7-23.
 */

public class ToastUtils {
    public static void makeToast(Context context,String message,int duration){
        Toast.makeText(context,message,duration).show();
    }
    public static void showShort(String message){
        Toast.makeText(App.getContext(),message,Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String message){
        Toast.makeText(App.getContext(),message,Toast.LENGTH_LONG).show();
    }

    public static void showSpecificDuration(String message,int duration){
        Toast.makeText(App.getContext(),message,duration).show();
    }

}
