package aiwac.admin.com.healthrobot.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import aiwac.admin.com.healthrobot.bean.TimerEntity;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.exception.DBException;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.utils.LogUtil;

/**     用于对timer表进行增删改查
 * Created by luwang on 2017/11/14.
 */

public class TimerSqliteHelper{


    private final static String TABLENAME = Constant.DB_TIMER_TABLENAME;
    private DBSqliteManager dbManager;

    public TimerSqliteHelper(DBSqliteManager dbManager){
        this.dbManager = dbManager;
    }

    public TimerSqliteHelper(Context context){
        this.dbManager = new DBSqliteManager(context);
    }

    public TimerSqliteHelper(Context context, String name){
        this.dbManager = new DBSqliteManager(context, name);
    }

    public TimerSqliteHelper(Context context, String name, int version){
        this.dbManager = new DBSqliteManager(context, name, version);
    }

    public void insert(TimerEntity timerEntity) {
        ContentValues values = new ContentValues();
        values.put("clientId", timerEntity.getClientId());
        values.put("businessType", timerEntity.getBusinessType());
        values.put("uuid", timerEntity.getUuid());
        values.put("clientType", timerEntity.getClientType());
        values.put("attentionType", timerEntity.getAttentionType());
        values.put("attentionContent", timerEntity.getAttentionContent());
        values.put("activationMode", timerEntity.getActivationMode());
        values.put("activatedTime", timerEntity.getActivatedTime());
        values.put("operationType", timerEntity.getOperationType());
        if(timerEntity.isOpen())
            values.put("isOpen", Constant.TIMER_OPEN + "");
        else
            values.put("isOpen", Constant.TIMER_CLOSE + "");
        if(!timerEntity.isCommit())
            values.put("isCommit", Constant.TIMER_UNCOMMIT + "");  //插入后要确认是否提交
        else{
            values.put("isCommit", Constant.TIMER_COMMIT + "");
        }

        //检测uuid是否是唯一的
        TimerEntity timer = getEntity(timerEntity.getUuid());
        if(timer != null ){
            update(timerEntity);
            return;
        }

        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getWritableDatabase();
            LogUtil.d(Constant.DB_OPEN_WRITE_CONNECTION);

            sdb.insert(TABLENAME, null, values);
            LogUtil.d(Constant.DB_INSERT + timerEntity);
        }catch (Exception e){
            LogUtil.d(Constant.DB_INSERT_EXCEPTIOIN);

            throw new DBException(Constant.DB_INSERT_EXCEPTIOIN, e);
        }finally{
            close(sdb, true);
        }

    }

    public void update(TimerEntity timerEntity) {
        ContentValues values = new ContentValues();
        values.put("clientId", timerEntity.getClientId());
        values.put("businessType", timerEntity.getBusinessType());
        values.put("clientType", timerEntity.getClientType());
        values.put("attentionType", timerEntity.getAttentionType());
        values.put("attentionContent", timerEntity.getAttentionContent());
        values.put("activationMode", timerEntity.getActivationMode());
        values.put("activatedTime", timerEntity.getActivatedTime());
        values.put("operationType", timerEntity.getOperationType());
        if(timerEntity.isOpen())
            values.put("isOpen", Constant.TIMER_OPEN + "");
        else
            values.put("isOpen", Constant.TIMER_CLOSE + "");
        values.put("isCommit", Constant.TIMER_UNCOMMIT + "");  //更新后就要重新确认是否提交

        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getWritableDatabase();
            LogUtil.d(Constant.DB_OPEN_WRITE_CONNECTION);

            //确保更新操作同步
            synchronized (WebSocketApplication.getWebSocketApplication()) {
                sdb.update(TABLENAME, values, "uuid=?", new String[]{timerEntity.getUuid()});
            }
            LogUtil.d(Constant.DB_UPDATE + timerEntity);
        }catch (Exception e){
            LogUtil.d(Constant.DB_UPDATE_EXCEPTIOIN);

            throw new DBException(Constant.DB_UPDATE_EXCEPTIOIN, e);
        }finally{
            close(sdb, true);
        }
    }

    //确认提交
    public void updateCommit(String uuid){
        ContentValues values = new ContentValues();
        values.put("isCommit", Constant.TIMER_COMMIT + "");  //更新后就要重新确认是否提交

        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getWritableDatabase();
            LogUtil.d(Constant.DB_OPEN_WRITE_CONNECTION);
            //确保更新操作同步
            synchronized (WebSocketApplication.getWebSocketApplication()) {
                sdb.update(TABLENAME, values, "uuid=?", new String[]{uuid});
            }
            LogUtil.d(Constant.DB_UPDATE_COMMIT + uuid);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.DB_UPDATE_COMMIT_EXCEPTION);
            throw new DBException(Constant.DB_UPDATE_COMMIT_EXCEPTION, e);
        }finally{
            close(sdb, true);
        }
    }


    public void delete(String uuid) {
        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getWritableDatabase();
            LogUtil.d(Constant.DB_OPEN_WRITE_CONNECTION);

            sdb.delete(TABLENAME, "uuid=?", new String[]{uuid});
            LogUtil.d(Constant.DB_DELETE + uuid);
        }catch (Exception e){
            LogUtil.d(Constant.DB_DELETE_EXCEPTIOIN);

            throw new DBException(Constant.DB_DELETE_EXCEPTIOIN, e);
        }finally{
            close(sdb, true);
        }
    }

    public List<TimerEntity> findAll(){
        List<TimerEntity> timerEntities = new ArrayList<TimerEntity>();

        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getReadableDatabase();
            LogUtil.d(Constant.DB_OPEN_READ_CONNECTION);

            Cursor cursor = sdb.query(TABLENAME, null, null, null, null, null, null);
            LogUtil.d("count : " + cursor.getCount());
            while (cursor != null && cursor.moveToNext()) {
                TimerEntity timerEntity = new TimerEntity();
                timerEntity.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
                timerEntity.setClientId(cursor.getString(cursor.getColumnIndex("clientId")));
                timerEntity.setBusinessType(cursor.getString(cursor.getColumnIndex("businessType")));
                timerEntity.setClientType(cursor.getString(cursor.getColumnIndex("clientType")));
                timerEntity.setAttentionType(cursor.getString(cursor.getColumnIndex("attentionType")));
                timerEntity.setAttentionContent(cursor.getString(cursor.getColumnIndex("attentionContent")));
                timerEntity.setActivationMode(cursor.getString(cursor.getColumnIndex("activationMode")));
                timerEntity.setActivatedTime(cursor.getString(cursor.getColumnIndex("activatedTime")));
                timerEntity.setOperationType(Integer.parseInt(cursor.getString(cursor.getColumnIndex("operationType"))));
                int isOpen = Integer.parseInt(cursor.getString(cursor.getColumnIndex("isOpen")));
                if(isOpen == Constant.TIMER_OPEN)
                    timerEntity.setOpen(true);
                else
                    timerEntity.setOpen(false);
                timerEntities.add(timerEntity);

                LogUtil.d(timerEntity.toString());
            }
            return timerEntities;
        }catch (Exception e){
            LogUtil.d(Constant.DB_QUERY_EXCEPTIOIN);

            throw new DBException(Constant.DB_QUERY_EXCEPTIOIN, e);
        }finally {
            close(sdb, false);
        }

    }

    public List<TimerEntity> findAllUncommitEntity(){
        List<TimerEntity> timerEntities = new ArrayList<TimerEntity>();

        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getReadableDatabase();
            LogUtil.d(Constant.DB_OPEN_READ_CONNECTION);

            //需要和确认提交那里同步
            synchronized (WebSocketApplication.getWebSocketApplication()) {
                Cursor cursor = sdb.query(TABLENAME, null, null, null, null, null, null);
                LogUtil.d("count : " + cursor.getCount());
                while (cursor != null && cursor.moveToNext()) {
                    int isCommit = Constant.TIMER_UNCOMMIT;
                    try {
                        isCommit = Integer.parseInt(cursor.getString(cursor.getColumnIndex("isCommit")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isCommit == Constant.TIMER_UNCOMMIT) {
                        TimerEntity timerEntity = new TimerEntity();
                        timerEntity.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
                        timerEntity.setClientId(cursor.getString(cursor.getColumnIndex("clientId")));
                        timerEntity.setBusinessType(cursor.getString(cursor.getColumnIndex("businessType")));
                        timerEntity.setClientType(cursor.getString(cursor.getColumnIndex("clientType")));
                        timerEntity.setAttentionType(cursor.getString(cursor.getColumnIndex("attentionType")));
                        timerEntity.setAttentionContent(cursor.getString(cursor.getColumnIndex("attentionContent")));
                        timerEntity.setActivationMode(cursor.getString(cursor.getColumnIndex("activationMode")));
                        timerEntity.setActivatedTime(cursor.getString(cursor.getColumnIndex("activatedTime")));
                        timerEntity.setOperationType(Integer.parseInt(cursor.getString(cursor.getColumnIndex("operationType"))));
                        int isOpen = Integer.parseInt(cursor.getString(cursor.getColumnIndex("isOpen")));
                        if (isOpen == Constant.TIMER_OPEN)
                            timerEntity.setOpen(true);
                        else
                            timerEntity.setOpen(false);
                        timerEntity.setCommit(false);
                        timerEntities.add(timerEntity);

                        LogUtil.d(timerEntity.toString());
                    }
                }
            }
            return timerEntities;
        } catch (Exception e) {
            LogUtil.d(Constant.DB_QUERY_EXCEPTIOIN);

            throw new DBException(Constant.DB_QUERY_EXCEPTIOIN, e);
        } finally {
            close(sdb, false);
        }
    }

    public TimerEntity getEntity(String uuid) {
        TimerEntity timerEntity = null;
        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getReadableDatabase();
            LogUtil.d(Constant.DB_OPEN_READ_CONNECTION);

            Cursor cursor = sdb.query(TABLENAME, new String[]{"clientId", "businessType", "clientType", "attentionType",
                                            "attentionContent", "activationMode", "activatedTime", "operationType", "isOpen"},
                                    "uuid=?", new String[]{uuid}, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                timerEntity = new TimerEntity();
                timerEntity.setUuid(uuid);
                timerEntity.setClientId(cursor.getString(cursor.getColumnIndex("clientId")));
                timerEntity.setBusinessType(cursor.getString(cursor.getColumnIndex("businessType")));
                timerEntity.setClientType(cursor.getString(cursor.getColumnIndex("clientType")));
                timerEntity.setAttentionType(cursor.getString(cursor.getColumnIndex("attentionType")));
                timerEntity.setAttentionContent(cursor.getString(cursor.getColumnIndex("attentionContent")));
                timerEntity.setActivationMode(cursor.getString(cursor.getColumnIndex("activationMode")));
                timerEntity.setActivatedTime(cursor.getString(cursor.getColumnIndex("activatedTime")));
                timerEntity.setOperationType(Integer.parseInt(cursor.getString(cursor.getColumnIndex("operationType"))));
                int isOpen = Integer.parseInt(cursor.getString(cursor.getColumnIndex("isOpen")));
                if(isOpen == Constant.TIMER_OPEN)
                    timerEntity.setOpen(true);
                else
                    timerEntity.setOpen(false);

                LogUtil.d(timerEntity.toString());
                return timerEntity;
            }
        }catch (Exception e){
            LogUtil.d(Constant.DB_QUERY_EXCEPTIOIN);

            throw new DBException(Constant.DB_QUERY_EXCEPTIOIN, e);
        }finally {
            close(sdb, false);
        }
        return null;
    }

    //flag：true写连接 false：读连接
    private void close(SQLiteDatabase sdb, boolean flag){
        if(sdb != null && sdb.isOpen()) {
            sdb.close();
            if (flag)
                LogUtil.d(Constant.DB_CLOSE_WRITE_CONNECTION);
            else
                LogUtil.d(Constant.DB_CLOSE_READ_CONNECTION);

        }
    }


}
