package com.purity.yu.gameplatform.baccarat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.gangbeng.basemodule.utils.LogUtil;
import com.purity.yu.gameplatform.widget.DpUtil;

public class CanvasView extends View {
    private int mSlashColor = Color.RED;
    private float strokeWidth = DpUtil.dp2px(1);
    private int mTextColor = Color.BLACK;
    private String mText = "莊";
    private int mColor = Color.RED;
    private float mRingHeight = DpUtil.dp2px(1);

    public CanvasView(Context context) {
        super(context);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(mSlashColor);
        paint.setStrokeWidth(strokeWidth);
//        canvas.drawLine(100, 100, 300, 300, paint);// 画线
        canvas.drawLine(40, 0, 40, 40, paint);// 画竖线
        paint.setColor(Color.BLUE);
        canvas.drawLine(0, 40, 40, 40, paint);// 画横线
        //left=31.333334bottom=60.666668right=60.666668top=31.333334 右上左底


        paint.setColor(Color.RED);
        //直接再原来的地方减去15 上斜线
        canvas.drawLine(50f - 5,//右上
                40f,
                60f - 5,
                30f,
                paint);
        canvas.drawLine(30f,//左上
                60f - 5,
                40f,
                50f - 5,
                paint);
        paint.setColor(Color.BLUE);
        canvas.drawLine(50f,//右下
                40f + 5,
                60f,
                30f + 5,
                paint);
        canvas.drawLine(30f + 5,//左下
                60f,
                40f + 5,
                50f,
                paint);
        paint.setColor(Color.GREEN);
        canvas.drawLine(50f,
                40f,
                60f,
                30f,
                paint);
        canvas.drawLine(30f,
                60f,
                40f,
                50f,
                paint);
        paint.setColor(Color.BLACK);
        canvas.drawLine(30f,
                60f,
                60f,
                30f,
                paint);

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(mColor);
        LogUtil.i("left=" + getLeft() + " top=" + getTop() + " right=" + getRight() + " bottom=" + getBottom());
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paint);

        //居中文字
        float nodeW = Math.abs(getWidth()) / 2;
        float lineWidth = (float) ((1 - 1 / Math.sqrt(2)) * nodeW * 3 / 4);
        String line = String.valueOf(lineWidth);
        int _line = Integer.parseInt(line.split("\\.")[0]);
        paint.setTextSize(_line);
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(false);//非粗体
        paint.setStrokeWidth(0.6f);
        paint.setColor(mTextColor);
        float centerX = (getWidth() - _line) / 2;
        float centerY = (getHeight()) / 2;
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float nameTop = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float nameBottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineX = (int) (centerX - nameTop / 2 - nameBottom / 2);//基线中间点的y轴计算公式
        int baseLineY = (int) (centerY - nameTop / 2 - nameBottom / 2);//基线中间点的y轴计算公式
        canvas.drawText(mText,
                centerX,
                baseLineY,
                paint);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(1.0f);
        canvas.drawLine(getWidth() / 2.0f,
                0.0f,
                getWidth() / 2.0f,
                getHeight(),
                paint);
        canvas.drawLine(0.0f,
                getHeight() / 2.0f,
                getWidth(),
                getHeight() / 2.0f,
                paint);
    }

}
