package aiwac.admin.com.healthrobot.activity.skin;

import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import aiwac.admin.com.healthrobot.R;

public class TestHelpActivity extends AppCompatActivity {

    ImageView backbtn;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_help);

        backbtn=(ImageView) findViewById(R.id.backbtn);
        textView=(TextView)findViewById(R.id.testtext);

        setTextView();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestHelpActivity.super.onBackPressed();
            }
        });
    }
    private void setTextView(){
        Spanned text= Html.fromHtml(getResources().getString(R.string.htmlString));
        textView.setText(text);
    }

}
