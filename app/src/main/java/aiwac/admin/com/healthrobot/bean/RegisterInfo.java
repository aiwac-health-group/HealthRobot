package aiwac.admin.com.healthrobot.bean;

public class RegisterInfo extends BaseEntity {
    private String province;
    private String city;
    private String hospital;
    private String department;

    public RegisterInfo(String province, String city, String hospital, String department){
        this.province = province;
        this.city = city;
        this.hospital = hospital;
        this.department = department;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
