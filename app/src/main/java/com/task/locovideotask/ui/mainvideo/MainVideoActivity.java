package com.task.locovideotask.ui.mainvideo;

import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.task.locovideotask.R;


import java.io.IOException;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainVideoActivity extends AppCompatActivity implements  MediaPlayer.OnPreparedListener, TextureView.SurfaceTextureListener {

    @BindView(R.id.tv_main_video_content)
    TextureView videoView;

    @BindView(R.id.cv_view_parent)
    ConstraintLayout viewParent;

    private MediaPlayer videoPlayer;

    private boolean isFullScreen=false;

    private String VIDEO_URL = "http://www.exit109.com/~dnn/clips/RW20seconds_2.mp4";
    private String LOG_TAG = "MAIN_VIDEO_SCREEN";
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);
        ButterKnife.bind(this);
        initializeVideo();
        getDisplayMatrix();
    }

    private void getDisplayMatrix() {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
        toggleVideo();
    }

    private void toggleVideo() {
        if(isFullScreen){
            isFullScreen=false;
            setCircleConstraint();
        }else{
            isFullScreen=true;
            setFullScreenConstraint();
        }

        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleVideo();
            }
        },5000);


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

    private void setCircleConstraint(){
        findViewById(R.id.custom_view).setVisibility(View.VISIBLE);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(viewParent);
        ViewData viewData=adjustAspectRatio(150,150);
        constraintSet.constrainHeight(R.id.tv_main_video_content,getDPI(viewData.getHeight()));
        constraintSet.constrainWidth(R.id.tv_main_video_content,getDPI(viewData.getWidth()));
        constraintSet.connect(R.id.tv_main_video_content,ConstraintSet.END,R.id.cv_view_parent,ConstraintSet.END,0);
        constraintSet.connect(R.id.tv_main_video_content,ConstraintSet.START,R.id.cv_view_parent,ConstraintSet.START,0);
        constraintSet.connect(R.id.tv_main_video_content,ConstraintSet.BOTTOM,R.id.rv_question_card,ConstraintSet.TOP,0);
        constraintSet.connect(R.id.tv_main_video_content,ConstraintSet.TOP,R.id.rv_question_card,ConstraintSet.TOP,0);
        TransitionManager.beginDelayedTransition(viewParent);
        constraintSet.applyTo(viewParent);

        //adjustAspectRatio(100,100);
        //return constraintSet;

    }

    private void setFullScreenConstraint(){
        findViewById(R.id.custom_view).setVisibility(View.GONE);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(viewParent);
        constraintSet.constrainHeight(R.id.tv_main_video_content,0);
        constraintSet.constrainWidth(R.id.tv_main_video_content,0);
        constraintSet.connect(R.id.tv_main_video_content,ConstraintSet.END,R.id.cv_view_parent,ConstraintSet.END,0);
        constraintSet.connect(R.id.tv_main_video_content,ConstraintSet.START,R.id.cv_view_parent,ConstraintSet.START,0);
        constraintSet.connect(R.id.tv_main_video_content,ConstraintSet.BOTTOM,R.id.cv_view_parent,ConstraintSet.BOTTOM,0);
        constraintSet.connect(R.id.tv_main_video_content,ConstraintSet.TOP,R.id.cv_view_parent,ConstraintSet.TOP,0);
        TransitionManager.beginDelayedTransition(viewParent);
        constraintSet.applyTo(viewParent);
        //adjustAspectRatio(720,1280);
        //return constraintSet;



    }

    public int getDPI(int size){
        return (size * displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    private ViewData adjustAspectRatio(int proposedWidth, int proposedHeight) {


        float video_Width = videoPlayer.getVideoWidth();
        float video_Height = videoPlayer.getVideoHeight();

        float ratio_width = proposedWidth/video_Width;
        float ratio_height = proposedHeight/video_Height;
        float aspectratio = video_Width/video_Height;
        ViewData viewData=new ViewData();

        if (ratio_width > ratio_height){

            viewData.setWidth((int) (proposedHeight * aspectratio));
            viewData.setHeight(proposedHeight);
        }else{
            viewData.setWidth(proposedWidth);
            viewData.setHeight((int) (proposedWidth/aspectratio));
          }

          return viewData;


    }

    class ViewData{
        private int width;
        private int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}


