package aiwac.admin.com.healthrobot.activity.notification;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.activity.medicalexam.MedicalExamDetailActivity;
import aiwac.admin.com.healthrobot.activity.medicalexam.MedicalExamMenuActivity;
import aiwac.admin.com.healthrobot.activity.medicalexam.MedicalExamRecommendActivity;
import aiwac.admin.com.healthrobot.medicalexam.adapter.GetMedicalExamUtil;
import aiwac.admin.com.healthrobot.medicalexam.adapter.MedicalExamAdapter;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import aiwac.admin.com.healthrobot.notification.GetNotificationUtil;
import aiwac.admin.com.healthrobot.notification.Notification;
import aiwac.admin.com.healthrobot.notification.NotificationAdapter;
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
    }
    @Override
    public void getListAsync(final int page) {
        //实际使用时用这个，需要配置服务器地址		HttpRequest.getUserList(range, page, -page, this);

        //仅测试用<<<<<<<<<<<
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                onHttpResponse(-page, page >= 5 ? null : JSON.toJSONString(GetNotificationUtil.getUserList(page, 10)), null);
                Log.d(TAG, "run: "+JSON.toJSONString(GetNotificationUtil.getUserList(page, 5)));
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
        Log.d(TAG, "在体检推荐的消息页：点击了一项："+position+"  id:"+id);
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
