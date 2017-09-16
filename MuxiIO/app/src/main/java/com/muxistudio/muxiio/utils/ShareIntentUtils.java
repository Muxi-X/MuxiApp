package com.muxistudio.muxiio.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class ShareIntentUtils {

    public static String sharingIntent(Context context, Intent intent){
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                return handleSendText(intent);
            } else if (type.startsWith("image/")) {
                return handleSendImage(context, intent);
            }
        }
        return "";
    }

    private static String handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            return sharedText;
        }
        return "";
    }
    private static String handleSendImage(Context context, Intent intent) {
        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            if(imageUri.getPath().endsWith(".jpg") || imageUri.getPath().endsWith(".png")){
                return imageUri.getPath();
            }
            return getRealPathFromURI(context, imageUri);
        }
        return "";
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
