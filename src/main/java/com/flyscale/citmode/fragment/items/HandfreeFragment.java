package com.flyscale.citmode.fragment.items;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class HandfreeFragment extends BaseFragment {

    private static final String TAG = HandfreeFragment.class.getSimpleName();

    private boolean mRunning;

    private AudioRecord m_record;
    private AudioTrack m_track;
    private LoopThread loopThread;

    boolean isOpen;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return initView();
    }

    public View initView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.fragment_versioninfo, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(getResources().getStringArray(R.array.test_list)[6]);
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText(getResources().getString(R.string.openloop));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e(TAG,"Create new thread");
        loopThread = new LoopThread();
        loopThread.start();
    }

    public void micLoopBack() {
        //这两句话的作用是设置扬声器
        AudioManager service = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
        if(service != null) {
            service.setSpeakerphoneOn(true);
        }

        int SAMPLE_RATE = 8000;
        int BUF_SIZE = 1024;

        //计算缓冲区尺寸
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        bufferSize = Math.max(bufferSize,
                AudioTrack.getMinBufferSize(SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
        bufferSize = Math.max(bufferSize, BUF_SIZE);
        byte[] buffer = new byte[bufferSize];

        //创建音频采集设备，输入源是麦克风
        //AudioSource.CAMCORDER
        m_record = new AudioRecord(MediaRecorder.AudioSource.CAMCORDER,
                SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);


        //创建音频播放设备
        m_track = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM);

        m_track.setPlaybackRate(SAMPLE_RATE);

        //一边采集，一边播放
        m_record.startRecording();
        m_track.play();

        //需要停止的时候，把mRunning置为false即可。
        Log.e(TAG, "mRunning value is " + mRunning);
        while (!mRunning) {
            int readSize = m_record.read(buffer, 0, bufferSize);

            /*
            for (int i = 0; i < buffer.length; i++) {
                // 这里没有做运算的优化，为了更加清晰的展示代码
                readSize += buffer[i] * buffer[i];
            }
            */

            if (readSize > 0)
                m_track.write(buffer, 0, readSize);
        }

        mRunning = !mRunning;

        Log.e(TAG, "finish the thread");
    }

    public void setCancel() {
        mRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(m_record != null) {
            m_record.release();
            m_record = null;
        }

        if(m_track != null) {
            m_track.release();
            m_track = null;
        }

        if(loopThread != null) {
            loopThread = null;
        }
    }

    @Override
    public void onKeyUp(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_MENU ||
                keyCode == KeyEvent.KEYCODE_DPAD_CENTER){

            setCancel();
            mActivity.remove(this);
        }
    }

    class LoopThread extends Thread {
        @Override
        public void run() {
            super.run();

            micLoopBack();
        }
    }
}
