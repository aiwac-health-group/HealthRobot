package aiwac.admin.com.healthrobot.exception;

/**     用于编码或者解码异常
 * Created by luwang on 2017/11/2.
 */

public class CodeException extends RuntimeException{
    public CodeException(){
        super();
    }

    public CodeException(String msg){
        super(msg);
    }

    public CodeException(String msg, Throwable cause){
        super(msg, cause);
    }
}
