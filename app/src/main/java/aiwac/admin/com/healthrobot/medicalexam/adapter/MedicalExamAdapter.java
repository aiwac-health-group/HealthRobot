package aiwac.admin.com.healthrobot.medicalexam.adapter;

import android.app.Activity;
import android.view.ViewGroup;


import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import aiwac.admin.com.healthrobot.medicalexam.view.MedicalExamView;
import zuo.biao.library.base.BaseAdapter;

public class MedicalExamAdapter extends BaseAdapter<MedicalExam,MedicalExamView> {
    public MedicalExamAdapter(Activity context) {
        super(context);
    }

    @Override
    public MedicalExamView createView(int viewType, ViewGroup parent) {
        return new MedicalExamView(context,parent);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getExamID();
    }
}
