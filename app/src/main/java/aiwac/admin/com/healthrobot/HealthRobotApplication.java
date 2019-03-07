package aiwac.admin.com.healthrobot;

import android.content.Context;

import aiwac.admin.com.healthrobot.utils.LogUtil;
import zuo.biao.library.base.BaseApplication;

public class HealthRobotApplication extends BaseApplication {
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


