package com.chuangku.gameplatform3.controler;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.activity.BaccaratLiveActivity;
import com.chuangku.gameplatform3.utils.ScreenUtil;
import com.chuangku.gameplatform3.base.Constant;
import com.chuangku.gameplatform3.widget.popup.PopupWindow.CommonPopupWindow;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 功能：桌台限红下拉和视频选项上拉
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class BaccaratLiveAnimControler implements CommonPopupWindow.ViewInterface {
    private Context mContext;
    private static BaccaratLiveAnimControler instance = null;
    private CommonPopupWindow popupWindow;
    private DisplayMetrics displayMetrics;

    TextView tv_table_limits;
    RelativeLayout rl_in_time_layout;
    ImageView iv_in;
    ImageView iv_ex;
    RelativeLayout rl_video_select;
    ImageView iv_video_select;
    TXCloudVideoView tvv_1;
    RelativeLayout root_video;
    LinearLayout ll_back;

    private boolean isExpand;
    private int isVideoSelect = 0;
    private TXLivePlayer mLivePlayer0 = null;
    private int mPlayRtmp = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;//RTMP推流类型
    private int mPlayFlv = TXLivePlayer.PLAY_TYPE_LIVE_FLV;//RTMP推流类型
    private TXLivePlayConfig mPlayConfig;

    public void init(Context context) {
        mContext = context;
        displayMetrics = context.getResources().getDisplayMetrics();

    }

    public static BaccaratLiveAnimControler getInstance() {
        if (null == instance) {
            instance = new BaccaratLiveAnimControler();
        }
        return instance;
    }

    public void copyWidget(TextView tv_table_limits, RelativeLayout rl_in_time_layout, ImageView iv_in, ImageView iv_ex,
                           RelativeLayout rl_video_select, ImageView iv_video_select, TXCloudVideoView tvv_1, RelativeLayout root_video, LinearLayout ll_back) {
        this.tv_table_limits = tv_table_limits;
        this.rl_in_time_layout = rl_in_time_layout;
        this.iv_in = iv_in;
        this.iv_ex = iv_ex;
        this.rl_video_select = rl_video_select;
        this.iv_video_select = iv_video_select;
        this.tvv_1 = tvv_1;
        this.root_video = root_video;
        this.ll_back = ll_back;
        if (mLivePlayer0 == null) {
            mLivePlayer0 = new TXLivePlayer(mContext);

            mPlayConfig = new TXLivePlayConfig();
            mPlayConfig.setAutoAdjustCacheTime(true);
            mPlayConfig.setMinAutoAdjustCacheTime(1);
            mPlayConfig.setMaxAutoAdjustCacheTime(1);//设置小于1s的延迟

            mLivePlayer0.setConfig(mPlayConfig);
            mLivePlayer0.enableHardwareDecode(true);//true 开启硬件加速 false 软件加速
        }
        //硬件加速，极速缓存
        //https://3891.liveplay.myqcloud.com/live/3891_www1234.flv
        //rtmp://119.188.246.217/live/room8 or rtmp://119.188.246.217/live/room9 room9比room8快 创酷科技
        startPlay("rtmp://live.hkstv.hk.lxdns.com/live/hks", tvv_1, mLivePlayer0, mPlayRtmp);
        initEvent();
    }

    private void initEvent() {
        tv_table_limits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInTimeLayout();
                downPopupWindow(v);
//                Util.getInstance().init(mContext);
//                Util.getInstance().hideSystemNavigationBar();
            }
        });
        rl_video_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVideoSelect == 0) {
                    iv_video_select.setRotation(180f);
                    isVideoSelect = 1;
                } else {
                    iv_video_select.setRotation(0f);
                    isVideoSelect = 0;
                }
