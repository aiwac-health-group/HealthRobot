package aiwac.admin.com.healthrobot.activity.lecture;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;


//  讲座activity ,  视频 音频  文章三类
public class LectureActivtiy extends AppCompatActivity {

    private View videoView,musicView,articleView;

    private Button backButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏系统栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_lecture_activtiy);


        //隐藏标题栏
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }

        // 发起向服务器 获取讲座摘要的请求
        sendeQquestLectureAllAbstract();

        setView();


    }



    public void selected(){
        videoView.setSelected(false);
        musicView.setSelected(false);
        articleView.setSelected(false);
    }


    private void setView() {

        videoView = findViewById(R.id.topbar_lecture_video);
        musicView = findViewById(R.id.topbar_lecture_music);
        articleView = findViewById(R.id.topbar_lecture_article);


        videoView.setSelected(true);

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected();
                videoView.setSelected(true);
                addFragment(new fragment_lecture_video());
            }
        });

        musicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected();
                musicView.setSelected(true);
                addFragment(new fragment_lecture_audio());
            }
        });

        articleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected();
                articleView.setSelected(true);
                addFragment(new fragment_lecture_article());
            }
        });
        videoView.performClick();

        backButton1 = (Button)findViewById(R.id.backButton_2) ;
        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addFragment(Fragment fragment) {
        //获取到FragmentManager，在V4包中通过getSupportFragmentManager，
        //在系统中原生的Fragment是通过getFragmentManager获得的。
        FragmentManager FMs = getSupportFragmentManager();
        //2.开启一个事务，通过调用beginTransaction方法开启。
        FragmentTransaction MfragmentTransactions = FMs.beginTransaction();
        //把自己创建好的fragment创建一个对象
        //向容器内加入Fragment，一般使用add或者replace方法实现，需要传入容器的id和Fragment的实例。
        MfragmentTransactions.replace(R.id.main_content,fragment);
        //提交事务，调用commit方法提交。
        MfragmentTransactions.commit();
    }


    private void sendeQquestLectureAllAbstract()
    {

        // 视频讲座 摘要 请求
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    WebSocketApplication.getWebSocketApplication().send( JsonUtil.lectureVideoAbstract2Json());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("tag", "LoadEducationInfoAsync onPostExecute setOnItemClickListener exception");
                }
            }
        });


        // 音频讲座   摘要请求
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    WebSocketApplication.getWebSocketApplication().send( JsonUtil.lectureAudioAbstract2Json());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("tag", "LoadEducationInfoAsync onPostExecute setOnItemClickListener exception");
                }
            }
        });

        // 文章讲座   摘要请求
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    WebSocketApplication.getWebSocketApplication().send( JsonUtil.lectureArticleAbstract2Json());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("tag", "LoadEducationInfoAsync onPostExecute setOnItemClickListener exception");
                }
            }
        });


    }

}
