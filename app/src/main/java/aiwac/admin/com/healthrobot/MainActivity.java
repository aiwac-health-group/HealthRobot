package aiwac.admin.com.healthrobot;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import aiwac.admin.com.healthrobot.activity.SkinMainActivity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.Calendar;

import aiwac.admin.com.healthrobot.activity.voicechat.WaitChatActivity;
import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.receiver.AlarmReceiver;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.ActivityUtil;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;

import static android.app.AlarmManager.RTC_WAKEUP;


public class MainActivity extends AppCompatActivity {

    private Button btn_voicechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarm(MainActivity.this);


        //测肤功能测试
        Button sendButton = (Button) findViewById(R.id.skin);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SkinMainActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "按按钮",
                        Toast.LENGTH_SHORT).show();
            }
        });


        btn_voicechat = findViewById(R.id.btn_voicechat);
        btn_voicechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setCancelable(true)
                        .setTitle("即将与医生建立语音通话?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //发送请求
                                BaseEntity baseEntity = new BaseEntity();
                                baseEntity.setBusinessType(Constant.WEBSOCKET_VOICECHAT_BUSSINESSTYPE_CODE);
                                String json = JsonUtil.baseEntity2Json(baseEntity);
                                sendJson(json);
                                ActivityUtil.skipActivity(MainActivity.this, WaitChatActivity.class);
                            }
                        }
                ).show();

            }
        });

    }

    public void sendJson(final String json) {
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try {
                    WebSocketApplication.getWebSocketApplication().send(json);
                } catch (Exception e) {
                    LogUtil.d(e.getMessage());
                    //其他异常处理
                }
            }
        });
    }

    /**
     *
     * 每天十二点弹出alarmDialog，提醒用户测肤拍照
     * @param
     */
    public static void alarm(Context context){
        int INTERVAL = 1000 * 60 * 60 * 24;// 24h

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),INTERVAL,pendingIntent);

//        //10s 一次
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setWindow(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),10000,pendingIntent);
//        } else {
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),10000,pendingIntent);
//        }

        //每天一次
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }

    }




}