package aiwac.admin.com.healthrobot.db;

/**     SQLite数据库工具类, 主要用于创建数据库和表
 * Created by luwang on 2017/10/16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.exception.DBException;
import aiwac.admin.com.healthrobot.utils.StringUtil;

public class DBSqliteManager extends SQLiteOpenHelper{

    private final static String LOG_TAG = "DBSqliteManager";
    private Context context=null;
    private static int version;
    private static String name; //数据库名称

    public DBSqliteManager(Context context) {
        this(context, Constant.DB_NAME);

        this.context = context;
    }

    public DBSqliteManager(Context context, String name) {
        this(context,name, Constant.DB_VERSION);

        this.context = context;
        this.name = name;
    }

    public DBSqliteManager(Context context, String name, int version) {
        this(context,name,null,version);

        this.context = context;
        this.name = name;
        this.version = version;
    }

    public DBSqliteManager(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);

        this.name = name;
        this.context = context;
        this.version = version;
    }

    public static int getVersion() {
        if(version == 0)
            return Constant.DB_VERSION;
        return version;
    }

    public static String getName(){
        if(!StringUtil.isValidate(name))
            return Constant.DB_NAME;
        return name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, Constant.DB_CREATING + name);

        //初始化数据表可以放在这里创建   暂且用不到的表可以在使用的时候创建
        //用户表
        HashMap<String, String> userMap = new HashMap<String, String>();
        userMap.put("id", "integer PRIMARY KEY autoincrement"); //这里必须用integer，而不能用int 否则创建失败
        userMap.put("password", "varchar(50)");
        userMap.put("number", "varchar(20) UNIQUE");
        createTable(db, Constant.DB_USER_TABLENAME, userMap);

        //定时任务表
        HashMap<String, String> timerMap = new HashMap<String, String>();
        timerMap.put("clientId", "varchar(15)");
        timerMap.put("businessType", "varchar(10)");
        timerMap.put("uuid", "varchar(32)");
        timerMap.put("clientType", "varchar(2)");
        timerMap.put("attentionType", "varchar(2)");
        timerMap.put("attentionContent", "varchar(100)");
        timerMap.put("activationMode", "varchar(7)");
        timerMap.put("activatedTime", "varchar(10)");
        timerMap.put("operationType", "varchar(2)");
        timerMap.put("isOpen", "varchar(1)");
        timerMap.put("isCommit", "varchar(1) default('2')");
        createTable(db, Constant.DB_TIMER_TABLENAME, timerMap);

        //创建消息数据库
        HashMap<String,String> notificationMap=new HashMap<String,String >();
        notificationMap.put("notificationid","integer PRIMARY KEY autoincrement");
        notificationMap.put("messageid","integer");//表示体检推荐ID或者周报ID，挂号id
        notificationMap.put("messagetype","integer");//0表示新的体检推荐，1表示健康检测周报,2表示挂号结果
        notificationMap.put("isread","integer");//0未读，1已读
        createTable(db,Constant.DB_Notification,notificationMap);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Upgrade db named " + name);

        //do something

    }


    /**
     * 创建数据库
     *      数据库创建失败时，会抛出运行时异常DBException
     */
    public void createDB(){
        SQLiteDatabase sdb = null;

        try {
            sdb = getReadableDatabase();
            Log.d(LOG_TAG, Constant.DB_OPEN_READ_CONNECTION);

            Log.d(LOG_TAG, Constant.DB_CREATE_SUCCESS);
            // throw new RuntimeException("应用异常");   //测试用的
        }catch (Exception e){
            Log.d(LOG_TAG, Constant.DB_CREATE_FAILURE);

            throw new DBException(Constant.DB_CREATE_FAILURE, e);
        }finally {
            if(sdb != null) {
                sdb.close();
                Log.d(LOG_TAG, Constant.DB_CLOSE_READ_CONNECTION);
            }
        }
    }


    //默认的建表
    private void createTable(SQLiteDatabase sdb, String tableName, HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> entry : params.entrySet()){
            sb.append(entry.getKey());
            sb.append(" ");
            sb.append(entry.getValue());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1); //删除最后一个多余的,逗号
        String sql="create table "+ tableName +"(" + sb.toString() + ")";
        Log.d(LOG_TAG, sql);
        sdb.execSQL(sql);
        Log.d(LOG_TAG, tableName + Constant.DB_CREATE_TABLE_SUCCESS);
    }

    /**
     *  创建一个数据表
     *      数据表创建失败时，会抛出运行时异常
     * @param tableName
     */
    public void createTable(String tableName, HashMap<String, String> params){
        SQLiteDatabase sdb = null;
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> entry : params.entrySet()){
            sb.append(entry.getKey());
            sb.append(" ");
            sb.append(entry.getValue());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1); //删除最后一个多余的,逗号
        String sql="create table "+ tableName +"(" + sb.toString() + ")";

        Log.d(LOG_TAG, sql);

        try {
            sdb = getWritableDatabase();
            Log.d(LOG_TAG, Constant.DB_OPEN_WRITE_CONNECTION);

            sdb.execSQL(sql);
            Log.d(LOG_TAG, tableName + Constant.DB_CREATE_TABLE_SUCCESS);
        }catch (Exception e){
            if(e.getMessage().contains("table "+ tableName +" already exists")){
                Log.d(LOG_TAG, tableName + Constant.DB_CREATE_TABLE_EXIST);
                return;
            }

            Log.d(LOG_TAG, Constant.DB_CREATE_TABLE_FAILURE);

            throw new DBException(Constant.DB_CREATE_TABLE_FAILURE, e);
        }finally {
            if(sdb != null) {
                sdb.close();
                Log.d(LOG_TAG, Constant.DB_CLOSE_WRITE_CONNECTION);
            }
        }
    }


    /**
     *  创建一批数据表
     *      数据表创建失败时，会抛出运行时异常
     * @param tableNames    需要创建的表的名字集合
     */

    /*
    private void createTable(Set<String> tableNames){
        SQLiteDatabase sdb = null;
        try {
            sdb = getWritableDatabase();

        }catch (Exception e){
            Log.d(Constant.LOG_TAG, Constant.DB_CREATE_TABLE_FAILURE);
            throw new DBException(Constant.DB_CREATE_TABLE_FAILURE, e);
        }finally {
            if(sdb != null)
                sdb.close();
        }
    }

    */


}

