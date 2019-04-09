package com.purity.yu.gameplatform.baccarat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.purity.yu.gameplatform.widget.DpUtil;

/**
 * Created by
 */
public class SingleNode implements NodeImp {

    private int mX = 0;

    private int mY = 0;

    private float mRingHeight = DpUtil.dp2px(1);

    private float mLineHeight = -100;

    private boolean mNeedSlash = false;


    private int mColor = Color.BLUE;

    private int mSlashColor = Color.GREEN;
    private int mTextColor = Color.BLACK;

    private String mText = "";
    private Context mContext;
    private TextView tv_game_name;

    public SingleNode() {
    }

    /**
     * 大路
     *
     * @param mX    x轴
     * @param mY    y轴
     * @param mText 文本
     */
    public SingleNode(int mX, int mY, String mText, Context context,TextView tv_game_name) {
        this.mX = mX;
        this.mY = mY;
        this.mText = mText;
        this.mContext = context;
        this.tv_game_name = tv_game_name;
//        tv_game_name.setText("a"+mText);
//        LogUtil.i("ces_g"+mText);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, float left, float top, float right, float bottom) {
//        LogUtil.i("SingleNode=left2=" + left + "top=" + top + "right=" + right + "bottom=" + bottom);//这是NodeImp决定的
        paint.setStrokeWidth(mRingHeight);
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
//        LogUtil.i("ces_h"+mText);
//        tv_game_name.setText("b"+mText);
        //居中文字
//        paint.setTextSize(30);
        float _textFont = mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.font8);
        paint.setTextSize(_textFont);
        paint.setStyle(Paint.Style.FILL);//字体细
        if (mText.contains("黑") || mText.contains("花")) {
            paint.setColor(Color.BLACK);
        } else if (mText.contains("红") || mText.contains("方")) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.BLUE);
        }

        float centerX = (right + left) / 2;
        float centerY = (bottom + top) / 2;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float nameTop = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float nameBottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (centerY - nameTop / 2 - nameBottom / 2);//基线中间点的y轴计算公式
        float a = DpUtil.dp2px(25.0f);
        LogUtil.i("SingleNode=draw=" + ((left + 70.73529f)) + " a=" + (left + a));
        //数据为空，就会报空指针
        canvas.drawText(mText,
                left + a,
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
