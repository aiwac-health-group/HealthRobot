package aiwac.admin.com.healthrobot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import aiwac.admin.com.healthrobot.activity.loginandregister.LoginActivity;
import aiwac.admin.com.healthrobot.activity.voicechat.WaitChatActivity;
import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.UserData;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.service.WebSocketService;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.ActivityUtil;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import aiwac.admin.com.healthrobot.utils.StringUtil;

public class MainActivity extends AppCompatActivity {

    private Button btn_voicechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    //判断用户是否登录，如果没有登录，则跳转到登录界面
    @Override
    protected void onResume() {
        super.onResume();

        LogUtil.d( Constant.USER_IS_LOGIN);
        if(!StringUtil.isValidate(UserData.getUserData().getNumber())){
            //用户没有登录, 跳转到登录界面
            ActivityUtil.skipActivity(MainActivity.this, LoginActivity.class);
        }


        //开启服务，创建websocket连接
        Intent intent = new Intent(this, WebSocketService.class);
        intent.putExtra(Constant.SERVICE_TIMER_TYPE, Constant.SERVICE_TIMER_TYPE_WEBSOCKET);
        startService(intent);

    }


    public void sendJson(final String json){
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
    }



}
