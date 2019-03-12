package aiwac.admin.com.healthrobot.activity.lecture;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.LectureAVDetail;
import aiwac.admin.com.healthrobot.bean.LectureCourse;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import io.vov.vitamio.Vitamio;

import static com.baidu.tts.loopj.AsyncHttpClient.log;

public class LectureVideoDetailActivity extends AppCompatActivity {

    protected LectureCourse lectureCourseNow;
    private LectureAVDetail lectureAVDetail;
    protected ImageView lectureCover;
    protected TextView lectureName, lectureDuration, lectureUpdateTime, lectureDescription;
    private Button backButton, buttonplay_pause;
    protected String link = "noLink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("lecture","viodeo detail");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lecture_av_details);


        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        setView();

        //获取已经到达 的讲座组消息数据，信息请求在 fragment_lecture_article 被发送
        getLectureVideoDetailAsync loadCourseGroupAsync = new getLectureVideoDetailAsync();
        loadCourseGroupAsync.execute();


        Vitamio.isInitialized(this);

    }



    private void setView() {
        lectureCourseNow = (LectureCourse) getIntent().getSerializableExtra("LectureCourse");

        lectureCover = (ImageView)findViewById(R.id.lecture_cover);
        lectureName = (TextView)findViewById(R.id.lecture_name);
        lectureDuration = (TextView)findViewById(R.id.lecture_duration);
        lectureUpdateTime = (TextView)findViewById(R.id.lecture_update_time);
        lectureDescription = (TextView)findViewById(R.id.lecture_description);
        buttonplay_pause = (Button)findViewById(R.id.buttonPlayPause) ;
        buttonplay_pause.setSelected(false);
        buttonplay_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonplay_pause.isSelected() == true)
                {
                    buttonplay_pause.setSelected(false);
                }
                else
                {
                    buttonplay_pause.setSelected(true);

                    if ( link.equals("noLink" ) )
                    {
                        Toast.makeText(LectureVideoDetailActivity.this, "抱歉，暂无相关资源", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Intent intent = new Intent(LectureVideoDetailActivity.this, LectureVideoPlayActivity.class);

//                    //测试
//                    link  = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//                    //测试
                        Log.d("lecture",link);
                        intent.putExtra("Link",link);
                        startActivity(intent);
                    }

                    buttonplay_pause.setSelected(false);
                }


            }
        });

        backButton = (Button)findViewById(R.id.backButton) ;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //lectureCover.setImageBitmap(lectureCourseNow.getCover());
        Bitmap receive=(Bitmap)(getIntent().getParcelableExtra("bitmap"));
        lectureCover.setImageBitmap(receive);
        lectureName.setText(lectureCourseNow.getName());
        lectureDuration.setText(lectureCourseNow.getDuration());
        lectureUpdateTime.setText(lectureCourseNow.getUpdateTime());
        lectureDescription.setText(lectureCourseNow.getDescription());

    }




    class getLectureVideoDetailAsync extends AsyncTask<Void, Void, Boolean> {

        private AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder builder = new AlertDialog.Builder(LectureVideoDetailActivity.this);
            View view = View.inflate(LectureVideoDetailActivity.this, R.layout.activity_progress, null);
            builder.setIcon(R.drawable.login_aiwac_log);
            builder.setTitle(Constant.WEBSOCKET_BUSINESS_DOWNLOAD_LECTURE);
            builder.setView(view);  //必须使用view加载，如果使用R.layout设置则无法改变bar的进度

            dialog = builder.create();
            dialog.setCancelable(false);

            dialog.show();
        }

        // 获取已经到达的讲座视频详细数据
        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                for (int i = 0; i < 5; i++) {
                    Thread.sleep(500);
                    lectureAVDetail = WebSocketApplication.getWebSocketApplication().getWebSocketHelperLectureVideoDetail();

                    if (lectureAVDetail != null) {
                        link = lectureAVDetail.getLink();

                        Log.d("lecture","link: "+link);
                    }
                    return true;
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

            } else { // 失败。显示空白

                AlertDialog.Builder builder = new AlertDialog.Builder(LectureVideoDetailActivity.this);
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
