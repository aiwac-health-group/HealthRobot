package aiwac.admin.com.healthrobot.db;

import android.app.Application;
import android.content.Context;

/**     用于获取android的全局context对象
 * Created by luwang on 2017/12/11.
 */

public class AiwacApplication extends Application{

    private static Context context;
    //登录用户信息
    private Long uid;
    private String token;


    /**
     * Application单例
     */
    private static AiwacApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sInstance = this;
    }

    public static Context getContext(){

        return context;
    }

    /**
     * @return Application实例
     */
    public static AiwacApplication getInstance() {
        return sInstance;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Long getUid() {
        return uid;
    }

    public boolean isLogin(){
        return token!=null;
    }

}
