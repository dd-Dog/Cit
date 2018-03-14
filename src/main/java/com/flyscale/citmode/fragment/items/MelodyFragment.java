package com.flyscale.citmode.fragment.items;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyscale.citmode.R;
import com.flyscale.citmode.fragment.BaseFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bian on 2018/3/6.
 */

public class MelodyFragment extends BaseFragment {

    private static String TAG = "MelodyFragment";
    private int mIndex;
    private String strRingtoneFolder = "/system/media/audio/ringtones";
    private ArrayList<String> mPathList;
    private MediaPlayer mCurrentPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    public View initView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.fragment_versioninfo, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(getResources().getStringArray(R.array.test_list)[4]);
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText(getResources().getString(R.string.melody_test));
        getRingtonesName(strRingtoneFolder);

        playNext(mIndex);
        return view;
    }


    private void getRingtonesName(String path) {
        File file = new File(path);
        File[] subFile = file.listFiles();
        mPathList = new ArrayList<String>();
        for (int i = 0; i < subFile.length; i++) {
            String name = subFile[i].getName();
            mPathList.add("/" + name);
            Log.i(TAG, "name=" + name);
        }
    }

    @Override
    public void onKeyUp(int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                mActivity.remove(this);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_LEFT:
                mIndex--;
                if (mIndex < 0){
                    mIndex = mPathList.size()-1;
                }
                playNext(mIndex);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                mIndex++;
                if (mIndex >= mPathList.size()){
                    mIndex = 0;
                }
                playNext(mIndex);
                break;

        }
    }

    private void playNext(int index) {
        Log.i(TAG, "playNext::index=" + index);
        if (mCurrentPlayer != null) {
            mCurrentPlayer.stop();
            mCurrentPlayer.release();
        }
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(strRingtoneFolder + mPathList.get(index));
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            mCurrentPlayer = mediaPlayer;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mActivity.setCurrentFragment(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCurrentPlayer != null) {
            mCurrentPlayer.stop();
            mCurrentPlayer.release();
        }
    }
}
