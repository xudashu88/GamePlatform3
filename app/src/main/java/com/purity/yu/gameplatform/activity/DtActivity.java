package com.purity.yu.gameplatform.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuangku.languagemodule.CommSharedUtil;
import com.chuangku.languagemodule.LanguageType;
import com.chuangku.languagemodule.MultiLanguageUtil;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daniulive.smartplayer.SmartPlayerJniV2;
import com.eventhandle.NTSmartEventCallbackV2;
import com.eventhandle.NTSmartEventID;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.google.gson.Gson;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.baccarat.YHZGridView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.LocalHtmlWebViewActivity;
import com.purity.yu.gameplatform.controler.DtSocketController;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.Liquidation;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.Algorithm;
import com.purity.yu.gameplatform.utils.AlgorithmMacau;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.MyYAnimation;
import com.purity.yu.gameplatform.utils.SoundPoolUtil;
import com.purity.yu.gameplatform.widget.ChipViewHoly;
import com.purity.yu.gameplatform.widget.FlashHelper;
import com.purity.yu.gameplatform.widget.HintDialog;
import com.purity.yu.gameplatform.widget.popup.PopupWindow.CommonPopupWindow;
import com.videoengine.NTRenderer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import io.socket.client.Socket;

@ContentView(R.layout.activity_dt)
public class DtActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {
    //https://jingyan.baidu.com/article/4dc40848e6a99ac8d946f13a.html 去水印 61.140.45.46
    //下注玩法一：老版本
    //发牌之前都可以取消下注，也可以续押
    //下注玩法二：新版本
    //和AG一样，下一次注可以确定和取消
    @BindView(R.id.tv_time)
    public TextView tv_time;
    @BindView(R.id.tv_device)
    public TextView tv_device;
    @BindView(R.id.tv_records)
    public TextView tv_records;
    @BindView(R.id.tv_game_rec)
    public TextView tv_game_rec;
    @BindView(R.id.tv_manual)
    public TextView tv_manual;
    @BindView(R.id.tv_exit)
    public TextView tv_exit;
    @BindView(R.id.tv_banker)
    public TextView tv_banker;
    @BindView(R.id.tv_player)
    public TextView tv_player;
    @BindView(R.id.tv_tie)
    public TextView tv_tie;
    @BindView(R.id.tv_sure)
    public TextView tv_sure;
    @BindView(R.id.rl_sure)
    public RelativeLayout rl_sure;
    @BindView(R.id.rl_cancel)
    public RelativeLayout rl_cancel;
    @BindView(R.id.iv_chip_10)
    public ImageView iv_chip_10;
    @BindView(R.id.iv_chip_50)
    public ImageView iv_chip_50;
    @BindView(R.id.iv_chip_100)
    public ImageView iv_chip_100;
    @BindView(R.id.iv_chip_500)
    public ImageView iv_chip_500;
    @BindView(R.id.iv_chip_2000)
    public ImageView iv_chip_2000;
    @BindView(R.id.rl_chip_100)
    public RelativeLayout rl_chip_100;

    @BindView(R.id.sv1)
    public SurfaceView sv1;
    @BindView(R.id.tv_middle_sv_l)
    public TextView tv_middle_sv_l;
    @BindView(R.id.tv_middle_sv_h)
    public TextView tv_middle_sv_h;
    @BindView(R.id.tv_big_sv_l)
    public TextView tv_big_sv_l;
    @BindView(R.id.tv_big_sv_h)
    public TextView tv_big_sv_h;
    @BindView(R.id.tv_money)
    public TextView tv_money;

    //赢后的变化
    @BindView(R.id.tv_win)
    public TextView tv_win;

    @BindView(R.id.tv_in_game_total_value)
    public TextView tv_in_game_total_value;
    @BindView(R.id.tv_in_game_banker_value)
    public TextView tv_in_game_banker_value;
    @BindView(R.id.tv_in_game_player_value)
    public TextView tv_in_game_player_value;
    @BindView(R.id.tv_in_game_tie_value)
    public TextView tv_in_game_tie_value;
    @BindView(R.id.tv_open_dragon)
    public TextView tv_open_dragon;
    @BindView(R.id.tv_open_tiger)
    public TextView tv_open_tiger;
    @BindView(R.id.tv_open_tie)
    public TextView tv_open_tie;
    @BindView(R.id.tv_ask_bank)
    public TextView tv_ask_bank;
    @BindView(R.id.tv_ask_play)
    public TextView tv_ask_play;

    @BindView(R.id.tv_room_name)
    public TextView tv_room_name;
    @BindView(R.id.tv_nickname)
    public TextView tv_nickname;
    @BindView(R.id.rl_poker)
    public RelativeLayout ll_poker;
    //实时下注积分
    @BindView(R.id.tv_play_date)
    TextView tv_play_date;
    @BindView(R.id.tv_tie_date)
    TextView tv_tie_date;
    @BindView(R.id.tv_banker_date)
    TextView tv_banker_date;
    @BindView(R.id.tv_dragon_bet_limit)
    TextView tv_dragon_bet_limit;
    @BindView(R.id.tv_dragon_bet_limit_text)
    TextView tv_dragon_bet_limit_text;
    @BindView(R.id.tv_tiger_bet_limit)
    TextView tv_tiger_bet_limit;
    @BindView(R.id.tv_tiger_bet_limit_text)
    TextView tv_tiger_bet_limit_text;
    @BindView(R.id.tv_tie_bet_limit)
    TextView tv_tie_bet_limit;
    @BindView(R.id.tv_tie_bet_limit_text)
    TextView tv_tie_bet_limit_text;
    @BindView(R.id.tv_bank_odds_text)
    TextView tv_bank_odds_text;
    @BindView(R.id.tv_bank_odds)
    TextView tv_bank_odds;
    @BindView(R.id.tv_play_odds_text)
    TextView tv_play_odds_text;
    @BindView(R.id.tv_play_odds)
    TextView tv_play_odds;
    @BindView(R.id.tv_tie_odds_text)
    TextView tv_tie_odds_text;
    @BindView(R.id.tv_tie_odds)
    TextView tv_tie_odds;

    //闪光
    @BindView(R.id.iv_blue_border)
    public ImageView iv_blue_border;
    @BindView(R.id.iv_red_border)
    public ImageView iv_red_border;
    @BindView(R.id.iv_player_01_flash)
    public ImageView iv_player_01_flash;
    @BindView(R.id.iv_banker_01_flash)
    public ImageView iv_banker_01_flash;

    @BindView(R.id.iv_player_01)
    public ImageView iv_player_01;
    @BindView(R.id.iv_banker_01)
    public ImageView iv_banker_01;
    @BindView(R.id.tv_jet_player_value)
    TextView tv_jet_player_value;
    @BindView(R.id.tv_jet_banker_value)
    TextView tv_jet_banker_value;
    @BindView(R.id.rl_player)
    public RelativeLayout rl_player;
    @BindView(R.id.rl_tie)
    public RelativeLayout rl_tie;
    @BindView(R.id.rl_banker)
    public RelativeLayout rl_banker;
    @BindView(R.id.tv_banker1)
    TextView tv_banker1;
    @BindView(R.id.tv_banker2)
    TextView tv_banker2;
    @BindView(R.id.tv_player1)
    TextView tv_player1;
    @BindView(R.id.tv_player2)
    TextView tv_player2;

    @BindView(R.id.gv_big_road)
    public YHZGridView gv_big_road;
    @BindView(R.id.gv_bead_road)
    public YHZGridView gv_bead_road;
    @BindView(R.id.gv_right_middle)
    public YHZGridView gv_right_middle;
    @BindView(R.id.gv_right_bottom_1)
    public YHZGridView gv_right_bottom_1;
    @BindView(R.id.gv_right_bottom_2)
    public YHZGridView gv_right_bottom_2;

    @BindView(R.id.rl_player_chip)
    RelativeLayout rl_player_chip;
    @BindView(R.id.cv_player_chip)
    ChipViewHoly cv_player_chip;
    @BindView(R.id.tv_player_integer)
    TextView tv_player_integer;

    @BindView(R.id.rl_tie_chip)
    RelativeLayout rl_tie_chip;
    @BindView(R.id.cv_tie_chip)
    ChipViewHoly cv_tie_chip;
    @BindView(R.id.tv_tie_integer)
    TextView tv_tie_integer;

    @BindView(R.id.rl_banker_chip)
    RelativeLayout rl_banker_chip;
    @BindView(R.id.cv_banker_chip)
    ChipViewHoly cv_banker_chip;
    @BindView(R.id.tv_banker_integer)
    TextView tv_banker_integer;

    @BindView(R.id.rl_chip_10_2000)
    public RelativeLayout rl_chip_10_2000;
    @BindView(R.id.rl_chip_50_500)
    public RelativeLayout rl_chip_50_500;
    @BindView(R.id.rl_banker_dot_layout)
    public RelativeLayout rl_banker_dot_layout;
    @BindView(R.id.rl_player_dot_layout)
    public RelativeLayout rl_player_dot_layout;

    @BindView(R.id.rl_top)
    public RelativeLayout rl_top;
    @BindView(R.id.rl_test)
    public RelativeLayout rl_test;
    @BindView(R.id.rl_bet)
    public RelativeLayout rl_bet;
    @BindView(R.id.rl_test2)
    public RelativeLayout rl_test2;
    @BindView(R.id.tv_bet_all_score)
    public TextView tv_bet_all_score;

    @BindView(R.id.tv_banker_limit)
    TextView tv_banker_limit;
    @BindView(R.id.tv_player_limit)
    TextView tv_player_limit;
    @BindView(R.id.tv_tie_limit)
    TextView tv_tie_limit;
    @BindView(R.id.v_divide)
    View v_divide;
    @BindView(R.id.v_divide2)
    View v_divide2;

    @BindView(R.id.rl_left)
    RelativeLayout rl_left;
    @BindView(R.id.ll_statistic)
    LinearLayout ll_statistic;
    @BindView(R.id.ll_bet_score)
    LinearLayout ll_bet_score;
    @BindView(R.id.fl_sv)
    FrameLayout fl_sv;
    @BindView(R.id.ll_trends)
    LinearLayout ll_trends;

    @BindView(R.id.iv_ask_bank_1)
    ImageView iv_ask_bank_1;
    @BindView(R.id.iv_ask_bank_2)
    ImageView iv_ask_bank_2;
    @BindView(R.id.iv_ask_bank_3)
    ImageView iv_ask_bank_3;
    @BindView(R.id.iv_ask_play_1)
    ImageView iv_ask_play_1;
    @BindView(R.id.iv_ask_play_2)
    ImageView iv_ask_play_2;
    @BindView(R.id.iv_ask_play_3)
    ImageView iv_ask_play_3;

    @BindView(R.id.rl_video_select1)
    RelativeLayout rl_video_select1;
    @BindView(R.id.rl_video_select2)
    RelativeLayout rl_video_select2;
    @BindView(R.id.iv_video_select1)
    ImageView iv_video_select1;
    @BindView(R.id.iv_video_select2)
    ImageView iv_video_select2;
    @BindView(R.id.tv_robot)
    TextView tv_robot;
    @BindView(R.id.rl_ask_play)
    RelativeLayout rl_ask_play;
    @BindView(R.id.rl_ask_bank)
    RelativeLayout rl_ask_bank;

    private DisplayMetrics displayMetrics;
    private Context mContext;
    private List<ImageView> chipImgList;//下注筹码
    public List<Integer> betLimitList = new ArrayList<>();
    public List<Integer> maxBetList = new ArrayList<>();
    public List<Integer> minBetList = new ArrayList<>();
    public List<Integer> currentBetScore = new ArrayList<>();
    private String roomName;
    private String roomId;
    private double currentMoney;
    private Socket mSocket;
    private Socket mSocketProdict;
    private String token;
    private Baccarat baccarat;
    private int playerIng = 0, bankerIng, tieIng;//实时下注积分
    private int playerScore = 0, bankerScore = 0, tieScore = 0;//当前局每个位置下注积分
    private int perPlayer = 0, perBanker = 0, perTie = 0;//每局每个位置下注总积分
    private int free = 0;//默认庄非免佣1:0.95 1免佣
    private boolean isChip = false;//默认未保存下注记录
    private int isChipSuccess = 0;//默认0下注不成功 1下注成功
    private boolean isSame = false;//默认不续押
    private boolean isAllRemain = false;//默认未全押
    private int whoWin = -1;
    private int isPeopleNum = -1;
    private int betsRemind = -1;
    private int bankColor = 0;
    private int playColor = 0;
    private int tieColor = 0;
    private CommonPopupWindow popupWindow;
    private boolean isRobot = true;
    private boolean isSwitchRobot = false;
    private String state;
    private Map<Integer, Integer> chipScore = new HashMap<>();//0-庄 1-闲 2-和 3-庄对 4-闲对
    //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
    private List<Integer> boardMessageList = new ArrayList<>();//原始数据
    private List<Integer> boardMessageList1 = new ArrayList<>();//原始数据 模拟下一局
    private List<Integer> beadRoadList = new ArrayList<>();//珠盘路(展开)
    private List<Integer> beadRoadListShort = new ArrayList<>();//珠盘路(缩小)

