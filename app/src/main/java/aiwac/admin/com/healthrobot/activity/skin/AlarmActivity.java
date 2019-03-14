package aiwac.admin.com.healthrobot.activity.skin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

import aiwac.admin.com.healthrobot.R;

public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "AlarmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去除黑边儿

        Calendar calendar = Calendar.getInstance();
        //设置明天的提醒
        calendar.add(Calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        ////测试用:设置十秒后的提醒
        //calendar.add(Calendar.SECOND,10);

        //打开activity
        Intent intent = new Intent(AlarmActivity.this, AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(AlarmActivity.this, 0,
                intent, 0);


        AlarmManager alarmManager = (AlarmManager) AlarmActivity.this.getSystemService(ALARM_SERVICE);

//        //10s 一次
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setWindow(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),20000,pendingIntent);
//        } else {
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),20000,pendingIntent);
//        }

        //每天一次
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }


        //ok
        Button okButton = (Button) findViewById(R.id.ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmActivity.this, SkinMainActivity.class);
                startActivity(intent);
                Log.d(TAG, "onClick: 去测肤");
                finish();

            }
        });

        //cancel
        Button cancleButton = (Button) findViewById(R.id.cancel_btn);
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
