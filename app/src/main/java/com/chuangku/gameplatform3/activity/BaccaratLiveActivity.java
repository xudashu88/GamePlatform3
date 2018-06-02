package com.chuangku.gameplatform3.activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseActivity;
import com.chuangku.gameplatform3.base.Constant;
import com.chuangku.gameplatform3.controler.BaccaratLiveControler;
import com.chuangku.gameplatform3.widget.PercentCircleAntiClockwise;
import com.gangbeng.basemodule.utils.SharedPreUtil;
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
    @BindView(R.id.pcac)
    public PercentCircleAntiClockwise pcac;
    @BindView(R.id.rv_chip)
    public RecyclerView rv_chip;
    private Context mContext;
    private TXLivePlayer mLivePlayer0 = null;
    private int mPlayRtmp = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;//RTMP推流类型
    private LayoutInflater layoutInflater;

    @Override
    protected void initView() {
        mContext = BaccaratLiveActivity.this;
        Util.getInstance().init(this);
        layoutInflater = getWindow().getLayoutInflater();
        if (mLivePlayer0 == null) {
            mLivePlayer0 = new TXLivePlayer(mContext);
        }
        //rtmp://player.daniulive.com:1935/hls/stream
        //rtmp://192.168.0.120:1935/live/stream 本地 ok
        //rtmp://live.hkstv.hk.lxdns.com/live/hks 香港电视ok
        //rtmp://player.daniulive.com:1935/hls/stream
        //rtmp://119.188.246.217:1935/live/stream
        //rtmp://119.188.246.217/live/room1
//        startPlay("rtmp://live.hkstv.hk.lxdns.com/live/hks",tvv_1,mLivePlayer0,mPlayRtmp);
        initEvent();
        BaccaratLiveControler.getInstance().init(this);
        BaccaratLiveControler.getInstance().copyWidget(rv_chip);
    }

    private void initEvent() {
        rl_dragon_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDragonHelpDialog();
            }
        });
        int toggle_on = SharedPreUtil.getInstance(this).getInt(Constant.COMMISSION_ON);
        int toggle_off = SharedPreUtil.getInstance(this).getInt(Constant.COMMISSION_OFF);
        if (0 == toggle_on || toggle_on == 1) {
            tv_switch_on.setVisibility(View.VISIBLE);
            tv_switch_off.setVisibility(View.GONE);
        } else {
            tv_switch_off.setVisibility(View.VISIBLE);
            tv_switch_on.setVisibility(View.GONE);
        }
        if (toggle_off == 1) {
            tv_switch_on.setVisibility(View.GONE);
            tv_switch_off.setVisibility(View.VISIBLE);
        } else {
            tv_switch_off.setVisibility(View.GONE);
            tv_switch_on.setVisibility(View.VISIBLE);
        }
        tv_switch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_switch_on.setVisibility(View.GONE);
                tv_switch_off.setVisibility(View.VISIBLE);
                SharedPreUtil.getInstance(BaccaratLiveActivity.this).saveParam(Constant.COMMISSION_ON, 1);
            }
        });
        tv_switch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_switch_on.setVisibility(View.VISIBLE);
                tv_switch_off.setVisibility(View.GONE);
                SharedPreUtil.getInstance(BaccaratLiveActivity.this).saveParam(Constant.COMMISSION_OFF, 1);
            }
        });
        pcac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcac.setTargetPercent(0);
                pcac.reInitView();
            }
        });
    }



    private void initDragonHelpDialog() {
        View layout12 = layoutInflater.inflate(R.layout.dialog_bonus, null);
         Util.getInstance().setDialog(mContext, R.style.dialog, layout12, 0.65f, 0f, 0.6f, 0, 0, Gravity.CENTER, Gravity.CENTER_HORIZONTAL);
    }

    private void startPlay(String playUrl, TXCloudVideoView mPlayerView, TXLivePlayer mLivePlayer, int playType) {
        mLivePlayer.setPlayerView(mPlayerView);
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mLivePlayer.startPlay(playUrl, playType);
    }

    private void stopPlay(TXLivePlayer mLivePlayer, TXCloudVideoView mPlayerView) {
        if (mLivePlayer != null) {
            mLivePlayer.stopRecord();
            mLivePlayer.stopPlay(true);
            mPlayerView.onDestroy();
        }
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay(mLivePlayer0, tvv_1);
    }
}
