package aiwac.admin.com.healthrobot;

import android.app.Application;
import android.content.Context;

import aiwac.admin.com.healthrobot.utils.LogUtil;

public class HealthRobotApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
       // LogcatHelper.getInstance(this).start();

        context = getApplicationContext();
        LogUtil.d( context.toString()+"+++++++++++++++++++++++++++++++++++++");
    }

    public static Context getContext(){
        return context;
    }
}

