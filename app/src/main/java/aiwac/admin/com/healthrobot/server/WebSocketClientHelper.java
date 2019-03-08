package aiwac.admin.com.healthrobot.server;


import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import aiwac.admin.com.healthrobot.HealthRobotApplication;
import aiwac.admin.com.healthrobot.activity.healthweeklyreport.HealthWeeklyReportActivity;
import aiwac.admin.com.healthrobot.activity.medicalexam.MedicalExamMenuActivity;
import aiwac.admin.com.healthrobot.bean.ExamInfoForCarousel;
import aiwac.admin.com.healthrobot.bean.LectureAVDetail;
import aiwac.admin.com.healthrobot.bean.LectureArticleDetail;
import aiwac.admin.com.healthrobot.bean.LectureCourseAbstractInfo;
import aiwac.admin.com.healthrobot.bean.MessageEvent;
import aiwac.admin.com.healthrobot.bean.User;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.db.NotificationSqliteHelper;
import aiwac.admin.com.healthrobot.medicalexam.adapter.GetMedicalExamUtil;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import aiwac.admin.com.healthrobot.notification.Notification;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;

/**     用于WebSocket客户端通信
 * Created by luwang on 2017/10/31.
 */

public class WebSocketClientHelper extends WebSocketClient {

    private Context context;
    private User user;

    public ArrayList<ExamInfoForCarousel> getExamInfoForCarousels() {
        return examInfoForCarousels;
    }

    public void setExamInfoForCarousels(ArrayList<ExamInfoForCarousel> examInfoForCarousels) {
        this.examInfoForCarousels = examInfoForCarousels;
    }

    private ArrayList<ExamInfoForCarousel> examInfoForCarousels;

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }







    // 讲座   视频 音频  文章  和 健康检测结果摘要信息，每次获取到一次摘要json 更新一次
    protected LectureCourseAbstractInfo lectureVideoAllInfo;
    protected LectureCourseAbstractInfo lectureAudioAllInfo;
    protected LectureCourseAbstractInfo lectureArticleAllInfo;


    // 讲座   视频 音频  文章  和 健康检测结果详细信息，每次获取到一次摘要json 更新一次
