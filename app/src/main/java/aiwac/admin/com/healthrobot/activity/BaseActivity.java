package aiwac.admin.com.healthrobot.activity;

import android.content.pm.PackageManager;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import aiwac.admin.com.healthrobot.activity.PermissionListener;

/**
 * 动态权限处理
 */
public class BaseActivity extends AppCompatActivity {

    private PermissionListener mListener;
    private static final int PERMISSION_REQUESTCODE = 100;
    @Override
    public void onCreate(Bundle savedInstanceState,PersistableBundle persistentState) {
        super.onCreate(savedInstanceState,persistentState);
    }

    public void requestRunPermisssion(String[] permissions, PermissionListener listener){
        mListener = listener;
        List<String> permissionLists = new ArrayList<>();
        for(String permission : permissions){
            //判断是否已经获得授权，将未授权的权限存储在permissionLists中
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                permissionLists.add(permission);
            }
        }
        //若有未授权则向用户申请权限，全部授权则进行回调
        if(!permissionLists.isEmpty()){
            ActivityCompat.requestPermissions(this, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
        }else{
            mListener.onGranted();
        }
    }

    //回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUESTCODE:
                if(grantResults.length > 0){
                    //存放用户拒绝授权的权限
                    List<String> deniedPermissions = new ArrayList<>();
                    for(int i = 0; i < grantResults.length; i++){
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if(grantResult != PackageManager.PERMISSION_GRANTED){
                            deniedPermissions.add(permission);
                        }
                    }
                    if(deniedPermissions.isEmpty()){
                        //说明用户都授权了
                        mListener.onGranted();
                    }else{
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }
}
