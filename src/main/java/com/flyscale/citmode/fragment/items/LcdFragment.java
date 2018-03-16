package com.flyscale.citmode.fragment.items;

import android.graphics.Color;
import android.os.Bundle;
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

public class LcdFragment extends BaseFragment {

    private static final String TAG = "LcdFragment";
    private boolean flag = false;
    private View mView;
    private TextView title;
    private TextView content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    public View initView() {
        mView = mActivity.getLayoutInflater().inflate(R.layout.fragment_lcd, null);
        title = (TextView) mView.findViewById(R.id.title);
        content = (TextView) mView.findViewById(R.id.content);
        title.setText(getResources().getStringArray(R.array.test_list)[1]);
//        startTest();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setCurrentFragment(this);
    }

    private void startTest() {
        flag = true;
        if (title != null)
            title.setVisibility(View.GONE);
        if ((content != null)) {
            content.setVisibility(View.GONE);
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (flag) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.setBackgroundColor(Color.BLACK);
                        }
                    });
                    LcdFragment.this.sleep();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.setBackgroundColor(Color.WHITE);
                        }
                    });
                    LcdFragment.this.sleep();
                }
            }
        }.start();
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onKeyUp(int keyCode) {
        Log.i(TAG, "keyCode=" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                || keyCode == KeyEvent.KEYCODE_MENU) {
            flag = !flag;
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && flag) {
                startTest();
            }
        }
    }
}
