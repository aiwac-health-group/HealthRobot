package aiwac.admin.com.healthrobot.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.baidu.aip.asrwakeup3.core.mini.AutoCheck;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * AndroidManifest.xml需要加上权限
 * 需要在core的AndroidManifest.xml设置好appid，API_KEY，SECRET_KEY
 * core的assets目录下把baidu_speech_grammar.bsg设置成你要的语法，
 * 获取地址为：https://console.bce.baidu.com/ai/?_=1551840630539&fromai=1#/ai/speech/speech/asr/index
 */

public class SpeechRecogService extends Service implements EventListener {
    private static final String TAG="ServiceSpeechRecog";

    private EventManager asr;
    private boolean keepRecog=false;
    private boolean logTime = true;
    private int decoderType=1;//0--纯在线  2---离线在线都有，在线优先  其他---离在线的并行策略
    protected boolean enableOffline = true; // 测试离线命令词，需要改成true

    public SpeechRecogService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        decoderType=intent.getIntExtra("decoderType",1);
        enableOffline=intent.getBooleanExtra("enableOffline",true);
        return myBinder;//注意这里返回值不能为null  否则在activity中绑定时不会触发onServiceConnected()
    }

    @Override
    public void onCreate() {
        super.onCreate();
        keepRecog=true;
        // 基于sdk集成1.1 初始化EventManager对象
        asr = EventManagerFactory.create(this, "asr");
        // 基于sdk集成1.3 注册自己的输出事件类
        asr.registerListener(this); //  EventListener 中 onEvent方法

        //开启离线引擎
        if (enableOffline) {
            loadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }

        //开启识别
        start();
    }



    public void startRecog(){
        this.start();
    }

    /**
     * 基于SDK集成2.2 发送开始事件
     * 点击开始按钮
     * 测试参数填在这里
     */
    private void start() {
        keepRecog=true;
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        if (enableOffline) {
            params.put(SpeechConstant.DECODER,decoderType);//0--纯在线  2---离线在线都有，在线优先  其他---离在线的并行策略
        }
        // 基于SDK集成2.1 设置识别参数
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");

        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        (new AutoCheck(getApplicationContext(), new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                        //txtLog.append(message + "\n");
                        ; // 可以用下面一行替代，在logcat中查看代码
                        Log.w("AutoCheckMessage", message);
                    }
                }
            }
        },enableOffline)).checkAsr(params);
        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);
        printLog("输入参数：" + json);
    }

    // 基于sdk集成1.2 自定义输出事件类 EventListener 回调方法
    // 基于SDK集成3.1 开始回调事件
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logTxt = "name: " + name;
        if("asr.exit".equals(name)&&keepRecog==true){
            asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
            start();
        }
        if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)){
            if (params != null && !params.isEmpty()){
                Log.d(TAG, "this:"+params);
                //RecogResult recogResult = RecogResult.parseJson(params);
                //Log.d(TAG, "结果是："+recogResult.getResultsRecognition());
                try {
                    /*JSONObject json = new JSONObject(params);
                    Log.d(TAG, "结果是："+json.getString("origin_result"));
                    JSONObject jcon = json.getJSONObject("origin_result");
                    String txt=jcon.getJSONObject("content").getString("item");
                    Log.d(TAG, "最终结果是："+txt+" :"+txt.length());*/
                    JSONObject json = new JSONObject(params);
                    Log.d(TAG, "结果是："+json.getString("results_recognition"));
                    callback.speechRecogResult(json.getString("results_recognition").split("\"")[1]);

                } catch (JSONException e) {
                    callback.speechRecogResult("");
                    e.printStackTrace();
                }
            }
        }


        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            if (params != null && params.contains("\"nlu_result\"")) {
                if (length > 0 && data.length > 0) {
                    logTxt += ", 语义解析结果：" + new String(data, offset, length);
                }
            }
        } else if (data != null) {
            logTxt += " ;data length=" + data.length;
        }
        printLog(logTxt);

        Log.d(TAG, "onEvent: "+logTxt);
    }
    /**
     * enableOffline设为true时，在onCreate中调用
     * 基于SDK离线命令词1.4 加载离线资源(离线时使用)
     */
    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    /**
     * enableOffline为true时，在onDestory中调用，与loadOfflineEngine对应
     * 基于SDK集成5.1 卸载离线资源步骤(离线时使用)
     */
    private void unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0); //
    }

    /**
     * 点击停止按钮
     *  基于SDK集成4.1 发送停止事件
     */
    public void stop() {
        printLog("停止识别：ASR_STOP");
        keepRecog=false;
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }



/*    @Override
    protected void onPause(){
        super.onPause();
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        Log.i("ActivityMiniRecog","On pause");
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 基于SDK集成4.2 发送取消事件
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (enableOffline) {
            unloadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }

        // 基于SDK集成5.2 退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr.unregisterListener(this);
    }

    private void printLog(String text) {
        if (logTime) {
            text += "  ;time=" + System.currentTimeMillis();
        }
        text += "\n";
        Log.i(getClass().getName(), text);
    }

    private MyBinder myBinder = new MyBinder();

    public  class MyBinder extends Binder {
        public SpeechRecogService getService(){
            return SpeechRecogService.this;
        }
    }
    private Callback callback = null;
    public void setCallBack(Callback callback){//注意这里以单个回调为例  如果是向多个activity传送数据 可以定义一个回调集合 在此处进行集合的添加
        this.callback =callback;
    }
    /**
     * 定义回调接口
     */
    public interface Callback{
        public void speechRecogResult(String result);
    }

}