//                Util.getInstance().init(mContext);
//                Util.getInstance().hideSystemNavigationBar();
                upPopupWindow(v);
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay(mLivePlayer0, tvv_1);
                ((BaccaratLiveActivity) mContext).finish();
            }
        });
    }

    public void upPopupWindow(View v) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.pop_video_select)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimUp1)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();

        popupWindow.showAsDropDown(v, 0, -(popupWindow.getHeight() + v.getMeasuredHeight()));
    }

    private void downPopupWindow(View v) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.pop_bet_limit)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setOutsideTouchable(true)
                .create();
        popupWindow.showAsDropDown(v);
    }

    private void hideInTimeLayout() {
        isExpand = SharedPreUtil.getInstance(mContext).getBoolean(Constant.IS_EXPAND);
        if (isExpand == true) {
            //-1520f
//            if (displayMetrics.widthPixels < 1800) {
//                BaccaratLiveInTimeControler.getInstance().slideView(0, -((float) (displayMetrics.widthPixels * 0.848)), rl_in_time_layout);
//            } else if (displayMetrics.widthPixels < 2000) {
//                BaccaratLiveInTimeControler.getInstance().slideView(0, -((float) (displayMetrics.widthPixels * 0.795)), rl_in_time_layout);
//            } else if (displayMetrics.widthPixels < 2100) {
//                BaccaratLiveInTimeControler.getInstance().slideView(0, -((float) (displayMetrics.widthPixels * 0.733)), rl_in_time_layout);
//            } else {
//                BaccaratLiveInTimeControler.getInstance().slideView(0, -((float) (displayMetrics.widthPixels * 0.828)), rl_in_time_layout);
//            }
            BaccaratLiveInTimeControler.getInstance().slideView(0, -(ScreenUtil.dip2px(506f)), rl_in_time_layout);
            iv_in.setVisibility(View.GONE);
            iv_ex.setVisibility(View.VISIBLE);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, false);
        }
    }

    public void startPlay(String playUrl, TXCloudVideoView mPlayerView, TXLivePlayer mLivePlayer, int playType) {
        mLivePlayer.setPlayerView(mPlayerView);
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mLivePlayer.startPlay(playUrl, playType);
    }

    public void stopPlay(TXLivePlayer mLivePlayer, TXCloudVideoView mPlayerView) {
        root_video.setBackgroundResource(R.drawable.bg_baccarat_video);
        if (mLivePlayer != null) {
            mLivePlayer.stopRecord();
            mLivePlayer.stopPlay(true);
//            mPlayerView.onDestroy();
        }
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.pop_bet_limit:
                break;
            case R.layout.pop_video_select:
                RelativeLayout rl_hd = (RelativeLayout) view.findViewById(R.id.rl_hd);
                RelativeLayout rl_ld = (RelativeLayout) view.findViewById(R.id.rl_ld);
                TextView tv_close = (TextView) view.findViewById(R.id.tv_close);
                final String str = mContext.getResources().getString(R.string.change_channel_resolution);

                rl_hd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPlay("rtmp://119.188.246.217/live/room8", tvv_1, mLivePlayer0, mPlayRtmp);
                        String sFinal = String.format(str, "HD");
                        ToastUtil.showToast(sFinal);
                        popupWindow.dismiss();
                        iv_video_select.setRotation(0f);
                        isVideoSelect = 0;
                    }
                });
                rl_ld.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startPlay("rtmp://119.188.246.217/live/room9", tvv_1, mLivePlayer0, mPlayRtmp);
                        String sFinal = String.format(str, "LD");
                        ToastUtil.showToast(sFinal);
                        popupWindow.dismiss();
                        iv_video_select.setRotation(0f);
                        isVideoSelect = 0;
                    }
                });
                tv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopPlay(mLivePlayer0, tvv_1);
                        ToastUtil.showToast(mContext.getResources().getString(R.string.video_close));
                        popupWindow.dismiss();
                        iv_video_select.setRotation(0f);
                        isVideoSelect = 0;
                    }
                });
                break;
        }
    }
}
