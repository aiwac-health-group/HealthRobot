package aiwac.admin.com.healthrobot.activity.voicechat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.bean.MessageEvent;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;

public class WaitChatActivity extends AppCompatActivity{

    private Chronometer timer;
    private View hangup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_wait_chat);

        timer = (Chronometer) findViewById(R.id.timer);
        //设置开始计时时间
        timer.setBase(SystemClock.elapsedRealtime());
        //启动计时器
        timer.start();

        EventBus.getDefault().register(this);



        hangup = findViewById(R.id.calling_hangup);
        hangup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(WaitChatActivity.this).setCancelable(true)
                        .setTitle("是否挂断?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                timer.stop();
                                //挂断
                                BaseEntity baseEntity = new BaseEntity();
                                baseEntity.setBusinessType(Constant.WEBSOCKET_VOICECHAT_BUSSINESSTYPE_CODE);
                                final String json = JsonUtil.baseEntity2Json(baseEntity);
                                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{

                                            WebSocketApplication.getWebSocketApplication().send(json);
                                        }catch (Exception e){
                                            LogUtil.d( e.getMessage());
                                            //其他异常处理
                                        }
                                    }
                                });
                                finish();
                            }
                        }
                ).show();
            }
        });

        //为计时器绑定监听事件
        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener()
        {
            @Override
            public void onChronometerTick(Chronometer ch)
            {
                // 如果从开始计时到现在超过了180s
                if (SystemClock.elapsedRealtime() - ch.getBase() > 180 * 1000)
                {
                    ch.stop();
                    Toast.makeText(WaitChatActivity.this, "对方线路忙", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }

    /**
     * 接收websocketclientHelper发过来的json
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(MessageEvent messageEvent){

        String roomID = JsonUtil.parseByKey(messageEvent.getMessage(),"roomID");
        Intent intent = new Intent(WaitChatActivity.this, VoiceChatActivity.class);
        intent.putExtra("roomID", roomID);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(WaitChatActivity.this).setCancelable(true)
                .setTitle("是否挂断?")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        timer.stop();
                        //挂断
                        BaseEntity baseEntity = new BaseEntity();
                        baseEntity.setBusinessType(Constant.WEBSOCKET_VOICECHAT_BUSSINESSTYPE_CODE);
                        final String json = JsonUtil.baseEntity2Json(baseEntity);
                        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                            @Override
                            public void run() {
                                try{

                                    WebSocketApplication.getWebSocketApplication().send(json);
                                }catch (Exception e){
                                    LogUtil.d( e.getMessage());
                                    //其他异常处理
                                }
                            }
                        });
                        finish();
                    }
                }
        ).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}

