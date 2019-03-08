package aiwac.admin.com.healthrobot.bean;

public class LectureDetail {


    protected String clientID;
    protected String businessType;
    protected String clientType;
    protected String uniqueID;


    public String getLectureID() {
        return clientID;
    }
    public void setLectureID(String clientID) {
        this.clientID = clientID;
    }

    public String getBusinessTyp() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getClientType() {
        return clientType;
    }
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getUniqueID() {
        return uniqueID;
    }
    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

}
