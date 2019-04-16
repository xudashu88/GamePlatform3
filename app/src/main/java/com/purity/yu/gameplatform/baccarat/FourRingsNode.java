package com.purity.yu.gameplatform.baccarat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.gangbeng.basemodule.utils.LogUtil;
import com.purity.yu.gameplatform.widget.DpUtil;

import java.util.List;

/**
 * 大眼路
 * Created by yanghaozhang on 2018/7/4.
 */
public class FourRingsNode implements NodeImp {
    private int mX;

    private int mY ;

    private int mColor = Color.RED;

    private float mRingHeight = DpUtil.dp2px(1);

    public FourRingsNode() {
    }

    public FourRingsNode(int x,
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

        paint.setStrokeWidth(mRingHeight);
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(new RectF(left + mRingHeight / 2,
                top + mRingHeight / 2,
                right - mRingHeight / 2,
                bottom - mRingHeight / 2), 0, 360, false, paint);
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

    @Override
    public boolean isSpecialNode(List<NodeImp> nodeList) {
        for (NodeImp nodeImp : nodeList) {
            if (nodeImp.getX() > mX) {
                return false;
            } else if (nodeImp.getX() == mX && nodeImp.getY() > mY) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void drawSpecial(Canvas canvas, Paint paint, float left, float top, float right, float bottom) {

    }
}
