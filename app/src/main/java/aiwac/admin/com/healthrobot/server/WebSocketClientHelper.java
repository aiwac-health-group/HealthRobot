package aiwac.admin.com.healthrobot.server;


import android.content.Context;
import android.content.SharedPreferences;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import aiwac.admin.com.healthrobot.bean.MessageInfo;
import aiwac.admin.com.healthrobot.bean.TimerEntity;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.TimerSqliteHelper;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import aiwac.admin.com.healthrobot.utils.StringUtil;

/**     用于WebSocket客户端通信
 * Created by luwang on 2017/10/31.
 */

public class WebSocketClientHelper extends WebSocketClient {

    private Context context;

    private List<MessageInfo> messageInfos = Collections.synchronizedList(new ArrayList<MessageInfo>());


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    
    

    public WebSocketClientHelper(URI serverUri, Map<String, String> httpHeaders, Context context) {
        this(serverUri, new Draft_6455(), httpHeaders, 0, context);
        //LogUtil.d( "serverUri  : " + serverUri);
    }

    public WebSocketClientHelper(URI serverUri, Draft draft, Map<String, String> httpHeaders, Context context) {
        this(serverUri, draft, httpHeaders, 0, context);
    }

    public WebSocketClientHelper(URI serverUri, Draft draft, Map<String, String> httpHeaders, int connectionTimeout, Context context) {
        super(serverUri,draft,httpHeaders,connectionTimeout);

        //获取全局唯一的context对象，否则activity不能销毁问题
        this.context = context.getApplicationContext();
    }



    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_OPEN + getRemoteSocketAddress());

        //开启连接的时候检查要不要同步数据
        checkSyncTimer();
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_CLOSE + i + s + b);
        WebSocketApplication.getWebSocketApplication().setNull();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_EXCEPTION);
    }

    @Override
    public void onMessage(final String json) {
        //处理具体逻辑

       LogUtil.printJson( Constant.WEBSOCKET_MESSAGE_FROM_SERVER ,json,"##");

        try{
            String businessType = JsonUtil.parseBusinessType(json);
            if(businessType.equals(Constant.WEBSOCKET_VOICECHAT_BUSSINESSTYPE_CODE)){  //在线问诊房间号
                EventBus.getDefault().post(json);
            }else if(businessType.equals(Constant.WEBSOCKET_REGISTERRESULT_BUSSINESSTYPE_CODE)) { //语音挂号结果

            }

        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( "onMessage : " + e.getMessage());

        }

    }

    //判断是否重新安装，是否需要同步
    private void checkSyncTimer(){
        SharedPreferences pref = context.getSharedPreferences(Constant.DB_USER_TABLENAME, Context.MODE_PRIVATE);
        String timerSync = pref.getString(Constant.USER_DATA_FIELD_TIMER_SYNC, "");
        if(!StringUtil.isValidate(timerSync)) { // 新的线程中发送同步请求
            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        //发送同步请求
                        String json = JsonUtil.timerSync2Json();
                        send(json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    private void saveSyncTimer(final String json){
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    //处理将定时提醒数据保存到数据库
                    List<TimerEntity> timerEntities = JsonUtil.parseTimerSync(json);
                    TimerSqliteHelper timerSqliteHelper = new TimerSqliteHelper(context);
                    for(TimerEntity timerEntity : timerEntities){
                        timerSqliteHelper.insert(timerEntity);
                    }

                    //设置为已经同步
                    SharedPreferences.Editor editor = context.getSharedPreferences(Constant.DB_USER_TABLENAME, context.MODE_PRIVATE).edit();
                    editor.putString(Constant.USER_DATA_FIELD_TIMER_SYNC, Constant.USER_DATA_FIELD_TIMER_SYNC_VALUE);
                    editor.apply();
                    LogUtil.d( Constant.USER_DATA_PERSISTENCE_TIMER_SYNC);
                }catch (Exception e){
                    LogUtil.d( "saveSyncTimer : " + e.getMessage());
                }
            }
        });
    }


    public List<MessageInfo> getMessageInfos(){
        return messageInfos;
    }







}


