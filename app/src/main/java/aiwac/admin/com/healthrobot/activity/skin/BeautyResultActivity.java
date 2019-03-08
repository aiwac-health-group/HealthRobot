package aiwac.admin.com.healthrobot.activity.skin;

import android.content.Intent;
import android.os.Bundle;

//import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.SkinResult;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.server.WebSocketApplication;
import aiwac.admin.com.healthrobot.task.ThreadPoolManager;
import aiwac.admin.com.healthrobot.utils.JsonUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;

//import android.text.Html;

public class BeautyResultActivity extends BaseActivity {
    private static final String TAG = "BeautyResultActivity";

    private float[] heitouResults;
    private float[] douResults;
    private float[] banResults;
    private float[] heiyanquanResults;
    private float[] cleanResults;
    private float[] fuseResults;
    private int skinType=1;

    ImageView Back; //返回按钮
    TextView Age; //肌龄
    TextView BodyContent; //体质描述
    TextView DietContent; //饮食建议
    TextView TMedicine; //美容中药方剂
    TextView ShengYao; //生药

    TextView HeitouDegree; //黑头
    TextView HeitouDesprt; //黑头描述
    TextView DouDegree; //痘
    TextView BanDegree; //斑
    TextView FuseDesprt; //肤色描述

    MyActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_result);

        //调用存放activity类
        instance = MyActivity.getInstance();
        //判断存放activity类是否存放该activity，不存在加入类
        if (!instance.isexitlist(this)){
            instance.addActivity(this);
        }

        //点击标题栏的后退按钮，返回SkinMainActivity
        Back =(ImageView)findViewById(R.id.tv_back) ;
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(BeautyResultActivity.this,SkinMainActivity.class);
//                startActivity(intent);
                Log.d(TAG, "onClick: click back button");
                instance.exit();
            }
        });



        Age=(TextView)findViewById(R.id.tv_age);
        BodyContent=(TextView)findViewById(R.id.tv_body_content);
        DietContent=(TextView)findViewById(R.id.tv_diet_content);
        TMedicine=(TextView)findViewById(R.id.tv_medicine_content);
        ShengYao=(TextView)findViewById(R.id.tv_shengyao_content);


        HeitouDegree=(TextView)findViewById(R.id.heitouDegree);
        HeitouDesprt=(TextView)findViewById(R.id.heitouDesprt);
        DouDegree=(TextView)findViewById(R.id.douDegree);
        BanDegree=(TextView)findViewById(R.id.banDegree);
        FuseDesprt=(TextView)findViewById(R.id.fuseDesprt);

        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            fuseResults = intent.getFloatArrayExtra("fuseResults");

            banResults = intent.getFloatArrayExtra("banResults");
            heitouResults = intent.getFloatArrayExtra("heitouResults");
            douResults = intent.getFloatArrayExtra("douResults");
            heiyanquanResults = intent.getFloatArrayExtra("heiyanquanResults");
            cleanResults = intent.getFloatArrayExtra("cleanResults");

            FuseDesprt.setText(String.valueOf(fuseResults[0]) + "\n" + String.valueOf(fuseResults[1])+ "\n" + String.valueOf(fuseResults[2]));

            BanDegree.setText(String.valueOf(banResults[0] + "\t" + String.valueOf(banResults[1])));
            DouDegree.setText(String.valueOf(douResults[0] + "\t" + String.valueOf(douResults[1])));
            HeitouDegree.setText(String.valueOf(heitouResults[0] + "\t" + String.valueOf(heitouResults[1])));
        }

        HeitouDesprt.setText(getResources().getString(R.string.heitou));
