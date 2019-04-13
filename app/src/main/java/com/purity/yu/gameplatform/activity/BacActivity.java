package com.purity.yu.gameplatform.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuangku.languagemodule.CommSharedUtil;
import com.chuangku.languagemodule.LanguageType;
import com.chuangku.languagemodule.MultiLanguageUtil;
import com.daniulive.smartplayer.SmartPlayerJniV2;
import com.eventhandle.NTSmartEventCallbackV2;
import com.eventhandle.NTSmartEventID;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.ChipAdapter2;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.baccarat.YHZGridView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.LocalHtmlWebViewActivity;
import com.purity.yu.gameplatform.controler.BacSocketController;
import com.purity.yu.gameplatform.controler.MacauSocketController;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.Chip;
import com.purity.yu.gameplatform.entity.Liquidation;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.AlgorithmMacau;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.MyYAnimation;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.utils.SoundPoolUtil;
import com.purity.yu.gameplatform.widget.ChipViewHoly;
import com.purity.yu.gameplatform.widget.FlashHelper;
import com.purity.yu.gameplatform.widget.popup.PopupWindow.CommonPopupWindow;
import com.videoengine.NTRenderer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

@ContentView(R.layout.activity_bac)
public class BacActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.rv_chip)
    public RecyclerView rv_chip;
    @BindView(R.id.rl_wrap)
    public RelativeLayout rl_wrap;
    @BindView(R.id.rl_right_blank)
    public RelativeLayout rl_right_blank;
    @BindView(R.id.rl_bottom_blank)
    public RelativeLayout rl_bottom_blank;
    @BindView(R.id.ll_info)
    public LinearLayout ll_info;

    @BindView(R.id.tv_time)
    public TextView tv_time;
    @BindView(R.id.tv_win_value)
    public TextView tv_win_value;


    @BindView(R.id.tv_banker)
    public TextView tv_banker;
    @BindView(R.id.tv_player)
    public TextView tv_player;
    @BindView(R.id.tv_tie)
    public TextView tv_tie;
    @BindView(R.id.tv_player_pair)
    public TextView tv_player_pair;
    @BindView(R.id.tv_banker_pair)
    public TextView tv_banker_pair;
    @BindView(R.id.tv_sure)
    public TextView tv_sure;
    @BindView(R.id.rl_sure)
    public RelativeLayout rl_sure;
    @BindView(R.id.rl_cancel)
    public RelativeLayout rl_cancel;

    @BindView(R.id.sv1)
    public SurfaceView sv1;
    @BindView(R.id.tv_big_sv_l)
    public TextView tv_big_sv_l;
    @BindView(R.id.tv_big_sv_h)
    public TextView tv_big_sv_h;

    //*****************************************发牌******************************************
    @BindView(R.id.rl_poker)
    public RelativeLayout rl_poker;
    @BindView(R.id.tv_player_pair_date)
    TextView tv_player_pair_date;
    @BindView(R.id.tv_banker_pair_date)
    TextView tv_banker_pair_date;
    @BindView(R.id.tv_play_date)
    TextView tv_play_date;
    @BindView(R.id.tv_tie_date)
    TextView tv_tie_date;
    @BindView(R.id.tv_banker_date)
    TextView tv_banker_date;

    @BindView(R.id.iv_blue_border)
    public ImageView iv_blue_border;
    @BindView(R.id.iv_red_border)
    public ImageView iv_red_border;
    @BindView(R.id.iv_player_01_flash)
    public ImageView iv_player_01_flash;
    @BindView(R.id.iv_player_02_flash)
    public ImageView iv_player_02_flash;
    @BindView(R.id.iv_banker_01_flash)
    public ImageView iv_banker_01_flash;
    @BindView(R.id.iv_banker_02_flash)
    public ImageView iv_banker_02_flash;
    @BindView(R.id.iv_player_01)
    public ImageView iv_player_01;
    @BindView(R.id.iv_player_02)
    public ImageView iv_player_02;
    @BindView(R.id.iv_player_03)
    public ImageView iv_player_03;
    @BindView(R.id.iv_banker_01)
    public ImageView iv_banker_01;
    @BindView(R.id.iv_banker_02)
    public ImageView iv_banker_02;
    @BindView(R.id.iv_banker_03)
    public ImageView iv_banker_03;
    @BindView(R.id.tv_fill_banker)
    public TextView tv_fill_banker;
    @BindView(R.id.tv_fill_player)
    public TextView tv_fill_player;
    @BindView(R.id.tv_jet_player_value)
    TextView tv_jet_player_value;
    @BindView(R.id.tv_jet_banker_value)
    TextView tv_jet_banker_value;
    @BindView(R.id.rl_player_pair)
    public RelativeLayout rl_player_pair;
    @BindView(R.id.rl_banker_pair)
    public RelativeLayout rl_banker_pair;
    @BindView(R.id.rl_player)
    public RelativeLayout rl_player;
    @BindView(R.id.rl_tie)
    public RelativeLayout rl_tie;
    @BindView(R.id.rl_banker)
    public RelativeLayout rl_banker;

    //**************************************************三路*****************************************************
    @BindView(R.id.rl_top)
    public RelativeLayout rl_top;
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
    @BindView(R.id.rl_player_pair_chip)
    RelativeLayout rl_player_pair_chip;
    @BindView(R.id.cv_player_pair_chip)
    ChipViewHoly cv_player_pair_chip;
    @BindView(R.id.tv_player_pair_integer)
    TextView tv_player_pair_integer;
    @BindView(R.id.rl_banker_pair_chip)
    RelativeLayout rl_banker_pair_chip;
    @BindView(R.id.cv_banker_pair_chip)
    ChipViewHoly cv_banker_pair_chip;
    @BindView(R.id.tv_banker_pair_integer)
    TextView tv_banker_pair_integer;
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
    //三路统计
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
    @BindView(R.id.tv_in_game_gold_value)
    public TextView tv_in_game_gold_value;
    //问路
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
    //**************************************************房间基本信息*****************************************************

    @BindView(R.id.rl_banker_dot_layout)
    public RelativeLayout rl_banker_dot_layout;
    @BindView(R.id.rl_player_dot_layout)
    public RelativeLayout rl_player_dot_layout;
    @BindView(R.id.rl_sv)
    public RelativeLayout rl_sv;
    @BindView(R.id.tv_room_name)
    public TextView tv_room_name;
    @BindView(R.id.tv_nickname)
    public TextView tv_nickname;
    @BindView(R.id.tv_money)
    public TextView tv_money;
    @BindView(R.id.tv_bet_all_score)
    public TextView tv_bet_all_score;
    @BindView(R.id.tv_win)
    public TextView tv_win;
    @BindView(R.id.tv_online_count)
    TextView tv_online_count;
    @BindView(R.id.tv_switch_on)
    public TextView tv_switch_on;
    @BindView(R.id.tv_bank_odds)
    public TextView tv_bank_odds;

    //**************************************************菜单点击*****************************************************
    @BindView(R.id.rl_video_select2)
    RelativeLayout rl_video_select2;
    @BindView(R.id.iv_video_select2)
    ImageView iv_video_select2;
    @BindView(R.id.tv_table_limits)
    TextView tv_table_limits;
    @BindView(R.id.tv_menu)
    TextView tv_menu;
    @BindView(R.id.tv_return)
    TextView tv_return;
    @BindView(R.id.tv_show)
    TextView tv_show;
    @BindView(R.id.rl_show)
    RelativeLayout rl_show;

    private Context mContext;
    private DisplayMetrics displayMetrics;
    private float scaleX;
    private float scaleY;
    private List<Chip> chipList;
    private ChipAdapter2 mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int selectChipNumber;
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
    private int playerIng = 0, bankerIng, tieIng, playerPairIng = 0, bankerPairIng = 0, bigIng = 0, smallIng = 0;//实时下注积分
    private int playerScore = 0, bankerScore = 0, tieScore = 0, playerPairScore = 0, bankerPairScore = 0, bigScore = 0, smallScore = 0;//当前局每个位置下注积分
    private int perPlayer = 0, perBanker = 0, perTie = 0, perPlayerPair = 0, perBankerPair = 0, perBig = 0, perSmall = 0;//每局每个位置下注总积分
    private int free = 0;//默认庄非免佣1:0.95 1免佣
    private boolean isChip = false;//默认未保存下注记录
    private int isChipSuccess = 0;//默认0下注不成功 1下注成功
    private boolean isSame = false;//默认不续押
    private boolean isAllRemain = false;//默认未全押
    private boolean isFirstJoinRoom = true;
    private int isPeopleNum = -1;
    private int betsRemind = -1;
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
    private int skyCard;
    private float topScaleY = 1.1F;
    private boolean isFirstPlay = true, isFirstBank = true, isFirstTie = true, isFirstPlayPair = true, isFirstBankPair = true;
    private int betSecond;
    private CommonPopupWindow popupWindow;
    private boolean isRobot = false;
    private boolean isSwitchRobot = false;
    private String state = Constant.BACCARAT_INNINGS_END;//如果没有连接就是洗牌
    private Timer timer;
    private int second = 25;

    //测试
