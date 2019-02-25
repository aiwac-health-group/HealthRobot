package aiwac.admin.com.healthrobot.exception;

/**     文件操作异常
 * Created by luwang on 2017/11/3.
 */

public class FileException extends RuntimeException{

    public FileException(){
        super();
    }

    public FileException(String msg){
        super(msg);
    }

    public FileException(String msg, Throwable cause){
        super(msg, cause);
    }


}
