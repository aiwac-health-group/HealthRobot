package aiwac.admin.com.healthrobot.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.bean.MessageInfo;
import aiwac.admin.com.healthrobot.bean.RegisterInfo;
import aiwac.admin.com.healthrobot.bean.TimerEntity;
import aiwac.admin.com.healthrobot.bean.User;
import aiwac.admin.com.healthrobot.bean.WifiInfo;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.UserData;
import aiwac.admin.com.healthrobot.exception.JsonException;



/**     对象和json直接的相互转换
 * Created by luwang on 2017/10/23.
 */

public class JsonUtil {


    /*     用户 Json 字符串格式
        String jsonStr =
                {
                "opt" : "insert"
                "user":
                    [{"id":1,"number":"15911112222","name":"zhangsan"}]
                }
     */

    //解析json获取操作 ，如插入，更新等
    public static String parseOpt(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String opt = root.getString(Constant.JSON_OPT);
            LogUtil.d(Constant.JSON_PARSE_SUCCESS + opt);
            return opt;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析businessType 获取事物类型
    public static String parseBusinessType(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析errorCode 获取消息是否成功传递到后台
    public static String parseErrorCode(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_TIMER_ERRORCODE);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析ErrorDesc
    public static String parseErrorDesc(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_ERRORDESC);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析用户登录密码
    public static String parsePWD(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.USER_DATA_FIELD_PASSWORD);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //解析uuid 获取定时任务消息的UUID属性
    public static String parseTimerUUID(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.WEBSOCKET_MESSAGE_UUID);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json result等
    public static boolean parseResult(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String errorCode = root.getString(Constant.WEBSOCKET_TIMER_ERRORCODE);
            return Constant.MESSAGE_ERRORCODE_200.equals(errorCode);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    /**
     *
     * @param jsonStr  json串
     * @param key
     * @return
     */
    public static String parseByKey(String jsonStr, String key){
        try{
            JSONObject root = new JSONObject(jsonStr);
            return root.getString(key);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //将BaseEntity对象转换成json字符串
    public static String baseEntity2Json(BaseEntity baseEntity){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, baseEntity.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis());
            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //将timerEntity对象转换成json字符串
    public static String timerEntity2Json(TimerEntity timerEntity){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, timerEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, Constant.WEBSOCKET_TIMER_BUSSINESSTYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, timerEntity.getUuid());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, timerEntity.getClientType());
            root.put(Constant.WEBSOCKET_TIMER_OPERATIONTYPE, timerEntity.getOperationType());
            root.put(Constant.WEBSOCKET_TIMER_ATTENTIONTYPE, timerEntity.getAttentionType());
            root.put(Constant.WEBSOCKET_TIMER_ATTENTIONCONTENT, timerEntity.getAttentionContent());
            root.put(Constant.WEBSOCKET_TIMER_ACTIVATIONMODE, timerEntity.getActivationMode());
            root.put(Constant.WEBSOCKET_TIMER_ACTIVATEDTIME, timerEntity.getActivatedTime());

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }



    //解析businessType 获取事物类型
    public static MessageInfo parseMessageInfo(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);

            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setClientId(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTID));
            messageInfo.setUuid(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));
            messageInfo.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
            messageInfo.setDescription(root.getString(Constant.WEBSOCKET_MESSAGE_DESCRIPTION));
            messageInfo.setTime(StringUtil.longToString(root.getString(Constant.WEBSOCKET_MESSAGE_TIME)));

            return messageInfo;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }



