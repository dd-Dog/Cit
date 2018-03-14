package com.flyscale.citmode.fragment.items;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyscale.citmode.R;
import com.flyscale.citmode.fragment.BaseFragment;


import java.util.Date;

/**
 * Created by bian on 2018/3/6.
 */

public class VersionInfoFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return initView();
    }

    public View initView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.fragment_versioninfo, null);
        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(getResources().getStringArray(R.array.test_list)[0]);
        TextView content = (TextView)view.findViewById(R.id.content);
        content.setText(getVersionInfo());
        return view;
    }

    private String getVersionInfo() {
        String info = "";
        info += Build.PRODUCT;
        info += Build.DISPLAY;
//        Date date = new Date("");

        return info;
    }

    @Override
    public void onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_MENU ||
                keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
            mActivity.remove(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setCurrentFragment(this);
    }
}
