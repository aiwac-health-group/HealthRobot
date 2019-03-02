package aiwac.admin.com.healthrobot.bean;

//使用EventBus发送的消息类型（用来向activity传递从后台收到的json串）
public class MessageEvent {

    private String message;

    public MessageEvent(){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
