package com.flyscale.citmode.fragment.items;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyscale.citmode.R;
import com.flyscale.citmode.fragment.BaseFragment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by bian on 2018/3/6.
 */

public class KeyboardFragment extends BaseFragment {

    private static final String TAG = "KeyboardFragment";
    private View mView;
    private String[] mKeys;
    //存储KeyEvent的值和对应的view在ViewGroup中的索引，必须固定，包括string.xml中定义的顺序也不可随便更改
    private HashMap<Integer, Integer> mKeyMaps = new HashMap<Integer, Integer>();
    private HashMap<Integer, Boolean> mKeysRecord = new HashMap<Integer, Boolean>();
    private LinearLayout keyboard;
    private View[] mViews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initKeyMaps();
        initKeyRecord();
        return initView();
    }

    /**
     * 初始化按键状态，记录按键是否被按下,如果有新增按键，在末尾添加，
     * 同时也要在{@link #initKeyMaps}和string.xml中末尾添加
     */
    private void initKeyRecord() {
        mKeysRecord.put(KeyEvent.KEYCODE_DPAD_UP, false);
        mKeysRecord.put(KeyEvent.KEYCODE_DPAD_DOWN, false);
        mKeysRecord.put(KeyEvent.KEYCODE_DPAD_LEFT, false);
        mKeysRecord.put(KeyEvent.KEYCODE_DPAD_RIGHT, false);
        mKeysRecord.put(KeyEvent.KEYCODE_DPAD_CENTER, false);
        mKeysRecord.put(KeyEvent.KEYCODE_MENU, false);
        mKeysRecord.put(KeyEvent.KEYCODE_BACK, false);
        mKeysRecord.put(KeyEvent.KEYCODE_CALL, false);
        mKeysRecord.put(KeyEvent.KEYCODE_POWER, false);
        mKeysRecord.put(KeyEvent.KEYCODE_0, false);
        mKeysRecord.put(KeyEvent.KEYCODE_1, false);
        mKeysRecord.put(KeyEvent.KEYCODE_2, false);
        mKeysRecord.put(KeyEvent.KEYCODE_3, false);
        mKeysRecord.put(KeyEvent.KEYCODE_4, false);
        mKeysRecord.put(KeyEvent.KEYCODE_5, false);
        mKeysRecord.put(KeyEvent.KEYCODE_6, false);
        mKeysRecord.put(KeyEvent.KEYCODE_7, false);
        mKeysRecord.put(KeyEvent.KEYCODE_8, false);
        mKeysRecord.put(KeyEvent.KEYCODE_9, false);
        mKeysRecord.put(KeyEvent.KEYCODE_STAR, false);
        mKeysRecord.put(KeyEvent.KEYCODE_POUND, false);
        mKeysRecord.put(126, false);//HandFree

    }

    /**
     * 定义按键和对应view的映射关系，如果有新增按键，在末尾添加，
     * 同时也要在{@link #initKeyRecord()}和string.xml中末尾添加
     */
    private void initKeyMaps() {
        mKeyMaps.put(KeyEvent.KEYCODE_DPAD_UP, 0);
        mKeyMaps.put(KeyEvent.KEYCODE_DPAD_DOWN, 1);
        mKeyMaps.put(KeyEvent.KEYCODE_DPAD_LEFT, 2);
        mKeyMaps.put(KeyEvent.KEYCODE_DPAD_RIGHT, 3);
        mKeyMaps.put(KeyEvent.KEYCODE_DPAD_CENTER, 4);
        mKeyMaps.put(KeyEvent.KEYCODE_MENU, 5);
        mKeyMaps.put(KeyEvent.KEYCODE_BACK, 6);
        mKeyMaps.put(KeyEvent.KEYCODE_CALL, 7);
        mKeyMaps.put(KeyEvent.KEYCODE_POWER, 8);
        mKeyMaps.put(KeyEvent.KEYCODE_0, 9);
        mKeyMaps.put(KeyEvent.KEYCODE_1, 10);
        mKeyMaps.put(KeyEvent.KEYCODE_2, 11);
        mKeyMaps.put(KeyEvent.KEYCODE_3, 12);
        mKeyMaps.put(KeyEvent.KEYCODE_4, 13);
        mKeyMaps.put(KeyEvent.KEYCODE_5, 14);
        mKeyMaps.put(KeyEvent.KEYCODE_6, 15);
        mKeyMaps.put(KeyEvent.KEYCODE_7, 16);
        mKeyMaps.put(KeyEvent.KEYCODE_8, 17);
        mKeyMaps.put(KeyEvent.KEYCODE_9, 18);
        mKeyMaps.put(KeyEvent.KEYCODE_STAR, 19);
        mKeyMaps.put(KeyEvent.KEYCODE_POUND, 20);
        mKeyMaps.put(126, 21);//HandFree

    }

    public View initView() {
        mView = mActivity.getLayoutInflater().inflate(R.layout.fragment_keyboard, null);
        inflate();
        return mView;
    }

    private void inflate() {
        int lineCount = 8;
        keyboard = (LinearLayout) mView.findViewById(R.id.keyboard);
        mKeys = getResources().getStringArray(R.array.keys);
        mViews = new View[mKeys.length];
        for (int i = 0; i < Math.ceil((double) mKeys.length / (double) lineCount); i++) {
            LinearLayout line = new LinearLayout(mActivity);
            line.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < lineCount; j++) {
                View child = mActivity.getLayoutInflater().inflate(R.layout.key, null);
                TextView key = (TextView) child.findViewById(R.id.tv);
                int index = i * lineCount + j;
                if (index >= mKeys.length)
                    break;
                mViews[index] = key;
                if (index >= mKeys.length) {
                    break;
                } else {
                    Log.i(TAG, "key=" + mKeys[index]);
                    key.setText(mKeys[index]);
                }
                line.addView(child);
            }
            keyboard.addView(line);
        }
    }

    @Override
    public void onKeyUp(int keyCode) {
        Log.i(TAG, "keyCode=" + keyCode);
        View child = mViews[mKeyMaps.get(keyCode)];
        Log.i(TAG, "child=" + child);
        if (child != null) {
            child.setVisibility(View.INVISIBLE);
            if (isAllTest()) {
                mActivity.remove(this);
            }
        }
    }

    /**
     * 所有按键是否验证通过
     *
     * @return
     */
    public boolean isAllTest() {
        Set<Map.Entry<Integer, Boolean>> entries = mKeysRecord.entrySet();
        Iterator<Map.Entry<Integer, Boolean>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Boolean> next = iterator.next();
            if (!next.getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        mActivity.setCurrentFragment(this);
    }

}
