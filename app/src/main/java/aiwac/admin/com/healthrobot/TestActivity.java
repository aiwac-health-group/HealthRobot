package aiwac.admin.com.healthrobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import aiwac.admin.com.healthrobot.activity.loginandregister.LoginActivity;
import aiwac.admin.com.healthrobot.common.Constant;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button btn=findViewById(R.id.btn_start);
        EditText ip=findViewById(R.id.editTextIP);
        /*Constant.IP=ip.getText().toString().trim();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TestActivity.this, LoginActivity.class));
            }
        });*/
    }
}
