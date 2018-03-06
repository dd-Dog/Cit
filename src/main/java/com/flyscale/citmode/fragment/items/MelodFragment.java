package com.flyscale.citmode.fragment.items;

import android.os.Bundle;
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

public class MelodFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return initView();
    }

    public View initView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.fragment_versioninfo, null);
        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(getResources().getStringArray(R.array.test_list)[4]);
        TextView content = (TextView)view.findViewById(R.id.content);
        content.setText(getResources().getString(R.string.melody_test));
        return view;
    }

    @Override
    public void onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_MENU ||
                keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
            mActivity.remove(this);
        }
    }
}
