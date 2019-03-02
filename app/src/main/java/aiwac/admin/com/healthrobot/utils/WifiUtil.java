package aiwac.admin.com.healthrobot.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import aiwac.admin.com.healthrobot.bean.WifiInfo;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.exception.WifiException;

/**     Wifi的工具类，用于判断Wifi是否开启以及wifi的强弱等
 * Created by zyt on 2017/11/8.
 */

public class WifiUtil {


    public static boolean checkWifi(Context context){
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            boolean isAvailability = false;
            if(wifiManager != null) {
                isAvailability = wifiManager.isWifiEnabled();
                LogUtil.d(Constant.WIFI_AVAILABILITY);
            }else {
                LogUtil.d(Constant.WIFI_UNAVAILABILITY);
                throw new WifiException(Constant.WIFI_UNAVAILABILITY);
            }
            return isAvailability;
        }catch (Exception e){
            LogUtil.d(Constant.WIFI_UNAVAILABILITY);
            throw new WifiException(Constant.WIFI_UNAVAILABILITY, e);
        }
    }

    public static List<WifiInfo> findConnectableWifi(Context context){
        List<WifiInfo> wifiInfos = new ArrayList<WifiInfo>();
        //wifi模块
        try{
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            List<ScanResult> list = wifiManager.getScanResults();
            if(list != null && list.size() > 0) {
                int wifiCount = list.size();
                LogUtil.d(Constant.WIFI_CONNECTABLE + " :　" + wifiCount);
                //wifis = new String[wifiCount];
                for (ScanResult result: list) {
                    WifiInfo wifiInfo = new WifiInfo();
                    wifiInfo.setSsid(result.SSID);
                    wifiInfo.setBssid(result.BSSID);
                    wifiInfo.setCapabilities(result.capabilities);
                    wifiInfo.setFrequency(result.frequency);
                    wifiInfo.setLevel(result.level);
                    wifiInfos.add(wifiInfo);

                    //wifis[i] = list.get(i).SSID; //+ " : " + list.get(i).level;
                    LogUtil.d(wifiInfo.getSsid());
                }

                return wifiInfos;
            }
        }catch (Exception e){
            LogUtil.d(e.getMessage());
        }

        LogUtil.d(Constant.WIFI_NO_CONNECTABLE);
        return wifiInfos;
    }




    public static int obtainWifiStrong(Context context){
        try{
            int level = Constant.WIFI_SIGNAL_LEVEL_NO;
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); // 取得WifiManager对象
            android.net.wifi.WifiInfo info = wifiManager.getConnectionInfo(); // 取得WifiInfo对象
            //获得信号强度值
            int value = info.getRssi();

            //根据获得的信号强度发送信息
            if (value <= 0 && value >= -50) {
                level = Constant.WIFI_SIGNAL_LEVEL_VERYHIGH;
            } else if (value < -50 && value >= -70) {
                level = Constant.WIFI_SIGNAL_LEVEL_HIGH;
            } else if (value < -70 && value >= -80) {
                level = Constant.WIFI_SIGNAL_LEVEL_NORMAL;
            } else if (value < -80 && value >= -100) {
                level = Constant.WIFI_SIGNAL_LEVEL_LOW;
            } else {
                level = Constant.WIFI_SIGNAL_LEVEL_NO;
            }

            return level;
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.d(Constant.WIFI_GET_WIFIMANAGER_EXCEPTION);
            throw new WifiException(Constant.WIFI_GET_WIFIMANAGER_EXCEPTION, e);
        }
    }


    /**
     * 检查用户是用的 Wifi 还是 流量
     * @return 0 既不是wifi也不是流量  1 流量  2 wifi
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static int isWifiOrTraffic(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //需要ipi21才可以使用  目前最小min api 15
            Network[] networks = cm.getAllNetworks();
            for (Network network : networks) {
                NetworkInfo info = cm.getNetworkInfo(network);
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                        LogUtil.d(Constant.WIFI_USE_WIFI);
                        return Constant.WIFI_TYPE_WIFI;
                    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        LogUtil.d(Constant.WIFI_USE_MOBILE);
                        return Constant.WIFI_TYPE_MOBILE;
                    }
                }
            }

            LogUtil.d(Constant.WIFI_USE_NO);
            return Constant.WIFI_TYPE_NO;

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(Constant.WIFI_GET_CONNECTIVITYMANAGER_EXCEPTION);
            throw new WifiException(Constant.WIFI_GET_CONNECTIVITYMANAGER_EXCEPTION, e);
        }
    }

    private List<WifiConfiguration> wificonfigList = new ArrayList<WifiConfiguration>();
    private WifiManager mWifiManager;//管理wifi
    private Context context;

    public WifiUtil(Context context){
        this.context = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

    }


    public boolean isOpenWifi(){
        return mWifiManager.isWifiEnabled();
    }

    // 打开WIFI
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }else if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
            LogUtil.d("Wifi正在开启");

        }else{
            LogUtil.d("Wifi已经开启");

        }
    }

    // 关闭WIFI
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }else if(mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED){
            LogUtil.d("Wifi已经关闭");
        }else{
            LogUtil.d("请重新关闭");
        }
    }

    /* 得到配置信息 */
    public void getConfigurations() {
        wificonfigList = mWifiManager.getConfiguredNetworks();
    }

    /**
     * 该链接是否已经配置过
     * @param SSID
     * @return
     */
    public int isConfigured(String SSID) {
        getConfigurations();
        for (int i = 0; i < wificonfigList.size(); i++) {
            if (wificonfigList.get(i).SSID.equals(SSID)) {
                return wificonfigList.get(i).networkId;
            }
        }
        return -1;
    }

    /**
     * 添加wifi配置
     * @param ssid
     * @param pwd
     * @return
     */
    public int addWifiConfig(String ssid,String pwd){
        int wifiId = -1;
        WifiConfiguration wifiCong = new WifiConfiguration();
        wifiCong.SSID = "\""+ssid+"\"";
        wifiCong.preSharedKey = "\""+pwd+"\"";
        wifiCong.hiddenSSID = false;
        wifiCong.status = WifiConfiguration.Status.ENABLED;
        wifiId = mWifiManager.addNetwork(wifiCong);
        return wifiId;
    }



    public void connect(String ssid,String pass){

        int wifiId = isConfigured("\""+ssid+"\"");
        System.out.println("wifiId:"+wifiId);
        if(wifiId != -1){
            System.out.println("@@");
            //mWifiManager.enableNetwork(wifiId, true);
            mWifiManager.removeNetwork(wifiId);
        }
        int netWorkId = addWifiConfig(ssid, pass);//添加该网络的配置
        System.out.println("netWorkId:"+netWorkId);
        mWifiManager.enableNetwork(netWorkId, true);

    }

    public void delete(String ssid){
        int wifiId = isConfigured("\""+ssid+"\"");
        mWifiManager.removeNetwork(wifiId);
    }
}
