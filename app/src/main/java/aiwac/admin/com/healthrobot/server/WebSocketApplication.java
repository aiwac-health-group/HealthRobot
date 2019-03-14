package aiwac.admin.com.healthrobot.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import aiwac.admin.com.healthrobot.HealthRobotApplication;
import aiwac.admin.com.healthrobot.bean.ExamInfoForCarousel;
import aiwac.admin.com.healthrobot.bean.LectureAVDetail;
import aiwac.admin.com.healthrobot.bean.LectureArticleDetail;
import aiwac.admin.com.healthrobot.bean.LectureCourseAbstractInfo;
import aiwac.admin.com.healthrobot.bean.User;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.UserData;
import aiwac.admin.com.healthrobot.exception.WebSocketException;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.HttpUtil;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import aiwac.admin.com.healthrobot.utils.StringUtil;

import static aiwac.admin.com.healthrobot.common.Constant.USER_CHECKCODE_ERROR_EXCEPTION;
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
            LogUtil.d("uri:"+uri);
            //URI uri = new URI(Constant.WEBSOCKET_URL);
            //这里会进行和服务端的握手操作
            webSocketHelper = new WebSocketClientHelper(uri, getDefaultMap(),context);
            Log.d("11", "init: "+webSocketHelper);
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

    public User getUser(){
        return webSocketHelper.getUser();
    }

    public void setUser(User user){
        webSocketHelper.setUser(user);
    }
    public ArrayList<ExamInfoForCarousel> getExamInfoForCarousels() {
        return webSocketHelper.getExamInfoForCarousels();
    }
    //  健康讲座  、健康检测结果查询等相关操作
    public LectureCourseAbstractInfo getWebSocketHelperLectureVideoAllInfo(){
        return webSocketHelper.getLectureVideoAllInfo();
    }

    public LectureCourseAbstractInfo getWebSocketHelperLectureAudioAllInfo(){
        return   webSocketHelper.getLectureAudioAllInfo();
    }

    public LectureCourseAbstractInfo getWebSocketHelperLectureArticleAllInfo(){
        return  webSocketHelper.getLectureArticleAllInfo();
    }

    public LectureAVDetail getWebSocketHelperLectureAudioDetail(){
        return webSocketHelper.getLectureAudioDetail();
    }

    public LectureAVDetail getWebSocketHelperLectureVideoDetail(){
        return webSocketHelper.getLectureVideoDetail();
    }

    public LectureArticleDetail getWebSocketHelperLectureArticleDetail(){
        return webSocketHelper.getLectureArticleDetail();
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
        //startWebSocketConnection(HealthRobotApplication.getContext());
    }

    //在新的线程中开启一个websocket连接
    public void startWebSocketConnection(final Context context){
        LogUtil.d("startWebSocketConnection");
        if(webSocketHelper == null || (!webSocketHelper.isOpen() && !webSocketHelper.isConnecting())) {
            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        //检测token有效期，无效则更新
                        SharedPreferences pref = context.getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE);
                        Long validTime = pref.getLong(Constant.USER_DATA_FIELD_TOKENTIME, 0);
                        if (System.currentTimeMillis() - validTime > 23 * 60 * 60 * 1000) {  //有效期为1天
                            JSONObject root = new JSONObject();
                            root.put(Constant.USER_REGISTER_NUMBER, UserData.getUserData().getNumber());
                            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
                            String resultJson = HttpUtil.requestTokenString(Constant.HTTP_GET_TOKEN_STRING_URL, root.toString());
                            LogUtil.d("resultJson : " + resultJson);
                            if (resultJson != null) {
                                String errorCode = JsonUtil.parseErrorCode(resultJson);
                                if (errorCode.equals(Constant.MESSAGE_ERRORCODE_2000)) {
                                    String token = JsonUtil.parseToken(resultJson);
                                    SharedPreferences.Editor editor = context.getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
                                    editor.putString(Constant.USER_DATA_FIELD_TOKEN, token);
                                    editor.putLong(Constant.USER_DATA_FIELD_TOKENTIME, System.currentTimeMillis());
                                    editor.apply();
                                    LogUtil.d("token更新成功");
                                    close();
                                } else {
                                    LogUtil.d("token更新失败");
                                }
                            } else {
                                LogUtil.d("连接失败，token更新失败");
                            }
                        }
                        //获得连接
                        webSocketApplication.connection(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.d(Constant.WEBSOCKET_CONNECTION_EXCEPTION);
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

