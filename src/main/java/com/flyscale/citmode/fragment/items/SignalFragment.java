package com.flyscale.citmode.fragment.items;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyscale.citmode.R;
import com.flyscale.citmode.fragment.BaseFragment;
import com.flyscale.citmode.utils.NetUtils;

/**
 * Created by bian on 2018/3/6.
 */

public class SignalFragment extends BaseFragment {

    private static final String TAG = SignalFragment.class.getSimpleName();
    // chinamobile  46000
    // chinaunicom  46001
    // chinatelecom 46003
    private static final int MOBILE  = 46000;
    private static final int UNICOM  = 46001;
    private static final int TELECOM = 46003;

    private TelephonyManager telephonyManager;
    private TextView content;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onStart() {
        super.onStart();

        initData();
    }

    public View initView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.fragment_versioninfo, null);
        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(getResources().getStringArray(R.array.test_list)[8]);
        content = (TextView)view.findViewById(R.id.content);

        return view;
    }



    public void initData() {
        telephonyManager = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {

            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);

                StringBuilder sb = new StringBuilder();

                if(NetUtils.getInstance().isNetworkAvailable(mActivity)) {
                    if(NetUtils.getInstance().isWifi(mActivity)) {
                        WifiManager wifiManager = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                        if(wifiInfo.getBSSID() != null) {
                            sb.append("Wifi Signal").append("\n")
                                    .append("strength: ").append(wifiManager.calculateSignalLevel(wifiInfo.getRssi(),5));
                        } else {
                            sb.append("Wifi Signal").append("\n")
                                    .append("strength:").append("0");
                        }
                    } else {
                        content.setText(displayDbm(getOperator(telephonyManager),signalStrength,sb));
                    }
                } else {
                    Log.e(TAG, "There is no network");
                    sb.append("Unknown strength: 0").append("\n").append(" Unknown range: 0");
                    content.setText(sb.toString());
                }
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    public void onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_MENU ||
                keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
            mActivity.remove(this);
        }
    }

    public int getOperator(TelephonyManager telephonyManager) {
        String operator = telephonyManager.getNetworkOperator();
        Log.e(TAG, "Display the operator code: " + operator);

        return Integer.parseInt(operator);
    }

    public String displayDbm(int operator, SignalStrength signalStrength,StringBuilder sb) {
        String signalInfo = signalStrength.toString();
        Log.e(TAG, "signal info is " + signalInfo);

        String[] params = signalInfo.split(" ");
        for(String pa : params) {
            Log.i(TAG, "element is " + pa);
        }

        if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
            int lteDbm = Integer.parseInt(params[9]);
            sb.append(getOperatorName(operator)).append(" strength: ").append(lteDbm)
                    .append("\n").append(getOperatorName(operator)).append(" range: >=-90");
        } else if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS){
            switch(operator) {
                case MOBILE:
                    sb.append(getOperatorName(operator)).append(" strength: ").append(0)
                            .append("\n").append(getOperatorName(operator)).append(" range: >=-100");
                    break;
                case UNICOM:
                    int cdmaDbm = signalStrength.getCdmaDbm();
                    sb.append(getOperatorName(operator)).append(" strength: ").append(cdmaDbm)
                            .append("\n").append(getOperatorName(operator)).append(" range: >=-100");
                    break;
                case TELECOM:
                    int evdoDbm = signalStrength.getEvdoDbm();
                    sb.append(getOperatorName(operator)).append(" strength: ").append(evdoDbm)
                            .append("\n").append(getOperatorName(operator)).append(" range: >=-110");
                    break;
                default:
                    break;
            }
        } else {
            int asu = signalStrength.getGsmSignalStrength();
            int dbm = -113 + 2*asu;
            sb.append(getOperatorName(operator)).append(" strength: ").append(dbm)
                    .append("\n").append(getOperatorName(operator)).append(" range: >=-110");
        }

        return sb.toString();
    }

    public String getOperatorName(int operator) {
        switch(operator) {
            case MOBILE:
                return "MOBILE";
            case UNICOM:
                return "UNICOM";
            case TELECOM:
                return "TELECOM";
            default:
                break;
        }
        return "UNKNOWN";
    }
}
