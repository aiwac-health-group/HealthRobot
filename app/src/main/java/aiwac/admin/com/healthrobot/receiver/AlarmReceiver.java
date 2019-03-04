package aiwac.admin.com.healthrobot.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import aiwac.admin.com.healthrobot.MainActivity;
import aiwac.admin.com.healthrobot.activity.SkinMainActivity;
import aiwac.admin.com.healthrobot.activity.ViewDialogFragment;
import aiwac.admin.com.healthrobot.activity.voicechat.WaitChatActivity;
import aiwac.admin.com.healthrobot.bean.BaseEntity;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.utils.ActivityUtil;
import aiwac.admin.com.healthrobot.utils.JsonUtil;


public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG="AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context, "闹铃响了, 可以做点事情了~~", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onReceive: 闹铃响了, 可以做点事情了~~");

        //
        MainActivity.alarm(context);
        new AlertDialog.Builder(context).setCancelable(true)
                .setTitle("该拍照了~")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Intent intent = new Intent(context, SkinMainActivity.class);
                        //context.startActivity(intent);

                    }
                }
        ).show();


    }
}
