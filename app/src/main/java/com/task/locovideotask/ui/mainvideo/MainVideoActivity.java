package com.task.locovideotask.ui.mainvideo;

import android.animation.Animator;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;

import com.task.locovideotask.R;
import com.task.locovideotask.ui.videoutils.RoundedCornerLayout;
import com.task.locovideotask.ui.videoutils.VideoUtils;


import java.io.IOException;
import java.util.Random;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainVideoActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, TextureView.SurfaceTextureListener, MediaPlayer.OnErrorListener {

    @BindView(R.id.tv_main_video_content)
    TextureView videoView;

    @BindView(R.id.cv_view_parent)
    ConstraintLayout viewParent;

    @BindView(R.id.ff_main_video_content)
    RoundedCornerLayout roundedView;

    private MediaPlayer videoPlayer;

    private boolean isFullScreen = false;

    private String VIDEO_URL = "https://files.fm/thumb_video/vjzw6gph.mp4";
    //private String VIDEO_URL = "https://r3---sn-a5mlrn7y.googlevideo.com/videoplayback?itag=18&pl=21&mime=video%2Fmp4&c=WEB&gir=yes&ratebypass=yes&requiressl=yes&ei=rI0NW9q4DJacz7sP67utwAM&lmt=1517078405376929&ip=115.99.112.227&key=cms1&expire=1527636492&dur=702.357&clen=25091730&id=o-AAFWrv3zLS9LE2aYhGaXvsLc5D9vCx9HdbFdJ-eCFk14&source=youtube&fvip=3&sparams=clen,dur,ei,expire,gir,id,initcwndbps,ip,ipbits,itag,lmt,mime,mm,mn,ms,mv,pl,ratebypass,requiressl,source&ipbits=0&signature=4EF9F8250680588CF98CECC67F320DCBCE2EF0FB.258B5392E47837885369BF7A01AA99052E1D05DE&cm2rm=sn-i5uif5t-cagl7s,sn-h55ez7s&req_id=4eda5be50dd0a3ee&redirect_counter=2&cms_redirect=yes&mm=34&mn=sn-a5mlrn7y&ms=ltu&mt=1527614929&mv=m";

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
        videoView.setSurfaceTextureListener(MainVideoActivity.this);
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        toggleVideo();
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

        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleVideo();
            }
        }, 5000);


    }


    private void releaseVideo() {
        if (videoPlayer != null) {
            videoPlayer.release();
            videoPlayer = null;
        }
        if (videoView != null) {
            videoView.requestLayout();
        }

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
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    private void setCircleConstraint() {
        //Set the frame layout to clip the edges and draw the circle canvas.
        roundedView.setToCircle(true);

        updateTextureViewSize(VideoUtils.getPixelsFromDPI(80, displayMetrics), VideoUtils.getPixelsFromDPI(80, displayMetrics));
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(viewParent);
        constraintSet.constrainHeight(R.id.ff_main_video_content, VideoUtils.getPixelsFromDPI(80, displayMetrics));
        constraintSet.constrainHeight(R.id.rv_question_card, VideoUtils.getPixelsFromDPI(randomNumberInRange(300,500), displayMetrics));
        constraintSet.constrainWidth(R.id.ff_main_video_content, VideoUtils.getPixelsFromDPI(80, displayMetrics));
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.END, R.id.cv_view_parent, ConstraintSet.END, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.START, R.id.cv_view_parent, ConstraintSet.START, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.BOTTOM, R.id.rv_question_card, ConstraintSet.TOP, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.TOP, R.id.rv_question_card, ConstraintSet.TOP, 0);

        performCircularReveal();




        constraintSet.applyTo(viewParent);
    }

    private void setFullScreenConstraint() {
        //Set the frame layout to un-clip the edges and remove the circle canvas. (Basically re-create stock frame layout)
        roundedView.setToCircle(false);

        updateTextureViewSize(0, 0);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(viewParent);
        constraintSet.constrainHeight(R.id.ff_main_video_content, 0);
        constraintSet.constrainWidth(R.id.ff_main_video_content, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.END, R.id.cv_view_parent, ConstraintSet.END, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.START, R.id.cv_view_parent, ConstraintSet.START, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.BOTTOM, R.id.cv_view_parent, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(R.id.ff_main_video_content, ConstraintSet.TOP, R.id.cv_view_parent, ConstraintSet.TOP, 0);

        performCircularReveal();

        constraintSet.applyTo(viewParent);

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

            //  crop from center, if we have our character on top we have to re-frame accordingly.
            int pivotPointX = viewWidth / 2;
            int pivotPointY = viewHeight / 3;

            Matrix matrix = new Matrix();
            matrix.setScale(scaleX, scaleY, pivotPointX, pivotPointY);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = viewParent.getWidth() / 2;
            int cy = viewParent.getHeight() / 2;

            float finalRadius = Math.max(viewParent.getWidth(), viewParent.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(viewParent, cx, cy, 0, finalRadius);

            circularReveal.setDuration(500);

            viewParent.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(viewParent);
        } else{
            //TODO
        }
    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}


