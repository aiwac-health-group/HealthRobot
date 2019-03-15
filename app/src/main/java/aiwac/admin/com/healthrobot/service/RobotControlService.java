package aiwac.admin.com.healthrobot.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import aiwac.admin.com.healthrobot.sport.AiwacSportApi;
import aiwac.admin.com.healthrobot.utils.LogUtil;

public class RobotControlService extends Service {
    private static RobotControlService minstance;
    private static String TAG="RobotControlService";
   private AiwacSportApi aiwacSportApi;
    private int type=0b0000000;
    public RobotControlService() {
        minstance=this;
        minstance=this;

        aiwacSportApi=new AiwacSportApi();

    }
    public static RobotControlService getInstance(){
        return minstance;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void getMessage(String message){
        String messageCode="";
        if("前".equals(message)){
            messageCode="UP";
        }else if("后".equals(message)){
            messageCode="DOWN";
        }else if("左".equals(message)){
            messageCode="LEFT";
        }else if("右".equals(message)){
            messageCode="RIGHT";
        }else{
            messageCode="NONE";
        }
        try {
            this.getMessageCode(messageCode);
        }catch (Exception e){
            LogUtil.d("控制模块还没有正常连接");
        }
    }

    public void getMessageCode(String messageCode){
        Log.d(TAG, "getMessage: "+messageCode);
        if(messageCode.equals("UP")){
            Log.d(TAG, "上");
            up();
        }
        if(messageCode.equals("DOWN")){
            Log.d(TAG, "下");
            down();
        }
        if(messageCode.equals("LEFT")){
            Log.d(TAG, "左");
            left();
        }
        if(messageCode.equals("RIGHT")){
            Log.d(TAG, "右");
            right();
        }
        if(messageCode.equals("NONE")){
            Log.d(TAG, "停止");
            stop();
        }
        if(messageCode.equals("UP_AND_LEFT")){
            Log.d(TAG, "左上");
            upAndLeft();
        }
        if(messageCode.equals("UP_AND_RIGHT")){
            Log.d(TAG, "右上");
            upAndRight();
        }
        if(messageCode.equals("DOWN_AND_LEFT")){
            Log.d(TAG, "左下");
            downAndLeft();
        }
        if(messageCode.equals("DOWN_AND_RIGHT")){
            Log.d(TAG, "右下");
            downAndRight();
        }

        type=0b0000000;
        Log.d(TAG, "getMessage: type:"+type);
    }

    private void  up(){
        type=type|0b0000001;
        aiwacSportApi.aiwacSportType(type);
        type= 0;
    }
    private void  down(){
        type=type|0b0000010;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  left(){
        type=type|0b0000100;
        aiwacSportApi.aiwacSportType(type);
    }
    private void  right(){
        type=type|0b0001000;
        aiwacSportApi.aiwacSportType(type);
    }
    private void upAndLeft(){
        type=type|0b0000101;
        aiwacSportApi.aiwacSportType(type);
    }
    private void upAndRight(){
        type=type|0b0001001;
        aiwacSportApi.aiwacSportType(type);
    }
    private void downAndLeft(){
        type=type|0b0000110;
        aiwacSportApi.aiwacSportType(type);
    }
    private void downAndRight(){
        type=type|0b0001010;
        aiwacSportApi.aiwacSportType(type);
    }
    private void stop(){
        type=type&0b1110000;
        aiwacSportApi.aiwacSportType(type);
    }
    private void openLightOne(){
        type=type|0b0010000;
        aiwacSportApi.aiwacSportType(type);
    }
    private void openLightTwo(){
        type=type|0b0100000;
        aiwacSportApi.aiwacSportType(type);
    }
    private void closeLightOne(){
        type=type&0b1101111;
        aiwacSportApi.aiwacSportType(type);
    }
    private void closeLightTwo(){
        type=type|0b1011111;
        aiwacSportApi.aiwacSportType(type);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
