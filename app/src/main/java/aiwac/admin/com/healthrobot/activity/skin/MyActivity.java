package aiwac.admin.com.healthrobot.activity.skin;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;

/**
 * 我们希望，在测肤结果界面，
 * 点击返回按钮后，直接回到SkinMainActivity,销毁中间的DetectProcessActivity，CameraActivity等活动
 * 这个MyActivity类维护一个list，存放activity，在点击返回键时，逐个销毁list中的activity
 */
public class MyActivity extends Application {
    //创建一个集合，用来存放activity的对象
    ArrayList<Activity>list=new ArrayList<>();
    //声明一个本类的对象
    private static MyActivity instance;
    public MyActivity() {
    }
    //创建一个方法，用来初始化MyActivity的对象，并且初始化的对像的返回
    public synchronized static MyActivity getInstance(){
        if (instance==null){
            instance=new MyActivity();
        }
        return instance;
    }
    //调用此方法用来向集合当中添加activity对象
    public void addActivity(Activity activity){
        list.add(activity);
    }
    //判断activity是否已经在集合当中
    public boolean isexitlist(Activity activity){
        if (list.contains(activity)){
            return true;
        }
        return false;
    }
    //当调用此方法的时候，关闭所有的activity
    public void exit(){
        for (Activity activity:list){
            activity.finish();
        }
//        //退出当前的MyActivity
//        System.exit(0);
    }

//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        //当系统的存储空间不够的时候，调用系统的垃圾回收期，清理里面的垃圾
//        System.gc();
//    }

}
