package aiwac.admin.com.healthrobot;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import aiwac.admin.com.healthrobot.activity.skin.AlarmActivity;
import aiwac.admin.com.healthrobot.activity.skin.SkinMainActivity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ViewSwitcher;

import java.util.Calendar;

import aiwac.admin.com.healthrobot.activity.loginandregister.LoginActivity;
import aiwac.admin.com.healthrobot.activity.speechRecog.SpeechRecogActivity;
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


public class MainActivity extends AppCompatActivity  implements ViewSwitcher.ViewFactory {

    private Button btn_voicechat;

    //滚动动画用
    private ImageSwitcher mImageSwitcher;
    private int[] imgIds;
    private LinearLayout linearLayout;
    private ImageView[] tips;
    private int curPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        //每天中午十二点提醒测肤拍照
        alarm(MainActivity.this);
        //设置
        initView();


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

        //闹钟功能测试
        Button alarmButton = (Button) findViewById(R.id.test_alarm);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
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


        //离线语音识别测试--需要在AndroidManifest.xml添加权限
        Button btnSpeechRecog=findViewById(R.id.btn_speech_rec);
        btnSpeechRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SpeechRecogActivity.class);
                startActivity(intent);
            }
        });

    }

//    //判断用户是否登录，如果没有登录，则跳转到登录界面
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        LogUtil.d( Constant.USER_IS_LOGIN);
//        if(!StringUtil.isValidate(UserData.getUserData().getNumber())){
//            //用户没有登录, 跳转到登录界面
//            ActivityUtil.skipActivity(MainActivity.this, LoginActivity.class);
//        }
//
//
//        //开启服务，创建websocket连接
//        Intent intent = new Intent(this, WebSocketService.class);
//        intent.putExtra(Constant.SERVICE_TIMER_TYPE, Constant.SERVICE_TIMER_TYPE_WEBSOCKET);
//        startService(intent);
//
//    }


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
     * 设置循环播放的图片
     *
     */
    private void initView(){

        imgIds = new int[]{R.drawable.fig1, R.drawable.fig2, R.drawable.fig3};
        mImageSwitcher = findViewById(R.id.imageSwitcher1);
        mImageSwitcher.setFactory(this);
        linearLayout = findViewById(R.id.viewGroup);

        //设置banner
        setImageSwitcher();

    }
    /**
     * 设置切换动画
     */
    private void setImageSwitcher(){
        // 图片 index 布局的生成
        tips = new ImageView[imgIds.length];
        for (int i=0; i<imgIds.length; i++){
            ImageView mImageView = new ImageView(MainActivity.this);
            tips[i] = mImageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = 3;
            layoutParams.leftMargin = 3;
            layoutParams.height = 30;
            layoutParams.width = 30;
            mImageView.setBackgroundResource(R.drawable.dot_unfocus);
            linearLayout.addView(mImageView, layoutParams);
        }

        // 设置动画
        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
        curPos=0;
        mImageSwitcher.setImageResource(imgIds[curPos]);
        setImageBackground(curPos);
        // 按时切换图片
        mImageSwitcher.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(curPos==imgIds.length-1){
                    curPos=0;
                }else{
                    curPos++;
                }
                mImageSwitcher.setImageResource(imgIds[curPos]);
                setImageBackground(curPos);
                mImageSwitcher.postDelayed(this, 1500);
            }
        }, 1500);

    }
    private void setImageBackground(int selectItems) {
        for (int i=0; i<tips.length; i++){
            if(i==selectItems){
                tips[i].setBackgroundResource(R.drawable.dot_focus);
            }else{
                tips[i].setBackgroundResource(R.drawable.dot_unfocus);
            }
        }
    }
    @Override
    public View makeView() {
        final ImageView i = new ImageView(this);
        i.setScaleType(ImageView.ScaleType.CENTER_CROP);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        return i;
    }

    /**
     *
     * 每天十二点弹出alarmDialog，提醒用户测肤拍照
     * @param
     */
    public static void alarm(Context context){
        int INTERVAL = 1000 * 60 * 60 * 24;// 24h

        Calendar calendar = Calendar.getInstance();

        Log.d("what's the time now", "alarm: "+
                calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));
        //如果超过了十二点，就明天再提醒
        if(calendar.get(Calendar.HOUR_OF_DAY)>12  ||
                (calendar.get(Calendar.HOUR_OF_DAY)==12 && calendar.get(Calendar.MINUTE)> 0))
        {
            calendar.add(Calendar.DATE,1);
        }

        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

//        //发广播
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        //打开activity
        Intent intent = new Intent(context, AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),INTERVAL,pendingIntent);

        //10s 一次
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setWindow(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),20000,pendingIntent);
//        } else {
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),20000,pendingIntent);
//        }

        //每天一次
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }

    }

    /**
     * 弹出全局弹框
     * @param context
     */
    public static void showLogoutDialog(final Context context) {



//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        /*true 代表点击空白可消失   false代表点击空白哦不可消失 */
//        builder.setCancelable(false);
//        View view = View.inflate(context, R.layout.dialog_alarm_skin, null);
//
//        Button okButton =   view.findViewById(R.id.ok_btn);
//        Button cancelButton =   view.findViewById(R.id.cancel_btn);
//
//
//        builder.setView(view);
//        final AlertDialog dialog = builder.create();
//
//        //设置弹出全局对话框，但是这句话并不能解决在android的其他手机上能弹出来（例如用户华为p10 就无法弹框）
//        // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
//
//        //只有这样才能弹框
//        if (Build.VERSION.SDK_INT>=26) {//8.0新特性
//            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
//        }else{
//            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        }
//
//
//        okButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
    }
}