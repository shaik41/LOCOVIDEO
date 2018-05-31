package com.task.locovideotask.ui.mainvideo;

import android.animation.Animator;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;

import com.task.locovideotask.R;
import com.task.locovideotask.ui.videoutils.RoundedCornerLayout;
import com.task.locovideotask.ui.videoutils.VideoUtils;


import java.io.IOException;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainVideoActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, TextureView.SurfaceTextureListener, MediaPlayer.OnErrorListener {

    @BindView(R.id.tv_main_video_content)
    TextureView videoView;

    @BindView(R.id.cv_view_parent)
    ConstraintLayout videoParent;

    @BindView(R.id.ff_main_video_content)
    RoundedCornerLayout roundedView;

    private MediaPlayer videoPlayer;

    private boolean isFullScreen = true;

    private String VIDEO_URL = "https://files.fm/thumb_video/vjzw6gph.mp4";

    private String LOG_TAG = "MAIN_VIDEO_SCREEN";

    private DisplayMetrics displayMetrics;

    private Handler toggleHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_video);
        ButterKnife.bind(this);
        getDisplayMatrix();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeVideo();
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
        videoView.setSurfaceTextureListener(MainVideoActivity.this);
    }

    private void getDisplayMatrix() {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }



    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Surface videoSurface = new Surface(surfaceTexture);
        videoPlayer = new MediaPlayer();
        videoPlayer.setSurface(videoSurface);
        try {
            videoPlayer.setDataSource(VIDEO_URL);
            videoPlayer.prepare();
            videoPlayer.setOnPreparedListener(this);
            videoPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            videoPlayer.setOnErrorListener(this);
        } catch (IOException exception) {
            Log.d(LOG_TAG, getString(R.string.error_video_player) + exception.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        releaseVideo();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        if(toggleHandler ==null){
            toggleHandler =new Handler();
            toggleHandler.postDelayed(toggleRunnable,5000);
        }
    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        switch (what){
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d(LOG_TAG,"Unsupported codec or size for the device.");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d(LOG_TAG,"The stream url is unavailable or not reachable.");
                break;
        }
        return false;
    }


    private void toggleVideo() {
        if (videoPlayer == null) {
            return;
        }

        if (isFullScreen) {
            setCircleConstraint();
        } else {
            setFullScreenConstraint();
        }

        isFullScreen = !isFullScreen;

        if(toggleHandler !=null) {
            toggleHandler.postDelayed(toggleRunnable, 5000);
        }

    }

    private Runnable toggleRunnable=new Runnable() {
        @Override
        public void run() {
            toggleVideo();
        }
    };

    private void setCircleConstraint() {
        //Set the frame layout to clip the edges and draw the circle canvas.
        roundedView.setToCircle(true);

        updateTextureViewSize(VideoUtils.getPixelsFromDPI(80, displayMetrics), VideoUtils.getPixelsFromDPI(80, displayMetrics));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(videoParent);
        constraintSet.constrainHeight(R.id.ff_main_video_content, VideoUtils.getPixelsFromDPI(80, displayMetrics));
        constraintSet.constrainHeight(R.id.rv_question_card, VideoUtils.getPixelsFromDPI(VideoUtils.randomNumberInRange(300,500), displayMetrics));
        constraintSet.constrainWidth(R.id.ff_main_video_content, VideoUtils.getPixelsFromDPI(80, displayMetrics));
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.END, R.id.cv_view_parent, ConstraintSet.END, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.START, R.id.cv_view_parent, ConstraintSet.START, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.BOTTOM, R.id.rv_question_card, ConstraintSet.TOP, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.TOP, R.id.rv_question_card, ConstraintSet.TOP, 0);

        performCircularReveal();

        constraintSet.applyTo(videoParent);
    }

    private void setFullScreenConstraint() {
        //Set the frame layout to un-clip the edges and remove the circle canvas. (Basically re-create stock frame layout)
        roundedView.setToCircle(false);

        updateTextureViewSize(0, 0);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(videoParent);
        constraintSet.constrainHeight(R.id.ff_main_video_content, 0);
        constraintSet.constrainWidth(R.id.ff_main_video_content, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.END, R.id.cv_view_parent, ConstraintSet.END, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.START, R.id.cv_view_parent, ConstraintSet.START, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.BOTTOM, R.id.cv_view_parent, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.TOP, R.id.cv_view_parent, ConstraintSet.TOP, 0);

        performCircularReveal();

        constraintSet.applyTo(videoParent);

    }


    private void updateTextureViewSize(int viewWidth, int viewHeight) {
        if (viewHeight != 0) {
            float scaleX = 1.0f;
            float scaleY = 1.0f;
            int mVideoWidth = videoPlayer.getVideoWidth();
            int mVideoHeight = videoPlayer.getVideoHeight();

            try {
                if (mVideoWidth > viewWidth && mVideoHeight > viewHeight) {
                    scaleX = (float) mVideoWidth / viewWidth;
                    scaleY = (float) mVideoHeight / viewHeight;
                } else if (mVideoWidth < viewWidth && mVideoHeight < viewHeight) {
                    scaleY = (float) viewWidth / mVideoWidth;
                    scaleX = (float) viewHeight / mVideoHeight;
                } else if (viewWidth > mVideoWidth) {
                    scaleY = (viewWidth / mVideoWidth) / (viewHeight / mVideoHeight);
                } else if (viewHeight > mVideoHeight) {
                    scaleX = (viewHeight / mVideoHeight) / (viewWidth / mVideoWidth);
                }
            }catch(ArithmeticException ar){
                Log.d(LOG_TAG,ar.getMessage());
            }

            //  crop from  center, if we have our character on top we have to re-frame accordingly.
            int pivotPointX = viewWidth / 2;
            int pivotPointY = viewHeight / 2;

            Matrix matrix = new Matrix();
            matrix.setScale(scaleX/2, scaleY/2, pivotPointX, pivotPointY);

            videoView.setTransform(matrix);
            videoView.setLayoutParams(new FrameLayout.LayoutParams(viewWidth, viewHeight));
        } else {
            Matrix matrix = new Matrix();
            matrix.setScale(1, 1, 0, 0);

            videoView.setTransform(matrix);
            videoView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
    }


    private void performCircularReveal() {
        int cx = videoParent.getWidth() / 2;
        int cy = videoParent.getHeight() / 2;

        float finalRadius = Math.max(videoParent.getWidth(), videoParent.getHeight());

        Animator circularReveal;
        //Use default circular reveal if 20+ else use the custom from codetail.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularReveal = ViewAnimationUtils.createCircularReveal(videoParent, cx, cy, 0, finalRadius);
        } else {
            circularReveal = io.codetail.animation.ViewAnimationUtils.createCircularReveal(videoParent, cx, cy, 0, finalRadius);
        }
        circularReveal.setDuration(500);
        circularReveal.start();
    }

    private void releaseVideo() {
        if (videoPlayer != null) {
            videoPlayer.release();
            videoPlayer = null;
        }

        if(toggleHandler !=null){
            toggleHandler.removeCallbacks(toggleRunnable);
            toggleHandler =null;
        }

    }







}


