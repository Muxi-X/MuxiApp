package com.muxistudio.muxiio.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by kolibreath on 17-8-29.
 */

public class KeyBoardUtils {

    public static void hideKeyBoard(Activity activity){
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if(view==null){
            view = new View(activity);
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
