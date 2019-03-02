package aiwac.admin.com.healthrobot.BaiduSpeechSynthesizer;

import android.text.TextUtils;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import aiwac.admin.com.healthrobot.HealthRobotApplication;
import aiwac.admin.com.healthrobot.utils.LogUtil;

/**
 * Created by Admin on 2018/4/10.
 */

public class SpeechSyntherizer {

    protected static String appId = "11005757";

    protected static String appKey = "Ovcz19MGzIKoDDb3IsFFncG1";

    protected static String secretKey = "e72ebb6d43387fc7f85205ca7e6706e2";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected static TtsMode ttsMode = TtsMode.MIX;

    protected static String offlineVoice = OfflineResource.VOICE_MALE;

    protected static MySyntherizer synthesizer;

    private static SpeechSyntherizer speechSyntherizer;

    private SpeechSyntherizer(){

    }


    static  {

        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new MySynthesizerListener();
        Map<String, String> params = getParams();

        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode,  params, listener);
        synthesizer = new MySyntherizer( HealthRobotApplication.getContext(), initConfig);
    }


    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected static Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "4");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
        params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                offlineResource.getModelFilename());
        return params;
    }


    protected static OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(HealthRobotApplication.getContext(), voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            LogUtil.d("【error】:copy files from assets failed." + e.getMessage());
        }
        return offlineResource;
    }

    /**
     * 合成但是不播放，
     * 音频流保存为文件的方法可以参见SaveFileActivity及FileSaveListener
     */
    public static void synthesize(String text) {
        setSpeaker();
        int result = synthesizer.synthesize(text);
        checkResult(result, "synthesize");
    }

    /**合成并播放
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    public static void speak(String text) {
        setSpeaker();
        // 需要合成的文本text的长度不能超过1024个GBK字节。
        if (TextUtils.isEmpty(text)) {
            text = "欢迎使用健康机器人";
        }
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // synthesizer.setParams(params);
        int result = synthesizer.speak(text);
        checkResult(result, "speak");
    }

    private static void checkResult(int result, String method) {
        if (result != 0) {
            LogUtil.d("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    public static  void setSpeaker(){
        /*
        //根据用户设置选择发音人
        String sex = BasePropertiesConfig.getPropertiesConfig().getString(Constant.CONSTANT_VOICE_TYPE, Constant.GIRL_VOICE_TYPE);
        if(sex.equals("00")){
            synthesizer.setSpeaker("4");
        }else{
            synthesizer.setSpeaker("3");
        }*/
        //这里默认选择普通女生
        synthesizer.setSpeaker("0");

    }

}
