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
import aiwac.admin.com.healthrobot.medicalexam.adapter.GetMedicalExamUtil;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;

public class MedicalExamDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_exam_detail);
        initView(getIntent().getExtras().getInt("position"));
    }

    private void initView(int position){
        TextView tvTitle=findViewById(R.id.textview_rec_detail_title);
        TextView tvDate=findViewById(R.id.textview_rec_detail_date);
        TextView tvcontext=findViewById(R.id.textview_rec_detail_context);
        //ImageView img=findViewById(R.id.imageview_rec_detail_image);
        MedicalExam medicalExam= GetMedicalExamUtil.getList().get(position);
        tvTitle.setText(medicalExam.getTitle());
        tvcontext.setText(medicalExam.getExamContext());
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
}
