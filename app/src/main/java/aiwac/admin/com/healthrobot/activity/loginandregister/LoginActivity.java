package aiwac.admin.com.healthrobot.activity.loginandregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import aiwac.admin.com.healthrobot.MainActivity;
import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.User;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.UserData;
import aiwac.admin.com.healthrobot.db.UserSqliteHelper;
import aiwac.admin.com.healthrobot.exception.HttpException;
import aiwac.admin.com.healthrobot.exception.JsonException;
import aiwac.admin.com.healthrobot.service.WebSocketService;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.ActivityUtil;
import aiwac.admin.com.healthrobot.utils.HttpUtil;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import aiwac.admin.com.healthrobot.utils.StringUtil;
import aiwac.admin.com.healthrobot.utils.WifiUtil;

import static aiwac.admin.com.healthrobot.common.Constant.USER_CHECKCODE_ERROR_EXCEPTION;

public class LoginActivity extends AppCompatActivity {

    private final static String LOG_TAG = "LoginActivity";
    private Button checkcodeBtn;
    private Button registerBtn;

    private AutoCompleteTextView numberEdit;
    private EditText checkcodeEidt;

    private String phoneNumber;

    //处理获取验证码handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case Constant.USER_GET_CHECKCODE:
                    registerBtn.setClickable(true);
                    registerBtn.setEnabled(true);
                    checkcodeBtn.setClickable(false);
                    checkcodeBtn.setEnabled(false);
                        /* 倒计时60秒，一次1秒  在此期间不能在此点击获取验证码按钮 */
                    CountDownTimer timer = new CountDownTimer(Constant.USER_CHECKCODE_MILLISINFUTURE, Constant.USER_CHECKCODE_COUNTDOWNINTERVAL) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            checkcodeBtn.setText(StringUtil.joinButtonText(checkcodeBtn.getText().toString(), millisUntilFinished/1000));
                        }

                        @Override
                        public void onFinish() {
                            checkcodeBtn.setText(StringUtil.joinButtonText(checkcodeBtn.getText().toString(), 0));
                            checkcodeBtn.setClickable(true);
                            checkcodeBtn.setEnabled(true);
                        }
                    }.start();

                    break;
                case Constant.USER_GET_CHECKCODE_EXCEPTION:
                    Toast.makeText(LoginActivity.this, Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show();
                    break;
                case Constant.USER_JSON_EXCEPTION:
                    Toast.makeText(LoginActivity.this, Constant.USER_JSON_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show();
                    break;
                case USER_CHECKCODE_ERROR_EXCEPTION:
                    Toast.makeText(LoginActivity.this, Constant.USER_CHECKCODE_ERROR_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show();
                    break;
                case Constant.USER_CHECKCODE_SUCCESS:
                    //开启服务，创建websocket连接
                    Intent intent = new Intent(LoginActivity.this, WebSocketService.class);
                    intent.putExtra(Constant.SERVICE_TIMER_TYPE, Constant.SERVICE_TIMER_TYPE_WEBSOCKET);
                    startService(intent);
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            // 检查该用户是否注册
                            UserSqliteHelper userSqliteHelper = new UserSqliteHelper(LoginActivity.this);
                            User user = userSqliteHelper.getUser(phoneNumber);
                            Log.d(LOG_TAG, UserData.getUserData().getNumber());
                            User user2 = new User(UserData.getUserData().getNumber(),"123456");//没有改之前的用户数据库表代码，随机写了一个密码替代
                            Log.d(LOG_TAG, "user："+user);
                            if(user != null) {

                                Log.d(LOG_TAG, Constant.USER_REGISTER + user.toString());
                                //判断有没有填写个人信息
                                SharedPreferences pref = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE);
                                Boolean isRegister = pref.getBoolean(Constant.USER_DATA_FIELD_REGISTER, false);
                                if(isRegister){
                                    ActivityUtil.skipActivity(LoginActivity.this, MainActivity.class);
                                }else{
                                    ActivityUtil.skipActivity(LoginActivity.this, RegisterActivity.class);
                                }

                            } else {
                                userSqliteHelper.insert(user2);
                                SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
                                editor.putString(Constant.USER_DATA_FIELD_NUMBER, phoneNumber);
                                editor.putBoolean(Constant.USER_DATA_FIELD_REGISTER, false);
                                editor.apply();
                                //跳转到填写个人信息界面
                                ActivityUtil.skipActivity(LoginActivity.this, RegisterActivity.class, true);

                            }
                        }
                    });


                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        numberEdit = (AutoCompleteTextView) findViewById(R.id.register_number_edit);
        checkcodeEidt = (EditText) findViewById(R.id.register_checkcode_edit);
        checkcodeBtn = (Button) findViewById(R.id.register_check_code_button);
        registerBtn = (Button) findViewById(R.id.register_button);


        checkcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = numberEdit.getText().toString().trim();
                if(StringUtil.isNumber(number)) {
                    phoneNumber = number;

                    //获取验证码
                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            Message message = handler.obtainMessage();
                            try{
                                //zyt修改
                                JSONObject root = new JSONObject();
                                root.put(Constant.USER_REGISTER_NUMBER, phoneNumber);
                                Log.d(LOG_TAG, Constant.JSON_GENERATE_SUCCESS + root.toString());
                                String resultJson = HttpUtil.requestPostJson(Constant.HTTP_CHECKCODE_URL, root.toString());
                                Log.d(LOG_TAG, "resultJson : " + resultJson);
                                if((resultJson != null) && resultJson.equals("200")) {
                                    message.what = Constant.USER_GET_CHECKCODE;
                                }else{
                                    message.what = Constant.USER_GET_CHECKCODE_EXCEPTION;
                                    Log.d(LOG_TAG, Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE);
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                                if(e instanceof HttpException) {
                                    message.what = Constant.USER_GET_CHECKCODE_EXCEPTION;
                                    Log.d(LOG_TAG, Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE);
                                }else if(e instanceof JsonException){
                                    message.what = Constant.USER_JSON_EXCEPTION;
                                    Log.d(LOG_TAG, Constant.JSON_EXCEPTION);
                                }else{
                                    message.what = Constant.USER_GET_CHECKCODE_EXCEPTION;
                                    Log.d(LOG_TAG, Constant.USER_UNKNOW_EXCEPTION);
                                }
                            }finally {
                                handler.sendMessage(message);
                            }
                        }
                    });

                    checkcodeEidt.requestFocus(); //输入验证码文本框聚焦
                }else {
                    Log.d(LOG_TAG, Constant.USER_INPUT_NUMBER_ERROR);
                    numberEdit.setError(Constant.USER_INPUT_NUMBER_ERROR);
                    Toast.makeText(LoginActivity.this, Constant.USER_INPUT_NUMBER_ERROR, Toast.LENGTH_SHORT).show();
                    numberEdit.setFocusable(true);
                    numberEdit.setFocusableInTouchMode(true);
                    numberEdit.requestFocus();
                }
            }
        });


        //用户注册
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                    @Override
                    public void run() {
                        String checkcode = checkcodeEidt.getText().toString().trim();
                        Log.d(LOG_TAG, checkcode + " : " + phoneNumber);
                        Message message = new Message();
                        if(phoneNumber != null && checkcode != null){
                            JSONObject root = new JSONObject();
                            try {
                                root.put(Constant.USER_REGISTER_NUMBER, phoneNumber);
                                root.put(Constant.USER_REGISTER_CHECKCODE, checkcode);
                                Log.d(LOG_TAG, Constant.JSON_GENERATE_SUCCESS + root.toString());
                                String resultJson = HttpUtil.requestPostJson(Constant.HTTP_USER_LOGIN_IDENTIFYCODE_BASEURL, root.toString());
                                Log.d(LOG_TAG, "resultJson : " + resultJson);
                                if(resultJson != null) {
                                    String errorCode = JsonUtil.parseErrorCode(resultJson);
                                    if(errorCode.equals(Constant.MESSAGE_ERRORCODE_2000)){
                                        String token =JsonUtil.parseToken(resultJson);
                                        SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
                                        editor.putString(Constant.USER_DATA_FIELD_TOKEN, token);
                                        editor.putLong(Constant.USER_DATA_FIELD_TOKENTIME, System.currentTimeMillis());
                                        editor.apply();
                                        UserData userData = UserData.getUserData();
                                        userData.setNumber(phoneNumber);
                                        message.what = Constant.USER_CHECKCODE_SUCCESS;
                                        Log.d(LOG_TAG, Constant.USER_CHECKCODE_SUCCESS_MESSAGE);


                                    }else{
                                        message.what = USER_CHECKCODE_ERROR_EXCEPTION;
                                        Log.d(LOG_TAG, Constant.USER_CHECKCODE_ERROR_EXCEPTION_MESSAGE);
                                    }

                                }else{
                                    message.what = Constant.USER_GET_CHECKCODE_EXCEPTION;
                                    Log.d(LOG_TAG, Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                message.what = Constant.USER_JSON_EXCEPTION;
                                Log.d(LOG_TAG, Constant.JSON_EXCEPTION);
                            }finally {
                                handler.sendMessage(message);
                            }

                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //测试用，去掉获取验证码这一步
        /*SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
        editor.putString(Constant.USER_DATA_FIELD_NUMBER, "15844096407");
        editor.putBoolean(Constant.USER_DATA_FIELD_REGISTER, true);
        editor.apply();*/
        //检查用户是否已经持久化
        SharedPreferences pref = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE);
        String number = pref.getString(Constant.USER_DATA_FIELD_NUMBER, "");
        Boolean isRegister = pref.getBoolean(Constant.USER_DATA_FIELD_REGISTER, false);
        Boolean isConnectWifi = pref.getBoolean(Constant.USER_DATA_ISCONNECTWIFI, false);
        LogUtil.d("number:"+number+"isRegister:"+isRegister);
        boolean isNet = WifiUtil.checkNet(this); //判断是否连接网络
        if(isNet){
            if(!number.equals("")){
                UserData.getUserData().setNumber(number);

                //开启服务，创建websocket连接
                Intent intent = new Intent(this, WebSocketService.class);
                intent.putExtra(Constant.SERVICE_TIMER_TYPE, Constant.SERVICE_TIMER_TYPE_WEBSOCKET);
                startService(intent);

                if(isRegister){
                    ActivityUtil.skipActivity(LoginActivity.this, MainActivity.class,true);
                }else{
                    ActivityUtil.skipActivity(LoginActivity.this, RegisterActivity.class,true);
                }

            }
        }else{
            //没联网

            ActivityUtil.skipActivity(LoginActivity.this, ConnectWifiActivity.class,true);
            finish();


            /*if(!number.equals("")){
                UserData.getUserData().setNumber(number);
                if(isConnectWifi){
                    //开启服务，创建websocket连接
                    Intent intent = new Intent(this, WebSocketService.class);
                    intent.putExtra(Constant.SERVICE_TIMER_TYPE, Constant.SERVICE_TIMER_TYPE_WEBSOCKET);
                    startService(intent);
                }else{
                    ActivityUtil.skipActivity(LoginActivity.this, ConnectWifiActivity.class,true);
                    finish();
                }

                if(isRegister){
                    ActivityUtil.skipActivity(LoginActivity.this, MainActivity.class,true);
                }else{
                    ActivityUtil.skipActivity(LoginActivity.this, RegisterActivity.class,true);
                }

            }*/
        }

    }

    //判断用户以前有没有登录并填写信息了，如果登录则直接进入
    private User getUser(){
        SharedPreferences pref = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE);
        String number = pref.getString(Constant.USER_DATA_FIELD_NUMBER, "");
        Boolean isRegister = pref.getBoolean(Constant.USER_DATA_FIELD_REGISTER, false);
        Log.d(LOG_TAG, "number :" + number + " isRegister : " + isRegister);
        User user = new User();
        user.setNumber(number);
        user.setRegister(isRegister);
        return user;
    }
}

