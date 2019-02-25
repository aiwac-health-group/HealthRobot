package aiwac.admin.com.healthrobot.bean;

/**     实体对象
 * Created by luwang on 2017/10/17.
 */

public class User {

    private Integer id;
    private String name;
    private String number;
    private String password;


    public User(){

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
