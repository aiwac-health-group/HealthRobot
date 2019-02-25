package aiwac.admin.com.healthrobot.exception;

/**     用于运行时数据库异常处理
 * Created by luwang on 2017/10/17.
 */

public class DBException extends RuntimeException {

    public DBException(){
        super();
    }

    public DBException(String msg){
        super(msg);
    }

    public DBException(String msg, Throwable cause){
        super(msg, cause);
    }


}
