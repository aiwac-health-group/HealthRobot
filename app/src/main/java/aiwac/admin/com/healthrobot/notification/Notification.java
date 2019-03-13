package aiwac.admin.com.healthrobot.notification;

import android.support.annotation.NonNull;

import zuo.biao.library.base.BaseModel;

public class Notification extends BaseModel implements Comparable<Notification> {
    private int notificationId;//消息的id
    private int isRead = 0;//1已读 0未读
    private int messageType=0;//0表示新的体检推荐,1是健康周报  2是挂号信息
    private int messageID;//体检推荐ID或者周报ID，挂号id

    @Override
    protected boolean isCorrect() {
        return false;
    }


    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "这是一个消息的内容：id:"+notificationId+"  messageid:"+ messageID+ "   messageType:"+messageType+"   isRead:"+ isRead+"";
    }

    /**
     * 这个是用于排序的比较函数
     * @param o
     * @return
     */
    @Override
    public int compareTo(@NonNull Notification o) {
        int i=o.getNotificationId()-this.getNotificationId();
        return i;
    }
}
