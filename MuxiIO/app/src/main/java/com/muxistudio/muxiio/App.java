package com.muxistudio.muxiio;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kolibreath on 17-7-23.
 */

public class App extends Application {

    /**
     *{// this is an accessible account
     "email": "734780178@qq.com",
     "username": "Android",
     "password": "12345678"
     }
     */
    public static Context sContext;
    public static Map<Integer,String > sUserId2Name = new HashMap<>();

    public static int[] sAndroiGroup = {5,18,20,28,8};
    public static int[] sBackEndGroup = {1,12,22,32,43,45,54};
    public static int[] sFrontEndGroup = {4,14,16,19,21,41};
    public static int[] sDesignGroup = {6,11,27,29,30,31,34,39,46,51,52,53,55,56};
    public static int[] sProductGroup = {10,24,26,47,48,49,50};
    private static Object[] stemp =  sUserId2Name.keySet().toArray();
    public static int[] sAll = new int[stemp.length];
    private void initAllArray(){
        int index = 0;
        for(int i=0;i<stemp.length;i++){
            sAll[index++] = (int) stemp[i];
        }
    }

    //add this to the map manually
    private void addToMap() {
        sUserId2Name.put(1, "kasheemlew");
        sUserId2Name.put(4, "zindex");
        sUserId2Name.put(5, "kolibreath");
        sUserId2Name.put(6, "Zoi IV");
        sUserId2Name.put(8,"夏玮蔚");
        sUserId2Name.put(9, "2bab");
        sUserId2Name.put(10, "李旭辉");
        sUserId2Name.put(11, "wakako");
        sUserId2Name.put(12, "anyaliang");
        sUserId2Name.put(14,"wwyqinaqian");
        sUserId2Name.put(16,"Amanda");
        sUserId2Name.put(17,"房子");
        sUserId2Name.put(18,"ybao");
        sUserId2Name.put(19,"徐雅静");
        sUserId2Name.put(20,"底衫不二");
        sUserId2Name.put(21,"vueact");
        sUserId2Name.put(22,"neo1218");
        sUserId2Name.put(24,"pmwwwwwy");
        sUserId2Name.put(26,"houhou");
        sUserId2Name.put(27,"张贝贝");
        sUserId2Name.put(28,"mana-cyan");
        sUserId2Name.put(29,"梁不换");
        sUserId2Name.put(30,"胡薇");
        sUserId2Name.put(31,"韩阿斗");
        sUserId2Name.put(32,"rose");
        sUserId2Name.put(34,"Ukiko");
        sUserId2Name.put(39, "王露晨");
        sUserId2Name.put(41, "Cruyun");
        sUserId2Name.put(43, "lyy");
        sUserId2Name.put(45, "HumbertZhang");
        sUserId2Name.put(46, "starry");
        sUserId2Name.put(47, "陈雅澜");
        sUserId2Name.put(48, "杨涵");
        sUserId2Name.put(49, "王涵");
        sUserId2Name.put(50, "刘梓轩");
        sUserId2Name.put(51, "怂蛋");
        sUserId2Name.put(52, "吴彬");
        sUserId2Name.put(53, "卫诗琪");
        sUserId2Name.put(54, "阿超");
        sUserId2Name.put(55, "李子琛");
        sUserId2Name.put(56, "糯米团");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();

        addToMap();
        initAllArray();

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setAppVersion("1.0.0"); //App的版本
        strategy.setAppPackageName("com.muxistudio.muxiio");

        CrashReport.initCrashReport(getApplicationContext(),"196f2c0be0", true);
        CrashReport.initCrashReport(getApplicationContext());
    }

    public static Context getContext(){
        return sContext;
    }
}
