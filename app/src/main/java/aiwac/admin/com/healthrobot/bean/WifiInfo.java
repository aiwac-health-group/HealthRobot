package aiwac.admin.com.healthrobot.bean;

/**     存放wifi相关的信息，如wifi名，ssid，bssid等
 * Created by lwuang on 2017/10/23.
 */

public class WifiInfo {

    //ScanResult类中的一些关于wifi的常量
    private String ssid;    //即wifi名
    private String bssid;
    private String capabilities;
    private int frequency;
    private int level;

    //wifi密码
    private String password;


    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
