package aiwac.admin.com.healthrobot.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.text.Html;
import android.widget.TextView;

import aiwac.admin.com.healthrobot.R;

public class BeautyResultActivity extends AppCompatActivity {

    private float[] heitouResults;
    private float[] douResults;
    private float[] banResults;
    private float[] heiyanquanResults;
    private float[] cleanResults;
    private float[] fuseResults;
    private int skinType=1;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty_result);

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

    }
}
