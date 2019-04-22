package com.purity.yu.gameplatform.controler;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.baccarat.YCHGridView;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.widget.GestureListener;

/**
 * 功能：实时开奖模块
 * 描述：左上角缩放模块和正中下方黑色背景纸牌模块
 * Create by purity on 2018/5/19.
 */
public class SinglePickLiveInTimeController {
    private Context mContext;
    private static SinglePickLiveInTimeController instance = null;

    public RelativeLayout rl_in_time_layout;
    public RelativeLayout rl_content;
    public RelativeLayout rl_just_in_time;
    public RelativeLayout rl_road;

    public YCHGridView gv_left;

    public RelativeLayout rl_statistic;
    public RelativeLayout rl_ex_in;
    public ImageView iv_ex;
    LinearLayout ll_back;
    TextView tv_game_name;
    RelativeLayout rl_table;

    private int isExpand;

    public void init(Context context) {
        mContext = context;
    }

    public static SinglePickLiveInTimeController getInstance() {
        if (null == instance) {
            instance = new SinglePickLiveInTimeController();
        }
        return instance;
    }

    public void copyWidget(RelativeLayout rl_in_time_layout, RelativeLayout rl_content, RelativeLayout rl_just_in_time, RelativeLayout rl_road,
                           YCHGridView gv_left, RelativeLayout rl_statistic,
                           RelativeLayout rl_ex_in, ImageView iv_ex,
                           LinearLayout ll_back,TextView tv_game_name, RelativeLayout rl_table) {
        this.rl_in_time_layout = rl_in_time_layout;
        this.rl_just_in_time = rl_just_in_time;
        this.rl_content = rl_content;
        this.rl_road = rl_road;
        this.gv_left = gv_left;
        this.rl_statistic = rl_statistic;
        this.rl_ex_in = rl_ex_in;
        this.iv_ex = iv_ex;
        this.ll_back = ll_back;
        this.tv_game_name = tv_game_name;
        this.rl_table = rl_table;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 0);
        initEvent();
    }

    private void initEvent() {
        isExpand = SharedPreUtil.getInstance(mContext).getInt(Constant.IS_EXPAND);
        rl_ex_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpand = SharedPreUtil.getInstance(mContext).getInt(Constant.IS_EXPAND);
                if (isExpand == 0) {
                    trans0to1();//中隐 左滑
                } else if (isExpand == 1) {
                    trans1to0();//隐中 右滑
                }
            }
        });
    }

    /**
     * 继承GestureListener，重写left和right方法
     */
    public class MyGestureListener extends GestureListener {
        private int isExpand;

        public MyGestureListener(Context context) {
            super(context);
        }

        @Override
        public boolean left() {
            isExpand = SharedPreUtil.getInstance(mContext).getInt(Constant.IS_EXPAND);
            if (isExpand == 0) {
                trans0to1();//中隐 左滑
            }
            return super.left();
        }

        @Override
        public boolean right() {
            isExpand = SharedPreUtil.getInstance(mContext).getInt(Constant.IS_EXPAND);
            if (isExpand == 1) {
                trans1to0();//隐中 右滑
            }
            return super.right();
        }
    }

    private void trans1to0() {
        slideView(-(mContext.getResources().getDimensionPixelSize(R.dimen.unit380)), 0, rl_in_time_layout);
        iv_ex.setRotation(180);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 0);
        setNav(View.INVISIBLE);
    }

    private void trans0to1() {
        slideView(0, -(mContext.getResources().getDimensionPixelSize(R.dimen.unit380)), rl_in_time_layout);//本来是向右平移，结果右边为负数，就以看到的点为坐标原点，向左平移
        iv_ex.setRotation(0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 1);
        setNav(View.VISIBLE);
    }

    private void setNav(int visible) {
        ll_back.setVisibility(visible);
        tv_game_name.setVisibility(visible);
        rl_table.setVisibility(visible);
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
