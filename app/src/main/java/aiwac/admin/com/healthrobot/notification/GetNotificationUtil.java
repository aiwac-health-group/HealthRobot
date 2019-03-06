package aiwac.admin.com.healthrobot.notification;

import java.util.ArrayList;
import java.util.List;


public class GetNotificationUtil {

    public static List<Notification> getList() {
        return list;
    }

    public static List<Notification> list = new ArrayList<Notification>();

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
                notification.setType(1);
            }else{
                notification.setType(2);
            }
            list.add(notification);
        }
        return list;
    }

}
