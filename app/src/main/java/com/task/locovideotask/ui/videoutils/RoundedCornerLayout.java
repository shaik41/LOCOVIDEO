package com.task.locovideotask.ui.videoutils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RoundedCornerLayout extends FrameLayout {


    int borderColor;
    Paint paint;
    private Path path = new Path();
    private boolean setToCircle = false;


    public RoundedCornerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            borderColor = android.graphics.Color.rgb(128, 0, 129);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(borderColor);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(8);
            paint.setStyle(Paint.Style.STROKE);
            paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        }


    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (setToCircle && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // compute the path
            float halfWidth = w / 2f;
            float halfHeight = h / 2f;
            float centerX = halfWidth;
            float centerY = halfHeight;
            path.reset();

            path.addCircle(centerX, centerY, Math.min(halfWidth, halfHeight), Path.Direction.CW);
            path.close();
        }

    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        int save;
        if (setToCircle && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            save = canvas.save();
            canvas.clipPath(path);
            super.dispatchDraw(canvas);
            canvas.drawPath(path, paint);
            canvas.restoreToCount(save);
        } else {
            super.dispatchDraw(canvas);
        }

    }


    public void setToCircle(boolean setToCircle) {
        this.setToCircle = setToCircle;
    }



}