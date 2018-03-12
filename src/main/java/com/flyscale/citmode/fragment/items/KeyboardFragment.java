package com.flyscale.citmode.fragment.items;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyscale.citmode.R;
import com.flyscale.citmode.fragment.BaseFragment;

/**
 * Created by bian on 2018/3/6.
 */

public class KeyboardFragment extends BaseFragment {

    private static final String TAG = "KeyboardFragment";
    private View mView;
    private String[] mKeys;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    public View initView() {
        mView = mActivity.getLayoutInflater().inflate(R.layout.fragment_keyboard, null);
        inflate();
        return mView;
    }

    private void inflate() {
        LinearLayout keyboard = (LinearLayout) mView.findViewById(R.id.keyboard);
        mKeys = getResources().getStringArray(R.array.keys);
        for (int i = 0; i < mKeys.length / 8; i++) {
            LinearLayout line = new LinearLayout(mActivity);
            line.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < 8; j++) {
                TextView key = (TextView) mActivity.getLayoutInflater().inflate(R.layout.key, null);
                int index = i * 8 + j;
                if (index >= mKeys.length) {
                    break;
                } else {
                    Log.i(TAG, "key="+mKeys[index]);
                    key.setText(mKeys[index]);
                }
                line.addView(key);
            }
            keyboard.addView(line);
        }
    }

    @Override
    public void onKeyUp(int keyCode) {

    }
}