    //0庄 1闲 2和 3庄和 4庄和2  5闲和 6闲和2 7庄和3 8闲和3 9和2 10和3  (只存庄和闲，用于大眼路，小路和小强路分析,因为只分析长度空位无关颜色 )
    List<List<Integer>> bigRoadListAll = new ArrayList<>();//根据所有数据进行偏移一个列(原始大路数据==原始珠盘路数据)
    List<List<Integer>> bigRoadList = new ArrayList<>();//大路(展开)
    List<List<Integer>> bigRoadListShort = new ArrayList<>();//大路(缩小)

    //0庄 1闲
    List<List<Integer>> bigEyeRoadListAll = new ArrayList<>();// 大眼路原始数据
    List<Integer> bigEyeRoadList_color1 = new ArrayList<>();// 大路原始数据 红红蓝蓝按顺序存 0红圈 1蓝圈 转成二维便于其他3路分析-
    List<Integer> bigEyeRoad = new ArrayList<>();// 大眼路原始数据(转成二维就可以直接画出来了)
    List<List<Integer>> bigEyeRoadList = new ArrayList<>();// 大眼路(展开)
    List<List<Integer>> bigEyeRoadListShort = new ArrayList<>();// 大眼路(缩小)

    //0庄 1闲
    List<List<Integer>> smallRoadListAll = new ArrayList<>();// 小路原始数据
    List<Integer> smallRoad = new ArrayList<>();// 小路原始数据(转成二维就可以直接画出来了)
    List<List<Integer>> smallRoadList = new ArrayList<>();// 小路(展开)
    List<List<Integer>> smallRoadListShort = new ArrayList<>();// 小路(缩小)

    //0庄 1闲
    List<List<Integer>> cockroachRoadListAll = new ArrayList<>();// 小强路原始数据
    List<Integer> cockroachRoad = new ArrayList<>();// 小强路原始数据(转成二维就可以直接画出来了)
    List<List<Integer>> cockroachRoadList = new ArrayList<>();// 小强路(展开)
    List<List<Integer>> cockroachRoadListShort = new ArrayList<>();// 小强路(缩小)

