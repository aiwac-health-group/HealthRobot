package aiwac.admin.com.healthrobot.server;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import aiwac.admin.com.healthrobot.HealthRobotApplication;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.UserData;
import aiwac.admin.com.healthrobot.exception.WebSocketException;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import aiwac.admin.com.healthrobot.utils.StringUtil;

import static android.content.Context.MODE_PRIVATE;

/**     用于WebSocket建立唯一的连接等操作
 * Created by luwang on 2017/10/31.
 */

public class WebSocketApplication {

    private WebSocketClientHelper webSocketHelper;
    private final static WebSocketApplication webSocketApplication;
    private UserData userData;

    static {
        webSocketApplication = new WebSocketApplication();
    }

    private void init(Context context){
        try{
            SharedPreferences pref = HealthRobotApplication.getContext().getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE);
            String token = pref.getString(Constant.USER_DATA_FIELD_TOKEN, "");
            URI uri = new URI(Constant.WEBSOCKET_URL+token);
            //这里会进行和服务端的握手操作
            webSocketHelper = new WebSocketClientHelper(uri, getDefaultMap(),context);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.WEBSOCKET_URI_EXCEPTION);
            throw new WebSocketException(Constant.WEBSOCKET_URI_EXCEPTION, e);
        }
    }

    private void connection(Context context){

        if(webSocketHelper == null) {
            init(context);
        }

        if(!webSocketHelper.isOpen() && !webSocketHelper.isConnecting()) {
            try{
                webSocketHelper.connect();
                LogUtil.d( Constant.WEBSOCKET_CONNECTION_SUCCESS);
            }catch (Exception e){
                e.printStackTrace();
                LogUtil.d( Constant.WEBSOCKET_CONNECTION_EXCEPTION);
            }
        }
    }


    private WebSocketApplication(){
        //初始化私有变量
        userData = UserData.getUserData();
    }

    public static WebSocketApplication getWebSocketApplication(){
        return webSocketApplication;
    }

    private Map<String, String> getDefaultMap(){
        Map<String, String> map = new HashMap<String, String>();

        //获取用户电话号码
        String number = userData.getNumber();
        if(StringUtil.isValidate(number)){
            map.put(Constant.WEBSOCKET_USER_IDENTITY, number);

            //其他需要添加的字段

            return map;
        }else{
            throw new WebSocketException(Constant.WEBSOCKET_USER_IDENTITY_EXCEPTION);
        }
    }


    public WebSocketClientHelper getWebSocketHelper(){
        return webSocketHelper;
    }



    public UserData getUserData(){
        return userData;
    }

    public String getUserNumber(){
        return userData.getNumber();
    }

    public boolean isNetwork(){
        return userData.isNetwork();
    }

    public void setNull(){
        close();
        webSocketHelper = null;
    }

    public void close(){
        if(webSocketHelper !=null && !webSocketHelper.isClosed()) {
            webSocketHelper.close();
            LogUtil.d( Constant.WEBSOCKET_CONNECTION_CLOSE);
        }
    }

    //在新的线程中开启一个websocket连接
    public void startWebSocketConnection(final Context context){
        if(webSocketHelper == null || (!webSocketHelper.isOpen() && !webSocketHelper.isConnecting())) {
            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        //获得连接
                        webSocketApplication.connection(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.d( Constant.WEBSOCKET_CONNECTION_EXCEPTION);
                    }
                }
            });
        }
    }

    //发送消息
    public void send(String json){
        LogUtil.printJson("json",json,"##");
        webSocketHelper.send(json);

    }




}

