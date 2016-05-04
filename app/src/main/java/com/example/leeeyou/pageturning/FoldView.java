package com.example.leeeyou.pageturning;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by leeeyou on 16/5/4.
 */
public class FoldView extends View {

    private Path mPath;
    private int pointX, pointY;
    private int mViewWidth, mViewHeight;
    private Paint mPaint;


    public FoldView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                pointX = (int) event.getX();
                pointY = (int) event.getY();
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();

        canvas.drawColor(Color.BLUE);


        float mk = mViewWidth - pointX;
        float ml = mViewHeight - pointY;

        float temp = (float) (Math.pow(ml, 2) + Math.pow(mk, 2));

        float sizeShort = temp / (2F * mk);
        float sizeLong = temp / (2F * ml);

        mPath.moveTo(pointX, pointY);
        mPath.lineTo(mViewWidth, mViewHeight - sizeLong);
        mPath.lineTo(mViewWidth - sizeShort, mViewHeight);
        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

}
