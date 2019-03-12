package aiwac.admin.com.healthrobot.activity.lecture;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.LectureArticleDetail;
import aiwac.admin.com.healthrobot.bean.LectureCourse;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;

public class LectureArticleDetailActivity extends AppCompatActivity {


    protected LectureCourse lectureCourseNow;
    private TextView articleName, articleTime, articleContent;
    private LectureArticleDetail lectureArticleDetail;
    private String content;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lecture_article_detail);

        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        lectureCourseNow = (LectureCourse) getIntent().getSerializableExtra("LectureCourse");


        articleName = (TextView)findViewById(R.id.lecture_articl_detail_name) ;
        articleTime = (TextView)findViewById(R.id.lecture_articl_detail_time) ;
        articleContent = (TextView)findViewById(R.id.lecture_articl_detail_content) ;

        backButton = (Button)findViewById(R.id.backButton_2) ;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //获取已经到达 的讲座组消息数据，信息请求在 fragment_lecture_article 被发送
        getLectureArticleDetailAsync loadCourseGroupAsync = new getLectureArticleDetailAsync();
        loadCourseGroupAsync.execute();



        articleName.setText(lectureCourseNow.getName());
        articleTime.setText(lectureCourseNow.getUpdateTime());





        //articleContent.setText("正在加载...");


    }




    class getLectureArticleDetailAsync extends AsyncTask<Void, Void, Boolean> {

        private AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(LectureArticleDetailActivity.this);
            View view = View.inflate(LectureArticleDetailActivity.this, R.layout.activity_progress, null);
            builder.setIcon(R.drawable.login_aiwac_log);
            builder.setTitle(Constant.WEBSOCKET_BUSINESS_DOWNLOAD_LECTURE);
            builder.setView(view);  //必须使用view加载，如果使用R.layout设置则无法改变bar的进度

            dialog = builder.create();
            dialog.setCancelable(false);

            dialog.show();
        }

        // 获取已经到达的讲座摘要数据
        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                for (int i = 0; i < 5; i++) {
                    Thread.sleep(500);
                    lectureArticleDetail = WebSocketApplication.getWebSocketApplication().getWebSocketHelperLectureArticleDetail();

                    if (lectureArticleDetail != null) {
                        content = lectureArticleDetail.getLectureContext();

                        Log.d("lecture", "  (lectureArticleDetail != null)   content: "+content);
                        return true;
                    }


                }
            } catch (Exception e) {
                Log.d("tag", e.getMessage());
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.cancel();
            if (aBoolean) {   //加载讲座内容
                articleContent.setText(content);
                Log.d("lecture", "设置content: "+content);

            } else { // 失败。显示空白

                AlertDialog.Builder builder = new AlertDialog.Builder(LectureArticleDetailActivity.this);
                builder.setIcon(R.drawable.login_aiwac_log);
                builder.setTitle("抱歉");
                builder.setMessage("亲，暂无资源~");
                builder.setNegativeButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ;
                    }
                });

                builder.show();

            }
        }
    }



}
