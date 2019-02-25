package aiwac.admin.com.healthrobot.exception;

/**     用于处理websocket通信异常
 * Created by luwang on 2017/10/31.
 */

public class WebSocketException extends RuntimeException{

    public WebSocketException(){
        super();
    }

    public WebSocketException(String msg){
        super(msg);
    }

    public WebSocketException(String msg, Throwable cause){
        super(msg, cause);
    }

}
