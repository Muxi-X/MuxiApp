package com.muxistudio.muxiio.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.muxistudio.muxiio.App;
import com.muxistudio.muxiio.R;
import com.muxistudio.muxiio.ui.ShareActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

    public static void initPicture(final Context context, String url
            , final ImageView imageView, final RelativeLayout upLoadingHints) {
        Bitmap bitmapCache = CacheUtils.readBitmapCache(CacheUtils.BITMAP_KEY);
        if(bitmapCache!=null){
            imageView.setImageBitmap(bitmapCache);
            upLoadingHints.setVisibility(View.GONE);
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

            }
            picasso.with(context).load(url)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Bitmap temp = bitmap;
                            imageView.setImageBitmap(temp);
                            CacheUtils.saveBitmapCache(CacheUtils.BITMAP_KEY,temp);
                            Log.d("bitmap", "onBitmapLoaded: "+ CacheUtils.readBitmapCache(CacheUtils
                            .BITMAP_KEY));
                            upLoadingHints.setVisibility(View.GONE);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            upLoadingHints.setVisibility(View.GONE);
                            Bitmap bitmap = BitmapFactory.decodeResource(App.getContext().getResources(),
                                    R.mipmap.muxiicon);
                            imageView.setImageBitmap(bitmap);
                            ToastUtils.showShort("头像加载失败，使用默认头像");
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

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
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    private static void clipBitmapCirCle(Bitmap bitmap){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        Canvas canvas = new Canvas(bitmap);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        int radius = bitmap.getWidth()>bitmap.getHeight()?bitmap.getHeight():bitmap.getHeight();
        canvas.drawCircle(radius,radius,radius,paint);
    }

}
