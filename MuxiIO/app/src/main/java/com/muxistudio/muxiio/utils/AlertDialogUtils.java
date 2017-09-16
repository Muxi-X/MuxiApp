package com.muxistudio.muxiio.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by kolibreath on 17-8-10.
 */

public class AlertDialogUtils {

    public static void showAlert(Context context, String title, String positive
    , String negative, DialogInterface.OnClickListener positiveListener
    , DialogInterface.OnClickListener negativeListener){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog
                //.setCancelable(false).
                .setMessage(title)
                .setPositiveButton(positive, positiveListener)
                .setNegativeButton(negative, negativeListener)
                .create();

        alertDialog.show();


    }

}
