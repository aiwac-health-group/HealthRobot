package aiwac.admin.com.healthrobot.activity.medicalexam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.medicalexam.adapter.GetMedicalExamUtil;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;

public class MedicalExamDetailActivity extends AppCompatActivity {
    private TextView tvTitle=findViewById(R.id.textview_rec_detail_title);
    private TextView tvDate=findViewById(R.id.textview_rec_detail_date);
    private TextView tvcontext=findViewById(R.id.textview_rec_detail_context);
    private Thread thread;
    private MedicalExam medicalExam;
    private boolean runExit=false;//退出询问线程的标志位
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_exam_detail);
        initViewFromWeb(getIntent().getExtras().getInt("position"));
    }
    /**
     * 这里是从服务器通过examid来查询体检推荐的具体信息
     * @param position
     */
    private void initViewFromWeb(int position){
        medicalExam= GetMedicalExamUtil.getList().get(position);
        tvTitle.setText(medicalExam.getName());
        tvDate.setText(medicalExam.getDataToShowAsText());

        //发送id给服务器 ，查询详细内容，内容会在WebSocketClientHelper的onMessage里赋值给MedicalExam里的examContext中
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    String string = JsonUtil.requestMedicalExamDetailString(medicalExam.getExamID());
                    WebSocketApplication.getWebSocketApplication().send(string);
                }catch (Exception e){
                    LogUtil.d(e.getMessage());
                    //其他异常处理
                }
            }
        });

        //检查examContext有没有内容，直到有内容的时候就显示
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while((MedicalExam.getExamContext()==null||MedicalExam.getExamContext().equals(""))&&!runExit){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tvcontext.setText(MedicalExam.getExamContext());
            }
        });
        thread.start();
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
        MedicalExam.setExamContext("");

    }
}
