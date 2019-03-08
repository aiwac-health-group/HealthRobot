package aiwac.admin.com.healthrobot.notification;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aiwac.admin.com.healthrobot.db.NotificationSqliteHelper;


public class GetNotificationUtil {


    public static List<Notification> list = new ArrayList<Notification>();

    NotificationSqliteHelper notifiSqlHelper = null;



    public GetNotificationUtil(Context context){
        notifiSqlHelper=new NotificationSqliteHelper(context);
        initListFromLocal();
    }

    /**
     * 获取本地所有消息通知
     */
    private void initListFromLocal(){
        list = notifiSqlHelper.getAllNotification();

        //进行排序
        Collections.sort(list);
    }

    public static List<Notification> getUserList() {
        return getUserList(0);
    }
    /**
     * @param page 页码
     * @return
     */
    public static List<Notification> getUserList(int page) {
        return getUserList(page, 10);
    }
    /**
     * @param page 页码
     * @param count 最大一页数量
     * @return
     */
    public static List<Notification> getUserList(int page, int count) {
        list=textGetExam();
        return list;
    }

    private static List<Notification> textGetExam(){
        List<Notification> list = new ArrayList<Notification>();
        Notification notification;
        for(int i=0;i<20;i++){
            notification=new Notification();
            notification.setMessageID(i+1);
            if(i%2==0){
                notification.setMessageType(1);
                notification.setIsRead(0);
            }else{
                notification.setMessageType(2);
                notification.setIsRead(1);
            }
            list.add(notification);
        }
        return list;
    }

    public static List<Notification> getList() {
        return list;
    }
}
