package aiwac.admin.com.healthrobot.bean;


import java.io.Serializable;

import aiwac.admin.com.healthrobot.common.Constant;

/**     用于描述定时任务
 * Created by luwang on 2017/11/13.
 */

public class TimerEntity implements Serializable{

    private String clientId;
    private String businessType = Constant.WEBSOCKET_TIMER_BUSSINESSTYPE_CODE;
    private String uuid;
    private String clientType;

    private String activationMode; //activationMode:0000001代表每个星期一都响铃, 1000000表示每个星期天都响，0000000表示只响一次
    private String activatedTime; //00:00
    private String attentionType; //标识提醒业务的类型,自定义类型定义为”99”
    private String attentionContent; // str提醒的内容
    private int operationType; //operationType:1 add 2 update 3 delete

    private boolean isOpen; //是否开启定时任务
    private boolean isCommit; //是否提交到后台

    public TimerEntity(){
        operationType = Constant.TIMER_OPERATIONTYPE_ADD;
        isOpen = true;
        isCommit = false;
    }

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

    public String getActivationMode() {
        return activationMode;
    }

    public void setActivationMode(String activationMode) {
        this.activationMode = activationMode;
    }

    public String getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(String activatedTime) {
        this.activatedTime = activatedTime;
    }

    public String getAttentionType() {
        return attentionType;
    }

    public void setAttentionType(String attentionType) {
        this.attentionType = attentionType;
    }

    public String getAttentionContent() {
        return attentionContent;
    }

    public void setAttentionContent(String attentionContent) {
        this.attentionContent = attentionContent;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isCommit() {
        return isCommit;
    }

    public void setCommit(boolean commit) {
        isCommit = commit;
    }

    @Override
    public String toString() {
        return "TimerEntity[" + uuid + ", " + activatedTime + ", " + attentionContent + ", " + activationMode +"]";
    }
}
