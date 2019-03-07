package aiwac.admin.com.healthrobot.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import aiwac.admin.com.healthrobot.utils.StringUtil;

/**     用于WebSocket相关操作的后台服务，如开启连接，关闭连接等
 * Created by luwang on 2017/10/23.
 */

public class WebSocketService extends Service{

    //绑定service留给后续功能使用
    private PicVoiceBinder picVoiceBinder = new PicVoiceBinder();

    private WebSocketApplication webSocketApplication;

    private Intent timerIntent;



    @Override
    public void onCreate() {
        super.onCreate();

        //获取WebSocketApplication对象，保存一些必要的变量
        webSocketApplication = WebSocketApplication.getWebSocketApplication();

        LogUtil.d( "WebSocketService" + Constant.SERVICE_CREATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //如果用户清理了应用，就没有intent了，当时我们有一个定时的任务，会重新启动该服务，此时会有空指针异常需要处理
        //LogUtil.d( "intent : " + intent);
        if(intent == null) {
            LogUtil.d( Constant.APLLICATION_CLEAN);
            LogUtil.d( Constant.SERVICE_STOP_SELF);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        //如果没有用户登录，停止该任务，如果有继续执行
        if(!StringUtil.isValidate(webSocketApplication.getUserNumber()) || !webSocketApplication.isNetwork()) {
            LogUtil.d( Constant.SERVICE_STOP_SELF);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        //做具体的事， 如：关闭连接，重新连接等

        //开启一个定时任务，实时监测WebSocket连接有没有断开,如果断开，重新连接
        int timerType = intent.getIntExtra(Constant.SERVICE_TIMER_TYPE, 0);
        switch (timerType){
            case 1:
                //如果是1，做具体的事

                //检测websocket连接
                webSocketApplication.startWebSocketConnection(this);

                //开启定时任务
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                int anMinute = 60 * 1000; //10分钟，单位为毫秒数
                long triggerAtTime = SystemClock.elapsedRealtime() + anMinute;
                Intent timeIntent = new Intent(this, WebSocketService.class);
                timeIntent.putExtra(Constant.SERVICE_TIMER_TYPE, Constant.SERVICE_TIMER_TYPE_WEBSOCKET);
                PendingIntent pendingIntent = PendingIntent.getService(this, 0, timeIntent, 0);

                //开启一分钟检测一次的定时任务
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
                LogUtil.d( Constant.WEBSOCKET_SERVER_TIMER_WEBSOCKET);

                //开启定时任务服务，保证定时任务的可靠性
                startService(timerIntent);

                break;
            default:
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d( "WebSocketService" + Constant.SERVICE_BINDER);

        return picVoiceBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d( "WebSocketService" + Constant.SERVICE_DESTROY);

        //关闭连接资源
        WebSocketApplication.getWebSocketApplication().setNull();

        //销毁时关闭timerService
        stopService(timerIntent);

    }



    public class PicVoiceBinder extends Binder{


    }



}
