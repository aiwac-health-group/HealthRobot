package aiwac.admin.com.healthrobot.db;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import aiwac.admin.com.healthrobot.common.Constant;

/**     保存用户相关的数据  单例模式
 * Created by luwang on 2017/10/27.
 */

public class UserData {

    private final static String LOG_TAG = "UserData";
    private final static UserData userData = new UserData();
    private boolean isNetwork = false; //用户是否联网标志
    private boolean isMonitor = false; //用户是否开启监控模式标记
    private  Map<String, String> map;


    //初始化操作
    private void init() {
        map = new HashMap<String, String>();
        map.put(Constant.USER_DATA_FIELD_PASSWORD, "");
        map.put(Constant.USER_DATA_FIELD_NUMBER, "");
        isNetwork = true;
    }

    private UserData(){
        init();
    }

    public static UserData getUserData(){
        return userData;
    }

    public String getPassword(){
        return map.get(Constant.USER_DATA_FIELD_PASSWORD);
    }
    public void setPassword(String password){
        map.put(Constant.USER_DATA_FIELD_PASSWORD, password);
        Log.d(LOG_TAG, "UserData setPassword : " + password);
    }

    public String getNumber(){
        return map.get(Constant.USER_DATA_FIELD_NUMBER);
    }
    public void setNumber(String number){
        map.put(Constant.USER_DATA_FIELD_NUMBER, number);
        Log.d(LOG_TAG, "UserData setNumber : " + number);
    }

    public boolean isNetwork() {
        return isNetwork;
    }
    public void setNetwork(boolean network) {
        isNetwork = network;
    }

    public boolean isMonitor() {
        return isMonitor;
    }
    public void setMonitor(boolean monitor) {
        isMonitor = monitor;
    }

}
