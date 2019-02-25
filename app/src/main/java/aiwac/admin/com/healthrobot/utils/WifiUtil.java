package aiwac.admin.com.healthrobot.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import aiwac.admin.com.healthrobot.bean.WifiInfo;
import aiwac.admin.com.healthrobot.common.Constant;
import aiwac.admin.com.healthrobot.exception.WifiException;

/**     Wifi的工具类，用于判断Wifi是否开启以及wifi的强弱等
 * Created by luwang on 2017/11/8.
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


    /*

     // Wifi的连接速度及信号强度
    public int obtainWifiInfo(Context context) {
        int strength = 0;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); // 取得WifiManager对象
        WifiInfo info = wifiManager.getConnectionInfo(); // 取得WifiInfo对象
        if (info.getBSSID() != null) {
            strength = WifiManager.calculateSignalLevel(info.getRssi(), 5); // 链接信号强度，5为获取的信号强度值在5以内
            int speed = info.getLinkSpeed(); // 链接速度
            String units = WifiInfo.LINK_SPEED_UNITS; // 链接速度单位
            String ssid = info.getSSID(); // Wifi源名称
            LogUtil.d(ssid + "wifi连接 : " + strength + speed + units);
        }else{
            LogUtil.d(Constant.WIFI_CLOSE);
        }
        return strength; // return info.toString();
    }
    */

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
}