//    static {
//        System.loadLibrary("SmartPlayer");//大牛直播
//    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //todo https://zhuanlan.zhihu.com/p/33128370 立体筹码
        //进入房间再次获取视频流
        mContext = this;
        timer = new Timer();
        displayMetrics = mContext.getResources().getDisplayMetrics();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        roomName = getIntent().getStringExtra("roomName");
        roomId = getIntent().getStringExtra("roomId");
        token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN_SOCKET);
        betSecond = getIntent().getIntExtra("betSecond", 25);
        tv_room_name.bringToFront();
        tv_room_name.setText(roomName);
        tv_nickname.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, 0);//进入房间未下注状态
        Util.getInstance().hideSystemNavigationBar();
        BacSocketController.getInstance().init(this);
        BacSocketController.getInstance().copyWidget(boardMessageList, tv_time, rl_poker, iv_banker_01, iv_banker_02, iv_banker_03,
                tv_jet_banker_value, iv_player_01, iv_player_02, iv_player_03, tv_jet_player_value, roomId,
                rl_player_pair, rl_banker_pair, rl_player, rl_tie, rl_banker, tv_online_count, rl_show, tv_show);
        BacSocketController.getInstance().connectSocket();
        EventBus.getDefault().register(this);
        baccarat = (Baccarat) getIntent().getSerializableExtra("baccarat");
        init(mContext);
        copyWidget(sv1, tv_big_sv_l, tv_big_sv_h, baccarat);
        initEvent();
        initDate();
        initAdapter();
        initDisplay();
        initTypeFace();
        tv_nickname.setTypeface(null);
        currentBetScore.clear();
        currentBetScore.add(0);//闲
        currentBetScore.add(0);//庄
        currentBetScore.add(0);//和
        currentBetScore.add(0);//闲对
        currentBetScore.add(0);//庄对
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
        tv_online_count.setText(String.valueOf(event.peoplenumber));
        tv_in_game_gold_value.setText(String.valueOf(skyCard));
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
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
        if (!event.state.equals(Constant.BACCARAT_BET)) {
            onClick(false);
            //测试用例 暂时下注
            rl_poker.setVisibility(View.GONE);
        } else {
            currentMoney = Double.parseDouble(tv_money.getText().toString());
        }
        if (event.state.equals(Constant.BACCARAT_BET)) {
            second = event.downtownTime;
            perOnePerformance();
            rl_poker.setVisibility(View.GONE);
        }
        if (event.state.equals(Constant.BACCARAT_RESULT)) {
            tv_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.unit13));
            tv_time.setText("发牌中");
            isRobot = true;//只有发牌阶段才能换托管状态

        } else if (event.state.equals(Constant.BACCARAT_WAIT)) {
            tv_time.setText("等待");
        } else if (event.state.equals(Constant.BACCARAT_INNINGS_END)) {
            tv_time.setText("洗牌中");
        }
        if (state.equals(Constant.BACCARAT_RESULT)) {
            zoomQuarterVideo();
        } else {
            zoomReturnVideo();
        }
        if (event.boardMessageList.size() > 0) {
            boardMessageList.clear();
            boardMessageList.addAll(event.boardMessageList);
//            maxScoreListAll.clear();
//            maxScoreListAll.addAll(event.maxScoreList);

            initMessages();
            AlgorithmMacau.getInstance().drawRoad(maxScoreList, beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                    gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 0,
                    Color.parseColor("#a40001"), Color.parseColor("#004A86"), Color.parseColor("#0D7D25"));//11列 加一局
            ask();
        }
        String _1 = "0";
        String _2 = "0";
        String _3 = "0";
        String _4 = "0";
        String _5 = "0";
        isPeopleNum = 0;//强制置为0
        if (isPeopleNum == 0) {
            _1 = String.valueOf(playerIng);
            _2 = String.valueOf(bankerIng);
            _3 = String.valueOf(tieIng);
            _4 = String.valueOf(playerPairIng);
            _5 = String.valueOf(bankerPairIng);
        } else if (isPeopleNum == 1) {
            _1 = String.valueOf(playerIng) + "/" + 0;
            _2 = String.valueOf(bankerIng) + "/" + 0;
            _3 = String.valueOf(tieIng) + "/" + 0;
            _4 = String.valueOf(playerPairIng) + "/" + 0;
            _5 = String.valueOf(bankerPairIng) + "/" + 0;
        }
        tv_play_date.setText(_1);
        tv_banker_date.setText(_2);
        tv_tie_date.setText(_3);
        tv_player_pair_date.setText(_4);
        tv_banker_pair_date.setText(_5);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initSound();//在低端机上 加载assets资源，超级耗时
            }
        }, 200);
