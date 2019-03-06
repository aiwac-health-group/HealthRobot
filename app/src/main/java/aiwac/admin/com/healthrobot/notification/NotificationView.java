package aiwac.admin.com.healthrobot.notification;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.medicalexam.model.MedicalExam;
import zuo.biao.library.base.BaseView;

public class NotificationView extends BaseView<Notification> implements View.OnClickListener {
    private static String TAG="NotificationView";

    public NotificationView(Activity context, ViewGroup parent) {
        super(context, R.layout.view_notification, parent);
    }
    public TextView textViewNotification;

    @Override
    public View createView() {
        textViewNotification=findViewById(R.id.textview_notification);

        return super.createView();


    }

    @Override
    public void bindView(Notification data_) {
        super.bindView(data_!=null?data_:new Notification());
        if(data_.getType()==1){//1是健康周报  2是挂号信息
            textViewNotification.setText("您的健康周报已经生成啦");
        }else if(data_.getType()==2){
            textViewNotification.setText("您的挂号信息更新啦");
        }else{
            textViewNotification.setText("空消息");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.textview_notification){
            Log.d(TAG, "onClick: 单击了一个体检推荐---不知道有没有触发");
            //不知道有没有触发
        }
    }
}
