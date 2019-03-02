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
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.ActivityUtil;
import aiwac.admin.com.healthrobot.utils.HttpUtil;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import aiwac.admin.com.healthrobot.utils.StringUtil;

import static aiwac.admin.com.healthrobot.common.Constant.USER_CHECKCODE_ERROR_EXCEPTION;

public class RegisterActivity extends AppCompatActivity {

    private final static String LOG_TAG = "RegisterActivity";
    private Button checkcodeBtn;
    private Button registerBtn;

    private AutoCompleteTextView numberEdit;
    private EditText checkcodeEidt;

    private String phoneNumber;
    private String from;//从哪个界面跳转来的

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
                    Toast.makeText(RegisterActivity.this, Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show();
                    break;
                case Constant.USER_JSON_EXCEPTION:
                    Toast.makeText(RegisterActivity.this, Constant.USER_JSON_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show();
                    break;
                case USER_CHECKCODE_ERROR_EXCEPTION:
                    Toast.makeText(RegisterActivity.this, Constant.USER_CHECKCODE_ERROR_EXCEPTION_MESSAGE, Toast.LENGTH_LONG).show();
                    break;
                case Constant.USER_CHECKCODE_SUCCESS:

                    ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                        @Override
                        public void run() {
                            // 检查该用户是否注册
                            UserSqliteHelper userSqliteHelper = new UserSqliteHelper(RegisterActivity.this);
                            User user = userSqliteHelper.getUser(phoneNumber);
                            Log.d(LOG_TAG, UserData.getUserData().getNumber());
                            User user2 = new User(UserData.getUserData().getNumber(),"123456");
                            Log.d(LOG_TAG, "user："+user);
                            if(user != null) {
                                Log.d(LOG_TAG, Constant.USER_REGISTER + user.toString());
                                //userSqliteHelper.update(user2);
                                ActivityUtil.skipActivity(RegisterActivity.this, MainActivity.class);



                            } else {
                                userSqliteHelper.insert(user2);
                                //跳转到填写个人信息界面
                                ActivityUtil.skipActivity(RegisterActivity.this, PersonalInformationActivity.class, true);


                            }



                        }
                    });

                    /*if(isPhoneNumberExist){ //存在，直接登录
                        Log.d(LOG_TAG, "注册7");
                        //注入用户数据

                        //ActivityUtil.skipActivity(RegisterActivity.this, Main2Activity.class, true);
                    }else {  //不存在，注册
                        //这里实现注册逻辑
                        Log.d(LOG_TAG, "注册8");
                        RegisterTask registerTask = new RegisterTask(RegisterActivity.this);
                        registerTask.execute(phoneNumber);
                    }*/
                    break;
                default:
                    break;
            }
        }
    };


    private void writeUserData(){
        SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
        editor.putString(Constant.USER_DATA_FIELD_NUMBER, phoneNumber);
        editor.putString(Constant.USER_DATA_FIELD_PASSWORD, UserData.getUserData().getPassword());
        editor.apply();
        Log.d(LOG_TAG, Constant.USER_DATA_PERSISTENCE + phoneNumber + " " +UserData.getUserData().getNumber());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        from = intent.getStringExtra("from");


        numberEdit = (AutoCompleteTextView) findViewById(R.id.register_number_edit);
        checkcodeEidt = (EditText) findViewById(R.id.register_checkcode_edit);
        //passwordEdit = (EditText) findViewById(R.id.register_password);


        checkcodeBtn = (Button) findViewById(R.id.register_check_code_button);
        registerBtn = (Button) findViewById(R.id.register_button);


        checkcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = numberEdit.getText().toString().trim();
                /*
                if(StringUtil.isValidate(password) && StringUtil.isPassword(password)){
                    phonePassword = password;
                }else{
                    passwordEdit.setError(getString(R.string.error_invalid_password));
                    passwordEdit.requestFocus();
                    return;
                }*/

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
                                LogUtil.d("resultJson : " + resultJson);
                                if(resultJson != null) {
                                    String errorCode = JsonUtil.parseErrorCode(resultJson);
                                    if(errorCode.equals(Constant.MESSAGE_ERRORCODE_200)){
                                        message.what = Constant.USER_GET_CHECKCODE;

                                    }else{
                                        message.what = Constant.USER_GET_CHECKCODE_EXCEPTION;
                                        Log.d(LOG_TAG, Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE);
                                    }

                                }else{
                                    message.what = Constant.USER_GET_CHECKCODE_EXCEPTION;
                                    Log.d(LOG_TAG, Constant.USER_GET_CHECKCODE_EXCEPTION_MESSAGE);
                                }

                                /*HashMap<String, String> map = new HashMap<String, String>();
                                User user = new User();
                                user.setNumber(number);
                                map.put(Constant.JSON_JSON, JsonUtil.parseObject(user, Constant.USER_GET_CHECKCODE_OPT));
                                String resultJson = HttpUtil.requestPost(Constant.HTTP_CHECKCODE_URL, map);
                                String opt = JsonUtil.parseOpt(resultJson);
                                    isPhoneNumberExist = JsonUtil.isUserRegisted(resultJson);
                                    String checkcode = JsonUtil.parseCheckcode(resultJson);

                                    message.what = Constant.USER_GET_CHECKCODE;
                                    Bundle bundle = new Bundle();
                                    bundle.putString(Constant.USER_CHECKCODE_MESSAGE_KEY, checkcode);
                                    message.setData(bundle);
                                    Log.d(LOG_TAG, resultJson);*/
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
                    Toast.makeText(RegisterActivity.this, Constant.USER_INPUT_NUMBER_ERROR, Toast.LENGTH_SHORT).show();
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
                                    if(errorCode.equals(Constant.MESSAGE_ERRORCODE_200)){

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


                /*if(phoneNumber != null && checkcode != null && StringUtil.isCheckcodeValidate(userCheckcode, checkcode)){

                    if(isPhoneNumberExist){ //存在，直接登录
                        //注入用户数据
                        UserData userData = UserData.getUserData();
                        userData.setNumber(phoneNumber);
                        ActivityUtil.skipActivity(RegisterActivity.this, Main2Activity.class, true);
                    }else {  //不存在，注册
                        //这里实现注册逻辑
                        RegisterTask registerTask = new RegisterTask(RegisterActivity.this);
                        registerTask.execute(phoneNumber);
                    }

                    //Map<String, String> msg = new HashMap<String, String>();
                    //msg.put("number", phoneNumber);

                    *//*
                    Intent bindIntent = new Intent(RegisterActivity.this, WebSocketService.class);
                    bindIntent.putExtra(Constant.SERVICE_USERSERVICE_ACTION, Constant.SERVICE_USERSERVICE_REGISTER);
                    bindService(bindIntent, connection, BIND_AUTO_CREATE);
                    *//*

                }else{
                    //验证码无效
                    checkcodeEidt.setError(getString(R.string.error_incorrect_checkcode));
                    checkcodeEidt.requestFocus();
                }*/

            }
        });


    }
}

