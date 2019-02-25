package aiwac.admin.com.healthrobot.bean;


import java.io.Serializable;

import aiwac.admin.com.healthrobot.common.Constant;

/**     用于描述消息对象
 * Created by luwang on 2017/11/1.
 */

public class MessageInfo implements Serializable{

    private String clientId;
    private String businessType = Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE_CODE;
    private String uuid;
    private String clientType;
    private String time;
    private String description;

    private int emotionValue; //该UUID所对应的那批事务 情感标记值


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEmotionValue() {
        return emotionValue;
    }

    public void setEmotionValue(int emotionValue) {
        this.emotionValue = emotionValue;
    }
}
