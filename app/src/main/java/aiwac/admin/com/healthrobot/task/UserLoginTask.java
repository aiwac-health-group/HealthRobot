package aiwac.admin.com.healthrobot.task;

/**     登录异步任务
 * Created by luwang on 2017/10/23.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONObject;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.UserData;
import aiwac.admin.com.healthrobot.exception.HttpException;
import aiwac.admin.com.healthrobot.exception.JsonException;
import aiwac.admin.com.healthrobot.utils.HttpUtil;
import aiwac.admin.com.healthrobot.utils.JsonUtil;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<String, Void, Boolean> {

    private AlertDialog dialog;

    private Context context;
    private LoginCallback callback;
    private boolean isPreExecute;

    private String userPassword;
    private String phoneNumber;

    private static final String LOG_TAG = "UserLoginTask";

    public UserLoginTask(Context context, boolean isPreExecute, LoginCallback callback) {
        this.context = context;
        this.callback = callback;
        this.isPreExecute = isPreExecute;
    }

    @Override
    protected void onPreExecute() {

        //从主界面进入的执行  否则不执行
        if(isPreExecute) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(R.layout.activity_progress);
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }

    }

    @Override
    protected Boolean doInBackground(String... params) {
        phoneNumber = params[0];
        userPassword = params[1];

        try{
            JSONObject root = new JSONObject();
            root.put(Constant.USER_REGISTER_NUMBER, phoneNumber);
            root.put(Constant.USER_DATA_FIELD_PASSWORD, userPassword);

//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put(Constant.JSON_JSON, JsonUtil.parseObject(user, Constant.USER_LOGIN_PASSWORD_OPT));
//            map.put(Constant.JSON_JSON, root.toString());
            Log.d(LOG_TAG, Constant.JSON_GENERATE_SUCCESS + root.toString());
            String resultJson = HttpUtil.requestPostJson(Constant.HTTP_USER_LOGIN_PASSWORD_BASEURL, root.toString());
            Log.d(LOG_TAG, "return json:" + resultJson);
            if(resultJson == null){
                return null;
            }
            return JsonUtil.parseResult(resultJson);
        }catch (Exception e){
            e.printStackTrace();
            if(e instanceof JsonException){
                Log.d(LOG_TAG, e.getMessage());
            }else if(e instanceof HttpException){
                Log.d(LOG_TAG, e.getMessage());
            }

            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(dialog != null){
            dialog.cancel();
        }

        if (success) {
            //登录成功，保存用户数据和密码     应该直接跳转到主界面
            UserData userData = UserData.getUserData();
            userData.setNumber(phoneNumber);
            userData.setPassword(userPassword);

            writeUserData();
        }

        if(callback != null)
            callback.callBack(success);

    }

    //利用SharedPreferences持久化用户数据
    private void writeUserData(){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constant.DB_USER_TABLENAME, context.MODE_PRIVATE).edit();
        editor.putString(Constant.USER_DATA_FIELD_NUMBER, phoneNumber);
        editor.putString(Constant.USER_DATA_FIELD_PASSWORD, userPassword);
        editor.apply();
        Log.d(LOG_TAG, Constant.USER_DATA_PERSISTENCE + phoneNumber + " " +userPassword);
    }

    @Override
    protected void onCancelled() {
        //任务取消
        callback.callBack(false);

    }
}
