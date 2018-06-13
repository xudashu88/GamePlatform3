package com.chuangku.gameplatform3.controler;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.activity.UserActivity;
import com.chuangku.gameplatform3.adapter.GridLeftLiveAdapter;
import com.chuangku.gameplatform3.adapter.GridRightBottom1LiveAdapter;
import com.chuangku.gameplatform3.adapter.GridRightBottom2LiveAdapter;
import com.chuangku.gameplatform3.adapter.GridRightMiddleLiveAdapter;
import com.chuangku.gameplatform3.adapter.GridRightTopLiveAdapter;
import com.chuangku.gameplatform3.base.Constant;
import com.gangbeng.basemodule.utils.SharedPreUtil;

import butterknife.BindView;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class BaccaratLiveInTimeControler {
    private Context mContext;
    private static BaccaratLiveInTimeControler instance = null;

    public RelativeLayout rl_in_time_layout;
    public GridView gv_left;
    public GridView gv_right_top;
    public GridView gv_right_middle;
    public GridView gv_right_bottom_1;
    public GridView gv_right_bottom_2;
    public RelativeLayout rl_statistic;
    public TextView tv_in_game_total_value;
    public TextView tv_in_game_banker_value;
    public TextView tv_in_game_player_value;
    public TextView tv_in_game_tie_value;
    public TextView tv_in_game_banker_pair_value;
    public TextView tv_in_game_player_pair_value;
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
    public ImageView iv_in;

    private boolean isExpand;
    private DisplayMetrics displayMetrics;

    public void init(Context context) {
        mContext = context;
        displayMetrics = context.getResources().getDisplayMetrics();
    }

    public static BaccaratLiveInTimeControler getInstance() {
        if (null == instance) {
            instance = new BaccaratLiveInTimeControler();
        }
        return instance;
    }

    public void copyWidget(RelativeLayout rl_in_time_layout, GridView gv_left, GridView gv_right_top, GridView gv_right_middle, GridView gv_right_bottom_1, GridView gv_right_bottom_2,
                           RelativeLayout rl_statistic, TextView tv_in_game_total_value, TextView tv_in_game_banker_value, TextView tv_in_game_player_value, TextView tv_in_game_tie_value, TextView tv_in_game_banker_pair_value, TextView tv_in_game_player_pair_value,
                           RelativeLayout rl_banker_to_player, ImageView iv_banker_to_player_1, ImageView iv_banker_to_player_2, ImageView iv_banker_to_player_3,
                           RelativeLayout rl_player_to_banker, ImageView iv_player_to_banker_1, ImageView iv_player_to_banker_2, ImageView iv_player_to_banker_3,
                           RelativeLayout rl_ex_in, ImageView iv_ex, ImageView iv_in) {
        this.rl_in_time_layout = rl_in_time_layout;
        this.gv_left = gv_left;
        this.gv_right_top = gv_right_top;
        this.gv_right_middle = gv_right_middle;
        this.gv_right_bottom_1 = gv_right_bottom_1;
        this.gv_right_bottom_2 = gv_right_bottom_2;
        this.rl_statistic = rl_statistic;
        this.tv_in_game_total_value = tv_in_game_total_value;
        this.tv_in_game_banker_value = tv_in_game_banker_value;
        this.tv_in_game_player_value = tv_in_game_player_value;
        this.tv_in_game_tie_value = tv_in_game_tie_value;
        this.tv_in_game_banker_pair_value = tv_in_game_banker_pair_value;
        this.tv_in_game_player_pair_value = tv_in_game_player_pair_value;
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
        this.iv_in = iv_in;


        gv_left.setAdapter(new GridLeftLiveAdapter(mContext));
        gv_right_top.setAdapter(new GridRightTopLiveAdapter(mContext));
        gv_right_middle.setAdapter(new GridRightMiddleLiveAdapter(mContext));
        gv_right_bottom_1.setAdapter(new GridRightBottom1LiveAdapter(mContext));
        gv_right_bottom_2.setAdapter(new GridRightBottom2LiveAdapter(mContext));
        initEvent();
    }

    private void initEvent() {
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, true);
        rl_ex_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpand = SharedPreUtil.getInstance(mContext).getBoolean(Constant.IS_EXPAND);
                if (isExpand == true) {
                    //-1520f
                    if (displayMetrics.widthPixels < 1800) {
                        slideView(0, -((float) (displayMetrics.widthPixels * 0.848)), rl_in_time_layout);
                    } else if (displayMetrics.widthPixels < 2100) {
                        slideView(0, -((float) (displayMetrics.widthPixels * 0.733)), rl_in_time_layout);
                    } else {
                        slideView(0, -((float) (displayMetrics.widthPixels * 0.828)), rl_in_time_layout);
                    }
                    iv_in.setVisibility(View.GONE);
                    iv_ex.setVisibility(View.VISIBLE);
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, false);
                } else if (isExpand == false) {
                    if (displayMetrics.widthPixels < 1800) {
                        slideView(-((float) (displayMetrics.widthPixels * 0.848)), 0, rl_in_time_layout);
                    } else if (displayMetrics.widthPixels < 2100) {
                        slideView(-((float) (displayMetrics.widthPixels * 0.733)), 0, rl_in_time_layout);
                    } else {
                        slideView(-((float) (displayMetrics.widthPixels * 0.828)), 0, rl_in_time_layout);
                    }

                    iv_in.setVisibility(View.VISIBLE);
                    iv_ex.setVisibility(View.GONE);
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, true);
                }
            }
        });
//        iv_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics());
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
