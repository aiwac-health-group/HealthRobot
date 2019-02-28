package aiwac.admin.com.healthrobot;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import aiwac.admin.com.healthrobot.R;

import java.util.List;

public class MainActivity extends BaseActivity implements ViewDialogFragment.Callback {

    private View homeView, societyView, serviceView, personalView;
    private long mExitTime;
    private final int CAMERA_REQUEST=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setView();
    }

    public void selected(){
        homeView.setSelected(false);
        societyView.setSelected(false);
        personalView.setSelected(false);
        serviceView.setSelected(false);
    }
    public void setView(){
        addFragment(new HomeFragment());
        homeView = findViewById(R.id.bottombar_home);
        societyView = findViewById(R.id.bottombar_society);
        personalView = findViewById(R.id.bottombar_personal);
        serviceView = findViewById(R.id.bottombar_service);

        homeView.setSelected(true);

        homeView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                selected();
                homeView.setSelected(true);
                addFragment(new HomeFragment());
            }
        });

        societyView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                selected();
                societyView.setSelected(true);
                addFragment(new SocietyFragment());
            }
        });

        serviceView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                selected();
                serviceView.setSelected(true);
                addFragment(new ServiceFragment());
            }
        });

        personalView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                selected();
                personalView.setSelected(true);
                addFragment(new PersonalFragment());
            }
        });
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            // 与上次点击返回时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000){
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }else{
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(int which) {
        switch (which) {
            case 1:
                //Toast.makeText(MainActivity.this, "自己测", Toast.LENGTH_SHORT).show();
                permission();
                break;
            case 2:
                //Toast.makeText(MainActivity.this, "帮朋友测", Toast.LENGTH_SHORT).show();
                permission();
                break;
            case 3:
                //Toast.makeText(MainActivity.this, "上传照片", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //权限申请
    private void permission(){
        requestRunPermisssion(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionListener() {
            @Override
            public void onGranted() {
                //表示所有权限都授权了,可以调用摄像头拍照
                Intent intent=new Intent(MainActivity.this,CameraActivity.class);
                startActivityForResult(intent,CAMERA_REQUEST);

            }

            @Override
            public void onDenied(List<String> deniedPermission) {

                //权限被用户拒绝
                //当拒绝了授权后，为提升用户体验，可以以弹窗的方式引导用户到设置中去进行设置
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("需要开启权限才能使用此功能")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //引导用户到设置中去进行设置
                                Intent intent = new Intent();
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
            }

        });

    }
}