//        MacauSocketController.getInstance().connectSocketProdict();
//        mSocketProdict = MacauSocketController.getInstance().getSocketProdict();
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
            bigIng = event.scoreList.get(5);
            smallIng = event.scoreList.get(6);
            String _1 = "0";
            String _2 = "0";
            String _3 = "0";
            String _4 = "0";
            String _5 = "0";
            String _6 = "0";
            String _7 = "0";
            isPeopleNum = 0;//强制置为0
            if (isPeopleNum == 0) {
                _1 = String.valueOf(playerIng);
                _2 = String.valueOf(bankerIng);
                _3 = String.valueOf(tieIng);
                _4 = String.valueOf(playerPairIng);
                _5 = String.valueOf(bankerPairIng);
                _6 = String.valueOf(bigIng);
                _7 = String.valueOf(smallIng);
            } else if (isPeopleNum == 1) {
                _1 = String.valueOf(playerIng) + "/" + event.betPeopleList.get(0);
                _2 = String.valueOf(bankerIng) + "/" + event.betPeopleList.get(1);
                _3 = String.valueOf(tieIng) + "/" + String.valueOf(event.betPeopleList.get(2));
                _4 = String.valueOf(playerPairIng) + "/" + String.valueOf(event.betPeopleList.get(3));
                _5 = String.valueOf(bankerPairIng) + "/" + String.valueOf(event.betPeopleList.get(4));
                _6 = String.valueOf(bigIng) + "/" + String.valueOf(event.betPeopleList.get(5));
                _7 = String.valueOf(smallIng) + "/" + String.valueOf(event.betPeopleList.get(6));
            }
            tv_play_date.setText(_1);
            tv_banker_date.setText(_2);
            tv_tie_date.setText(_3);
            tv_player_pair_date.setText(_4);
            tv_banker_pair_date.setText(_5);
        }
    }

    /*
     * 当前下注金额(牌局的下注积分数 0闲 1庄 2和 3闲对 4庄对)
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBetScore(final ObjectEvent.betScoreEvent event) {
        currentBetScore.clear();
        currentBetScore.addAll(event.scoreList);
        int allScore = currentBetScore.get(0) + currentBetScore.get(1) + currentBetScore.get(2) + currentBetScore.get(3) + currentBetScore.get(4);
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
        isFirstJoinRoom = false;
        state = event.state;
        if (event.state.equals("BET")) {
            onClick(true);
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
            rl_banker_pair_chip.setVisibility(View.GONE);
            rl_tie_chip.setVisibility(View.GONE);
            rl_player_chip.setVisibility(View.GONE);
            rl_banker_chip.setVisibility(View.GONE);
            if (perPlayerPair != 0 || perBankerPair != 0 || perPlayer != 0 || perTie != 0 || perBanker != 0) {
                isChip = true;
                isChipClick(false, true, R.drawable.button_bet_disable_bg, R.drawable.button_bet_normal_bg, R.string.repeat);
            } else {
                isChip = false;
                isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            }
            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_start));
            isChipSuccess = 0;//新一局 代表未下注成功
            SoundPoolUtil.getInstance().play("2");//已开局，请下注
            tv_fill_player.setVisibility(View.GONE);
            tv_fill_banker.setVisibility(View.GONE);
            rl_poker.setVisibility(View.GONE);
//            isShowChip(View.VISIBLE);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);

            second = betSecond;
            perOnePerformance();
            zoomReturnVideo();

            int allScore = currentBetScore.get(0) + currentBetScore.get(1) + currentBetScore.get(2) + currentBetScore.get(3) + currentBetScore.get(4);
            tv_bet_all_score.setText(String.valueOf(allScore));
            tv_win.setText("0");
            tv_win_value.setText("");
            isFirstPlay = true;
            isFirstBank = true;
            isFirstTie = true;
            isFirstPlayPair = true;
            isFirstBankPair = true;
            if (mSocketProdict != null && mSocketProdict.connected()) {
                MacauSocketController.getInstance().connectSocketProdict();
                mSocketProdict = MacauSocketController.getInstance().getSocketProdict();
            }
            isRobot = false;
            int[] postDelayed = {3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000, 15000, 16000, 17000, 18000, 19000, 20000, 21000, 22000, 23000, 24000, 25000};
            int randomPostDelayed = new Random().nextInt(20);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    betRobot();
                }
            }, postDelayed[randomPostDelayed]);

            startLightById(0, 0, 0, 0, 0);//有问题 导致后面的没运行
        } else if (event.state.equals("RESULT")) {
            onClick(false);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            rl_poker.setVisibility(View.VISIBLE);
            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_stop));
            SoundPoolUtil.getInstance().play("4");//停止下注
//            isShowChip(View.GONE);
            rl_player_dot_layout.setVisibility(View.VISIBLE);
            rl_banker_dot_layout.setVisibility(View.VISIBLE);
            //清空处理
            tv_jet_player_value.setText("0");
            tv_jet_banker_value.setText("0");
            iv_player_01.setBackgroundResource(0);
            iv_player_02.setBackgroundResource(0);
            iv_player_03.setBackgroundResource(0);
            iv_banker_01.setBackgroundResource(0);
            iv_banker_02.setBackgroundResource(0);
            iv_banker_03.setBackgroundResource(0);
            _calPlayer.clear();
            _calBanker.clear();
//            if (betsRemind == 1) {
//                outGame();
//            }
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_TIE, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY_PAIR, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK_PAIR, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
            tv_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.unit13));
            tv_time.setText("发牌中");
            zoomQuarterVideo();
            timer.cancel();
            second = 0;
            isRobot = true;
            iv_player_01_flash.setVisibility(View.VISIBLE);
            FlashHelper.getInstance().startFlick(iv_player_01_flash, Animation.INFINITE);
        } else if (event.state.equals("OVER")) {
            onClick(false);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
        } else if (event.state.equals("WAITTING")) {
            onClick(false);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
        } else if (event.state.equals(Constant.BACCARAT_INNINGS_END)) {//当前轮结束
            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_start));
            rl_show.setBackground(mContext.getResources().getDrawable(R.drawable.toast));
            tv_show.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.unit16));
            tv_show.setText("当前轮已结束，请等待下一局！");
            rl_poker.setVisibility(View.GONE);
            cleanChip();
            time = 1;
            tv_play_date.setText("0");
            tv_banker_date.setText("0");
            tv_tie_date.setText("0");
            tv_player_pair_date.setText("0");
            tv_banker_pair_date.setText("0");
            SharedPreUtil.getInstance(mContext).saveParam(Constant.BACCARAT_STATE, event.state);
            startLightById(0, 0, 0, 0, 0);
            tv_time.setText("洗牌中");
            tv_win.setText("0");
            tv_bet_all_score.setText("");
            tv_win_value.setText("");
        } else if (event.state.equals(Constant.BACCARAT_CREATED)) {//新的一轮开始
            boardMessageList.clear();
            maxScoreListAll.clear();
            maxScoreList.clear();
            tv_in_game_gold_value.setText("0");
            initMessages();
            AlgorithmMacau.getInstance().drawRoad(maxScoreList, beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                    gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 0,
                    Color.parseColor("#a40001"), Color.parseColor("#004A86"), Color.parseColor("#0D7D25"));//11列 加一局
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

    /*
     * 补牌 //01 庄补牌  02闲补牌
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventFillCard(final ObjectEvent.FillCardEvent event) {
        if (!TextUtils.isEmpty(event.fill)) {
            if (event.fill.equals("02")) {
                SoundPoolUtil.getInstance().play("94");//庄补
                tv_fill_banker.setVisibility(View.VISIBLE);
                tv_fill_banker.setText(mContext.getResources().getString(R.string.fill_card));
                iv_blue_border.setVisibility(View.INVISIBLE);
                FlashHelper.getInstance().stopFlick(iv_blue_border);
                iv_red_border.setVisibility(View.VISIBLE);
                FlashHelper.getInstance().startFlick(iv_red_border, Animation.INFINITE);
            } else if (event.fill.equals("01")) {
                SoundPoolUtil.getInstance().play("95");//闲补
                tv_fill_player.setVisibility(View.VISIBLE);
                tv_fill_player.setText(mContext.getResources().getString(R.string.fill_card));
                iv_blue_border.setVisibility(View.VISIBLE);
                FlashHelper.getInstance().startFlick(iv_blue_border, Animation.INFINITE);
            }
        }
    }

    /*
     * 发牌 黑桃 红桃 草花 方块 1,2,3,4 10-a j-b q-c k-d
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventSwitchResult(final ObjectEvent.SwitchResultEvent event) {
        rl_poker.setVisibility(View.VISIBLE);

        SoundPoolUtil.getInstance().play("96");//发牌音效
        if (event.pos.equals("01")) {//第一张 闲
            _calPlayer.clear();
            _calBanker.clear();
            iv_player_01.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calPlayer.add(event.card);
            tv_jet_player_value.setText("" + event.playCardValue);
            iv_player_01_flash.setVisibility(View.GONE);
            FlashHelper.getInstance().stopFlick(iv_player_01_flash);
            iv_banker_01_flash.setVisibility(View.VISIBLE);
            FlashHelper.getInstance().startFlick(iv_banker_01_flash, Animation.INFINITE);
        } else if (event.pos.equals("03")) {//第三张 闲
            iv_player_02.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calPlayer.add(event.card);
            tv_jet_player_value.setText("" + event.playCardValue);
            iv_player_02_flash.setVisibility(View.GONE);
            FlashHelper.getInstance().stopFlick(iv_player_02_flash);
            iv_banker_02_flash.setVisibility(View.VISIBLE);
            FlashHelper.getInstance().startFlick(iv_banker_02_flash, Animation.INFINITE);
            if (event.playCardValue == 8 || event.playCardValue == 9) {
                tv_fill_player.setVisibility(View.VISIBLE);
                tv_fill_player.setText(R.string.sky_card);
            }
        } else if (event.pos.equals("05")) {//第五张 闲
            iv_player_03.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calPlayer.add(event.card);
            tv_jet_player_value.setText("" + event.playCardValue);
            iv_blue_border.setVisibility(View.INVISIBLE);
            FlashHelper.getInstance().stopFlick(iv_blue_border);
        } else if (event.pos.equals("02")) {//第二张 庄
            iv_banker_01.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calBanker.add(event.card);
            tv_jet_banker_value.setText("" + event.bankCardValue);
            iv_banker_01_flash.setVisibility(View.GONE);
            FlashHelper.getInstance().stopFlick(iv_banker_01_flash);
            iv_player_02_flash.setVisibility(View.VISIBLE);
            FlashHelper.getInstance().startFlick(iv_player_02_flash, Animation.INFINITE);
        } else if (event.pos.equals("04")) {//第四张 庄
            iv_banker_02.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calBanker.add(event.card);
            tv_jet_banker_value.setText("" + event.bankCardValue);
            iv_banker_02_flash.setVisibility(View.GONE);
            FlashHelper.getInstance().stopFlick(iv_banker_02_flash);
            if (event.bankCardValue == 8 || event.bankCardValue == 9) {
                tv_fill_banker.setVisibility(View.VISIBLE);
                tv_fill_banker.setText(R.string.sky_card);
            }
        } else if (event.pos.equals("06")) {//第六张 庄
            iv_banker_03.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calBanker.add(event.card);
            tv_jet_banker_value.setText("" + event.bankCardValue);
            iv_red_border.setVisibility(View.INVISIBLE);
            FlashHelper.getInstance().stopFlick(iv_red_border);
        }
    }

    /*
     * 一局结束，将这局结果放入珠路 04K57-KH8WG-7KADL-HZZAA
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventResult(final ObjectEvent.ResultEvent event) {
        skyCard = event.skyCard;
        tv_in_game_gold_value.setText(String.valueOf(skyCard));
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
//                maxScoreListAll.add(-1);
//                maxScoreListAll.set(event.round - 1, event.resultMaxScore);
            }

            initMessages();
            LogUtil.i("maxScoreList=" + maxScoreList.toString());
            AlgorithmMacau.getInstance().drawRoad(maxScoreList, beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                    gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 0,
                    Color.parseColor("#a40001"), Color.parseColor("#004A86"), Color.parseColor("#0D7D25"));//11列 加一局
            ask();//庄反闲
            //发光

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
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaccaratUtil.sayBacDot(1, playScore, isPlayPair, isBankPair);
                    }
                }, 3500);

            } else if (playScore < bankScore) {
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
            //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
            if (event.win == 0) {
                tv_win_value.setText("庄赢");
                startLightById(0, 1, 0, 0, 0);
            } else if (event.win == 1) {
                tv_win_value.setText("闲赢");
                startLightById(1, 0, 0, 0, 0);
            } else if (event.win == 2) {
                tv_win_value.setText("和局");
                startLightById(0, 0, 0, 0, 1);
            } else if (event.win == 3) {
                tv_win_value.setText("庄赢庄对");
                startLightById(0, 1, 0, 1, 0);
            } else if (event.win == 4) {
                tv_win_value.setText("庄赢闲对");
                startLightById(0, 1, 1, 0, 0);
            } else if (event.win == 5) {
                tv_win_value.setText("庄赢庄对闲对");
                startLightById(0, 1, 1, 1, 0);
            } else if (event.win == 6) {
                tv_win_value.setText("闲赢庄对");
                startLightById(1, 0, 0, 1, 0);
            } else if (event.win == 7) {
                tv_win_value.setText("闲赢闲对");
                startLightById(1, 0, 1, 0, 0);
            } else if (event.win == 8) {
                tv_win_value.setText("闲赢庄对闲对");
                startLightById(1, 0, 1, 1, 0);
            } else if (event.win == 9) {
                tv_win_value.setText("和赢庄对");
                startLightById(0, 0, 0, 1, 1);
            } else if (event.win == 10) {
                tv_win_value.setText("和赢闲对");
                startLightById(0, 0, 1, 0, 1);
            } else if (event.win == 11) {
                tv_win_value.setText("和赢庄对闲对");
                startLightById(0, 0, 1, 1, 1);
            }
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
                BaccaratUtil.getInstance().win(rl_show, tv_show, 4000, _win + "" + event.score,
                        mContext.getResources().getDrawable(R.drawable.result_popup_gradient),
                        mContext.getResources().getDimension(R.dimen.unit16), Color.RED);
                SoundPoolUtil.getInstance().play("win");
                tv_win.setText("" + event.score);
            } else if (event.win == false && Float.parseFloat(event.score) > 0) {
                String _lose = mContext.getResources().getString(R.string.you_lose);
                BaccaratUtil.getInstance().win(rl_show, tv_show, 4000, _lose + "" + event.score,
                        mContext.getResources().getDrawable(R.drawable.result_popup_gradient),
                        mContext.getResources().getDimension(R.dimen.unit16), Color.RED);
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
        tv_switch_on.setClickable(false);
//        tv_switch_off.setClickable(false);
        isSame = false;
        tv_money.setText(event.availableAmount);
        if (event.scoreArr.length > 0) {
            int[] hisScore = event.scoreArr;
            perPlayer = hisScore[0];
            perBanker = hisScore[1];
            perTie = hisScore[2];
            perPlayerPair = hisScore[3];
            perBankerPair = hisScore[4];
        } else {
            perPlayer = perPlayer - playerScore + event.playScore;
            perBanker = perBanker - bankerScore + event.bankScore;
            perTie = perTie - tieScore + event.tieScore;
            perPlayerPair = perPlayerPair - playerPairScore + event.playPairScore;
            perBankerPair = perBankerPair - bankerPairScore + event.bankPairScore;
        }
        chipScore.put(0, 0);
        chipScore.put(1, 0);
        chipScore.put(2, 0);
        chipScore.put(3, 0);
        chipScore.put(4, 0);
//        chipScore.put(5, 0);
//        chipScore.put(6, 0);
        playerPairScore = 0;
        bankerPairScore = 0;
        playerScore = 0;
        tieScore = 0;
        bankerScore = 0;
        bigScore = 0;
        smallScore = 0;
        currentBetScore.set(0, perPlayer);
        currentBetScore.set(1, perBanker);
        currentBetScore.set(2, perTie);
        currentBetScore.set(3, perPlayerPair);
        currentBetScore.set(4, perBankerPair);
//        currentBetScore.set(5, perBig);
//        currentBetScore.set(6, perSmall);
        int allScore = currentBetScore.get(0) + currentBetScore.get(1) + currentBetScore.get(2) + currentBetScore.get(3) + currentBetScore.get(4);
        tv_bet_all_score.setText(String.valueOf(allScore));
//        LogUtil.i("下注成功的回显 socket_command=" + currentBetScore.get(0) + " currentBetScore.get(1)=" + currentBetScore.get(1) +
//                " currentBetScore.get(2)=" + currentBetScore.get(2) + " currentBetScore.get(3)=" + currentBetScore.get(3) + " currentBetScore.get(4)=" + currentBetScore.get(4)+
//                ""+);
        currentChip2();
        SoundPoolUtil.getInstance().play("3");
        isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
        isChipSuccess = 1;//当前局下注成功 就不能中途退出房间
        noBetCount = 0;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, isChipSuccess);
//        tv_robot.setClickable(false);//每局只能托管下注一次,当手动下注，也不能托管了
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
            chipScore.put(3, 0);
            chipScore.put(4, 0);
//            chipScore.put(5, 0);
//            chipScore.put(6, 0);
            isAllRemain = false;
            int allScore = currentBetScore.get(0) + currentBetScore.get(1) + currentBetScore.get(2) + currentBetScore.get(3) + currentBetScore.get(4);
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
            ProtocolUtil.getInstance().hintDialog(mContext, "上分：" + event.upperScore + " 成功");
        } else if (!TextUtils.isEmpty(event.type) && event.type.equals("1")) {
            ProtocolUtil.getInstance().hintDialog(mContext, "下分：" + event.upperScore + " 成功");
        }
    }

    private void postEventMoney(String money) {
        ObjectEvent.MoneyChangeEvent moneyChangeEvent = new ObjectEvent.MoneyChangeEvent();
        moneyChangeEvent.money = money;
        EventBus.getDefault().post(moneyChangeEvent);
    }

    /**
     * 根据赢的位置闪
     *
     * @param iv_play
     * @param iv_bank
     * @param iv_play_pair
     * @param iv_bank_pair
     * @param iv_tie
     */
    private void startLightById(int iv_play, int iv_bank, int iv_play_pair, int iv_bank_pair, int iv_tie) {
        if (iv_play == 1) {//1播放 0不播放
            rl_player.setBackground(mContext.getResources().getDrawable(R.drawable.anim_bet));
            ((AnimationDrawable) rl_player.getBackground()).start();
        } else {
            rl_player.setBackground(mContext.getResources().getDrawable(R.drawable.select_bet));
        }
        if (iv_bank == 1) {
            rl_banker.setBackground(mContext.getResources().getDrawable(R.drawable.anim_bet));
            ((AnimationDrawable) rl_banker.getBackground()).start();
        } else {
            rl_banker.setBackground(mContext.getResources().getDrawable(R.drawable.select_bet));
        }
        if (iv_play_pair == 1) {
            rl_player_pair.setBackground(mContext.getResources().getDrawable(R.drawable.anim_bet));
            ((AnimationDrawable) rl_player_pair.getBackground()).start();
        } else {
            rl_player_pair.setBackground(mContext.getResources().getDrawable(R.drawable.select_bet));
        }
        if (iv_bank_pair == 1) {
            rl_banker_pair.setBackground(mContext.getResources().getDrawable(R.drawable.anim_bet));
            ((AnimationDrawable) rl_banker_pair.getBackground()).start();
        } else {
            rl_banker_pair.setBackground(mContext.getResources().getDrawable(R.drawable.select_bet));
        }
        if (iv_tie == 1) {
            rl_tie.setBackground(mContext.getResources().getDrawable(R.drawable.anim_bet));
            ((AnimationDrawable) rl_tie.getBackground()).start();
        } else {
            rl_tie.setBackground(mContext.getResources().getDrawable(R.drawable.select_bet));
        }
    }

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
                ProtocolUtil.getInstance().hintDialog(mContext, mContext.getResources().getString(R.string.no_bet_warning));
            } else if (noBetCount == 5) {
                this.finish();
            }
        }
    }

    /**
     * 1.currentBetScore 成功下注的分数
     * 2.currentBetScore赋值给per 闲 庄 和 闲对 庄对
     * 3.chipScore清空
     */
    private void currentChip2() {
        if (currentBetScore.get(0) == 0) {
            rl_player_chip.setVisibility(View.GONE);
            cv_player_chip.addChip(0);
            perPlayer = currentBetScore.get(0);
            playerScore = currentBetScore.get(0);
            chipScore.put(1, 0);
        } else {
            rl_player_chip.setVisibility(View.VISIBLE);
            perPlayer = currentBetScore.get(0);
            cv_player_chip.addChip(0);
            chipScore.put(1, 0);
            tv_player_integer.setText("" + currentBetScore.get(0));
            List<Integer> _playerChip = new ArrayList<>();
            _playerChip.addAll(BaccaratUtil.addChip(currentBetScore.get(0)));
            for (Integer integer : _playerChip) {
                cv_player_chip.addChip(integer);
            }
            if (cv_player_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_chip.getLayoutParams();
                if (cv_player_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                } else if (cv_player_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                } else if (cv_player_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                } else if (cv_player_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                } else if (cv_player_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                } else if (cv_player_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                } else if (cv_player_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
            chipScore.put(0, 0);
        } else {
            rl_banker_chip.setVisibility(View.VISIBLE);
            perBanker = currentBetScore.get(1);
            cv_banker_chip.addChip(0);
            chipScore.put(0, 0);
            tv_banker_integer.setText("" + currentBetScore.get(1));
            List<Integer> _bankerChip = new ArrayList<>();
            _bankerChip.addAll(BaccaratUtil.addChip(currentBetScore.get(1)));
            for (Integer integer : _bankerChip) {
                cv_banker_chip.addChip(integer);
            }
            if (cv_banker_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_chip.getLayoutParams();
                if (cv_banker_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                } else if (cv_banker_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                } else if (cv_banker_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                } else if (cv_banker_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                } else if (cv_banker_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                } else if (cv_banker_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                } else if (cv_banker_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
            chipScore.put(2, 0);
        } else {
            rl_tie_chip.setVisibility(View.VISIBLE);
            perTie = currentBetScore.get(2);
            cv_tie_chip.addChip(0);
            chipScore.put(2, 0);
            tv_tie_integer.setText("" + currentBetScore.get(2));
            List<Integer> _tieChip = new ArrayList<>();
            _tieChip.addAll(BaccaratUtil.addChip(currentBetScore.get(2)));
            for (Integer integer : _tieChip) {
                cv_tie_chip.addChip(integer);
            }
            if (cv_tie_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_tie_chip.getLayoutParams();
                if (cv_tie_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                } else if (cv_tie_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                } else if (cv_tie_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                } else if (cv_tie_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                } else if (cv_tie_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                } else if (cv_tie_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                } else if (cv_tie_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
        } else {
            rl_player_pair_chip.setVisibility(View.VISIBLE);
            perPlayerPair = currentBetScore.get(3);
            cv_player_pair_chip.addChip(0);
            chipScore.put(4, 0);
            tv_player_pair_integer.setText("" + currentBetScore.get(3));
            List<Integer> _playerPairChip = new ArrayList<>();
            _playerPairChip.addAll(BaccaratUtil.addChip(currentBetScore.get(3)));
            for (Integer integer : _playerPairChip) {
                cv_player_pair_chip.addChip(integer);
            }
            if (cv_player_pair_chip.getChipNumSize() < 15) {//筹码堆叠1
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_pair_chip.getLayoutParams();
                if (cv_player_pair_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                } else if (cv_player_pair_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                } else if (cv_player_pair_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                } else if (cv_player_pair_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                } else if (cv_player_pair_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                } else if (cv_player_pair_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                } else if (cv_player_pair_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
        } else {
            rl_banker_pair_chip.setVisibility(View.VISIBLE);
            perBankerPair = currentBetScore.get(4);
            cv_banker_pair_chip.addChip(0);
            chipScore.put(3, 0);
            tv_banker_pair_integer.setText("" + currentBetScore.get(4));
            List<Integer> _bankerPairChip = new ArrayList<>();
            _bankerPairChip.addAll(BaccaratUtil.addChip(currentBetScore.get(4)));
            for (Integer integer : _bankerPairChip) {
                cv_banker_pair_chip.addChip(integer);
            }
            if (cv_banker_pair_chip.getChipNumSize() < 15) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_pair_chip.getLayoutParams();
                if (cv_banker_pair_chip.getChipNumSize() < 2) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                } else if (cv_banker_pair_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                } else if (cv_banker_pair_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                } else if (cv_banker_pair_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                } else if (cv_banker_pair_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                } else if (cv_banker_pair_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                } else if (cv_banker_pair_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_banker_pair_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_pair_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_banker_pair_integer.setLayoutParams(tvParams);
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
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                } else if (cv_player_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                } else if (cv_player_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                } else if (cv_player_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                } else if (cv_player_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                } else if (cv_player_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                } else if (cv_player_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                } else if (cv_banker_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                } else if (cv_banker_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                } else if (cv_banker_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                } else if (cv_banker_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                } else if (cv_banker_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                } else if (cv_banker_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                } else if (cv_tie_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                } else if (cv_tie_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                } else if (cv_tie_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                } else if (cv_tie_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                } else if (cv_tie_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                } else if (cv_tie_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                } else if (cv_player_pair_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                } else if (cv_player_pair_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                } else if (cv_player_pair_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                } else if (cv_player_pair_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                } else if (cv_player_pair_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                } else if (cv_player_pair_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                } else if (cv_banker_pair_chip.getChipNumSize() < 4) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                } else if (cv_banker_pair_chip.getChipNumSize() < 6) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                } else if (cv_banker_pair_chip.getChipNumSize() < 8) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                } else if (cv_banker_pair_chip.getChipNumSize() < 10) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                } else if (cv_banker_pair_chip.getChipNumSize() < 12) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                } else if (cv_banker_pair_chip.getChipNumSize() < 14) {
                    layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
                }
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                rl_banker_pair_chip.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_pair_integer.getLayoutParams();
                tvParams.addRule(RelativeLayout.ABOVE);
                tv_banker_pair_integer.setLayoutParams(tvParams);
            }
        }


    }

    private void onClick(boolean clickable) {
        rl_player_pair.setClickable(clickable);
        rl_banker_pair.setClickable(clickable);
        rl_player.setClickable(clickable);
        rl_tie.setClickable(clickable);
        rl_banker.setClickable(clickable);
        rl_sure.setClickable(clickable);
        tv_switch_on.setClickable(clickable);
    }

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
                                tv_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.unit13));
                                tv_time.setText("发牌中");
                            } else {
                                tv_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.unit18));
                                tv_time.setText(String.valueOf(second));
                                if (second < 6 && second > 0) {
                                    SoundPoolUtil.getInstance().play("time");//倒计时5开始滴答声
                                }
                            }
                        } else {
                            tv_time.setText("0");
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }

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
        int total = boardMessageList.size(), banker = 0, player = 0, tie = 0, bankerPair = 0, playerPair = 0, gold = 0;

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

