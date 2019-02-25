package aiwac.admin.com.healthrobot.db;

/**     数据库工具类，主要用于对表的增删改查
 * Created by luwang on 2017/10/17.
 */

public interface SQLiteHelper<T> {

    public void insert(T t);
    public void update(T t);
    public void delete(T t);
    //public List<T> query(String sql, );
    public T getEntity(Integer id);


}