    //计算天牌 只有8和9
    private List<Integer> maxScoreListAll = new ArrayList<>();//赢家分数
    private List<Integer> maxScoreList = new ArrayList<>();//赢家分数
    private int noBetCount = 0;
    private float scaleX;
    private int skyCard;
    private float topScaleY = 1.1F;
    private boolean isFirstPlay = true, isFirstBank = true, isFirstTie = true;
    private int betSecond;
    private String dragonColorStr;
    private String tigerColorStr;
    private String tieColorStr;
    private int twinkleBank = 0;
    private int twinklePlay = 0;
    //测试用例
//    static {
//        System.loadLibrary("SmartPlayer");//大牛直播
//    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        this.mContext = DtActivity.this;
        displayMetrics = mContext.getResources().getDisplayMetrics();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        roomName = getIntent().getStringExtra("roomName");
        roomId = getIntent().getStringExtra("roomId");
        token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN_SOCKET);
        betSecond = getIntent().getIntExtra("betSecond", 25);
        tv_room_name.setText(roomName);
        tv_nickname.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, 0);//进入房间未下注状态

        DtSocketController.getInstance().init(this);
        DtSocketController.getInstance().copyWidget(boardMessageList, tv_time, ll_poker, iv_banker_01,
                tv_jet_banker_value, iv_player_01, tv_jet_player_value, roomId,
                rl_player, rl_tie, rl_banker);
        DtSocketController.getInstance().connectSocket();
        EventBus.getDefault().register(this);
        baccarat = (Baccarat) getIntent().getSerializableExtra("baccarat");
        init(mContext);
        copyWidget(sv1, tv_middle_sv_l, tv_middle_sv_h, tv_big_sv_l, tv_big_sv_h, tv_exit, baccarat);

        initEn();
        initEvent();

        chipImgList = new ArrayList<>();
        selfChipValue();
        onInitImg(iv_chip_10);
        onInitImg(iv_chip_50);
        onInitImg(iv_chip_100);
        onInitImg(iv_chip_500);
        onInitImg(iv_chip_2000);

        currentBetScore.clear();
        currentBetScore.add(0);//闲
        currentBetScore.add(0);//庄
        currentBetScore.add(0);//和
        initAsk();
        initChip();
        initDisplay();
        initTypeFace();
        //默认全为-1
        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_BIG_EYE, -1);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_SMALL, -1);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_COCKROACH, -1);
    }

    private void initDisplay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //顶部宽度=2560 总宽度=2454 珠路=72 需要花=0 留白=0 华为
                float _value = 0.0f;
                float __value = 0.0f;
                float _value1 = Float.parseFloat(getScaleX(rl_top.getHeight(), displayMetrics.heightPixels));
                float _value2 = Float.parseFloat(getScaleX(rl_bet.getHeight(), displayMetrics.heightPixels));
                float _value3 = Float.parseFloat(getScaleX(rl_test2.getHeight(), displayMetrics.heightPixels));
                scaleX = 0.0f;
                topScaleY = 1.1f;
                if (rl_top.getWidth() > 0) {
                    _value = Float.parseFloat(getScaleX(rl_test.getWidth() * 2, rl_top.getWidth()));
                    if (rl_test.getWidth() > 0) {
                        scaleX = _value + 1.00f;
                        rl_top.setScaleX(scaleX);   //rl_test.getWidth()*2/displayMetrics.widthPixels+0.05
                    }
                }
                if (rl_top.getWidth() > 0) {
                    __value = Float.parseFloat(getScaleX(displayMetrics.widthPixels - rl_top.getWidth(), rl_top.getWidth()));
                    if (rl_top.getWidth() > displayMetrics.widthPixels) {
                        scaleX = 1 - __value;
                        rl_top.setScaleX(scaleX);
                    }
                }
                if (displayMetrics.densityDpi == 160) {
                    rl_top.setPivotY(0f);
                    rl_top.setScaleY(topScaleY);
                    RelativeLayout.LayoutParams rl_bet_params = (RelativeLayout.LayoutParams) rl_bet.getLayoutParams();
                    rl_bet_params.topMargin = (int) (rl_top.getHeight() * 0.1f);
                    rl_bet.setLayoutParams(rl_bet_params);
                }
                float scaleY = 0.0f;
                rl_bet.setPivotY(0f);//设置了任意值往上压缩，默认没设置往下压缩
                if (_value2 / _value3 > 2) {
                    scaleY = 1 - _value3;
                    rl_bet.setScaleY(scaleY);
                } else if (_value2 / _value3 < 2 && _value2 / _value3 > 1) {

                } else if (_value2 < _value3) {
                    scaleY = 1 + _value1;
                    rl_bet.setScaleY(scaleY);
                }
                zoomReturnVideo();
            }
        }, 150);
    }

    private void selfChipValue() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(10, R.drawable.chip_10);
        map.put(20, R.drawable.chip_20);
        map.put(50, R.drawable.chip_50);
        map.put(100, R.drawable.chip_100);
        map.put(200, R.drawable.chip_200);
        map.put(500, R.drawable.chip_500);
        map.put(1000, R.drawable.chip_1000);
        map.put(5000, R.drawable.chip_5000);
        map.put(10000, R.drawable.chip_10000);
        map.put(50000, R.drawable.chip_50000);
        chipImgList.add(iv_chip_10);
        chipImgList.add(iv_chip_50);
        chipImgList.add(iv_chip_100);
        chipImgList.add(iv_chip_500);
        chipImgList.add(iv_chip_2000);
        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.DT_CHIP))) {
            String macau = SharedPreUtil.getInstance(mContext).getString(Constant.DT_CHIP);
            String[] macauArr = new Gson().fromJson(macau, String[].class);
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < macauArr.length; i++) {
                int macauInt = Integer.parseInt(macauArr[i].split("_")[2]);
                list.add(macauInt);
            }
            Collections.sort(list);//升序
            for (int j = 0; j < chipImgList.size(); j++) {
                chipImgList.get(j).setBackgroundResource(map.get(list.get(j)));
                chipImgList.get(j).setTag("chip_iv_" + list.get(j));
            }
        }
    }

    private String getScaleX(int a, int b) {
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format((float) a / b);
    }

    /**
     * 根据赢的位置闪
     *
     * @param iv_play
     * @param iv_bank
     * @param iv_tie
     */
    private void startLightById(int iv_play, int iv_bank, int iv_tie) {
        if (iv_bank == 1) {//1播放 0不播放
            if (!adDragon.isRunning()) {
                adDragon.start();
            }
        } else {
            if (adDragon.isRunning()) {
                adDragon.stop();
            }
            GradientDrawable drawableDragon1 = new GradientDrawable();
            drawableDragon1.setShape(GradientDrawable.OVAL);
            drawableDragon1.setUseLevel(false);
            drawableDragon1.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), bankColor);
            drawableDragon1.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));

            GradientDrawable drawableDragon2 = new GradientDrawable();
            drawableDragon2.setShape(GradientDrawable.OVAL);
            drawableDragon2.setUseLevel(false);
            String newDragonLight = dragonColorStr.replace("#", "#66");
            drawableDragon2.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), Color.parseColor(newDragonLight));
            drawableDragon2.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));
            adDragon.addFrame(drawableDragon2, 300);
            adDragon.addFrame(drawableDragon1, 300);
            adDragon.setOneShot(false);
            rl_banker.setBackgroundDrawable(adDragon);
        }
        if (iv_play == 1) {
            if (!adTiger.isRunning()) {
                adTiger.start();
            }
        } else {
            if (adTiger.isRunning()) {
                adTiger.stop();
            }
            GradientDrawable drawableTiger1 = new GradientDrawable();
            drawableTiger1.setShape(GradientDrawable.OVAL);
            drawableTiger1.setUseLevel(false);
            drawableTiger1.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), Color.parseColor(tigerColorStr));
            drawableTiger1.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));

            GradientDrawable drawableTiger2 = new GradientDrawable();
            drawableTiger2.setShape(GradientDrawable.OVAL);
            drawableTiger2.setUseLevel(false);
            String newTigerLight = tigerColorStr.replace("#", "#66");
            drawableTiger2.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), Color.parseColor(newTigerLight));
            drawableTiger2.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));
            adTiger.addFrame(drawableTiger2, 300);
            adTiger.addFrame(drawableTiger1, 300);
            adTiger.setOneShot(false);
            rl_player.setBackgroundDrawable(adTiger);

        }
        if (iv_tie == 1) {
            if (!adTie.isRunning()) {
                adTie.start();
            }
        } else {
            if (adTie.isRunning()) {
                adTie.stop();
            }
            GradientDrawable drawableTie1 = new GradientDrawable();
            drawableTie1.setShape(GradientDrawable.OVAL);
            drawableTie1.setUseLevel(false);
            drawableTie1.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), Color.parseColor(tieColorStr));
            drawableTie1.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));

            GradientDrawable drawableTie2 = new GradientDrawable();
            drawableTie2.setShape(GradientDrawable.OVAL);
            drawableTie2.setUseLevel(false);
            String newTieLight = tieColorStr.replace("#", "#66");
            drawableTie2.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), Color.parseColor(newTieLight));
            drawableTie2.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));
            adTie.addFrame(drawableTie2, 300);
            adTie.addFrame(drawableTie1, 300);
            adTie.setOneShot(false);
            rl_tie.setBackgroundDrawable(adTie);
        }
    }

    private void initTypeFace() {
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/fontzipMin.ttf");
        tv_player.setTypeface(tf);
        tv_banker.setTypeface(tf);
        tv_tie.setTypeface(tf);
    }

    /**
     * 默认庄问路为-1 闲问路则相反
     */
    private void initAsk() {
        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_BIG_EYE, -1);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_SMALL, -1);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_COCKROACH, -1);
    }

    private void initSound() {
        if (SharedPreUtil.getInstance(this).getInt(Constant.SOUND_SWITCH) == 0) {
            try {
                int languageType = CommSharedUtil.getInstance(this).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
                SoundPoolUtil.getInstance().release();
                if (languageType == LanguageType.LANGUAGE_EN) {
                    SoundPoolUtil.getInstance().initDtEN(this);
                } else {
                    SoundPoolUtil.getInstance().initDtZH(this);
                }
                //初始化完毕才能播放（延迟加载，是因为偶尔播放）
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        SoundPoolUtil.getInstance().play("49");
                    }
                }, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Timer timer;
    private int second = 25;

    private void perOnePerformance() {
        timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                second--;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (second > -1) {
                            if (second == 0) {
                                tv_time.setText("发牌中");
                            } else
                                tv_time.setText(String.valueOf(second));
                        } else {
                            tv_time.setText("0");
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }

    private AnimationDrawable adDragon;
    private AnimationDrawable adTiger;
    private AnimationDrawable adTie;

    /*
     * 同步数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventSyn(final ObjectEvent.BoardMessageEvent event) {
        if (event.boardMessageList.size() > 0) {
            boardMessageList.clear();
            boardMessageList.addAll(event.boardMessageList);
            maxScoreListAll.clear();
            maxScoreListAll.addAll(event.maxScoreList);
            initMessages();
            Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                    gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 1,
                    bankColor, playColor, tieColor, 0, false, 2);//4列 网络进入房间
//            ask(0, true, false);
        }
    }

    /*
     * 进入房间
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBoardMessage(final ObjectEvent.BoardMessageEvent event) {
        state = event.state;
        tv_money.setText(event.goldCoins);
        skyCard = event.skyCard;
        isPeopleNum = event.isPeopleNum;
        betsRemind = event.betsRemind;
        betLimitList.clear();
        maxBetList.clear();
        minBetList.clear();
        for (int i = 0; i < event.betLimitList.length; i++) {
            int _limit = Integer.parseInt(event.betLimitList[i]);
            betLimitList.add(_limit);
        }
        for (int i = 0; i < event.maxBetList.length; i++) {
            int _limit = Integer.parseInt(event.maxBetList[i]);
            maxBetList.add(_limit);
        }
        for (int i = 0; i < event.minBetList.length; i++) {
            int _limit = Integer.parseInt(event.minBetList[i]);
            minBetList.add(_limit);
        }
        dragonColorStr = event.color[0];
        tigerColorStr = event.color[1];
        tieColorStr = event.color[2];
        bankColor = Color.parseColor(dragonColorStr);
        playColor = Color.parseColor(tigerColorStr);
        tieColor = Color.parseColor(tieColorStr);

        SharedPreUtil.getInstance(mContext).saveParam(Constant.BET_LIMIT, betLimitList.toString());
        SharedPreUtil.getInstance(mContext).saveParam(Constant.MAX_BET, maxBetList.toString());
        SharedPreUtil.getInstance(mContext).saveParam(Constant.MIN_BET, minBetList.toString());
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
        adDragon = new AnimationDrawable();
        adTiger = new AnimationDrawable();
        adTie = new AnimationDrawable();
        GradientDrawable drawableDragon1 = new GradientDrawable();
        drawableDragon1.setShape(GradientDrawable.OVAL);
        drawableDragon1.setUseLevel(false);
        drawableDragon1.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), bankColor);
        drawableDragon1.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));

        GradientDrawable drawableDragon2 = new GradientDrawable();
        drawableDragon2.setShape(GradientDrawable.OVAL);
        drawableDragon2.setUseLevel(false);
        String newDragonLight = dragonColorStr.replace("#", "#66");
        drawableDragon2.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), Color.parseColor(newDragonLight));
        drawableDragon2.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));
        adDragon.addFrame(drawableDragon1, 300);
        adDragon.addFrame(drawableDragon2, 300);
        adDragon.setOneShot(false);
        rl_banker.setBackgroundDrawable(adDragon);

        GradientDrawable drawableTiger1 = new GradientDrawable();
        drawableTiger1.setShape(GradientDrawable.OVAL);
        drawableTiger1.setUseLevel(false);
        drawableTiger1.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), playColor);
        drawableTiger1.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));

        GradientDrawable drawableTiger2 = new GradientDrawable();
        drawableTiger2.setShape(GradientDrawable.OVAL);
        drawableTiger2.setUseLevel(false);
        String newTigerLight = tigerColorStr.replace("#", "#66");
        drawableTiger2.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), Color.parseColor(newTigerLight));
        drawableTiger2.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));
        adTiger.addFrame(drawableTiger1, 300);
        adTiger.addFrame(drawableTiger2, 300);
        adTiger.setOneShot(false);
        rl_player.setBackgroundDrawable(adTiger);

        GradientDrawable drawableTie1 = new GradientDrawable();
        drawableTie1.setShape(GradientDrawable.OVAL);
        drawableTie1.setUseLevel(false);
        drawableTie1.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), tieColor);
        drawableTie1.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));

        GradientDrawable drawableTie2 = new GradientDrawable();
        drawableTie2.setShape(GradientDrawable.OVAL);
        drawableTie2.setUseLevel(false);
        String newTieLight = tieColorStr.replace("#", "#66");
        drawableTie2.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), Color.parseColor(newTieLight));
        drawableTie2.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit147), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));
        adTie.addFrame(drawableTie1, 300);
        adTie.addFrame(drawableTie2, 300);
        adTie.setOneShot(false);
        rl_tie.setBackgroundDrawable(adTie);
        String banker = String.valueOf(minBetList.get(0)) + "-" + String.valueOf(maxBetList.get(0));
        String player = String.valueOf(minBetList.get(0)) + "-" + String.valueOf(maxBetList.get(0));
        String tie = String.valueOf(minBetList.get(1)) + "-" + String.valueOf(maxBetList.get(1));

        tv_open_dragon.setTextColor(bankColor);
        tv_in_game_banker_value.setTextColor(bankColor);
        tv_open_tiger.setTextColor(playColor);
        tv_in_game_player_value.setTextColor(playColor);
        tv_open_tie.setTextColor(tieColor);
        tv_in_game_tie_value.setTextColor(tieColor);
        tv_banker_date.setTextColor(bankColor);
        tv_play_date.setTextColor(playColor);
        tv_tie_date.setTextColor(tieColor);
        tv_ask_bank.setTextColor(bankColor);
        tv_ask_play.setTextColor(playColor);

        tv_dragon_bet_limit_text.setTextColor(bankColor);
        tv_dragon_bet_limit.setTextColor(bankColor);
        tv_tiger_bet_limit_text.setTextColor(playColor);
        tv_tiger_bet_limit.setTextColor(playColor);
        tv_tie_bet_limit_text.setTextColor(tieColor);
        tv_tie_bet_limit.setTextColor(tieColor);
        tv_bank_odds_text.setTextColor(bankColor);
        tv_bank_odds.setTextColor(bankColor);
        tv_play_odds_text.setTextColor(playColor);
        tv_play_odds.setTextColor(playColor);
        tv_tie_odds_text.setTextColor(tieColor);
        tv_tie_odds.setTextColor(tieColor);

        tv_banker1.setTextColor(bankColor);
        tv_banker2.setTextColor(bankColor);
        tv_player1.setTextColor(playColor);
        tv_player2.setTextColor(playColor);
        tv_dragon_bet_limit.setText(String.valueOf(betLimitList.get(0)));
        tv_tiger_bet_limit.setText(String.valueOf(betLimitList.get(0)));
        tv_tie_bet_limit.setText(String.valueOf(betLimitList.get(1)));
        tv_banker_limit.setText(banker);
        tv_player_limit.setText(player);
        tv_tie_limit.setText(tie);
        tv_banker.setTextColor(bankColor);
        tv_player.setTextColor(playColor);
        tv_tie.setTextColor(tieColor);
        tv_banker_limit.setTextColor(bankColor);
        tv_player_limit.setTextColor(playColor);
        tv_tie_limit.setTextColor(tieColor);


        if (!event.state.equals(Constant.BACCARAT_BET)) {
            onClick(false);
            //测试用例 暂时下注
            ll_poker.setVisibility(View.GONE);
            isShowChip(View.GONE);
            tv_robot.setVisibility(View.GONE);//显示
        } else {
            currentMoney = Double.parseDouble(tv_money.getText().toString());
            isShowChip(View.VISIBLE);
            tv_robot.setVisibility(View.GONE);
        }
        if (event.state.equals(Constant.BACCARAT_BET)) {
            second = event.downtownTime;
            perOnePerformance();
            ll_poker.setVisibility(View.GONE);
        }
        if (event.state.equals(Constant.BACCARAT_RESULT)) {
            tv_time.setText("发牌中");
            tv_robot.setVisibility(View.GONE);//只有发牌阶段才能换托管状态 显示
            zoomQuarterVideo();
        } else if (event.state.equals(Constant.BACCARAT_WAIT)) {
            tv_time.setText("等待");
        } else if (event.state.equals(Constant.BACCARAT_INNINGS_END)) {
            tv_time.setText("洗牌中");
            tv_robot.setVisibility(View.GONE);//只有洗牌状态不能托管
        }
        if (event.boardMessageList.size() > 0) {
            boardMessageList.clear();
            boardMessageList.addAll(event.boardMessageList);
            maxScoreListAll.clear();
            maxScoreListAll.addAll(event.maxScoreList);
            initMessages();
            Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                    gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 1,
                    bankColor, playColor, tieColor, 0, false, 2);
//            AlgorithmMacau.getInstance().drawRoad(maxScoreList, beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
//                    gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 1,
//                    bankColor, playColor, tieColor, 0, false, 2);//11列 加一局
            ask(0, true, false, 0, false);
        }
        String _1 = "0";
        String _2 = "0";
        String _3 = "0";
        if (isPeopleNum == 0) {
            _1 = String.valueOf(bankerIng);
            _2 = String.valueOf(playerIng);
            _3 = String.valueOf(tieIng);
        } else if (isPeopleNum == 1) {
            _1 = String.valueOf(bankerIng) + "/" + 0;
            _2 = String.valueOf(playerIng) + "/" + 0;
            _3 = String.valueOf(tieIng) + "/" + 0;
        }
        tv_banker_date.setText(_1);
        tv_play_date.setText(_2);
        tv_tie_date.setText(_3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                initSound();//在低端机上 加载assets资源，超级耗时
            }
        }, 200);
        DtSocketController.getInstance().connectSocketProdict();
        mSocketProdict = DtSocketController.getInstance().getSocketProdict();
    }

    private void ask(int value, boolean isShowAsk, boolean isDraw, int count, boolean isShow) {
        boardMessageList1.clear();
        boardMessageList1.addAll(boardMessageList);
        boardMessageList1.add(value);
        AlgorithmMacau.getInstance().initBigRoad(boardMessageList1, beadRoadList, beadRoadListShort,
                bigRoadListAll, bigRoadList, bigRoadListShort,
                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
                smallRoadListAll, smallRoadList, smallRoadListShort,
                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort,
                maxScoreListAll, maxScoreList, mContext, 2, isShowAsk,
                iv_ask_bank_1, iv_ask_bank_2, iv_ask_bank_3, iv_ask_play_1, iv_ask_play_2, iv_ask_play_3,
                bankColor, playColor, tieColor);//1房间 2大厅
        if (isDraw) {
            Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                    gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 1,
                    bankColor, playColor, tieColor, count, isShow, 2);
        }
    }

    private void isShowChip(int show) {
        rl_chip_10_2000.setVisibility(show);
        rl_chip_100.setVisibility(show);
        rl_chip_50_500.setVisibility(show);
    }

    /*
     * 初始化牌局(牌局的下注积分数 0闲 1庄 2和 3闲对 4庄对)
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBetRecord(final ObjectEvent.BetRecordEvent event) {
        if (event.scoreList.size() > 0) {
            bankerIng = event.scoreList.get(0);
            playerIng = event.scoreList.get(1);
            tieIng = event.scoreList.get(2);
            String _1 = "0";
            String _2 = "0";
            String _3 = "0";
            if (isPeopleNum == 0) {
                _1 = String.valueOf(bankerIng);
                _2 = String.valueOf(playerIng);
                _3 = String.valueOf(tieIng);
            } else if (isPeopleNum == 1) {
                _1 = String.valueOf(bankerIng) + "/" + event.betPeopleList.get(0);
                _2 = String.valueOf(playerIng) + "/" + event.betPeopleList.get(1);
                _3 = String.valueOf(tieIng) + "/" + String.valueOf(event.betPeopleList.get(2));
            }
            tv_banker_date.setText(_1);
            tv_play_date.setText(_2);
            tv_tie_date.setText(_3);
            currentBetScore.clear();
            currentBetScore.addAll(event.scoreList);
        }
        mSocketProdict = DtSocketController.getInstance().getSocketProdict();
    }

    /*
     * 当前下注金额(牌局的下注积分数 0闲 1庄 2和 3闲对 4庄对)
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBetScore(final ObjectEvent.betScoreEvent event) {
        currentBetScore.clear();
        currentBetScore.addAll(event.scoreList);
        int allScore = currentBetScore.get(0) + currentBetScore.get(1) + currentBetScore.get(2);
        tv_bet_all_score.setText(String.valueOf(allScore));
        currentChip();
    }

    /*
     * 状态 BET:下注阶段 RESULT:开牌阶段 OVER:清算阶段 WAITTING:同步识别端
     * */
    List<String> _calPlayer = new ArrayList<>();
    List<String> _calBanker = new ArrayList<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventState(final ObjectEvent.StateEvent event) {
        state = event.state;
        if (event.state.equals("BET")) {
            onClick(true);
            currentBetScore.clear();
            currentBetScore.add(0);
            currentBetScore.add(0);
            currentBetScore.add(0);
            isAllRemain = false;//恢复下注所有金额
            currentMoney = Double.parseDouble(tv_money.getText().toString());
            //先隐藏
            rl_tie_chip.setVisibility(View.GONE);
            rl_player_chip.setVisibility(View.GONE);
            rl_banker_chip.setVisibility(View.GONE);
            if (perPlayer != 0 || perTie != 0 || perBanker != 0) {
                isChip = true;
                isChipClick(false, true, R.drawable.button_bet_disable_bg, R.drawable.button_bet_normal_bg, R.string.repeat);
            } else {
                isChip = false;
                isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            }

            ToastUtil.getInstance().showToast(mContext, mContext.getResources().getString(R.string.bet_start));
            isChipSuccess = 0;//新一局 代表未下注成功
            SoundPoolUtil.getInstance().play("50");//已开局，请下注
            ll_poker.setVisibility(View.GONE);
            isShowChip(View.VISIBLE);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);

            iv_banker_01_flash.setVisibility(View.VISIBLE);
            GradientDrawable drawableDragon2 = new GradientDrawable();
            drawableDragon2.setShape(GradientDrawable.RECTANGLE);
            drawableDragon2.setUseLevel(false);
            String newDragonLight = dragonColorStr.replace("#", "#66");
            drawableDragon2.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), Color.parseColor(newDragonLight));
            drawableDragon2.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit50), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));
            iv_banker_01_flash.setBackgroundDrawable(drawableDragon2);
            FlashHelper.getInstance().startFlick(iv_banker_01_flash, Animation.INFINITE);
            second = betSecond;
            perOnePerformance();
            zoomReturnVideo();
            startLightById(0, 0, 0);
            int allScore = currentBetScore.get(0) + currentBetScore.get(1) + currentBetScore.get(2);
            tv_bet_all_score.setText(String.valueOf(allScore));
            tv_win.setText("0");
            isFirstPlay = true;
            isFirstBank = true;
            isFirstTie = true;
            if (mSocketProdict != null && mSocketProdict.connected()) {
                DtSocketController.getInstance().connectSocketProdict();
                mSocketProdict = DtSocketController.getInstance().getSocketProdict();
            }
            tv_robot.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    betRobot();
                }
            }, 3000);
        } else if (event.state.equals("RESULT")) {
            onClick(false);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            ll_poker.setVisibility(View.VISIBLE);
            ToastUtil.getInstance().showToast(mContext, mContext.getResources().getString(R.string.bet_stop));
            SoundPoolUtil.getInstance().play("52");//停止下注
            isShowChip(View.GONE);
            rl_player_dot_layout.setVisibility(View.VISIBLE);
            rl_banker_dot_layout.setVisibility(View.VISIBLE);
            //清空处理
            tv_jet_player_value.setText("0");
            tv_jet_banker_value.setText("0");
            iv_player_01.setBackgroundResource(0);
//            iv_player_02.setBackgroundResource(0);
//            iv_player_03.setBackgroundResource(0);
            iv_banker_01.setBackgroundResource(0);
//            iv_banker_02.setBackgroundResource(0);
//            iv_banker_03.setBackgroundResource(0);
            _calPlayer.clear();
            _calBanker.clear();
            if (betsRemind == 1) {
                outGame();
            }
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_TIE, 0);
//            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY_PAIR, 0);
//            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK_PAIR, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
            tv_time.setText("发牌中");
            zoomQuarterVideo();
            timer.cancel();
            second = 0;
            tv_robot.setClickable(true);//发牌阶段才能换托管状态
            tv_robot.setVisibility(View.GONE);//显示
        } else if (event.state.equals("OVER")) {
            onClick(false);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
        } else if (event.state.equals("WAITTING")) {
            onClick(false);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
        } else if (event.state.equals(Constant.BACCARAT_INNINGS_END)) {//当前轮结束
            ToastUtil.showToast(mContext, Constant.BACCARAT_INNINGS_END);
            ll_poker.setVisibility(View.GONE);
            cleanChip();
            String _1 = 0 + "/" + 0;
            String _2 = 0 + "/" + 0;
            String _3 = 0 + "/" + 0;
            tv_play_date.setText(_1);
            tv_banker_date.setText(_2);
            tv_tie_date.setText(_3);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
            startLightById(0, 0, 0);
            tv_time.setText("洗牌中");
            tv_robot.setVisibility(View.GONE);
        } else if (event.state.equals(Constant.BACCARAT_CREATED)) {//新的一轮开始
            boardMessageList.clear();
            maxScoreListAll.clear();
            initMessages();
            AlgorithmMacau.getInstance().drawRoad(maxScoreList, beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                    gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 0, bankColor, playColor, tieColor, 0, false, 2);//11列 加一局
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
            iv_ask_bank_1.setBackgroundDrawable(null);
            iv_ask_bank_2.setBackgroundDrawable(null);
            iv_ask_bank_3.setBackgroundDrawable(null);
            iv_ask_play_1.setBackgroundDrawable(null);
            iv_ask_play_2.setBackgroundDrawable(null);
            iv_ask_play_3.setBackgroundDrawable(null);
        } else if (event.state.equals(Constant.BACCARAT_FAULT)) {
            ToastUtil.show(mContext, "机械臂异常");
        } else if (event.state.equals(Constant.BACCARAT_INVALID)) {
            ToastUtil.show(mContext, "本局作废");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventSwitchResult(final ObjectEvent.SwitchResultEvent event) {
        ll_poker.setVisibility(View.VISIBLE);
        SoundPoolUtil.getInstance().play("96");//发牌音效
        if (event.pos.equals("04")) {//乐虎只有一张牌 03和04 1-虎
            _calPlayer.clear();
            _calBanker.clear();
            iv_player_01.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calPlayer.add(event.card);
            iv_player_01_flash.setVisibility(View.GONE);
            FlashHelper.getInstance().stopFlick(iv_player_01_flash);
            if (event.playCardValue == 11) {
                tv_jet_player_value.setText("J");
            } else if (event.playCardValue == 12) {
                tv_jet_player_value.setText("Q");
            } else if (event.playCardValue == 13) {
                tv_jet_player_value.setText("K");
            } else {
                tv_jet_player_value.setText("" + event.playCardValue);
            }
        } else if (event.pos.equals("03")) {//0-龙
            iv_banker_01.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calBanker.add(event.card);
            iv_banker_01_flash.setVisibility(View.GONE);
            FlashHelper.getInstance().stopFlick(iv_banker_01_flash);
            iv_player_01_flash.setVisibility(View.VISIBLE);
            GradientDrawable drawableTiger = new GradientDrawable();
            drawableTiger.setShape(GradientDrawable.RECTANGLE);
            drawableTiger.setUseLevel(false);
            String newDragonLight = tigerColorStr.replace("#", "#66");
            drawableTiger.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit2), Color.parseColor(newDragonLight));
            drawableTiger.setSize(mContext.getResources().getDimensionPixelOffset(R.dimen.unit50), mContext.getResources().getDimensionPixelOffset(R.dimen.unit70));
            iv_player_01_flash.setBackgroundDrawable(drawableTiger);
            FlashHelper.getInstance().startFlick(iv_player_01_flash, Animation.INFINITE);
            if (event.bankCardValue == 11) {
                tv_jet_banker_value.setText("J");
            } else if (event.bankCardValue == 12) {
                tv_jet_banker_value.setText("Q");
            } else if (event.bankCardValue == 13) {
                tv_jet_banker_value.setText("K");
            } else {
                tv_jet_banker_value.setText("" + event.bankCardValue);
            }
        }
    }

    /*
     * 一局结束，将这局结果放入珠路 04K57-KH8WG-7KADL-HZZAA
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventResult(final ObjectEvent.ResultEvent event) {
//        skyCard = event.skyCard;
        if (event.win != -1) {
            List<String> player = event.playCardList;
            List<String> banker = event.bankCardList;
            final int playScore = BaccaratUtil.getInstance().getDtCardScore(player.get(0));
            final int bankScore = BaccaratUtil.getInstance().getDtCardScore(banker.get(0));
            final boolean isTie = (playScore == bankScore);
            if (boardMessageList.size() + 1 == event.round) {
                boardMessageList.add(-1);
                boardMessageList.set(event.round - 1, event.win);
            }

            initMessages();
            Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                    gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 1,
                    bankColor, playColor, tieColor, 0, false, 2);//4列 网络进入房间
            ask(0, true, false, 0, false);
            twinkleBank = 0;
            twinklePlay = 0;
            //发光

            //1.庄几点 闲几点 一遍 先报庄几点再报闲几点
            //2.庄赢/闲赢 2遍 和局 1遍
            BaccaratUtil.sayDtDot(0, bankScore);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaccaratUtil.sayDtDot(1, playScore);
                }
            }, 1200);
            if (playScore > bankScore) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaccaratUtil.sayDtDot(1, isTie);
                    }
                }, 3500);

            } else if (playScore < bankScore) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaccaratUtil.sayDtDot(0, isTie);
                    }
                }, 3500);

            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaccaratUtil.sayDtDot(-1, isTie);//和局
                    }
                }, 3500);
            }
            //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
            LogUtil.i("");
            if (event.win == 0) {
                startLightById(0, 1, 0);
            } else if (event.win == 1) {
                startLightById(1, 0, 0);
            } else if (event.win == 2) {
                startLightById(0, 0, 1);
            }
            zoomReturnVideo();
        }
    }

    /*
     * 清算
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventLiquidation(final Liquidation event) {
        if (event != null) {
            tv_money.setText(event.balance);
            postEventMoney(event.balance);
            if (event.win) {
                String _win = mContext.getResources().getString(R.string.you_win);
                ToastUtil.getInstance().showToastWin(mContext, _win + "" + event.score);
                SoundPoolUtil.getInstance().play("win");
                tv_win.setText("" + event.score);
            } else if (event.win == false && Float.parseFloat(event.score) > 0) {
                String _lose = mContext.getResources().getString(R.string.you_lose);
                ToastUtil.getInstance().showToastWin(mContext, _lose + "" + event.score);
                tv_win.setText("-" + event.score);
            }
        }
        isChipSuccess = 0;//清算完才能退出房间
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, isChipSuccess);
    }

    /**
     * 下注成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventChip(final ObjectEvent.ChipEvent event) {
        isSame = false;
        tv_money.setText(event.availableAmount);
        LogUtil.i("当前下注=-1 " + event.tieScore + " score=" + event.scoreArr.length);
        if (event.scoreArr.length > 0) {
            int[] hisScore = event.scoreArr;
            perPlayer = hisScore[1];
            perBanker = hisScore[0];
            perTie = hisScore[2];
        } else {
            perPlayer = perPlayer - playerScore + event.playScore;
            perBanker = perBanker - bankerScore + event.bankScore;
            perTie = perTie - tieScore + event.tieScore;
        }
        chipScore.clear();
        chipScore.put(0, 0);
        chipScore.put(1, 0);
        chipScore.put(2, 0);
        playerScore = 0;
        tieScore = 0;
        bankerScore = 0;
        LogUtil.i("当前下注=3" + currentBetScore.toString() + " perBanker=" + perBanker + " perPlayer=" + perPlayer + " perTie=" + perTie + " chipScore=" + chipScore.toString());
        currentBetScore.set(0, perBanker);
        currentBetScore.set(1, perPlayer);
        currentBetScore.set(2, perTie);
        LogUtil.i("当前下注=4" + currentBetScore.toString() + " perBanker=" + perBanker + " perPlayer=" + perPlayer + " perTie=" + perTie);
        int allScore = currentBetScore.get(0) + currentBetScore.get(1) + currentBetScore.get(2);
        tv_bet_all_score.setText(String.valueOf(allScore));
        currentChip2();
        SoundPoolUtil.getInstance().play("51");//下注成功
        isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
        isChipSuccess = 1;//当前局下注成功 就不能中途退出房间
        noBetCount = 0;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, isChipSuccess);
        tv_robot.setClickable(false);//每局只能托管下注一次,当手动下注，也不能托管了
    }

    /**
     * 超过限红
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventLimitBet(final ObjectEvent.LimitBetEvent event) {
        if (event.limitBetScore > 0) {
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            currentChip();
            chipScore.put(0, 0);
            chipScore.put(1, 0);
            chipScore.put(2, 0);
            isAllRemain = false;
            int allScore = currentBetScore.get(0) + currentBetScore.get(1) + currentBetScore.get(2);
            tv_bet_all_score.setText(String.valueOf(allScore));
        }
    }

    /*
     * 上分 下分
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventUpperMoney(final ObjectEvent.UpperMoneyEvent event) {
        tv_money.setText(event.money);
        currentMoney = Double.parseDouble(tv_money.getText().toString());
        postEventMoney(event.money);
        if (!TextUtils.isEmpty(event.type) && event.type.equals("0")) {
            hintDialog("上分：" + event.upperScore + " 成功");
        } else if (!TextUtils.isEmpty(event.type) && event.type.equals("1")) {
            hintDialog("下分：" + event.upperScore + " 成功");
        }
    }

    private void postEventMoney(String money) {
        ObjectEvent.MoneyChangeEvent moneyChangeEvent = new ObjectEvent.MoneyChangeEvent();
        moneyChangeEvent.money = money;
        EventBus.getDefault().post(moneyChangeEvent);
    }

    private void currentChip2() {
        if (currentBetScore.get(1) == 0) {
            rl_player_chip.setVisibility(View.GONE);
            cv_player_chip.addChip(0);
            perPlayer = currentBetScore.get(1);
            playerScore = currentBetScore.get(1);
//            chipScore.put(1, currentBetScore.get(1));
        } else if (currentBetScore.get(1) == perPlayer) {
            rl_player_chip.setVisibility(View.VISIBLE);
            perPlayer = currentBetScore.get(1);
            cv_player_chip.addChip(0);
//            chipScore.put(1, currentBetScore.get(1));
            tv_player_integer.setText("" + currentBetScore.get(1));
            List<Integer> _playerChip = new ArrayList<>();
            _playerChip.addAll(BaccaratUtil.addChip(currentBetScore.get(1)));
            for (Integer integer : _playerChip) {
                cv_player_chip.addChip(integer);
            }
            if (cv_player_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_chip.getLayoutParams();
                if (cv_player_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_player_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_player_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_player_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_player_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_player_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_player_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_player_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_player_integer.setLayoutParams(tvParams);
            }
        }
        if (currentBetScore.get(0) == 0) {
            rl_banker_chip.setVisibility(View.GONE);
            cv_banker_chip.addChip(0);
            perBanker = currentBetScore.get(0);
            bankerScore = currentBetScore.get(0);
//            chipScore.put(0, currentBetScore.get(0));
        } else if (currentBetScore.get(0) == perBanker) {
            rl_banker_chip.setVisibility(View.VISIBLE);
            perBanker = currentBetScore.get(0);
            cv_banker_chip.addChip(0);
//            chipScore.put(0, currentBetScore.get(0));
            tv_banker_integer.setText("" + currentBetScore.get(0));
            List<Integer> _bankerChip = new ArrayList<>();
            _bankerChip.addAll(BaccaratUtil.addChip(currentBetScore.get(0)));
            for (Integer integer : _bankerChip) {
                cv_banker_chip.addChip(integer);
            }
            if (cv_banker_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_chip.getLayoutParams();
                if (cv_banker_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_banker_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_banker_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_banker_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_banker_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_banker_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_banker_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_banker_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_banker_integer.setLayoutParams(tvParams);
            }
        }
        if (currentBetScore.get(2) == 0) {
            rl_tie_chip.setVisibility(View.GONE);
            cv_tie_chip.addChip(0);
            perTie = currentBetScore.get(2);
            tieScore = currentBetScore.get(2);
//            chipScore.put(2, currentBetScore.get(2));
        } else if (currentBetScore.get(2) == perTie) {
            LogUtil.i("当前下注=5" + currentBetScore.toString());
            rl_tie_chip.setVisibility(View.VISIBLE);
            perTie = currentBetScore.get(2);
            cv_tie_chip.addChip(0);
//            chipScore.put(2, currentBetScore.get(2));
            tv_tie_integer.setText("" + currentBetScore.get(2));
            List<Integer> _tieChip = new ArrayList<>();
            _tieChip.addAll(BaccaratUtil.addChip(currentBetScore.get(2)));
            for (Integer integer : _tieChip) {
                cv_tie_chip.addChip(integer);
            }
            if (cv_tie_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_tie_chip.getLayoutParams();
                if (cv_tie_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_tie_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_tie_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_tie_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_tie_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_tie_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_tie_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_tie_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_tie_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_tie_integer.setLayoutParams(tvParams);
            }
        }
    }

    private void currentChip() {
        if (currentBetScore.get(1) == 0) {
            rl_player_chip.setVisibility(View.GONE);
            cv_player_chip.addChip(0);
            perPlayer = currentBetScore.get(1);
            playerScore = currentBetScore.get(1);
            chipScore.put(1, currentBetScore.get(1));
        } else if (currentBetScore.get(1) != perPlayer) {
            rl_player_chip.setVisibility(View.VISIBLE);
            perPlayer = currentBetScore.get(1);
            cv_player_chip.addChip(0);
            chipScore.put(1, currentBetScore.get(1));
            tv_player_integer.setText("" + currentBetScore.get(1));
            List<Integer> _playerChip = new ArrayList<>();
            _playerChip.addAll(BaccaratUtil.addChip(currentBetScore.get(1)));
            for (Integer integer : _playerChip) {
                cv_player_chip.addChip(integer);
            }
            if (cv_player_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_chip.getLayoutParams();
                if (cv_player_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_player_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_player_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_player_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_player_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_player_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_player_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_player_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_player_integer.setLayoutParams(tvParams);
            }
        }
        if (currentBetScore.get(0) == 0) {
            rl_banker_chip.setVisibility(View.GONE);
            cv_banker_chip.addChip(0);
            perBanker = currentBetScore.get(0);
            bankerScore = currentBetScore.get(0);
            chipScore.put(0, currentBetScore.get(0));
        } else if (currentBetScore.get(0) != perBanker) {
            rl_banker_chip.setVisibility(View.VISIBLE);
            perBanker = currentBetScore.get(0);
            cv_banker_chip.addChip(0);
            chipScore.put(0, currentBetScore.get(0));
            tv_banker_integer.setText("" + currentBetScore.get(0));
            List<Integer> _bankerChip = new ArrayList<>();
            _bankerChip.addAll(BaccaratUtil.addChip(currentBetScore.get(0)));
            for (Integer integer : _bankerChip) {
                cv_banker_chip.addChip(integer);
            }
            if (cv_banker_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_chip.getLayoutParams();
                if (cv_banker_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_banker_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_banker_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_banker_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_banker_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_banker_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_banker_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_banker_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_banker_integer.setLayoutParams(tvParams);
            }
        }
        if (currentBetScore.get(2) == 0) {
            rl_tie_chip.setVisibility(View.GONE);
            cv_tie_chip.addChip(0);
            perTie = currentBetScore.get(2);
            tieScore = currentBetScore.get(2);
            chipScore.put(2, currentBetScore.get(2));
        } else if (currentBetScore.get(2) != perTie) {
            LogUtil.i("当前下注=5" + currentBetScore.toString());
            rl_tie_chip.setVisibility(View.VISIBLE);
            perTie = currentBetScore.get(2);
            cv_tie_chip.addChip(0);
            chipScore.put(2, currentBetScore.get(2));
            tv_tie_integer.setText("" + currentBetScore.get(2));
            List<Integer> _tieChip = new ArrayList<>();
            _tieChip.addAll(BaccaratUtil.addChip(currentBetScore.get(2)));
            for (Integer integer : _tieChip) {
                cv_tie_chip.addChip(integer);
            }
            if (cv_tie_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_tie_chip.getLayoutParams();
                if (cv_tie_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_tie_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_tie_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_tie_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_tie_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_tie_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_tie_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_tie_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_tie_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_tie_integer.setLayoutParams(tvParams);
            }
        }
    }


    /**
     * 获取服务器历史牌局数据（一般72局，洗牌会根据第一张的结果抽取几张） 在根据珠盘路数据生成展开的珠盘路和缩小的珠盘路
     * <p>
     * 1.初始化所有数据init
     * 2.计算列数cal
     * 3.画出来draw
     */
    private void initMessages() {
        if (beadRoadList.size() > 0) {
            beadRoadList.clear();
        }
        if (bigRoadListAll.size() > 0) {
            bigRoadListAll.clear();
            bigRoadList.clear();
        }
        if (bigEyeRoadListAll.size() > 0) {
            bigEyeRoadListAll.clear();
            bigEyeRoadList.clear();
        }
        if (smallRoadListAll.size() > 0) {
            smallRoadListAll.clear();
            smallRoadList.clear();
        }
        if (cockroachRoadListAll.size() > 0) {
            cockroachRoadListAll.clear();
            cockroachRoadList.clear();
        }
        if (maxScoreList.size() > 0) {
            maxScoreList.clear();
        }
        //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
        int total = boardMessageList.size(), banker = 0, player = 0, tie = 0/*, bankerPair = 0, playerPair = 0, gold = 0*/;

        if (boardMessageList.size() > 0) {
            for (int i = 0; i < boardMessageList.size(); i++) {
                if (boardMessageList.get(i) == 0 || boardMessageList.get(i) == 3 || boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5) {
                    banker++;
                }
                if (boardMessageList.get(i) == 1 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8) {
                    player++;
                }
                if (boardMessageList.get(i) == 2 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11) {
                    tie++;
                }
//                if (boardMessageList.get(i) == 3 || boardMessageList.get(i) == 5 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 8 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 11) {
//                    bankerPair++;
//                }
//                if (boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11) {
//                    playerPair++;
//                }
            }
        }

        tv_in_game_total_value.setText(String.valueOf(total));
        tv_in_game_banker_value.setText(String.valueOf(banker));
        tv_in_game_player_value.setText(String.valueOf(player));
        tv_in_game_tie_value.setText(String.valueOf(tie));

//        AlgorithmMacau.getInstance().initBigRoad(boardMessageList, beadRoadList, beadRoadListShort,
//                bigRoadListAll, bigRoadList, bigRoadListShort,
//                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
//                smallRoadListAll, smallRoadList, smallRoadListShort,
//                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort, maxScoreListAll, maxScoreList, mContext, 2, false,
//                null, null, null, null, null, null,
//                0, 0, 0);//1路单 2视频
        AlgorithmMacau.getInstance().initBigRoad(boardMessageList, beadRoadList, beadRoadListShort,
                bigRoadListAll, bigRoadList, bigRoadListShort,
                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
                smallRoadListAll, smallRoadList, smallRoadListShort,
                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort,
                maxScoreListAll, maxScoreList, mContext, 2, false,
                iv_ask_bank_1, iv_ask_bank_2, iv_ask_bank_3, iv_ask_play_1, iv_ask_play_2, iv_ask_play_3,
                0, 0, 0);//1路单 2视频
    }

    /**
     * 下注
     *
     * @param clickable true 可下注
     */
    private void onClick(boolean clickable) {
        rl_player.setClickable(clickable);
        rl_tie.setClickable(clickable);
        rl_banker.setClickable(clickable);
        rl_sure.setClickable(clickable);
    }

    private void onInitImg(ImageView iv) {
//        chipImgList.add(iv);
        setImgOnClick(iv);
    }

    private void setImgOnClick(final ImageView iv) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyYAnimation animation = new MyYAnimation();
                animation.setRepeatCount(Animation.INFINITE);
                for (int i = 0; i < chipImgList.size(); i++) {
                    chipImgList.get(i).clearAnimation();
                }
                ImageView _iv = (ImageView) v;
                if (_iv != null) {
                    String tag = (String) _iv.getTag();
                    String[] _tag = tag.split("_");
                    LogUtil.i("选择筹码值 " + Integer.parseInt(_tag[2]));
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.SELECT_CHIP_NUMBER_DT, Integer.parseInt(_tag[2]));
                    _iv.startAnimation(animation);
                }
            }
        });
    }

    private void initChip() {
        MyYAnimation animation = new MyYAnimation();
        animation.setRepeatCount(Animation.INFINITE);
        int chipDefault = SharedPreUtil.getInstance(mContext).getInt(Constant.SELECT_CHIP_NUMBER_DT);
        int realValue = Integer.parseInt(((String) chipImgList.get(0).getTag()).split("_")[2]);
        LogUtil.i("chipDefault-dt=" + chipDefault + " realValue=" + realValue);
        if (chipDefault == 0 || chipDefault < realValue) {
            String _tag = (String) chipImgList.get(0).getTag();
            int minChipValue = Integer.parseInt(_tag.split("_")[2]);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.SELECT_CHIP_NUMBER_DT, minChipValue);
            iv_chip_10.startAnimation(animation);
        } else {
            int chip = SharedPreUtil.getInstance(mContext).getInt(Constant.SELECT_CHIP_NUMBER_DT);
            for (int i = 0; i < chipImgList.size(); i++) {
                String tag = (String) chipImgList.get(i).getTag();
                String _tag = tag.split("_")[2];
                if (_tag.equals(String.valueOf(chip))) {
                    chipImgList.get(i).startAnimation(animation);
                }
            }
        }
    }

    private void initEvent() {
        rl_ask_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask(0, false, true, 4, true);
            }
        });
        rl_ask_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask(1, false, true, 4, true);
            }
        });
        tv_robot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSwitchRobot == false) {
                    isRobot = true;
                    isSwitchRobot = true;
                } else if (isSwitchRobot == false && isRobot == false) {
                    isRobot = false;
                } else if (isSwitchRobot == true) {
                    isRobot = false;
                    isSwitchRobot = false;
                }
                betRobot();
            }
        });
        rl_video_select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_video_select1.setRotation(180f);//图片向下旋转
                upPopupWindow(v, iv_video_select1);
            }
        });
        rl_video_select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_video_select2.setRotation(180f);//图片向下旋转
                upPopupWindow(v, iv_video_select2);
            }
        });
        tv_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 1);
                startActivity(new Intent(mContext, LocalHtmlWebViewActivity.class).putExtra("selectBac", 1));
            }
        });
        rl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChip = false;
                isAllRemain = false;
                isFirstPlay = true;
                isFirstBank = true;
                isFirstTie = true;
                bankerScore = 0;
                playerScore = 0;
                tieScore = 0;
                if (mSocketProdict != null && mSocketProdict.connected()) {
                    LogUtil.i("mSocketProdict 预押注3=" + chipScore.toString());
                    mSocketProdict.emit("prodict_score", "{'room':" + roomId + ",'player':" + token +
                            ",'score':[" + playerScore + "," + bankerScore + "," + tieScore + " ],'clean':1}");
                }
                isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
                if (isSame) {
                    cleanChip();
                } else
                    currentChip();
                int allScore = currentBetScore.get(0) + currentBetScore.get(1) + currentBetScore.get(2);
                tv_bet_all_score.setText(String.valueOf(allScore));
            }
        });
        rl_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChip) {
                    List<Integer> allPer = new ArrayList<>();
                    allPer.clear();
                    allPer.add(perBanker);
                    allPer.add(perPlayer);
                    allPer.add(perTie);
                    Collections.sort(allPer);
                    LogUtil.i("allPer=" + allPer.toString());//升序
                    if (cv_player_chip.getChipNumSize() > 0) {
                        rl_player_chip.setVisibility(View.VISIBLE);
                        int total = perPlayer + perTie + perBanker;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeTwo = allPer.get(0) + allPer.get(1);
                            if (_beforeTwo < _currentMoney) {
                                if (perPlayer == allPer.get(2)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1));
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                    sameBetPlayer(_currentMoney);
                                } else {
                                    chipScore.put(1, perPlayer);
                                }
                            } else if (_beforeTwo == _currentMoney) {
                                if (perPlayer == allPer.get(2)) {
                                    cv_player_chip.addChip(0);
                                    chipScore.put(1, 0);
                                    rl_player_chip.setVisibility(View.GONE);
                                } else {
                                    chipScore.put(1, perPlayer);
                                }
                            } else if (_beforeTwo > _currentMoney) {
                                int _beforeOne = allPer.get(0);
                                if (_beforeOne < _currentMoney) {
                                    if (perPlayer == allPer.get(1)) {
                                        _currentMoney = _currentMoney - allPer.get(0);
                                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                        sameBetPlayer(_currentMoney);
                                    } else if (perPlayer == allPer.get(0)) {
                                        chipScore.put(1, perPlayer);
                                    }
                                } else if (_beforeOne == _currentMoney) {
                                    if (perPlayer == allPer.get(1)) {
                                        cv_player_chip.addChip(0);
                                        chipScore.put(1, 0);
                                        rl_player_chip.setVisibility(View.GONE);
                                    } else if (perPlayer == allPer.get(0)) {
                                        chipScore.put(1, perPlayer);
                                    }
                                }
                            }
                        } else {
                            chipScore.put(1, perPlayer);
                        }
                    }
                    if (cv_tie_chip.getChipNumSize() > 0) {
                        rl_tie_chip.setVisibility(View.VISIBLE);
                        int total = perPlayer + perTie + perBanker;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeTwo = allPer.get(0) + allPer.get(1);
                            if (_beforeTwo < _currentMoney) {
                                if (perTie == allPer.get(2)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1));
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                    sameBetTie(_currentMoney);
                                } else if (perTie == allPer.get(1)) {
                                    chipScore.put(2, perTie);
                                }
                            } else if (_beforeTwo == _currentMoney) {
                                if (perTie == allPer.get(2)) {
                                    cv_tie_chip.addChip(0);
                                    chipScore.put(2, 0);
                                    rl_tie_chip.setVisibility(View.GONE);
                                } else if (perTie == allPer.get(1)) {
                                    chipScore.put(2, perTie);
                                }
                            } else if (_beforeTwo > _currentMoney) {
                                int _beforeOne = allPer.get(0);
                                if (_beforeOne < _currentMoney) {
                                    if (perTie == allPer.get(1)) {
                                        _currentMoney = _currentMoney - allPer.get(0);
                                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                        sameBetTie(_currentMoney);
                                    } else if (perTie == allPer.get(0)) {
                                        chipScore.put(2, perTie);
                                    }
                                } else if (_beforeOne == _currentMoney) {
                                    if (perTie == allPer.get(1)) {
                                        cv_tie_chip.addChip(0);
                                        chipScore.put(2, 0);
                                        rl_tie_chip.setVisibility(View.GONE);
                                    } else if (perTie == allPer.get(0)) {
                                        chipScore.put(2, perTie);
                                    }
                                }
                            }
                        } else {
                            chipScore.put(2, perTie);
                        }
                    }
                    if (cv_banker_chip.getChipNumSize() > 0) {
                        rl_banker_chip.setVisibility(View.VISIBLE);
                        int total = perPlayer + perTie + perBanker;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeTwo = allPer.get(0) + allPer.get(1);
                            if (_beforeTwo < _currentMoney) {
                                if (perBanker == allPer.get(2)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1));
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                    sameBetBanker(_currentMoney);
                                } else if (perBanker == allPer.get(1)) {
                                    chipScore.put(0, perBanker);
                                }
                            } else if (_beforeTwo == _currentMoney) {
                                if (perBanker == allPer.get(2)) {
                                    cv_banker_chip.addChip(0);
                                    chipScore.put(0, 0);
                                    rl_banker_chip.setVisibility(View.GONE);
                                } else if (perBanker == allPer.get(1)) {
                                    chipScore.put(0, perBanker);
                                }
                            } else if (_beforeTwo > _currentMoney) {
                                int _beforeOne = allPer.get(0);
                                if (_beforeOne < _currentMoney) {
                                    if (perBanker == allPer.get(1)) {
                                        _currentMoney = _currentMoney - allPer.get(0);
                                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                        sameBetBanker(_currentMoney);
                                    } else if (perBanker == allPer.get(0)) {
                                        chipScore.put(0, perBanker);
                                    }
                                } else if (_beforeOne == _currentMoney) {
                                    if (perBanker == allPer.get(1)) {
                                        cv_banker_chip.addChip(0);
                                        chipScore.put(0, 0);
                                        rl_banker_chip.setVisibility(View.GONE);
                                    } else if (perBanker == allPer.get(0)) {
                                        chipScore.put(0, perBanker);
                                    }
                                }
                            }
                        } else {
                            chipScore.put(0, perBanker);
                        }
                    }
                    isChip = false;
                    isSame = true;
                    isChipClick(true, true, R.drawable.button_bet_normal_bg, R.drawable.button_bet_normal_bg, R.string.confirm);
                    SoundPoolUtil.getInstance().play("add_chips");
                    LogUtil.i("当前下注续押" + chipScore.toString());
                    int allScore = perPlayer + perBanker + perTie;
                    tv_bet_all_score.setText(String.valueOf(allScore));
                    if (mSocketProdict != null && mSocketProdict.connected()) {
                        LogUtil.i("mSocketProdict 预押注4=" + chipScore.toString());
                        mSocketProdict.emit("prodict_score", "{'room':" + roomId + ",'player':" + token +
                                ",'score':[" + perPlayer + "," + perBanker + "," + perTie + "],'clean':0}");

                    }
                } else {
                    mSocket = DtSocketController.getInstance().getSocket();
                    if (mSocket != null && mSocket.connected()) {
                        for (int i = 0; i < 3; i++) {
                            if (chipScore.get(i) == null) {
                                chipScore.put(i, 0);
                            }
                        }
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK, chipScore.get(0));
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY, chipScore.get(1));
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_TIE, chipScore.get(2));
                        //(0闲 1庄 2和 3闲对 4庄对)
                        LogUtil.i("当前下注=0" + currentBetScore.toString() + " perBanker=" + perBanker + " perPlayer=" + perPlayer + " perTie=" + perTie + " 每局下注=" + chipScore.toString());
                        mSocket.emit("dobet", "{'bets':[" + chipScore.get(0) + "," + chipScore.get(1) + "," + chipScore.get(2) + "],'freeCommission':'" + free + "'}");
                    }
                }
            }
        });

        //-----------------------------------------------------------------点击对应下注区域-start--------------------------------------------
        rl_player.setOnClickListener(new View.OnClickListener() {//闲
            @Override
            public void onClick(View v) {
                int selectChipNumber = initChipLayout();
                LogUtil.i("chipDefault-xian=" + selectChipNumber);
                if (isFirstPlay && minBetList.size() > 0 && selectChipNumber < minBetList.get(0) && perPlayer == 0) {//0 庄闲 1 和 2 对子 3大小
                    selectChipNumber = minBetList.get(0);
                    isFirstPlay = false;
                }
                playerScore += selectChipNumber;
                perPlayer += selectChipNumber;
                int total = perPlayer + perTie + perBanker;
                if (insufficientBalance(0)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
//                int _currentMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (total > limitMoney) {
                    if (/*isAllRemain &&*/ limitMoney < selectChipNumber) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
//                        return;
                    }
                    _currentMoney = _currentMoney - (total - selectChipNumber);
                    if (_currentMoney < selectChipNumber /*&& !isAllRemain*/) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        int currentValue = perPlayer - selectChipNumber + Math.abs(_currentMoney);
                        tv_player_integer.setText(String.valueOf(currentValue));
                        chipScore.put(1, (playerScore - selectChipNumber + _currentMoney));
                        betPlayer(_currentMoney);
                    }
                } else {
                    if (isSame == false) {
                        chipScore.put(1, playerScore);//每局下注分数
                    } else {
                        chipScore.put(1, perPlayer);//续押分数
                    }
                    if (cv_player_chip.getChipNumSize() < 15) {
                        rl_player_chip.setVisibility(View.VISIBLE);
                        cv_player_chip.addChip(selectChipNumber);
                        tv_player_integer.setText(String.valueOf(perPlayer));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_chip.getLayoutParams();
                        if (cv_player_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_player_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_player_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_player_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_player_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_player_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_player_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_player_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_player_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perTie;
                tv_bet_all_score.setText(String.valueOf(allScore));
                emitProdict();
            }
        });
        rl_tie.setOnClickListener(new View.OnClickListener() {//和
            @Override
            public void onClick(View v) {
                int selectChipNumber = initChipLayout();
                if (isFirstTie && minBetList.size() > 0 && selectChipNumber < minBetList.get(1) && perTie == 0) {//0 庄闲 1 和 2 对子 3大小
                    selectChipNumber = minBetList.get(1);
                    isFirstTie = false;
                }
                tieScore += selectChipNumber;
                perTie += selectChipNumber;
                int total = perPlayer + perTie + perBanker;
                if (insufficientBalance(1)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
//                int _currentMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (total > limitMoney) {
                    if (/*isAllRemain && */limitMoney < selectChipNumber) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
//                        return;
                    }
                    _currentMoney = _currentMoney - (total - selectChipNumber);
                    if (_currentMoney < selectChipNumber/* && !isAllRemain*/) {
                        LogUtil.i("1total=" + total + " _currentMoney=" + _currentMoney);
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        int currentValue = perTie - selectChipNumber + Math.abs(_currentMoney);
                        tv_tie_integer.setText(String.valueOf(currentValue));
                        chipScore.put(2, (tieScore - selectChipNumber + _currentMoney));
                        betTie(_currentMoney);
                    }
                } else {
                    if (isSame == false) {
                        chipScore.put(2, tieScore);
                    } else {
                        chipScore.put(2, perTie);//续押分数
                    }
                    if (cv_tie_chip.getChipNumSize() < 15) {
                        rl_tie_chip.setVisibility(View.VISIBLE);
                        cv_tie_chip.addChip(selectChipNumber);
                        tv_tie_integer.setText(String.valueOf(perTie));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_tie_chip.getLayoutParams();
                        if (cv_tie_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_tie_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_tie_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_tie_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_tie_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_tie_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_tie_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_tie_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_tie_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_tie_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perTie;
                tv_bet_all_score.setText(String.valueOf(allScore));
                emitProdict();
            }
        });
        rl_banker.setOnClickListener(new View.OnClickListener() {//庄
            @Override
            public void onClick(View v) {
                int selectChipNumber = initChipLayout();
                if (isFirstBank && minBetList.size() > 0 && selectChipNumber < minBetList.get(0) && perBanker == 0) {//0 庄闲 1 和 2 对子 3大小
                    selectChipNumber = minBetList.get(0);
                    isFirstBank = false;
                }
                bankerScore += selectChipNumber;
                perBanker += selectChipNumber;
                int total = perPlayer + perTie + perBanker;
                if (insufficientBalance(0)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
//                int _currentMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (total > limitMoney) {
                    if (/*isAllRemain && */limitMoney < selectChipNumber) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
//                        return;
                    }
                    _currentMoney = _currentMoney - (total - selectChipNumber);//多出来的分数
                    if (_currentMoney < selectChipNumber /*&& !isAllRemain*/) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        int currentValue = perBanker - selectChipNumber + Math.abs(_currentMoney);
                        tv_banker_integer.setText(String.valueOf(currentValue));//当前局总分数
                        chipScore.put(0, (bankerScore - selectChipNumber + _currentMoney));//每次的投注分数
                        betBanker(_currentMoney);
                    }
                } else {
                    if (isSame == false) {
                        chipScore.put(0, bankerScore);
                    } else {
                        chipScore.put(0, perBanker);//续押分数
                    }
                    if (cv_banker_chip.getChipNumSize() < 15) {
                        rl_banker_chip.setVisibility(View.VISIBLE);
                        cv_banker_chip.addChip(selectChipNumber);
                        tv_banker_integer.setText(String.valueOf(perBanker));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_chip.getLayoutParams();
                        if (cv_banker_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_banker_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_banker_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_banker_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_banker_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_banker_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_banker_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_banker_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_banker_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perTie;
                tv_bet_all_score.setText(String.valueOf(allScore));
                emitProdict();
            }
        });
        //-----------------------------------------------------------------点击对应下注区域-end--------------------------------------------
        tv_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 1);
                startActivity(new Intent(mContext, BetRecordListActivity.class));
            }
        });
        tv_game_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 1);
                Intent intent = new Intent(mContext, LdRoadActivity.class);
                intent.putIntegerArrayListExtra("boardMessageList", (ArrayList<Integer>) boardMessageList);
                intent.putIntegerArrayListExtra("maxScoreList", (ArrayList<Integer>) maxScoreListAll);
                LogUtil.i("报错1" + maxScoreList.size() + "boardMessageList=" + boardMessageList.size());
                intent.putExtra("roomName", roomName);
                intent.putExtra("skyCard", skyCard);
                startActivity(intent);
            }
        });
    }

    private void emitProdict() {
        if (mSocketProdict != null && mSocketProdict.connected()) {
            LogUtil.i("mSocketProdict 预押注1=" + chipScore.toString());
            mSocketProdict.emit("prodict_score", "{'room':" + roomId + ",'player':" + token +
                    ",'score':[" + chipScore.get(1) + "," + chipScore.get(0) + "," + chipScore.get(2) + "," + chipScore.get(4) + "," + chipScore.get(3) + ",0,0],'clean':0}");
        } else {
            mSocketProdict = DtSocketController.getInstance().getSocketProdict();
            if (mSocketProdict != null && mSocketProdict.connected()) {
                LogUtil.i("mSocketProdict 预押注2=" + chipScore.toString());
                mSocketProdict.emit("prodict_score", "{'room':" + roomId + ",'player':" + token +
                        ",'score':[" + chipScore.get(1) + "," + chipScore.get(0) + "," + chipScore.get(2) + "," + chipScore.get(4) + "," + chipScore.get(3) + ",0,0],'clean':0}");
            }
        }
    }

    private void betPlayer(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_player_chip.addChip(integer);
        }
        rl_player_chip.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_chip.getLayoutParams();
        if (cv_player_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
        } else if (cv_player_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
        } else if (cv_player_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
        } else if (cv_player_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
        } else if (cv_player_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
        } else if (cv_player_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
        } else if (cv_player_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_player_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_player_integer.setLayoutParams(tvParams);
    }

    private void betTie(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_tie_chip.addChip(integer);
        }
        rl_tie_chip.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_tie_chip.getLayoutParams();
        if (cv_tie_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
        } else if (cv_tie_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
        } else if (cv_tie_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
        } else if (cv_tie_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
        } else if (cv_tie_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
        } else if (cv_tie_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
        } else if (cv_tie_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_tie_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_tie_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_tie_integer.setLayoutParams(tvParams);
    }

    private void betBanker(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_banker_chip.addChip(integer);
        }

        rl_banker_chip.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_chip.getLayoutParams();
        if (cv_banker_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
        } else if (cv_banker_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
        } else if (cv_banker_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
        } else if (cv_banker_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
        } else if (cv_banker_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
        } else if (cv_banker_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
        } else if (cv_banker_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_banker_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_banker_integer.setLayoutParams(tvParams);
    }

    private void betRobot() {
        //1.先判断余额够吗
        //2.20-10000随机金额
        //3.5个位置选一个下注
        //4.洗牌不能托管 手动下注时成功后当前局不能托管
        if (isSwitchRobot) {
            tv_robot.setText("托管中");
        } else {
            tv_robot.setText(mContext.getResources().getString(R.string.robot));
            return;
        }
        double currentMoney = Double.parseDouble(tv_money.getText().toString());
        if (currentMoney < 50) {
            ToastUtil.show(mContext, "托管时，余额不能低于50");
            return;
        }
        if (state.equals(Constant.BACCARAT_BET) && isSwitchRobot) {
            int[] moneyArr = {20, 50, 100, 200, 500};

            int randomMoney = new Random().nextInt(5);//随机生成 0到4
            int randomSite = new Random().nextInt(2);//随机生成0和1
            chipScore.clear();
            chipScore.put(randomSite, moneyArr[randomMoney]);//随机位置，随机金额
            LogUtil.i("托管=" + chipScore.toString() + " state=" + state);

            mSocket = DtSocketController.getInstance().getSocket();
            if (mSocket != null && mSocket.connected()) {
                for (int i = 0; i < 3; i++) {
                    if (chipScore.get(i) == null) {
                        chipScore.put(i, 0);
                    }
                }
            }
            if (mSocket != null && mSocket.connected()) {
                if (randomSite == 0) {//龙
                    betBanker(randomMoney);
                } else if (randomSite == 1) {//虎
                    betPlayer(randomMoney);
                } else if (randomSite == 2) {//和
                    betTie(randomMoney);
                }
                mSocket.emit("dobet", "{'bets':[" + chipScore.get(0) + "," + chipScore.get(1) + "," + chipScore.get(2) + "],'freeCommission':'" + free + "'}");
            }
        }
    }

    /**
     * 当前余额-所有已下注积分<对应最小下注积分
     *
     * @param i
     * @return
     */
    private boolean insufficientBalance(int i) {
        int _allBet = Integer.parseInt(tv_bet_all_score.getText().toString());
        if (minBetList.size() > 0 && Double.parseDouble(tv_money.getText().toString()) < minBetList.get(i)) {
            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.balance_not_enough));
            if (_allBet > 0) {
                isChipClick(true, true, R.drawable.button_bet_normal_bg, R.drawable.button_bet_normal_bg, R.string.confirm);
            } else {
                isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            }
            return true;
        }
        return false;
    }

    /**
     * 取消 确认的下注按钮样式
     *
     * @param b1        取消是否可点击
     * @param b2        确定是否可点击
     * @param drawable1 取消背景
     * @param drawable2 确定背景
     * @param string    确定还是重复
     */
    private void isChipClick(boolean b1, boolean b2, int drawable1, int drawable2, int string) {
        rl_cancel.setClickable(b1);
        rl_sure.setClickable(b2);
        rl_cancel.setBackgroundResource(drawable1);
        rl_sure.setBackgroundResource(drawable2);
        tv_sure.setText(mContext.getResources().getString(string));
    }

    private void sameBetBanker(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_banker_chip.addChip(integer);
        }
        rl_banker_chip.setVisibility(View.VISIBLE);
        tv_banker_integer.setText(String.valueOf(_currentMoney));
        chipScore.put(0, _currentMoney);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_chip.getLayoutParams();
        if (cv_banker_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
        } else if (cv_banker_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
        } else if (cv_banker_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
        } else if (cv_banker_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
        } else if (cv_banker_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
        } else if (cv_banker_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
        } else if (cv_banker_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_banker_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_banker_integer.setLayoutParams(tvParams);
    }

    private void sameBetPlayer(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_player_chip.addChip(integer);
        }
        rl_player_chip.setVisibility(View.VISIBLE);
        tv_player_integer.setText(String.valueOf(_currentMoney));
        chipScore.put(1, _currentMoney);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_chip.getLayoutParams();
        if (cv_player_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
        } else if (cv_player_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
        } else if (cv_player_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
        } else if (cv_player_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
        } else if (cv_player_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
        } else if (cv_player_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
        } else if (cv_player_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_player_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_player_integer.setLayoutParams(tvParams);
    }

    private void sameBetTie(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_tie_chip.addChip(integer);
        }
        rl_tie_chip.setVisibility(View.VISIBLE);
        tv_tie_integer.setText(String.valueOf(_currentMoney));
        chipScore.put(2, _currentMoney);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_tie_chip.getLayoutParams();
        if (cv_tie_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
        } else if (cv_tie_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
        } else if (cv_tie_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
        } else if (cv_tie_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
        } else if (cv_tie_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
        } else if (cv_tie_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
        } else if (cv_tie_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_tie_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_tie_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_tie_integer.setLayoutParams(tvParams);
    }

    private int initChipLayout() {
        int selectChipNumber = SharedPreUtil.getInstance(mContext).getInt(Constant.SELECT_CHIP_NUMBER_DT);
        if (isChip) {
            cleanChip();
        }
        isChipClick(true, true, R.drawable.button_bet_normal_bg, R.drawable.button_bet_normal_bg, R.string.confirm);
        SoundPoolUtil.getInstance().play("add_chips");
        return selectChipNumber;
    }

    private void cleanChip() {
        //下注成功的 就不能清除
        chipScore.put(0, currentBetScore.get(0));
        chipScore.put(1, currentBetScore.get(1));
        chipScore.put(2, currentBetScore.get(2));
        cv_player_chip.addChip(0);
        cv_banker_chip.addChip(0);
        cv_tie_chip.addChip(0);
        rl_sure.setBackgroundResource(R.drawable.button_bet_disable_bg);
        rl_cancel.setBackgroundResource(R.drawable.button_bet_disable_bg);

//        playerPairScore = 0;
//        bankerPairScore = 0;
        playerScore = 0;
        tieScore = 0;
        bankerScore = 0;
        perBanker = 0;
        perPlayer = 0;
        perTie = 0;
//        perBankerPair = 0;
//        perPlayerPair = 0;
        chipScore.clear();
        isChip = false;
        isSame = false;

        rl_tie_chip.setVisibility(View.GONE);
        rl_player_chip.setVisibility(View.GONE);
        rl_banker_chip.setVisibility(View.GONE);
        LogUtil.i("cleanChip=" + cv_player_chip.getChipNumSize());
    }

    private void initEn() {
        int languageType = CommSharedUtil.getInstance(this).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_EN) {
            tv_manual.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font8));
            tv_records.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font8));
            tv_game_rec.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font8));
            tv_exit.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.font8));
        }
    }

    //---------------------------------------------视频start------------------------------------------------------------
    private static final String TAG = "DtActivityVideo";
    private SurfaceView mSv1 = null;
    private long playerHandle = 0;
    private SmartPlayerJniV2 libPlayer = null;
    private boolean isHardwareDecoder = Constant.DANIU_HARDWARE;//true 硬件解码  false 软件解码

    private int playBuffer = Constant.DANIU_BUFFER; // 默认200ms 已改为0
    private boolean isLowLatency = true; // 超低延时，默认不开启  已改为开启
    private boolean isFastStartup = true; // 是否秒开, 默认true
    private int rotate_degrees = 0;//视频顺时针旋转角度
    private boolean isPlaying = false;//默认未播放
    private int time = 0;
    private Baccarat mBaccarat;
    private String hVideo = "";
    private String lVideo = "";

    public void init(Context context) {
        mContext = context;
        libPlayer = new SmartPlayerJniV2();
    }

    public void copyWidget(SurfaceView sv1, TextView tv_middle_sv_l, TextView tv_middle_sv_h, TextView tv_big_sv_l, TextView tv_big_sv_h,
                           TextView tv_exit, Baccarat baccarat) {
        this.tv_middle_sv_l = tv_middle_sv_l;
        this.tv_middle_sv_h = tv_middle_sv_h;
        this.tv_big_sv_l = tv_big_sv_l;
        this.tv_big_sv_h = tv_big_sv_h;
        this.tv_exit = tv_exit;
        this.mBaccarat = baccarat;
        initBacDataVideo(mBaccarat);
        this.mSv1 = CreateView(sv1);
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.CLOSE_ALL_VIDEO) == 0) {
            autoPlayer(hVideo);//在控件上播放
        }
        initVideoEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.BAC_VIDEO_PLAYING) == 1) {
            libPlayer.SmartPlayerSetOrientation(playerHandle, 2);//大牛直播 横竖屏切换重走生命周期
        }
    }

    public void initBacDataVideo(Baccarat baccarat) {
        if (baccarat != null && baccarat.rtmp != null && baccarat.rtmp.length != 0) {
            String[] _url = baccarat.rtmp;
            hVideo = "rtmp://" + _url[1];
//            hVideo = "rtmp://live.hkstv.hk.lxdns.com/live/hks1";
            lVideo = "rtmp://" + _url[0];
            LogUtil.i("视频地址" + hVideo + " lVideo=" + lVideo);
        }
    }

    private void initVideoEvent() {
        mSv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time == 0) {
                    zoomQuarterVideo();
                } else if (time == 1) {
                    zoomFullVideo();
                } else if (time == 2) {
                    zoomReturnVideo();
                }
            }
        });
        tv_middle_sv_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPlay(lVideo);
                LogUtil.i("视频切换 tv_middle_sv_l" + lVideo);
                ToastUtil.show(mContext, mContext.getResources().getString(R.string.vv_2));
            }
        });
        tv_middle_sv_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPlay(hVideo);
                LogUtil.i("视频切换 tv_middle_sv_h" + hVideo);
                ToastUtil.show(mContext, mContext.getResources().getString(R.string.vv_1));
            }
        });
        tv_big_sv_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPlay(lVideo);
                LogUtil.i("视频切换 tv_big_sv_l" + lVideo);
                ToastUtil.show(mContext, mContext.getResources().getString(R.string.vv_2));
            }
        });
        tv_big_sv_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPlay(hVideo);
                LogUtil.i("视频切换 tv_big_sv_h" + hVideo);
                ToastUtil.show(mContext, mContext.getResources().getString(R.string.vv_1));
            }
        });
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreUtil.getInstance(mContext).getInt(Constant.IS_CHIP_SUCCESS) != 0) {
                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.normal_lock_alert));
                    return;
                }
                SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 0);
                libPlayer.SmartPlayerClose(playerHandle);//退出要及时关闭 下次就不会出现重音
                mContext.startActivity(new Intent(mContext, DtListActivity2.class));
                ((DtActivity) mContext).finish();
            }
        });
    }

    /**
     * 下注-视频还原
     */
    private void zoomReturnVideo() {
        if (getSDK_INT() > 23) {
            AnimatorSet animatorSetVideo = new AnimatorSet();//组合动画
            float toX3 = Float.parseFloat(BaccaratUtil.getInstance().getScaleX((fl_sv.getWidth() - mContext.getResources().getDimensionPixelOffset(R.dimen.unit2)) * scaleX, mSv1.getWidth()));
            float toY3 = Float.parseFloat(BaccaratUtil.getInstance().getScaleX((fl_sv.getHeight() - mContext.getResources().getDimensionPixelOffset(R.dimen.unit2)), mSv1.getHeight()));
            ObjectAnimator _scaleX = ObjectAnimator.ofFloat(mSv1, "scaleX", 1f, toX3);
            ObjectAnimator _scaleY = ObjectAnimator.ofFloat(mSv1, "scaleY", 1f, toY3);
            mSv1.setPivotX(mSv1.getWidth());
            mSv1.setPivotY(0f);
            animatorSetVideo.setDuration(600);
            _scaleX.setRepeatMode(ObjectAnimator.RESTART);
            _scaleY.setRepeatMode(ObjectAnimator.RESTART);
            animatorSetVideo.setInterpolator(new DecelerateInterpolator());
            animatorSetVideo.play(_scaleX).with(_scaleY);//两个动画同时开始
            animatorSetVideo.start();
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSv1.getLayoutParams();
            if (rl_test.getWidth() > 5) {
                layoutParams.width = (int) (fl_sv.getWidth() * scaleX - mContext.getResources().getDimensionPixelOffset(R.dimen.unit2));//0.132 0.135
            } else {
                layoutParams.width = fl_sv.getWidth() - mContext.getResources().getDimensionPixelOffset(R.dimen.unit2);
            }
            if (displayMetrics.densityDpi == 160) {
                layoutParams.height = (int) (fl_sv.getHeight() * topScaleY) - mContext.getResources().getDimensionPixelOffset(R.dimen.unit2);
            } else {
                layoutParams.height = fl_sv.getHeight() - mContext.getResources().getDimensionPixelOffset(R.dimen.unit2);
            }
            layoutParams.addRule(RelativeLayout.LEFT_OF, v_divide.getId());
            mSv1.setLayoutParams(layoutParams);
        }

        tv_big_sv_l.setVisibility(View.GONE);
        tv_big_sv_h.setVisibility(View.GONE);
        tv_middle_sv_l.setVisibility(View.GONE);
        tv_middle_sv_h.setVisibility(View.GONE);
        rl_video_select1.setVisibility(View.GONE);
        rl_video_select2.setVisibility(View.GONE);
        time = 0;
    }

    /**
     * 发牌-视频宽高各一半
     */
    private void zoomQuarterVideo() {
        tv_middle_sv_l.setVisibility(View.VISIBLE);
        tv_middle_sv_h.setVisibility(View.VISIBLE);
        tv_big_sv_l.setVisibility(View.GONE);
        tv_big_sv_h.setVisibility(View.GONE);
        rl_video_select1.setVisibility(View.VISIBLE);
        rl_video_select2.setVisibility(View.GONE);
        time = 1;
        if (getSDK_INT() > 23) {
            AnimatorSet animatorSetVideo = new AnimatorSet();//组合动画
            ObjectAnimator _scaleX = ObjectAnimator.ofFloat(mSv1, "scaleX", 1f, 1f);
            ObjectAnimator _scaleY = ObjectAnimator.ofFloat(mSv1, "scaleY", 1f, 1f);
            float pivotX = mSv1.getWidth();
            mSv1.setPivotX(pivotX);
            mSv1.setPivotY(0);
            animatorSetVideo.setDuration(600);
            _scaleX.setRepeatMode(ObjectAnimator.RESTART);
            _scaleY.setRepeatMode(ObjectAnimator.RESTART);
            animatorSetVideo.setInterpolator(new DecelerateInterpolator());
            animatorSetVideo.play(_scaleX).with(_scaleY);//两个动画同时开始
            animatorSetVideo.start();
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSv1.getLayoutParams();
            layoutParams.width = mContext.getResources().getDimensionPixelOffset(R.dimen.unit270);
            layoutParams.height = mContext.getResources().getDimensionPixelOffset(R.dimen.unit130);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.removeRule(RelativeLayout.LEFT_OF);
            mSv1.setLayoutParams(layoutParams);
        }
    }

    private void zoomFullVideo() {
        tv_middle_sv_l.setVisibility(View.GONE);
        tv_middle_sv_h.setVisibility(View.GONE);
        rl_video_select1.setVisibility(View.GONE);
        time = 2;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_big_sv_l.setVisibility(View.VISIBLE);
                tv_big_sv_h.setVisibility(View.VISIBLE);
                rl_video_select2.setVisibility(View.VISIBLE);
            }
        }, 600);
        if (getSDK_INT() > 23) {
            float toX3 = Float.parseFloat(BaccaratUtil.getInstance().getScaleX(displayMetrics.widthPixels * 1.06f, mSv1.getWidth()));
            float toY3 = Float.parseFloat(BaccaratUtil.getInstance().getScaleX(displayMetrics.heightPixels, mSv1.getHeight()));
            AnimatorSet animatorSetVideo = new AnimatorSet();//组合动画
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mSv1, "scaleX", 1f, toX3);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mSv1, "scaleY", 1f, toY3);
            float pivotX = mSv1.getWidth();
            mSv1.setPivotX(pivotX);
            mSv1.setPivotY(0);
            animatorSetVideo.setDuration(600);
            scaleX.setRepeatMode(ObjectAnimator.RESTART);
            scaleY.setRepeatMode(ObjectAnimator.RESTART);
            animatorSetVideo.setInterpolator(new DecelerateInterpolator());
            animatorSetVideo.play(scaleX).with(scaleY);//两个动画同时开始
            animatorSetVideo.start();
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSv1.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.removeRule(RelativeLayout.LEFT_OF);
            mSv1.setLayoutParams(layoutParams);
        }
    }

    public int getSDK_INT() {
        //大于安卓6.0 SurfaceVIew可以进行缩放
        return Build.VERSION.SDK_INT;
    }

    //---------------------daniu start------------------------
    private SurfaceView CreateView(SurfaceView sv1) {
        if (sv1 == null) {
            sv1 = NTRenderer.CreateRenderer(mContext, false);
        }

        return sv1;
    }

    private void autoPlayer(String switchURL1) {
        InitAndSetConfig(switchURL1);//初始化参数
        libPlayer.SmartPlayerSetSurface(playerHandle, mSv1);
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

    private void InitAndSetConfig(String switchURL1) {
        playerHandle = libPlayer.SmartPlayerOpen(mContext);
//        savePlayerHandle.add(playerHandle);
        LogUtil.i("init playerHandle=" + playerHandle);
        if (playerHandle == 0) {
            return;
        }
        libPlayer.SetSmartPlayerEventCallbackV2(playerHandle, new EventHandleV2());
        libPlayer.SmartPlayerSetBuffer(playerHandle, playBuffer);
        libPlayer.SmartPlayerSetFastStartup(playerHandle, isFastStartup ? 1 : 0);
        if (switchURL1 != null) {
            libPlayer.SmartPlayerSetUrl(playerHandle, switchURL1);
        }
    }

    class EventHandleV2 implements NTSmartEventCallbackV2 {
        @Override
        public void onNTSmartEventCallbackV2(long handle, int id, long param1, long param2, String param3, String param4, Object param5) {

            switch (id) {
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STARTED:
                    LogUtil.i(TAG, "开始。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTING:
                    LogUtil.i(TAG, "连接中。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTION_FAILED:
                    LogUtil.i(TAG, "连接失败。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTED:
                    LogUtil.i(TAG, "连接成功。。");
//                    initNullBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_DISCONNECTED:
                    LogUtil.i(TAG, "连接断开。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STOP:
                    LogUtil.i(TAG, "停止播放。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_RESOLUTION_INFO:
                    LogUtil.i(TAG, "分辨率信息: width: " + param1 + ", height: " + param2 + "   isPlaying=");
//                    initNullBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_NO_MEDIADATA_RECEIVED:
                    LogUtil.i(TAG, "收不到媒体数据，可能是url错误。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_SWITCH_URL:
                    LogUtil.i(TAG, "切换播放URL。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_START_BUFFERING:
                    LogUtil.i(TAG, "Start_Buffering");
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_BUFFERING:
                    LogUtil.i(TAG, "Buffering:" + param1 + "%");
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STOP_BUFFERING:
                    LogUtil.i(TAG, "Stop_Buffering");
                    break;
            }
        }
    }

    private void initDefaultBackground() {
        ((DtActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSv1.setBackground(mContext.getResources().getDrawable(R.drawable.holy_bg_2));
            }
        });
    }

    private void initNullBackground() {
        ((DtActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSv1.setBackground(null);
            }
        });
    }

    //---------------------------------------------视频end------------------------------------------------------------
    private void outGame() {
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_PLAY) == 0 &&
                SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_BANK) == 0 &&
                SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_TIE) == 0 &&
                SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_PLAY_PAIR) == 0 &&
                SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_BANK_PAIR) == 0 &&
                SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_BIG) == 0 &&
                SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_SMALL) == 0) {
            noBetCount++;
            if (noBetCount == 3) {
                hintDialog(mContext.getResources().getString(R.string.no_bet_warning));
            } else if (noBetCount == 5) {
                this.finish();
//                mContext.startActivity(new Intent(mContext, DtListActivity2.class));
            }
        }
    }

    private void hintDialog(String hint) {
        final HintDialog myDialog = new HintDialog(mContext);
        myDialog.setContent(hint);
        myDialog.show();
        myDialog.setAttributes();//需先显示，然后才能查找控件
        myDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
    }

    public void upPopupWindow(View v, final ImageView iv) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.pop_video_select1)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimUp1)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();

        popupWindow.showAsDropDown(v, 0, -(popupWindow.getHeight() + v.getMeasuredHeight()));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv.setRotation(0f);//图片向上旋转
            }
        });
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.pop_video_select1:
                RelativeLayout rl_telecom = (RelativeLayout) view.findViewById(R.id.rl_telecom);
                RelativeLayout rl_mobile = (RelativeLayout) view.findViewById(R.id.rl_mobile);
                RelativeLayout rl_lan = (RelativeLayout) view.findViewById(R.id.rl_lan);
                TextView tv_close = (TextView) view.findViewById(R.id.tv_close);
                final String str = mContext.getResources().getString(R.string.change_channel_resolution);

                rl_telecom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//电信
