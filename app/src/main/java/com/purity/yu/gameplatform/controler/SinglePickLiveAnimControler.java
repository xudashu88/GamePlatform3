package com.purity.yu.gameplatform.controler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daniulive.smartplayer.SmartPlayerJniV2;
import com.eventhandle.NTSmartEventCallbackV2;
import com.eventhandle.NTSmartEventID;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.google.gson.Gson;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.BetRecordListActivity;
import com.purity.yu.gameplatform.activity.MessageListActivity;
import com.purity.yu.gameplatform.activity.QuotaRecordListActivity;
import com.purity.yu.gameplatform.activity.SettingActivity;
import com.purity.yu.gameplatform.activity.SinglePickListActivity;
import com.purity.yu.gameplatform.activity.SinglePickLiveActivity;
import com.purity.yu.gameplatform.activity.SuggestionActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.LocalHtmlWebViewActivity;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.widget.popup.PopupWindow.CommonPopupWindow;
import com.videoengine.NTRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：桌台限红下拉和视频选项上拉
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class SinglePickLiveAnimControler implements CommonPopupWindow.ViewInterface {
    private static final String TAG = "DtLiveAnimControler";
    private Context mContext;
    private static SinglePickLiveAnimControler instance = null;
    private CommonPopupWindow popupWindow;
    private DisplayMetrics displayMetrics;

    TextView tv_table_limits;
    RelativeLayout rl_in_time_layout;
    ImageView iv_in;
    RelativeLayout rl_video_select;
    ImageView iv_video_select;
    SurfaceView sv;
    RelativeLayout root_video;
    LinearLayout ll_back;
    ImageView iv_set;
    RelativeLayout rl_video_select1;
    ImageView iv_video_select1;
    private int isExpand;

    private SurfaceView sSurfaceView = null;
    private long playerHandle = 0;
    private SmartPlayerJniV2 libPlayer = null;
    private boolean isHardwareDecoder = Constant.DANIU_HARDWARE;//true 硬件解码  false 软件解码

    private int playBuffer = Constant.DANIU_BUFFER; // 默认200ms 已改为0
    private boolean isLowLatency = true; // 超低延时，默认不开启  已改为开启
    private boolean isFastStartup = true; // 是否秒开, 默认true
    private int rotate_degrees = 0;//视频顺时针旋转角度

    private boolean switchUrlFlag = false;
    //    private String switchURL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";//默认播放
    private String switchURL = Constant.ROOM8;//默认播放
    private boolean isPlaying = false;//默认未播放

    private List<Baccarat> baccaratList = new ArrayList<>();
    private String hVideo = "";
    private String lVideo = "";
    private Baccarat mBaccarat;

//    static {
//        System.loadLibrary("SmartPlayer");//加载so库
//    }

    public void init(Context context) {
        mContext = context;
        displayMetrics = context.getResources().getDisplayMetrics();
        layoutInflater = ((SinglePickLiveActivity) mContext).getWindow().getLayoutInflater();
    }

    //---------------------daniu start------------------------
    private boolean CreateView() {
        sSurfaceView = sv;
        if (sSurfaceView == null) {
            sSurfaceView = NTRenderer.CreateRenderer(mContext, false);
        }
        if (sSurfaceView == null) {
            return false;
        }
        return true;
    }

    private void autoPlayer(String switchURL1) {
        InitAndSetConfig(switchURL1);//初始化参数
        libPlayer.SmartPlayerSetSurface(playerHandle, sv);
        libPlayer.SmartPlayerSetAudioOutputType(playerHandle, 0);
        if (isHardwareDecoder) {
            libPlayer.SetSmartPlayerVideoHWDecoder(playerHandle, isHardwareDecoder ? 1 : 0);
        }
        libPlayer.SmartPlayerSetLowLatencyMode(playerHandle, isLowLatency ? 1 : 0);
        libPlayer.SmartPlayerSetRotation(playerHandle, rotate_degrees);
        int iPlaybackRet1 = libPlayer.SmartPlayerStartPlay(playerHandle);//playerHandle 547740596352 547740595584
        if (iPlaybackRet1 != 0) { //非0  就不能直接播放
            return;
        }
        isPlaying = true;
    }

    private void start(String switchURL1) {
        if (playerHandle != 0) {
            libPlayer.SmartPlayerSwitchPlaybackUrl(playerHandle, switchURL1);
        }
    }

    private void InitAndSetConfig(String switchURL) {
        playerHandle = libPlayer.SmartPlayerOpen(mContext);
        if (switchURL == null) {
            initNullBackground();
            return;
        }
        if (playerHandle == 0) {
            return;
        }
        libPlayer.SetSmartPlayerEventCallbackV2(playerHandle, new EventHandleV2());
        libPlayer.SmartPlayerSetBuffer(playerHandle, playBuffer);
        libPlayer.SmartPlayerSetFastStartup(playerHandle, isFastStartup ? 1 : 0);

        libPlayer.SmartPlayerSetUrl(playerHandle, hVideo);
    }

    class EventHandleV2 implements NTSmartEventCallbackV2 {
        @Override
        public void onNTSmartEventCallbackV2(long handle, int id, long param1, long param2, String param3, String param4, Object param5) {

            switch (id) {
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STARTED:
                    Log.i(TAG, "开始。。");
                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTING:
                    Log.i(TAG, "连接中。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTION_FAILED:
                    Log.i(TAG, "连接失败。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTED:
                    Log.i(TAG, "连接成功。。");
                    if (SharedPreUtil.getInstance(mContext).getInt(Constant.VIDEO_PLAYING) == 0) {
                        initDefaultBackground();
                    } else
                        initNullBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_DISCONNECTED:
                    Log.i(TAG, "连接断开。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STOP:
                    Log.i(TAG, "停止播放。。");
                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_RESOLUTION_INFO:
                    Log.i(TAG, "分辨率信息: width: " + param1 + ", height: " + param2 + "   isPlaying=");
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.VIDEO_PLAYING, 1);
                    initNullBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_NO_MEDIADATA_RECEIVED:
                    Log.i(TAG, "收不到媒体数据，可能是url错误。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_SWITCH_URL:
                    Log.i(TAG, "切换播放URL。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_START_BUFFERING:
                    Log.i(TAG, "Start_Buffering");
                    break;

                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_BUFFERING:
                    Log.i(TAG, "Buffering:" + param1 + "%");
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STOP_BUFFERING:
                    Log.i(TAG, "Stop_Buffering");
                    break;
            }
        }
    }

    private void initDefaultBackground() {
        ((SinglePickLiveActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sSurfaceView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_baccarat_video));
            }
        });
    }

    private void initNullBackground() {
        ((SinglePickLiveActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sSurfaceView.setBackground(null);
            }
        });
    }

    //---------------------daniu end------------------------
    public static SinglePickLiveAnimControler getInstance() {
        if (null == instance) {
            instance = new SinglePickLiveAnimControler();
        }
        return instance;
    }

    public void copyWidget(TextView tv_table_limits, RelativeLayout rl_in_time_layout, ImageView iv_in,
                           RelativeLayout rl_video_select, ImageView iv_video_select, SurfaceView sv, RelativeLayout root_video, LinearLayout ll_back, ImageView iv_set, Baccarat baccarat,
                           RelativeLayout rl_video_select1, ImageView iv_video_select1) {
        this.tv_table_limits = tv_table_limits;
        this.rl_in_time_layout = rl_in_time_layout;
        this.iv_in = iv_in;
        this.rl_video_select = rl_video_select;
        this.iv_video_select = iv_video_select;
        this.sv = sv;
        this.root_video = root_video;
        this.ll_back = ll_back;
        this.iv_set = iv_set;
        this.mBaccarat = baccarat;
        this.rl_video_select1 = rl_video_select1;
        this.iv_video_select1 = iv_video_select1;
        libPlayer = new SmartPlayerJniV2();
        initBacDataVideo(mBaccarat);
        initEvent();
    }

    public void initBacDataVideo(Baccarat baccarat) {
        if (baccarat != null && baccarat.rtmp != null && baccarat.rtmp.length != 0) {
            String[] _url = baccarat.rtmp;
            hVideo = "rtmp://" + _url[1];
//            hVideo = "rtmp://live.hkstv.hk.lxdns.com/live/hks1";
            lVideo = "rtmp://" + _url[0];
            LogUtil.i("视频地址" + hVideo + " lVideo=" + lVideo);
        }
        CreateView();
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.CLOSE_ALL_VIDEO) == 0) {
            autoPlayer(hVideo);//在控件上播放
        } else {
            initDefaultBackground();
        }
    }

    private void initEvent() {
        tv_table_limits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInTimeLayout();
                downPopupWindow(v);
            }
        });
        rl_video_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_video_select.setRotation(180f);//图片向下旋转
                upPopupWindow(v);
            }
        });
        rl_video_select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_video_select1.setRotation(0f);//图片向下旋转
                upPopupWindow(v, iv_video_select1);
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libPlayer.SmartPlayerStopPlay(playerHandle);
                isPlaying = false;
                SinglePickLiveSocketController.getInstance().disconnectSocket();
                SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 0);//点击退出停止播放
                SharedPreUtil.getInstance(mContext).saveParam(Constant.VIDEO_PLAYING, 0);
                ((SinglePickLiveActivity) mContext).finish();
                mContext.startActivity(new Intent(mContext, SinglePickListActivity.class));
            }
        });
        iv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 1);//点击设置说明已经进入了
                SharedPreUtil.getInstance(mContext).saveParam(Constant.WHERE_SET, 4);
                initMenu(mContext);
            }
        });
    }

    private LayoutInflater layoutInflater;

    public void initMenu(final Context mContext) {
        final View layout = layoutInflater.inflate(R.layout.dialog_menu, null);
        final AlertDialog menu = Util.setDialog(mContext, R.style.dialog, layout, 0.21f, 1f, 0.5f, 0, 0, Gravity.RIGHT, Gravity.TOP, R.style.AnimRight);
        RelativeLayout rl_menu_records = (RelativeLayout) layout.findViewById(R.id.rl_menu_records);
        RelativeLayout rl_menu_setting = (RelativeLayout) layout.findViewById(R.id.rl_menu_setting);
        RelativeLayout rl_menu_suggestion = (RelativeLayout) layout.findViewById(R.id.rl_menu_suggestion);
        RelativeLayout rl_menu_game_rule = (RelativeLayout) layout.findViewById(R.id.rl_menu_game_rule);
        RelativeLayout rl_menu_message = (RelativeLayout) layout.findViewById(R.id.rl_menu_message);
        RelativeLayout rl_menu_exit = (RelativeLayout) layout.findViewById(R.id.rl_menu_exit);
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.WHERE_SET) != 1) {
            rl_menu_exit.setVisibility(View.GONE);
        }
        rl_menu_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View layout = layoutInflater.inflate(R.layout.dialog_menu_records, null);
                final AlertDialog records = Util.setDialog(mContext, R.style.dialog, layout, 0.21f, 1f, 0.5f, 0, 0, Gravity.RIGHT, Gravity.TOP, R.style.AnimRight);
                RelativeLayout rl_menu_title = (RelativeLayout) layout.findViewById(R.id.rl_menu_title);
                RelativeLayout rl_menu_bet_record = (RelativeLayout) layout.findViewById(R.id.rl_menu_bet_record);
                RelativeLayout rl_menu_quota_record = (RelativeLayout) layout.findViewById(R.id.rl_menu_quota_record);
                records.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        records.dismiss();
                        menu.dismiss();
                    }
                });
                rl_menu_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        records.hide();
                    }
                });
                rl_menu_bet_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        libPlayer.SmartPlayerStopPlay(playerHandle);
                        mContext.startActivity(new Intent(mContext, BetRecordListActivity.class));
                    }
                });
                rl_menu_quota_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        libPlayer.SmartPlayerStopPlay(playerHandle);
                        mContext.startActivity(new Intent(mContext, QuotaRecordListActivity.class));
                    }
                });

            }
        });
        rl_menu_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libPlayer.SmartPlayerStopPlay(playerHandle);
                mContext.startActivity(new Intent(mContext, SettingActivity.class));
            }
        });
        rl_menu_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libPlayer.SmartPlayerStopPlay(playerHandle);
                mContext.startActivity(new Intent(mContext, SuggestionActivity.class));
            }
        });
        rl_menu_game_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libPlayer.SmartPlayerStopPlay(playerHandle);
                mContext.startActivity(new Intent(mContext, LocalHtmlWebViewActivity.class).putExtra("selectBac", 0));
            }
        });
        rl_menu_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libPlayer.SmartPlayerStopPlay(playerHandle);
                mContext.startActivity(new Intent(mContext, MessageListActivity.class));
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
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_video_select.setRotation(0f);//图片向上旋转
            }
        });
    }

    public void upPopupWindow(View v, final ImageView iv) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.pop_video_select1)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();

