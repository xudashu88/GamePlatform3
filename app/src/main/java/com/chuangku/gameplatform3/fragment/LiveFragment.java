package com.chuangku.gameplatform3.fragment;

import android.content.Intent;
import android.view.View;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.activity.TXCDemoActivity;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseFragment;
import com.chuangku.gameplatform3.widget.popup.PopupWindowActivity;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import butterknife.BindView;

@ContentView(R.layout.fragment_live)
public class LiveFragment extends BaseFragment {
    @BindView(R.id.tvv_1)
    TXCloudVideoView tvv_1;

    private TXLivePlayer mLivePlayer0 = null;
    private int mPlayRtmp = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;//RTMP推流类型
    @Override
    protected void initView(View view) {
        if (mLivePlayer0 == null) {
            mLivePlayer0 = new TXLivePlayer(getActivity());
        }
//        rtmp://player.daniulive.com:1935/hls/stream
//        rtmp://192.168.0.120:1935/live/stream 本地 ok
//        rtmp://live.hkstv.hk.lxdns.com/live/hks 香港电视ok
//        rtmp://player.daniulive.com:1935/hls/stream
//        rtmp://119.188.246.217:1935/live/stream
//        rtmp://119.188.246.217/live/room1
//       startPlay("rtmp://live.hkstv.hk.lxdns.com/live/hks",tvv_1,mLivePlayer0,mPlayRtmp);

//        startActivity(new Intent(getActivity(), TXCDemoActivity.class));
        startActivity(new Intent(getActivity(), PopupWindowActivity.class));
    }

    private void startPlay(String playUrl, TXCloudVideoView mPlayerView, TXLivePlayer mLivePlayer, int playType) {
        mLivePlayer.setPlayerView(mPlayerView);
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mLivePlayer.startPlay(playUrl, playType);
    }
}
