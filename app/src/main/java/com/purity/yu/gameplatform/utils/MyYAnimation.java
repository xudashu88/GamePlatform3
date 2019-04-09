package com.purity.yu.gameplatform.utils;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * 自定义Y轴旋转动画
 * Created by Administrator on 2017/2/10.
 */

public class MyYAnimation extends Animation {
    int centerX, centerY;
    Camera camera = new Camera();

    /**
     * 获取坐标，定义动画时间
     *
     * @param width
     * @param height
     * @param parentWidth
     * @param parentHeight
     */
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        //获得中心点坐标
        centerX = width / 2;
        centerY = width / 2;
        //动画执行时间 自行定义
        setDuration(700);
        setInterpolator(new LinearInterpolator());
    }

    /**
     * 旋转的角度设置
     *
     * @param interpolatedTime
     * @param t
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final Matrix matrix = t.getMatrix();
        camera.save();
        //
        float from = 0.0f, to = 0.0f;
        if (interpolatedTime < 0.5) {
            from = 0.0f;
            to = 180.0f;
        } else {
            from = 360.0f;
            to = 180.0f;
        }

        // 旋转的角度
        float degree = from + (to - from) * interpolatedTime;
        boolean overHalf = (interpolatedTime > 0.5f);
        if (overHalf) {
            // 翻转过半的情况下，为保证数字仍为可读的文字而非镜面效果的文字，需翻转180度。
            degree = degree - 180;
        }
        //中心是Y轴旋转，这里可以自行设置X轴 Y轴 Z轴
//        camera.rotateY(360 * interpolatedTime);
        camera.rotateY(degree);
        //把我们的摄像头加在变换矩阵上
        camera.getMatrix(matrix);
        //设置翻转中心点
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        camera.restore();
    }
}