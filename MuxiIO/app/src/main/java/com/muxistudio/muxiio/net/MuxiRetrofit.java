package com.muxistudio.muxiio.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kolibreath on 17-9-7.
 */

public class MuxiRetrofit {

    private final IRetrofit mIRetrofit;


    //需要在使用之前调用setBaseUrl
    public MuxiRetrofit(String baseUrl){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15,TimeUnit.SECONDS)
                .writeTimeout(15,TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
        mIRetrofit = retrofit.create(IRetrofit.class);
    }

    public  IRetrofit getIRetrofit(){
        return mIRetrofit;
    }
}
