package aiwac.admin.com.healthrobot.bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
@SmartTable(name="挂号信息列表")
public class RegisterInfo extends BaseEntity {

    @SmartColumn(id =1,name = "创建时间")
    private String createTime;
    @SmartColumn(id =2,name = "省份")
    private String province;
    @SmartColumn(id =3,name = "城市")
    private String city;
    @SmartColumn(id =4,name = "医院")
    private String hospital;
    @SmartColumn(id =5,name = "科室")
    private String department;
    @SmartColumn(id =6,name = "挂号状态")
    private String registerStatus;
    @SmartColumn(id =7,name = "备注")
    private String description;
    @SmartColumn(id =8,name = "更新时间")
    private String updateTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createTime);
            if(date!=null){
                String showDate="";
                showDate+=(date.getYear()+1900)+"年"+date.getMonth()+"月"+date.getDay()+"日  "
                        +(date.getHours()==0?"00":date.getHours())+ ":"
                        +(date.getMinutes()==0?"00":date.getMinutes())+":"
                        +(date.getSeconds()==0?"00":date.getSeconds());

                this.createTime = showDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RegisterInfo(String province, String city, String hospital, String department){
        this.province = province;
        this.city = city;
        this.hospital = hospital;
        this.department = department;
    }

    public RegisterInfo(){}


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
