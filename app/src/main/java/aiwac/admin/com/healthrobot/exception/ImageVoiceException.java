package aiwac.admin.com.healthrobot.exception;

/**     用于处理图片声音异常
 * Created by luwang on 2017/11/7.
 */

public class ImageVoiceException extends RuntimeException {

    public ImageVoiceException(){
        super();
    }

    public ImageVoiceException(String msg){
        super(msg);
    }

    public ImageVoiceException(String msg, Throwable cause){
        super(msg, cause);
    }

}
