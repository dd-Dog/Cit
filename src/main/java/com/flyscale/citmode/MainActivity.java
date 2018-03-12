package com.flyscale.citmode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.flyscale.citmode.fragment.BaseFragment;
import com.flyscale.citmode.fragment.MainFragment;
import com.flyscale.citmode.fragment.ItemTestListFragment;
import com.flyscale.citmode.fragment.items.BacklightFragment;
import com.flyscale.citmode.fragment.items.BatteryFragment;
import com.flyscale.citmode.fragment.items.HandfreeFragment;
import com.flyscale.citmode.fragment.items.HandsetFragment;
import com.flyscale.citmode.fragment.items.KeyboardFragment;
import com.flyscale.citmode.fragment.items.LcdFragment;
import com.flyscale.citmode.fragment.items.MelodyFragment;
import com.flyscale.citmode.fragment.items.SignalFragment;
import com.flyscale.citmode.fragment.items.VersionInfoFragment;


//CIT即Customer Interface Test，用户界面测试
//中诺*#87#
public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private MainFragment mMainFragment;
    private BaseFragment mCurentFragment;
    private BaseFragment[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        //添加主界面
        mMainFragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, mMainFragment);
        fragmentTransaction.commit();

        items = new BaseFragment[9];
        items[0] = new VersionInfoFragment();
        items[1] = new LcdFragment();
        items[2] = new BacklightFragment();
        items[3] = new BatteryFragment();
        items[4] = new MelodyFragment();
        items[5] = new HandsetFragment();
        items[6] = new HandfreeFragment();
        items[7] = new KeyboardFragment();
        items[8] = new SignalFragment();
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp::keyCode=" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.i(TAG, "mCurrentFragment=" + mCurentFragment);
                if (mCurentFragment != null) {
                    mCurentFragment.onKeyUp(keyCode);
                }
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        Log.i(TAG, "onAttachFragment");
        mCurentFragment = (BaseFragment) fragment;

        super.onAttachFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        super.onBackPressed();
    }

    /**
     * 切换到ITEM测试
     */
    public void attachItemList() {
        ItemTestListFragment itemTestListFragment = new ItemTestListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, itemTestListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void testItem(int position) {
        ItemTestListFragment itemTestListFragment = new ItemTestListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, items[position]);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setCurrentFragment(BaseFragment fragment) {
        mCurentFragment = fragment;
    }

    public void remove(BaseFragment fragment) {
        Log.i(TAG, "remove::fragment=" + fragment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
        onBackPressed();
    }
}
