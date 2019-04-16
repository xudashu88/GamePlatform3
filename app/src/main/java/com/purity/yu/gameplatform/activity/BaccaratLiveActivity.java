package com.purity.yu.gameplatform.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuangku.languagemodule.CommSharedUtil;
import com.chuangku.languagemodule.LanguageType;
import com.chuangku.languagemodule.MultiLanguageUtil;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.baccarat.YHZGridView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.controler.BaccaratLiveAnimController;
import com.purity.yu.gameplatform.controler.BaccaratLiveController;
import com.purity.yu.gameplatform.controler.BaccaratLiveSocketController;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.Liquidation;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.Algorithm;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.SoundPoolUtil;
import com.purity.yu.gameplatform.widget.ChipView;
import com.purity.yu.gameplatform.widget.FlashHelper;
import com.purity.yu.gameplatform.widget.GestureListener;
import com.purity.yu.gameplatform.widget.HintDialog;
import com.purity.yu.gameplatform.widget.PercentCircleAntiClockwise;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.Socket;

@ContentView(R.layout.activity_baccarat_live)
//@Xml(layouts = "activity_baccarat_live")
public class BaccaratLiveActivity extends BaseActivity {
    private GestureDetector gestureDetector;
    private Baccarat baccarat;
    private Context mContext;
    private String roomName;
    private String roomId;
    private Socket mSocket;
    private int playerIng = 0, bankerIng, tieIng, playerPairIng = 0, bankerPairIng = 0, bigIng = 0, smallIng = 0;//实时下注积分
    private int playerScore = 0, bankerScore = 0, tieScore = 0, playerPairScore = 0, bankerPairScore = 0, bigScore = 0, smallScore = 0;//当前局每个位置下注积分
    private int perPlayer = 0, perBanker = 0, perTie = 0, perPlayerPair = 0, perBankerPair = 0, perBig = 0, perSmall = 0;//每局每个位置下注总积分
    private int free = 0;//默认庄非免佣1:0.95 1免佣
    private boolean isChip = false;//默认未保存下注记录
    private int isChipSuccess = 0;//默认0下注不成功 1下注成功
    private boolean isSame = false;//默认不续押
    private boolean isAllRemain = false;//默认未全押
    public List<Integer> betLimitList = new ArrayList<>();
    public List<Integer> maxBetList = new ArrayList<>();
    public List<Integer> minBetList = new ArrayList<>();
    public List<Integer> currentBetScore = new ArrayList<>();

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
    private List<Integer> maxScoreListAll = new ArrayList<>();//赢家分数
    private List<Integer> maxScoreList = new ArrayList<>();//赢家分数
    private double currentMoney;
    private int deskId = 0;
    private int noBetCount = 0;
    private int betSecond;
    private int isPeopleNum = -1;
    private int betsRemind = -1;
    private boolean isFirstPlay = true, isFirstBank = true, isFirstTie = true, isFirstPlayPair = true, isFirstBankPair = true, isFirstBig = true, isFirstSmall = true;
    private RelativeLayout root_video;
    private SurfaceView sv;
    private LinearLayout ll_back;
    private TextView tv_table_limits;
    private TextView tv_nickname;
    private TextView tv_money;
    private TextView tv_game_name;
    private TextView tv_code;
    private RelativeLayout rl_dragon_bonus;
    private TextView tv_switch_on;
    private TextView tv_switch_off;
    private TextView tv_no_commission_msg;
    private PercentCircleAntiClockwise pcac;
    private RecyclerView rv_chip;
    private RelativeLayout rl_player_pair;
    private RelativeLayout rl_banker_pair;
    private RelativeLayout rl_player;
    private RelativeLayout rl_tie;
    private RelativeLayout rl_banker;
    private RelativeLayout rl_big;
    private RelativeLayout rl_small;
    private RelativeLayout rl_in_time_layout;
    private RelativeLayout rl_content;
    private RelativeLayout rl_just_in_time;
    private RelativeLayout rl_road;
    private YHZGridView gv_left;
    private YHZGridView gv_right_top;
    private YHZGridView gv_right_middle;
    private YHZGridView gv_right_middle_grid;
    private YHZGridView gv_right_bottom_1;
    private YHZGridView gv_right_bottom_2;
    private YHZGridView gv_right_bottom_1_grid;
    private YHZGridView gv_right_bottom_2_grid;
    private View v_1;
    private View v_2;
    private View v_3;
    private View v_4;
    private View v_5;
    private YHZGridView gv_left_short;
    private YHZGridView gv_right_top_short;
    private YHZGridView gv_right_middle_short;
    private YHZGridView gv_right_middle_short_grid;
    private YHZGridView gv_right_bottom_1_short;
    private YHZGridView gv_right_bottom_1_short_grid;
    private YHZGridView gv_right_bottom_2_short;
    private YHZGridView gv_right_bottom_2_short_grid;
    private View v_1_short;
    private View v_2_short;
    private View v_3_short;
    private View v_4_short;
    private View v_5_short;
    private RelativeLayout rl_statistic;
    private TextView tv_in_game_total_value;
    private TextView tv_in_game_banker_value;
    private TextView tv_in_game_player_value;
    private TextView tv_in_game_tie_value;
    private TextView tv_in_game_banker_pair_value;
    private TextView tv_in_game_player_pair_value;
    private RelativeLayout rl_banker_to_player;
    private ImageView iv_ask_bank_1;
    private ImageView iv_ask_bank_2;
    private ImageView iv_ask_bank_3;
    private RelativeLayout rl_player_to_banker;
    private ImageView iv_ask_play_1;
    private ImageView iv_ask_play_2;
    private ImageView iv_ask_play_3;
    private TextView tv_change_banker;
    private TextView tv_player;
    private TextView tv_small;
    private TextView tv_big;
    private TextView tv_tie;
    private TextView tv_player_pair;
    private TextView tv_banker_pair;
    private TextView tv_full_road;
    private RelativeLayout rl_ex_in;
    private ImageView iv_ex;
    private RelativeLayout rl_video_select;
    private ImageView iv_video_select;
    private ImageView iv_bac_set;
    private TextView tv_player_pair_date;
    private TextView tv_banker_pair_date;
    private TextView tv_play_date;
    private TextView tv_tie_date;
    private TextView tv_banker_date;
    private TextView tv_big_date;
    private TextView tv_small_date;
    private LinearLayout ll_poker;
    private RelativeLayout rl_jet_player_layout;
    private RelativeLayout rl_jet_player;
    private TextView tv_jet_player_value;
    private ImageView iv_jet_player_01;
    private ImageView iv_jet_player_02;
    private ImageView iv_jet_player_03;
    private RelativeLayout rl_jet_banker_layout;
    private RelativeLayout rl_jet_banker;
    private TextView tv_jet_banker_value;
    private ImageView iv_jet_banker_01;
    private ImageView iv_jet_banker_02;
    private ImageView iv_jet_banker_03;
    private RelativeLayout rl_sure;
    private TextView tv_sure;
    private RelativeLayout rl_cancel;
    private RelativeLayout rl_play_light;
    private RelativeLayout rl_bank_light;
    private RelativeLayout rl_player_pair_chip;
    private ChipView cv_player_pair_chip;
    private TextView tv_player_pair_integer;
    private RelativeLayout rl_player_pair_chip1;
    private ChipView cv_player_pair_chip1;
    private TextView tv_player_pair_integer1;
    private RelativeLayout rl_banker_pair_chip;
    private ChipView cv_banker_pair_chip;
    private TextView tv_banker_pair_integer;
    private RelativeLayout rl_banker_pair_chip1;
    private ChipView cv_banker_pair_chip1;
    private TextView tv_banker_pair_integer1;
    private RelativeLayout rl_player_chip;
    private ChipView cv_player_chip;
    private TextView tv_player_integer;
    private RelativeLayout rl_player_chip1;
    private ChipView cv_player_chip1;
    private TextView tv_player_integer1;
    private RelativeLayout rl_player_chip2;
    private ChipView cv_player_chip2;
    private TextView tv_player_integer2;
    private RelativeLayout rl_tie_chip;
    private ChipView cv_tie_chip;
    private TextView tv_tie_integer;
    private RelativeLayout rl_banker_chip;
    private ChipView cv_banker_chip;
    private TextView tv_banker_integer;
    private RelativeLayout rl_banker_chip1;
    private ChipView cv_banker_chip1;
    private TextView tv_banker_integer1;
    private RelativeLayout rl_banker_chip2;
    private ChipView cv_banker_chip2;
    private TextView tv_banker_integer2;
    private RelativeLayout rl_big_chip;
    private ChipView cv_big_chip;
    private TextView tv_big_integer;
    private RelativeLayout rl_small_chip;
    private ChipView cv_small_chip;
    private TextView tv_small_integer;
    private TextView tv_bet_all_score;
    private RelativeLayout rl_video_select1;
    private ImageView iv_video_select1;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = BaccaratLiveActivity.this;
//        ButterKnife.bind(this);
        initFind();
        initView();
    }

    private void initFind() {
        root_video = findViewById(R.id.root_video);
        sv = findViewById(R.id.sv);
        ll_back = findViewById(R.id.ll_back);
        tv_table_limits = findViewById(R.id.tv_table_limits);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_money = findViewById(R.id.tv_money);
        tv_game_name = findViewById(R.id.tv_game_name);
        tv_code = findViewById(R.id.tv_code);
        rl_dragon_bonus = findViewById(R.id.rl_dragon_bonus);
        tv_switch_on = findViewById(R.id.tv_switch_on);
        tv_switch_off = findViewById(R.id.tv_switch_off);
        tv_no_commission_msg = findViewById(R.id.tv_no_commission_msg);
        pcac = findViewById(R.id.pcac);
        rv_chip = findViewById(R.id.rv_chip);

        rl_player_pair = findViewById(R.id.rl_player_pair);
        rl_banker_pair = findViewById(R.id.rl_banker_pair);
        rl_player = findViewById(R.id.rl_player);
        rl_tie = findViewById(R.id.rl_tie);
        rl_banker = findViewById(R.id.rl_banker);
        rl_big = findViewById(R.id.rl_big);
        rl_small = findViewById(R.id.rl_small);
        rl_in_time_layout = findViewById(R.id.rl_in_time_layout);
        rl_content = findViewById(R.id.rl_content);
        rl_just_in_time = findViewById(R.id.rl_just_in_time);
        rl_road = findViewById(R.id.rl_road);

        gv_left = findViewById(R.id.gv_left);
        gv_right_top = findViewById(R.id.gv_right_top);
        gv_right_middle = findViewById(R.id.gv_right_middle);
        gv_right_middle_grid = findViewById(R.id.gv_right_middle_grid);
        gv_right_bottom_1 = findViewById(R.id.gv_right_bottom_1);
        gv_right_bottom_1_grid = findViewById(R.id.gv_right_bottom_1_grid);
        gv_right_bottom_2 = findViewById(R.id.gv_right_bottom_2);
        gv_right_bottom_2_grid = findViewById(R.id.gv_right_bottom_2_grid);
        v_1 = findViewById(R.id.v_1);
        v_2 = findViewById(R.id.v_2);
        v_3 = findViewById(R.id.v_3);
        v_4 = findViewById(R.id.v_4);
        v_5 = findViewById(R.id.v_5);
        gv_left_short = findViewById(R.id.gv_left_short);
        gv_right_top_short = findViewById(R.id.gv_right_top_short);
        gv_right_middle_short = findViewById(R.id.gv_right_middle_short);
        gv_right_middle_short_grid = findViewById(R.id.gv_right_middle_short_grid);
        gv_right_bottom_1_short = findViewById(R.id.gv_right_bottom_1_short);
        gv_right_bottom_1_short_grid = findViewById(R.id.gv_right_bottom_1_short_grid);
        gv_right_bottom_2_short = findViewById(R.id.gv_right_bottom_2_short);
        gv_right_bottom_2_short_grid = findViewById(R.id.gv_right_bottom_2_short_grid);
        v_1_short = findViewById(R.id.v_1_short);
        v_2_short = findViewById(R.id.v_2_short);
        v_3_short = findViewById(R.id.v_3_short);
        v_4_short = findViewById(R.id.v_4_short);
        v_5_short = findViewById(R.id.v_5_short);

        rl_statistic = findViewById(R.id.rl_statistic);
        tv_in_game_total_value = findViewById(R.id.tv_in_game_total_value);
        tv_in_game_banker_value = findViewById(R.id.tv_in_game_banker_value);
        tv_in_game_player_value = findViewById(R.id.tv_in_game_player_value);
        tv_in_game_tie_value = findViewById(R.id.tv_in_game_tie_value);
        tv_in_game_banker_pair_value = findViewById(R.id.tv_in_game_banker_pair_value);
        tv_in_game_player_pair_value = findViewById(R.id.tv_in_game_player_pair_value);

        rl_banker_to_player = findViewById(R.id.rl_banker_to_player);
        iv_ask_bank_1 = findViewById(R.id.iv_banker_to_player_1);
        iv_ask_bank_2 = findViewById(R.id.iv_banker_to_player_2);
        iv_ask_bank_3 = findViewById(R.id.iv_banker_to_player_3);
        rl_player_to_banker = findViewById(R.id.rl_player_to_banker);
        iv_ask_play_1 = findViewById(R.id.iv_player_to_banker_1);
        iv_ask_play_2 = findViewById(R.id.iv_player_to_banker_2);
        iv_ask_play_3 = findViewById(R.id.iv_player_to_banker_3);

        tv_change_banker = findViewById(R.id.tv_change_banker);
        tv_player = findViewById(R.id.tv_player);
        tv_small = findViewById(R.id.tv_small);
        tv_big = findViewById(R.id.tv_big);
        tv_tie = findViewById(R.id.tv_tie);
        tv_player_pair = findViewById(R.id.tv_player_pair);
        tv_banker_pair = findViewById(R.id.tv_banker_pair);

        tv_full_road = findViewById(R.id.tv_full_road);
        rl_ex_in = findViewById(R.id.rl_ex_in);
        iv_ex = findViewById(R.id.iv_ex);
        rl_video_select = findViewById(R.id.rl_video_select);
        iv_video_select = findViewById(R.id.iv_video_select);
        iv_bac_set = findViewById(R.id.iv_bac_set);
        //实时下注积分
        tv_player_pair_date = findViewById(R.id.tv_player_pair_date);
        tv_banker_pair_date = findViewById(R.id.tv_banker_pair_date);
        tv_play_date = findViewById(R.id.tv_play_date);
        tv_tie_date = findViewById(R.id.tv_tie_date);
        tv_banker_date = findViewById(R.id.tv_banker_date);
        tv_big_date = findViewById(R.id.tv_big_date);
        tv_small_date = findViewById(R.id.tv_small_date);
        //发牌
        ll_poker = findViewById(R.id.ll_poker);
        rl_jet_player_layout = findViewById(R.id.rl_jet_player_layout);
        rl_jet_player = findViewById(R.id.rl_jet_player);
        tv_jet_player_value = findViewById(R.id.tv_jet_player_value);
        iv_jet_player_01 = findViewById(R.id.iv_jet_player_01);
        iv_jet_player_02 = findViewById(R.id.iv_jet_player_02);
        iv_jet_player_03 = findViewById(R.id.iv_jet_player_03);

        rl_jet_banker_layout = findViewById(R.id.rl_jet_banker_layout);
        rl_jet_banker = findViewById(R.id.rl_jet_banker);
        tv_jet_banker_value = findViewById(R.id.tv_jet_banker_value);
        iv_jet_banker_01 = findViewById(R.id.iv_jet_banker_01);
        iv_jet_banker_02 = findViewById(R.id.iv_jet_banker_02);
        iv_jet_banker_03 = findViewById(R.id.iv_jet_banker_03);

        rl_sure = findViewById(R.id.rl_sure);
        tv_sure = findViewById(R.id.tv_sure);
        //取消下注
        rl_cancel = findViewById(R.id.rl_cancel);
        rl_play_light = findViewById(R.id.rl_play_light);
        rl_bank_light = findViewById(R.id.rl_bank_light);

        rl_player_pair_chip = findViewById(R.id.rl_player_pair_chip);
        cv_player_pair_chip = findViewById(R.id.cv_player_pair_chip);
        tv_player_pair_integer = findViewById(R.id.tv_player_pair_integer);
        rl_player_pair_chip1 = findViewById(R.id.rl_player_pair_chip1);
        cv_player_pair_chip1 = findViewById(R.id.cv_player_pair_chip1);
        tv_player_pair_integer1 = findViewById(R.id.tv_player_pair_integer1);

        rl_banker_pair_chip = findViewById(R.id.rl_banker_pair_chip);
        cv_banker_pair_chip = findViewById(R.id.cv_banker_pair_chip);
        tv_banker_pair_integer = findViewById(R.id.tv_banker_pair_integer);
        rl_banker_pair_chip1 = findViewById(R.id.rl_banker_pair_chip1);
        cv_banker_pair_chip1 = findViewById(R.id.cv_banker_pair_chip1);
        tv_banker_pair_integer1 = findViewById(R.id.tv_banker_pair_integer1);

        rl_player_chip = findViewById(R.id.rl_player_chip);
        cv_player_chip = findViewById(R.id.cv_player_chip);
        tv_player_integer = findViewById(R.id.tv_player_integer);
        rl_player_chip1 = findViewById(R.id.rl_player_chip1);
        cv_player_chip1 = findViewById(R.id.cv_player_chip1);
        tv_player_integer1 = findViewById(R.id.tv_player_integer1);
        rl_player_chip2 = findViewById(R.id.rl_player_chip2);
        cv_player_chip2 = findViewById(R.id.cv_player_chip2);
        tv_player_integer2 = findViewById(R.id.tv_player_integer2);

        rl_tie_chip = findViewById(R.id.rl_tie_chip);
        cv_tie_chip = findViewById(R.id.cv_tie_chip);
        tv_tie_integer = findViewById(R.id.tv_tie_integer);

        rl_banker_chip = findViewById(R.id.rl_banker_chip);
        cv_banker_chip = findViewById(R.id.cv_banker_chip);
        tv_banker_integer = findViewById(R.id.tv_banker_integer);
        rl_banker_chip1 = findViewById(R.id.rl_banker_chip1);
        cv_banker_chip1 = findViewById(R.id.cv_banker_chip1);
        tv_banker_integer1 = findViewById(R.id.tv_banker_integer1);
        rl_banker_chip2 = findViewById(R.id.rl_banker_chip2);
        cv_banker_chip2 = findViewById(R.id.cv_banker_chip2);
        tv_banker_integer2 = findViewById(R.id.tv_banker_integer2);

        rl_big_chip = findViewById(R.id.rl_big_chip);
        cv_big_chip = findViewById(R.id.cv_big_chip);
        tv_big_integer = findViewById(R.id.tv_big_integer);
        rl_small_chip = findViewById(R.id.rl_small_chip);
        cv_small_chip = findViewById(R.id.cv_small_chip);
        tv_small_integer = findViewById(R.id.tv_small_integer);
        tv_bet_all_score = findViewById(R.id.tv_bet_all_score);

        rl_video_select1 = findViewById(R.id.rl_video_select1);
        iv_video_select1 = findViewById(R.id.iv_video_select1);
    }

    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        tv_nickname.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, 0);//进入房间未下注状态
        roomName = getIntent().getStringExtra("roomName");
        roomId = getIntent().getStringExtra("roomId");
        betSecond = getIntent().getIntExtra("betSecond", 25);
        tv_game_name.setText(mContext.getResources().getString(R.string.ba_game) + roomName);
        EventBus.getDefault().register(this);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.WHERE_SETED, 0);
        baccarat = (Baccarat) getIntent().getSerializableExtra("baccarat");
        gestureDetector = new GestureDetector(mContext, new MyGestureListener(this));

        BaccaratLiveSocketController.getInstance().init(this);
        BaccaratLiveSocketController.getInstance().copyWidget(boardMessageList, pcac, ll_poker, iv_jet_banker_01, iv_jet_banker_02, iv_jet_banker_03,
                tv_jet_banker_value, iv_jet_player_01, iv_jet_player_02, iv_jet_player_03, tv_jet_player_value, _calPlayer, _calBanker, roomId,
                rl_sure, rl_player_pair, rl_banker_pair, rl_player, rl_tie, rl_banker, betSecond);
        BaccaratLiveSocketController.getInstance().connectSocket();

        BaccaratLiveController.getInstance().init(this);
        BaccaratLiveController.getInstance().copyWidget(rl_dragon_bonus, rv_chip);

        BaccaratLiveAnimController.getInstance().init(mContext);
        BaccaratLiveAnimController.getInstance().copyWidget(tv_table_limits, rl_in_time_layout, iv_ex, rl_video_select, iv_video_select, sv, root_video, ll_back, iv_bac_set,
                baccarat, rl_video_select1, iv_video_select1);

        initLiveInTime();
        currentBetScore.clear();
        currentBetScore.add(0);//闲
        currentBetScore.add(0);//庄
        currentBetScore.add(0);//和
        currentBetScore.add(0);//闲对
        currentBetScore.add(0);//庄对
        currentBetScore.add(0);//小
        currentBetScore.add(0);//大
        initEn();


    }

    private void initEn() {
        int languageType = CommSharedUtil.getInstance(this).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_EN) {
            tv_small.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.unit12));
            tv_big.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.unit12));
            tv_player_pair.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.unit12));
            tv_banker_pair.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.unit12));
            tv_player.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.unit20));
            tv_tie.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.unit20));
            tv_change_banker.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.unit20));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev); // 让GestureDetector响应触碰事件
        super.dispatchTouchEvent(ev); // 让Activity响应触碰事件
        return false;
    }

    /**
     * 1(onResume)--2(onPause) 2(onPause)--1(onResume)
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreUtil.getInstance(this).getInt(Constant.BAC_VIDEO_PLAYING) == 1) {
            BaccaratLiveAnimController.getInstance().initBacDataVideo(baccarat);
        }
    }


    //-----------------------------------------------原 BaccaratLiveInTime start----------------------------------------------------------
    private int isExpand;
    private DisplayMetrics displayMetrics;
    private RelativeLayout.LayoutParams rlRoadLayoutParams;
    private RelativeLayout.LayoutParams rlStatisticLayoutParams;
    private RelativeLayout.LayoutParams rlContentLayoutParams;

    private void initLiveInTime() {
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 0);
        int _isExpand = SharedPreUtil.getInstance(mContext).getInt(Constant.IS_EXPAND);
        displayMetrics = getResources().getDisplayMetrics();


        if (_isExpand == 0) {
            initGvShort();
            //测试
//            boardMessageList = BaccaratUtil.getInstance().initBoardMessage(53, boardMessageList);
//            initMessages();
            Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                    gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 0, Color.RED, Color.BLUE, Color.GREEN, 0, false, 2);//4列 手动进房间
        }
        initEvent();
    }

    private void initEvent() {
        rl_banker_to_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask(0, false, true, 4, true);
            }
        });
        rl_player_to_banker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ask(1, false, true, 4, true);
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
                isFirstPlayPair = true;
                isFirstBankPair = true;
                isFirstSmall = true;
                isFirstBig = true;
                bankerScore = 0;
                playerScore = 0;
                tieScore = 0;
                bankerPairScore = 0;
                playerPairScore = 0;
                bigScore = 0;
                smallScore = 0;
                isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
                if (isSame) {
                    cleanChip();
                } else
                    currentChip();
            }
        });
        rl_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogUtil.i("下注4_confirm_before_command=chipScore.get(0)庄=" + chipScore.get(0) + " chipScore.get(1)闲=" + chipScore.get(1) + " chipScore.get(2)和=" + chipScore.get(2) +
                        " chipScore.get(3)庄对=" + chipScore.get(3) + " chipScore.get(4)闲对=" + chipScore.get(4) + "chipScore.get(5)大=" + chipScore.get(5) + "chipScore.get(6)小=" + chipScore.get(6) +
                        " free=" + free + " chipScore=" + chipScore.size());
                if (isChip) {
                    //todo 当前积分小于前一句押注积分时，会出问题
                    List<Integer> allPer = new ArrayList<>();
                    allPer.clear();
                    allPer.add(perBanker);
                    allPer.add(perPlayer);
                    allPer.add(perTie);
                    allPer.add(perBankerPair);
                    allPer.add(perPlayerPair);
                    allPer.add(perBig);
                    allPer.add(perSmall);
                    Collections.sort(allPer);
                    LogUtil.i("allPer=" + allPer.toString());//升序

                    if (cv_player_pair_chip.getChipNumSize() > 0) {
                        rl_player_pair_chip.setVisibility(View.VISIBLE);

                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perBig + perSmall;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perPlayerPair == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                    sameBetPlayPair(_currentMoney);
                                } else {
                                    chipScore.put(4, perPlayerPair);
                                }
                            } else if (_beforeFour == _currentMoney) {//刚刚好 最后一个不显示
                                if (perPlayerPair == allPer.get(4)) {
                                    cv_player_pair_chip.addChip(0);
                                    chipScore.put(4, 0);
                                    rl_player_pair_chip.setVisibility(View.GONE);
                                } else {
                                    chipScore.put(4, perPlayerPair);
                                }
                            } else if (_beforeFour > _currentMoney) {//前4个都大于当前余额 allPer.get(3)开始减少
                                int _beforeThird = allPer.get(0) + allPer.get(1) + allPer.get(2);
                                if (_beforeThird < _currentMoney) {
                                    if (perPlayerPair == allPer.get(3)) {
                                        _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2));
                                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                        sameBetPlayPair(_currentMoney);
                                    } else {
                                        chipScore.put(4, perPlayerPair);
                                    }
                                } else if (_beforeThird == _currentMoney) {
                                    if (perPlayerPair == allPer.get(3)) {
                                        cv_player_pair_chip.addChip(0);
                                        chipScore.put(4, 0);
                                        rl_player_pair_chip.setVisibility(View.GONE);
                                    } else {
                                        chipScore.put(4, perPlayerPair);
                                    }
                                } else if (_beforeThird > _currentMoney) {
                                    int _beforeTwo = allPer.get(0) + allPer.get(1);
                                    if (_beforeTwo < _currentMoney) {
                                        if (perPlayerPair == allPer.get(2)) {
                                            _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1));
                                            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                            sameBetPlayPair(_currentMoney);
                                        } else {
                                            chipScore.put(4, perPlayerPair);
                                        }
                                    } else if (_beforeTwo == _currentMoney) {
                                        if (perPlayerPair == allPer.get(2)) {
                                            cv_player_pair_chip.addChip(0);
                                            chipScore.put(4, 0);
                                            rl_player_pair_chip.setVisibility(View.GONE);
                                        } else {
                                            chipScore.put(4, perPlayerPair);
                                        }
                                    } else if (_beforeTwo > _currentMoney) {
                                        int _beforeOne = allPer.get(0);
                                        if (_beforeOne < _currentMoney) {
                                            if (perPlayerPair == allPer.get(1)) {
                                                _currentMoney = _currentMoney - allPer.get(0);
                                                ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                                sameBetPlayPair(_currentMoney);
                                            } else if (perPlayerPair == allPer.get(0)) {
                                                chipScore.put(4, perPlayerPair);
                                            }
                                        } else if (_beforeOne == _currentMoney) {
                                            if (perPlayerPair == allPer.get(1)) {
                                                cv_player_pair_chip.addChip(0);
                                                chipScore.put(4, 0);
                                                rl_player_pair_chip.setVisibility(View.GONE);
                                            } else if (perPlayerPair == allPer.get(0)) {
                                                chipScore.put(4, perPlayerPair);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            chipScore.put(4, perPlayerPair);
                        }
                    }
                    if (cv_banker_pair_chip.getChipNumSize() > 0) {
                        rl_banker_pair_chip.setVisibility(View.VISIBLE);

                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perBig + perSmall;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perBankerPair == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                    sameBetBankPair(_currentMoney);
                                } else {
                                    chipScore.put(3, perBankerPair);
                                }
                            } else if (_beforeFour == _currentMoney) {//刚刚好 最后一个不显示
                                if (perBankerPair == allPer.get(4)) {
                                    cv_banker_pair_chip.addChip(0);
                                    chipScore.put(3, 0);
                                    rl_banker_pair_chip.setVisibility(View.GONE);
                                } else {
                                    chipScore.put(3, perBankerPair);
                                }
                            } else if (_beforeFour > _currentMoney) {//前4个都大于当前余额 allPer.get(3)开始减少
                                int _beforeThird = allPer.get(0) + allPer.get(1) + allPer.get(2);
                                if (_beforeThird < _currentMoney) {
                                    if (perBankerPair == allPer.get(3)) {
                                        _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2));
                                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                        sameBetBankPair(_currentMoney);
                                    } else {
                                        chipScore.put(3, perBankerPair);
                                    }
                                } else if (_beforeThird == _currentMoney) {
                                    if (perBankerPair == allPer.get(3)) {
                                        cv_banker_pair_chip.addChip(0);
                                        chipScore.put(3, 0);
                                        rl_banker_pair_chip.setVisibility(View.GONE);
                                    } else {
                                        chipScore.put(3, perBankerPair);
                                    }
                                } else if (_beforeThird > _currentMoney) {
                                    int _beforeTwo = allPer.get(0) + allPer.get(1);
                                    if (_beforeTwo < _currentMoney) {
                                        if (perBankerPair == allPer.get(2)) {
                                            _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1));
                                            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                            sameBetBankPair(_currentMoney);
                                        } else {
                                            chipScore.put(3, perBankerPair);
                                        }
                                    } else if (_beforeTwo == _currentMoney) {
                                        if (perBankerPair == allPer.get(2)) {
                                            cv_banker_pair_chip.addChip(0);
                                            chipScore.put(3, 0);
                                            rl_banker_pair_chip.setVisibility(View.GONE);
                                        } else {
                                            chipScore.put(3, perBankerPair);
                                        }
                                    } else if (_beforeTwo > _currentMoney) {
                                        int _beforeOne = allPer.get(0);
                                        if (_beforeOne < _currentMoney) {
                                            if (perBankerPair == allPer.get(1)) {
                                                _currentMoney = _currentMoney - allPer.get(0);
                                                ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                                sameBetBankPair(_currentMoney);
                                            } else if (perBankerPair == allPer.get(0)) {
                                                chipScore.put(3, perBankerPair);
                                            }
                                        } else if (_beforeOne == _currentMoney) {
                                            if (perBankerPair == allPer.get(1)) {
                                                cv_banker_pair_chip.addChip(0);
                                                chipScore.put(3, 0);
                                                rl_banker_pair_chip.setVisibility(View.GONE);
                                            } else if (perBankerPair == allPer.get(0)) {
                                                chipScore.put(3, perBankerPair);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            chipScore.put(3, perBankerPair);
                        }
                    }
                    if (cv_player_chip.getChipNumSize() > 0) {
                        rl_player_chip.setVisibility(View.VISIBLE);

                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perBig + perSmall;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perPlayer == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                    sameBetPlayer(_currentMoney);
                                } else {
                                    chipScore.put(1, perPlayer);
                                }
                            } else if (_beforeFour == _currentMoney) {//刚刚好 最后一个不显示
                                if (perPlayer == allPer.get(4)) {
                                    cv_player_chip.addChip(0);
                                    chipScore.put(1, 0);
                                    rl_player_chip.setVisibility(View.GONE);
                                } else {
                                    chipScore.put(1, perPlayer);
                                }
                            } else if (_beforeFour > _currentMoney) {//前4个都大于当前余额 allPer.get(3)开始减少
                                int _beforeThird = allPer.get(0) + allPer.get(1) + allPer.get(2);
                                if (_beforeThird < _currentMoney) {
                                    if (perPlayer == allPer.get(3)) {
                                        _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2));
                                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                        sameBetPlayer(_currentMoney);
                                    } else {
                                        chipScore.put(1, perPlayer);
                                    }
                                } else if (_beforeThird == _currentMoney) {
                                    if (perPlayer == allPer.get(3)) {
                                        cv_player_chip.addChip(0);
                                        chipScore.put(1, 0);
                                        rl_player_chip.setVisibility(View.GONE);
                                    } else {
                                        chipScore.put(1, perPlayer);
                                    }
                                } else if (_beforeThird > _currentMoney) {
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
                                }
                            }
                        } else {
                            chipScore.put(1, perPlayer);
                        }

                    }
                    if (cv_tie_chip.getChipNumSize() > 0) {
                        rl_tie_chip.setVisibility(View.VISIBLE);

                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perBig + perSmall;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perTie == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                    sameBetTie(_currentMoney);
                                } else {
                                    chipScore.put(2, perTie);
                                }
                            } else if (_beforeFour == _currentMoney) {//刚刚好 最后一个不显示
                                if (perTie == allPer.get(4)) {
                                    cv_tie_chip.addChip(0);
                                    chipScore.put(2, 0);
                                    rl_tie_chip.setVisibility(View.GONE);
                                } else {
                                    chipScore.put(2, perTie);
                                }
                            } else if (_beforeFour > _currentMoney) {//前4个都大于当前余额 allPer.get(3)开始减少
                                int _beforeThird = allPer.get(0) + allPer.get(1) + allPer.get(2);
                                if (_beforeThird < _currentMoney) {
                                    if (perTie == allPer.get(3)) {
                                        _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2));
                                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                        sameBetTie(_currentMoney);
                                    } else {
                                        chipScore.put(2, perTie);
                                    }
                                } else if (_beforeThird == _currentMoney) {
                                    if (perTie == allPer.get(3)) {
                                        cv_tie_chip.addChip(0);
                                        chipScore.put(2, 0);
                                        rl_tie_chip.setVisibility(View.GONE);
                                    } else {
                                        chipScore.put(2, perTie);
                                    }
                                } else if (_beforeThird > _currentMoney) {
                                    int _beforeTwo = allPer.get(0) + allPer.get(1);
                                    if (_beforeTwo < _currentMoney) {
                                        if (perTie == allPer.get(2)) {
                                            _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1));
                                            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                            sameBetTie(_currentMoney);
                                        } else {
                                            chipScore.put(2, perTie);
                                        }
                                    } else if (_beforeTwo == _currentMoney) {
                                        if (perTie == allPer.get(2)) {
                                            cv_tie_chip.addChip(0);
                                            chipScore.put(2, 0);
                                            rl_tie_chip.setVisibility(View.GONE);
                                        } else {
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
                                }
                            }
                        } else {
                            chipScore.put(2, perTie);
                        }
                    }
                    if (cv_big_chip.getChipNumSize() > 0) {
                        rl_big_chip.setVisibility(View.VISIBLE);

                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perBig + perSmall;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perBig == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                    sameBetBig(_currentMoney);
                                } else {
                                    chipScore.put(6, perBig);
                                }
                            } else if (_beforeFour == _currentMoney) {//刚刚好 最后一个不显示
                                if (perBig == allPer.get(4)) {
                                    cv_big_chip.addChip(0);
                                    chipScore.put(6, 0);
                                    rl_big_chip.setVisibility(View.GONE);
                                } else {
                                    chipScore.put(6, perBig);
                                }
                            } else if (_beforeFour > _currentMoney) {//前4个都大于当前余额 allPer.get(3)开始减少
                                int _beforeThird = allPer.get(0) + allPer.get(1) + allPer.get(2);
                                if (_beforeThird < _currentMoney) {
                                    if (perBig == allPer.get(3)) {
                                        _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2));
                                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                        sameBetBig(_currentMoney);
                                    } else {
                                        chipScore.put(6, perBig);
                                    }
                                } else if (_beforeThird == _currentMoney) {
                                    if (perBig == allPer.get(3)) {
                                        cv_big_chip.addChip(0);
                                        chipScore.put(6, 0);
                                        rl_big_chip.setVisibility(View.GONE);
                                    } else {
                                        chipScore.put(6, perBig);
                                    }
                                } else if (_beforeThird > _currentMoney) {
                                    int _beforeTwo = allPer.get(0) + allPer.get(1);
                                    if (_beforeTwo < _currentMoney) {
                                        if (perBig == allPer.get(2)) {
                                            _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1));
                                            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                            sameBetBig(_currentMoney);
                                        } else {
                                            chipScore.put(6, perBig);
                                        }
                                    } else if (_beforeTwo == _currentMoney) {
                                        if (perBig == allPer.get(2)) {
                                            cv_big_chip.addChip(0);
                                            chipScore.put(6, 0);
                                            rl_big_chip.setVisibility(View.GONE);
                                        } else {
                                            chipScore.put(6, perBig);
                                        }
                                    } else if (_beforeTwo > _currentMoney) {
                                        int _beforeOne = allPer.get(0);
                                        if (_beforeOne < _currentMoney) {
                                            if (perBig == allPer.get(1)) {
                                                _currentMoney = _currentMoney - allPer.get(0);
                                                ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                                sameBetBig(_currentMoney);
                                            } else if (perBig == allPer.get(0)) {
                                                chipScore.put(6, perBig);
                                            }
                                        } else if (_beforeOne == _currentMoney) {
                                            if (perBig == allPer.get(1)) {
                                                cv_big_chip.addChip(0);
                                                chipScore.put(6, 0);
                                                rl_big_chip.setVisibility(View.GONE);
                                            } else if (perBig == allPer.get(0)) {
                                                chipScore.put(6, perBig);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            chipScore.put(6, perBig);
                        }
                    }
                    if (cv_small_chip.getChipNumSize() > 0) {
                        rl_small_chip.setVisibility(View.VISIBLE);
                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perBig + perSmall;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perSmall == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                    sameBetSmall(_currentMoney);
                                } else {
                                    chipScore.put(5, perSmall);
                                }
                            } else if (_beforeFour == _currentMoney) {//刚刚好 最后一个不显示
                                if (perSmall == allPer.get(4)) {
                                    cv_small_chip.addChip(0);
                                    chipScore.put(5, 0);
                                    rl_small_chip.setVisibility(View.GONE);
                                } else {
                                    chipScore.put(5, perSmall);
                                }
                            } else if (_beforeFour > _currentMoney) {//前4个都大于当前余额 allPer.get(3)开始减少
                                int _beforeThird = allPer.get(0) + allPer.get(1) + allPer.get(2);
                                if (_beforeThird < _currentMoney) {
                                    if (perSmall == allPer.get(3)) {
                                        _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2));
                                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                        sameBetSmall(_currentMoney);
                                    } else {
                                        chipScore.put(5, perSmall);
                                    }
                                } else if (_beforeThird == _currentMoney) {
                                    if (perSmall == allPer.get(3)) {
                                        cv_small_chip.addChip(0);
                                        chipScore.put(5, 0);
                                        rl_small_chip.setVisibility(View.GONE);
                                    } else {
                                        chipScore.put(5, perSmall);
                                    }
                                } else if (_beforeThird > _currentMoney) {
                                    int _beforeTwo = allPer.get(0) + allPer.get(1);
                                    if (_beforeTwo < _currentMoney) {
                                        if (perSmall == allPer.get(2)) {
                                            _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1));
                                            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                            sameBetSmall(_currentMoney);
                                        } else {
                                            chipScore.put(5, perSmall);
                                        }
                                    } else if (_beforeTwo == _currentMoney) {
                                        if (perSmall == allPer.get(2)) {
                                            cv_small_chip.addChip(0);
                                            chipScore.put(5, 0);
                                            rl_small_chip.setVisibility(View.GONE);
                                        } else {
                                            chipScore.put(5, perSmall);
                                        }
                                    } else if (_beforeTwo > _currentMoney) {
                                        int _beforeOne = allPer.get(0);
                                        if (_beforeOne < _currentMoney) {
                                            if (perSmall == allPer.get(1)) {
                                                _currentMoney = _currentMoney - allPer.get(0);
                                                ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                                sameBetSmall(_currentMoney);
                                            } else if (perSmall == allPer.get(0)) {
                                                chipScore.put(5, perSmall);
                                            }
                                        } else if (_beforeOne == _currentMoney) {
                                            if (perSmall == allPer.get(1)) {
                                                cv_small_chip.addChip(0);
                                                chipScore.put(5, 0);
                                                rl_small_chip.setVisibility(View.GONE);
                                            } else if (perSmall == allPer.get(0)) {
                                                chipScore.put(5, perSmall);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            chipScore.put(5, perSmall);
                        }
                    }
                    if (cv_banker_chip.getChipNumSize() > 0) {
                        rl_banker_chip.setVisibility(View.VISIBLE);

                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perBig + perSmall;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perBanker == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                    sameBetBanker(_currentMoney);
                                } else {
                                    chipScore.put(0, perBanker);
                                }
                            } else if (_beforeFour == _currentMoney) {//刚刚好 最后一个不显示
                                if (perBanker == allPer.get(4)) {
                                    cv_banker_chip.addChip(0);
                                    chipScore.put(0, 0);
                                    rl_banker_chip.setVisibility(View.GONE);
                                } else {
                                    chipScore.put(0, perBanker);
                                }
                            } else if (_beforeFour > _currentMoney) {//前4个都大于当前余额 allPer.get(3)开始减少
                                int _beforeThird = allPer.get(0) + allPer.get(1) + allPer.get(2);
                                if (_beforeThird < _currentMoney) {
                                    if (perBanker == allPer.get(3)) {
                                        _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2));
                                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                        sameBetBanker(_currentMoney);
                                    } else {
                                        chipScore.put(0, perBanker);
                                    }
                                } else if (_beforeThird == _currentMoney) {
                                    if (perBanker == allPer.get(3)) {
                                        cv_banker_chip.addChip(0);
                                        chipScore.put(0, 0);
                                        rl_banker_chip.setVisibility(View.GONE);
                                    } else {
                                        chipScore.put(0, perBanker);
                                    }
                                } else if (_beforeThird > _currentMoney) {
                                    int _beforeTwo = allPer.get(0) + allPer.get(1);
                                    if (_beforeTwo < _currentMoney) {
                                        if (perBanker == allPer.get(2)) {
                                            _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1));
                                            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                                            sameBetBanker(_currentMoney);
                                        } else {
                                            chipScore.put(0, perBanker);
                                        }
                                    } else if (_beforeTwo == _currentMoney) {
                                        if (perBanker == allPer.get(2)) {
                                            cv_banker_chip.addChip(0);
                                            chipScore.put(0, 0);
                                            rl_banker_chip.setVisibility(View.GONE);
                                        } else {
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
                } else {
                    mSocket = BaccaratLiveSocketController.getInstance().getSocket();
                    if (mSocket != null && mSocket.connected()) {
                        for (int i = 0; i < 7; i++) {
                            if (chipScore.get(i) == null) {
                                chipScore.put(i, 0);
                            }
                        }
                        LogUtil.i("socket_command=当前下注" + chipScore.toString());
                        //(0闲 1庄 2和 3闲对 4庄对 5大 6小) chipScore=庄 闲 和 庄对 闲对 大 小
                        mSocket.emit("dobet", "{'bets':[" + chipScore.get(1) + "," + chipScore.get(0) + "," + chipScore.get(2) +
                                "," + chipScore.get(4) + "," + chipScore.get(3) + "," + chipScore.get(5) + "," + chipScore.get(6) + "],'freeCommission':'" + free + "'}");
                    }
                }
            }
        });

        isExpand = SharedPreUtil.getInstance(mContext).getInt(Constant.IS_EXPAND);
        //-----------------------------------------------------------------点击对应下注区域-start--------------------------------------------
        rl_player_pair.setOnClickListener(new View.OnClickListener() {//闲对
            @Override
            public void onClick(View v) {
                int selectChipNumber = initChipLayout();//不点重复，直接下注，清空以前所有位置记录，从新开始;
                if (isFirstPlayPair && minBetList.size() > 0 && selectChipNumber < minBetList.get(2) && perPlayerPair == 0) {//0 庄闲 1 和 2 对子 3大小
                    selectChipNumber = minBetList.get(2);
                    isFirstPlayPair = false;
                }
                playerPairScore += selectChipNumber;//每局压分，下注成功就清空
                perPlayerPair += selectChipNumber;//续押，记录上局值
                int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perSmall + perBig;
                if (insufficientBalance(2)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (total > limitMoney) {
                    if (/*isAllRemain &&*/ limitMoney < selectChipNumber) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
