package aiwac.admin.com.healthrobot;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;

import aiwac.admin.com.healthrobot.activity.lecture.LectureActivtiy;
import aiwac.admin.com.healthrobot.activity.medicalexam.MedicalExamMenuActivity;
import aiwac.admin.com.healthrobot.activity.medicalexam.MedicalExamRecommendActivity;
import aiwac.admin.com.healthrobot.activity.notification.NotificationActivity;
import aiwac.admin.com.healthrobot.activity.setting.SettingActivity;
import aiwac.admin.com.healthrobot.activity.skin.AlarmActivity;
import aiwac.admin.com.healthrobot.activity.skin.SkinMainActivity;
import aiwac.admin.com.healthrobot.activity.speechrecog.SpeechRecogActivity;
import aiwac.admin.com.healthrobot.activity.voicechat.WaitChatActivity;
import aiwac.admin.com.healthrobot.activity.voiceregister.VoiceRegisterActivity;
import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.bean.ExamInfoForCarousel;
import aiwac.admin.com.healthrobot.bean.MessageEvent;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.ActivityUtil;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;


public class MainActivity extends AppCompatActivity {

    private ImageButton btn_voicechat;
    private ArrayList<ExamInfoForCarousel> examInfoForCarousels;

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

        mImageSwitcher = findViewById(R.id.imageSwitcher1);
        linearLayout = findViewById(R.id.viewGroup);

        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        //每天中午十二点提醒用户拍照测肤
        alarm(MainActivity.this);

        initView();

        //异步加载，查询三条体检推荐消息
        /*LoadThreeExamAsyc loadThreeExamAsyc = new LoadThreeExamAsyc();
        loadThreeExamAsyc.execute();*/


        //测肤功能测试
        ImageButton sendButton = (ImageButton) findViewById(R.id.skin);
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

        //个人中心
        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        //语音挂号
        findViewById(R.id.voice_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VoiceRegisterActivity.class);
                startActivity(intent);
            }
        });

        //健康讲座
        findViewById(R.id.start_lecture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LectureActivtiy.class);
                startActivity(intent);
            }
        });


        //在线问诊
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
        Button btnSpeechRecog = findViewById(R.id.btn_speech_rec);
        btnSpeechRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SpeechRecogActivity.class);
                startActivity(intent);
            }
        });

        //体检推荐测试
        ImageButton btnMedicalExamRecommand = findViewById(R.id.btn_medical_exam_recommand);
        btnMedicalExamRecommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MedicalExamRecommendActivity.class);
                startActivity(intent);
            }
        });

        //消息通知测试
        ImageView btnNotification = findViewById(R.id.btn_notification);
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        EventBus.getDefault().register(this);

    }
//
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
//
//    }




    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getTo().equals("MainActivity")){
            //更新体检推荐
            ExamInfoForCarousel examInfoForCarousel = JsonUtil.jsonToExamInfoForCarousel(messageEvent.getMessage());
            examInfoForCarousels.remove(0);
            examInfoForCarousels.add(examInfoForCarousel);
            //通知界面更改
            setImageSwitcherByNetwork();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
     * 设置循环播放的图片
     *
     */
    private void initView(){

        imgIds = new int[]{R.drawable.fig1, R.drawable.fig2, R.drawable.fig3};
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                final ImageView i = new ImageView(MainActivity.this);
                i.setScaleType(ImageView.ScaleType.CENTER_CROP);
                i.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                return i;
            }
        });
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

    }

    /**
     * 从网络获取图片
     */
    public void setImageSwitcherByNetwork(){
        curPos=0;
        Drawable drawable = new BitmapDrawable(examInfoForCarousels.get(curPos).getCover());
        mImageSwitcher.setImageDrawable(drawable);
        setImageBackground(curPos);
        //
        // 按时切换图片
        mImageSwitcher.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(curPos==imgIds.length-1){
                    curPos=0;
                }else{
                    curPos++;
                }
                Drawable drawable = new BitmapDrawable(examInfoForCarousels.get(curPos).getCover());
                mImageSwitcher.setImageDrawable(drawable);
                setImageBackground(curPos);
                mImageSwitcher.postDelayed(this, 1500);
            }
        }, 1500);
        mImageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向后台查询体检推荐具体信息
                String examId = examInfoForCarousels.get(curPos).getExamID();
                BaseEntity baseEntity = new BaseEntity();
                baseEntity.setBusinessType("0008");
                sendJson(JsonUtil.baseEntity2Json(baseEntity));
                //跳转到体检推荐详情界面****************************************************************

            }
        });
    }


    /**
     * 设置默认切换动画
     */
    public void setDefaultImageSwitcher(){
        // 图片 index 布局的生成

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



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if((boolean)msg.obj){
                //图片获取成功
                setImageSwitcherByNetwork();
            }else{

                //图片获取失败，显示默认图片
                setDefaultImageSwitcher();
            }

        }
    };




    class LoadThreeExamAsyc extends AsyncTask<Void, Void, Boolean> {
        private AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = View.inflate(MainActivity.this, R.layout.activity_progress, null);
            builder.setIcon(R.drawable.aiwac);
            builder.setTitle(Constant.WEBSOCKET_BUSINESS_DOWNLOAD_INFO);
            builder.setView(view);  //必须使用view加载，如果使用R.layout设置则无法改变bar的进度

            dialog = builder.create();
            dialog.setCancelable(false);

            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                BaseEntity baseEntity = new BaseEntity();
                baseEntity.setBusinessType(Constant.WEBSOCKET_THREE_EXAM_BUSSINESSTYPE_CODE);
                WebSocketApplication.getWebSocketApplication().send(JsonUtil.baseEntity2Json(baseEntity));
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(500);
                    examInfoForCarousels = WebSocketApplication.getWebSocketApplication().getExamInfoForCarousels();
                    if (examInfoForCarousels!=null) {
                        return true;
                    }
                }
            } catch (Exception e) {
                LogUtil.d( e.getMessage());
                e.printStackTrace();
            }

            return false;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.cancel();

            if (aBoolean) {   //加载轮播
                //获取控件，添加图片，监听图片点击事件
                //设置滚动动画
                Message message = new Message();
                message.obj = true;
                handler.sendMessage(message);
            } else { // 失败， 返回主界面
                Message message = new Message();
                message.obj = false;
                handler.sendMessage(message);
                Toast.makeText(MainActivity.this, "当前网络不可用，请检查你的网络设置", Toast.LENGTH_LONG).show();
            }
        }
    }
}