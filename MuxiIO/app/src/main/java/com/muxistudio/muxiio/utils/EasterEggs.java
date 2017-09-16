package com.muxistudio.muxiio.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.muxistudio.muxiio.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kolibreath on 17-8-4.
 */

public class EasterEggs {

    /**
     * first easter egg pops up when you refresh 15 times at the share page
     */
    public static void easterEgg1(Context context, int time){
        if(time==15){
            ToastUtils.makeToast(context,"一直这样刷分享的，只有老板了吧...", Toast.LENGTH_SHORT);
        }
    }

    /**
     * second easter egg : if you tap the send button  continuously it will
     * switch to ZATANA mode
     */
    public static boolean easterEgg2(Context context, int time,
                                  Button send, MaterialEditText editText){

        boolean mode = false;
        if(time==2){
            send.setText("ZATANNA mode!");

            ToastUtils.makeToast(context,"ZATANNA mode on!",Toast.LENGTH_SHORT);

            String string = editText.getText().toString();
            char chars[] = string.toCharArray();
            String magicWords  = "";
            int length = chars.length;
            for(int i=length-1;i>=0;i--){
                magicWords += chars[i];
            }
            mode = true;
            editText.setText(magicWords);
        }

        return mode;
    }

    public static String ZATANABack(Context context,String string){
        char chars[] = string.toCharArray();
        int length = chars.length;
        String s = "";
        for(int i=length-1;i>=0;i--){
            s += chars[i];
        }
        ToastUtils.makeToast(context,"kcab ti nrut",Toast.LENGTH_SHORT);
        return s;
    }

    public static void match(Context context, String s) {
        // Here matches to "难" and "好"
        Pattern p = Pattern.compile("[\\u597d\\u96be]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        if (m.find()) {
            popPhoto(context);
        }
    }

    public static void popPhoto(Context context) {
        ImageView view1 = new ImageView(context);
        view1.setImageResource(R.drawable.ic_easter1);
        Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.addContentView(view1, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        dialog.show();
    }
}

