package com.muxistudio.muxiio.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public class CloseAppUtils {

    public static List<Activity> activityList = new ArrayList<Activity>();

    public static void exitClient(Context context)
    {
        // Close all Activity

        //SharedPreferences must be cleared
        for (int i = 0; i < activityList.size(); i++)
        {
            if (null != activityList.get(i))
            {
                activityList.get(i).finish();
            }
        }

        ActivityManager activityMgr= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE );
        activityMgr.restartPackage(context.getPackageName());

        System.exit(0);
    }
}
