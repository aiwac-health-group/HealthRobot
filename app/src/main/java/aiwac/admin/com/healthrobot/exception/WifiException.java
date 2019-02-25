package aiwac.admin.com.healthrobot.exception;

/**     用于处理wifi异常的情况
 * Created by luwang on 2017/10/16.
 */

public class WifiException extends RuntimeException {

    public WifiException(){
        super();
    }

    public WifiException(String msg){
        super(msg);
    }

    public WifiException(String msg, Throwable cause){
        super(msg, cause);
    }


}
