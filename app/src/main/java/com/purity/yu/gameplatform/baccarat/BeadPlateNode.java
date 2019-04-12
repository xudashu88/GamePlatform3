package com.purity.yu.gameplatform.baccarat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * 珠盘路
 * Created by yanghaozhang on 2018/7/3.
 */
public class BeadPlateNode implements NodeImp {

    private int mX = 0;

    private int mY = 0;

    private String mText = "";

    private int mColor = Color.WHITE;

    private int mFontBgColor = Color.BLUE;

    private boolean mRedLeftTop = false;
    private boolean mBlueRightBottom = false;

    private float mFontSize = -100;

    private float mCircleSub = 0;

    private int mLeftTopColor = Color.RED;
    private int mRightBottomColor = Color.BLUE;

    private int mWhiteColor = Color.WHITE;
    private Context mContext;
    public static Typeface typeface1;

    public BeadPlateNode() {
    }

    /**
     * 珠盘路
     *
     * @param x                x轴
     * @param y                y轴
     * @param text             庄、闲、和
     * @param fontBgColor      字体背景颜色
     * @param mRedLeftTop      左上角红点
     * @param mBlueRightBottom 右下角蓝点
     */
    public BeadPlateNode(int x, int y, String text, int fontBgColor, boolean mRedLeftTop, boolean mBlueRightBottom) {
        this.mX = x;
        this.mY = y;
        this.mText = text;
        this.mFontBgColor = fontBgColor;
        this.mRedLeftTop = mRedLeftTop;
        this.mBlueRightBottom = mBlueRightBottom;
    }

    public BeadPlateNode(int x, int y, String text, int fontBgColor, boolean mRedLeftTop, boolean mBlueRightBottom, Context mContext) {
        this.mX = x;
        this.mY = y;
        this.mText = text;
        this.mFontBgColor = fontBgColor;
        this.mRedLeftTop = mRedLeftTop;
        this.mBlueRightBottom = mBlueRightBottom;
        this.mContext = mContext;
        typeface1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/fontzipMin.ttf");
    }

    @Override
    public void draw(Canvas canvas, Paint paint, float left, float top, float right, float bottom) {
        paint.setTypeface(typeface1);
        paint.setFakeBoldText(true);
        paint.setColor(mFontBgColor);
        paint.setStyle(Paint.Style.FILL);
        if (mFontSize < 0) {
            mFontSize = (right - left) * 0.75f;
        }
        paint.setTextSize(mFontSize);
        float centerX = (right + left) / 2;
        float centerY = (bottom + top) / 2;

        canvas.drawCircle(centerX, centerY, Math.abs(right - left) / 2 /*- mCircleSub*/, paint);

        paint.setColor(mColor);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float nameTop = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float nameBottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        //https://blog.csdn.net/zly921112/article/details/50401976
        int baseLineY = (int) (centerY - nameTop / 2 - nameBottom / 2);//基线中间点的y轴计算公式
        canvas.drawText(mText,
                centerX,
                baseLineY,
                paint);


        //左上角红
        Paint _outCirclePaint = new Paint();
        _outCirclePaint.setStyle(Paint.Style.STROKE);
        _outCirclePaint.setStrokeWidth(3);
        _outCirclePaint.setColor(mWhiteColor);
        float minWidth = Math.abs(right - left) / 5 - 1;
        float _left, _right, _top, _bottom;
        _right = centerX;
        _bottom = centerY;

        Paint _inCirclePaint = new Paint();
        _inCirclePaint.setStyle(Paint.Style.FILL);
        _inCirclePaint.setColor(mLeftTopColor);
        float _inWidth = Math.abs(right - left) / 5 - 3;

        if (mRedLeftTop) {
            canvas.drawCircle((left + _right) / 2, (top + _bottom) / 2, minWidth, _outCirclePaint);
            canvas.drawCircle((left + _right) / 2, (top + _bottom) / 2, _inWidth, _inCirclePaint);
        }

        //右下角蓝
        _left = centerX;
        _top = centerY;
        _inCirclePaint.setColor(mRightBottomColor);
        if (mBlueRightBottom) {
            canvas.drawCircle((_left + right) / 2, (_top + bottom) / 2, minWidth, _outCirclePaint);
            canvas.drawCircle((_left + right) / 2, (_top + bottom) / 2, _inWidth, _inCirclePaint);
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
        return mColor;
    }

    public void setmX(int x) {
        this.mX = x;
    }

    public void setmY(int y) {
        this.mY = y;
    }

    public void setmText(String text) {
        this.mText = text;
    }

    public void setmColor(int color) {
        this.mColor = color;
    }

    public void setmFontBgColor(int fontBgColor) {
        this.mFontBgColor = fontBgColor;
    }

    public void setmFontSize(float mFontSize) {
        this.mFontSize = mFontSize;
    }

    public void setCircleSub(float circleSub) {
        this.mCircleSub = circleSub;
    }
}
