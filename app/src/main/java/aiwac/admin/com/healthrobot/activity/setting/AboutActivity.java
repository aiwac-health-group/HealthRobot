package aiwac.admin.com.healthrobot.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import aiwac.admin.com.healthrobot.R;
import zuo.biao.library.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
