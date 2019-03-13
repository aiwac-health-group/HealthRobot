package aiwac.admin.com.healthrobot.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.bean.ExamInfoForCarousel;
import aiwac.admin.com.healthrobot.bean.LectureAVDetail;
import aiwac.admin.com.healthrobot.bean.LectureArticleDetail;
import aiwac.admin.com.healthrobot.bean.LectureCourse;
import aiwac.admin.com.healthrobot.bean.LectureCourseAbstractInfo;
import aiwac.admin.com.healthrobot.bean.RegisterInfo;
import aiwac.admin.com.healthrobot.bean.SkinResult;
import aiwac.admin.com.healthrobot.bean.User;
import aiwac.admin.com.healthrobot.bean.WifiInfo;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.exception.JsonException;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import aiwac.admin.com.healthrobot.notification.Notification;

import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_LECTURE_ARTICLE_ABSTRACT_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_LECTURE_ARTICLE_DETAIL_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_LECTURE_AUDIO_ABSTRACT_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_LECTURE_AV_DETAIL_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE;
import static aiwac.admin.com.healthrobot.common.Constant.WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE;


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

    //解析errorCode 获取消息是否成功传递到后台
    public static String parseToken(String jsonStr){
        try{
            JSONObject root = new JSONObject(jsonStr);
            String result = root.getString(Constant.USER_DATA_FIELD_TOKEN);
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
            return Constant.MESSAGE_ERRORCODE_2000.equals(errorCode);
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
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");
            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //通过id查询挂号信息
    public static String queryRegistgerInfoById(BaseEntity baseEntity, String id){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, baseEntity.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");
            root.put(Constant.WEBSOCKET_REGISTER_ID, id);
            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
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
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");
            root.put(Constant.WEBSOCKET_USER_NAME, user.getName());
            root.put(Constant.WEBSOCKET_USER_SEX, user.getSex());
            root.put(Constant.WEBSOCKET_USER_BIRTHDAY, user.getBirthday());
            root.put(Constant.WEBSOCKET_USER_WECHAT, user.getWechat());
            root.put(Constant.WEBSOCKET_USER_ADDRESS, user.getArea()+user.getAddress());

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
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");
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
    public static String skinResultToJson(SkinResult skinResult){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, skinResult.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, skinResult.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, skinResult.getUuid());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, skinResult.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");



            root.put(Constant.WEBSOCKET_SKINRESULT_FACE, ImageUtil.getBase64Str(skinResult.getFace()));
    //        root.put(Constant.WEBSOCKET_SKINRESULT_FACE,"1");


            JSONObject resultJson = new JSONObject();

            resultJson.put(Constant.WEBSOCKET_SKINRESULT_SCORE, skinResult.getScore().toString());

            JSONArray heitou = new JSONArray();
            heitou.put(""+skinResult.getHeitouResults()[0]);
            heitou.put(""+skinResult.getHeitouResults()[1]);
            resultJson.put(Constant.WEBSOCKET_SKINRESULT_HEITOU, heitou);

            JSONArray dou = new JSONArray();
            dou.put(""+skinResult.getDouResults()[0]);
            dou.put(""+skinResult.getDouResults()[1]);
            resultJson.put(Constant.WEBSOCKET_SKINRESULT_DOU, dou);

            JSONArray ban = new JSONArray();
            ban.put(""+skinResult.getBanResults()[0]);
            ban.put(""+skinResult.getBanResults()[1]);
            resultJson.put(Constant.WEBSOCKET_SKINRESULT_BAN, ban);


            JSONArray fuse = new JSONArray();
            fuse.put(""+skinResult.getFuseResults()[0]);
            fuse.put(""+skinResult.getFuseResults()[1]);
            fuse.put(""+skinResult.getFuseResults()[2]);
            resultJson.put(Constant.WEBSOCKET_SKINRESULT_FUSE, fuse);


            resultJson.put(Constant.WEBSOCKET_SKINRESULT_BODY, skinResult.getBody());
            resultJson.put(Constant.WEBSOCKET_SKINRESULT_DIET, skinResult.getDiet());
            resultJson.put(Constant.WEBSOCKET_SKINRESULT_MEDICINE, skinResult.getMedicine());
            resultJson.put(Constant.WEBSOCKET_SKINRESULT_DRUG, skinResult.getDrug());

            //JSONArray results=new JSONArray();
           // results.put(resultJson);

            root.put(Constant.WEBSOCKET_SKINRESULT_RESULT,resultJson.toString());

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }



    public static String queryPersonInfo(BaseEntity baseEntity){
        JSONObject root = new JSONObject();
        try{
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, baseEntity.getBusinessType());
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    public static User jsonToPersonInfo(String json){
        User user = new User();
        try{
            JSONObject root = new JSONObject(json);
            user.setName(root.getString(Constant.WEBSOCKET_USER_NAME));
            user.setSex(root.getString(Constant.WEBSOCKET_USER_SEX));
            user.setBirthday(root.getString(Constant.WEBSOCKET_USER_BIRTHDAY));
            user.setWechat(root.getString(Constant.WEBSOCKET_USER_WECHAT));
            String place = root.getString(Constant.WEBSOCKET_USER_ADDRESS);
            int index = place.indexOf("市", 3);
            user.setArea(place.substring(0,index+1));
            user.setAddress(place.substring(index+1,place.length()));

            return user;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    public static RegisterInfo jsonToRegisterInfo(String json){
        RegisterInfo registerInfo = new RegisterInfo();
        try{
            JSONObject root = new JSONObject(json);
            registerInfo.setProvince(root.getString(Constant.WEBSOCKET_REGISTERINFO_PROVINCE));
            registerInfo.setBusinessType(root.getString(Constant.WEBSOCKET_REGISTERINFO_CITY));
            registerInfo.setHospital(root.getString(Constant.WEBSOCKET_REGISTERINFO_HOSPITAL));
            registerInfo.setDepartment(root.getString(Constant.WEBSOCKET_REGISTERINFO_DEPARTMENT));
            registerInfo.setRegisterStatus(root.getString(Constant.WEBSOCKET_REGISTERINFO_STATUS));
            registerInfo.setDescription(root.getString(Constant.WEBSOCKET_REGISTERINFO_DESCRIPTION));
            return registerInfo;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }

    public static ArrayList<RegisterInfo> jsonToRegisterInfoList(String json){
        ArrayList<RegisterInfo> list = new ArrayList<>();

        try{
            JSONObject object = new JSONObject(json);
            JSONObject data=object.getJSONObject(Constant.WEBSOCKET_MESSAGE_DATA);
            JSONArray infos = data.getJSONArray(Constant.WEBSOCKET_MESSAGE_ITEMS);
            for(int i=0; i<infos.length(); i++){
                JSONObject root = infos.getJSONObject(i);
                RegisterInfo registerInfo = new RegisterInfo();
                registerInfo.setProvince(root.getString(Constant.WEBSOCKET_REGISTERINFO_PROVINCE));
                registerInfo.setBusinessType(root.getString(Constant.WEBSOCKET_REGISTERINFO_CITY));
                registerInfo.setHospital(root.getString(Constant.WEBSOCKET_REGISTERINFO_HOSPITAL));
                registerInfo.setCity(root.getString(Constant.WEBSOCKET_REGISTERINFO_CITY));
                registerInfo.setDepartment(root.getString(Constant.WEBSOCKET_REGISTERINFO_DEPARTMENT));
                registerInfo.setRegisterStatus(root.getString(Constant.WEBSOCKET_REGISTERINFO_STATUS));
                registerInfo.setDescription(root.getString(Constant.WEBSOCKET_REGISTERINFO_DESCRIPTION));
                registerInfo.setCreateTime(root.getString(Constant.WEBSOCKET_REGISTERINFO_CREATETIME));
                registerInfo.setUpdateTime(root.getString(Constant.WEBSOCKET_REGISTERINFO_UPDATETIME));
                list.add(registerInfo);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    //将json串转换成ExamInfoForCarousel对象
    public static ArrayList<ExamInfoForCarousel> jsonToExamInfoForCarouselList(String json){
        ArrayList<ExamInfoForCarousel> list = new ArrayList<>();
        try{
            JSONObject object = new JSONObject(json);
            JSONObject data = object.getJSONObject(Constant.WEBSOCKET_BUSINESS_DATA);
            JSONArray infos = data.getJSONArray(Constant.WEBSOCKET_MESSAGE_ITEMS);
            for(int i=0; i<infos.length(); i++){
                JSONObject root = infos.getJSONObject(i);
                ExamInfoForCarousel examInfoForCarousel = new ExamInfoForCarousel();
                examInfoForCarousel.setExamID(root.getString(Constant.WEBSOCKET_EXAM_ID));
                examInfoForCarousel.setCover(ImageUtil.getBitmap(root.getString(Constant.WEBSOCKET_EXAM_COVER)));
                list.add(examInfoForCarousel);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }


    public static ExamInfoForCarousel jsonToExamInfoForCarousel(String json){
        try{
            JSONObject root = new JSONObject(json);

            ExamInfoForCarousel examInfoForCarousel = new ExamInfoForCarousel();
            examInfoForCarousel.setExamID(root.getString(Constant.WEBSOCKET_MESSAGE_ID));
            examInfoForCarousel.setCover(ImageUtil.getBitmap(root.getString(Constant.WEBSOCKET_EXAM_COVER)));
            return examInfoForCarousel;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_PARSE_EXCEPTION);
            throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
        }
    }
    /**
     * 向服务器请求--7.体检推荐摘要查询
     */
    public static String requestMedicalExamSummaryString(){
        try {
            BaseEntity baseEntity = new BaseEntity();
            baseEntity.setBusinessType(Constant.WEBSOCKET_MEDICAL_EXAM_SUMMARY_BUSSINESSTYPE_CODE);
            //WEBSOCKET_MEDICAL_EXAM_SUMMARY_BUSSINESSTYPE_CODE="0007";//体检推荐摘要查询
            String stringJson = JsonUtil.baseEntity2Json(baseEntity);

            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + stringJson);
            return stringJson;
        } catch (Exception e) {
            LogUtil.d(e.getMessage());
            //其他异常处理
        }
        return "";
    }

    /**
     * 把json转化为medicalexam类型的list，供体检推荐摘要查询使用
     * @param json
     * @return
     */
    public static List<MedicalExam> jsonToMedicalExam(String json){
        List<MedicalExam> list =new ArrayList<MedicalExam>();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject data = root.getJSONObject(Constant.WEBSOCKET_BUSINESS_DATA);
            JSONArray jsonArray = data.getJSONArray(Constant.WEBSOCKET_MESSAGE_ITEMS);
            if(root.getString(Constant.WEBSOCKET_MESSAGE_ERRORDESC).equals(Constant.WEBSOCKET_EXAM_LIST_IS_NULL)){
                return list;
            }else{
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject oneItem=jsonArray.getJSONObject(i);
                    MedicalExam medicalExam = new MedicalExam();
                    medicalExam.setExamID(oneItem.getInt(Constant.WEBSOCKET_EXAM_ID));
                    medicalExam.setName(oneItem.getString(Constant.WEBSOCKET_EXAM_NAME));
                    medicalExam.setDescription(oneItem.getString(Constant.WEBSOCKET_EXAM_DESCRIPTION));
                    medicalExam.setDate(oneItem.getString(Constant.WEBSOCKET_EXAM_UPDATETIME));
                    medicalExam.setCover(ImageUtil.getBitmap(oneItem.getString(Constant.WEBSOCKET_EXAM_COVER)));
                    list.add(medicalExam);
                    LogUtil.d(medicalExam.toString());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 向服务器请求--8.体检推荐详细信息查询
     */
    public static String requestMedicalExamDetailString(final int examID){

        JSONObject root = new JSONObject();
        BaseEntity baseEntity = new BaseEntity();
        try {
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());

            // WEBSOCKET_MEDICAL_EXAM_DETAIL_BUSSINESSTYPE_CODE="0008";//体检推荐详细信息查询
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, Constant.WEBSOCKET_MEDICAL_EXAM_DETAIL_BUSSINESSTYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");

            root.put(Constant.WEBSOCKET_EXAM_ID,examID);

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());

            return root.toString();
        } catch (Exception e) {
            LogUtil.d(e.getMessage());
            //其他异常处理
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    /**
     * 从服务器端传来的json，从中获取MedicalExam
     * @param json
     * @return
     */
    public static MedicalExam getExamContextFromJson(String json){
        MedicalExam medicalExam = new MedicalExam();
        try {
            JSONObject root=new JSONObject(json);
            if(root.getString(Constant.WEBSOCKET_MESSAGE_ERRORDESC).equals(Constant.WEBSOCKET_EXAM_DETAIL_IS_NULL)){
                return null;
            }else{
                medicalExam.setDate(root.getString(Constant.WEBSOCKET_EXAM_UPDATETIME));
                medicalExam.setExamContext(root.getString(Constant.WEBSOCKET_EXAM_CONTEXT));
                medicalExam.setName(root.getString(Constant.WEBSOCKET_EXAM_NAME));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return medicalExam;
    }


    /**
     * 向服务器请求--22.体检套餐查询
     */
    public static String requestMedicalExamMenuString(){
        try {
            BaseEntity baseEntity = new BaseEntity();
            baseEntity.setBusinessType(Constant.WEBSOCKET_MEDICAL_EXAM_MENU_CODE);
            //WEBSOCKET_MEDICAL_EXAM_MENU_CODE="0022";//体检套餐查询
            String stringJson = JsonUtil.baseEntity2Json(baseEntity);

            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + stringJson);
            return stringJson;
        } catch (Exception e) {
            LogUtil.d(e.getMessage());
            //其他异常处理
        }
        return "";
    }

    /**
     * 从服务器获取 体检套餐的link
     * @param json
     * @return
     */
    public static String getMedicalExamMenuLinkFromJson(String json){
        try {
            JSONObject root=new JSONObject(json);
            return root.getString(Constant.WEBSOCKET_EXAM_MENU_LINK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 解析消息通知
     * @param json
     * @return
     */
    public static Notification json2Notification(String json){
        try {
            JSONObject root=new JSONObject(json);


            Notification notification = new Notification();
            notification.setMessageID(root.getInt(Constant.WEBSOCKET_NOTIFICTION_MESSAGEID));
            notification.setMessageType(root.getInt(Constant.WEBSOCKET_NOTIFICTION_MESSAGETYPE));
            notification.setIsRead(0);//1已读 0未读
            return notification;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向服务器请求健康报告
     * @param resultID
     * @return
     */
    public static String ResultId2Json(int resultID) {

        JSONObject root = new JSONObject();
        BaseEntity baseEntity = new BaseEntity();
        try {
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTID, baseEntity.getClientId());

            // WEBSOCKET_HEALTH_WEEKLY_REPORT_CODE="0015";//健康周报，健康检查结果
            root.put(Constant.WEBSOCKET_MESSAGE_BUSSINESSTYPE, Constant.WEBSOCKET_HEALTH_WEEKLY_REPORT_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, baseEntity.getClientType());
            root.put(Constant.WEBSOCKET_MESSAGE_TIME, System.currentTimeMillis()+"");

            root.put(Constant.WEBSOCKET_RESULT_ID, resultID);

            LogUtil.d(Constant.JSON_GENERATE_SUCCESS + root.toString());

            return root.toString();
        } catch (Exception e) {
            LogUtil.d(e.getMessage());
            //其他异常处理
            LogUtil.d(Constant.JSON_GENERATE_EXCEPTION);

        }
        return "";
    }
         //解析json 获取讲座  视频 音频的摘要信息、

        public static LectureCourseAbstractInfo parseLectureAVAbstractInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.MESSAGE_ERRORCODE_2000)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                LectureCourseAbstractInfo lectureAVAbstractInfo = new LectureCourseAbstractInfo();

                lectureAVAbstractInfo.setClientId(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                lectureAVAbstractInfo.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                lectureAVAbstractInfo.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                lectureAVAbstractInfo.setUuid(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                //具体的课程摘要
                String dataStr = root.getString(Constant.WEBSOCKET_MESSAGE_DATA);
                JSONObject data = new JSONObject(dataStr);
                JSONArray jsonArray = data.getJSONArray(Constant.WEBSOCKET_MESSAGE_ITEMS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    LectureCourse lectureCourse = new LectureCourse();
                    JSONObject lectureCourseJson = jsonArray.getJSONObject(i);

                    //  在json里获取某一讲座课程的摘要信息
                    lectureCourse.setLectureID(lectureCourseJson.getString(Constant.WEBSOCKET_MESSAGE_LECTUREID));
                    lectureCourse.setName(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_NAME));
                    lectureCourse.setUpdateTime(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_UPDATETIME));
                    lectureCourse.setDescription(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_DESCRIPTION));
                    lectureCourse.setCover(ImageUtil.getBitmap(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_COVER)));
                    lectureCourse.setDuration(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_DURATION));

                    lectureAVAbstractInfo.getLectureCourseAbstracts().add( lectureCourse);
                }
                return lectureAVAbstractInfo;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }

    //解析json 获取讲座  文章的摘要信息
    public static LectureCourseAbstractInfo parseLectureArticleAbstractInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.MESSAGE_ERRORCODE_2000)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                LectureCourseAbstractInfo lectureArticleAbstractInfo = new LectureCourseAbstractInfo();

                lectureArticleAbstractInfo.setClientId(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                lectureArticleAbstractInfo.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                lectureArticleAbstractInfo.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                lectureArticleAbstractInfo.setUuid(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                //具体的课程摘要
                String dataStr = root.getString(Constant.WEBSOCKET_MESSAGE_DATA);
                JSONObject data = new JSONObject(dataStr);
                JSONArray jsonArray = data.getJSONArray(Constant.WEBSOCKET_MESSAGE_ITEMS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    LectureCourse lectureCourse = new LectureCourse();
                    JSONObject lectureCourseJson = jsonArray.getJSONObject(i);

                    //  在json里获取某一讲座课程的摘要信息
                    lectureCourse.setLectureID(lectureCourseJson.getString(Constant.WEBSOCKET_MESSAGE_LECTUREID));
                    lectureCourse.setName(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_NAME));
                    lectureCourse.setUpdateTime(lectureCourseJson.getString(Constant.WEBSOCKET_LECTURE_COURSE_UPDATETIME));

                    lectureArticleAbstractInfo.getLectureCourseAbstracts().add( lectureCourse);
                }
                return lectureArticleAbstractInfo;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }


    //解析json 获取讲座  视音频的详细信息
    public static LectureAVDetail parseLectureAVDetailInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.MESSAGE_ERRORCODE_2000)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                LectureAVDetail  lectureAVDetail = new  LectureAVDetail();

                lectureAVDetail.setLectureID(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                lectureAVDetail.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                lectureAVDetail.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                lectureAVDetail.setUniqueID(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                lectureAVDetail.setLink(root.getString(Constant.WEBSOCKET_MESSAGE_LECTURE_AV_LINK));

                return lectureAVDetail;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }



    //解析json 获取讲座  文章的详细信息
    public static LectureArticleDetail parseLectureArticleDetailInfo(String jsonStr){
        String errorCode = JsonUtil.parseErrorCode(jsonStr);
        if(errorCode.equals(Constant.MESSAGE_ERRORCODE_2000)) {

            try {
                JSONObject root = new JSONObject(jsonStr);

                LectureArticleDetail  lectureArticleDetail = new LectureArticleDetail();

                lectureArticleDetail.setLectureID(root.getString(Constant.WEBSOCKET_MESSAGE_ACCOUNT));
                lectureArticleDetail.setBusinessType(root.getString(Constant.WEBSOCKET_MESSAGE_CODE));
                lectureArticleDetail.setClientType(root.getString(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE));
                lectureArticleDetail.setUniqueID(root.getString(Constant.WEBSOCKET_MESSAGE_UUID));

                lectureArticleDetail.setLectureContext(root.getString(Constant.WEBSOCKET_MESSAGE_LECTURE_CONTEXT));

                return lectureArticleDetail;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG",Constant.JSON_PARSE_EXCEPTION);
                throw new JsonException(Constant.JSON_PARSE_EXCEPTION, e);
            }
        }else {
            return null;
        }
    }



    //生成查询讲座音频摘要的json
    public static String lectureAudioAbstract2Json( ){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_AUDIO_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    //生成查询讲座视频摘要的json
    public static String lectureVideoAbstract2Json(){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }
    //生成查询讲座音视频详情的json
    public static String lectureAVDetail2Json(String lectureID ){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_AV_DETAIL_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_LECTUREID, lectureID);
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


    /**
     * 从服务器获取 健康周报的link
     * @param json
     * @return
     */
    public static String getHealthWeeklyReportLinkFromJson(String json){
        try {
            JSONObject root=new JSONObject(json);
            return root.getString(Constant.WEBSOCKET_HEALTH_WEEKLY_REPORT_RESULT_CONTEXT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    //生成查询讲座文章摘要的json
    public static String lectureArticleAbstract2Json( ){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_ARTICLE_ABSTRACT_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }

    //生成查询讲座文章详情的json
    public static String lectureArticleDetail2Json(String lectureID){
        JSONObject root = new JSONObject();
        try{
            User user = new User();
            root.put(Constant.WEBSOCKET_MESSAGE_ACCOUNT, user.clientId);
            root.put(Constant.WEBSOCKET_MESSAGE_CODE, WEBSOCKET_LECTURE_ARTICLE_DETAIL_TYPE_CODE);
            root.put(Constant.WEBSOCKET_MESSAGE_UUID, UUID.randomUUID().toString());
            root.put(Constant.WEBSOCKET_MESSAGE_CLIENTTYPE, WEBSOCKET_MESSAGE_SYSYTEM_CLIENTTYPE);
            root.put(Constant.WEBSOCKET_MESSAGE_TIME,System.currentTimeMillis() + "");
            root.put(Constant.WEBSOCKET_MESSAGE_LECTUREID, lectureID);
            Log.d("make",root.toString());
            LogUtil.d( Constant.JSON_GENERATE_SUCCESS + root.toString());
            return root.toString();
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( Constant.JSON_GENERATE_EXCEPTION);
            throw new JsonException(Constant.JSON_GENERATE_EXCEPTION, e);
        }
    }


}
