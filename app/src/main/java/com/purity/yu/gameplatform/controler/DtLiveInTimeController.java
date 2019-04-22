package com.purity.yu.gameplatform.controler;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.baccarat.YHZGridView;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.utils.ScreenUtil;
import com.purity.yu.gameplatform.widget.GestureListener;

/**
 * 功能：实时开奖模块
 * 描述：左上角缩放模块和正中下方黑色背景纸牌模块
 * Create by purity on 2018/5/19.
 */
public class DtLiveInTimeController {
    private Context mContext;
    private static DtLiveInTimeController instance = null;

    public RelativeLayout rl_in_time_layout;
    public RelativeLayout rl_content;
    public RelativeLayout rl_just_in_time;
    public RelativeLayout rl_road;

    public YHZGridView gv_left;
    public YHZGridView gv_right_top;
    public YHZGridView gv_right_middle;
    public YHZGridView gv_right_bottom_1;
    public YHZGridView gv_right_bottom_2;
    public View v_1;
    public View v_2;
    public View v_3;
    public View v_4;
    public View v_5;
    public YHZGridView gv_left_short;
    public YHZGridView gv_right_top_short;
    public YHZGridView gv_right_middle_short;
    public YHZGridView gv_right_bottom_1_short;
    public YHZGridView gv_right_bottom_2_short;
    public View v_1_short;
    public View v_2_short;
    public View v_3_short;
    public View v_4_short;
    public View v_5_short;

    public RelativeLayout rl_statistic;
    public TextView tv_in_game_total_value;
    public TextView tv_in_game_banker_value;
    public TextView tv_in_game_player_value;
    public TextView tv_in_game_tie_value;
    public RelativeLayout rl_banker_to_player;
    public ImageView iv_banker_to_player_1;
    public ImageView iv_banker_to_player_2;
    public ImageView iv_banker_to_player_3;
    public RelativeLayout rl_player_to_banker;
    public ImageView iv_player_to_banker_1;
    public ImageView iv_player_to_banker_2;
    public ImageView iv_player_to_banker_3;
    public RelativeLayout rl_ex_in;
    public ImageView iv_ex;

    private int isExpand;
    private DisplayMetrics displayMetrics;
    private RelativeLayout.LayoutParams rlRoadLayoutParams;
    private RelativeLayout.LayoutParams rlStatisticLayoutParams;
    private RelativeLayout.LayoutParams rlContentLayoutParams;

    public void init(Context context) {
        mContext = context;
        displayMetrics = context.getResources().getDisplayMetrics();
    }

    public static DtLiveInTimeController getInstance() {
        if (null == instance) {
            instance = new DtLiveInTimeController();
        }
        return instance;
    }

    public void copyWidget(RelativeLayout rl_in_time_layout, RelativeLayout rl_content, RelativeLayout rl_just_in_time, RelativeLayout rl_road,
                           YHZGridView gv_left, YHZGridView gv_right_top, YHZGridView gv_right_middle, YHZGridView gv_right_bottom_1, YHZGridView gv_right_bottom_2, View v_1, View v_2, View v_3, View v_4, View v_5,
                           YHZGridView gv_left_short, YHZGridView gv_right_top_short, YHZGridView gv_right_middle_short, YHZGridView gv_right_bottom_1_short, YHZGridView gv_right_bottom_2_short, View v_1_short, View v_2_short, View v_3_short, View v_4_short, View v_5_short,
                           RelativeLayout rl_statistic, TextView tv_in_game_total_value, TextView tv_in_game_banker_value, TextView tv_in_game_banker_pair_value, TextView tv_in_game_player_pair_value,
                           RelativeLayout rl_banker_to_player, ImageView iv_banker_to_player_1, ImageView iv_banker_to_player_2, ImageView iv_banker_to_player_3,
                           RelativeLayout rl_player_to_banker, ImageView iv_player_to_banker_1, ImageView iv_player_to_banker_2, ImageView iv_player_to_banker_3,
                           RelativeLayout rl_ex_in, ImageView iv_ex) {
        this.rl_in_time_layout = rl_in_time_layout;
        this.rl_just_in_time = rl_just_in_time;
        this.rl_content = rl_content;
        this.rl_road = rl_road;
        this.gv_left = gv_left;
        this.gv_right_top = gv_right_top;
        this.gv_right_middle = gv_right_middle;
        this.gv_right_bottom_1 = gv_right_bottom_1;
        this.gv_right_bottom_2 = gv_right_bottom_2;
        this.v_1 = v_1;
        this.v_2 = v_2;
        this.v_3 = v_3;
        this.v_4 = v_4;
        this.v_5 = v_5;
        this.gv_left_short = gv_left_short;
        this.gv_right_top_short = gv_right_top_short;
        this.gv_right_middle_short = gv_right_middle_short;
        this.gv_right_bottom_1_short = gv_right_bottom_1_short;
        this.gv_right_bottom_2_short = gv_right_bottom_2_short;
        this.v_1_short = v_1_short;
        this.v_2_short = v_2_short;
        this.v_3_short = v_3_short;
        this.v_4_short = v_4_short;
        this.v_5_short = v_5_short;

        this.rl_statistic = rl_statistic;
        this.tv_in_game_total_value = tv_in_game_total_value;
        this.tv_in_game_banker_value = tv_in_game_banker_value;
        this.rl_banker_to_player = rl_banker_to_player;
        this.iv_banker_to_player_1 = iv_banker_to_player_1;
        this.iv_banker_to_player_2 = iv_banker_to_player_2;
        this.iv_banker_to_player_3 = iv_banker_to_player_3;
        this.rl_player_to_banker = rl_player_to_banker;
        this.iv_player_to_banker_1 = iv_player_to_banker_1;
        this.iv_player_to_banker_2 = iv_player_to_banker_2;
        this.iv_player_to_banker_3 = iv_player_to_banker_3;
        this.rl_ex_in = rl_ex_in;
        this.iv_ex = iv_ex;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 0);
        int _isExpand = SharedPreUtil.getInstance(mContext).getInt(Constant.IS_EXPAND);

