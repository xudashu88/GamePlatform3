package com.purity.yu.gameplatform.baccarat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.purity.yu.gameplatform.R;

/**
 * 圓圈和居中字
 */
public class TextCircleView extends View {
    /**
     * 圆圈
     * 1.背景颜色
     * 2.半径最小宽度或者最小高度
     * 字体
     * 1.颜色
     * 2.居中
     */
    private int mBgColor = Color.WHITE;
    private int mCircleTextColor = Color.WHITE;
    private String mText = "";

    public TextCircleView(Context context) {
        super(context);
    }

    public TextCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.textCircle);
        //背景颜色
        mBgColor = typedArray.getColor(R.styleable.textCircle_bgColor, mBgColor);
        //Text颜色
        mCircleTextColor = typedArray.getColor(R.styleable.textCircle_circleTextColor, mCircleTextColor);
        //Text
        mText = (String) typedArray.getText(R.styleable.textCircle_text);
        typedArray.recycle();
    }

    public TextCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(mBgColor);
        int radius = 0;
        if (getWidth() > getHeight()) {
            radius = getHeight();
        } else {
            radius = getWidth();
        }
        canvas.drawCircle(radius / 2, radius / 2, radius / 2, paint);

        //居中文字
        float nodeW = Math.abs(getWidth()) / 2;
        float lineWidth = (float) ((1 - 1 / Math.sqrt(2)) * nodeW * 3 / 4);
        String line = String.valueOf(lineWidth);
        int _line = Integer.parseInt(line.split("\\.")[0]);
        int textWidth = _line * 6;
        paint.setTextSize(textWidth);
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(false);//非粗体
        paint.setColor(mCircleTextColor);
        float centerX = (radius - textWidth) / 2;//最小宽高
        float centerY = (radius) / 2;//最小宽高
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float nameTop = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float nameBottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (centerY - nameTop / 2 - nameBottom / 2);//基线中间点的y轴计算公式
        canvas.drawText(mText,
                centerX,
                baseLineY,
                paint);
    }

    public void setCirCleColor(int mCirCleColor) {
        this.mBgColor = mCirCleColor;
    }

    public void setTextColor(int mTextColor) {
        this.mCircleTextColor = mTextColor;
    }

    public void setText(String mText) {
        this.mText = mText;
    }
}
