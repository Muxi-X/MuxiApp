package com.muxistudio.muxiio.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.muxistudio.muxiio.App;
import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.ui.ShareActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by kolibreath on 17-7-24.
 */

public class NetworkUtils {

    public static final int  WIFI_CONNECTED = 2;
    public static final int MOBILE_DATA_CONNECTED = 1;
    public static final int UNCONNECTED = 0;
    public static ImageView tempView ;
    public static Context sContext = App.getContext();
    public static int getNetworkStatus(){
        ConnectivityManager manager = (ConnectivityManager) sContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager!=null){
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if(networkInfo!=null&&networkInfo.isConnected()) {
                if (networkInfo.getType() == manager.TYPE_WIFI) {
                    return WIFI_CONNECTED;
                } else if (networkInfo.getType() == manager.TYPE_MOBILE) {
                    return MOBILE_DATA_CONNECTED;
                } else {
                    return UNCONNECTED;
                }
            }
        }
        return UNCONNECTED;
    }

    //give a hint
    public static void makeToast( int sig){
        switch (sig){
            case MOBILE_DATA_CONNECTED:
                Toast.makeText(sContext,"你正在使用数据联网",Toast.LENGTH_SHORT).show();
                break;
            case UNCONNECTED:
                Toast.makeText(sContext,"没有连接网络请重试",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //先会从缓存中读取bitmap 如果是null的话发送请求
    //如果请求失败，使用默认icon
    //为什么要使用这个奇怪的方法缓存Bitmap—>1.由于图片的原因，不一定都可以view.getDrawingCache()返回非空
    //2.因此使用了一个private方法在canvas之中绘制bitmap
    //3.由于CircleImageView的特殊性，上下没有对齐，所以Bitmap会有莫名其妙的黑色阴影，于是先缓存在一个无关的imageview
    //中然后在setBitmap
    public static void initPicture(final Context context, String url
            , final ImageView imageView, final RelativeLayout upLoadingHints) {
        Bitmap bitmapCache = CacheUtils.readBitmapCache(CacheUtils.BITMAP_KEY);
        if(bitmapCache!=null){
            imageView.setImageBitmap(bitmapCache);
        }else {
            int status = NetworkUtils.getNetworkStatus();
            if(status==NetworkUtils.UNCONNECTED){
                NetworkUtils.makeToast(status);
                upLoadingHints.setVisibility(View.GONE);
                    return;
            }
            Picasso picasso;
            OkHttpClient okHttpClient;
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build();
            picasso = new Picasso.Builder(sContext)
                    .downloader(new OkHttp3Downloader(okHttpClient))
                    .build();
            if (context.getClass() == ShareActivity.class){
                tempView = (ImageView) ((Activity) context).findViewById(R.id.testimage);
            }
            picasso.with(context).load(url)
                    .into(tempView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap temp = convertViewToBitmap(tempView);
                            CacheUtils.saveBitmapCache(CacheUtils.BITMAP_KEY,temp);
                            imageView.setImageBitmap(temp);
                            upLoadingHints.setVisibility(View.GONE);
                        }
                        @Override
                        public void onError() {
                            upLoadingHints.setVisibility(View.GONE);
                            Bitmap bitmap = BitmapFactory.decodeResource(App.getContext().getResources(),
                                    R.mipmap.muxiicon);
                            imageView.setImageBitmap(bitmap);
                            ToastUtils.showShort("头像加载失败，使用默认头像");
                        }
                    });
        }
    }
    public static void downloadingTimeout(Context context,Throwable t, View view){
        if(t instanceof SocketException){
            view.setVisibility(View.GONE);
            ToastUtils.makeToast(context,"与服务器端丢失连接...",2000);
        }
    }

    private static Bitmap convertViewToBitmap(View view){
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

}
