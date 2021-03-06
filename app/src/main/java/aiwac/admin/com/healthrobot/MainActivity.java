package aiwac.admin.com.healthrobot;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
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
import aiwac.admin.com.healthrobot.activity.medicalexam.MedicalExamDetailActivity;
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
import zuo.biao.library.base.BaseActivity;


public class MainActivity extends BaseActivity {

    private ImageButton btn_voicechat;
    private ArrayList<ExamInfoForCarousel> examInfoForCarousels;
    private ImageView btnNotification;
    //滚动动画用
    private ImageSwitcher mImageSwitcher;
    private int[] imgIds;
    private LinearLayout linearLayout;
    private ImageView[] tips;
    /**
     * 当前选中的图片id序号
     */
    private int mCurrentPosition=0;
    /**
     * 按下点的X坐标
     */
    private float downX;

    /**
     * 设置循环播放的图片
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageSwitcher = findViewById(R.id.imageSwitcher1);
        linearLayout = findViewById(R.id.viewGroup);

        //申请权限
        initPermission();

        //隐藏标题栏
        /*ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }*/
        //每天中午十二点提醒用户拍照测肤
        alarm(MainActivity.this);

        initView();

        //异步加载，查询三条体检推荐消息
        LoadThreeExamAsyc loadThreeExamAsyc = new LoadThreeExamAsyc();
        loadThreeExamAsyc.execute();


        //测肤功能测试
        ImageButton sendButton = (ImageButton) findViewById(R.id.skin);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SkinMainActivity.class);
                startActivity(intent);
            }
        });

        //闹钟功能测试
        Button alarmButton = (Button) findViewById(R.id.test_alarm);
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(MainActivity.this, VoiceRegisterActivity.class);
                startActivity(intent);
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
        btnNotification = findViewById(R.id.btn_notification);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getTo().equals("MainActivity")){
            //更新体检推荐
            ExamInfoForCarousel examInfoForCarousel = JsonUtil.jsonToExamInfoForCarousel(messageEvent.getMessage());
            examInfoForCarousels.remove(0);
            examInfoForCarousels.add(examInfoForCarousel);
            //通知界面更改
            setImageSwitcherByNetwork();
        }else if(messageEvent.getTo().equals("NewNotificationComing")){
            btnNotification.setBackgroundResource(R.drawable.message2);
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


    @Override
    public void initView(){

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
        mImageSwitcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();//downX为全局变量：手指按下时x坐标
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        float upX = event.getX(); //upX: 手指抬起时x坐标
                        if (upX > downX) {   //向右滑
                            if (mCurrentPosition == 0) {
                                mCurrentPosition = imgIds.length - 1;
                            } else {
                                --mCurrentPosition;
                            }
                            mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_in_left));
                            mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.slide_out_right));
                        }
                        if (upX < downX) {    //向左滑
                            if (mCurrentPosition == imgIds.length - 1) {
                                mCurrentPosition = 0;
                            } else {
                                ++mCurrentPosition;
                            }
                            mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_right));
                            mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_left));
                        }
                        Drawable drawable = new BitmapDrawable(examInfoForCarousels.get(mCurrentPosition).getCover());
                        mImageSwitcher.setImageDrawable(drawable);
                        setImageBackground(mCurrentPosition);
                        break;
                }
                return true;//返回true才能touch有效
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


    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }

    /**
     * 从网络获取图片
     */
    public void setImageSwitcherByNetwork(){
        Drawable drawable = new BitmapDrawable(examInfoForCarousels.get(mCurrentPosition).getCover());
        mImageSwitcher.setImageDrawable(drawable);
        setImageBackground(mCurrentPosition);
        //
        // 按时切换图片
        mImageSwitcher.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mCurrentPosition >= imgIds.length-1){
                    mCurrentPosition = 0;
                }else{
                    mCurrentPosition++;
                }
                Drawable drawable = new BitmapDrawable(examInfoForCarousels.get(mCurrentPosition).getCover());
                mImageSwitcher.setImageDrawable(drawable);
                setImageBackground(mCurrentPosition);
                mImageSwitcher.postDelayed(this, 5000);
            }
        }, 5000);
        mImageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向后台查询体检推荐具体信息
                String examId = examInfoForCarousels.get(mCurrentPosition).getExamID();
                /*BaseEntity baseEntity = new BaseEntity();
                baseEntity.setBusinessType("0008");*/
                //sendJson(JsonUtil.requestMedicalExamDetailString(Integer.parseInt(examId)));

                Intent intent = new Intent(MainActivity.this, MedicalExamDetailActivity.class);
                intent.putExtra(Constant.WEBSOCKET_EXAM_ID,Integer.parseInt(examId));
                startActivity(intent);
                //跳转到体检推荐详情界面********************

            }
        });
    }


    /**
     * 设置默认切换动画
     */
    public void setDefaultImageSwitcher(){
        // 图片 index 布局的生成
        mImageSwitcher.setImageResource(imgIds[mCurrentPosition]);
        setImageBackground(mCurrentPosition);
        // 按时切换图片
        mImageSwitcher.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mCurrentPosition >= imgIds.length-1){
                    mCurrentPosition = 0;
                }else{
                    mCurrentPosition++;
                }
                mImageSwitcher.setImageResource(imgIds[mCurrentPosition]);
                setImageBackground(mCurrentPosition);
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




    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.ACCESS_WIFI_STATE
                /*,
                Manifest.permission.READ_EXTERNAL_STORAGE*/
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm :permissions){
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()){
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。

    }
}