//        tv_in_game_total_value.setText(String.valueOf(total));
        tv_in_game_banker_value.setText(String.valueOf(banker));
        tv_in_game_player_value.setText(String.valueOf(player));
        tv_in_game_tie_value.setText(String.valueOf(tie));
        tv_in_game_banker_pair_value.setText(String.valueOf(bankerPair));
        tv_in_game_player_pair_value.setText(String.valueOf(playerPair));

        AlgorithmMacau.getInstance().initBigRoad(boardMessageList, beadRoadList, beadRoadListShort,
                bigRoadListAll, bigRoadList, bigRoadListShort,
                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
                smallRoadListAll, smallRoadList, smallRoadListShort,
                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort,
                maxScoreListAll, maxScoreList, mContext, 2, false,
                iv_ask_bank_1, iv_ask_bank_2, iv_ask_bank_3, iv_ask_play_1, iv_ask_play_2, iv_ask_play_3,
                0, 0, 0);//1路单 2视频
    }

    private void ask() {
        boardMessageList1.clear();
        boardMessageList1.addAll(boardMessageList);
        boardMessageList1.add(0);
        AlgorithmMacau.getInstance().initBigRoad(boardMessageList1, beadRoadList, beadRoadListShort,
                bigRoadListAll, bigRoadList, bigRoadListShort,
                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
                smallRoadListAll, smallRoadList, smallRoadListShort,
                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort,
                maxScoreListAll, maxScoreList, mContext, 2, true,
                iv_ask_bank_1, iv_ask_bank_2, iv_ask_bank_3, iv_ask_play_1, iv_ask_play_2, iv_ask_play_3,
                Color.RED, Color.BLUE, Color.GREEN);//1房间 2大厅
    }

    //************************************加载基本页面***********************************************
    private void initDisplay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scaleX = 0.0f;
                scaleY = 0.0f;
                float _value1;
                float _value2;
                if (rl_right_blank.getWidth() > 0) {
                    _value1 = Float.parseFloat(BaccaratUtil.getInstance().getScaleX(rl_right_blank.getWidth(), rl_wrap.getWidth()));//剩余部分占要缩放长度的百分比
                    rl_wrap.setPivotX(0);//左上角x轴0点为原点
                    scaleX = 1 + _value1;
                    rl_wrap.setScaleX(scaleX);
                }
                if (rl_bottom_blank.getHeight() > 0) {
                    _value2 = Float.parseFloat(BaccaratUtil.getInstance().getScaleX(rl_bottom_blank.getHeight(), rl_wrap.getHeight()));//剩余部分占要缩放高度的百分比
                    rl_wrap.setPivotY(0);//左上角y轴0点为原点
                    scaleY = 1 + _value2;
                    rl_wrap.setScaleY(scaleY);
                }
                zoomReturnVideo();
            }

        }, 150);
    }

    private void initTypeFace() {
        AssetManager mgr = getAssets();
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/fontzipMin.ttf");
        tv_player.setTypeface(tf);
        tv_banker.setTypeface(tf);
        tv_tie.setTypeface(tf);
        tv_banker_pair.setTypeface(tf);
        tv_player_pair.setTypeface(tf);
    }

    protected void initAdapter() {
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new ChipAdapter2(mContext, chipList);
        rv_chip.setLayoutManager(mLayoutManager);
        rv_chip.setAdapter(mAdapter);

        //滚动到指定位置，显示在最顶部 默认2
        int chipPosition;
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_POSITION) == 0) {
            chipPosition = 2;
        } else {
            chipPosition = SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_POSITION);
        }
        chipList.get(chipPosition).isCheck = true;
        mAdapter.updateData(chipList);
        rv_chip.scrollToPosition(chipPosition);
        selectChipNumber = chipList.get(chipPosition).chipNumber;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.SELECT_CHIP_NUMBER_BAC, selectChipNumber);
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) rv_chip.getLayoutManager();
        mLayoutManager.scrollToPositionWithOffset(chipPosition, 0);
        final MyYAnimation animation = new MyYAnimation();
        animation.setRepeatCount(Animation.INFINITE);

        mAdapter.setOnItemClickListener(new ChipAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position, ImageView imageView) {
                for (int i = 0; i < chipList.size(); i++) {
                    if (i == position) {
                        chipList.get(i).isCheck = true;
                        selectChipNumber = chipList.get(i).chipNumber;
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.SELECT_CHIP_NUMBER_BAC, selectChipNumber);
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_POSITION, position);
                    } else {
                        chipList.get(i).isCheck = false;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initDate() {
        chipImgList = new ArrayList<>();
        chipList = new ArrayList<>();
        Chip chip1 = new Chip();
        chip1.chipNumber = 10;
        chip1.chipImg = R.drawable.chip_10;
        chip1.isCheck = false;
        Chip chip2 = new Chip();
        chip2.chipNumber = 20;
        chip2.chipImg = R.drawable.chip_20;
        chip2.isCheck = false;
        Chip chip3 = new Chip();
        chip3.chipNumber = 50;
        chip3.chipImg = R.drawable.chip_50;
        chip3.isCheck = false;
        Chip chip4 = new Chip();
        chip4.chipNumber = 100;
        chip4.chipImg = R.drawable.chip_100;
        chip4.isCheck = false;
        Chip chip5 = new Chip();
        chip5.chipNumber = 200;
        chip5.chipImg = R.drawable.chip_200;
        chip5.isCheck = false;
        Chip chip6 = new Chip();
        chip6.chipNumber = 500;
        chip6.chipImg = R.drawable.chip_500;
        chip6.isCheck = false;
        Chip chip7 = new Chip();
        chip7.chipNumber = 1000;
        chip7.chipImg = R.drawable.chip_1000;
        chip7.isCheck = false;
        Chip chip11 = new Chip();
        chip11.chipNumber = 2000;
        chip11.chipImg = R.drawable.chip_2000;
        chip11.isCheck = false;
        Chip chip8 = new Chip();
        chip8.chipNumber = 5000;
        chip8.chipImg = R.drawable.chip_5000;
        chip8.isCheck = false;
        Chip chip9 = new Chip();
        chip9.chipNumber = 10000;
        chip9.chipImg = R.drawable.chip_10000;
        chip9.isCheck = false;
        Chip chip10 = new Chip();
        chip10.chipNumber = 50000;
        chip10.chipImg = R.drawable.chip_50000;
        chip10.isCheck = false;
        chipList.add(chip1);
        chipList.add(chip2);
        chipList.add(chip3);
        chipList.add(chip4);
        chipList.add(chip5);
        chipList.add(chip6);
        chipList.add(chip7);
        chipList.add(chip8);
        chipList.add(chip9);
        chipList.add(chip10);
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

    private int initChipLayout() {
        int selectChipNumber = SharedPreUtil.getInstance(mContext).getInt(Constant.SELECT_CHIP_NUMBER_BAC);
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

        cv_player_pair_chip.addChip(0);
        cv_banker_pair_chip.addChip(0);
        cv_player_chip.addChip(0);
        cv_banker_chip.addChip(0);
        cv_tie_chip.addChip(0);
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
        rl_banker_pair_chip.setVisibility(View.GONE);
        rl_tie_chip.setVisibility(View.GONE);
        rl_player_chip.setVisibility(View.GONE);
        rl_banker_chip.setVisibility(View.GONE);
        LogUtil.i("cleanChip=" + cv_player_chip.getChipNumSize());
    }

    private void betPlayerPair(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_player_pair_chip.addChip(integer);
        }
        rl_player_pair_chip.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_player_pair_chip.getLayoutParams();
        if (cv_player_pair_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
        } else if (cv_player_pair_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
        } else if (cv_player_pair_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
        } else if (cv_player_pair_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
        } else if (cv_player_pair_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
        } else if (cv_player_pair_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
        } else if (cv_player_pair_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_player_pair_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_pair_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_player_pair_integer.setLayoutParams(tvParams);
    }

    private void betBankerPair(int _currentMoney) {
        List<Integer> _playerChip = new ArrayList<>();
        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
        for (Integer integer : _playerChip) {
            cv_banker_pair_chip.addChip(integer);
        }
        rl_banker_pair_chip.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_banker_pair_chip.getLayoutParams();
        if (cv_banker_pair_chip.getChipNumSize() < 2) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
        } else if (cv_banker_pair_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
        } else if (cv_banker_pair_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
        } else if (cv_banker_pair_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
        } else if (cv_banker_pair_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
        } else if (cv_banker_pair_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
        } else if (cv_banker_pair_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_banker_pair_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_pair_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_banker_pair_integer.setLayoutParams(tvParams);
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
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
        } else if (cv_player_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
        } else if (cv_player_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
        } else if (cv_player_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
        } else if (cv_player_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
        } else if (cv_player_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
        } else if (cv_player_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
        } else if (cv_tie_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
        } else if (cv_tie_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
        } else if (cv_tie_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
        } else if (cv_tie_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
        } else if (cv_tie_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
        } else if (cv_tie_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
        } else if (cv_banker_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
        } else if (cv_banker_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
        } else if (cv_banker_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
        } else if (cv_banker_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
        } else if (cv_banker_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
        } else if (cv_banker_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_banker_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_banker_integer.setLayoutParams(tvParams);
    }

    private boolean insufficientBalance(int i) {
        int _allBet = Integer.parseInt(tv_bet_all_score.getText().toString());
        if (minBetList.size() > 0 && Double.parseDouble(tv_money.getText().toString()) < minBetList.get(i)) {
            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.balance_not_enough));
            if (_allBet > 0) {
                isChipClick(true, true, R.drawable.button_bet_normal_bg, R.drawable.button_bet_normal_bg, R.string.confirm);
            } else {
                isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            }
            return true;
        }
        return false;
    }

    private long lastOnClickTime = 0;

    private void initEvent() {
        tv_switch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (free == 0) {
                    tv_switch_on.setText(mContext.getResources().getString(R.string.no_commission));
                    tv_bank_odds.setText("1:1 " + mContext.getResources().getString(R.string.no_commission_msg));
                    free = 1;
                } else if (free == 1) {
                    tv_switch_on.setText(mContext.getResources().getString(R.string.no_commission_off));
                    tv_bank_odds.setText("1:0.95");
                    free = 0;
                }
            }
        });
        rl_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChip) {
                    //todo 当前积分小于前一句押注积分时，会出问题
                    List<Integer> allPer = new ArrayList<>();
                    allPer.clear();
                    allPer.add(perBanker);
                    allPer.add(perPlayer);
                    allPer.add(perTie);
                    allPer.add(perBankerPair);
                    allPer.add(perPlayerPair);
                    Collections.sort(allPer);
                    LogUtil.i("allPer=" + allPer.toString());//升序
                    if (cv_player_pair_chip.getChipNumSize() > 0) {
                        rl_player_pair_chip.setVisibility(View.VISIBLE);
                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perPlayerPair == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                                BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perBankerPair == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                                BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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

                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perPlayer == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                                BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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

                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perTie == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                                BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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

                    if (cv_banker_chip.getChipNumSize() > 0) {
                        rl_banker_chip.setVisibility(View.VISIBLE);
                        int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker;
                        double currentMoney = Double.parseDouble(tv_money.getText().toString());
                        int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                        if (total > _currentMoney) {
                            int _beforeFour = allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3);
                            if (_beforeFour < _currentMoney) {//最后下注剩余积分
                                if (perBanker == allPer.get(4)) {
                                    _currentMoney = _currentMoney - (allPer.get(0) + allPer.get(1) + allPer.get(2) + allPer.get(3));
                                    BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                                                BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
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
                    int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie;
                    tv_bet_all_score.setText(String.valueOf(allScore));
                    if (mSocketProdict != null && mSocketProdict.connected()) {
                        mSocketProdict.emit("prodict_score", "{'room':" + roomId + ",'player':" + token +
                                ",'score':[" + perPlayer + "," + perBanker + "," + perTie + "," + perPlayerPair + "," + perBankerPair + ",0,0],'clean':0}");
                    }
                } else {
                    mSocket = BacSocketController.getInstance().getSocket();
                    if (mSocket != null && mSocket.connected()) {
                        for (int i = 0; i < 7; i++) {
                            if (chipScore.get(i) == null) {
                                chipScore.put(i, 0);
                            }
                        }
                        if (Util.isFastClick(300)) {
                            //(0闲 1庄 2和 3闲对 4庄对 5大 6小) chipScore=庄 闲 和 庄对 闲对 大 小
                            mSocket.emit("dobet", "{'bets':[" + chipScore.get(1) + "," + chipScore.get(0) + "," + chipScore.get(2) +
                                    "," + chipScore.get(4) + "," + chipScore.get(3) + "," + chipScore.get(5) + "," + chipScore.get(6) + "],'freeCommission':'" + free + "'}");
                        }
                    }
                }
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
                if (mSocketProdict != null && mSocketProdict.connected()) {
                    LogUtil.i("mSocketProdict 预押注3=" + chipScore.toString());
                    mSocketProdict.emit("prodict_score", "{'room':" + roomId + ",'player':" + token +
                            ",'score':[" + playerScore + "," + bankerScore + "," + tieScore + "," + playerPairScore + "," + bankerPairScore + ",0,0],'clean':1}");
                }
                bankerScore = 0;
                playerScore = 0;
                tieScore = 0;
                bankerPairScore = 0;
                playerPairScore = 0;
                isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
                if (isSame) {
                    cleanChip();
                } else
                    currentChip2();
                int allScore = currentBetScore.get(0) + currentBetScore.get(1) + currentBetScore.get(2) + currentBetScore.get(3) + currentBetScore.get(4);
                tv_bet_all_score.setText(String.valueOf(allScore));

            }
        });
        //-----------------------------------------------------------------点击对应下注区域-start--------------------------------------------
        rl_player_pair.setOnClickListener(new View.OnClickListener() {//闲对
            @Override
            public void onClick(View v) {
                int selectChipNumber = initChipLayout();//不点重复，直接下注，清空以前所有位置记录，从新开始;
                //每局第一次下注 当选择的筹码小于最小下注筹码时，第一次下注实际是最小下注筹码 第二次就实际选择的筹码
                if (isFirstPlayPair && minBetList.size() > 0 && selectChipNumber < minBetList.get(2) && perPlayerPair == 0) {//0 庄闲 1 和 2 对子 3大小
                    selectChipNumber = minBetList.get(2);
                    isFirstPlayPair = false;
                }
                playerPairScore += selectChipNumber;//每局压分，下注成功就清空
                perPlayerPair += selectChipNumber;//续押，记录上局值
                int total = perPlayerPair + perBankerPair + perPlayer + perTie + perBanker + perSmall + perBig;
                if (insufficientBalance(2)) return;
                isRobot = false;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                _currentMoney = _currentMoney - (total - selectChipNumber);
                if (total > limitMoney) {
                    if (_currentMoney < 0) {
                        playerPairScore -= selectChipNumber;
                        perPlayerPair -= selectChipNumber;
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        return;
                    }

                    if (_currentMoney < selectChipNumber) {
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        int currentValue = perPlayerPair - selectChipNumber + Math.abs(_currentMoney);
                        tv_player_pair_integer.setText(String.valueOf(currentValue));
                        chipScore.put(4, (playerPairScore - selectChipNumber + _currentMoney));
                        betPlayerPair(_currentMoney);
                    }
                } else {
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
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                        } else if (cv_player_pair_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                        } else if (cv_player_pair_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                        } else if (cv_player_pair_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                        } else if (cv_player_pair_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                        } else if (cv_player_pair_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                        } else if (cv_player_pair_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_player_pair_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_pair_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_player_pair_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie;
                tv_bet_all_score.setText(String.valueOf(allScore));
//                emitProdict();
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
                isRobot = false;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                _currentMoney = _currentMoney - (total - selectChipNumber);
                if (total > limitMoney) {
                    if (_currentMoney < 0) {
                        playerPairScore -= selectChipNumber;
                        perPlayerPair -= selectChipNumber;
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        return;
                    }

                    if (_currentMoney < selectChipNumber) {
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        int currentValue = perBankerPair - selectChipNumber + Math.abs(_currentMoney);
                        tv_banker_pair_integer.setText(String.valueOf(currentValue));
                        chipScore.put(3, (bankerPairScore - selectChipNumber + _currentMoney));
                        betBankerPair(_currentMoney);
                    }
                } else {
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
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                        } else if (cv_banker_pair_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_banker_pair_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_pair_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_banker_pair_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie;
                tv_bet_all_score.setText(String.valueOf(allScore));
//                emitProdict();
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
                isRobot = false;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                _currentMoney = _currentMoney - (total - selectChipNumber);
                if (total > limitMoney) {
                    if (_currentMoney < 0) {
                        playerPairScore -= selectChipNumber;
                        perPlayerPair -= selectChipNumber;
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        return;
                    }

                    if (_currentMoney < selectChipNumber) {
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        int currentValue = perPlayer - selectChipNumber + Math.abs(_currentMoney);
                        tv_player_integer.setText(String.valueOf(currentValue));
                        chipScore.put(1, (playerScore - selectChipNumber + _currentMoney));
                        betPlayer(_currentMoney);
                    }
                } else {
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
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);//25
                        } else if (cv_player_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);//30
                        } else if (cv_player_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);//34
                        } else if (cv_player_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);//38
                        } else if (cv_player_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);//
                        } else if (cv_player_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                        } else if (cv_player_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_player_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_player_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie;
                tv_bet_all_score.setText(String.valueOf(allScore));
//                emitProdict();
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
                isRobot = false;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                _currentMoney = _currentMoney - (total - selectChipNumber);
                if (total > limitMoney) {
                    if (_currentMoney < 0) {
                        playerPairScore -= selectChipNumber;
                        perPlayerPair -= selectChipNumber;
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        return;
                    }

                    if (_currentMoney < selectChipNumber) {
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        int currentValue = perTie - selectChipNumber + Math.abs(_currentMoney);
                        tv_tie_integer.setText(String.valueOf(currentValue));
                        chipScore.put(2, (tieScore - selectChipNumber + _currentMoney));
                        betTie(_currentMoney);
                    }
                } else {
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
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                        } else if (cv_tie_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                        } else if (cv_tie_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                        } else if (cv_tie_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                        } else if (cv_tie_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                        } else if (cv_tie_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                        } else if (cv_tie_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_tie_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_tie_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_tie_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie;
                tv_bet_all_score.setText(String.valueOf(allScore));
//                emitProdict();
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
                isRobot = false;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
                int limitMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                _currentMoney = _currentMoney - (total - selectChipNumber);
                if (total > limitMoney) {
                    if (_currentMoney < 0) {
                        playerPairScore -= selectChipNumber;
                        perPlayerPair -= selectChipNumber;
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        return;
                    }

                    if (_currentMoney < selectChipNumber) {
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        int currentValue = perBanker - selectChipNumber + Math.abs(_currentMoney);
                        tv_banker_integer.setText(String.valueOf(currentValue));//当前局总分数
                        chipScore.put(0, (bankerScore - selectChipNumber + _currentMoney));//每次的投注分数
                        betBanker(_currentMoney);
                    }
                } else {
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
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
                        } else if (cv_banker_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
                        } else if (cv_banker_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
                        } else if (cv_banker_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
                        } else if (cv_banker_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
                        } else if (cv_banker_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
                        } else if (cv_banker_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_banker_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_banker_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_banker_integer.setLayoutParams(tvParams);
                    }
                }
                int allScore = perPlayer + perBanker + perPlayerPair + perBankerPair + perTie;
                tv_bet_all_score.setText(String.valueOf(allScore));
//                emitProdict();
            }
        });
        tv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_menu.setText("菜单 △");
                downPopupWindow(v);
            }
        });
        rl_video_select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_video_select2.setRotation(180f);//图片向下旋转
                upPopupWindow(v, iv_video_select2);
            }
        });
        tv_table_limits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_table_limits.setText("桌台限红 △");
                downPopupWindow2(v);
            }
        });
        tv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreUtil.getInstance(mContext).getInt(Constant.IS_CHIP_SUCCESS) != 0) {
                    BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.normal_lock_alert));
                    return;
                }
                SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 0);
                libPlayer.SmartPlayerClose(playerHandle);//退出要及时关闭 下次就不会出现重音
                mContext.startActivity(new Intent(mContext, BaccaratListActivity2.class));
                ((BacActivity) mContext).finish();
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

    public void upPopupWindow2(View v, final ImageView iv) {
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
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
        } else if (cv_banker_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
        } else if (cv_banker_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
        } else if (cv_banker_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
        } else if (cv_banker_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
        } else if (cv_banker_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
        } else if (cv_banker_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
        } else if (cv_player_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
        } else if (cv_player_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
        } else if (cv_player_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
        } else if (cv_player_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
        } else if (cv_player_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
        } else if (cv_player_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
        } else if (cv_tie_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
        } else if (cv_tie_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
        } else if (cv_tie_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
        } else if (cv_tie_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
        } else if (cv_tie_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
        } else if (cv_tie_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_tie_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_tie_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_tie_integer.setLayoutParams(tvParams);
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
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
        } else if (cv_banker_pair_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
        } else if (cv_banker_pair_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
        } else if (cv_banker_pair_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
        } else if (cv_banker_pair_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
        } else if (cv_banker_pair_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
        } else if (cv_banker_pair_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
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
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit28);
        } else if (cv_player_pair_chip.getChipNumSize() < 4) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit32);
        } else if (cv_player_pair_chip.getChipNumSize() < 6) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit36);
        } else if (cv_player_pair_chip.getChipNumSize() < 8) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit40);
        } else if (cv_player_pair_chip.getChipNumSize() < 10) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit44);
        } else if (cv_player_pair_chip.getChipNumSize() < 12) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit48);
        } else if (cv_player_pair_chip.getChipNumSize() < 14) {
            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit52);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rl_player_pair_chip.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_player_pair_integer.getLayoutParams();
        tvParams.addRule(RelativeLayout.ABOVE);
        tv_player_pair_integer.setLayoutParams(tvParams);
    }

    private void downPopupWindow(View v) {
//        Util.getInstance().hideSystemNavigationBar();
        if (popupWindow != null && popupWindow.isShowing()) return;
        //
        int width = (int) (tv_menu.getWidth() * scaleX);
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.pop_menu)
                .setWidthAndHeight(width, mContext.getResources().getDimensionPixelOffset(R.dimen.unit160))
                .setAnimationStyle(R.style.AnimDown)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();
        LogUtil.i("isSwitchRobot=" + isSwitchRobot + " isRobot=" + isRobot);
        TextView tv = popupWindow.getView().findViewById(R.id.tv_robot);
        if (state.equals(Constant.BACCARAT_RESULT)) {

        } else {

        }
        if (isSwitchRobot) {
            tv.setText("托管中");
        } else {
            tv.setText(mContext.getResources().getString(R.string.robot));
        }
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_menu.setText("菜单 ▽");
            }
        });
        popupWindow.showAsDropDown(v);
    }

    private void downPopupWindow2(View v) {
//        Util.getInstance().hideSystemNavigationBar();
        if (popupWindow != null && popupWindow.isShowing()) return;
        int width = (int) (tv_menu.getWidth() * scaleX);
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.pop_bac_limit2)
                .setWidthAndHeight(width, mContext.getResources().getDimensionPixelOffset(R.dimen.unit120))
                .setAnimationStyle(R.style.AnimDown)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tv_table_limits.setText("桌台限红 ▽");
            }
        });
        popupWindow.showAsDropDown(v);
    }

    //---------------------------------------------视频start------------------------------------------------------------
    private static final String TAG = "BacActivity";
    private SurfaceView mSv1 = null;
    private long playerHandle = 0;
    private SmartPlayerJniV2 libPlayer = null;
    private static final int PLAYER_EVENT_MSG = 1;
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

    public void copyWidget(SurfaceView sv1, TextView tv_big_sv_l, TextView tv_big_sv_h, Baccarat baccarat) {
        this.tv_big_sv_l = tv_big_sv_l;
        this.tv_big_sv_h = tv_big_sv_h;
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
        libPlayer.SmartPlayerSetOrientation(playerHandle, 2);//大牛直播 横竖屏切换重走生命周期 从其他app打开或者任务管理器上打开 视频都能正常播放 666
    }

    public void initBacDataVideo(Baccarat baccarat) {
        if (baccarat != null && baccarat.rtmp != null && baccarat.rtmp.length != 0) {
            String[] _url = baccarat.rtmp;
            hVideo = "rtmp://" + _url[1];
//            hVideo = "rtmp://live.hkstv.hk.lxdns.com/live/hks1";
            lVideo = "rtmp://" + _url[0];
//            hVideo = "rtmp://39.98.177.215/live/bl01a";
//            lVideo = "rtmp://39.98.177.215/live/bl01b";
            LogUtil.i("视频地址 初始化" + hVideo + " lVideo=" + lVideo);
            //视频地址=rtmp://39.98.177.215/live/bl01a lVideo=rtmp://39.98.177.215/live/bl01b
        }
    }

    private void initVideoEvent() {
        mSv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state != null && state.equals(Constant.BACCARAT_BET) || state.equals(Constant.BACCARAT_INNINGS_END) || (state.equals(Constant.BACCARAT_WAIT) && isFirstJoinRoom)) {
                    if (time == 0) {
                        zoomFullVideo();// 2
                    } else if (time == 2) {
                        zoomReturnVideo();//0
                    }
                } else {
                    if (time == 2) {
                        zoomQuarterVideo();//1 原来的发牌是现在的高度减少
                    } else if (time == 1 || time == 0) {
                        zoomFullVideo();//2
                    }
                }
            }
        });
        tv_big_sv_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPlay(lVideo);
                ToastUtil.show(mContext, mContext.getResources().getString(R.string.vv_2));
            }
        });
        tv_big_sv_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchPlay(hVideo);
                ToastUtil.show(mContext, mContext.getResources().getString(R.string.vv_1));
            }
        });
    }

    private void switchPlay(String url) {
        if (isPlaying) {
            start(url);
        } else {
            autoPlayer(url);
        }
        SharedPreUtil.getInstance(mContext).saveParam(Constant.CLOSE_ALL_VIDEO, 0);//点击除了关闭视频按钮，代表打开状态
    }

    /**
     * 视频还原
     */
    private void zoomReturnVideo() {
        tv_big_sv_l.setVisibility(View.GONE);
        tv_big_sv_h.setVisibility(View.GONE);
        rl_video_select2.setVisibility(View.GONE);
        time = 0;

        if (getSDK_INT() > 23) {
            AnimatorSet animatorSetVideo = new AnimatorSet();//组合动画
            ObjectAnimator _scaleX = ObjectAnimator.ofFloat(mSv1, "scaleX", 1f, 1f);
            ObjectAnimator _scaleY = ObjectAnimator.ofFloat(mSv1, "scaleY", 1f, scaleY);
            mSv1.setPivotX(0);
            mSv1.setPivotY(0);
            animatorSetVideo.setDuration(600);
            _scaleX.setRepeatMode(ObjectAnimator.RESTART);
            _scaleY.setRepeatMode(ObjectAnimator.RESTART);
            animatorSetVideo.setInterpolator(new DecelerateInterpolator());
            animatorSetVideo.play(_scaleX).with(_scaleY);//两个动画同时开始
            animatorSetVideo.start();
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSv1.getLayoutParams();
            layoutParams.width = mContext.getResources().getDimensionPixelOffset(R.dimen.unit210);
            layoutParams.height = mContext.getResources().getDimensionPixelOffset(R.dimen.unit180);
            mSv1.setLayoutParams(layoutParams);
        }


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSv1.getLayoutParams();
        if (rl_right_blank.getWidth() > 0) {
            layoutParams.width = (int) (rl_sv.getWidth() * scaleX);
        }
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) tv_room_name.getLayoutParams();
        layoutParams2.leftMargin = layoutParams.width - tv_room_name.getWidth() - mContext.getResources().getDimensionPixelOffset(R.dimen.unit10);
        tv_room_name.setLayoutParams(layoutParams2);
    }

    /**
     * 发牌 高度缩小 记录没数据
     */
    private void zoomQuarterVideo() {
        int scaleY_sv = (int) ((rl_sv.getHeight() - rl_poker.getHeight()) * scaleY);
        float toY = Float.parseFloat(BaccaratUtil.getInstance().getScaleX(scaleY_sv, rl_sv.getHeight()));
        if (getSDK_INT() > 23) {
            AnimatorSet animatorSetVideo = new AnimatorSet();//组合动画
            ObjectAnimator _scaleX = ObjectAnimator.ofFloat(mSv1, "scaleX", 1f, 1f);
            ObjectAnimator _scaleY = ObjectAnimator.ofFloat(mSv1, "scaleY", 1f, toY);
            mSv1.setPivotX(0);
            mSv1.setPivotY(0);
            animatorSetVideo.setDuration(600);
            _scaleX.setRepeatMode(ObjectAnimator.RESTART);
            _scaleY.setRepeatMode(ObjectAnimator.RESTART);
            animatorSetVideo.setInterpolator(new DecelerateInterpolator());
            animatorSetVideo.play(_scaleX).with(_scaleY);//两个动画同时开始
            animatorSetVideo.start();
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSv1.getLayoutParams();
            layoutParams.width = mContext.getResources().getDimensionPixelOffset(R.dimen.unit210);
            layoutParams.height = mContext.getResources().getDimensionPixelOffset(R.dimen.unit180)-mContext.getResources().getDimensionPixelOffset(R.dimen.unit48);
            mSv1.setLayoutParams(layoutParams);
        }
        tv_big_sv_l.setVisibility(View.GONE);
        tv_big_sv_h.setVisibility(View.GONE);
        rl_video_select2.setVisibility(View.GONE);
        time = 1;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mSv1.getLayoutParams();
        if (rl_right_blank.getWidth() > 0) {
            layoutParams.width = (int) (rl_sv.getWidth() * scaleX);
        }
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) tv_room_name.getLayoutParams();
        layoutParams2.leftMargin = layoutParams.width - tv_room_name.getWidth() - mContext.getResources().getDimensionPixelOffset(R.dimen.unit10);
        tv_room_name.setLayoutParams(layoutParams2);
    }

    private void zoomFullVideo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_big_sv_l.setVisibility(View.VISIBLE);
                tv_big_sv_h.setVisibility(View.VISIBLE);
                rl_video_select2.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) tv_room_name.getLayoutParams();
                layoutParams2.leftMargin = displayMetrics.widthPixels - tv_room_name.getWidth();
                tv_room_name.setLayoutParams(layoutParams2);
            }
        }, 600);

        time = 2;
        if (getSDK_INT() > 23) {
            float toY3 = Float.parseFloat(BaccaratUtil.getInstance().getScaleX(displayMetrics.heightPixels, mSv1.getHeight()));
            float toX3 = Float.parseFloat(BaccaratUtil.getInstance().getScaleX((rl_wrap.getWidth() + rl_right_blank.getWidth()), mSv1.getWidth()));
            AnimatorSet animatorSetVideo = new AnimatorSet();//组合动画
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mSv1, "scaleX", 1f, toX3);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mSv1, "scaleY", 1f, toY3);
            mSv1.setPivotX(0);
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
            LogUtil.i("EventHandleV2");
            String player_event = "";
            switch (id) {
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STARTED:
                    LogUtil.i(TAG, "开始。。");
                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTING:
                    LogUtil.i(TAG, "连接中。。");
                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTION_FAILED:
                    LogUtil.i(TAG, "连接失败。。");
                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTED:
                    LogUtil.i(TAG, "连接成功。。");
                    initNullBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_DISCONNECTED:
                    LogUtil.i(TAG, "连接断开。。");
                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STOP:
                    LogUtil.i(TAG, "停止播放。。");
                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_RESOLUTION_INFO:
                    LogUtil.i(TAG, "分辨率信息: width: " + param1 + ", height: " + param2 + "   isPlaying=");
                    initNullBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_NO_MEDIADATA_RECEIVED:
                    LogUtil.i(TAG, "收不到媒体数据，可能是url错误。。");
                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_SWITCH_URL:
                    LogUtil.i(TAG, "切换播放URL。。");
                    initDefaultBackground();
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
        ((BacActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSv1.setBackground(mContext.getResources().getDrawable(R.color.new_bg_bet));
            }
        });
    }

    private void initNullBackground() {
        ((BacActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSv1.setBackground(null);
            }
        });
    }

    //---------------------------------------------视频end------------------------------------------------------------
    private void betRobot() {
        //1.先判断余额够吗
        //2.20-10000随机金额
        //3.5个位置选一个下注
        //4.洗牌不能托管 手动下注时成功后当前局不能托管

        double currentMoney = Double.parseDouble(tv_money.getText().toString());
        if (currentMoney < 50) {
//            ToastUtil.show(mContext, "托管时，余额不能低于50");

            return;
        }
        if (state.equals(Constant.BACCARAT_BET) && isSwitchRobot) {
            int[] moneyArr1 = {200, 150, 100, 200, 1000, 260, 380, 720, 640, 850};
            int[] moneyArr2 = {200, 150, 100, 50, 250};
            int randomMoney1 = new Random().nextInt(10);
            int randomMoney2 = new Random().nextInt(5);
            int randomSite = new Random().nextInt(5);
            chipScore.clear();
            if (randomSite > 1) {
                chipScore.put(randomSite, moneyArr2[randomMoney2]);//随机位置，随机金额 和 对子
            } else {
                chipScore.put(randomSite, moneyArr1[randomMoney1]);//随机位置，随机金额 庄闲
            }
            if (randomSite > 1 && randomMoney2 > 1) {
                chipScore.put(new Random().nextInt(2), moneyArr1[randomMoney1]);
            }

            LogUtil.i("托管=" + chipScore.toString() + " state=" + state);

            mSocket = BacSocketController.getInstance().getSocket();
            if (mSocket != null && mSocket.connected()) {
                for (int i = 0; i < 7; i++) {
                    if (chipScore.get(i) == null) {
                        chipScore.put(i, 0);
                    }
                }
            }
            if (mSocket != null && mSocket.connected()) {
                if (randomSite == 0) {//闲
                    betPlayer(moneyArr1[randomMoney1]);
                } else if (randomSite == 1) {//庄
                    betBanker(moneyArr1[randomMoney1]);
                } else if (randomSite == 2) {//和
                    betTie(moneyArr2[randomMoney2]);
                } else if (randomSite == 3) {//闲对
                    betPlayerPair(moneyArr2[randomMoney2]);
                } else if (randomSite == 4) {//庄对
                    betBankerPair(moneyArr2[randomMoney2]);
                }
                mSocket.emit("dobet", "{'bets':[" + chipScore.get(1) + "," + chipScore.get(0) + "," + chipScore.get(2) +
                        "," + chipScore.get(4) + "," + chipScore.get(3) + "," + chipScore.get(5) + "," + chipScore.get(6) + "],'freeCommission':'" + free + "'}");
            }
        }
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.pop_menu:
                LinearLayout ll_ludan = view.findViewById(R.id.ll_ludan);
                LinearLayout ll_jilu = view.findViewById(R.id.ll_jilu);
                LinearLayout ll_shuoming = view.findViewById(R.id.ll_shuoming);
                LinearLayout ll_robot = view.findViewById(R.id.ll_robot);
                if (isRobot && state.equals(Constant.BACCARAT_RESULT)) {
                    ll_robot.setVisibility(View.VISIBLE);//后台设置
                } else if (!state.equals(Constant.BACCARAT_RESULT)) {
                    ll_robot.setVisibility(View.INVISIBLE);//后台设置
                }
                final TextView tv_robot = view.findViewById(R.id.tv_robot);
                ll_ludan.setOnClickListener(new View.OnClickListener() {
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
                ll_jilu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 1);
                        startActivity(new Intent(mContext, BetRecordListActivity.class));
                    }
                });
                ll_shuoming.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 1);
                        startActivity(new Intent(mContext, LocalHtmlWebViewActivity.class).putExtra("selectBac", 0));
                    }
                });
                ll_robot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isSwitchRobot == false) {
                            isRobot = true;
                            isSwitchRobot = true;
                            tv_robot.setText("托管中");
                            BaccaratUtil.getInstance().toast(rl_show, tv_show, "托管中");
                        } else if (isSwitchRobot == false && isRobot == false) {
                            isRobot = false;
                            tv_robot.setText(mContext.getResources().getString(R.string.robot));
                            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.robot));
                        } else if (isSwitchRobot == true) {
                            isRobot = false;
                            isSwitchRobot = false;
                            BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.robot));
                        }
                    }
                });
                break;
            case R.layout.pop_bac_limit2:
                TextView tv_bet_limit_bp = view.findViewById(R.id.tv_bet_limit_bp);
                TextView tv_bet_limit_tie = view.findViewById(R.id.tv_bet_limit_tie);
                TextView tv_bet_limit_pair = view.findViewById(R.id.tv_bet_limit_pair);
                TextView tv_table_limit_bp = view.findViewById(R.id.tv_table_limit_bp);
                TextView tv_table_limit_tie = view.findViewById(R.id.tv_table_limit_tie);
                TextView tv_table_limit_pair = view.findViewById(R.id.tv_table_limit_pair);
                if (betLimitList.size() > 0 && minBetList.size() > 0 && maxBetList.size() > 0) {
                    tv_bet_limit_bp.setText(String.valueOf(betLimitList.get(0)));
                    tv_bet_limit_tie.setText(String.valueOf(betLimitList.get(1)));
                    tv_bet_limit_pair.setText(String.valueOf(betLimitList.get(2)));
                    tv_table_limit_bp.setText(minBetList.get(0) + "-" + maxBetList.get(0));
                    tv_table_limit_tie.setText(minBetList.get(1) + "-" + maxBetList.get(1));
                    tv_table_limit_pair.setText(minBetList.get(2) + "-" + maxBetList.get(2));
                }
                break;
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
//                            LogUtil.i("视频地址 初始化电信" + hVideo + " lVideo=" + lVideo);
                            switchPlay(hVideo);
                        } else {
                            ToastUtil.show(mContext, "没有视频！");
                        }
                        popupWindow.dismiss();
                        if (time == 0) {

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
//                            LogUtil.i("视频地址 初始化移动" + hVideo + " lVideo=" + lVideo);
                            switchPlay(hVideo);
                        } else {
                            ToastUtil.show(mContext, "没有视频！");
                        }
                        popupWindow.dismiss();
                        if (time == 0) {

                        } else if (time == 1) {
                            iv_video_select2.setRotation(0f);
                        }
                    }
                });
                rl_lan.setOnClickListener(new View.OnClickListener() {//局域网
                    @Override
                    public void onClick(View v) {
                        if (baccarat.rtmp.length != 0 && !TextUtils.isEmpty(baccarat.rtmp[0])) {
                            String[] _url = baccarat.rtmp;
                            hVideo = "rtmp://" + _url[1];
                            lVideo = "rtmp://" + _url[0];
//                            LogUtil.i("视频地址 初始化局域网" + hVideo + " lVideo=" + lVideo);
                            switchPlay(hVideo);
                        } else {
                            ToastUtil.show(mContext, "没有视频！");
                        }
                        popupWindow.dismiss();
                        if (time == 0) {

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
                        BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.video_close));
                        popupWindow.dismiss();
                        if (time == 0) {

                        } else if (time == 1) {
                            iv_video_select2.setRotation(0f);
                        }
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.CLOSE_ALL_VIDEO, 1);
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        BacSocketController.getInstance().disconnectSocket();
    }
}
