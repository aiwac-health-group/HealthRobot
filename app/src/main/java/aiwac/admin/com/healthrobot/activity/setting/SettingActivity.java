package aiwac.admin.com.healthrobot.activity.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.activity.loginandregister.ConnectWifiActivity;
import aiwac.admin.com.healthrobot.activity.loginandregister.LoginActivity;
import aiwac.admin.com.healthrobot.activity.loginandregister.RegisterActivity;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.utils.ActivityUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.ui.AlertDialog;

public class SettingActivity extends BaseActivity implements View.OnClickListener, AlertDialog.OnDialogButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        initEvent();
    }



    @Override
    public void initView() {//必须调用

    }


    @Override
    public void initData() {//必须调用

    }


    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
        editor.putString(Constant.USER_DATA_FIELD_NUMBER, "");
        editor.apply();
        ActivityUtil.skipActivity(SettingActivity.this, LoginActivity.class,true);
    }


    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








    //Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void initEvent() {//必须调用

        findViewById(R.id.llSettingSetting).setOnClickListener(this);
        findViewById(R.id.llSettingAbout).setOnClickListener(this);
        findViewById(R.id.llSettingLogout).setOnClickListener(this);
        findViewById(R.id.llChangeWifi).setOnClickListener(this);
    }




    @Override
    public void onDialogButtonClick(int requestCode, boolean isPositive) {
        if (! isPositive) {
            return;
        }

        switch (requestCode) {
            case 0:
                logout();
                break;
            default:
                break;
        }
    }



    @Override
    public void onClick(View v) {//直接调用不会显示v被点击效果
        switch (v.getId()) {

            case R.id.llSettingSetting:
                Intent intent = new Intent(SettingActivity.this, RegisterActivity.class);
                intent.putExtra("from", "setting");
                startActivity(intent);
                break;
            case R.id.llSettingAbout:
                toActivity(AboutActivity.createIntent(context));
                break;
            case R.id.llSettingLogout:
                new AlertDialog(context, "退出登录", "确定退出登录？", true, 0, this).show();
                break;
            case R.id.llChangeWifi:
                Intent wifiIntent = new Intent(SettingActivity.this, ConnectWifiActivity.class);
                wifiIntent.putExtra("from", "setting");
                startActivity(wifiIntent);
                break;
            default:
                break;
        }
    }
}
