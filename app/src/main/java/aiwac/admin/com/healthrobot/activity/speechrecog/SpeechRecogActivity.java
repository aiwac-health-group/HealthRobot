package aiwac.admin.com.healthrobot.activity.speechrecog;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.service.RobotControlService;
import aiwac.admin.com.healthrobot.service.SpeechRecogService;
import aiwac.admin.com.healthrobot.utils.LogUtil;


/**
 * 这个是测试activity，真正的语音识别功能在service/SpeechRecogService中
 */
public class SpeechRecogActivity extends AppCompatActivity implements  SpeechRecogService.Callback  {
    private static final String TAG="ActivityOnlineRecog";
    private Button btn_start;
    private Button btn_stop;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_recog);
        btn_start=findViewById(R.id.btn_start);
        btn_stop=findViewById(R.id.btn_stop);
        textView=findViewById(R.id.textView_result);

        initPermission();

        Intent bindIntent=new Intent(SpeechRecogActivity.this,SpeechRecogService.class);
        //以下两句是离线识别的，可以不加
        // 如果要用离线，把decoderType设置为1
        // 如果要用在线（离线只能识别定好的词），把decoderType设置为2
        bindIntent.putExtra("decoderType",1);//0--纯在线  2---离线在线都有，在线优先  其他---离在线的并行策略
        //bindIntent.putExtra("enableOffline",true);
        bindService(bindIntent,sconnection, Context.BIND_AUTO_CREATE);

        textView.setText("开始识别");

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("开始识别");
                mMyService.startRecog();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMyService!=null){
                    mMyService.stop();
                    textView.setText("结束识别");
                }
            }
        });

    }
    private SpeechRecogService mMyService=null;
    /* 绑定service监听*/
    private ServiceConnection sconnection = new ServiceConnection() {
        /*当绑定时执行*/
        public void onServiceConnected(ComponentName name, IBinder service) {  //service的onbind（）中返回值不为null才会触发
            mMyService = ((SpeechRecogService.MyBinder)service).getService();//得到该service实例
            mMyService.setCallBack(SpeechRecogActivity.this);//把回调对象传送给service
        }
        /*当异常结束service时执行，但调用unbindService()时不会触发改方法 测试的话可以在bind时使用Context.BIND_NOT_FOREGROUND  调用stopservice（）可触发*/
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sconnection);
    }


    @Override
    public void speechRecogResult(String result) {
        Log.d(TAG, "speechRecogResult: "+result);
        textView.setText(result);
        //进入机器人方向控制
        //tring subDirection=result.substring(1,2);
        //LogUtil.d("方向："+subDirection);
        //RobotControlService.getInstance().getMessage(subDirection);
    }


    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE/*,
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