    //发送定时提醒同步请求
    public static String timerSync2Json(){
        try{
            //基本信息
            JSONObject root = new JSONObject();
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, UserData.getUserData().getNumber());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, Constant.WEBSOCKET_SYNC_REMINDER_BUSSINESSTYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, Constant.WEBSOCKET_MESSAGE_CLIENTTYPE_NUMBER);

            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //解析同步定时提醒json
    public static List<TimerEntity> parseTimerSync(String jsonStr){
        try{
            List<TimerEntity> timerEntities = new ArrayList<TimerEntity>();
            JSONObject root = new JSONObject(jsonStr);

            JSONArray jsonArray = root.getJSONArray(Constant.WEBSOCKET_BUSINESS_DATA);
            for(int i=0; i<jsonArray.length(); i++) {
                //共同设置
                TimerEntity timerEntity = new TimerEntity();
                timerEntity.setClientId(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTID));
                timerEntity.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                timerEntity.setOperationType(Constant.TIMER_OPERATIONTYPE_ADD);
                timerEntity.setBusinessType(Constant.WEBSOCKET_SYNC_REMINDER_BUSSINESSTYPE_CODE);
                timerEntity.setCommit(true);
                timerEntity.setOpen(true);

                JSONObject timerJson = jsonArray.getJSONObject(i);
                //不同的定时提醒
                timerEntity.setUuid(timerJson.getString(Constant.WEBSOCKET_MESSAGE_UUID));
                timerEntity.setActivatedTime(timerJson.getString(Constant.WEBSOCKET_TIMER_ACTIVATEDTIME));
                timerEntity.setAttentionType(timerJson.getString(Constant.WEBSOCKET_TIMER_ATTENTIONTYPE));
                timerEntity.setAttentionContent(timerJson.getString(Constant.WEBSOCKET_TIMER_ATTENTIONCONTENT));
                timerEntity.setActivationMode(timerJson.getString(Constant.WEBSOCKET_TIMER_ACTIVATIONMODE));

                timerEntities.add(timerEntity);
            }

            return timerEntities;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //解析json获取用户注册是否成功
    public static boolean isUserRegisterSucess(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String isSuccess = root.getString(Constant.USER_REGISTER_ISSUCCESS);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + isSuccess);
            return isSuccess.equals(Constant.USER_REGISTER_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json获取验证码
    public static String parseCheckcode(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String checkcode = jsonObject.getString(Constant.USER_REGISTER_CHECKCODE);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + checkcode);

            return checkcode;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json 判断用户是否注册
    public static boolean isUserRegisted(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String isNumberExist = jsonObject.getString(Constant.USER_REGISTER_PHONENUMBER_EXIST);
            LogUtil.d( Constant.JSON_PARSE_SUCCESS + isNumberExist);

            return isNumberExist.equals(Constant.USER_REGISTER_PHONENUMBER_EXIST_YES);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    //解析json获取用户
    public static User parseJson(String jsonStr) {

        //String jsonStr = "{"id":1,"number":"15911112222","name":"zhangsan"}";
        try {
            User user = new User();
            //将json字符串jsonData装入JSON数组，即JSONArray
            //jsonData可以是从文件中读取，也可以从服务器端获得
            JSONObject root = new JSONObject(jsonStr);

            JSONArray jsonArray = root.getJSONArray(Constant.JSON_OBJECT_USER_NAME);
            // for (int i = 0; i< jsonArray.length(); i++) {
            //循环遍历，依次取出JSONObject对象
            //用getInt和getString方法取出对应键值
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            user.setId(jsonObject.getInt("id"));
            user.setName(jsonObject.getString("name"));
            user.setNumber(jsonObject.getString("number"));

            LogUtil.d( Constant.JSON_PARSE_SUCCESS + user.toString());
            // }

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static String parseObject(User user, String opt) {
        try{
            JSONObject root = new JSONObject();
            root.put(Constant.JSON_OPT, opt);

            JSONArray jsonArray = new JSONArray();

            JSONObject userJson = new JSONObject();
            userJson.put("id", user.getId());
            userJson.put(Constant.USER_REGISTER_NUMBER, user.getNumber());
            userJson.put("passwd", user.getPassword());
            userJson.put("name", user.getName());
            jsonArray.put(0, userJson);

            root.put(Constant.JSON_OBJECT_USER_NAME, jsonArray);


            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());

            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }

    }



    public static String setBussinessType(String bussinessType){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, bussinessType);

            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    public static String wifiInfoToJson(WifiInfo wifiInfo){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, "0000");
            root.put("ssid", wifiInfo.getSsid());
            root.put("password",wifiInfo.getPassword());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    public static String userToJson(User user){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, user.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, user.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, user.getUuid());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, user.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis());
            root.put(Constant.WEBSOCKET_USER_NAME, user.getName());
            root.put(Constant.WEBSOCKET_USER_SEX, user.getSex());
            root.put(Constant.WEBSOCKET_USER_BIRTHDAY, user.getBirthday());
            root.put(Constant.WEBSOCKET_USER_WECHAT, user.getWechat());
            root.put(Constant.WEBSOCKET_USER_ADDRESS, user.getPlace());

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    public static String registerInfoToJson(RegisterInfo registerInfo){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, registerInfo.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, registerInfo.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, registerInfo.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis());
            root.put(Constant.WEBSOCKET_REGISTERINFO_PROVINCE, registerInfo.getProvince());
            root.put(Constant.WEBSOCKET_REGISTERINFO_CITY, registerInfo.getCity());
            root.put(Constant.WEBSOCKET_REGISTERINFO_HOSPITAL, registerInfo.getHospital());
            root.put(Constant.WEBSOCKET_REGISTERINFO_DEPARTMENT, registerInfo.getDepartment());

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


}
