package aiwac.admin.com.healthrobot.activity.medicalexam;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import aiwac.admin.com.healthrobot.medicalexam.tool.LoadFileModel;
import aiwac.admin.com.healthrobot.medicalexam.tool.Md5Tool;
import aiwac.admin.com.healthrobot.medicalexam.tool.SuperFileView2;
import aiwac.admin.com.healthrobot.medicalexam.tool.TLog;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalExamMenuActivity extends AppCompatActivity {
    private static final String TAG="ActivityMedicalExamMenu";
    private SuperFileView2 mSuperFileView;
    private static String filePath;//保存文件路径，。可以是网络路径
    private boolean runExit=false;//退出询问线程的标志位
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_exam_menu);
        init();
    }
    public void init() {
        mSuperFileView = (SuperFileView2) findViewById(R.id.mSuperFileView);

        filePath="menu.docx";
        mSuperFileView.setOnGetFilePathListener(new SuperFileView2.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView2 mSuperFileView2) {
                getFilePathAndShowFile(mSuperFileView2);
            }
        });
        Log.d("111", "init: "+getFilesDir().getAbsolutePath());

        getFileUrlFromServer();

        //getFileFromLocal();

    }

    /**
     * 从服务器获取文件的网络地址
     */
    private void getFileUrlFromServer(){
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    String stringJson = JsonUtil.requestMedicalExamMenuString();
                    WebSocketApplication.getWebSocketApplication().send(stringJson);
                }catch (Exception e){
                    LogUtil.d(e.getMessage());
                    //其他异常处理
                }
            }
        });
        filePath="";

        //检查filePath有没有内容，直到有内容的时候就结束
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(filePath.equals("")&&!runExit){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //获得了，开始显示
                mSuperFileView.show();
            }
        });
        thread.start();
    }

    /**
     * 从本地获取文件
     */
    private void getFileFromLocal(){
        //filePath="/data/user/0/com.aiwac.helloworld.test.speechhelloworld/activity/menu.docx";
        //filePath="menu.docx";
        filePath="/storage/emulated/0/menu.docx";
        //filePath="http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc";
        mSuperFileView.show();
    }
    private void getFilePathAndShowFile(SuperFileView2 mSuperFileView2) {
        if (filePath.contains("http")) {//网络地址要先下载
            downLoadFromNet(filePath,mSuperFileView2);
        } else {
            mSuperFileView2.displayFile(new File(filePath));
        }
    }
    private void downLoadFromNet(final String url,final SuperFileView2 mSuperFileView2) {

        //1.网络下载、存储路径、
        File cacheFile = getCacheFile(url);
        if (cacheFile.exists()) {
            if (cacheFile.length() <= 0) {
                TLog.d(TAG, "删除空文件！！");
                cacheFile.delete();
                return;
            }
        }



        LoadFileModel.loadPdfFile(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                TLog.d(TAG, "下载文件-->onResponse");
                boolean flag;
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    ResponseBody responseBody = response.body();
                    is = responseBody.byteStream();
                    long total = responseBody.contentLength();

                    File file1 = getCacheDir(url);
                    if (!file1.exists()) {
                        file1.mkdirs();
                        TLog.d(TAG, "创建缓存目录： " + file1.toString());
                    }


                    //fileN : /storage/emulated/0/pdf/kauibao20170821040512.pdf
                    File fileN = getCacheFile(url);//new File(getCacheDir(url), getFileName(url))

                    TLog.d(TAG, "创建缓存文件： " + fileN.toString());
                    if (!fileN.exists()) {
                        boolean mkdir = fileN.createNewFile();
                    }
                    fos = new FileOutputStream(fileN);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        TLog.d(TAG, "写入缓存文件" + fileN.getName() + "进度: " + progress);
                    }
                    fos.flush();
                    TLog.d(TAG, "文件下载成功,准备展示文件。");
                    //2.ACache记录文件的有效期
                    mSuperFileView2.displayFile(fileN);
                } catch (Exception e) {
                    TLog.d(TAG, "文件下载异常 = " + e.toString());
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                TLog.d(TAG, "文件下载失败");
                File file = getCacheFile(url);
                if (!file.exists()) {
                    TLog.d(TAG, "删除下载失败文件");
                    file.delete();
                }
            }
        });


    }
    /***
     * 获取缓存目录
     *
     * @param url
     * @return
     */
    private File getCacheDir(String url) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/");

    }
    /***
     * 绝对路径获取缓存文件
     *
     * @param url
     * @return
     */
    private File getCacheFile(String url) {
        File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/"
                + getFileName(url));
        TLog.d(TAG, "缓存文件 = " + cacheFile.toString());
        return cacheFile;
    }
    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        String fileName = Md5Tool.hashKey(url) + "." + getFileType(url);
        return fileName;
    }
    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            TLog.d(TAG, "paramString---->null");
            return str;
        }
        TLog.d(TAG,"paramString:"+paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            TLog.d(TAG,"i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        TLog.d(TAG,"paramString.substring(i + 1)------>"+str);
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runExit=true;
    }

    public static void setFilePath(String filePath) {
        MedicalExamMenuActivity.filePath = filePath;
    }
}
