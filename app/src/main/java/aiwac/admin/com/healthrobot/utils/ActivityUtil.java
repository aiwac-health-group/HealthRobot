package aiwac.admin.com.healthrobot.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.List;
import java.util.Map;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.common.Constant;


/**     界面工具类，辅助界面完成相关功能
 * Created by Admin on 2017/10/19.
 */

public class ActivityUtil {


    public static void skipActivity(Context context, Class<?> clazz){
        skipActivity(context, clazz, null);
    }

    public static void skipActivity(Context context, Class<?> clazz, Map<String, Object> msg){
        Intent intent = new Intent();
        intent.setClass(context, clazz);

        //传递参数
        if(msg != null && msg.size() > 0 ){
            for(Map.Entry<String, Object> entry : msg.entrySet()){
                if(entry.getValue() instanceof Integer)
                    intent.putExtra(entry.getKey(), ((Integer) entry.getValue()).intValue());
                if(entry.getValue() instanceof String)
                    intent.putExtra(entry.getKey(), entry.getValue().toString());
            }
        }

        LogUtil.d(Constant.ACTIVITY_SKIP);
        context.startActivity(intent);
    }

    // 启动activity后，将之前活动栈中的activity都清空
    public static void skipActivity(Context context, Class<?> clazz, boolean isClearActivityStack){
        Intent intent = new Intent();
        intent.setClass(context, clazz);

        if(isClearActivityStack){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }else{
            skipActivity(context, clazz);
        }

        LogUtil.d(Constant.ACTIVITY_SKIP);
        context.startActivity(intent);
    }

    public static void createDialog(Context context, String message){
        createDialog(context, null, message);
    }

    public static void createDialog(Context context, String title, String message){
        createDialog(context, title, message, R.drawable.aiwac, true);
    }

    public static void createDialog(Context context, String title, String message, int icon, boolean isCancelable){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setIcon(icon);
        builder.setPositiveButton(Constant.DEFAULT_POSITIVE_BUTTON, null);
        builder.setNegativeButton(Constant.DEFAULT_NEGATIVE_BUTTON, null);

        builder.setMessage(message);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(isCancelable);

        dialog.show();
    }


    //Android 判断app是否在前台还是在后台运行，直接看代码，可直接使用。  还没有测试该方法
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace =" + appProcess.importance
                                    + ",context.getClass().getName()=" + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 方法描述：判断某一Service是否正在运行
     *
     * @param context     上下文
     * @param serviceName Service的全路径： 包名 + service的类名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
            if (serviceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }


}
