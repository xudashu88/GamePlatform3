package com.purity.yu.gameplatform.baccarat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.purity.yu.gameplatform.widget.DpUtil;

/**
 * 小强路-蟑螂路
 * Created by yanghaozhang on 2018/7/4.
 */
public class FourLineNode implements NodeImp {

    private int mX = 0;

    private int mY = 0;

    private int mColor = Color.RED;

    private float mLineHeight = DpUtil.dp2px(1);

    public FourLineNode() {
    }

    public FourLineNode(int x,
                        int y,
                        int color) {
        this.mX = x;
        this.mY = y;
        this.mColor = color;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, float left, float top, float right, float bottom) {
        paint.setStyle(Paint.Style.STROKE);

//        if (mLineHeight < 0) {
//            mLineHeight = DpUtil.dp2px(2);
//        }
        paint.setStrokeWidth(mLineHeight);
        float lineSub = (float) (mLineHeight / Math.sqrt(2));

        float centerX = (right + left) / 2;
        float centerY = (bottom + top) / 2;
        paint.setColor(mColor);
        canvas.drawLine(left + lineSub,
                bottom - lineSub,
                right - lineSub,
                top + lineSub,
                paint);
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

    public void setX(int x) {
        this.mX = x;
    }

    public void setY(int y) {
        this.mY = y;
    }

    public void setLineHeight(float lineHeight) {
        this.mLineHeight = lineHeight;
    }
}
