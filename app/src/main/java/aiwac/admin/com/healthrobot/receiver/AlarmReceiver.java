package aiwac.admin.com.healthrobot.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import aiwac.admin.com.healthrobot.MainActivity;


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

    }
}
