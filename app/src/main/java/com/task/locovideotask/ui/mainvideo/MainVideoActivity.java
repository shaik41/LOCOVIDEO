package com.task.locovideotask.ui.mainvideo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.task.locovideotask.R;


import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainVideoActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

    @BindView(R.id.sv_main_video_content)
    SurfaceView videoView;

    private SurfaceHolder videoViewHolder;
    private MediaPlayer videoPlayer;

    private String VIDEO_URL = "http://www.exit109.com/~dnn/clips/RW20seconds_2.mp4";
    private String LOG_TAG = "MAIN_VIDEO_SCREEN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);
        ButterKnife.bind(this);
        initializeVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseVideo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseVideo();
    }

    private void initializeVideo() {
        videoViewHolder= videoView.getHolder();
        videoViewHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        videoPlayer = new MediaPlayer();
        videoPlayer.setDisplay(videoViewHolder);
        try {
            videoPlayer.setDataSource(VIDEO_URL);
            videoPlayer.prepare();
            videoPlayer.setOnPreparedListener(this);
            videoPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }catch (IOException exception){
            Log.d(LOG_TAG,getString(R.string.error_video_player) + exception.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //NOOP
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //NOOP
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    private void releaseVideo(){
        if(videoPlayer!=null){
            videoPlayer.release();
            videoPlayer=null;
        }
    }
}

