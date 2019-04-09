package com.purity.yu.gameplatform.controler;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

/**
 * 功能：实时开奖模块
 * 描述：左上角缩放模块和正中下方黑色背景纸牌模块
 * Create by purity on 2018/5/19.
 */
public class BaccaratLiveInTimeController {
    private Context mContext;
    private static BaccaratLiveInTimeController instance = null;

    public void init(Context context) {
        mContext = context;
    }

    public static BaccaratLiveInTimeController getInstance() {
        if (null == instance) {
            instance = new BaccaratLiveInTimeController();
        }
        return instance;
    }

    /**
     * 平移
     *
     * @param p1   x轴向左
     * @param p2   x轴向右
     * @param view 目标
     */
    public void slideView(final float p1, final float p2, final View view) {
        ObjectAnimator translationX = new ObjectAnimator().ofFloat(view, "translationX", p1, p2);
        ObjectAnimator translationY = new ObjectAnimator().ofFloat(view, "translationY", 0, 0);
        AnimatorSet animatorSet = new AnimatorSet();  //组合动画
        animatorSet.playTogether(translationX, translationY); //设置动画
        animatorSet.setDuration(200);
        animatorSet.start();
    }
}
