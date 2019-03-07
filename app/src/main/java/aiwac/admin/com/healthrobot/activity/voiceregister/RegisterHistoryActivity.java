package aiwac.admin.com.healthrobot.activity.voiceregister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.MessageEvent;
import aiwac.admin.com.healthrobot.bean.RegisterInfo;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import zuo.biao.library.base.BaseActivity;

public class RegisterHistoryActivity extends BaseActivity {


    private SmartTable<RegisterInfo> table;


    /**启动这个Activity的Intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, RegisterHistoryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_history);
        table = findViewById(R.id.register_table);
        table.getConfig().setContentBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if(cellInfo.position%2==0){
                    return ContextCompat.getColor(RegisterHistoryActivity.this,R.color.colorLightBlue);
                }else{
                    return ContextCompat.getColor(RegisterHistoryActivity.this,R.color.colorBackivory);
                }

            }
        });

        EventBus.getDefault().register(this);


    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void Event(MessageEvent messageEvent) {
        List<RegisterInfo> registerInfoList = JsonUtil.jsonToRegisterInfoList(messageEvent.getMessage());
        table.setData(registerInfoList);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        //测试
        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setProvince("北京市");
        registerInfo.setBusinessType("北京市");
        registerInfo.setHospital("北京市");
        registerInfo.setDepartment("北京市");
        registerInfo.setRegisterStatus("北京市");
        registerInfo.setDescription("北京市");
        registerInfo.setCreateTime("北京市");
        registerInfo.setUpdateTime("北京市");
        List<RegisterInfo> registerInfoList = new ArrayList<>();
        registerInfoList.add(registerInfo);
        registerInfoList.add(registerInfo);
        table.setData(registerInfoList);
    }

    @Override
    public void initEvent() {

    }
}
