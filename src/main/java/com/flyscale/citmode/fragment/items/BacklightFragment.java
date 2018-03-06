package com.flyscale.citmode.fragment.items;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyscale.citmode.R;
import com.flyscale.citmode.fragment.BaseFragment;

/**
 * Created by bian on 2018/3/6.
 */

public class BacklightFragment extends BaseFragment {
    private static final String TAG = "BacklightFragment";
    private View mView;
    private boolean status;
    private int brightness;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            brightness = android.provider.Settings.System.getInt(mActivity.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return initView();
    }

    public View initView() {
        mView = mActivity.getLayoutInflater().inflate(R.layout.fragment_versioninfo, null);
        TextView title = (TextView) mView.findViewById(R.id.title);
        title.setText(getResources().getStringArray(R.array.test_list)[2]);
        TextView content = (TextView) mView.findViewById(R.id.content);
        content.setText(getResources().getString(R.string.test_backlight));
        switchBackLight(true);
        status = true;
        return mView;
    }

    private void switchBackLight(boolean state) {
        android.provider.Settings.System.putInt(mActivity.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                state ? 255 : 0);
        status = state;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy");
        super.onDestroy();
        android.provider.Settings.System.putInt(mActivity.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                brightness);
    }

    @Override
    public void onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                || keyCode == KeyEvent.KEYCODE_MENU) {
            switchBackLight(!status);
        }
    }
}
