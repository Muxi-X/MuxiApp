package com.muxistudio.muxiio.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by kolibreath on 17-8-15.
 */

public class CameraUtils {

    //request code
    public  static final int TAKE_PHOTO = 1;
    public  static final int OPEN_ALBUM = 2;

    //<provider> and content provider authority
    private static String authority = "com.muxistuido.muxiIo";

    //select "open camera"
    public static Uri getImageUri(Context context){
        //create file to store the image
        Uri imageUri;
        File outPutImage = new File(context.getExternalCacheDir(),
        System.currentTimeMillis() +"muxiApp.jpg");

        try{
            if(outPutImage.exists()){
                outPutImage.delete();
            }
        }catch (Exception e){
            ToastUtils.makeToast(context,"存储相片失败",2000);
        }
        if(Build.VERSION.SDK_INT>=24){
            //wrap for safe
            imageUri = FileProvider.getUriForFile(context,authority,outPutImage);
        }else{
            imageUri = Uri.fromFile(outPutImage);
        }
        return imageUri;
    }


    public static void display(Context context,Uri imgUri, ImageView view) throws FileNotFoundException {
        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(imgUri));
        view.setImageBitmap(bitmap);
    }

    public static void display(String imagePath,ImageView imageView){
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
    }

    @TargetApi(19)
    public static String  handlImageOnKitKat(Context context, Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(context,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "="+ id;
                imagePath = getImagePath(context,uri,selection);
            }else  if("com.android.providers.downloads.documents"
                    .equals(uri.getAuthority())){
                Uri contentUri = ContentUris
                        .withAppendedId(Uri.parse("content://downloads/public_downloads")
                                ,Long.valueOf(docId));
                imagePath = getImagePath(context,contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(context,uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(context,uri,null);
        }
        return imagePath;
  }

    private static String getImagePath(Context context, Uri uri,String selection){
        String path = null;
        Cursor cursor =  context.getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex
                        (MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