//    private LectureAVDetail lectureAudioDetail;
//    private LectureAVDetail lectureVideoDetail;
    protected LectureAVDetail lectureAVDetail;
    protected LectureArticleDetail lectureArticleDetail;


    public LectureCourseAbstractInfo getLectureVideoAllInfo(){
        return lectureVideoAllInfo;
    }

    public LectureCourseAbstractInfo getLectureAudioAllInfo(){
        return  lectureAudioAllInfo;
    }

    public LectureCourseAbstractInfo getLectureArticleAllInfo(){
        return  lectureArticleAllInfo;
    }

    public LectureAVDetail getLectureAudioDetail(){
        return lectureAVDetail;
    }

    public LectureAVDetail getLectureVideoDetail(){
        return lectureAVDetail;
    }

    public LectureArticleDetail getLectureArticleDetail(){
        return lectureArticleDetail;
    }







    public WebSocketClientHelper(URI serverUri, Map<String, String> httpHeaders, Context context) {
        this(serverUri, new Draft_6455(), httpHeaders, 0, context);
        //LogUtil.d( "serverUri  : " + serverUri);
    }

    public WebSocketClientHelper(URI serverUri, Draft draft, Map<String, String> httpHeaders, Context context) {
        this(serverUri, draft, httpHeaders, 0, context);
    }

    public WebSocketClientHelper(URI serverUri, Draft draft, Map<String, String> httpHeaders, int connectionTimeout, Context context) {
        super(serverUri,draft,httpHeaders,connectionTimeout);

        //获取全局唯一的context对象，否则activity不能销毁问题
        this.context = context.getApplicationContext();
    }



    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_OPEN + getRemoteSocketAddress());

        //开启连接的时候检查要不要同步数据
       // checkSyncTimer();
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_CLOSE + i + s + b);
        WebSocketApplication.getWebSocketApplication().setNull();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        LogUtil.d( Constant.WEBSOCKET_CONNECTION_EXCEPTION);
    }

    @Override
    public void onMessage(final String json) {
        //处理具体逻辑

       LogUtil.printJson( Constant.WEBSOCKET_MESSAGE_FROM_SERVER ,json,"##");

        try{
            String businessType = JsonUtil.parseBusinessType(json);
            if(businessType.equals(Constant.WEBSOCKET_VOICECHAT_BUSSINESSTYPE_CODE)){  //在线问诊房间号
                EventBus.getDefault().postSticky(new MessageEvent(json));//eventbus黏性事件
            }else if(businessType.equals(Constant.WEBSOCKET_REGISTERRESULT_BUSSINESSTYPE_CODE)) { //语音挂号结果

            }else if(businessType.equals(Constant.WEBSOCKET_REGISTERHISTORY_BUSSINESSTYPE_CODE)){ //挂号历史纪录
                EventBus.getDefault().postSticky(new MessageEvent(json));
            }else if(businessType.equals(Constant.WEBSOCKET_QUERYPERSONINFO_BUSSINESSTYPE_CODE)){ //个人信息查询

                user = JsonUtil.jsonToPersonInfo(json);
            }else if(businessType.equals(Constant.WEBSOCKET_NEW_MESSAGE_BUSSINESSTYPE_CODE)){  //新消息通知
                Notification notification = JsonUtil.json2Notification(json);
                if(notification.getMessageType()==0){
                    //如果messageType=0，为体检推荐新消息
                    MessageEvent messageEvent = new MessageEvent("MainActivity", json);
                    EventBus.getDefault().post(messageEvent);
                }else if(notification.getMessageType()==1){
                    //如果messageType=1，为健康周报新消息

                }else if(notification.getMessageType()==2){
                    //如果messageType=2，为挂号信息新消息

                }
                NotificationSqliteHelper notificationSqliteHelper = new NotificationSqliteHelper(HealthRobotApplication.getContext());
                notificationSqliteHelper.insert(notification);

            }else if(businessType.equals(Constant.WEBSOCKET_THREE_EXAM_BUSSINESSTYPE_CODE)){
                examInfoForCarousels = JsonUtil.jsonToExamInfoForCarouselList(json);
            }else if(businessType.equals(Constant.WEBSOCKET_MEDICAL_EXAM_SUMMARY_BUSSINESSTYPE_CODE)){//体检推荐摘要查询
                 new GetMedicalExamUtil().initList(JsonUtil.jsonToMedicalExam(json));
            }else if(businessType.equals(Constant.WEBSOCKET_MEDICAL_EXAM_DETAIL_BUSSINESSTYPE_CODE)){//体检推荐详情查询
                MedicalExam.setExamContext(JsonUtil.getExamContextFromJson(json));
            }else if(businessType.equals(Constant.WEBSOCKET_MEDICAL_EXAM_MENU_CODE)){//体检套餐查询
                MedicalExamMenuActivity.setFilePath(JsonUtil.getMedicalExamMenuLinkFromJson(json));
            }else if(businessType.equals(Constant.WEBSOCKET_HEALTH_WEEKLY_REPORT_CODE)){//健康周报查询
                HealthWeeklyReportActivity.setFilePath(JsonUtil.getHealthWeeklyReportLinkFromJson(json));
            }else   if (businessType.equals(Constant.WEBSOCKET_LECTURE_AUDIO_ABSTRACT_TYPE_CODE)) //讲座音频摘要信息到达
            {
                lectureAudioAllInfo = JsonUtil.parseLectureAVAbstractInfo(json);
            }else if (businessType.equals(Constant.WEBSOCKET_LECTURE_VIDEO_ABSTRACT_TYPE_CODE)) //讲座视频摘要信息到达
            {
                lectureVideoAllInfo = JsonUtil.parseLectureAVAbstractInfo(json);
            }else if ((businessType.equals(Constant.WEBSOCKET_LECTURE_ARTICLE_ABSTRACT_TYPE_CODE))) //讲座文章摘要信息到达
            {
                lectureArticleAllInfo = JsonUtil.parseLectureArticleAbstractInfo(json);
            }else if ((businessType.equals(Constant.WEBSOCKET_LECTURE_AV_DETAIL_TYPE_CODE))) //讲座音视频详细信息到达
            {
                lectureAVDetail = JsonUtil.parseLectureAVDetailInfo(json);
            }else if  ((businessType.equals(Constant.WEBSOCKET_LECTURE_ARTICLE_DETAIL_TYPE_CODE))) //讲座文章详细信息到达
            {
                lectureArticleDetail = JsonUtil.parseLectureArticleDetailInfo(json);
            }


        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d( "onMessage : " + e.getMessage());

        }

    }

    //判断是否重新安装，是否需要同步
    private void checkSyncTimer(){
        /*SharedPreferences pref = context.getSharedPreferences(Constant.DB_USER_TABLENAME, Context.MODE_PRIVATE);
        String timerSync = pref.getString(Constant.USER_DATA_FIELD_TIMER_SYNC, "");
        if(!StringUtil.isValidate(timerSync)) { // 新的线程中发送同步请求
            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        //发送同步请求
                        String json = JsonUtil.timerSync2Json();
                        send(json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }*/
    }




}


