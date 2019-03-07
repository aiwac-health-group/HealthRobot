package aiwac.admin.com.healthrobot.bean;

//使用EventBus发送的消息类型（用来向activity传递从后台收到的json串）
public class MessageEvent {

    private String to; //传给哪个activity
    private String message;

    public MessageEvent(String to, String message){
        this.to = to;
        this.message = message;
    }

    public  MessageEvent(String message){
        this.to = "";
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
