package com.purity.yu.gameplatform.baccarat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.purity.yu.gameplatform.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 网格线
 * Created by PURITY on 2018/9/25.
 */
public class YCHGridView extends View {
    private Paint mPaint;

    private float mWidth;

    private float mHeight;

    private int mRowCount = 10;

    private int mColumnCount = 10;

    private float mNodeW;

    private float mLineH;
    private float mDefaultLineH = 1f;

    private int mLineColor;
    private int mDefaultLineColor = 0xff2e7cea;

    private List<NodeImp> mNodeList;

    private boolean mNoRight = false;

    private boolean mNoBottom = false;

    public YCHGridView(Context context) {
        super(context);
        init();
    }

    public YCHGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.gridLine);
        //网格颜色
        mLineColor = typedArray.getColor(R.styleable.gridLine_divideColor, mDefaultLineColor);
        //网格线宽度
        mLineH = typedArray.getDimension(R.styleable.gridLine_divideHeight, mDefaultLineH);
        //网格行数
        mRowCount = typedArray.getInt(R.styleable.gridLine_row, mRowCount);
        //网格列数
        mColumnCount = typedArray.getInt(R.styleable.gridLine_column, mColumnCount);
        //有没有最右边的竖线
        mNoRight = typedArray.getBoolean(R.styleable.gridLine_noRight, mNoRight);
        //有没有最底边的横线
        mNoBottom = typedArray.getBoolean(R.styleable.gridLine_noBottom, mNoBottom);
        typedArray.recycle();
        init();
    }

    public YCHGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mNodeList = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 自定义view绘制流程 构造View() –> onMeasure() –> onSizeChanged() –> onLayout() –> onDraw()
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        LogUtil.i("YCHGridView=left0=" + w + "top=" + h + "right=" + oldw + "bottom=" + oldh);//w是layout_width，h是layout_height
        float _h = (float) h;//损失精度 强转一下
        //神来之笔 去掉右边和下边的线
        float minHeight = (_h - (mNoBottom ? 0 : mLineH)) / mRowCount;//高度固定，用它作为长和宽
        mNodeW = minHeight;//给高度解决适配问题  哈哈
        mWidth = w;
        mHeight = h;
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineH);
        mPaint.setColor(mLineColor);
        float lineHeight = mLineH / 2;
        //处理因为除以2的误差
        float differentH = mLineH - (mNoBottom ? mLineH : lineHeight);
        //竖线
        for (int i = 0; i <= (mNoRight ? mColumnCount - 1 : mColumnCount); i++) {
            canvas.drawLine(i * mNodeW * 3 + lineHeight,
                    0,
                    i * mNodeW * 3 + lineHeight,
                    mHeight - differentH,
                    mPaint);
//            LogUtil.i("YCHGridView=竖线间距=" + (i * mNodeW * 3 + lineHeight) + "YCHGridView=竖线间距居中=" + (i * mNodeW * 3 + lineHeight + 80.73529));//竖线是YHZGridView三倍
        }
        float differentW = mLineH - (mNoRight ? mLineH : lineHeight);
        //横线
        for (int i = 0; i <= (mNoBottom ? mRowCount - 1 : mRowCount); i++) {
            canvas.drawLine(0,
                    i * mNodeW + lineHeight,
                    mWidth - differentW,
                    i * mNodeW + lineHeight,
                    mPaint);
        }

        for (NodeImp node : mNodeList) {
//            ToastUtil.show(getContext(),"NodeImp");
//            LogUtil.i("ces_bac_NodeImp");
            node.draw(canvas,
                    mPaint,
                    node.getX() * mNodeW * 3 + lineHeight,//这里宽网格的起始位置是竖线的间距
//                    node.getX() * mNodeW + mLineH,
                    node.getY() * mNodeW + mLineH,
                    node.getX() * mNodeW + mNodeW,
                    node.getY() * mNodeW + mNodeW);
        }
    }

    public void setRowCount(int rowCount) {
        this.mRowCount = rowCount;
    }

    public void setColumnCount(int columnCount) {
        this.mColumnCount = columnCount;
    }

    public void setLineColor(int lineColor) {
        this.mLineColor = lineColor;
    }

    public void setNodeList(List<NodeImp> nodeList) {
        this.mNodeList = nodeList;
    }

    public void setLineH(float lineH) {
        this.mLineH = lineH;
    }

    public void setNoRight(boolean noRight) {
        this.mNoRight = noRight;
    }

    public void setNoBottom(boolean noBottom) {
        this.mNoBottom = noBottom;
    }
}
