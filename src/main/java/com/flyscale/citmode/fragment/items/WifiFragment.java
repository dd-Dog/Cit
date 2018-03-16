package com.flyscale.citmode.fragment.items;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyscale.citmode.MainActivity;
import com.flyscale.citmode.R;
import com.flyscale.citmode.fragment.BaseFragment;
import com.flyscale.citmode.utils.NetUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by wesson_wxy on 2018/3/14.
 */

public class WifiFragment extends BaseFragment {
    private static final String TAG = WifiFragment.class.getSimpleName();
    private static final int SUCCESS = 1;
    private static final int FAILED = 2;

    TextView content;
    private Handler handler;
    private SearchThread thread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    public View initView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.fragment_versioninfo, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(getResources().getStringArray(R.array.test_list)[9]);
        content = (TextView) view.findViewById(R.id.content);
        content.setText(getResources().getString(R.string.wifitest));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        searchWifiSignal();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopSearchWifiSignal();
    }

    public void searchWifiSignal() {
        handler = new MyHandler(mActivity,content);

        thread = new SearchThread();
        thread.start();
    }

    public void stopSearchWifiSignal() {
        if(thread != null) {
            thread = null;
        }

        if(handler != null) {
            handler = null;
        }
    }

    @Override
    public void onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_MENU ||
                keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
            mActivity.remove(this);
        }
    }

    class SearchThread extends Thread {

        @Override
        public void run() {
            Message msg = new Message();

            NetUtils.getInstance().setWifiEnabled(mActivity,true);

            try{
                Thread.sleep(3000);

                switch(NetUtils.getInstance().checkWifiState(mActivity)) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        Log.e(TAG,"Wifi closed");
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        Log.e(TAG, "Wifi closing");
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        Log.e(TAG,"Wifi opening");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.e(TAG, "Wifi opened");

                        List<ScanResult> results = NetUtils.getInstance().getAroundWifiDeviceInfo(mActivity);

                        if(results != null && results.size() != 0) {
                            msg.what = SUCCESS;
                        } else {
                            WifiInfo wifiInfo = NetUtils.getInstance().getDetailWifiInfo(mActivity);
                            if(wifiInfo != null) {
                                msg.what = SUCCESS;
                            } else {
                                msg.what = FAILED;
                            }
                        }

                        NetUtils.getInstance().setWifiEnabled(mActivity,false);
                        handler.sendEmptyMessage(msg.what);

                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        Log.e(TAG, "Wifi state unknown");
                        break;
                }
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;
        private TextView content;

        MyHandler(MainActivity activity, final TextView content) {
            mActivity = new WeakReference<MainActivity>(activity);
            this.content = content;
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = mActivity.get();
            switch(msg.what) {
                case SUCCESS:
                    content.setText(R.string.search_success);
                    break;
                case FAILED:
                    content.setText(R.string.search_failed);
                    break;
            }
        }
    }
}
