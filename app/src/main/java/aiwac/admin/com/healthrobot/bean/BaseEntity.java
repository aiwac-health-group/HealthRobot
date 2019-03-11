package aiwac.admin.com.healthrobot.bean;

/**     基本的类，实现实体类共有的功能，如序列化等
 * Created by zyt on 2017/10/18.
 */

public class BaseEntity {
    public String businessType;    //事物类型，如：数据采集，数据查询等
    public String uuid;    //一次事务
    public String clientType = "robot";  // 客户端类型 1表示手机  2 表示后台
    public String time;    //采集的时间

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

    public String clientId = "22222222222";    //客户端id
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
}
