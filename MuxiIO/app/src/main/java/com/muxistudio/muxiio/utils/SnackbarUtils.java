package com.muxistudio.muxiio.utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

/**
 * Created by kolibreath on 17-7-23.
 */

public class SnackbarUtils {

    // these methods are used to verify some infos

    public static void snackUsername(View view, EditText editText,String username){
        int length = username.length();
        if(!(length>=2&&length<=20)){
            Snackbar.make(view,"用户名长度不符合要求",Snackbar.LENGTH_INDEFINITE)
                    .setAction("重新输入",new SnackbarListener(editText)).show();
        }
    }

    public static void snackPwd(View view, EditText editText,String pwd1,String pwd2){
        if(!(pwd1.equals(pwd2))){
            Snackbar.make(view,"两次输入的用户名不一致",Snackbar.LENGTH_INDEFINITE)
                    .setAction("重新输入",new SnackbarListener(editText)).show();
        }
    }

    public static class SnackbarListener implements View.OnClickListener {
        //used to clear userinput;username for example
        EditText username;

        public SnackbarListener(EditText username){
            this.username = username;
        }
        @Override
        public void onClick(View view) {
            username.setText("");
        }
    }

    public static void showShort(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showLong(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
