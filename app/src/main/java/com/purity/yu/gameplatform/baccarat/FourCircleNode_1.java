package com.purity.yu.gameplatform.baccarat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by yanghaozhang on 2018/7/4.
 */
public class FourCircleNode_1 implements NodeImp {
    private int mX = 0;

    private int mY = 0;

    private int[] mRings = {0};

    private int mLeftColor = Color.RED;

    private int mRightColor = Color.BLUE;

    public FourCircleNode_1() {
    }

    public FourCircleNode_1(int x,
                            int y,
                            int leftColor,
                            int rightColor,
                            int... rings) {
        this.mX = x;
        this.mY = y;
        this.mRings = rings;
        this.mLeftColor = leftColor;
        this.mRightColor = rightColor;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, float left, float top, float right, float bottom) {
        paint.setStyle(Paint.Style.FILL);

        float minWidth = Math.abs(right - left) / 4;

        float centerX = (right + left) / 2;
        float centerY = (bottom + top) / 2;

        for (int ring : mRings) {
            float ringLeft = left;
            float ringTop = top;
            float ringRight = right;
            float ringBottom = bottom;
            switch (ring) {
                case 3:
                    paint.setColor(mRightColor);
                    ringLeft = centerX;
                    ringTop = centerY;
                    break;
                case 2:
                    paint.setColor(mLeftColor);
                    ringTop = centerY;
                    ringRight = centerX;
                    break;
                case 1:
                    paint.setColor(mRightColor);
                    ringLeft = centerX;
                    ringBottom = centerY;
                    break;
                case 0:
                default:
                    paint.setColor(mLeftColor);
                    ringRight = centerX;
                    ringBottom = centerY;
                    break;
            }
            canvas.drawCircle((ringRight + ringLeft) / 2,
                              (ringTop + ringBottom) / 2,
                              minWidth,
                              paint);
        }
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
        return 0;
    }

    public void setX(int x) {
        this.mX = x;
    }

    public void setY(int y) {
        this.mY = y;
    }

    public void setRings(int[] rings) {
        this.mRings = rings;
    }

    public void setLeftColor(int leftColor) {
        this.mLeftColor = leftColor;
    }

    public void setRightColor(int rightColor) {
        this.mRightColor = rightColor;
    }
}
