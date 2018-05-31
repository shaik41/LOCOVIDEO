package com.task.locovideotask.ui.videoutils;

import android.util.DisplayMetrics;


import com.task.locovideotask.model.ViewData;

import java.util.Random;

/**
 * Created by shaikatif on 5/29/18.
 */

public class VideoUtils {

    public static ViewData adjustAspectRatio(int proposedWidth, int proposedHeight, int videoWidth, int videoHeight) {

        float ratio_width = proposedWidth/videoWidth;
        float ratio_height = proposedHeight/videoHeight;
        float aspect_ratio = videoWidth/videoHeight;
        ViewData viewData=new ViewData();
        if (ratio_width > ratio_height){
            viewData.setWidth((int) (proposedHeight * aspect_ratio));
            viewData.setHeight(proposedHeight);
        }else{
            viewData.setWidth(proposedWidth);
            viewData.setHeight((int) (proposedWidth/aspect_ratio));
        }

        return viewData;
    }

    public static int getPixelsFromDPI(int size, DisplayMetrics displayMetrics){
        return (size * displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
