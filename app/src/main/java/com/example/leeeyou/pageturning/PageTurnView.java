package com.example.leeeyou.pageturning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leeeyou on 16/5/4.
 * <p/>
 * 翻页自定义view
 */
public class PageTurnView extends View {
    private static final float TEXT_SIZE_NORMAL = 1 / 40F, TEXT_SIZE_LARGER = 1 / 20F;// 标准文字尺寸和大号文字尺寸的占比

    private List<Bitmap> mBitmaps;

    private TextPaint mTextPaint;

    private int mViewWidth, mViewHeight;// 控件宽高

    private float mClipX;//裁剪右端点坐标

    private float mTextSizeNormal, mTextSizeLarger;//标准文字尺寸和大号文字尺寸
    private float autoAreaLeft, autoAreaRight;//控件左右侧自动吸附区域
    private float mCurPointX;//指尖触碰屏幕时点X的坐标值
    private float mMoveValid;//移动事件的有效距离

    private boolean isLastPage = false, isNextPage = true;// 是否该显示下一页、是否最后一页的标识值
    private int pageIndex = 0;// 当前显示mBitmaps数据的下标

    public PageTurnView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
    }

    public synchronized void setBitmaps(List<Bitmap> bitmaps) {
        if (bitmaps == null || bitmaps.size() <= 0) {
            throw new IllegalArgumentException("no bitmap to display");
        }

        if (bitmaps.size() < 2) {
            throw new IllegalArgumentException("please give me more bitmap");
        }

        this.mBitmaps = bitmaps;

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;

        mClipX = w;

        // 计算控件左侧和右侧自动吸附的区域
        autoAreaLeft = mViewWidth * 1 / 5F;
        autoAreaRight = mViewWidth * 4 / 5F;

        mMoveValid = mViewWidth * 1 / 100F;

        mTextSizeNormal = mViewHeight * TEXT_SIZE_NORMAL;
        mTextSizeLarger = mViewHeight * TEXT_SIZE_LARGER;

        // 初始化位图数据
        initBitmaps();
    }

    private void defaultDisplay(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#757A79"));

        mTextPaint.setTextSize(mTextSizeLarger);
        mTextPaint.setColor(Color.parseColor("#BBE9DB"));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("BEAUTIFUL WARNING", mViewWidth / 2, mViewHeight / 4, mTextPaint);

        mTextPaint.setTextSize(mTextSizeNormal);
        mTextPaint.setColor(Color.parseColor("#AECCC6"));
        canvas.drawText("please use setBitmaps method to inject datas", mViewWidth / 2, mViewHeight / 3, mTextPaint);
    }

    private void initBitmaps() {
        if (mBitmaps == null || mBitmaps.size() <= 0) {
            return;
        }

        List<Bitmap> temp = new ArrayList<>(mBitmaps.size());

        for (int i = mBitmaps.size() - 1; i >= 0; i--) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(mBitmaps.get(i), mViewWidth, mViewHeight, true);
            temp.add(scaledBitmap);
        }

        mBitmaps = temp;
    }

    private void drawBitmap(Canvas canvas) {
        // 绘制位图前重置isLasstPage为false
        isLastPage = false;

        // 限制pageIndex的值范围
        pageIndex = pageIndex < 0 ? 0 : pageIndex;
        pageIndex = pageIndex > mBitmaps.size() ? mBitmaps.size() : pageIndex;

        // 计算数据起始位置
        int start = mBitmaps.size() - 2 - pageIndex;
        int end = mBitmaps.size() - pageIndex;

        //如果数据起点位置小于0则表示当前已经到了最后一张图片
        if (start < 0) {
            isLastPage = true;

            Toast.makeText(getContext(), "this is the son of bitch lastest page", Toast.LENGTH_SHORT).show();

            // 强制重置起始位置
            start = 0;
            end = 1;
        }

        for (int i = start; i < end; i++) {
            canvas.save();

            /*
             * 仅裁剪位于最顶层的画布区域
             * 如果到了末页则不在执行裁剪
             */
            if (!isLastPage && i == end - 1) {
                canvas.clipRect(0, 0, mClipX, mViewHeight);
            }

            canvas.drawBitmap(mBitmaps.get(i), 0, 0, null);

            canvas.restore();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmaps == null || mBitmaps.size() <= 0) {
            defaultDisplay(canvas);
        } else {
            drawBitmap(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 每次触发TouchEvent重置isNextPage为true
        isNextPage = true;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // 获取当前事件点x坐标
                mCurPointX = event.getX();

                //如果事件点位于回滚区域
                if (mCurPointX < autoAreaLeft) {//翻上一页
                    isNextPage = false;
                    pageIndex--;
                    mClipX = mCurPointX;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float SlideDis = mCurPointX - event.getX();

                if (Math.abs(SlideDis) > mMoveValid) {
                    // 获取触摸点的x坐标
                    mClipX = event.getX();
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                // 判断是否需要自动滑动
                judgeSlideAuto();

                /*
                 * 如果当前页不是最后一页
                 * 如果是需要翻下一页
                 * 并且上一页已被clip掉
                 */
                if (!isLastPage && isNextPage && mClipX <= 0) {
                    pageIndex++;
                    mClipX = mViewWidth;
                    invalidate();
                }
                break;
        }

        return true;
    }

    private void judgeSlideAuto() {
        //如果裁剪的右端点坐标在控件左端五分之一的区域内，那么我们直接让其自动滑到控件左端
        if (mClipX < autoAreaLeft) {
            while (mClipX > 0) {
                mClipX--;
                invalidate();
            }
        }

        //如果裁剪的右端点坐标在控件右端五分之一的区域内，那么我们直接让其自动滑到控件右端
        if (mClipX > autoAreaRight) {
            while (mClipX < mViewWidth) {
                mClipX++;
                invalidate();
            }
        }
    }

}
