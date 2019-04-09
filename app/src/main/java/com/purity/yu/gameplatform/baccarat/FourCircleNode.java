package com.purity.yu.gameplatform.baccarat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.purity.yu.gameplatform.widget.DpUtil;

/**
 * 小路
 * Created by yanghaozhang on 2018/7/4.
 */
public class FourCircleNode implements NodeImp {
    private int mX = 0;

    private int mY = 0;

    private int mColor = Color.RED;

    private float mRingHeight = DpUtil.dp2px(1);

    public FourCircleNode() {
    }

    public FourCircleNode(int x,
                          int y,
                          int color) {
        this.mX = x;
        this.mY = y;
        this.mColor = color;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, float left, float top, float right, float bottom) {
        if (mRingHeight < 0) {
            mRingHeight = DpUtil.dp2px(6);
        }
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(mColor);
        canvas.drawArc(new RectF(left + mRingHeight / 2,
                top + mRingHeight / 2,
                right - mRingHeight / 2,
                bottom - mRingHeight / 2), 0, 360, true, paint);
    }

    @Override
    public int getX() {
        return mX;
    }

    @Override
    public int getY() {
        return mY;
    }

    @Override
    public int getColor() {
        return mColor;
    }
}
