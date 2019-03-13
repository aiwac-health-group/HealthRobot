package aiwac.admin.com.healthrobot.activity.medicalexam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.MessageEvent;
import aiwac.admin.com.healthrobot.bean.RegisterInfo;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.medicalexam.adapter.GetMedicalExamUtil;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.Log;

public class MedicalExamDetailActivity extends BaseActivity {
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvcontext;
    private Thread thread;
    private MedicalExam medicalExam;
    private boolean runExit=false;//退出询问线程的标志位
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_exam_detail);
        tvTitle=findViewById(R.id.textview_rec_detail_title);
        tvDate=findViewById(R.id.textview_rec_detail_date);
        tvcontext=findViewById(R.id.textview_rec_detail_context);
        initViewFromWeb(getIntent().getExtras().getInt("examID"));
        EventBus.getDefault().register(this);

        //返回按钮
/*
        ImageView imageViewBtn= findViewById(R.id.btn_return_img);
        imageViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/


    }
    /**
     * 这里是从服务器通过examid来查询体检推荐的具体信息
     * @param examID
     */
    private void initViewFromWeb(final int examID) {

        //发送id给服务器 ，查询详细内容，内容会在WebSocketClientHelper的onMessage里赋值给MedicalExam里的examContext中
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try {
                    String string = JsonUtil.requestMedicalExamDetailString(examID);
                    WebSocketApplication.getWebSocketApplication().send(string);
                } catch (Exception e) {
                    LogUtil.d(e.getMessage());
                    //其他异常处理
                }
            }
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if(messageEvent.getTo().equals("MedicalExamDetail")){
            String json = messageEvent.getMessage();
            MedicalExam medExamTemp =JsonUtil.getExamContextFromJson(json);
            tvcontext.setText(medExamTemp.getExamContext());
            tvTitle.setText(medExamTemp.getName());
            tvDate.setText(medExamTemp.getDataToShowAsText());
        }
    }



    /**
     * 这里是本地显示体检推荐的测试函数
     * @param position
     */
    private void initView(int position){

        //ImageView img=findViewById(R.id.imageview_rec_detail_image);
        final MedicalExam medicalExam= GetMedicalExamUtil.getList().get(position);
        tvTitle.setText(medicalExam.getName());
        tvcontext.setText(medicalExam.getDescription());
        tvDate.setText(medicalExam.getDataToShowAsText());



    /*    img.setImageBitmap(
                BitmapFactory.decodeResource(getResources(),R.drawable.test)
        );
        this.setContentView(img);*/
        //img.setImageBitmap(getBitmap());//http://120.27.103.151:8088/PicServiceDemo/MyServlet


        /**
         * String urlStr = "http://..............."; //网络图片地址
         *
         * URL url = new URL(urlStr);
         *
         * Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
         *
         * ImageView view_pic.setImageBitmap(bitmap);
         * ---------------------
         * 作者：简单生活_cocoa
         * 来源：CSDN
         * 原文：https://blog.csdn.net/kafei_kings/article/details/8135664
         * 版权声明：本文为博主原创文章，转载请附上博文链接！
         */
    }

    /**
     * 通过url显示图片
     */
    public Bitmap getBitmap(String path) throws IOException {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出页面的时候要吧这个内容设置为空
        runExit=true;

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}
