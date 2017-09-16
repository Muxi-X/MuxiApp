package com.muxistudio.muxiio.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kolibreath on 17-8-13.
 */

public class PictureDownloadUtils {


    private static String SDcard ;
    private static Bitmap bitmap;
    private static String FILENAME = "AVATAR";
    private static String SAVED_PATH  = Environment.getExternalStorageDirectory().getPath()
            + "/" + FILENAME + ".jpg";
    private static int SUCCESS = 200;
    public static void startDownLoad(final String imageUrl,CircleImageView imageView){

        new Runnable(){
            @Override
            public void run() {
                downLoadPicture(imageUrl);
            }
        }.run();
        //loadSavedImage(imageUrl,imageView);
    }

    public static int  downLoadPicture(String imageUrl){
        URL url = null;
        byte bytes[];

        try{
            url = new URL(imageUrl);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            InputStream in = connection.getInputStream();
            bytes = readInputBytesArray(in);
            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }catch (Exception e){
            try {
                saveJPGFile(bitmap, FILENAME);
            }catch (Exception e1){
                Log.d("failed", "downLoadPicture: "+"图片保存失败");
            }
        }
        return SUCCESS;
    }

    private static byte[] readInputBytesArray(InputStream in) throws IOException {
        int len = 0;
        byte buf[] = new byte[1024];

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //read some byte from inputStream and store it in buf
        //return how many in reads
        while((len = in.read(buf))!=-1){
            out.write(buf,0,len);
        }
        out.close();
        return out.toByteArray();
    }

    private static void saveJPGFile(Bitmap bitmap,String filename) throws IOException{
        SDcard = Environment.getExternalStorageDirectory().getPath();
        String path = SDcard + "/";

        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }

        //create a file of JPG
        File picFile = new File(path+ filename + ".jpg");

        try{
            //get it buffered
            BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(picFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
            outputStream.flush();;
            outputStream.close();
            Log.d("success", "saveJPGFile: ");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void loadSavedImage(String path, CircleImageView imageView){
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(bitmap);
            Log.d("success", "loadSavedImage: ");
        }catch (Exception e){
            Log.d("failed", "loadSavedImage: "+"读取失败");
        }
    }
}