//                        baccarat.dxRtmp=null;
//                        baccarat.dxRtmp=new String[2];
//                        baccarat.dxRtmp[0]="39.98.177.215/live/lh01b";
//                        baccarat.dxRtmp[1]="39.98.177.215/live/lh01a";
                        if (baccarat.dxRtmp.length != 0 && !TextUtils.isEmpty(baccarat.dxRtmp[0])) {
                            String[] _url = baccarat.dxRtmp;
                            hVideo = "rtmp://" + _url[1];
                            lVideo = "rtmp://" + _url[0];
                            switchPlay(hVideo);
                        } else {
                            ToastUtil.show(mContext, "没有视频！");
                        }
                        popupWindow.dismiss();
                        if (time == 0) {
                            iv_video_select1.setRotation(0f);
                        } else if (time == 1) {
                            iv_video_select2.setRotation(0f);
                        }
                    }
                });

                rl_mobile.setOnClickListener(new View.OnClickListener() {//移动
                    @Override
                    public void onClick(View v) {
                        if (baccarat.ydRtmp.length != 0 && !TextUtils.isEmpty(baccarat.ydRtmp[0])) {
                            String[] _url = baccarat.ydRtmp;
                            hVideo = "rtmp://" + _url[1];
                            lVideo = "rtmp://" + _url[0];
                            switchPlay(hVideo);
                        } else {
                            ToastUtil.show(mContext, "没有视频！");
                        }
                        popupWindow.dismiss();
                        if (time == 0) {
                            iv_video_select1.setRotation(0f);
                        } else if (time == 1) {
                            iv_video_select2.setRotation(0f);
                        }
                    }
                });
                rl_lan.setOnClickListener(new View.OnClickListener() {//局域网
                    @Override
                    public void onClick(View v) {
//                        baccarat.rtmp=null;
//                        baccarat.rtmp=new String[2];
//                        baccarat.rtmp[0]="119.188.246.219/live/bl001a";
//                        baccarat.rtmp[1]="119.188.246.219/live/bl001b";
                        if (baccarat.rtmp.length != 0 && !TextUtils.isEmpty(baccarat.rtmp[0])) {
                            String[] _url = baccarat.rtmp;
                            hVideo = "rtmp://" + _url[1];
                            lVideo = "rtmp://" + _url[0];
                            switchPlay(hVideo);
                        } else {
                            ToastUtil.show(mContext, "没有视频！");
                        }
                        popupWindow.dismiss();
                        if (time == 0) {
                            iv_video_select1.setRotation(0f);
                        } else if (time == 1) {
                            iv_video_select2.setRotation(0f);
                        }
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
                        if (time == 0) {
                            iv_video_select1.setRotation(0f);
                        } else if (time == 1) {
                            iv_video_select2.setRotation(0f);
                        }
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.CLOSE_ALL_VIDEO, 1);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DtSocketController.getInstance().disconnectSocket();
    }
}
