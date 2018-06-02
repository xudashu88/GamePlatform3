package com.chuangku.gameplatform3.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.sevenheaven.gesturelock.GestureLockView;

/**
 * 继承GestureLockView来实现自定义样式的block
 * Created by yucaihua on 2018/5/15.
 */
public class NexusStyleLockView extends GestureLockView {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mCenterX, mCenterY;
    private int mWidth, mHeight;
    private int mRadius;

    private static final int COLOR_NORMAL = 0xFFFFFFFF;
    private static final int COLOR_ERROR = 0xFFFF0000;

    private float innerRate = 0.2F;
    private float outerWidthRate = 0.15F;
    private float outerRate = 0.91F;

    private float arrowRate = 0.3F;
    private float arrowDistanceRate = 0.65F;
    private int arrowDistance;

    private Path arrow;

    public NexusStyleLockView(Context context) {
        this(context, null);
    }

    public NexusStyleLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NexusStyleLockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        arrow = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        mCenterX = w / 2;
        mCenterY = h / 2;

        mRadius = w > h ? h : w;
        mRadius /= 4;

        arrowDistance = (int) (mRadius * arrowDistanceRate);

        int length = (int) (mRadius * arrowRate);
        arrow.reset();
        arrow.moveTo(mCenterX - length, mCenterY + length - arrowDistance);
        arrow.lineTo(mCenterX, mCenterY - arrowDistance);
        arrow.lineTo(mCenterX + length, mCenterY + length - arrowDistance);
        arrow.close();
    }

    /**
     * 绘制箭头(箭头角度0的时候为向上)
     * @param canvas
     */
    @Override
    protected void doArrowDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(COLOR_ERROR);
        canvas.drawPath(arrow, mPaint);
    }

    /**
     * 实现Block内容样式的绘制
     * @param state
     * @param canvas
     */
    @Override
    protected void doDraw(LockerState state, Canvas canvas) {
        switch (state) {
            case LOCKER_STATE_NORMAL: //正常状态
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(COLOR_NORMAL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * innerRate, mPaint);
                break;
            case LOCKER_STATE_SELECTED: //选中状态
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(COLOR_NORMAL);
                mPaint.setStrokeWidth(mRadius * outerWidthRate);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * outerRate, mPaint);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * innerRate, mPaint);
                break;
            case LOCKER_STATE_ERROR: //错误状态
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(COLOR_ERROR);
                mPaint.setStrokeWidth(mRadius * outerWidthRate);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * outerRate, mPaint);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * innerRate, mPaint);
                break;
        }
    }
}