//                        return;
                    }
                    _currentMoney = _currentMoney - (total - selectChipNumber);
                    if (_currentMoney < selectChipNumber /*&& !isAllRemain*/) {
                        LogUtil.i("1total=" + total + " _currentMoney=" + _currentMoney);

                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        LogUtil.i("2total=" + total + " _currentMoney=" + BaccaratUtil.addChip(Math.abs(_currentMoney)) + " chipScore.get(4)=" + chipScore.get(4) + " cv_player_pair_chip=" + cv_player_pair_chip.getChipNumSize());
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_player_pair_chip.addChip(integer);
                        }
                        int currentValue = perPlayerPair - selectChipNumber + Math.abs(_currentMoney);
                        tv_player_pair_integer.setText(String.valueOf(currentValue));
                        chipScore.put(4, (playerPairScore - selectChipNumber + _currentMoney));
                        LogUtil.i("3total=" + total + " _currentMoney=" + _currentMoney + " chipScore.get(4)=" + chipScore.get(4) + " cv_player_pair_chip=" + cv_player_pair_chip.getChipNumSize());
                        rl_player_pair_chip.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_pair_chip.getLayoutParams();
                        if (cv_player_pair_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_player_pair_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_player_pair_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_player_pair_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_player_pair_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_player_pair_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_player_pair_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_player_pair_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_pair_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_player_pair_integer.setLayoutParams(tvParams);
                    }
                } else {
//                    if (maxBetList.size() > 0 && (playerPairScore > maxBetList.get(2) || perPlayerPair > maxBetList.get(2))) {
//                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
//                        playerPairScore = 0;
//                        perPlayerPair = perPlayerPair - selectChipNumber;
//                        return;
//                    }
                    if (!isSame) {
                        chipScore.put(4, playerPairScore);//每次下注的分数
                    } else {
                        chipScore.put(4, perPlayerPair);//续押分数
                    }
                    if (cv_player_pair_chip.getChipNumSize() < 15) {//筹码堆叠1
                        rl_player_pair_chip.setVisibility(View.VISIBLE);
                        cv_player_pair_chip.addChip(selectChipNumber);//传0 清空数据，其他就堆叠筹码
                        tv_player_pair_integer.setText(String.valueOf(perPlayerPair));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_pair_chip.getLayoutParams();
                        if (cv_player_pair_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_player_pair_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_player_pair_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_player_pair_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_player_pair_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_player_pair_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_player_pair_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_player_pair_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_pair_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_player_pair_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie + perSmall + perBig;
                tv_bet_all_score.setText(String.valueOf(allScore));
            }
        });

        rl_banker_pair.setOnClickListener(new View.OnClickListener() {//庄对
            @Override
            public void onClick(View v) {
                int selectChipNumber = initChipLayout();
                if (isFirstBankPair && minBetList.size() > 0 && selectChipNumber < minBetList.get(2) && perBankerPair == 0) {//0 庄闲 1 和 2 对子 3大小
                    selectChipNumber = minBetList.get(2);
                    isFirstBankPair = false;
                }
                bankerPairScore += selectChipNumber;
                perBankerPair += selectChipNumber;
                int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perSmall + perBig;
                if (insufficientBalance(2)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (total > limitMoney) {
                    if (/*isAllRemain &&*/ limitMoney < selectChipNumber) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
//                        return;
                    }
                    _currentMoney = _currentMoney - (total - selectChipNumber);
                    if (_currentMoney < selectChipNumber /*&& !isAllRemain*/) {
                        LogUtil.i("1total=" + total + " _currentMoney=" + _currentMoney);
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        LogUtil.i("2total=" + total + " _currentMoney=" + BaccaratUtil.addChip(Math.abs(_currentMoney)) + " chipScore.get(3)=" + chipScore.get(3) + " cv_banker_pair_chip=" + cv_banker_pair_chip.getChipNumSize());
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_banker_pair_chip.addChip(integer);
                        }
                        int currentValue = perBankerPair - selectChipNumber + Math.abs(_currentMoney);
                        tv_banker_pair_integer.setText(String.valueOf(currentValue));
                        chipScore.put(3, (bankerPairScore - selectChipNumber + _currentMoney));
                        LogUtil.i("3total=" + total + " _currentMoney=" + _currentMoney + " chipScore.get(3)=" + chipScore.get(3) + " cv_banker_pair_chip=" + cv_banker_pair_chip.getChipNumSize());
                        rl_banker_pair_chip.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_pair_chip.getLayoutParams();
                        if (cv_banker_pair_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_banker_pair_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_pair_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_banker_pair_integer.setLayoutParams(tvParams);
                    }
                } else {
//                    if (maxBetList.size() > 0 && (bankerPairScore > maxBetList.get(2) || perBankerPair > maxBetList.get(2))) {
//                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
//                        bankerPairScore = 0;
//                        perBankerPair = perBankerPair - selectChipNumber;
//                        return;
//                    }
                    if (!isSame) {
                        chipScore.put(3, bankerPairScore);
                    } else {
                        chipScore.put(3, perBankerPair);//续押分数
                    }
                    if (cv_banker_pair_chip.getChipNumSize() < 15) {
                        rl_banker_pair_chip.setVisibility(View.VISIBLE);
                        cv_banker_pair_chip.addChip(selectChipNumber);
                        tv_banker_pair_integer.setText(String.valueOf(perBankerPair));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_pair_chip.getLayoutParams();
                        if (cv_banker_pair_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_banker_pair_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_pair_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_banker_pair_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie + perSmall + perBig;
                tv_bet_all_score.setText(String.valueOf(allScore));
            }
        });
        rl_player.setOnClickListener(new View.OnClickListener() {//闲
            @Override
            public void onClick(View v) {
                int selectChipNumber = initChipLayout();
                if (isFirstPlay && minBetList.size() > 0 && selectChipNumber < minBetList.get(0) && perPlayer == 0) {//0 庄闲 1 和 2 对子 3大小
                    selectChipNumber = minBetList.get(0);
                    isFirstPlay = false;
                }
                playerScore += selectChipNumber;
                perPlayer += selectChipNumber;
                int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perSmall + perBig;
                if (insufficientBalance(0)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                LogUtil.i("total=" + total + " isSame=" + isSame + " _currentMoney=" + _currentMoney + " isAllRemain=" + isAllRemain);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (total > limitMoney) {
                    if (/*isAllRemain &&*/ limitMoney < selectChipNumber) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
//                        return;
                    }
                    _currentMoney = _currentMoney - (total - selectChipNumber);
                    if (_currentMoney < selectChipNumber /*&& !isAllRemain*/) {
                        LogUtil.i("1total=" + total + " _currentMoney=" + _currentMoney);
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        LogUtil.i("2total=" + total + " _currentMoney=" + BaccaratUtil.addChip(Math.abs(_currentMoney)) + " chipScore.get(1)=" + chipScore.get(1) + " cv_player_chip=" + cv_player_chip.getChipNumSize() + " perPlayer=" + perPlayer);
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_player_chip.addChip(integer);
                        }
                        rl_player_chip.setVisibility(View.VISIBLE);
                        int currentValue = perPlayer - selectChipNumber + Math.abs(_currentMoney);
                        tv_player_integer.setText(String.valueOf(currentValue));
                        chipScore.put(1, (playerScore - selectChipNumber + _currentMoney));
                        LogUtil.i("3total=" + total + " _currentMoney=" + _currentMoney + " chipScore.get(1)=" + chipScore.get(1) + " cv_player_chip=" + cv_player_chip.getChipNumSize());
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
                } else {
//                    if (maxBetList.size() > 0 && (playerScore > maxBetList.get(0) || perPlayer > maxBetList.get(0))) {
//                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
//                        playerScore = 0;
//                        perPlayer = perPlayer - selectChipNumber;
//                        return;
//                    }
                    if (!isSame) {
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
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie + perSmall + perBig;
                tv_bet_all_score.setText(String.valueOf(allScore));
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
                int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perSmall + perBig;
                if (insufficientBalance(1)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (total > limitMoney) {
                    if (/*isAllRemain &&*/ limitMoney < selectChipNumber) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
//                        return;
                    }
                    _currentMoney = _currentMoney - (total - selectChipNumber);
                    if (_currentMoney < selectChipNumber/* && !isAllRemain*/) {
                        LogUtil.i("1total=" + total + " _currentMoney=" + _currentMoney);
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        LogUtil.i("2total=" + total + " _currentMoney=" + BaccaratUtil.addChip(Math.abs(_currentMoney)) + " chipScore.get(2)=" + chipScore.get(2) + " cv_tie_chip=" + cv_tie_chip.getChipNumSize());
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_tie_chip.addChip(integer);
                        }
                        rl_tie_chip.setVisibility(View.VISIBLE);
                        int currentValue = perTie - selectChipNumber + Math.abs(_currentMoney);
                        tv_tie_integer.setText(String.valueOf(currentValue));
                        chipScore.put(2, (tieScore - selectChipNumber + _currentMoney));
                        LogUtil.i("3total=" + total + " _currentMoney=" + _currentMoney + " chipScore.get(2)=" + chipScore.get(2) + " cv_tie_chip=" + cv_tie_chip.getChipNumSize());
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
                } else {
//                    if (maxBetList.size() > 0 && (tieScore > maxBetList.get(1) || perTie > maxBetList.get(1))) {
//                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
//                        tieScore = 0;
//                        perTie = perTie - selectChipNumber;
//                        return;
//                    }
                    if (!isSame) {
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
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie + perSmall + perBig;
                tv_bet_all_score.setText(String.valueOf(allScore));
            }
        });
        rl_big.setOnClickListener(new View.OnClickListener() {//大
            @Override
            public void onClick(View v) {
                int selectChipNumber = initChipLayout();
                if (isFirstBig && minBetList.size() > 0 && selectChipNumber < minBetList.get(3) && perBig == 0) {//0 庄闲 1 和 2 对子 3大小
                    selectChipNumber = minBetList.get(3);
                    isFirstBig = false;
                }
                bigScore += selectChipNumber;
                perBig += selectChipNumber;
                int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perBig + perSmall;
                if (insufficientBalance(3)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (total > limitMoney) {
                    if (/*isAllRemain &&*/ limitMoney < selectChipNumber) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
//                        return;
                    }
                    _currentMoney = _currentMoney - (total - selectChipNumber);
                    if (_currentMoney < selectChipNumber /*&& !isAllRemain*/) {
                        LogUtil.i("1total=" + total + " _currentMoney=" + _currentMoney);
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        LogUtil.i("2total=" + total + " _currentMoney=" + BaccaratUtil.addChip(Math.abs(_currentMoney)) + " chipScore.get(2)=" + chipScore.get(2) + " cv_tie_chip=" + cv_tie_chip.getChipNumSize());
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_big_chip.addChip(integer);
                        }
                        rl_big_chip.setVisibility(View.VISIBLE);
                        int currentValue = perBig - selectChipNumber + Math.abs(_currentMoney);
                        tv_big_integer.setText(String.valueOf(currentValue));
                        chipScore.put(6, (bigScore - selectChipNumber + _currentMoney));
                        LogUtil.i("3total=" + total + " _currentMoney=" + _currentMoney + " chipScore.get(2)=" + chipScore.get(2) + " cv_tie_chip=" + cv_tie_chip.getChipNumSize());
                        rl_big_chip.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_big_chip.getLayoutParams();
                        if (cv_big_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_big_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_big_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_big_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_big_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_big_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_big_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_big_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_big_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_big_integer.setLayoutParams(tvParams);
                    }
                } else {
//                    if (maxBetList.size() > 0 && (bigScore > maxBetList.get(1) || perBig > maxBetList.get(3))) {
//                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
//                        bigScore = 0;
//                        perBig = perBig - selectChipNumber;
//                        return;
//                    }
                    if (!isSame) {
                        chipScore.put(6, bigScore);
                    } else {
                        chipScore.put(6, perBig);//续押分数
                    }
                    if (cv_big_chip.getChipNumSize() < 15) {
                        rl_big_chip.setVisibility(View.VISIBLE);
                        cv_big_chip.addChip(selectChipNumber);
                        tv_big_integer.setText(String.valueOf(perBig));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_big_chip.getLayoutParams();
                        if (cv_big_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_big_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_big_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_big_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_big_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_big_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_big_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_big_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_big_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_big_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie + perSmall + perBig;
                tv_bet_all_score.setText(String.valueOf(allScore));
            }
        });
        rl_small.setOnClickListener(new View.OnClickListener() {//小
            @Override
            public void onClick(View v) {
                int selectChipNumber = initChipLayout();
                if (isFirstSmall && minBetList.size() > 0 && selectChipNumber < minBetList.get(3) && perSmall == 0) {//0 庄闲 1 和 2 对子 3大小
                    selectChipNumber = minBetList.get(3);
                    isFirstSmall = false;
                }
                smallScore += selectChipNumber;
                perSmall += selectChipNumber;
                int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perBig + perSmall;
                if (insufficientBalance(3)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (total > limitMoney) {
                    if (/*isAllRemain &&*/ limitMoney < selectChipNumber) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
//                        return;
                    }
                    _currentMoney = _currentMoney - (total - selectChipNumber);
                    if (_currentMoney < selectChipNumber/* && !isAllRemain*/) {
                        LogUtil.i("1total=" + total + " _currentMoney=" + _currentMoney);
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        LogUtil.i("2total=" + total + " _currentMoney=" + BaccaratUtil.addChip(Math.abs(_currentMoney)) + " chipScore.get(2)=" + chipScore.get(2) + " cv_tie_chip=" + cv_tie_chip.getChipNumSize());
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_small_chip.addChip(integer);
                        }
                        rl_small_chip.setVisibility(View.VISIBLE);
                        int currentValue = perSmall - selectChipNumber + Math.abs(_currentMoney);
                        tv_small_integer.setText(String.valueOf(currentValue));
                        chipScore.put(5, (smallScore - selectChipNumber + _currentMoney));
                        LogUtil.i("3total=" + total + " _currentMoney=" + _currentMoney + " chipScore.get(2)=" + chipScore.get(2) + " cv_tie_chip=" + cv_tie_chip.getChipNumSize());
                        rl_small_chip.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_small_chip.getLayoutParams();
                        if (cv_small_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_small_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_small_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_small_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_small_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_small_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_small_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_small_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_small_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_small_integer.setLayoutParams(tvParams);
                    }
                } else {
//                    if (maxBetList.size() > 0 && (smallScore > maxBetList.get(1) || perSmall > maxBetList.get(3))) {
//                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
//                        smallScore = 0;
//                        perSmall = perSmall - selectChipNumber;
//                        return;
//                    }
                    if (!isSame) {
                        chipScore.put(5, smallScore);
                    } else {
                        chipScore.put(5, perSmall);//续押分数
                    }
                    if (cv_small_chip.getChipNumSize() < 15) {
                        rl_small_chip.setVisibility(View.VISIBLE);
                        cv_small_chip.addChip(selectChipNumber);
                        tv_small_integer.setText(String.valueOf(perSmall));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_small_chip.getLayoutParams();
                        if (cv_small_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_small_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_small_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_small_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_small_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_small_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_small_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_small_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_small_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_small_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie + perSmall + perBig;
                tv_bet_all_score.setText(String.valueOf(allScore));
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
                int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker;
                if (insufficientBalance(0)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                LogUtil.i("2total=" + total + " _currentMoney=" + _currentMoney + " chipScore.get(0)=" + chipScore.get(0) + " cv_banker_chip=" + cv_banker_chip.getChipNumSize() + " bankerScore=" + bankerScore + " perBanker=" + perBanker);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (total > limitMoney) {
                    if (/*isAllRemain &&*/ limitMoney < selectChipNumber) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
//                        return;
                    }
                    _currentMoney = _currentMoney - (total - selectChipNumber);//多出来的分数
                    if (_currentMoney < selectChipNumber/* && !isAllRemain*/) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        LogUtil.i("3total=" + total + " _currentMoney=" + _currentMoney + " chipScore.get(0)=" + chipScore.get(0) + " cv_banker_chip=" + cv_banker_chip.getChipNumSize() + " bankerScore=" + bankerScore + " perBanker=" + perBanker + " _playerChip=" + _playerChip.toString());
                        for (Integer integer : _playerChip) {
                            cv_banker_chip.addChip(integer);
                        }
                        rl_banker_chip.setVisibility(View.VISIBLE);
                        int currentValue = perBanker - selectChipNumber + Math.abs(_currentMoney);
                        tv_banker_integer.setText(String.valueOf(currentValue));//当前局总分数
                        chipScore.put(0, (bankerScore - selectChipNumber + _currentMoney));//每次的投注分数
                        LogUtil.i("4total=" + total + " _currentMoney=" + _currentMoney + " chipScore.get(0)=" + chipScore.get(0) + " cv_banker_chip=" + cv_banker_chip.getChipNumSize() + " bankerScore=" + bankerScore + " perBanker=" + perBanker);
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
                } else {
//                    if (maxBetList.size() > 0 && (bankerScore > maxBetList.get(0) || perBanker > maxBetList.get(0))) {
//                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
//                        bankerScore = 0;
//                        perBanker = perBanker - selectChipNumber;
//                        return;
//                    }
                    if (!isSame) {
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
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie + perSmall + perBig;
                tv_bet_all_score.setText(String.valueOf(allScore));
            }
        });
        //-----------------------------------------------------------------点击对应下注区域-end--------------------------------------------
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
        tv_switch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_switch_on.setVisibility(View.GONE);
                tv_switch_off.setVisibility(View.VISIBLE);
                tv_no_commission_msg.setVisibility(View.VISIBLE);
                tv_change_banker.setText(R.string.banker_color_1);
                free = 1;
            }
        });
        tv_switch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_switch_on.setVisibility(View.VISIBLE);
                tv_switch_off.setVisibility(View.GONE);
                tv_no_commission_msg.setVisibility(View.GONE);
                tv_change_banker.setText(R.string.banker_color_95);
                free = 0;
            }
        });
        tv_full_road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpand == 2) {
                    trans2to0();
                } else if (isExpand == 0) {
                    trans0to2();
                }
            }
        });
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

    private void sameBetBig(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_big_chip.addChip(integer);
        }
        rl_big_chip.setVisibility(View.VISIBLE);
        tv_big_integer.setText(String.valueOf(_currentMoney));
        chipScore.put(6, _currentMoney);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_big_chip.getLayoutParams();
        if (cv_big_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
        } else if (cv_big_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
        } else if (cv_big_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
        } else if (cv_big_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
        } else if (cv_big_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
        } else if (cv_big_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
        } else if (cv_big_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_big_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_big_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_big_integer.setLayoutParams(tvParams);
    }

    private void sameBetSmall(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_small_chip.addChip(integer);
        }
        rl_small_chip.setVisibility(View.VISIBLE);
        tv_small_integer.setText(String.valueOf(_currentMoney));
        chipScore.put(5, _currentMoney);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_small_chip.getLayoutParams();
        if (cv_small_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
        } else if (cv_small_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
        } else if (cv_small_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
        } else if (cv_small_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
        } else if (cv_small_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
        } else if (cv_small_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
        } else if (cv_small_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_small_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_small_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_small_integer.setLayoutParams(tvParams);
    }

    private void sameBetBankPair(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_banker_pair_chip.addChip(integer);
        }
        rl_banker_pair_chip.setVisibility(View.VISIBLE);
        tv_banker_pair_integer.setText(String.valueOf(_currentMoney));
        chipScore.put(3, _currentMoney);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_pair_chip.getLayoutParams();
        if (cv_banker_pair_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
        } else if (cv_banker_pair_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
        } else if (cv_banker_pair_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
        } else if (cv_banker_pair_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
        } else if (cv_banker_pair_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
        } else if (cv_banker_pair_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
        } else if (cv_banker_pair_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_banker_pair_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_pair_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_banker_pair_integer.setLayoutParams(tvParams);
    }

    private void sameBetPlayPair(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_player_pair_chip.addChip(integer);
        }
        rl_player_pair_chip.setVisibility(View.VISIBLE);
        tv_player_pair_integer.setText(String.valueOf(_currentMoney));
        chipScore.put(4, _currentMoney);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_pair_chip.getLayoutParams();
        if (cv_player_pair_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
        } else if (cv_player_pair_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
        } else if (cv_player_pair_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
        } else if (cv_player_pair_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
        } else if (cv_player_pair_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
        } else if (cv_player_pair_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
        } else if (cv_player_pair_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_player_pair_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_pair_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_player_pair_integer.setLayoutParams(tvParams);
    }

    /**
     * 庄问路
     */
    private void ask(int value, boolean isShowAsk, boolean isDraw, int count, boolean isShow) {
        boardMessageList1.clear();
        boardMessageList1.addAll(boardMessageList);
        boardMessageList1.add(value);
        Algorithm.getInstance().initBigRoad(boardMessageList1, beadRoadList, beadRoadListShort,
                bigRoadListAll, bigRoadList, bigRoadListShort,
                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
                smallRoadListAll, smallRoadList, smallRoadListShort,
                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort,
                maxScoreListAll, maxScoreList, mContext, 1, isShowAsk,
                iv_ask_bank_1, iv_ask_bank_2, iv_ask_bank_3, iv_ask_play_1, iv_ask_play_2, iv_ask_play_3,
                Color.RED, Color.BLUE, Color.GREEN);//1房间 2大厅
        if (isDraw) {
            if (isExpand == 0) {
                Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                        gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 0, Color.RED, Color.BLUE, Color.GREEN, count, isShow, 2);//4列 加一局
            } else if (isExpand == 2) {
                Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                        gv_left, gv_right_top, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 0, Color.RED, Color.BLUE, Color.GREEN, count, isShow, 2);//11列 加一局
            }
        }
    }

    private int initChipLayout() {
        int selectChipNumber = SharedPreUtil.getInstance(mContext).getInt(Constant.SELECT_CHIP_NUMBER);
        if (isChip) {
            cleanChip();
        }
        isChipClick(true, true, R.drawable.button_bet_normal_bg, R.drawable.button_bet_normal_bg, R.string.confirm);
        SoundPoolUtil.getInstance().play("add_chips");
        return selectChipNumber;
    }

    private void cleanChip() {
        //下注成功的 就不能清除
        chipScore.put(0, currentBetScore.get(1));
        chipScore.put(1, currentBetScore.get(0));
        chipScore.put(2, currentBetScore.get(2));
        chipScore.put(3, currentBetScore.get(4));
        chipScore.put(4, currentBetScore.get(3));
        chipScore.put(5, currentBetScore.get(5));
        chipScore.put(6, currentBetScore.get(6));

        cv_player_pair_chip.addChip(0);
        cv_player_pair_chip1.addChip(0);
        cv_banker_pair_chip.addChip(0);
        cv_banker_pair_chip1.addChip(0);
        cv_player_chip.addChip(0);
        cv_player_chip1.addChip(0);
        cv_player_chip2.addChip(0);
        cv_banker_chip.addChip(0);
        cv_banker_chip1.addChip(0);
        cv_banker_chip2.addChip(0);
        cv_tie_chip.addChip(0);
        cv_big_chip.addChip(0);
        cv_small_chip.addChip(0);
        rl_sure.setBackgroundResource(R.drawable.button_bet_disable_bg);
        rl_cancel.setBackgroundResource(R.drawable.button_bet_disable_bg);

        playerPairScore = 0;
        bankerPairScore = 0;
        playerScore = 0;
        tieScore = 0;
        bankerScore = 0;
        perBanker = 0;
        perPlayer = 0;
        perTie = 0;
        perBig = 0;
        perSmall = 0;
        perBankerPair = 0;
        perPlayerPair = 0;
        chipScore.clear();
        isChip = false;
        isSame = false;

        rl_player_pair_chip.setVisibility(View.GONE);
        rl_player_pair_chip1.setVisibility(View.GONE);
        rl_banker_pair_chip.setVisibility(View.GONE);
        rl_banker_pair_chip1.setVisibility(View.GONE);
        rl_tie_chip.setVisibility(View.GONE);
        rl_big_chip.setVisibility(View.GONE);
        rl_small_chip.setVisibility(View.GONE);
        rl_player_chip.setVisibility(View.GONE);
        rl_player_chip1.setVisibility(View.GONE);
        rl_player_chip2.setVisibility(View.GONE);
        rl_banker_chip.setVisibility(View.GONE);
        rl_banker_chip1.setVisibility(View.GONE);
        rl_banker_chip2.setVisibility(View.GONE);
        LogUtil.i("cleanChip=" + cv_player_chip.getChipNumSize());
    }

    /**
     * 继承GestureListener，重写left和right方法
     */
    public class MyGestureListener extends GestureListener {
        private int isExpand;

        MyGestureListener(Context context) {
            super(context);
        }

        @Override
        public boolean left() {
            //2160 1008
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
        slideView(-(getResources().getDimensionPixelSize(R.dimen.unit210)), 0, rl_in_time_layout);
        iv_ex.setRotation(180);
        isExpand = 0;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 0);
        tv_full_road.setText(mContext.getResources().getString(R.string.half_road));
        Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 0, Color.RED, Color.BLUE, Color.GREEN, 0, false, 2);//4列 trans1to0
    }

    private void trans0to1() {
        slideView(0, -(getResources().getDimensionPixelSize(R.dimen.unit210)), rl_in_time_layout);//本来是向右平移，结果右边为负数，就以看到的点为坐标原点，向左平移
        iv_ex.setRotation(0);
        isExpand = 1;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 1);
    }

    private void trans0to2() {
        isExpand = 2;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 2);
        tv_full_road.setText(mContext.getResources().getString(R.string.full_road));
        int codeAdapterDimens = getResources().getDimensionPixelSize(R.dimen.unit450);
        rlRoadLayoutParams.width = codeAdapterDimens;
        rlStatisticLayoutParams.width = codeAdapterDimens;
        rlContentLayoutParams.width = codeAdapterDimens;
        gv_left_short.setVisibility(View.GONE);
        gv_right_top_short.setVisibility(View.GONE);
        gv_right_middle_short.setVisibility(View.GONE);
        gv_right_middle_short_grid.setVisibility(View.GONE);
        gv_right_bottom_1_short.setVisibility(View.GONE);
        gv_right_bottom_1_short_grid.setVisibility(View.GONE);
        gv_right_bottom_2_short.setVisibility(View.GONE);
        gv_right_bottom_2_short_grid.setVisibility(View.GONE);
        v_1_short.setVisibility(View.GONE);
        v_2_short.setVisibility(View.GONE);
        v_3_short.setVisibility(View.GONE);
        v_4_short.setVisibility(View.GONE);
        v_5_short.setVisibility(View.GONE);
        gv_left.setVisibility(View.VISIBLE);
        gv_right_top.setVisibility(View.VISIBLE);
        gv_right_middle.setVisibility(View.VISIBLE);
        gv_right_middle_grid.setVisibility(View.VISIBLE);
        gv_right_bottom_1.setVisibility(View.VISIBLE);
        gv_right_bottom_1_grid.setVisibility(View.VISIBLE);
        gv_right_bottom_2.setVisibility(View.VISIBLE);
        gv_right_bottom_2_grid.setVisibility(View.VISIBLE);
        v_1.setVisibility(View.VISIBLE);
        v_2.setVisibility(View.VISIBLE);
        v_3.setVisibility(View.VISIBLE);
        v_4.setVisibility(View.VISIBLE);
        v_5.setVisibility(View.VISIBLE);
        Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                gv_left, gv_right_top, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 0, Color.RED, Color.BLUE, Color.GREEN, 0, false, 2);//11列 trans0to2
