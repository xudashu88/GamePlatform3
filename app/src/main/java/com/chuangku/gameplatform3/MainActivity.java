package com.chuangku.gameplatform3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

public class MainActivity extends AppCompatActivity {

    private TXCloudVideoView tvv_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvv_1 = (TXCloudVideoView) findViewById(R.id.tvv_1);
        initView();
    }

    private TXLivePlayer mLivePlayer0 = null;
    private int mPlayRtmp = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;//RTMP推流类型
    protected void initView() {
        if (mLivePlayer0 == null) {
            mLivePlayer0 = new TXLivePlayer(this);
        }
        //rtmp://player.daniulive.com:1935/hls/stream
        //rtmp://192.168.0.120:1935/live/stream 本地 ok
        //rtmp://live.hkstv.hk.lxdns.com/live/hks 香港电视ok
        //rtmp://player.daniulive.com:1935/hls/stream
        //rtmp://119.188.246.217:1935/live/stream
        //rtmp://119.188.246.217/live/room1
        startPlay("rtmp://live.hkstv.hk.lxdns.com/live/hks",tvv_1,mLivePlayer0,mPlayRtmp);
    }

    private void startPlay(String playUrl, TXCloudVideoView mPlayerView, TXLivePlayer mLivePlayer, int playType) {
        mLivePlayer.setPlayerView(mPlayerView);
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mLivePlayer.startPlay(playUrl, playType);
    }
}
