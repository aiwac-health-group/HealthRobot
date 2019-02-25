package aiwac.admin.com.healthrobot.exception;

/**     用于Esptouch第三方库异常处理
 * Created by luwang on 2017/10/23.
 */

public class EsptouchException extends RuntimeException {

    public EsptouchException(){
        super();
    }

    public EsptouchException(String msg){
        super(msg);
    }

    public EsptouchException(String msg, Throwable cause){
        super(msg, cause);
    }

}
