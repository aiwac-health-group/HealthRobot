package aiwac.admin.com.healthrobot.medicalexam.view;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import zuo.biao.library.base.BaseView;

public class MedicalExamView extends BaseView<MedicalExam> implements View.OnClickListener {
    private static String TAG="MedicalExamView";

    public MedicalExamView(Activity context, ViewGroup parent) {
        super(context, R.layout.view_medical_exam_recommend, parent);
    }
    public TextView textViewTitle;
    public TextView textViewDescription;
    public TextView textViewDate;

    @Override
    public View createView() {
        textViewTitle=findViewById(R.id.textview_rec_title);
        textViewDescription=findView(R.id.textview_rec_description);
        textViewDate=findViewById(R.id.textview_rec_date);


        return super.createView();


    }

    @Override
    public void bindView(MedicalExam data_) {
        super.bindView(data_!=null?data_:new MedicalExam());

        textViewTitle.setText(data_.getTitle());
        textViewDescription.setText(data_.getDescription());
        textViewDate.setText(data_.getDataToShowAsText());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.view_medical_exam_recommend){
            Log.d(TAG, "onClick: 单击了一个体检推荐---不知道有没有触发");
            //不知道有没有触发
        }
    }
}
