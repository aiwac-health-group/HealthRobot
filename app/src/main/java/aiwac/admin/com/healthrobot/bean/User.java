package aiwac.admin.com.healthrobot.bean;

/**     实体对象
 * Created by luwang on 2017/10/17.
 */

public class User extends BaseEntity{

    private Integer id;
    private String name;
    private String number;
    private String password;
    private String sex;
    private String birthday;
    private String wechat;
    private String place ;//plac = area+address

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public User(){}

    public User(String number){
        this.number = number;
    }

    public User(String name, String number){
        this.name = name;
        this.number = number;
    }

    public User(int id, String name, String number){
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User["+ id + ", " + name + ", " + number +"]";
    }
}
