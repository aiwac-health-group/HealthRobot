package aiwac.admin.com.healthrobot.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;

import aiwac.admin.com.healthrobot.bean.TimerEntity;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.TimerSqliteHelper;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;

/**     用于执行定时任务
 * Created by luwang on 2017/10/23.
 */

public class TimerService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d( "TimerService" + Constant.SERVICE_CREATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //在新的线程中重发哪些没有收到确认的定时任务
        resendTimerReminder();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d( "TimerService" + Constant.SERVICE_DESTROY);
    }

    //在新的线程中重发哪些没有收到确认的定时任务
    private void resendTimerReminder(){
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try {
                    TimerSqliteHelper timerSqliteHelper = new TimerSqliteHelper(TimerService.this);
                    List<TimerEntity> timerEntities = timerSqliteHelper.findAllUncommitEntity();
                    for(TimerEntity timerEntity : timerEntities){
                        if(!timerEntity.isOpen())
                            timerEntity.setOperationType(Constant.TIMER_OPERATIONTYPE_DELETE);
                        String json = JsonUtil.timerEntity2Json(timerEntity);
                        WebSocketApplication.getWebSocketApplication().send(json);
                        LogUtil.d( "resendTimerReminder : " + timerEntity);
                    }
                }catch (Exception e){
                    LogUtil.d( "resendTimerReminder : " + e.getMessage());
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
