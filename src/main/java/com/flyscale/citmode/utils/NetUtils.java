package com.flyscale.citmode.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

/**
 * Created by wesson_wxy on 2018/3/12.
 */

public class NetUtils {
    private static final String TAG = NetUtils.class.getSimpleName();

    private NetUtils() {}

    //
    public boolean isNetworkAvailable(Context context) {
        // get all connection manager object
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) {
            return false;
        } else {
            // get network info object
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

            if(networkInfos != null && networkInfos.length > 0) {
                for(NetworkInfo ni : networkInfos) {
                    // check the network state
                    if(ni.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getState() ==NetworkInfo.State.CONNECTED
                && telephonyManager.getNetworkType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * get a WifiManager instance
     * @param context
     * @return
     */
    public WifiManager getWifiManager(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if(wifiManager != null) {
            return wifiManager;
        }

        return null;
    }

    /**
     * wifi setting
     * @param context application context
     * @param flag flag
     */
    public void setWifiEnabled(Context context,boolean flag) {
        if(getWifiManager(context) != null) {
            getWifiManager(context).setWifiEnabled(flag);

            if(flag) {
                Log.e(TAG,"open wifi");
            } else {
                Log.e(TAG, "close wifi");
            }
        }
    }

    /**
     * check wifi state
     * @param context application context
     * @return state
     */
    public int checkWifiState(Context context) {
        int state = WifiManager.WIFI_STATE_UNKNOWN;

        if(getWifiManager(context) != null) {
            state = getWifiManager(context).getWifiState();

            return state;
        }

        return state;
    }

    public WifiInfo getDetailWifiInfo(Context context) {
        if(getWifiManager(context) != null) {
            WifiInfo wifiInfo = getWifiManager(context).getConnectionInfo();

            if(wifiInfo != null) {
                printDetailWifiInfo(wifiInfo);
                return wifiInfo;
            }
        }

        return null;
    }

    /**
     * print cell phone wifi information
     * @param wifiInfo
     */
    public void printDetailWifiInfo(WifiInfo wifiInfo) {
        StringBuilder sInfo = new StringBuilder();

        int Ip = wifiInfo.getIpAddress();
        String strIp = "" + (Ip & 0xFF) + "."
                + ((Ip >> 8) & 0xFF) + "."
                + ((Ip >> 16) & 0xFF) + "."
                + ((Ip >> 24) & 0xFF);

        sInfo.append("\n--BSSID : ").append(wifiInfo.getBSSID())
                .append("\n--SSID : ").append(wifiInfo.getSSID())
                .append("\n--nIpAddress : ").append(strIp)
                .append("\n--MacAddress : ").append(wifiInfo.getMacAddress())
                .append("\n--NetworkId : ").append(wifiInfo.getNetworkId())
                .append("\n--LinkSpeed : ").append(wifiInfo.getLinkSpeed()).append("Mbps")
                .append("\n--Rssi : ").append(wifiInfo.getRssi())
                .append("\n--SupplicantState : ").append(wifiInfo.getSupplicantState());

        Log.e(TAG, "Display cell phone wifi");
        Log.e(TAG, sInfo.toString());
    }

    public List<ScanResult> getAroundWifiDeviceInfo(Context context) {
        if(getWifiManager(context) != null) {

            List<ScanResult> scanResults = getWifiManager(context).getScanResults();

            for(ScanResult result : scanResults) {
                printAroundWifiDeviceInfo(result);
            }

            return scanResults;
        }

        return null;
    }

    /**
     * print around device wifi information
     * @param scanResult
     */
    public void printAroundWifiDeviceInfo(ScanResult scanResult) {
        StringBuilder sInfo = new StringBuilder();

        sInfo.append("\n设备名：").append(scanResult.SSID).append("\n信号强度：").append(scanResult.level).
                append("\n信号等级:").append(WifiManager.calculateSignalLevel(scanResult.level,4));

        Log.e(TAG,"Display around device wifi");
        Log.e(TAG,sInfo.toString());
    }


    public static NetUtils getInstance() {
        return NetUtilsHolder.INSTANCE;
    }

    private static class NetUtilsHolder {
        private static NetUtils INSTANCE = new NetUtils();
    }
}