//        popupWindow.showAsDropDown(v, 0, -(popupWindow.getHeight() + v.getMeasuredHeight()));
        popupWindow.showAsDropDown(v);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv.setRotation(180f);//图片向上旋转
            }
        });
    }

    private void downPopupWindow(View v) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.pop_single_limit)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setOutsideTouchable(true)
                .create();
        TextView tv_table_limit_bank = (TextView) popupWindow.getView().findViewById(R.id.tv_table_limit_bank);
        TextView tv_bet_limit_single = (TextView) popupWindow.getView().findViewById(R.id.tv_bet_limit_single);
        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.MIN_BET))) {
            String minBet = SharedPreUtil.getInstance(mContext).getString(Constant.MIN_BET);
            String maxBet = SharedPreUtil.getInstance(mContext).getString(Constant.MAX_BET);
            String betLimit = SharedPreUtil.getInstance(mContext).getString(Constant.BET_LIMIT);
            int[] minBetInt = new Gson().fromJson(minBet, int[].class);
            int[] maxBetInt = new Gson().fromJson(maxBet, int[].class);
            int[] betLimitInt = new Gson().fromJson(betLimit, int[].class);
            String banker = String.valueOf(minBetInt[0]) + "-" + String.valueOf(maxBetInt[0]);
            String betLimitStr = String.valueOf(betLimitInt[0]);
            tv_table_limit_bank.setText(banker);
            tv_bet_limit_single.setText(betLimitStr);
        }
        popupWindow.showAsDropDown(v);
    }

    private void hideInTimeLayout() {
        isExpand = SharedPreUtil.getInstance(mContext).getInt(Constant.IS_EXPAND);
        if (isExpand == 0) {
            LogUtil.i("displayMetrics.widthPixels=" + displayMetrics.widthPixels);
//mContext.getResources().getDimensionPixelSize(R.dimen.font12)  ScreenUtil.dip2px(365f)
//            if (displayMetrics.widthPixels > 2100) {//小米 max 2160
//                SinglePickLiveInTimeController.getInstance().slideView(0, -(mContext.getResources().getDimensionPixelSize(R.dimen.unit325)), rl_in_time_layout);//华为M5平板 676f
//            } else
            SinglePickLiveInTimeController.getInstance().slideView(0, -(mContext.getResources().getDimensionPixelSize(R.dimen.unit325)), rl_in_time_layout);//本来是向右平移，结果右边为负数，就以看到的点为坐标原点，向左平移
            iv_in.setRotation(0);
//            rl_down_ex_in.setVisibility(View.GONE);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 1);
        }
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.pop_bac_limit:
                break;
            case R.layout.pop_video_select1:
                RelativeLayout rl_telecom = (RelativeLayout) view.findViewById(R.id.rl_telecom);
                RelativeLayout rl_mobile = (RelativeLayout) view.findViewById(R.id.rl_mobile);
                RelativeLayout rl_lan = (RelativeLayout) view.findViewById(R.id.rl_lan);
                TextView tv_close1 = (TextView) view.findViewById(R.id.tv_close);
                tv_close1.setVisibility(View.GONE);
                rl_telecom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//电信
//                        mBaccarat.dxRtmp=null;
//                        mBaccarat.dxRtmp=new String[2];
//                        mBaccarat.dxRtmp[0]="39.98.177.215/live/lh01b";
//                        mBaccarat.dxRtmp[1]="39.98.177.215/live/lh01a";
                        if (mBaccarat.dxRtmp.length != 0 && !TextUtils.isEmpty(mBaccarat.dxRtmp[0])) {
                            String[] _url = mBaccarat.dxRtmp;
                            hVideo = "rtmp://" + _url[1];
                            lVideo = "rtmp://" + _url[0];
                            switchPlay(hVideo);
                        } else {
                            ToastUtil.show(mContext, "没有视频！");
                        }
                        popupWindow.dismiss();
                        iv_video_select1.setRotation(0f);
                    }
                });

                rl_mobile.setOnClickListener(new View.OnClickListener() {//移动
                    @Override
                    public void onClick(View v) {
                        if (mBaccarat.ydRtmp.length != 0 && !TextUtils.isEmpty(mBaccarat.ydRtmp[0])) {
                            String[] _url = mBaccarat.ydRtmp;
                            hVideo = "rtmp://" + _url[1];
                            lVideo = "rtmp://" + _url[0];
                            switchPlay(hVideo);
                        } else {
                            ToastUtil.show(mContext, "没有视频！");
                        }
                        popupWindow.dismiss();
                        iv_video_select1.setRotation(0f);
                    }
                });
                rl_lan.setOnClickListener(new View.OnClickListener() {//局域网
                    @Override
                    public void onClick(View v) {
                        if (mBaccarat.rtmp.length != 0 && !TextUtils.isEmpty(mBaccarat.rtmp[0])) {
                            String[] _url = mBaccarat.rtmp;
                            hVideo = "rtmp://" + _url[1];
                            lVideo = "rtmp://" + _url[0];
                            switchPlay(hVideo);
                        } else {
                            ToastUtil.show(mContext, "没有视频！");
                        }
                        popupWindow.dismiss();
                        iv_video_select1.setRotation(0f);
                    }
                });
                break;
            case R.layout.pop_video_select:
                RelativeLayout rl_hd = (RelativeLayout) view.findViewById(R.id.rl_hd);
                RelativeLayout rl_ld = (RelativeLayout) view.findViewById(R.id.rl_ld);
                TextView tv_close = (TextView) view.findViewById(R.id.tv_close);
                final String str = mContext.getResources().getString(R.string.change_channel_resolution);
                rl_hd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPlay(hVideo);
                        String sFinal = String.format(str, "HD");
                        ToastUtil.showToast(mContext, sFinal);
                        popupWindow.dismiss();
                        iv_video_select.setRotation(0f);
                    }
                });
                rl_ld.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchPlay(lVideo);
                        String sFinal = String.format(str, "LD");
                        ToastUtil.showToast(mContext, sFinal);
                        popupWindow.dismiss();
                        iv_video_select.setRotation(0f);
                    }
                });
                tv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //停止播放
                        libPlayer.SmartPlayerStopPlay(playerHandle);
                        isPlaying = false;
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.video_close));
                        popupWindow.dismiss();
                        iv_video_select.setRotation(0f);
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.CLOSE_ALL_VIDEO, 1);//关闭之后，所有视频启动时关闭状态
                    }
                });
                break;
        }
    }

    private void switchPlay(String url) {
        if (isPlaying) {
            start(url);
        } else {
            autoPlayer(url);
        }
        SharedPreUtil.getInstance(mContext).saveParam(Constant.CLOSE_ALL_VIDEO, 0);//点击除了关闭视频按钮，代表打开状态
    }
}
