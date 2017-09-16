package com.muxistudio.muxiio.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.muxistudio.muxiio.R;

/**
 * Created by kolibreath on 17-8-4.
 */

public class PopupWindowUtils {
    /**
     * values here
     *
     * @param view
     */

    Context Context;

    // the width and height is wrap_content by default

    //return
    public static View showPopupWindow(final Context context, View anchorView,int resid) {


        PopupWindow popupWindow = new PopupWindow(context);
        //set content
        View view = LayoutInflater.from(context).inflate(resid, null);
        popupWindow.setContentView(view);
        //set animation
        popupWindow.setAnimationStyle(R.style.popupwindow_anim);

        //set hight and width
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        //this is a bug of popupwindow api
        popupWindow.setBackgroundDrawable(new BitmapDrawable());


        //  popupWindow.showAtLocation(anchorView, Gravity.BOTTOM,0, 0);
        popupWindow.showAsDropDown(anchorView);
        return view;

    }

}
