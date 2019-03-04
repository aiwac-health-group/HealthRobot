package aiwac.admin.com.healthrobot.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import aiwac.admin.com.healthrobot.MainActivity;
import aiwac.admin.com.healthrobot.R;

public class AlarmActivity extends AppCompatActivity {
    private static final String TAG = "AlarmActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        //设置明天的提醒
        MainActivity.alarm(AlarmActivity.this);


        //ok
        Button okButton = (Button) findViewById(R.id.ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmActivity.this, SkinMainActivity.class);
                startActivity(intent);
                Log.d(TAG, "onClick: 去测肤");

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
