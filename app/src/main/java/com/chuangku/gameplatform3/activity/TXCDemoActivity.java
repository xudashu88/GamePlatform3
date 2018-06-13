package com.chuangku.gameplatform3.activity;

import android.content.Context;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseActivity;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import butterknife.BindView;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/24.
 */
@ContentView(R.layout.activity_txc_demo)
public class TXCDemoActivity extends BaseActivity {
    @BindView(R.id.tvv_1)
    TXCloudVideoView tvv_1;
    private Context mContext;
    private TXLivePlayer mLivePlayer0 = null;
    private int mPlayRtmp = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;//RTMP推流类型
    @Override
    protected void initView() {
        mContext=TXCDemoActivity.this;
        if (mLivePlayer0 == null) {
            mLivePlayer0 = new TXLivePlayer(mContext);
        }
        //rtmp://player.daniulive.com:1935/hls/stream
        //rtmp://192.168.0.120:1935/live/stream 本地 ok
        //rtmp://live.hkstv.hk.lxdns.com/live/hks 香港电视ok
        //rtmp://player.daniulive.com:1935/hls/stream
        //rtmp://119.188.246.217:1935/live/stream
        //rtmp://119.188.246.217/live/room1
        //rtmp://ledrp.agingames.com:1936/record/
        //rtmp://rcpb.11hk.com/record/ 白屏
        startPlay("rtmp://live.hkstv.hk.lxdns.com/live/hks",tvv_1,mLivePlayer0,mPlayRtmp);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlay(mLivePlayer0,tvv_1);
    }
}
