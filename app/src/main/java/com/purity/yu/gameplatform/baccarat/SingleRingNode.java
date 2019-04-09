package com.purity.yu.gameplatform.baccarat;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.purity.yu.gameplatform.widget.DpUtil;

/**
 * 大路
 * Created by yanghaozhang on 2018/7/3.
 */
public class SingleRingNode implements NodeImp {

    private int mX = 0;

    private int mY = 0;

    private float mRingHeight = DpUtil.dp2px(1);
//    private float mRingHeight = 1;

    private float mLineHeight = -100;

    private boolean mNeedSlash = false;


    private int mColor = Color.BLUE;

    private int mSlashColor = Color.GREEN;
    private int mTextColor = Color.BLACK;

    private String mText = "";

    public SingleRingNode() {
    }

    /**
     * 大路
     *
     * @param mX         x轴
     * @param mY         y轴
     * @param mRingColor 圆环颜色
     * @param mNeedSlash 是否有斜线
     * @param mText      文本
     */
    public SingleRingNode(int mX, int mY,/*float mRingHeight,*/int mRingColor, boolean mNeedSlash, String mText) {
        this.mX = mX;
        this.mY = mY;
//        this.mRingHeight = mRingHeight;
        this.mColor = mRingColor;
        this.mNeedSlash = mNeedSlash;
        this.mText = mText;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, float left, float top, float right, float bottom) {
        float nodeW = Math.abs(right - left) / 2;
        float lineWidth = (float) ((1 - 1 / Math.sqrt(2)) * nodeW * 3 / 4);
        paint.setStrokeWidth(mRingHeight);
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(new RectF(left + mRingHeight / 2,
                top + mRingHeight / 2,
                right - mRingHeight / 2,
                bottom - mRingHeight / 2), 0, 360, false, paint);

        if (mNeedSlash) {
            //设置斜线两点为斜边与圆环的中点距离
            paint.setColor(mSlashColor);
            paint.setStrokeWidth(mLineHeight);
            canvas.drawLine(left + lineWidth,
                    bottom - lineWidth,
                    right - lineWidth,
                    top + lineWidth,
                    paint);
        }
        //居中文字
        String line = String.valueOf(lineWidth);
        int _line = Integer.parseInt(line.split("\\.")[0]);
        paint.setTextSize(_line*8);
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(false);//非粗体
        paint.setStrokeWidth(0.6f);
        paint.setColor(mTextColor);
        float centerX = (right + left) / 2;
        float centerY = (bottom + top) / 2;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float nameTop = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float nameBottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (centerY - nameTop / 2 - nameBottom / 2);//基线中间点的y轴计算公式
        canvas.drawText(mText,
                centerX,
                baseLineY,
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

    public void setRingHeight(float ringHeight) {
        this.mRingHeight = ringHeight;
    }

    public void setLineHeight(float lineHeight) {
        this.mLineHeight = lineHeight;
    }

    public void setNeedSlash(boolean needSlash) {
        this.mNeedSlash = needSlash;
    }

    public void setSlashColor(int slashColor) {
        this.mSlashColor = slashColor;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }
}
