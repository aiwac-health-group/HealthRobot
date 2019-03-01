package aiwac.admin.com.healthrobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import aiwac.admin.com.healthrobot.activity.SkinMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //测肤功能测试
        Button sendButton = (Button) findViewById(R.id.skin);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SkinMainActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "按按钮",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }


}
