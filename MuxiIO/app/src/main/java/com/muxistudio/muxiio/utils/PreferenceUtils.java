package com.muxistudio.muxiio.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.muxistudio.muxiio.App;

/**
 * Created by kolibreath on 17-9-7.
 */

public class PreferenceUtils {

    public static void storeString(int key, String string){
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(App.getContext().getString(key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(App.getContext().getString(key),string);
        editor.apply();
    }

    //if the string exits if true read from memory; exit read string
    public static String readString(int key){
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(App.getContext().getString(key),
                Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(App.getContext().getString(key),"NOTHING");
        return string;
    }

    public static void storeInteger(int key,int value){
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(App.getContext().getString(key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(App.getContext().getString(key),value);
        editor.apply();
    }

    public static Integer readInteger(int key){
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(App.getContext().getString(key),
                Context.MODE_PRIVATE);
        int number = sharedPreferences.getInt(App.getContext().getString(key),-1);
        return number;
    }

}
