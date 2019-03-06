package aiwac.admin.com.healthrobot.notification;

import zuo.biao.library.base.BaseModel;

public class Notification extends BaseModel {

    private int type=0;//1是健康周报  2是挂号信息
    private int messageID;//体检推荐ID或者周报ID，挂号id

    @Override
    protected boolean isCorrect() {
        return false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

}
