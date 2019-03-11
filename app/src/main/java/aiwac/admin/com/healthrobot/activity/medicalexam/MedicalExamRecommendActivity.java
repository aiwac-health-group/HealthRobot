package aiwac.admin.com.healthrobot.activity.medicalexam;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.medicalexam.adapter.GetMedicalExamUtil;
import aiwac.admin.com.healthrobot.medicalexam.adapter.MedicalExamAdapter;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.JSON;



public class MedicalExamRecommendActivity extends BaseHttpListActivity<MedicalExam,ListView,MedicalExamAdapter> implements OnBottomDragListener {

    private static final String TAG="MedicalExamRecommend";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_medical_exam_recommend);


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
        //设置右上角体检套餐按钮
        Button btnMedicalExamMenu=findViewById(R.id.btn_medical_menu);
        btnMedicalExamMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MedicalExamRecommendActivity.this,MedicalExamMenuActivity.class);
               startActivity(intent);
            }
        });
    }

    @Override
    public void setList(final List<MedicalExam> list) {
        setList(new AdapterCallBack<MedicalExamAdapter>() {
            @Override
            public MedicalExamAdapter createAdapter() {
                return new MedicalExamAdapter(context);
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
        getMedicalExamRecommend();//获得体检推荐摘要
    }
    public void getMedicalExamRecommend(){
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    String string = JsonUtil.requestMedicalExamSummaryString();
                    WebSocketApplication.getWebSocketApplication().send(string);
                }catch (Exception e){
                    LogUtil.d(e.getMessage());
                    //其他异常处理
                }
            }
        });

    }
    @Override
    public void getListAsync(final int page) {
        //实际使用时用这个，需要配置服务器地址		HttpRequest.getUserList(range, page, -page, this);

        //仅测试用<<<<<<<<<<<
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //onHttpResponse(-page, page >= 5 ? null : JSON.toJSONString(GetMedicalExamUtil.getUserList(page, 10)), null);
                while (GetMedicalExamUtil.list.isEmpty()){
                    try {
                        Thread.sleep(700);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                onHttpResponse(-page, page >= 5 ? null : JSON.toJSONString(GetMedicalExamUtil.list), null);
            }
        }, 1000);
        //仅测试用>>>>>>>>>>>>
    }

    @Override
    public List<MedicalExam> parseArray(String json) {
        return JSON.parseArray(json, MedicalExam.class);
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
        Intent intent = new Intent(MedicalExamRecommendActivity.this,MedicalExamDetailActivity.class);
        intent.putExtra("position",GetMedicalExamUtil.list.get(position));
        startActivity(intent);
    }

    //生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



    //生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








    //内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    //内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



}