//        FuseDesprt.setText(getResources().getString(R.string.fuse));

        // 不同肤质报告
        switch (skinType){
            case 1:
                BodyContent.setText(getResources().getString(R.string.bodyConstitution1));
                DietContent.setText(getResources().getString(R.string.food1));
                TMedicine.setText(getResources().getString(R.string.medicine1));
                ShengYao.setText(getResources().getString(R.string.drag1));
                break;
            case 2:
                BodyContent.setText(getResources().getString(R.string.bodyConstitution2));
                DietContent.setText(getResources().getString(R.string.food2));
                TMedicine.setText(getResources().getString(R.string.medicine2));
                ShengYao.setText(getResources().getString(R.string.drag2));
                break;
            case 3:
                BodyContent.setText(getResources().getString(R.string.bodyConstitution3));
                DietContent.setText(getResources().getString(R.string.food3));
                TMedicine.setText(getResources().getString(R.string.medicine3));
                ShengYao.setText(getResources().getString(R.string.drag3));
                break;
            case 4:
                BodyContent.setText(getResources().getString(R.string.bodyConstitution4));
                DietContent.setText(getResources().getString(R.string.food4));
                TMedicine.setText(getResources().getString(R.string.medicine4));
                ShengYao.setText(getResources().getString(R.string.drag4));
                break;
            case 5:
                BodyContent.setText(getResources().getString(R.string.bodyConstitution5));
                DietContent.setText(getResources().getString(R.string.food5));
                TMedicine.setText(getResources().getString(R.string.medicine5));
                ShengYao.setText(getResources().getString(R.string.drag5));
                break;
            case 6:
                BodyContent.setText(getResources().getString(R.string.bodyConstitution6));
                DietContent.setText(getResources().getString(R.string.food6));
                TMedicine.setText(getResources().getString(R.string.medicine6));
                ShengYao.setText(getResources().getString(R.string.drag6));
                break;
            case 7:
                BodyContent.setText(getResources().getString(R.string.bodyConstitution7));
                DietContent.setText(getResources().getString(R.string.food7));
                TMedicine.setText(getResources().getString(R.string.medicine7));
                ShengYao.setText(getResources().getString(R.string.drag7));
                break;
            case 8:
                BodyContent.setText(getResources().getString(R.string.bodyConstitution8));
                DietContent.setText(getResources().getString(R.string.food8));
                TMedicine.setText(getResources().getString(R.string.medicine8));
                ShengYao.setText(getResources().getString(R.string.drag8));
                break;
        }

        //把结果上传到后台
        ThreadPoolManager.getThreadPoolManager().submitTask(new Runnable() {
            @Override
            public void run() {
                try{
                    SkinResult skinResult = new SkinResult();
                    //skinResult.setFace();
                    //一个个传.
                    skinResult.setHeitouResults(heitouResults[0],heitouResults[1]);
                    skinResult.setDouResults(douResults[0],douResults[1]);
                    skinResult.setBanResults(banResults[0],banResults[1]);
                    skinResult.setFuseResults(fuseResults[0],fuseResults[1],fuseResults[2]);
//                    //直接传
//                    skinResult.setHeitouResults(heitouResults);
//                    skinResult.setDouResults(douResults);
//                    skinResult.setBanResults(banResults);
//                    skinResult.setFuseResults(fuseResults);
                    //Bitmap bmp = BitmapFactory.decodeFile(uri.toString());//filePath

                    skinResult.setScore(21);
                    skinResult.setBody(getResources().getString(R.string.bodyConstitution1));
                    skinResult.setDiet(getResources().getString(R.string.food1));
                    skinResult.setMedicine(getResources().getString(R.string.medicine1));
                    skinResult.setDrug(getResources().getString(R.string.drag1));


                    skinResult.setBusinessType(Constant.WEBSOCKET_PERSONAL_INFOMATION_BUSSINESSTYPE_CODE);
                    skinResult.setUuid(UUID.randomUUID().toString());
                    String json = JsonUtil.skinResultToJson(skinResult);

                    WebSocketApplication.getWebSocketApplication().send(json);

                }catch (Exception e){
                    LogUtil.d(e.getMessage());
                    //其他异常处理
                }
            }
        } );

    }

    //点击返回键，销毁多个activity，直接回到SkinMainActivity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Log.d(TAG, "onClick: click back button");
            instance.exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
