package com.muxistudio.muxiio.net;

/**
 * Created by kolibreath on 17-9-7.
 */

public class MuxiFactory {
    //执行网络请求的工厂类
    //Thanks to CCNU box-Android

    //public volatile static IRetrofit sIRetrofit = null;

    //获取初始化完成的各种不同的Retrofit和方法
    public static IRetrofit getIRetrofit(String baseUrl){
        /*
        IRetrofit iRetrofit  = sIRetrofit;
        if(iRetrofit==null){
            synchronized (MuxiFactory.class){
                iRetrofit = sIRetrofit;
                if(iRetrofit==null){
                    sIRetrofit = new MuxiRetrofit(baseUrl)
                            .getIRetrofit();
                    iRetrofit = sIRetrofit;
                }
            }
        }
        */
        IRetrofit iRetrofit = new MuxiRetrofit(baseUrl).getIRetrofit();
        return iRetrofit;
    }
}
