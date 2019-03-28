package aiwac.admin.com.healthrobot.activity.medicalexam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

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
    private ImageView imageView1, imageView2, imageView3;
    private TextView tv_exam_name1,tv_exam_name2,tv_exam_name3,tv_exam_desc1,tv_exam_desc2,tv_exam_desc3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_medical_exam_recommend);


        //功能归类分区方法，必须调用<<<<<<<<<<
        initView();
        initData();
        initEvent();
        //功能归类分区方法，必须调用>>>>>>>>>>

        getMedicalExamRecommend();//获得体检推荐摘要
        srlBaseHttpList.autoRefresh();
    }

    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void initView() {//必须调用
        super.initView();

        imageView1 = findView(R.id.iv_exam_1);
        imageView2 = findView(R.id.iv_exam_2);
        imageView3 = findView(R.id.iv_exam_3);
        tv_exam_name1 = findView(R.id.tv_exam_name1);
        tv_exam_name2 = findView(R.id.tv_exam_name2);
        tv_exam_name3 = findView(R.id.tv_exam_name3);
        tv_exam_desc1 = findView(R.id.tv_exam_desc1);
        tv_exam_desc2 = findView(R.id.tv_exam_desc2);
        tv_exam_desc3 = findView(R.id.tv_exam_desc3);

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

        findView(R.id.ll_stronger_exam).setVisibility(View.VISIBLE);

        imageView1.setImageBitmap(list.get(0).getCover());
        tv_exam_name1.setText(list.get(0).getName());
        tv_exam_desc1.setText(list.get(0).getDescription());
        imageView2.setImageBitmap(list.get(1).getCover());
        tv_exam_name2.setText(list.get(1).getName());
        tv_exam_desc2.setText(list.get(1).getDescription());
        imageView3.setImageBitmap(list.get(2).getCover());
        tv_exam_name3.setText(list.get(2).getName());
        tv_exam_desc3.setText(list.get(2).getDescription());
        list.remove(0);
        list.remove(0);
        list.remove(0);
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

        ListView listView = findView(R.id.lvBaseList);
        setListViewHeightByItem(listView);
        ScrollView scrollView = findView(R.id.scrollView);
        scrollView.smoothScrollTo(0,0);

    }


    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Override
    public void initData() {//必须调用
        super.initData();
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
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for(MedicalExam m:GetMedicalExamUtil.list){
                    //LogUtil.d(m.toString());
                }
                onResponse(-page,  GetMedicalExamUtil.list, null);
            }
        }, 100);
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
        findView(R.id.ll_stronger_exam1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalExamRecommendActivity.this,MedicalExamDetailActivity.class);
                intent.putExtra(Constant.WEBSOCKET_EXAM_ID,0);
                startActivity(intent);
            }
        });
        findView(R.id.ll_stronger_exam2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalExamRecommendActivity.this,MedicalExamDetailActivity.class);
                intent.putExtra(Constant.WEBSOCKET_EXAM_ID,1);
                startActivity(intent);
            }
        });
        findView(R.id.ll_stronger_exam3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicalExamRecommendActivity.this,MedicalExamDetailActivity.class);
                intent.putExtra(Constant.WEBSOCKET_EXAM_ID,2);
                startActivity(intent);
            }
        });
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
        Log.d(TAG, "在体检推荐的消息页：点击了一项："+position+"  id:"+id+  "  examID"+GetMedicalExamUtil.list.get(position).getExamID());
        Intent intent = new Intent(MedicalExamRecommendActivity.this,MedicalExamDetailActivity.class);
        intent.putExtra(Constant.WEBSOCKET_EXAM_ID,new Long(id).intValue());
        startActivity(intent);
    }


    private void setListViewHeightByItem(ListView listView) {
        if (listView == null) {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View item = listAdapter.getView(i, null, listView);
            //item的布局要求是linearLayout，否则measure(0,0)会报错。
            item.measure(0, 0);
            //计算出所有item高度的总和
            totalHeight += item.getMeasuredHeight();
        }
        //获取ListView的LayoutParams,只需要修改高度就可以。
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //修改ListView高度为item总高度和所有分割线的高度总和。
        //这里的分隔线是指ListView自带的divider
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //将修改过的参数，重新设置给ListView
        listView.setLayoutParams(params);
    }

    //生命周期、onActivityResult<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



    //生命周期、onActivityResult>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //Event事件区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








    //内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    //内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



}
