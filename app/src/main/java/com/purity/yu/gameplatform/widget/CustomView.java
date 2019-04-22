package com.purity.yu.gameplatform.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class CustomView extends AppCompatImageView {

    //画笔
    private Paint linePaint;
    //路径效果
    private PathEffect pathEffect;
    //路径对象
    private Path mPath;
    private float phase;

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint();
    }

    public CustomView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pathEffect = new DashPathEffect(new float[]{200, 5, 200, 5}, phase);
        linePaint.setAntiAlias(true);//设置抗锯齿
        LinearGradient lg = new LinearGradient(0, 0, 100, 100, Color.WHITE, Color.BLUE, Shader.TileMode.MIRROR);
        linePaint.setShader(lg);
        linePaint.setStyle(Paint.Style.STROKE);//画笔样式
        linePaint.setStrokeWidth(12);//画笔宽度
        linePaint.setPathEffect(pathEffect);//路径动画

        mPath = new Path();
        mPath.moveTo(0, 0);//起点
        mPath.lineTo(0, getHeight());
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(getWidth(), 0);

        mPath.close();
        canvas.drawPath(mPath, linePaint);
    }

    public void setPhase(float phase) {
        this.phase = phase;
        postInvalidate();
    }


}