package com.chuangku.gameplatform3.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseActivity;
import com.chuangku.gameplatform3.controler.BaccaratLiveAnimControler;
import com.chuangku.gameplatform3.controler.BaccaratLiveControler;
import com.chuangku.gameplatform3.controler.BaccaratLiveInTimeControler;
import com.chuangku.gameplatform3.widget.PercentCircleAntiClockwise;
import com.gangbeng.basemodule.utils.Util;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/24.
 */
@ContentView(R.layout.activity_baccarat_live)
public class BaccaratLiveActivity extends BaseActivity {
    @BindView(R.id.root_video)
    RelativeLayout root_video;
    @BindView(R.id.tvv_1)
    TXCloudVideoView tvv_1;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_table_limits)
    TextView tv_table_limits;
    @BindView(R.id.rl_dragon_bonus)
    RelativeLayout rl_dragon_bonus;
    @BindView(R.id.tv_switch_on)
    public TextView tv_switch_on;
    @BindView(R.id.tv_switch_off)
    public TextView tv_switch_off;
    @BindView(R.id.tv_no_commission_msg)
    public TextView tv_no_commission_msg;
    @BindView(R.id.pcac)
    public PercentCircleAntiClockwise pcac;
    @BindView(R.id.rv_chip)
    public RecyclerView rv_chip;
    @BindView(R.id.rl_p_dragon_bonus)
    public RelativeLayout rl_p_dragon_bonus;
    @BindView(R.id.rl_player_pair)
    public RelativeLayout rl_player_pair;
    @BindView(R.id.rl_banker_pair)
    public RelativeLayout rl_banker_pair;
    @BindView(R.id.rl_b_dragon_bonus)
    public RelativeLayout rl_b_dragon_bonus;
    @BindView(R.id.rl_player)
    public RelativeLayout rl_player;
    @BindView(R.id.rl_tie)
    public RelativeLayout rl_tie;
    @BindView(R.id.rl_banker)
    public RelativeLayout rl_banker;

    @BindView(R.id.rl_in_time_layout)
    public RelativeLayout rl_in_time_layout;
    @BindView(R.id.gv_left)
    public GridView gv_left;
    @BindView(R.id.gv_right_top)
    public GridView gv_right_top;
    @BindView(R.id.gv_right_middle)
    public GridView gv_right_middle;
    @BindView(R.id.gv_right_bottom_1)
    public GridView gv_right_bottom_1;
    @BindView(R.id.gv_right_bottom_2)
    public GridView gv_right_bottom_2;
    @BindView(R.id.rl_statistic)
    public RelativeLayout rl_statistic;
    @BindView(R.id.tv_in_game_total_value)
    public TextView tv_in_game_total_value;
    @BindView(R.id.tv_in_game_banker_value)
    public TextView tv_in_game_banker_value;
    @BindView(R.id.tv_in_game_player_value)
    public TextView tv_in_game_player_value;
    @BindView(R.id.tv_in_game_tie_value)
    public TextView tv_in_game_tie_value;
    @BindView(R.id.tv_in_game_banker_pair_value)
    public TextView tv_in_game_banker_pair_value;
    @BindView(R.id.tv_in_game_player_pair_value)
    public TextView tv_in_game_player_pair_value;
    @BindView(R.id.rl_banker_to_player)
    public RelativeLayout rl_banker_to_player;
    @BindView(R.id.iv_banker_to_player_1)
    public ImageView iv_banker_to_player_1;
    @BindView(R.id.iv_banker_to_player_2)
    public ImageView iv_banker_to_player_2;
    @BindView(R.id.iv_banker_to_player_3)
    public ImageView iv_banker_to_player_3;
    @BindView(R.id.rl_player_to_banker)
    public RelativeLayout rl_player_to_banker;
    @BindView(R.id.iv_player_to_banker_1)
    public ImageView iv_player_to_banker_1;
    @BindView(R.id.iv_player_to_banker_2)
    public ImageView iv_player_to_banker_2;
    @BindView(R.id.iv_player_to_banker_3)
    public ImageView iv_player_to_banker_3;
    @BindView(R.id.rl_ex_in)
    public RelativeLayout rl_ex_in;
    @BindView(R.id.iv_ex)
    public ImageView iv_ex;
    @BindView(R.id.iv_in)
    public ImageView iv_in;

    @BindView(R.id.rl_video_select)
    public RelativeLayout rl_video_select;
    @BindView(R.id.iv_video_select)
    public ImageView iv_video_select;

    @Override
    protected void initView() {
        Util.getInstance().init(this);
        initEvent();
        BaccaratLiveControler.getInstance().init(this);
        BaccaratLiveControler.getInstance().copyWidget(tv_switch_on, tv_switch_off, tv_no_commission_msg, rl_dragon_bonus, rv_chip,
                rl_p_dragon_bonus, rl_player_pair, rl_banker_pair, rl_b_dragon_bonus, rl_player, rl_tie, rl_banker
        );
        BaccaratLiveInTimeControler.getInstance().init(this);
        BaccaratLiveInTimeControler.getInstance().copyWidget(rl_in_time_layout, gv_left, gv_right_top, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, rl_statistic, tv_in_game_total_value, tv_in_game_banker_value, tv_in_game_player_value,
                tv_in_game_tie_value, tv_in_game_banker_pair_value, tv_in_game_player_pair_value, rl_banker_to_player, iv_banker_to_player_1, iv_banker_to_player_2, iv_banker_to_player_3,
                rl_player_to_banker, iv_player_to_banker_1, iv_player_to_banker_2, iv_player_to_banker_3, rl_ex_in, iv_ex, iv_in);

        BaccaratLiveAnimControler.getInstance().init(this);
        BaccaratLiveAnimControler.getInstance().copyWidget(tv_table_limits, rl_in_time_layout, iv_in, iv_ex, rl_video_select, iv_video_select, tvv_1,root_video,ll_back);
        //rtmp://player.daniulive.com:1935/hls/stream
        //rtmp://192.168.0.120:1935/live/stream 本地 ok
        //rtmp://live.hkstv.hk.lxdns.com/live/hks 香港电视ok
        //rtmp://player.daniulive.com:1935/hls/stream
        //rtmp://119.188.246.217:1935/live/stream
        //rtmp://119.188.246.217/live/room8
//        startPlay("rtmp://119.188.246.217/live/room8", tvv_1, mLivePlayer0, mPlayRtmp);
    }

    private void initEvent() {

//        pcac.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pcac.setTargetPercent(0);
//                pcac.reInitView();
//            }
//        });
    }
}