        rlContentLayoutParams = (RelativeLayout.LayoutParams) rl_content.getLayoutParams();
        rlRoadLayoutParams = (RelativeLayout.LayoutParams) rl_road.getLayoutParams();
        rlStatisticLayoutParams = (RelativeLayout.LayoutParams) rl_statistic.getLayoutParams();

        if (_isExpand == 0) {
            gv_left_short.setVisibility(View.VISIBLE);
            gv_right_top_short.setVisibility(View.VISIBLE);
            gv_right_middle_short.setVisibility(View.VISIBLE);
            gv_right_bottom_1_short.setVisibility(View.VISIBLE);
            gv_right_bottom_2_short.setVisibility(View.VISIBLE);
            gv_left.setVisibility(View.GONE);
            gv_right_top.setVisibility(View.GONE);
            gv_right_middle.setVisibility(View.GONE);
            gv_right_bottom_1.setVisibility(View.GONE);
            gv_right_bottom_2.setVisibility(View.GONE);
            gv_left_short.setColumnCount(4);
            gv_left_short.setRowCount(6);
            gv_left_short.setNoRight(true);
            gv_left_short.setNoBottom(true);
            gv_right_top_short.setColumnCount(18);
            gv_right_top_short.setRowCount(6);
            gv_right_top_short.setNoBottom(true);
            gv_right_top_short.setNoRight(true);
            gv_right_middle_short.setColumnCount(18);
            gv_right_middle_short.setRowCount(3);
            gv_right_middle_short.setNoBottom(true);
            gv_right_middle_short.setNoRight(true);
            gv_right_bottom_1_short.setColumnCount(9);
            gv_right_bottom_1_short.setRowCount(3);
            gv_right_bottom_1_short.setNoRight(true);
            gv_right_bottom_1_short.setNoBottom(true);
            gv_right_bottom_2_short.setColumnCount(9);
            gv_right_bottom_2_short.setRowCount(3);
            gv_right_bottom_2_short.setNoRight(true);
            gv_right_bottom_2_short.setNoBottom(true);
        }
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
                } else if (isExpand == 2) {
                    trans2to0();//展中 左滑
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
            } else if (isExpand == 2) {
                trans2to0();//展中 左滑
            }
            return super.left();
        }

        @Override
        public boolean right() {
            isExpand = SharedPreUtil.getInstance(mContext).getInt(Constant.IS_EXPAND);
            if (isExpand == 1) {
                trans1to0();//隐中 右滑
            } else if (isExpand == 0) {
                trans0to2();//中展 右滑
            }
            return super.right();
        }
    }

    private void trans1to0() {
        if (displayMetrics.widthPixels > 2100) {
            slideView(-(ScreenUtil.dip2px(266f)), 0, rl_in_time_layout);//华为M5平板
        } else
            slideView(-(ScreenUtil.dip2px(236f)), 0, rl_in_time_layout);
        iv_ex.setRotation(180);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 0);
    }

    private void trans0to1() {
        if (displayMetrics.widthPixels > 2100) {
            slideView(0, -(ScreenUtil.dip2px(266f)), rl_in_time_layout);//华为M5平板
        } else
            slideView(0, -(ScreenUtil.dip2px(236f)), rl_in_time_layout);//本来是向右平移，结果右边为负数，就以看到的点为坐标原点，向左平移
        iv_ex.setRotation(0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 1);
    }

    private void trans0to2() {
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 2);
        rlRoadLayoutParams.width = (int) ScreenUtil.dip2px(506f);
        rlStatisticLayoutParams.width = (int) ScreenUtil.dip2px(506f);
        rlContentLayoutParams.width = (int) ScreenUtil.dip2px(506f);

        gv_left_short.setVisibility(View.GONE);
        gv_right_top_short.setVisibility(View.GONE);
        gv_right_middle_short.setVisibility(View.GONE);
        gv_right_bottom_1_short.setVisibility(View.GONE);
        gv_right_bottom_2_short.setVisibility(View.GONE);
        v_1_short.setVisibility(View.GONE);
        v_2_short.setVisibility(View.GONE);
        v_3_short.setVisibility(View.GONE);
        v_4_short.setVisibility(View.GONE);
        v_5_short.setVisibility(View.GONE);
        gv_left.setVisibility(View.VISIBLE);
        gv_right_top.setVisibility(View.VISIBLE);
        gv_right_middle.setVisibility(View.VISIBLE);
        gv_right_bottom_1.setVisibility(View.VISIBLE);
        gv_right_bottom_2.setVisibility(View.VISIBLE);
        v_1.setVisibility(View.VISIBLE);
        v_2.setVisibility(View.VISIBLE);
        v_3.setVisibility(View.VISIBLE);
        v_4.setVisibility(View.VISIBLE);
        v_5.setVisibility(View.VISIBLE);
        gv_left.setColumnCount(11);
        gv_left.setRowCount(6);
        gv_left.setNoRight(true);
        gv_left.setNoBottom(true);
        gv_right_top.setColumnCount(34);
        gv_right_top.setRowCount(6);
        gv_right_top.setNoBottom(true);
        gv_right_top.setNoRight(true);
        gv_right_middle.setColumnCount(34);
        gv_right_middle.setRowCount(3);
        gv_right_middle.setNoBottom(true);
        gv_right_middle.setNoRight(true);
        gv_right_bottom_1.setColumnCount(17);
        gv_right_bottom_1.setRowCount(3);
        gv_right_bottom_1.setNoRight(true);
        gv_right_bottom_1.setNoBottom(true);
        gv_right_bottom_2.setColumnCount(17);
        gv_right_bottom_2.setRowCount(3);
        gv_right_bottom_2.setNoRight(true);
        gv_right_bottom_2.setNoBottom(true);
    }

    private void trans2to0() {
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 0);
        rl_ex_in.setVisibility(View.VISIBLE);
        rlRoadLayoutParams.width = (int) ScreenUtil.dip2px(236f);
        rlStatisticLayoutParams.width = (int) ScreenUtil.dip2px(236f);
        rlContentLayoutParams.width = (int) ScreenUtil.dip2px(236f);

        gv_left_short.setVisibility(View.VISIBLE);
        gv_right_top_short.setVisibility(View.VISIBLE);
        gv_right_middle_short.setVisibility(View.VISIBLE);
        gv_right_bottom_1_short.setVisibility(View.VISIBLE);
        gv_right_bottom_2_short.setVisibility(View.VISIBLE);
        v_1_short.setVisibility(View.VISIBLE);
        v_2_short.setVisibility(View.VISIBLE);
        v_3_short.setVisibility(View.VISIBLE);
        v_4_short.setVisibility(View.VISIBLE);
        v_5_short.setVisibility(View.VISIBLE);
        gv_left.setVisibility(View.GONE);
        gv_right_top.setVisibility(View.GONE);
        gv_right_middle.setVisibility(View.GONE);
        gv_right_bottom_1.setVisibility(View.GONE);
        gv_right_bottom_2.setVisibility(View.GONE);
        gv_left_short.setColumnCount(4);
        gv_left_short.setRowCount(6);
        gv_left_short.setNoRight(true);
        gv_left_short.setNoBottom(true);
        gv_right_top_short.setColumnCount(18);
        gv_right_top_short.setRowCount(6);
        gv_right_top_short.setNoBottom(true);
        gv_right_top_short.setNoRight(true);
        gv_right_middle_short.setColumnCount(18);
        gv_right_middle_short.setRowCount(3);
        gv_right_middle_short.setNoBottom(true);
        gv_right_middle_short.setNoRight(true);
        gv_right_bottom_1_short.setColumnCount(9);
        gv_right_bottom_1_short.setRowCount(3);
        gv_right_bottom_1_short.setNoRight(true);
        gv_right_bottom_1_short.setNoBottom(true);
        gv_right_bottom_2_short.setColumnCount(9);
        gv_right_bottom_2_short.setRowCount(3);
        gv_right_bottom_2_short.setNoRight(true);
        gv_right_bottom_2_short.setNoBottom(true);
        v_1.setVisibility(View.GONE);
        v_2.setVisibility(View.GONE);
        v_3.setVisibility(View.GONE);
        v_4.setVisibility(View.GONE);
        v_5.setVisibility(View.GONE);
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
