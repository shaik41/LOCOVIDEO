package com.task.locovideotask.ui.mainvideo;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.task.locovideotask.R;


import java.io.IOException;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainVideoActivity extends AppCompatActivity implements  MediaPlayer.OnPreparedListener, TextureView.SurfaceTextureListener {

    @BindView(R.id.tv_main_video_content)
    TextureView videoView;

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
        videoView.setSurfaceTextureListener(this);
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
             toggleVideo();
            }
        },5000);

    }

    private void toggleVideo() {

    }


    private void releaseVideo(){
        if(videoPlayer!=null){
            videoPlayer.release();
            videoPlayer=null;
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Surface videoSurface=new Surface(surfaceTexture);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        videoPlayer = new MediaPlayer();
        videoPlayer.setSurface(videoSurface);
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
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }
}

