package com.purity.yu.gameplatform.baccarat;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.purity.yu.gameplatform.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 网格线
 * Created by yanghaozhang on 2018/7/3.
 */
public class YHZGridView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private static final String TAG = "YHZGridView";
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
    private List<NodeImp> mNodeTwinklingList;//保存闪烁的数据

    private boolean mNoRight = false;

    private boolean mNoBottom = false;
    private Context mContext;
    private DisplayMetrics displayMetrics;

    private int twinklingTime = 1;

    public YHZGridView(Context context) {
        super(context);
        init(context);
    }

    public YHZGridView(Context context, @Nullable AttributeSet attrs) {
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
        init(context);
    }

    public YHZGridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mNodeList = new ArrayList<>();
        mNodeTwinklingList = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mContext = context;
        displayMetrics = mContext.getResources().getDisplayMetrics();
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

        float _h = (float) h;//损失精度 强转一下
        //神来之笔 去掉右边和下边的线
        float minHeight = (_h - (mNoBottom ? 0 : mLineH)) / mRowCount;
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
            canvas.drawLine(i * mNodeW + lineHeight,
                    0,
                    i * mNodeW + lineHeight,
                    mHeight - differentH,
                    mPaint);

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

        boolean needTwinkling = twinklingTime % 2 == 0;
        for (NodeImp node : needTwinkling ? mNodeTwinklingList : mNodeList) {
            node.draw(canvas,
                    mPaint,
                    node.getX() * mNodeW + mLineH,
                    node.getY() * mNodeW + mLineH,
                    node.getX() * mNodeW + mNodeW,
                    node.getY() * mNodeW + mNodeW);
        }
    }

    public void setNodeList(List<NodeImp> nodeList, int count, boolean isShow) {
        if (count > 0 && isShow) {
            this.mNodeTwinklingList = nodeList;
            ValueAnimator animator = ValueAnimator.ofInt(count + 2, 0);
            animator.setDuration(5000);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(this);
            animator.addListener(this);
            animator.start();
            invalidate();
        } else {
            this.mNodeTwinklingList = this.mNodeList;
            this.mNodeList = nodeList;
        }
    }

    //ValueAnimator.ofObject用法
//https://wiki.jikexueyuan.com/project/android-animation/6.html
    //https://blog.csdn.net/guolin_blog/article/details/43816093
    public void setRowCount(int rowCount) {
        this.mRowCount = rowCount;
    }

    public void setColumnCount(int columnCount) {
        this.mColumnCount = columnCount;
    }


    public void setNoRight(boolean noRight) {
        this.mNoRight = noRight;
    }

    public void setNoBottom(boolean noBottom) {
        this.mNoBottom = noBottom;
    }

    @Override
    public void onAnimationStart(Animator animation) {
        twinklingTime = 0;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        twinklingTime = 1;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        int animateValue = (int) valueAnimator.getAnimatedValue();
        if (twinklingTime != animateValue) {
            twinklingTime = animateValue;
            invalidate();
        }
    }
}
