package aiwac.admin.com.healthrobot.exception;

/**     Http异常类，用于处理连接服务器异常
 * Created by luwang on 2017/10/24.
 */

public class HttpException extends RuntimeException {

    public HttpException(){
        super();
    }

    public HttpException(String msg){
        super(msg);
    }

    public HttpException(String msg, Throwable cause){
        super(msg, cause);
    }


}