//        drawAsk(1);
    }

    private void trans2to0() {
        isExpand = 0;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_EXPAND, 0);
        tv_full_road.setText(mContext.getResources().getString(R.string.half_road));
        rl_ex_in.setVisibility(View.VISIBLE);
        int codeAdapterDimens = getResources().getDimensionPixelSize(R.dimen.unit210);
        rlRoadLayoutParams.width = codeAdapterDimens;
        rlStatisticLayoutParams.width = codeAdapterDimens;
        rlContentLayoutParams.width = codeAdapterDimens;
        gv_left_short.setVisibility(View.VISIBLE);
        gv_right_top_short.setVisibility(View.VISIBLE);
        gv_right_middle_short.setVisibility(View.VISIBLE);
        gv_right_middle_short_grid.setVisibility(View.VISIBLE);
        gv_right_bottom_1_short.setVisibility(View.VISIBLE);
        gv_right_bottom_1_short_grid.setVisibility(View.VISIBLE);
        gv_right_bottom_2_short.setVisibility(View.VISIBLE);
        gv_right_bottom_2_short_grid.setVisibility(View.VISIBLE);
        v_1_short.setVisibility(View.VISIBLE);
        v_2_short.setVisibility(View.VISIBLE);
        v_3_short.setVisibility(View.VISIBLE);
        v_4_short.setVisibility(View.VISIBLE);
        v_5_short.setVisibility(View.VISIBLE);
        gv_left.setVisibility(View.GONE);
        gv_right_top.setVisibility(View.GONE);
        gv_right_middle.setVisibility(View.GONE);
        gv_right_middle_grid.setVisibility(View.GONE);
        gv_right_bottom_1.setVisibility(View.GONE);
        gv_right_bottom_1_grid.setVisibility(View.GONE);
        gv_right_bottom_2.setVisibility(View.GONE);
        gv_right_bottom_2_grid.setVisibility(View.GONE);
        v_1.setVisibility(View.GONE);
        v_2.setVisibility(View.GONE);
        v_3.setVisibility(View.GONE);
        v_4.setVisibility(View.GONE);
        v_5.setVisibility(View.GONE);

        Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 0, Color.RED, Color.BLUE, Color.GREEN, 0, false, 2);//4列 trans2to0
