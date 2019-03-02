package aiwac.admin.com.healthrobot.activity.loginandregister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.UUID;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.User;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.ui.DatePickerWindow;
import zuo.biao.library.ui.PlacePickerWindow;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.util.TimeUtil;

public class PersonalInformationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
    }

    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)
    private AutoCompleteTextView nameView;
    private AutoCompleteTextView sexView;
    private AutoCompleteTextView birthdayView;
    private AutoCompleteTextView wechatView;
    private AutoCompleteTextView areaView;
    private AutoCompleteTextView addressView;


    @Override
    public void initView() {

        nameView = findViewById(R.id.tv_name);
        sexView = findViewById(R.id.tv_sex);
        birthdayView = findViewById(R.id.tv_birthday);
        wechatView = findViewById(R.id.tv_wechat);
        areaView = findViewById(R.id.tv_area);
        addressView = findViewById(R.id.tv_address);
    }

    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)
    @Override
    public void initData() {

    }

    //Event事件区(只要存在事件监听代码就是)
    @Override
    public void initEvent() {

        birthdayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(DatePickerWindow.createIntent(context, new int[]{1971, 0, 1}
                        , TimeUtil.getDateDetail(System.currentTimeMillis())), REQUEST_TO_DATE_PICKER, false);

            }
        });

        areaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(PlacePickerWindow.createIntent(context, getPackageName(), 2), REQUEST_TO_PLACE_PICKER, false);

            }
        });

        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userName = nameView.getText().toString().trim();
                final String sex = sexView.getText().toString().trim();
                final String birthday = birthdayView.getText().toString().trim();
                final String area = areaView.getText().toString().trim();
                final String wechat = wechatView.getText().toString().trim();
                final String address = addressView.getText().toString().trim();

                if(userName.equals("")){
                    showShortToast("请输入姓名");
                }else if(sex.equals("")){
                    showShortToast("请输入性别");
                }else if(birthday.equals("")){
                    showShortToast("请输入出生年月");
                }else if(area.equals("")){
                    showShortToast("请输入所在地区");
                }else if(wechat.equals("")){
                    showShortToast("请输入详细地址");
                }else if(address.equals("")){
                    showShortToast("请输入微信号");
                }else{
                    //向后台发送信息
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                User user = new User();
                                user.setName(userName);
                                user.setBirthday(birthday);
                                user.setPlace(area+address);
                                user.setWechat(wechat);
                                user.setSex(sex);
                                user.setBusinessType(Constant.WEBSOCKET_PERSONAL_INFOMATION_BUSSINESSTYPE_CODE);
                                user.setUuid(UUID.randomUUID().toString());
                                String json = JsonUtil.userToJson(user);

                                WebSocketApplication.getWebSocketApplication().send(json);

                            }catch (Exception e){
                                LogUtil.d(e.getMessage());
                                //其他异常处理
                            }
                        }
                    });
                }
            }
        });
    }


    //生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private static final int REQUEST_TO_PLACE_PICKER = 32;
    private static final int REQUEST_TO_DATE_PICKER = 33;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_TO_PLACE_PICKER:
                if (data != null) {
                    ArrayList<String> placeList = data.getStringArrayListExtra(PlacePickerWindow.RESULT_PLACE_LIST);
                    if (placeList != null) {
                        String area = "";
                        for (String s : placeList) {
                            area += StringUtil.getTrimedString(s);
                        }
                        areaView.setText(area);
                        //showShortToast("选择的地区为: " + place);
                    }
                }
                break;
            case REQUEST_TO_DATE_PICKER:
                if (data != null) {
                    ArrayList<Integer> list = data.getIntegerArrayListExtra(DatePickerWindow.RESULT_DATE_DETAIL_LIST);
                    if (list != null && list.size() >= 3) {

                        int[] selectedDate = new int[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            selectedDate[i] = list.get(i);
                        }
                        birthdayView.setText(selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2]);
                        //showShortToast("选择的日期为" + selectedDate[0] + "-" + (selectedDate[1] + 1) + "-" + selectedDate[2]);
                    }
                }
                break;
            default:
                break;
        }
    }

}
