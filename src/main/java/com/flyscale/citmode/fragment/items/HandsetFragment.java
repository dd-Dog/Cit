package com.flyscale.citmode.fragment.items;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyscale.citmode.R;
import com.flyscale.citmode.fragment.BaseFragment;

/**
 * Created by bian on 2018/3/6.
 */

public class HandsetFragment extends BaseFragment {


    private AudioRecord m_record;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    public View initView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.fragment_versioninfo, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(getResources().getStringArray(R.array.test_list)[5]);
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText(getResources().getString(R.string.openloop));

        new Thread() {
            @Override
            public void run() {
                super.run();
                micLoopBack();
            }
        }.start();
        return view;
    }

    boolean mRunning = true;

    public void micLoopBack() {
        //这两句话的作用是打开设备扬声器
        AudioManager service = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
        service.setSpeakerphoneOn(true);

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
        m_record = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        //创建音频播放设备
        AudioTrack m_track = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
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
        while (mRunning) {
            int readSize = m_record.read(buffer, 0, bufferSize);
            if (readSize > 0)
                m_track.write(buffer, 0, readSize);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRunning = false;
        m_record.release();
        m_record = null;
    }
    @Override
    public void onResume() {
        super.onResume();
        mActivity.setCurrentFragment(this);
    }
    @Override
    public void onKeyUp(int keyCode) {
        switch (keyCode){

        }
    }
}