//        drawAsk(1);
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

    private void initSound() {
        if (SharedPreUtil.getInstance(this).getInt(Constant.SOUND_SWITCH) == 0) {
            try {
                int languageType = CommSharedUtil.getInstance(this).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
                SoundPoolUtil.getInstance().release();
                SoundPoolUtil.getInstance().initCommon(this);
                if (languageType == LanguageType.LANGUAGE_EN) {
                    SoundPoolUtil.getInstance().initBacEN(this);
                } else {
                    SoundPoolUtil.getInstance().initBacZH(this);
                }
                //初始化完毕才能播放（延迟加载，是因为偶尔播放）
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {
                        SoundPoolUtil.getInstance().play("1");
                    }
                }, 100);
            } catch (Exception e) {
                e.printStackTrace();
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
            beadRoadListShort.clear();
        }
        if (bigRoadListAll.size() > 0) {
            bigRoadListAll.clear();
            bigRoadList.clear();
            bigRoadListShort.clear();
        }
        if (bigEyeRoadListAll.size() > 0) {
            bigEyeRoadListAll.clear();
            bigEyeRoadList.clear();
            bigEyeRoadListShort.clear();
        }
        if (smallRoadListAll.size() > 0) {
            smallRoadListAll.clear();
            smallRoadList.clear();
            smallRoadListShort.clear();
        }
        if (cockroachRoadListAll.size() > 0) {
            cockroachRoadListAll.clear();
            cockroachRoadList.clear();
            cockroachRoadListShort.clear();
        }
        //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
        int total = boardMessageList.size(), banker = 0, player = 0, tie = 0, bankerPair = 0, playerPair = 0;

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
                if (boardMessageList.get(i) == 3 || boardMessageList.get(i) == 5 || boardMessageList.get(i) == 6 || boardMessageList.get(i) == 8 || boardMessageList.get(i) == 9 || boardMessageList.get(i) == 11) {
                    bankerPair++;
                }
                if (boardMessageList.get(i) == 4 || boardMessageList.get(i) == 5 || boardMessageList.get(i) == 7 || boardMessageList.get(i) == 8 || boardMessageList.get(i) == 10 || boardMessageList.get(i) == 11) {
                    playerPair++;
                }
            }
        }
        tv_in_game_total_value.setText(String.valueOf(total));
        tv_in_game_banker_value.setText(String.valueOf(banker));
        tv_in_game_player_value.setText(String.valueOf(player));
        tv_in_game_tie_value.setText(String.valueOf(tie));
        tv_in_game_banker_pair_value.setText(String.valueOf(bankerPair));
        tv_in_game_player_pair_value.setText(String.valueOf(playerPair));

        Algorithm.getInstance().initBigRoad(boardMessageList, beadRoadList, beadRoadListShort,
                bigRoadListAll, bigRoadList, bigRoadListShort,
                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
                smallRoadListAll, smallRoadList, smallRoadListShort,
                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort,
                maxScoreListAll, maxScoreList, mContext, 1, false,
                iv_ask_bank_1, iv_ask_bank_2, iv_ask_bank_3, iv_ask_play_1, iv_ask_play_2, iv_ask_play_3,
                Color.RED, Color.BLUE, Color.GREEN);//1房间 2大厅
    }

    /**
     * 初始化走势图（进入房间时）
     */
    private void initGvShort() {
        rlContentLayoutParams = (RelativeLayout.LayoutParams) rl_content.getLayoutParams();
        rlRoadLayoutParams = (RelativeLayout.LayoutParams) rl_road.getLayoutParams();
        rlStatisticLayoutParams = (RelativeLayout.LayoutParams) rl_statistic.getLayoutParams();

        gv_left_short.setVisibility(View.VISIBLE);
        gv_right_top_short.setVisibility(View.VISIBLE);
        gv_right_middle_short.setVisibility(View.VISIBLE);
        gv_right_middle_short_grid.setVisibility(View.VISIBLE);
        gv_right_bottom_1_short.setVisibility(View.VISIBLE);
        gv_right_bottom_1_short_grid.setVisibility(View.VISIBLE);
        gv_right_bottom_2_short.setVisibility(View.VISIBLE);
        gv_right_bottom_2_short_grid.setVisibility(View.VISIBLE);
        gv_left.setVisibility(View.GONE);
        gv_right_top.setVisibility(View.GONE);
        gv_right_middle.setVisibility(View.GONE);
        gv_right_middle_grid.setVisibility(View.GONE);
        gv_right_bottom_1.setVisibility(View.GONE);
        gv_right_bottom_1_grid.setVisibility(View.GONE);
        gv_right_bottom_2.setVisibility(View.GONE);
        gv_right_bottom_2_grid.setVisibility(View.GONE);
    }

    /**
     * 下注
     *
     * @param clickable true 可下注
     */
    private void onClick(boolean clickable) {
        rl_player_pair.setClickable(clickable);
        rl_banker_pair.setClickable(clickable);
        rl_player.setClickable(clickable);
        rl_tie.setClickable(clickable);
        rl_banker.setClickable(clickable);
        rl_sure.setClickable(clickable);
        tv_switch_on.setClickable(clickable);
        tv_switch_off.setClickable(clickable);
        rl_big.setClickable(clickable);
        rl_small.setClickable(clickable);
    }

    /*
     * 进入房间
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBoardMessage(final ObjectEvent.BoardMessageEvent event) {
        long startTime = SystemClock.currentThreadTimeMillis();
        tv_money.setText(event.goldCoins);
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
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BET_LIMIT, betLimitList.toString());
        SharedPreUtil.getInstance(mContext).saveParam(Constant.MAX_BET, maxBetList.toString());
        SharedPreUtil.getInstance(mContext).saveParam(Constant.MIN_BET, minBetList.toString());
        if (!event.state.equals(Constant.BACCARAT_BET)) {
            onClick(false);
            ll_poker.setVisibility(View.GONE);
        } else {
            currentMoney = Double.parseDouble(tv_money.getText().toString());
        }
        if (event.boardMessageList.size() > 0) {
            boardMessageList.clear();
            boardMessageList.addAll(event.boardMessageList);
            tv_game_name.setText(mContext.getResources().getString(R.string.ba_game) + event.roomName);
            deskId = event.deskId;
            tv_code.setText(getResources().getString(R.string.game_code) + ":" + String.valueOf(event.deskId) + boardMessageList.size());
            initMessages();
            Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                    gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 0, Color.RED,
                    Color.BLUE, Color.GREEN, 0, false, 2);//4列 网络进入房间
            ask(0, true, false, 0, false);
        }
        String _1 = "0";
        String _2 = "0";
        String _3 = "0";
        String _4 = "0";
        String _5 = "0";
        String _6 = "0";
        String _7 = "0";
        if (isPeopleNum == 0) {
            _1 = String.valueOf(playerIng);
            _2 = String.valueOf(bankerIng);
            _3 = String.valueOf(tieIng);
            _4 = String.valueOf(playerPairIng);
            _5 = String.valueOf(bankerPairIng);
            _6 = String.valueOf(bigIng);
            _7 = String.valueOf(smallIng);
        } else if (isPeopleNum == 1) {
            _1 = String.valueOf(playerIng) + "/" + 0;
            _2 = String.valueOf(bankerIng) + "/" + 0;
            _3 = String.valueOf(tieIng) + "/" + 0;
            _4 = String.valueOf(playerPairIng) + "/" + 0;
            _5 = String.valueOf(bankerPairIng) + "/" + 0;
            _6 = String.valueOf(bigIng) + "/" + 0;
            _7 = String.valueOf(smallIng) + "/" + 0;
        }
        tv_play_date.setText(_1);
        tv_banker_date.setText(_2);
        tv_tie_date.setText(_3);
        tv_player_pair_date.setText(_4);
        tv_banker_pair_date.setText(_5);
        //所有数据加载完成再初始化资源文件
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initSound();//在低端机上 加载assets资源，超级耗时
            }
        }, 200);
    }

    /*
     * 初始化牌局(牌局的下注积分数 0闲 1庄 2和 3闲对 4庄对)
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBetRecord(final ObjectEvent.BetRecordEvent event) {
        if (event.scoreList.size() > 0) {
            playerIng = event.scoreList.get(0);
            bankerIng = event.scoreList.get(1);
            tieIng = event.scoreList.get(2);
            playerPairIng = event.scoreList.get(3);
            bankerPairIng = event.scoreList.get(4);
            smallIng = event.scoreList.get(5);
            bigIng = event.scoreList.get(6);
            String _1 = "0";
            String _2 = "0";
            String _3 = "0";
            String _4 = "0";
            String _5 = "0";
            String _6 = "0";
            String _7 = "0";
            if (isPeopleNum == 0) {
                _1 = String.valueOf(playerIng);
                _2 = String.valueOf(bankerIng);
                _3 = String.valueOf(tieIng);
                _4 = String.valueOf(playerPairIng);
                _5 = String.valueOf(bankerPairIng);
                _6 = String.valueOf(smallIng);
                _7 = String.valueOf(bigIng);
            } else if (isPeopleNum == 1) {
                _1 = String.valueOf(playerIng) + "/" + event.betPeopleList.get(0);
                _2 = String.valueOf(bankerIng) + "/" + event.betPeopleList.get(1);
                _3 = String.valueOf(tieIng) + "/" + String.valueOf(event.betPeopleList.get(2));
                _4 = String.valueOf(playerPairIng) + "/" + String.valueOf(event.betPeopleList.get(3));
                _5 = String.valueOf(bankerPairIng) + "/" + String.valueOf(event.betPeopleList.get(4));
                _6 = String.valueOf(smallIng) + "/" + String.valueOf(event.betPeopleList.get(5));
                _7 = String.valueOf(bigIng) + "/" + String.valueOf(event.betPeopleList.get(6));
            }
            tv_play_date.setText(_1);
            tv_banker_date.setText(_2);
            tv_tie_date.setText(_3);
            tv_player_pair_date.setText(_4);
            tv_banker_pair_date.setText(_5);
            tv_small_date.setText(_6);
            tv_big_date.setText(_7);
        }
    }

    /*
     * 当前下注金额(牌局的下注积分数 0闲 1庄 2和 3闲对 4庄对)
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBetScore(final ObjectEvent.betScoreEvent event) {
        currentBetScore.clear();
        currentBetScore.addAll(event.scoreList);
        currentChip();
    }

    private void currentChip2() {
        if (currentBetScore.get(0) == 0) {
            rl_player_chip.setVisibility(View.GONE);
            cv_player_chip.addChip(0);
            perPlayer = currentBetScore.get(0);
            playerScore = currentBetScore.get(0);
//            chipScore.put(1, currentBetScore.get(0));
        } else if (currentBetScore.get(0) == perPlayer) {
            rl_player_chip.setVisibility(View.VISIBLE);
            perPlayer = currentBetScore.get(0);
            cv_player_chip.addChip(0);
//            chipScore.put(1, currentBetScore.get(0));
            tv_player_integer.setText("" + currentBetScore.get(0));
            List<Integer> _playerChip = new ArrayList<>();
            _playerChip.addAll(BaccaratUtil.addChip(currentBetScore.get(0)));
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
        if (currentBetScore.get(1) == 0) {
            rl_banker_chip.setVisibility(View.GONE);
            cv_banker_chip.addChip(0);
            perBanker = currentBetScore.get(1);
            bankerScore = currentBetScore.get(1);
//            chipScore.put(0, currentBetScore.get(1));
        } else if (currentBetScore.get(1) == perBanker) {
            rl_banker_chip.setVisibility(View.VISIBLE);
            perBanker = currentBetScore.get(1);
            cv_banker_chip.addChip(0);
//            chipScore.put(0, currentBetScore.get(1));
            tv_banker_integer.setText("" + currentBetScore.get(1));
            List<Integer> _bankerChip = new ArrayList<>();
            _bankerChip.addAll(BaccaratUtil.addChip(currentBetScore.get(1)));
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

        if (currentBetScore.get(3) == 0) {
            rl_player_pair_chip.setVisibility(View.GONE);
            cv_player_pair_chip.addChip(0);
            perPlayerPair = currentBetScore.get(3);
            playerPairScore = currentBetScore.get(3);
//            chipScore.put(4, currentBetScore.get(3));
        } else if (currentBetScore.get(3) == perPlayerPair) {
            rl_player_pair_chip.setVisibility(View.VISIBLE);
            perPlayerPair = currentBetScore.get(3);
            cv_player_pair_chip.addChip(0);
//            chipScore.put(4, currentBetScore.get(3));
            tv_player_pair_integer.setText("" + currentBetScore.get(3));
            List<Integer> _playerPairChip = new ArrayList<>();
            _playerPairChip.addAll(BaccaratUtil.addChip(currentBetScore.get(3)));
            for (Integer integer : _playerPairChip) {
                cv_player_pair_chip.addChip(integer);
            }
            if (cv_player_pair_chip.getChipNumSize() < 15) {//筹码堆叠1
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_pair_chip.getLayoutParams();
                if (cv_player_pair_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_player_pair_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_player_pair_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_player_pair_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_player_pair_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_player_pair_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_player_pair_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_player_pair_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_pair_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_player_pair_integer.setLayoutParams(tvParams);
            }
        }
        if (currentBetScore.get(4) == 0) {
            rl_banker_pair_chip.setVisibility(View.GONE);
            cv_banker_pair_chip.addChip(0);
            perBankerPair = currentBetScore.get(4);
            bankerPairScore = currentBetScore.get(4);
//            chipScore.put(3, currentBetScore.get(4));
        } else if (currentBetScore.get(4) == perBankerPair) {
            rl_banker_pair_chip.setVisibility(View.VISIBLE);
            perBankerPair = currentBetScore.get(4);
            cv_banker_pair_chip.addChip(0);
//            chipScore.put(3, currentBetScore.get(4));
            tv_banker_pair_integer.setText("" + currentBetScore.get(4));
            List<Integer> _bankerPairChip = new ArrayList<>();
            _bankerPairChip.addAll(BaccaratUtil.addChip(currentBetScore.get(4)));
            for (Integer integer : _bankerPairChip) {
                cv_banker_pair_chip.addChip(integer);
            }
            if (cv_banker_pair_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_pair_chip.getLayoutParams();
                if (cv_banker_pair_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_banker_pair_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_banker_pair_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_banker_pair_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_banker_pair_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_banker_pair_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_banker_pair_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_banker_pair_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_pair_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_banker_pair_integer.setLayoutParams(tvParams);
            }
        }

        if (currentBetScore.get(6) == 0) {
            rl_big_chip.setVisibility(View.GONE);
            cv_big_chip.addChip(0);
            perBig = currentBetScore.get(6);
            bigScore = currentBetScore.get(6);
//            chipScore.put(6, currentBetScore.get(6));
        } else if (currentBetScore.get(6) == perBig) {
            rl_big_chip.setVisibility(View.VISIBLE);
            perBig = currentBetScore.get(6);
            cv_big_chip.addChip(0);
//            chipScore.put(6, currentBetScore.get(6));
            tv_big_integer.setText("" + currentBetScore.get(6));
            List<Integer> _bankerPairChip = new ArrayList<>();
            _bankerPairChip.addAll(BaccaratUtil.addChip(currentBetScore.get(6)));
            for (Integer integer : _bankerPairChip) {
                cv_big_chip.addChip(integer);
            }
            if (cv_big_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_big_chip.getLayoutParams();
                if (cv_big_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_big_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_big_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_big_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_big_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_big_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_big_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_big_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_big_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_big_integer.setLayoutParams(tvParams);
            }
        }

        if (currentBetScore.get(5) == 0) {
            rl_small_chip.setVisibility(View.GONE);
            cv_small_chip.addChip(0);
            perSmall = currentBetScore.get(5);
            smallScore = currentBetScore.get(5);
//            chipScore.put(5, currentBetScore.get(5));
        } else if (currentBetScore.get(5) == perSmall) {
            rl_small_chip.setVisibility(View.VISIBLE);
            perSmall = currentBetScore.get(5);
            cv_small_chip.addChip(0);
//            chipScore.put(5, currentBetScore.get(5));
            tv_small_integer.setText("" + currentBetScore.get(5));
            List<Integer> _bankerPairChip = new ArrayList<>();
            _bankerPairChip.addAll(BaccaratUtil.addChip(currentBetScore.get(5)));
            for (Integer integer : _bankerPairChip) {
                cv_small_chip.addChip(integer);
            }
            if (cv_small_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_small_chip.getLayoutParams();
                if (cv_small_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_small_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_small_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_small_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_small_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_small_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_small_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_small_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_small_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_small_integer.setLayoutParams(tvParams);
            }
        }

    }

    private void currentChip() {
        if (currentBetScore.get(0) == 0) {
            rl_player_chip.setVisibility(View.GONE);
            cv_player_chip.addChip(0);
            perPlayer = currentBetScore.get(0);
            playerScore = currentBetScore.get(0);
            chipScore.put(1, currentBetScore.get(0));
        } else if (currentBetScore.get(0) != perPlayer) {
            rl_player_chip.setVisibility(View.VISIBLE);
            perPlayer = currentBetScore.get(0);
            cv_player_chip.addChip(0);
            chipScore.put(1, currentBetScore.get(0));
            tv_player_integer.setText("" + currentBetScore.get(0));
            List<Integer> _playerChip = new ArrayList<>();
            _playerChip.addAll(BaccaratUtil.addChip(currentBetScore.get(0)));
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
        if (currentBetScore.get(1) == 0) {
            rl_banker_chip.setVisibility(View.GONE);
            cv_banker_chip.addChip(0);
            perBanker = currentBetScore.get(1);
            bankerScore = currentBetScore.get(1);
            chipScore.put(0, currentBetScore.get(1));
        } else if (currentBetScore.get(1) != perBanker) {
            rl_banker_chip.setVisibility(View.VISIBLE);
            perBanker = currentBetScore.get(1);
            cv_banker_chip.addChip(0);
            chipScore.put(0, currentBetScore.get(1));
            tv_banker_integer.setText("" + currentBetScore.get(1));
            List<Integer> _bankerChip = new ArrayList<>();
            _bankerChip.addAll(BaccaratUtil.addChip(currentBetScore.get(1)));
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

        if (currentBetScore.get(3) == 0) {
            rl_player_pair_chip.setVisibility(View.GONE);
            cv_player_pair_chip.addChip(0);
            perPlayerPair = currentBetScore.get(3);
            playerPairScore = currentBetScore.get(3);
            chipScore.put(4, currentBetScore.get(3));
        } else if (currentBetScore.get(3) != perPlayerPair) {
            rl_player_pair_chip.setVisibility(View.VISIBLE);
            perPlayerPair = currentBetScore.get(3);
            cv_player_pair_chip.addChip(0);
            chipScore.put(4, currentBetScore.get(3));
            tv_player_pair_integer.setText("" + currentBetScore.get(3));
            List<Integer> _playerPairChip = new ArrayList<>();
            _playerPairChip.addAll(BaccaratUtil.addChip(currentBetScore.get(3)));
            for (Integer integer : _playerPairChip) {
                cv_player_pair_chip.addChip(integer);
            }
            if (cv_player_pair_chip.getChipNumSize() < 15) {//筹码堆叠1
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_pair_chip.getLayoutParams();
                if (cv_player_pair_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_player_pair_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_player_pair_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_player_pair_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_player_pair_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_player_pair_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_player_pair_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_player_pair_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_pair_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_player_pair_integer.setLayoutParams(tvParams);
            }
        }
        if (currentBetScore.get(4) == 0) {
            rl_banker_pair_chip.setVisibility(View.GONE);
            cv_banker_pair_chip.addChip(0);
            perBankerPair = currentBetScore.get(4);
            bankerPairScore = currentBetScore.get(4);
            chipScore.put(3, currentBetScore.get(4));
        } else if (currentBetScore.get(4) != perBankerPair) {
            rl_banker_pair_chip.setVisibility(View.VISIBLE);
            perBankerPair = currentBetScore.get(4);
            cv_banker_pair_chip.addChip(0);
            chipScore.put(3, currentBetScore.get(4));
            tv_banker_pair_integer.setText("" + currentBetScore.get(4));
            List<Integer> _bankerPairChip = new ArrayList<>();
            _bankerPairChip.addAll(BaccaratUtil.addChip(currentBetScore.get(4)));
            for (Integer integer : _bankerPairChip) {
                cv_banker_pair_chip.addChip(integer);
            }
            if (cv_banker_pair_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_pair_chip.getLayoutParams();
                if (cv_banker_pair_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_banker_pair_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_banker_pair_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_banker_pair_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_banker_pair_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_banker_pair_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_banker_pair_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_banker_pair_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_pair_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_banker_pair_integer.setLayoutParams(tvParams);
            }
        }

        if (currentBetScore.get(6) == 0) {
            rl_big_chip.setVisibility(View.GONE);
            cv_big_chip.addChip(0);
            perBig = currentBetScore.get(6);
            bigScore = currentBetScore.get(6);
            chipScore.put(6, currentBetScore.get(6));
        } else if (currentBetScore.get(6) != perBig) {
            rl_big_chip.setVisibility(View.VISIBLE);
            perBig = currentBetScore.get(6);
            cv_big_chip.addChip(0);
            chipScore.put(6, currentBetScore.get(6));
            tv_big_integer.setText("" + currentBetScore.get(6));
            List<Integer> _bankerPairChip = new ArrayList<>();
            _bankerPairChip.addAll(BaccaratUtil.addChip(currentBetScore.get(6)));
            for (Integer integer : _bankerPairChip) {
                cv_big_chip.addChip(integer);
            }
            if (cv_big_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_big_chip.getLayoutParams();
                if (cv_big_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_big_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_big_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_big_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_big_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_big_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_big_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_big_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_big_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_big_integer.setLayoutParams(tvParams);
            }
        }

        if (currentBetScore.get(5) == 0) {
            rl_small_chip.setVisibility(View.GONE);
            cv_small_chip.addChip(0);
            perSmall = currentBetScore.get(5);
            smallScore = currentBetScore.get(5);
            chipScore.put(5, currentBetScore.get(5));
        } else if (currentBetScore.get(5) != perSmall) {
            rl_small_chip.setVisibility(View.VISIBLE);
            perSmall = currentBetScore.get(5);
            cv_small_chip.addChip(0);
            chipScore.put(5, currentBetScore.get(5));
            tv_small_integer.setText("" + currentBetScore.get(5));
            List<Integer> _bankerPairChip = new ArrayList<>();
            _bankerPairChip.addAll(BaccaratUtil.addChip(currentBetScore.get(5)));
            for (Integer integer : _bankerPairChip) {
                cv_small_chip.addChip(integer);
            }
            if (cv_small_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_small_chip.getLayoutParams();
                if (cv_small_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                } else if (cv_small_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                } else if (cv_small_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                } else if (cv_small_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                } else if (cv_small_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                } else if (cv_small_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                } else if (cv_small_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_small_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_small_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_small_integer.setLayoutParams(tvParams);
            }
        }

    }

    /*
     * 状态 BET:下注阶段 RESULT:开牌阶段 OVER:清算阶段 WAITTING:同步识别端
     * */
    List<String> _calPlayer = new ArrayList<>();
    List<String> _calBanker = new ArrayList<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventState(final ObjectEvent.StateEvent event) {
        if (event.state.equals("BET")) {
            onClick(true);
            ll_poker.setVisibility(View.GONE);
            currentBetScore.clear();
            currentBetScore.add(0);
            currentBetScore.add(0);
            currentBetScore.add(0);
            currentBetScore.add(0);
            currentBetScore.add(0);
            currentBetScore.add(0);
            currentBetScore.add(0);
            isAllRemain = false;//恢复下注所有金额
            currentMoney = Double.parseDouble(tv_money.getText().toString());
            //先隐藏
            rl_player_pair_chip.setVisibility(View.GONE);
            rl_player_pair_chip1.setVisibility(View.GONE);
            rl_banker_pair_chip.setVisibility(View.GONE);
            rl_banker_pair_chip1.setVisibility(View.GONE);
            rl_tie_chip.setVisibility(View.GONE);
            rl_player_chip.setVisibility(View.GONE);
            rl_player_chip1.setVisibility(View.GONE);
            rl_player_chip2.setVisibility(View.GONE);
            rl_banker_chip.setVisibility(View.GONE);
            rl_banker_chip1.setVisibility(View.GONE);
            rl_banker_chip2.setVisibility(View.GONE);
            rl_big_chip.setVisibility(View.GONE);
            rl_small_chip.setVisibility(View.GONE);
            LogUtil.i("下注_command" + perPlayerPair + " " + isChip);
            if (perPlayerPair != 0 || perBankerPair != 0 || perPlayer != 0 || perTie != 0 || perBanker != 0 || perSmall != 0 || perBig != 0) {
                isChip = true;
                isChipClick(false, true, R.drawable.button_bet_disable_bg, R.drawable.button_bet_normal_bg, R.string.repeat);
            } else {
                isChip = false;
                isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            }

            ToastUtil.getInstance().showToast(mContext, mContext.getResources().getString(R.string.bet_start));
            isChipSuccess = 0;//新一局 代表未下注成功
            SoundPoolUtil.getInstance().play("2");//已开局，请下注
            Constant.SEND_CARD = betSecond;
            pcac.setAllTime(Constant.SEND_CARD);
            pcac.setTargetPercent(0);
            pcac.reInitView();

            rl_bank_light.setVisibility(View.GONE);
            FlashHelper.getInstance().stopFlick(rl_bank_light);
            rl_play_light.setVisibility(View.GONE);
            FlashHelper.getInstance().stopFlick(rl_play_light);
            isFirstPlay = true;
            isFirstBank = true;
            isFirstTie = true;
            isFirstPlayPair = true;
            isFirstBankPair = true;
            isFirstSmall = true;
            isFirstBig = true;
        } else if (event.state.equals("RESULT")) {
            onClick(false);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            ll_poker.setVisibility(View.VISIBLE);
            ToastUtil.getInstance().showToast(mContext, mContext.getResources().getString(R.string.bet_stop));
            SoundPoolUtil.getInstance().play("4");//停止下注
            //清空处理
            rl_jet_player_layout.setBackgroundResource(0);
            rl_jet_banker_layout.setBackgroundResource(0);
            rl_jet_player.setBackgroundResource(0);
            rl_jet_banker.setBackgroundResource(0);
            tv_jet_player_value.setText("0");
            tv_jet_banker_value.setText("0");
            iv_jet_player_01.setBackgroundResource(0);
            iv_jet_player_02.setBackgroundResource(0);
            iv_jet_player_03.setBackgroundResource(0);
            iv_jet_banker_01.setBackgroundResource(0);
            iv_jet_banker_02.setBackgroundResource(0);
            iv_jet_banker_03.setBackgroundResource(0);
            _calPlayer.clear();
            _calBanker.clear();
            if (betsRemind == 1) {
                outGame();
            }
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_TIE, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY_PAIR, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK_PAIR, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BIG, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_SMALL, 0);

        } else if (event.state.equals("OVER")) {
            onClick(false);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
        } else if (event.state.equals("WAITTING")) {
            onClick(false);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
        } else if (event.state.equals(Constant.BACCARAT_INNINGS_END)) {//当前轮结束
            ToastUtil.showToast(mContext, Constant.BACCARAT_INNINGS_END);
            pcac.setCurrentPercent(-1);//当前轮结束  没有改为  洗牌中
            pcac.update();//重新刷新一下就可以啦
            ll_poker.setVisibility(View.GONE);
            cleanChip();
            String _1 = 0 + "/" + 0;
            String _2 = 0 + "/" + 0;
            String _3 = 0 + "/" + 0;
            String _4 = 0 + "/" + 0;
            String _5 = 0 + "/" + 0;
            tv_play_date.setText(_1);
            tv_banker_date.setText(_2);
            tv_tie_date.setText(_3);
            tv_player_pair_date.setText(_4);
            tv_banker_pair_date.setText(_5);
        } else if (event.state.equals(Constant.BACCARAT_CREATED)) {//新的一轮开始
            boardMessageList.clear();
            initMessages();
            if (isExpand == 0) {
                Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                        gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 0, Color.RED, Color.BLUE, Color.GREEN, 0, false, 2);//4列 加一局
            } else if (isExpand == 2) {
                Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                        gv_left, gv_right_top, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 0, Color.RED, Color.BLUE, Color.GREEN, 0, false, 2);//11列 加一局
            }
        } else if (event.state.equals(Constant.BACCARAT_FAULT)) {
            ToastUtil.show(mContext, "机械臂异常");
        } else if (event.state.equals(Constant.BACCARAT_INVALID)) {
            ToastUtil.show(mContext, "本局作废");
        }
    }


    /*
     * 补牌 //01 庄补牌  02闲补牌
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventFillCard(final ObjectEvent.FillCardEvent event) {
        if (!TextUtils.isEmpty(event.fill)) {
            if (event.fill.equals("02")) {
                SoundPoolUtil.getInstance().play("94");//庄补
            } else if (event.fill.equals("01")) {
                SoundPoolUtil.getInstance().play("95");//闲补
            }
        }
    }

    /*
     * 轮数加下了多少局  为局号
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBureau(final ObjectEvent.BureauEvent event) {
        if (!TextUtils.isEmpty(event.bureau)) {
            tv_code.setText(getResources().getString(R.string.game_code) + ":" + event.bureau + boardMessageList.size());
        }
    }

    /*
     * 发牌 黑桃 红桃 草花 方块 1,2,3,4 10-a j-b q-c k-d
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventSwitchResult(final ObjectEvent.SwitchResultEvent event) {
        ll_poker.setVisibility(View.VISIBLE);
        SoundPoolUtil.getInstance().play("96");//发牌音效
        if (event.pos.equals("01")) {
            _calPlayer.clear();
            _calBanker.clear();
            iv_jet_player_01.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calPlayer.add(event.card);
            tv_jet_player_value.setText("" + event.playCardValue);
        } else if (event.pos.equals("03")) {
            iv_jet_player_02.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calPlayer.add(event.card);
            tv_jet_player_value.setText("" + event.playCardValue);
        } else if (event.pos.equals("05")) {
            iv_jet_player_03.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calPlayer.add(event.card);
            tv_jet_player_value.setText("" + event.playCardValue);
        }
        if (event.pos.equals("02")) {
            iv_jet_banker_01.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calBanker.add(event.card);
            tv_jet_banker_value.setText("" + event.bankCardValue);
        } else if (event.pos.equals("04")) {
            iv_jet_banker_02.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calBanker.add(event.card);
            tv_jet_banker_value.setText("" + event.bankCardValue);
        } else if (event.pos.equals("06")) {
            iv_jet_banker_03.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calBanker.add(event.card);
            tv_jet_banker_value.setText("" + event.bankCardValue);
        }
    }

    /*
     * 一局结束，将这局结果放入珠路 04K57-KH8WG-7KADL-HZZAA
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventResult(final ObjectEvent.ResultEvent event) {
        if (event.win != -1) {
            List<String> player = event.playCardList;
            List<String> banker = event.bankCardList;
            final int playScore = BaccaratUtil.getInstance().getBacCardScore(player);
            final int bankScore = BaccaratUtil.getInstance().getBacCardScore(banker);
            final boolean isPlayPair = player.get(0).substring(1).equals(player.get(1).substring(1));
            final boolean isBankPair = banker.get(0).substring(1).equals(banker.get(1).substring(1));
            LogUtil.i("boardMessageList.size()=" + boardMessageList.size() + " event.round=" + event.round);
            if (boardMessageList.size() + 1 == event.round) {
                boardMessageList.add(-1);
                boardMessageList.set(event.round - 1, event.win);//
            }

//            boardMessageList.add(event.win);
            initMessages();
            if (isExpand == 0) {
                Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                        gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 0, Color.RED, Color.BLUE, Color.GREEN, 0, false, 2);//4列 加一局
            } else if (isExpand == 2) {
                Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                        gv_left, gv_right_top, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 0, Color.RED, Color.BLUE, Color.GREEN, 0, false, 2);//11列 加一局
            }
            //1.庄几点 闲几点 一遍 先报庄几点再报闲几点
            //2.庄赢/闲赢 2遍 和局 1遍
            BaccaratUtil.sayBacDot(0, bankScore);
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaccaratUtil.sayBacDot(1, playScore);
                }
            }, 1200);
            if (playScore > bankScore) {
                rl_play_light.setVisibility(View.VISIBLE);
                FlashHelper.getInstance().startFlick(rl_play_light, 4);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaccaratUtil.sayBacDot(1, playScore, isPlayPair, isBankPair);
                    }
                }, 3500);

            } else if (playScore < bankScore) {
                rl_bank_light.setVisibility(View.VISIBLE);
                FlashHelper.getInstance().startFlick(rl_bank_light, 4);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaccaratUtil.sayBacDot(0, bankScore, isPlayPair, isBankPair);
                    }
                }, 3500);

            } else {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaccaratUtil.sayBacDot(2, -1, isPlayPair, isBankPair);//和局
                    }
                }, 3500);
            }
        }
        ask(0, true, false, 0, false);
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
            } else if (event.win == false && Float.parseFloat(event.score) > 0) {
                String _lose = mContext.getResources().getString(R.string.you_lose);
                ToastUtil.getInstance().showToastWin(mContext, _lose + "" + event.score);
            }
        }
        isChipSuccess = 0;//清算完才能退出房间
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, isChipSuccess);
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

    /**
     * 下注成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventChip(final ObjectEvent.ChipEvent event) {

        tv_switch_on.setClickable(false);
        tv_switch_off.setClickable(false);
        isSame = false;
        tv_money.setText(event.availableAmount);
        noBetCount = 0;
        if (event.scoreArr.length > 0) {
            int[] hisScore = event.scoreArr;
            perPlayer = hisScore[0];
            perBanker = hisScore[1];
            perTie = hisScore[2];
            perPlayerPair = hisScore[3];
            perBankerPair = hisScore[4];
            perSmall = hisScore[5];
            perBig = hisScore[6];
        } else {
            perPlayer = perPlayer - playerScore + event.playScore;
            perBanker = perBanker - bankerScore + event.bankScore;
            perTie = perTie - tieScore + event.tieScore;
            perPlayerPair = perPlayerPair - playerPairScore + event.playPairScore;
            perBankerPair = perBankerPair - bankerPairScore + event.bankPairScore;
            perBig = perBig - bigScore + event.bigScore;
            perSmall = perSmall - smallScore + event.smallScore;
        }

        chipScore.put(0, 0);//chipScore 对应 庄 闲 和 庄对 闲对 小 大
        chipScore.put(1, 0);
        chipScore.put(2, 0);
        chipScore.put(3, 0);
        chipScore.put(4, 0);
        chipScore.put(5, 0);
        chipScore.put(6, 0);
        playerPairScore = 0;
        bankerPairScore = 0;
        playerScore = 0;
        tieScore = 0;
        bankerScore = 0;
        bigScore = 0;
        smallScore = 0;
        LogUtil.i("当前下注=3" + currentBetScore.toString() + " perBanker=" + perBanker + " perPlayer=" + perPlayer + " perTie=" + perTie +
                " perBankerPair=" + perBankerPair + " perPlayerPair=" + perPlayerPair + " perSmall=" + perSmall + " perBig=" + perBig + " chipScore=" + chipScore.toString());
        currentBetScore.set(0, perPlayer);//currentBetScore 对应 闲 庄 和 闲对 庄对 小 大
        currentBetScore.set(1, perBanker);
        currentBetScore.set(2, perTie);
        currentBetScore.set(3, perPlayerPair);
        currentBetScore.set(4, perBankerPair);
        currentBetScore.set(5, perSmall);
        currentBetScore.set(6, perBig);

        currentChip2();
        SoundPoolUtil.getInstance().play("3");
        isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
        isChipSuccess = 1;//当前局下注成功 就不能中途退出房间
        noBetCount = 0;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, isChipSuccess);
    }

    /**
     * 超过限红
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventLimitBet(final ObjectEvent.LimitBetEvent event) {
        if (event.limitBetScore > 0) {
            LogUtil.i("下注4_over_limit1_command=chipScore.get(0)庄=" + chipScore.get(0) + " chipScore.get(1)闲=" + chipScore.get(1) + " chipScore.get(2)和=" + chipScore.get(2) +
                    " chipScore.get(3)庄对=" + chipScore.get(3) + " chipScore.get(4)闲对=" + chipScore.get(4) + " free=" + free + " currentBetScore=" + currentBetScore.toString() + " perBanker=" + perBanker + " bankerScore=" + bankerScore);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            currentChip();
            chipScore.put(0, 0);
            chipScore.put(1, 0);
            chipScore.put(2, 0);
            chipScore.put(3, 0);
            chipScore.put(4, 0);
            chipScore.put(5, 0);
            chipScore.put(6, 0);
            isAllRemain = false;
            LogUtil.i("下注4_over_limit2_command=chipScore.get(0)庄=" + chipScore.get(0) + " chipScore.get(1)闲=" + chipScore.get(1) + " chipScore.get(2)和=" + chipScore.get(2) +
                    " chipScore.get(3)庄对=" + chipScore.get(3) + " chipScore.get(4)闲对=" + chipScore.get(4) + " free=" + free + " currentBetScore=" + currentBetScore.toString() + " perBanker=" + perBanker + " bankerScore=" + bankerScore);
        }
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
//                mContext.startActivity(new Intent(mContext, BaccaratListActivity.class));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        BaccaratLiveSocketController.getInstance().disconnectSocket();
//        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_BIG_EYE, 0);
//        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_SMALL, 0);
//        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_COCKROACH, 0);
    }
}