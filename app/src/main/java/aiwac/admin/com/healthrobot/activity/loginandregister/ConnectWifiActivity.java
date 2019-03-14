package aiwac.admin.com.healthrobot.activity.loginandregister;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aiwac.admin.com.healthrobot.R;
import aiwac.admin.com.healthrobot.bean.WifiInfo;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.utils.ActivityUtil;
import aiwac.admin.com.healthrobot.utils.LogUtil;
import aiwac.admin.com.healthrobot.utils.StringUtil;
import aiwac.admin.com.healthrobot.utils.WifiUtil;
import zuo.biao.library.base.BaseActivity;

public class ConnectWifiActivity extends BaseActivity {

    private Button connectButton;
    private Button wifiChooseBtn;
    private TextView wifiText;
    private EditText passwordEdit;

    private WifiInfo wifiInfo = new WifiInfo();
    private int checkId;

    private AlertDialog dialog;

    private List<String> permissionList = new ArrayList<>();
    private String from;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_wifi);

        from = getIntent().getStringExtra("from");

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, filter);

        wifiUtil = new WifiUtil(this);

        wifiText = (TextView) findViewById(R.id.config_wifi_text);
        passwordEdit = (EditText) findViewById(R.id.config_password_edit);

        wifiChooseBtn = (Button)findViewById(R.id.wifi_choose_button);
        wifiChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //蓝牙连接需要模糊定位的权限
                if(ContextCompat.checkSelfPermission(ConnectWifiActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                if(ContextCompat.checkSelfPermission(ConnectWifiActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                if(!permissionList.isEmpty())
                    ActivityCompat.requestPermissions(ConnectWifiActivity.this, permissionList.toArray(new String[permissionList.size()]), 2);
                else{
                    toOpenGPS();
                }

            }
        });

        connectButton = (Button)findViewById(R.id.btn_connect_wifi);
        connectButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(wifiInfo == null){
                    Toast.makeText(ConnectWifiActivity.this, Constant.AIWAC_CONFIG_SELECT, Toast.LENGTH_LONG).show();
                    wifiChooseBtn.requestFocus();
                    return;
                }

                String password = passwordEdit.getText().toString().trim();
                if(!StringUtil.isValidate(password)){
                    Toast.makeText(ConnectWifiActivity.this, Constant.AIWAC_CONFIG_PASSWORD, Toast.LENGTH_LONG).show();
                    passwordEdit.requestFocus();
                    return;

                }else if(password.length()<8){
                    Toast.makeText(ConnectWifiActivity.this, Constant.AIWAC_CONFIG_PASSWORD_ERROR, Toast.LENGTH_LONG).show();
                    passwordEdit.requestFocus();
                    return;
                }else{
                    wifiInfo.setPassword(password);
                    if(wifiUtil.isOpenWifi()){
                        wifiUtil.connect(wifiInfo.getSsid(), wifiInfo.getPassword());
                    }else{

                        wifiUtil.openWifi();
                        isConnect = true;
                    }
                }


            }
        });

    }

    private boolean isConnect;
    private WifiUtil wifiUtil;

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction(); //得到action
            Log.d("action=", action);
            if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,0);
                LogUtil.d( "WIFI状态："+wifiState);
                switch(wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        LogUtil.d( "WIFI状态：已关闭WIFI功能");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        LogUtil.d( "WIFI状态：已打开WIFI功能");
                        if(isConnect){
                            wifiUtil.connect(wifiInfo.getSsid(), wifiInfo.getPassword());
                        }
                        break;
                    default:
                        break;
                }

            }else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    LogUtil.d("连接已断开");
                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    LogUtil.d("已连接到网络" );
                    count ++;
                    Message message = new Message();
                    message.obj = true;
                    handler.sendMessage(message);
                }
            }else if (action.equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
                int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
                if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                    LogUtil.d("wifi密码错误" );
                    Message message = new Message();
                    message.obj = false;
                    handler.sendMessage(message);

                }
            }

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //dialog.dismiss();
            if((boolean)msg.obj){
                SharedPreferences.Editor editor = getSharedPreferences(Constant.DB_USER_TABLENAME, MODE_PRIVATE).edit();
                editor.putBoolean(Constant.USER_DATA_ISCONNECTWIFI, true);
                editor.apply();

                if(from!= null && from.equals("setting")){
                    if(count > 1){
                        Toast.makeText(ConnectWifiActivity.this,"wifi连接成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    Toast.makeText(ConnectWifiActivity.this,"wifi连接成功",Toast.LENGTH_SHORT).show();
                    ActivityUtil.skipActivity(ConnectWifiActivity.this,LoginActivity.class,true);
                }

            }else{
                showNormalDialog("","wifi密码错误");
                connectButton.setEnabled(true);
                wifiChooseBtn.setEnabled(true);
            }
        }
    };


    public void showConfigDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = View.inflate(this, R.layout.activity_progress, null);
        ProgressBar bar = (ProgressBar) view.findViewById(R.id.bar_config);
        bar.setMax(100);

        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("正在连接wifi...");
        builder.setView(view);

        dialog = builder.create();
        dialog.setCancelable(false);

        dialog.show();
    }
    //创建一个普通的警告对话框
    private void showNormalDialog(String title, String message){
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(ConnectWifiActivity.this);
        normalDialog.setIcon(R.drawable.aiwac);
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton(Constant.DEFAULT_POSITIVE_BUTTON, null);
        // 显示
        normalDialog.show();
    }
    private void selectConfigWifi(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ConnectWifiActivity.this);
        dialog.setTitle(Constant.WIFI_SELECT);
        dialog.setNegativeButton(Constant.DEFAULT_NEGATIVE_BUTTON, null);

        try{
            if(WifiUtil.checkWifi(ConnectWifiActivity.this)){  //wifi可用，执行异常逻辑

                final List<WifiInfo> wifiInfos =  WifiUtil.findConnectableWifi(ConnectWifiActivity.this);
                if(wifiInfos != null) {
                    final String[] items = new String[wifiInfos.size()];
                    for(int i=0; i<items.length; i++){
                        items[i] = wifiInfos.get(i).getSsid();
                    }
                    dialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkId = which;
                        }
                    });
                    dialog.setPositiveButton(Constant.DEFAULT_POSITIVE_BUTTON, new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wifiText.setText(items[checkId]);
                            passwordEdit.setVisibility(EditText.VISIBLE); // 让密码输入框可见
                            connectButton.setEnabled(true);

                            //设置选择的wifi信息
                            wifiInfo = wifiInfos.get(checkId);

                            LogUtil.d(items[checkId] + " : " + wifiInfo.getSsid());
                        }
                    });
                }else{  //没有可连接的wifi
                    LogUtil.d( Constant.WIFI_NO_CONNECTABLE);
                    dialog.setTitle(Constant.WIFI_NO_CONNECTABLE);
                }
                dialog.show();
            }else{ // wifi没有打开，让用户打开wifi
                dialog.setTitle(Constant.WIFI_CLOSE);
                dialog.show();
            }
        }catch (Exception e){ // wifi模块不可用
            dialog.setTitle(Constant.WIFI_UNAVAILABILITY);
            dialog.show();
        }
    }

    //打开GPS（跳转到GPS界面）
    public void toOpenGPS(){
        boolean isOpen = isOPen();//判断GPS是否打开
        if (!isOpen) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请打开机器人定位")
                    // 拒绝, 退出应用
                    .setNegativeButton("取消", null)
                    .setPositiveButton("前往设置",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //跳转GPS设置界面
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivityForResult(intent, 2);
                                }
                            })
                    .show();

        }else{
            selectConfigWifi();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(isOPen()){
                selectConfigWifi();
            }else{
                Toast.makeText(ConnectWifiActivity.this,"请打开机器人定位",Toast.LENGTH_LONG).show();
            }
        }
    }
    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @return true 表示开启
     */
    public boolean isOPen() {
        LocationManager locationManager
                = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initEvent() {

    }
}

