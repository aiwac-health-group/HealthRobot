package aiwac.admin.com.healthrobot.exception;

/**     用于解析和生成Json字符串异常
 * Created by luwang on 2017/10/23.
 */

public class JsonException extends RuntimeException{

    public JsonException(){
        super();
    }

    public JsonException(String msg){
        super(msg);
    }

    public JsonException(String msg, Throwable cause){
        super(msg, cause);
    }


}
