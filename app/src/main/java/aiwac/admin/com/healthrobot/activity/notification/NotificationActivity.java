package aiwac.admin.com.healthrobot.activity.notification;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import aiwac.admin.com.healthrobot.HealthRobotApplication;
import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.activity.healthweeklyreport.HealthWeeklyReportActivity;
import aiwac.admin.com.healthrobot.activity.voiceregister.RegisterHistoryActivity;
import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.notification.GetNotificationUtil;
import aiwac.admin.com.healthrobot.notification.Notification;
import aiwac.admin.com.healthrobot.notification.NotificationAdapter;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.JSON;

public class NotificationActivity extends BaseHttpListActivity<Notification,ListView,NotificationAdapter> implements OnBottomDragListener {

    private static final String TAG="NotificationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        srlBaseHttpList.autoRefresh();
    }


    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void initView() {//必须调用
        super.initView();
    }


    @Override
    public void setList(final List<Notification> list) {
        setList(new AdapterCallBack<NotificationAdapter>() {
            @Override
            public NotificationAdapter createAdapter() {
                return new NotificationAdapter(context);
            }
            @Override
            public void refreshAdapter() {
                adapter.refresh(list);
            }
        });
    }


    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void initData() {//必须调用
        super.initData();
        getNotification();
    }

    private void getNotification(){
        GetNotificationUtil getNotificationUtil = new GetNotificationUtil(HealthRobotApplication.getContext());
    }


    @Override
    public void getListAsync(final int page) {
        //实际使用时用这个，需要配置服务器地址		HttpRequest.getUserList(range, page, -page, this);

        //仅测试用<<<<<<<<<<<
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              //  onHttpResponse(-page, page >= 5 ? null : JSON.toJSONString(GetNotificationUtil.getUserList(page, 10)), null);

                while (GetNotificationUtil.list.isEmpty()){
                    try {
                        Thread.sleep(700);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                onHttpResponse(-page, page >= 5 ? null : JSON.toJSONString(GetNotificationUtil.list), null);
            }
        }, 1000);
        //仅测试用>>>>>>>>>>>>
    }

    @Override
    public List<Notification> parseArray(String json) {
        return JSON.parseArray(json, Notification.class);
    }
    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //Event事件区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Override
    public void initEvent() {//必须调用
        super.initEvent();

    }
    @Override
    public void onDragBottom(boolean rightToLeft) {
        if (rightToLeft) {

            return;
        }
        finish();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // toActivity(UserActivity.createIntent(context, id));
        Log.d(TAG, "在消息通知的消息页：点击了一项："+position+"  id:"+id);
        Notification notification = GetNotificationUtil.list.get(position);
        if(notification.getMessageType()==1){//如果messageType=1，为健康周报新消息
            Intent intent = new Intent(NotificationActivity.this,HealthWeeklyReportActivity.class);
            intent.putExtra(Constant.WEBSOCKET_NOTIFICTION_MESSAGEID,notification.getMessageID());
            startActivity(intent);
        }else if(notification.getMessageType()==2){//如果messageType=2，为挂号信息新消息
            ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
                @Override
                public void run() {
                    try {
                        BaseEntity baseEntity = new BaseEntity();
                        baseEntity.setBusinessType(Constant.WEBSOCKET_REGISTERRESULT_BUSSINESSTYPE_CODE);
                        WebSocketApplication.getWebSocketApplication().send(JsonUtil.baseEntity2Json(baseEntity));
                    } catch (Exception e) {
                        LogUtil.d(e.getMessage());
                        //其他异常处理
                    }
                }
            });

            Intent intent = new Intent(NotificationActivity.this,RegisterHistoryActivity.class);
            intent.putExtra(Constant.WEBSOCKET_NOTIFICTION_MESSAGEID,notification.getMessageID());
            startActivity(intent);
        }
        /*Intent intent = new Intent(NotificationActivity.this,MedicalExamDetailActivity.class);
        intent.putExtra("position",position);
        startActivity(intent);*/
    }

    //生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



    //生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








    //内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    //内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



}
