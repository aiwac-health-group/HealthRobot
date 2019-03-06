package aiwac.admin.com.healthrobot.notification;

import android.app.Activity;
import android.view.ViewGroup;

import zuo.biao.library.base.BaseAdapter;

public class NotificationAdapter extends BaseAdapter<Notification,NotificationView> {
    public NotificationAdapter(Activity context) {
        super(context);
    }

    @Override
    public NotificationView createView(int viewType, ViewGroup parent) {
        return new NotificationView(context,parent);
    }

    @Override
    public long getItemId(int position) {
        return super.getItem(position).getMessageID();
    }
}
