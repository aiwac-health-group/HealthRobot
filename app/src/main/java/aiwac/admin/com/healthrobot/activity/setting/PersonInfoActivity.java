package aiwac.admin.com.healthrobot.activity.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import aiwac.admin.com.healthrobot.R;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.BottomMenuView;
import zuo.biao.library.ui.TextClearSuit;


/**
 * 已弃用
 */
public class PersonInfoActivity extends BaseActivity implements View.OnClickListener, OnBottomDragListener, BottomMenuView.OnBottomMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDragBottom(boolean rightToLeft) {

    }

    //UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)
    private TextView nameView;
    private TextView sexView;
    private TextView birthdayView;
    private TextView wechatView;
    private TextView areaView;
    private TextView addressView;
    private LinearLayout llUserName;
    private LinearLayout llSex;
    private LinearLayout llBirthday;
    private LinearLayout llWechat;
    private LinearLayout llArea;
    private LinearLayout llAddress;
    private LinearLayout etllUserName;
    private LinearLayout etllSex;
    private LinearLayout etllBirthday;
    private LinearLayout etllWechat;
    private LinearLayout etllArea;
    private LinearLayout etllAddress;
    private EditText etUserName;
    private EditText etSex;
    private EditText etBirthday;
    private EditText etWechat;
    private EditText etArea;
    private EditText etAddress;





    @Override
    public void initView() {

        nameView = findViewById(R.id.tv_userName_info);
        sexView = findViewById(R.id.tv_sex_info);
        birthdayView = findViewById(R.id.tv_birthday_info);
        wechatView = findViewById(R.id.tv_wechat_info);
        areaView = findViewById(R.id.tv_area_info);
        addressView = findViewById(R.id.tv_address_info);
        llUserName = findViewById(R.id.ll_userName);
        llSex = findViewById(R.id.ll_sex);
        llBirthday = findViewById(R.id.ll_birthday);
        llWechat = findViewById(R.id.ll_wechat);
        llArea = findViewById(R.id.ll_area);
        llAddress = findViewById(R.id.ll_address);
        etUserName = findView(R.id.et_userName);
        etSex = findView(R.id.et_sex);
        etBirthday = findView(R.id.et_birthday);
        etWechat = findView(R.id.et_wechat);
        etArea = findView(R.id.et_area);
        etAddress = findView(R.id.et_address);



    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {
        new TextClearSuit().addClearListener(etUserName, findView(R.id.ivUserRemarkClear));//清空备注按钮点击监听
        new TextClearSuit().addClearListener(etSex, findView(R.id.ivUserRemarkClear));//清空备注按钮点击监听
        new TextClearSuit().addClearListener(etBirthday, findView(R.id.ivUserRemarkClear));//清空备注按钮点击监听
        new TextClearSuit().addClearListener(etWechat, findView(R.id.ivUserRemarkClear));//清空备注按钮点击监听
        new TextClearSuit().addClearListener(etArea, findView(R.id.ivUserRemarkClear));//清空备注按钮点击监听
        new TextClearSuit().addClearListener(etAddress, findView(R.id.ivUserRemarkClear));//清空备注按钮点击监听

        llUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUserName.setText(nameView.getText());
                llUserName.setVisibility(View.GONE);
                etllUserName.setVisibility(View.VISIBLE);
            }
        });

        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    etllUserName.setVisibility(View.GONE);
                    llUserName.setVisibility(View.VISIBLE);
                    nameView.setText(etUserName.getText());
                }
            }
        });

        llUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUserName.setText(nameView.getText());
                llUserName.setVisibility(View.GONE);
                etllUserName.setVisibility(View.VISIBLE);
            }
        });

        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    etllUserName.setVisibility(View.GONE);
                    llUserName.setVisibility(View.VISIBLE);
                    nameView.setText(etUserName.getText());
                }
            }
        });





    }

    @Override
    public void onBottomMenuItemClick(int intentCode) {

    }
}
