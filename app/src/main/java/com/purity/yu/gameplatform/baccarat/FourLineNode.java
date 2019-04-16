package com.purity.yu.gameplatform.baccarat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gangbeng.basemodule.utils.LogUtil;
import com.purity.yu.gameplatform.widget.DpUtil;

import java.util.List;

/**
 * 小强路-蟑螂路
 * Created by yanghaozhang on 2018/7/4.
 */
public class FourLineNode implements NodeImp {

    private int mX ;

    private int mY ;

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
        paint.setStrokeWidth(mLineHeight);
        float lineSub = (float) (mLineHeight / Math.sqrt(2));
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

    public void setX(int x) {
        this.mX = x;
    }

    public void setY(int y) {
        this.mY = y;
    }

}
