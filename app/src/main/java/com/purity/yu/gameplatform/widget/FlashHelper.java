package com.purity.yu.gameplatform.widget;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.purity.yu.gameplatform.controler.DemoController;

/**
 * Title:控件闪烁帮助类
 * Description:
 * <p>
 * Created by pei
 * Date: 2018/4/22
 */
public class FlashHelper {
    private static FlashHelper instance = null;

    private FlashHelper() {
    }

//    private static class Holder {
//        private static FlashHelper instance = new FlashHelper();
//    }
//
//    public static FlashHelper getInstance() {
//        return FlashHelper.Holder.instance;//这玩意发牌的框框乱闪 以后再看原因吧
//    }

    public static FlashHelper getInstance() {
        if (null == instance) {
            synchronized (FlashHelper.class) {
                if (null == instance) {
                    instance = new FlashHelper();
                }
            }
        }
        return instance;
    }


    /**
     * 开启View闪烁效果
     **/
    public void startFlick(View view, int repeat) {
        if (null == view) {
            return;
        }
//        Animation alphaAnimation = new AlphaAnimation(1, 0);
//        alphaAnimation.setDuration(600);
//        alphaAnimation.setInterpolator(new LinearInterpolator());
//        alphaAnimation.setRepeatCount(Animation.INFINITE);
//        alphaAnimation.setRepeatMode(Animation.REVERSE);
//        view.startAnimation(alphaAnimation);
//        AnimationSet animationSet = new AnimationSet();
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(500);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(repeat);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 取消View闪烁效果
     **/
    public void stopFlick(View view) {
        if (null == view) {
            return;
        }
        view.clearAnimation();
    }

}
