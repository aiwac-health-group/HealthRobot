package aiwac.admin.com.healthrobot.utils;

import android.util.Base64;
import android.util.Log;

import aiwac.admin.com.healthrobot.common.Constant;

/**     编码解码类
 * Created by luwang on 2017/11/1.
 */

public class CodeUtil {

    public static byte[] decode(String base64Str){
        try{
            return Base64.decode(base64Str, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.CODE_DECODE_EXCEPTION);
            throw new RuntimeException(Constant.CODE_DECODE_EXCEPTION, e);
        }

    }


    public static String encode(byte[] bytes){
        try{
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.CODE_ENCODE_EXCEPTION);
            throw new RuntimeException(Constant.CODE_ENCODE_EXCEPTION, e);
        }
    }

}
