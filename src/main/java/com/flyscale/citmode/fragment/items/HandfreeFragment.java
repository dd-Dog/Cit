package com.flyscale.citmode.fragment.items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyscale.citmode.fragment.BaseFragment;

/**
 * Created by bian on 2018/3/6.
 */

public class HandfreeFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setCurrentFragment(this);
    }

    public View initView() {
        return null;
    }

    @Override
    public void onKeyUp(int keyCode) {

    }
}
