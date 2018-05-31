package com.task.locovideotask.ui.videoutils;

import android.util.DisplayMetrics;



import java.util.Random;

/**
 * Created by shaikatif on 5/29/18.
 */

public class VideoUtils {


    public static int getPixelsFromDPI(int size, DisplayMetrics displayMetrics){
        return (size * displayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    public static int randomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
