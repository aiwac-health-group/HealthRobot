package aiwac.admin.com.healthrobot.activity.loginandregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.UUID;

import aiwac.admin.com.healthrobot.MainActivity;
import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.activity.setting.SettingActivity;
import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.bean.User;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.ActivityUtil;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.ui.DatePickerWindow;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.ui.PlacePickerWindow;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.util.TimeUtil;

/**
 * zyt
 * 填写或修改用户个人信息
 */
public class RegisterActivity extends BaseActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        initView();

        from = getIntent().getStringExtra("from");
        if(from !=null && from.equals("setting")){
            LoadInfoAsyc loadInfoAsyc = new LoadInfoAsyc();
            loadInfoAsyc.execute();
        }

        //功能归类分区方法，必须调用<<<<<<<<<<

        initData();
        initEvent();

    }

    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)
    private TextView nameView;
    private TextView sexView;
    private TextView birthdayView;
    private TextView wechatView;
    private TextView areaView;
    private TextView addressView;

    private String from;

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

    /**显示性别选择弹窗
     */
    private static final int DIALOG_SET_TOPBAR = 1;
    private void showSexDialog() {
        final String[] items = getResources().getStringArray(R.array.sex);//性别
        new ItemDialog(context, items, "选择性别", DIALOG_SET_TOPBAR, new ItemDialog.OnDialogItemClickListener() {
            @Override
            public void onDialogItemClick(int requestCode, int position, String item) {
                if (position < 0) {
                    position = 0;
                }
                switch (requestCode) {
                    case DIALOG_SET_TOPBAR:
                        sexView.setText(items[position]);
                        break;
                    default:
                        break;
                }
            }
        }).show();
    }

    //Event事件区(只要存在事件监听代码就是)
    @Override
    public void initEvent() {

        findView(R.id.layout_birthday).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                toActivity(DatePickerWindow.createIntent(context, new int[]{1971, 0, 1}
                        , TimeUtil.getDateDetail(System.currentTimeMillis())), REQUEST_TO_DATE_PICKER, false);
            }
        });

        findView(R.id.layout_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(PlacePickerWindow.createIntent(context, getPackageName(), 2), REQUEST_TO_PLACE_PICKER, false);
            }
        });

        findView(R.id.layout_sex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexDialog();

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

                    SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
                    editor.putBoolean(Constant.USER_DATA_FIELD_REGISTER, true);
                    editor.apply();
                    LogUtil.d("注册成功");
                    //向后台发送信息
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                User user = new User();
                                user.setName(userName);
                                user.setBirthday(birthday);
                                user.setAddress(address);
                                user.setArea(area);
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

                    if(from !=null && from.equals("setting")){
                        showShortToast("更新成功");
                        finish();
                    }else{
                        ActivityUtil.skipActivity(RegisterActivity.this, MainActivity.class, true);
                    }
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




    @Override
    protected void onStop() {
        super.onStop();
        if(from ==null || !from.equals("register")){
            EventBus.getDefault().unregister(this);
        }
    }

    class LoadInfoAsyc extends AsyncTask<Void, Void, User> {

        private AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            View view = View.inflate(RegisterActivity.this, R.layout.activity_progress, null);
            builder.setIcon(R.drawable.aiwac);
            builder.setTitle("正在加载...");
            builder.setView(view);  //必须使用view加载，如果使用R.layout设置则无法改变bar的进度

            dialog = builder.create();
            dialog.setCancelable(false);

            dialog.show();
        }

        @Override
        protected User doInBackground(Void... params) {
            try{
                BaseEntity baseEntity = new BaseEntity();
                baseEntity.setBusinessType(Constant.WEBSOCKET_QUERYPERSONINFO_BUSSINESSTYPE_CODE);
                String jsonStr = JsonUtil.queryPersonInfo(baseEntity);
                WebSocketApplication.getWebSocketApplication().send(jsonStr);
                for(int i=0; i<10; i++) {
                    Thread.sleep(500);
                    User user = WebSocketApplication.getWebSocketApplication().getUser();
                    if(user != null) {
                        WebSocketApplication.getWebSocketApplication().setUser(null);
                        return user;
                    }
                }

            }catch (Exception e){
                LogUtil.d( e.getMessage());
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(User user) {
            dialog.cancel();

            if(user != null){   //加载用户信息
                nameView.setText(user.getName());
                sexView.setText(user.getSex());
                birthdayView.setText(user.getBirthday());
                areaView.setText(user.getArea());
                addressView.setText(user.getAddress());
                wechatView.setText(user.getWechat());

            }else{ // 失败， 返回主界面
                Toast.makeText(RegisterActivity.this, "网络信号不好", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
