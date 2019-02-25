package aiwac.admin.com.healthrobot.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import aiwac.admin.com.healthrobot.bean.User;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.exception.DBException;

/**     对用户表的增删改查
 * Created by luwang on 2017/10/18.
 */

public class UserSqliteHelper implements SQLiteHelper<User> {

    private final static String LOG_TAG = "UserSqliteHelper";
    private final static String tableName = Constant.DB_USER_TABLENAME;
    private DBSqliteManager dbManager;

    public UserSqliteHelper(DBSqliteManager dbManager){
        this.dbManager = dbManager;
    }

    public UserSqliteHelper(Context context){
        this.dbManager = new DBSqliteManager(context);
    }

    public UserSqliteHelper(Context context, String name){
        this.dbManager = new DBSqliteManager(context, name);
    }

    public UserSqliteHelper(Context context, String name, int version){
        this.dbManager = new DBSqliteManager(context, name, version);
    }

    @Override
    public void insert(User user) {
        ContentValues values = new ContentValues();
        values.put("password", user.getPassword());
        values.put("number", user.getNumber());

        //检测手机号码是否是唯一的
        User u = getUser(user.getNumber());
        if(u != null ){
            throw new DBException(Constant.DB_NUMBER_EXCEPTION);
        }

        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getWritableDatabase();
            Log.d(LOG_TAG, Constant.DB_OPEN_WRITE_CONNECTION);

            sdb.insert(tableName, null, values);
            Log.d(LOG_TAG, Constant.DB_INSERT + user);
        }catch (Exception e){
            Log.d(LOG_TAG, Constant.DB_INSERT_EXCEPTIOIN);

            throw new DBException(Constant.DB_INSERT_EXCEPTIOIN, e);
        }finally{
            close(sdb, true);
        }

    }

    @Override
    public void update(User user) {
        SQLiteDatabase sdb = null;
        try {
            ContentValues values = new ContentValues();
            values.put("password", user.getPassword());
            sdb = dbManager.getWritableDatabase();
            Log.d(LOG_TAG, Constant.DB_OPEN_WRITE_CONNECTION);

            sdb.update(tableName, values,"number = ?",new String[]{"user.getNumber()"});
            Log.d(LOG_TAG, Constant.DB_UPDATE + user);
        }catch (Exception e){
            Log.d(LOG_TAG, Constant.DB_UPDATE_EXCEPTIOIN);
            throw new DBException(Constant.DB_UPDATE_EXCEPTIOIN, e);
        }finally{
            close(sdb, true);
        }
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User getEntity(Integer id) {
        User user = null;
        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getReadableDatabase();
            Log.d(LOG_TAG, Constant.DB_OPEN_READ_CONNECTION);

            Cursor cursor = sdb.query(tableName, new String[]{"password", "number"}, "id = ?", new String[]{id.toString()}, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String number = cursor.getString(cursor.getColumnIndex("number"));
                user = new User(id, number, password);
                Log.d(LOG_TAG, user.toString());
                return user;
            }
        }catch (Exception e){
            Log.d(LOG_TAG, Constant.DB_QUERY_EXCEPTIOIN);

            throw new DBException(Constant.DB_QUERY_EXCEPTIOIN, e);
        }finally {
            close(sdb, false);
        }
        return null;
    }

    private void close(SQLiteDatabase sdb, boolean flag){
        if(sdb != null && sdb.isOpen()) {
            sdb.close();
            if (flag)
                Log.d(LOG_TAG, Constant.DB_CLOSE_WRITE_CONNECTION);
            else
                Log.d(LOG_TAG, Constant.DB_CLOSE_READ_CONNECTION);

        }
    }

    public User getUser(String number){
        User user = null;
        SQLiteDatabase sdb = null;
        try {
            sdb = dbManager.getReadableDatabase();
            Log.d(LOG_TAG, Constant.DB_OPEN_READ_CONNECTION);

            Cursor cursor = sdb.query(tableName, new String[]{"id", "password", "number"}, "number = ?", new String[]{number}, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                user = new User(id, number,password );
                Log.d(LOG_TAG, user.toString());
                return user;
            }
        }catch (Exception e){
            Log.d(LOG_TAG, Constant.DB_QUERY_EXCEPTIOIN);
            e.printStackTrace();

            //throw new DBException(Constant.DB_QUERY_EXCEPTIOIN, e);
        }finally {
            close(sdb, false);
        }
        return null;
    }

}
