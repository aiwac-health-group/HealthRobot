package aiwac.admin.com.healthrobot.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.exception.DBException;
import aiwac.admin.com.healthrobot.notification.Notification;

public class NotificationSqliteHelper implements SQLiteHelper<Notification> {
    private final static String LOG_TAG = "NotifSqliteHelper";
    private final static String tableName = Constant.DB_Notification;
    private DBSqliteManager dbManager;


    public NotificationSqliteHelper(DBSqliteManager dbManager){
        this.dbManager = dbManager;
    }

    public NotificationSqliteHelper(Context context){
        this.dbManager = new DBSqliteManager(context);
    }

    public NotificationSqliteHelper(Context context, String name){
        this.dbManager = new DBSqliteManager(context, name);
    }

    public NotificationSqliteHelper(Context context, String name, int version){
        this.dbManager = new DBSqliteManager(context, name, version);
    }

    /**
     * 插入一条新的通知
     * @param notification
     */
    @Override
    public void insert(Notification notification) {
        ContentValues values = new ContentValues();
        values.put("messageid", notification.getMessageID());
        values.put("messagetype", notification.getMessageType());
        values.put("isread", notification.getIsRead());
        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getWritableDatabase();
            Log.d(LOG_TAG, Constant.DB_OPEN_WRITE_CONNECTION);

            sdb.insert(tableName, null, values);
            Log.d(LOG_TAG, Constant.DB_INSERT + notification);
        }catch (Exception e){
            Log.d(LOG_TAG, Constant.DB_INSERT_EXCEPTIOIN);

            throw new DBException(Constant.DB_INSERT_EXCEPTIOIN, e);
        }finally{
            close(sdb, true);
        }
    }

    /**
     * 更新
     * @param notification
     */
    @Override
    public void update(Notification notification) {
        SQLiteDatabase sdb = null;
        try {
            ContentValues values = new ContentValues();
            values.put("messageid", notification.getMessageID());
            values.put("messagetype", notification.getMessageType());
            values.put("isread", notification.getIsRead());
            sdb = dbManager.getWritableDatabase();
            Log.d(LOG_TAG, Constant.DB_OPEN_WRITE_CONNECTION);

            sdb.update(tableName, values,"notificationid = ?",new String[]{"notification.getNotificationId()"});
            Log.d(LOG_TAG, Constant.DB_UPDATE + notification);
        }catch (Exception e){
            Log.d(LOG_TAG, Constant.DB_UPDATE_EXCEPTIOIN);
            throw new DBException(Constant.DB_UPDATE_EXCEPTIOIN, e);
        }finally{
            close(sdb, true);
        }
    }




    @Override
    public void delete(Notification notification) {

    }

    @Override
    public Notification getEntity(Integer id) {
        return null;
    }

    /**
     * 获取所有消息通知
     * @return
     */
    public List<Notification> getAllNotification(){
        List<Notification> list  = new ArrayList<Notification>();
        SQLiteDatabase sdb = null;
        sdb = dbManager.getWritableDatabase();
        Cursor cursor =sdb.query(tableName, null, null, null, null, null, null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                Notification notification=new Notification();
                notification.setNotificationId(cursor.getInt(0));
                notification.setMessageID(cursor.getInt(1));
                notification.setMessageType(cursor.getInt(2));
                notification.setIsRead(cursor.getInt(3));
                list.add(notification);
            }
        }
        return list;
    }

    private void close(SQLiteDatabase sdb, boolean flag){
        if(sdb != null && sdb.isOpen()) {
            sdb.close();
            if (flag) {
                Log.d(LOG_TAG, Constant.DB_CLOSE_WRITE_CONNECTION);
            } else {
                Log.d(LOG_TAG, Constant.DB_CLOSE_READ_CONNECTION);
            }

        }
    }

}
