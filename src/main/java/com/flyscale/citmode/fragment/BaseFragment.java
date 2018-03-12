package com.flyscale.citmode.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyscale.citmode.MainActivity;

/**
 * Created by bian on 2018/3/6.
 */

abstract public class BaseFragment extends Fragment {

    public MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    abstract public void onKeyUp(int keyCode);

    abstract public View initView();
}
