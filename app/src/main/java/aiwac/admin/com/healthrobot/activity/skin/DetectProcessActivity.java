package aiwac.admin.com.healthrobot.activity.skin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.SkinDetection.CommonAlexnetExtract;
import aiwac.admin.com.healthrobot.SkinDetection.FuseExtract;
import aiwac.admin.com.healthrobot.bean.SkinResult;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

public class DetectProcessActivity extends AppCompatActivity{

    private ImageView imageView;
    private TextView textView;

    Bitmap srcBitmap;
    private int count = 0;
    private float[] heitouResults;
    private float[] douResults;
    private float[] banResults;
    private float[] heiyanquanResults;
    private float[] cleanResults;
    private float[] fuseResults;
    //导航栏隐藏
    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
    //状态栏隐藏
    private void setStatusBarVisible(boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setStatusBarVisible(false);
        setContentView(R.layout.activity_detect_process);


        imageView = (ImageView) findViewById(R.id.miniPhoto);
        textView = (TextView) findViewById(R.id.processText);

        //byte[] bis = getIntent().getByteArrayExtra("miniPhoto");
        //srcBitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
        //imageView.setImageBitmap(srcBitmap);
        String mPath = getIntent().getStringExtra("photoPath");
        int mRotation=getIntent().getIntExtra("photoRoi",0);
        try {
            FileInputStream fis = new FileInputStream(new File(mPath));
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            // 因为画面预览时是旋转后显示的，所以保存的图片也需要旋转矫正
            Matrix matrix = new Matrix();
            matrix.setRotate(mRotation);
            // 创建一个新的Bitmap
            srcBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            imageView.setImageBitmap(srcBitmap);
        } catch (FileNotFoundException e) {
            Log.e("", "onCreate: FileInputStream error");
        }

        // 痘痘检测
        try {
            douResults = CommonAlexnetExtract.extractFeatureFromBitmap("mini_dou_graph.pb", srcBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(new Runnable(){
            @Override
            public void run() {
                while (true){
                    if(count>5){
                        break;
                    }
                    try{
                        count++;
                        Thread.sleep(1000);
                        mHandler.sendEmptyMessage(count);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    textView.setText("正在分析黑头严重程度...");
                    try {
                        heitouResults = CommonAlexnetExtract.extractFeatureFromBitmap("mini_heitou_graph.pb", srcBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    textView.setText("正在检测毛孔...");
                    try {
                        banResults = CommonAlexnetExtract.extractFeatureFromBitmap("mini_ban_graph.pb", srcBitmap);
                        heiyanquanResults = CommonAlexnetExtract.extractFeatureFromBitmap("mini_heiyanquan_graph.pb", srcBitmap);
                        cleanResults = CommonAlexnetExtract.extractFeatureFromBitmap("mini_clean_graph.pb", srcBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    textView.setText("正在检测肤质...");
                    // 肤色检测
                    try {
                        fuseResults = FuseExtract.extractFeatureFromBitmap("fuse_lenet_graph.pb", srcBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    textView.setText("正在生成报告...");
                    break;
                case 5:
                    //把结果上传到后台
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                SkinResult skinResult = new SkinResult();

                                skinResult.setFace(srcBitmap);

                                //一个个传
                                skinResult.setHeitouResults(heitouResults[0],heitouResults[1]);
                                skinResult.setDouResults(douResults[0],douResults[1]);
                                skinResult.setBanResults(banResults[0],banResults[1]);
                                skinResult.setFuseResults(fuseResults[0],fuseResults[1],fuseResults[2]);
//                    //直接传
//                    skinResult.setHeitouResults(heitouResults);
//                    skinResult.setDouResults(douResults);
//                    skinResult.setBanResults(banResults);
//                    skinResult.setFuseResults(fuseResults);


                                skinResult.setScore(21);
                                skinResult.setBody(getResources().getString(R.string.bodyConstitution1));
                                skinResult.setDiet(getResources().getString(R.string.food1));
                                skinResult.setMedicine(getResources().getString(R.string.medicine1));
                                skinResult.setDrug(getResources().getString(R.string.drag1));


                                skinResult.setBusinessType(Constant.WEBSOCKET_SKIN_RESULT_CODE);
                                skinResult.setUuid(UUID.randomUUID().toString());


                                String json = JsonUtil.skinResultToJson(skinResult);

                                Log.d("test",json.length()+"");
                                WebSocketApplication.getWebSocketApplication().send(json);

                            }catch (Exception e){
                                LogUtil.d(e.getMessage());
                                //其他异常处理
                            }
                        }
                    } );

                    //跳转到测肤结果页面:BeautyResultActivity

                    Intent intent = new Intent();
                    intent.putExtra("fuseResults", fuseResults);
                    intent.putExtra("douResults", douResults);
                    intent.putExtra("banResults", banResults);
                    intent.putExtra("heitouResults", heitouResults);
                    intent.putExtra("heiyanquanResults", heiyanquanResults);
                    intent.putExtra("cleanResults", cleanResults);


                    intent.setClass(DetectProcessActivity.this, BeautyResultActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);



                    break;
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        count=6;
        finish();
    }
}
