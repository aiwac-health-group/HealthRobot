package aiwac.admin.com.healthrobot.activity.skin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.MessageEvent;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import aiwac.admin.com.healthrobot.utils.JsonUtil;

import java.util.List;

public class SkinMainActivity extends BaseActivity implements HomeFragment.Callback {

    private View homeView, societyView, serviceView, personalView;
    private long mExitTime;
    private final int CAMERA_REQUEST=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_main);

        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
        setView();
        addFragment(new HomeFragment());
        EventBus.getDefault().register(this);
    }

    public void selected(){
        homeView.setSelected(false);
        societyView.setSelected(false);
        personalView.setSelected(false);
        serviceView.setSelected(false);
    }
    public void setView(){
//        addFragment(new HomeFragment());
//        homeView = findViewById(R.id.bottombar_home);
//
//
//        homeView.setSelected(true);
//
//        homeView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                selected();
//                homeView.setSelected(true);
//                addFragment(new HomeFragment());
//            }
//        });


    }

    private void addFragment(Fragment fragment) {
        //获取到FragmentManager，在V4包中通过getSupportFragmentManager，
        //在系统中原生的Fragment是通过getFragmentManager获得的。
        FragmentManager FMs = getSupportFragmentManager();
        //fig2.开启一个事务，通过调用beginTransaction方法开启。
        FragmentTransaction MfragmentTransactions = FMs.beginTransaction();
        //把自己创建好的fragment创建一个对象
        //向容器内加入Fragment，一般使用add或者replace方法实现，需要传入容器的id和Fragment的实例。
        MfragmentTransactions.replace(R.id.main_content,fragment);
        //提交事务，调用commit方法提交。
        MfragmentTransactions.commit();
    }

//    //点击两次back键才能退出
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            // 与上次点击返回时刻作差
//            if ((System.currentTimeMillis() - mExitTime) > 2000){
//                Toast.makeText(this, "click again to go back", Toast.LENGTH_SHORT).show();
//                mExitTime = System.currentTimeMillis();
//            }else{
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onClick(int which) {
        switch (which) {
            case 1:
                //Toast.makeText(SkinMainActivity.this, "自己测", Toast.LENGTH_SHORT).show();
                permission();
                break;
            case 2:
                //Toast.makeText(SkinMainActivity.this, "帮朋友测", Toast.LENGTH_SHORT).show();
                permission();
                break;
            case 3:
                //Toast.makeText(SkinMainActivity.this, "上传照片", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //权限申请
    private void permission(){
        requestRunPermisssion(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionListener() {
            @Override
            public void onGranted() {
                //表示所有权限都授权了,可以调用摄像头拍照
                Intent intent=new Intent(SkinMainActivity.this,CameraActivity.class);
                startActivityForResult(intent,CAMERA_REQUEST);

            }

            @Override
            public void onDenied(List<String> deniedPermission) {

                //权限被用户拒绝
                //当拒绝了授权后，为提升用户体验，可以以弹窗的方式引导用户到设置中去进行设置
                new AlertDialog.Builder(SkinMainActivity.this)
                        .setMessage("permissions are needed to use this function")
                        .setPositiveButton("go to set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //引导用户到设置中去进行设置
                                Intent intent = new Intent();
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("cancel", null)
                        .create()
                        .show();
            }

        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getTo().equals("SkinMainActivity")){
            if(messageEvent.getMessage().equals("back")){
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
