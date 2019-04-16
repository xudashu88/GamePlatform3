package com.purity.yu.gameplatform.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.purity.yu.gameplatform.controler.DtLiveAnimController;
import com.purity.yu.gameplatform.controler.DtLiveController;
import com.purity.yu.gameplatform.controler.DtLiveSocketController;
import com.purity.yu.gameplatform.controler.DtSocketController;
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

import butterknife.BindView;
import io.socket.client.Socket;

@ContentView(R.layout.activity_dt_live)
public class DtLiveActivity extends BaseActivity {

    @BindView(R.id.root_video)
    RelativeLayout root_video;
    @BindView(R.id.sv)
    SurfaceView sv;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_table_limits)
    TextView tv_table_limits;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_game_name)
    TextView tv_game_name;
    @BindView(R.id.tv_code)
    TextView tv_code;
    @BindView(R.id.pcac)
    public PercentCircleAntiClockwise pcac;
    @BindView(R.id.rv_chip)
    public RecyclerView rv_chip;
    @BindView(R.id.rl_player)
    public RelativeLayout rl_player;
    @BindView(R.id.rl_tie)
    public RelativeLayout rl_tie;
    @BindView(R.id.rl_banker)
    public RelativeLayout rl_banker;
    @BindView(R.id.rl_in_time_layout)
    public RelativeLayout rl_in_time_layout;
    @BindView(R.id.rl_content)
    public RelativeLayout rl_content;
    @BindView(R.id.rl_just_in_time)
    public RelativeLayout rl_just_in_time;
    @BindView(R.id.rl_road)
    public RelativeLayout rl_road;
    @BindView(R.id.tv_bet_all_score)
    public TextView tv_bet_all_score;

    @BindView(R.id.gv_left)
    public YHZGridView gv_left;
    @BindView(R.id.gv_right_top)
    public YHZGridView gv_right_top;
    @BindView(R.id.gv_right_middle)
    public YHZGridView gv_right_middle;
    @BindView(R.id.gv_right_middle_grid)
    public YHZGridView gv_right_middle_grid;
    @BindView(R.id.gv_right_bottom_1)
    public YHZGridView gv_right_bottom_1;
    @BindView(R.id.gv_right_bottom_1_grid)
    public YHZGridView gv_right_bottom_1_grid;
    @BindView(R.id.gv_right_bottom_2)
    public YHZGridView gv_right_bottom_2;
    @BindView(R.id.gv_right_bottom_2_grid)
    public YHZGridView gv_right_bottom_2_grid;
    @BindView(R.id.v_1)
    public View v_1;
    @BindView(R.id.v_2)
    public View v_2;
    @BindView(R.id.v_3)
    public View v_3;
    @BindView(R.id.v_4)
    public View v_4;
    @BindView(R.id.v_5)
    public View v_5;
    @BindView(R.id.gv_left_short)
    public YHZGridView gv_left_short;
    @BindView(R.id.gv_right_top_short)
    public YHZGridView gv_right_top_short;
    @BindView(R.id.gv_right_middle_short)
    public YHZGridView gv_right_middle_short;
    @BindView(R.id.gv_right_middle_short_grid)
    public YHZGridView gv_right_middle_short_grid;
    @BindView(R.id.gv_right_bottom_1_short)
    public YHZGridView gv_right_bottom_1_short;
    @BindView(R.id.gv_right_bottom_1_short_grid)
    public YHZGridView gv_right_bottom_1_short_grid;
    @BindView(R.id.gv_right_bottom_2_short)
    public YHZGridView gv_right_bottom_2_short;
    @BindView(R.id.gv_right_bottom_2_short_grid)
    public YHZGridView gv_right_bottom_2_short_grid;
    @BindView(R.id.v_1_short)
    public View v_1_short;
    @BindView(R.id.v_2_short)
    public View v_2_short;
    @BindView(R.id.v_3_short)
    public View v_3_short;
    @BindView(R.id.v_4_short)
    public View v_4_short;
    @BindView(R.id.v_5_short)
    public View v_5_short;

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
    @BindView(R.id.tv_in_game_banker)
    public TextView tv_in_game_banker;
    @BindView(R.id.tv_in_game_player)
    public TextView tv_in_game_player;
    @BindView(R.id.tv_in_game_tie)
    public TextView tv_in_game_tie;

    @BindView(R.id.rl_banker_to_player)
    public RelativeLayout rl_banker_to_player;
    @BindView(R.id.iv_banker_to_player_1)
    public ImageView iv_ask_bank_1;
    @BindView(R.id.iv_banker_to_player_2)
    public ImageView iv_ask_bank_2;
    @BindView(R.id.iv_banker_to_player_3)
    public ImageView iv_ask_bank_3;
    @BindView(R.id.rl_player_to_banker)
    public RelativeLayout rl_player_to_banker;
    @BindView(R.id.iv_player_to_banker_1)
    public ImageView iv_ask_play_1;
    @BindView(R.id.iv_player_to_banker_2)
    public ImageView iv_ask_play_2;
    @BindView(R.id.iv_player_to_banker_3)
    public ImageView iv_ask_play_3;

    @BindView(R.id.tv_full_road)
    public TextView tv_full_road;
    @BindView(R.id.rl_ex_in)
    public RelativeLayout rl_ex_in;
    @BindView(R.id.iv_ex)
    public ImageView iv_ex;
    @BindView(R.id.rl_video_select)
    public RelativeLayout rl_video_select;
    @BindView(R.id.iv_video_select)
    public ImageView iv_video_select;
    @BindView(R.id.iv_bac_set)
    ImageView iv_bac_set;
    //实时下注积分
    @BindView(R.id.tv_play_date)
    TextView tv_play_date;
    @BindView(R.id.tv_tie_date)
    TextView tv_tie_date;
    @BindView(R.id.tv_bank_date)
    TextView tv_bank_date;
    //发牌
    @BindView(R.id.ll_poker)
    LinearLayout ll_poker;
    @BindView(R.id.rl_jet_player_layout)
    RelativeLayout rl_jet_player_layout;
    @BindView(R.id.rl_jet_player)
    RelativeLayout rl_jet_player;
    @BindView(R.id.tv_jet_player_value)
    TextView tv_jet_player_value;
    @BindView(R.id.iv_jet_player_01)
    ImageView iv_jet_player_01;

    @BindView(R.id.rl_jet_banker_layout)
    RelativeLayout rl_jet_banker_layout;
    @BindView(R.id.rl_jet_banker)
    RelativeLayout rl_jet_banker;
    @BindView(R.id.tv_jet_banker_value)
    TextView tv_jet_banker_value;
    @BindView(R.id.iv_jet_banker_01)
    ImageView iv_jet_banker_01;

    @BindView(R.id.rl_sure)
    RelativeLayout rl_sure;//确定下注
    @BindView(R.id.tv_sure)
    TextView tv_sure;//确定下注
    @BindView(R.id.rl_cancel)
    RelativeLayout rl_cancel;//取消下注
    @BindView(R.id.rl_play_light)
    RelativeLayout rl_play_light;
    @BindView(R.id.rl_bank_light)
    RelativeLayout rl_bank_light;

    @BindView(R.id.rl_player_chip)
    RelativeLayout rl_player_chip;
    @BindView(R.id.cv_player_chip)
    ChipView cv_player_chip;
    @BindView(R.id.tv_player_integer)
    TextView tv_player_integer;

    @BindView(R.id.rl_tie_chip)
    RelativeLayout rl_tie_chip;
    @BindView(R.id.cv_tie_chip)
    ChipView cv_tie_chip;
    @BindView(R.id.tv_tie_integer)
    TextView tv_tie_integer;

    @BindView(R.id.rl_banker_chip)
    RelativeLayout rl_banker_chip;
    @BindView(R.id.cv_banker_chip)
    ChipView cv_banker_chip;
    @BindView(R.id.tv_banker_integer)
    TextView tv_banker_integer;
    @BindView(R.id.tv_dragon)
    TextView tv_dragon;
    @BindView(R.id.tv_tiger)
    TextView tv_tiger;
    @BindView(R.id.tv_tie)
    TextView tv_tie;
    @BindView(R.id.iv_result_player)
    ImageView iv_result_player;
    @BindView(R.id.iv_result_banker)
    ImageView iv_result_banker;
    @BindView(R.id.tv_push_tiger)
    TextView tv_push_tiger;
    @BindView(R.id.tv_push_dragon)
    TextView tv_push_dragon;
    @BindView(R.id.iv_result_banker2)
    ImageView iv_result_banker2;
    @BindView(R.id.iv_result_player2)
    ImageView iv_result_player2;

    @BindView(R.id.rl_video_select1)
    RelativeLayout rl_video_select1;
    @BindView(R.id.iv_video_select1)
    ImageView iv_video_select1;

    private GestureDetector gestureDetector;
    private Baccarat baccarat;
    private Context mContext;
    private String roomName;
    private String roomId;
    private Socket mSocket;
    private int playerIng = 0, bankerIng, tieIng, playerPairIng = 0, bankerPairIng = 0;//实时下注积分
    private int playerScore = 0, bankerScore = 0, tieScore = 0, playerPairScore = 0, bankerPairScore = 0;//当前局每个位置下注积分
    private int perPlayer = 0, perBanker = 0, perTie = 0, perPlayerPair = 0, perBankerPair = 0;//每局每个位置下注总积分
    private int free = 0;//默认庄非免佣1:0.95 1免佣
    private boolean isChip = false;//默认未保存下注记录
    private int isChipSuccess = 0;//默认0下注不成功 1下注成功
    private boolean isSame = false;//默认不续押
    private boolean isAllRemain = false;//默认未全押
    private int bankColor = 0;
    private int playColor = 0;
    private int tieColor = 0;

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
    private double currentMoney;
    private int deskId = 0;
    private int noBetCount = 0;
    private int isPeopleNum = -1;
    private int betsRemind = -1;
    private int betSecond;
    private boolean isFirstPlay = true, isFirstBank = true, isFirstTie = true;
    private String dragonColorStr;
    private String tigerColorStr;
    private String tieColorStr;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = DtLiveActivity.this;
        tv_nickname.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, 0);//进入房间未下注状态
        roomName = getIntent().getStringExtra("roomName");
        roomId = getIntent().getStringExtra("roomId");
        betSecond = getIntent().getIntExtra("betSecond", 25);
        tv_game_name.setText(getResources().getString(R.string.dt_game) + roomName);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        DtLiveSocketController.getInstance().init(this);
        DtLiveSocketController.getInstance().copyWidget(boardMessageList, pcac, ll_poker, iv_jet_banker_01,
                tv_jet_banker_value, iv_jet_player_01, tv_jet_player_value, _calPlayer, _calBanker, roomId,
                rl_sure, rl_player, rl_tie, rl_banker, betSecond);
        DtLiveSocketController.getInstance().connectSocket();
        EventBus.getDefault().register(this);

        SharedPreUtil.getInstance(mContext).saveParam(Constant.WHERE_SETED, 0);
        DtLiveController.getInstance().init(this);
        DtLiveController.getInstance().copyWidget(rv_chip,
                rl_player, rl_tie, rl_banker,
                rl_sure, rl_cancel, tv_money);

        baccarat = (Baccarat) getIntent().getSerializableExtra("baccarat");
        DtLiveAnimController.getInstance().init(this);
        DtLiveAnimController.getInstance().copyWidget(tv_table_limits, rl_in_time_layout, iv_ex, rl_video_select, iv_video_select, sv, root_video, ll_back, iv_bac_set,
                baccarat, rl_video_select1, iv_video_select1);

        gestureDetector = new GestureDetector(mContext, new MyGestureListener(this));
        initLiveInTime();

        currentBetScore.clear();
        currentBetScore.add(0);
        currentBetScore.add(0);
        currentBetScore.add(0);

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
            DtLiveAnimController.getInstance().initBacDataVideo(baccarat);
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
//            boardMessageList = BaccaratUtil.getInstance().initBoardMessage(53, boardMessageList);
//            initMessages();
            Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                    gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 1,
                    bankColor, playColor, tieColor, 0, false, 2);//4列 手动进房间
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
                bankerScore = 0;
                playerScore = 0;
                tieScore = 0;
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

        isExpand = SharedPreUtil.getInstance(mContext).getInt(Constant.IS_EXPAND);
        //-----------------------------------------------------------------点击对应下注区域-start--------------------------------------------
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
                LogUtil.i("当前下注=虎1=" + currentBetScore.toString() + " perPlayer=" + perPlayer + " playerScore=" + playerScore +
                        " perBanker=" + perBanker + " bankerScore=" + bankerScore +
                        " perTie=" + perTie + " tieScore=" + tieScore);
                int total = perPlayer + perTie + perBanker;
                if (insufficientBalance(0)) return;
                int _currentMoney = Integer.parseInt(String.valueOf(currentMoney).split("\\.")[0]);
//                int _currentMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
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
                    if (isSame == false) {
                        chipScore.put(1, playerScore);//每局下注分数
                    } else {
                        chipScore.put(1, perPlayer);//续押分数
                    }
                    LogUtil.i("当前下注=虎2=" + currentBetScore.toString() + " perPlayer=" + perPlayer + " playerScore=" + playerScore +
                            " perBanker=" + perBanker + " bankerScore=" + bankerScore +
                            " perTie=" + perTie + " tieScore=" + tieScore);
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
                LogUtil.i("当前下注=和1=" + currentBetScore.toString() + " perPlayer=" + perPlayer + " playerScore=" + playerScore +
                        " perBanker=" + perBanker + " bankerScore=" + bankerScore +
                        " perTie=" + perTie + " tieScore=" + tieScore);
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
                        List<Integer> _playerChip = new ArrayList<>();
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_tie_chip.addChip(integer);
                        }
                        rl_tie_chip.setVisibility(View.VISIBLE);
                        int currentValue = perTie - selectChipNumber + Math.abs(_currentMoney);
                        tv_tie_integer.setText(String.valueOf(currentValue));
                        chipScore.put(2, (tieScore - selectChipNumber + _currentMoney));
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
                    if (isSame == false) {
                        chipScore.put(2, tieScore);
                    } else {
                        chipScore.put(2, perTie);//续押分数
                    }
                    LogUtil.i("当前下注=和2=" + currentBetScore.toString() + " perPlayer=" + perPlayer + " playerScore=" + playerScore +
                            " perBanker=" + perBanker + " bankerScore=" + bankerScore +
                            " perTie=" + perTie + " tieScore=" + tieScore);
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
                LogUtil.i("当前下注=龙1=" + currentBetScore.toString() + " perPlayer=" + perPlayer + " playerScore=" + playerScore +
                        " perBanker=" + perBanker + " bankerScore=" + bankerScore +
                        " perTie=" + perTie + " tieScore=" + tieScore);
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
                    if (isSame == false) {
                        chipScore.put(0, bankerScore);
                    } else {
                        chipScore.put(0, perBanker);//续押分数
                    }
                    LogUtil.i("当前下注=龙2=" + currentBetScore.toString() + " perPlayer=" + perPlayer + " playerScore=" + playerScore +
                            " perBanker=" + perBanker + " bankerScore=" + bankerScore +
                            " perTie=" + perTie + " tieScore=" + tieScore);
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

    /**
     * 庄问路
     *
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
                new ArrayList<Integer>(), new ArrayList<Integer>(), mContext, 1, isShowAsk,
                iv_ask_bank_1, iv_ask_bank_2, iv_ask_bank_3, iv_ask_play_1, iv_ask_play_2, iv_ask_play_3,
                bankColor, playColor, tieColor);//1房间 2大厅
        if (isDraw) {
            if (isExpand == 0) {
                Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                        gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 1,
                        bankColor, playColor, tieColor, count, isShow, 2);//4列 加一局
            } else if (isExpand == 2) {
                Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                        gv_left, gv_right_top, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 1,
                        bankColor, playColor, tieColor, count, isShow, 2);//11列 加一局
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
                gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 1,
                bankColor, playColor, tieColor, 0, false, 2);//4列 trans1to0
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
                gv_left, gv_right_top, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 1, bankColor, playColor, tieColor, 0, false, 2);//11列 trans0to2
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
                gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 1,
                bankColor, playColor, tieColor, 0, false, 2);//4列 trans2to0
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
                if (languageType == LanguageType.LANGUAGE_EN) {
                    SoundPoolUtil.getInstance().initDtEN(this);
                } else {
                    SoundPoolUtil.getInstance().initDtZH(this);
                }
                //初始化完毕才能播放（延迟加载，是因为偶尔播放）
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {
                        SoundPoolUtil.getInstance().play("49");
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

//        Algorithm.getInstance().initBigRoad(boardMessageList, beadRoadList, beadRoadListShort,
//                bigRoadListAll, bigRoadList, bigRoadListShort,
//                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
//                smallRoadListAll, smallRoadList, smallRoadListShort,
//                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort, mContext, 1);//1房间 2大厅
        Algorithm.getInstance().initBigRoad(boardMessageList, beadRoadList, beadRoadListShort,
                bigRoadListAll, bigRoadList, bigRoadListShort,
                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
                smallRoadListAll, smallRoadList, smallRoadListShort,
                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort,
                new ArrayList<Integer>(), new ArrayList<Integer>(), mContext, 1, false,
                iv_ask_bank_1, iv_ask_bank_2, iv_ask_bank_3, iv_ask_play_1, iv_ask_play_2, iv_ask_play_3,
                bankColor, playColor, tieColor);//1房间 2大厅
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
        rl_player.setClickable(clickable);
        rl_tie.setClickable(clickable);
        rl_banker.setClickable(clickable);
        rl_sure.setClickable(clickable);
    }

    /*
     * 同步数据
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventSyn(final ObjectEvent.BoardMessageEvent event) {
        if (event.boardMessageList.size() > 0) {
            tv_game_name.setText(mContext.getResources().getString(R.string.dt_game) + event.roomName);
            deskId = event.deskId;
            tv_code.setText(getResources().getString(R.string.game_code) + ":" + String.valueOf(event.deskId) + boardMessageList.size());
            initMessages();
            Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                    gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 1,
                    bankColor, playColor, tieColor, 0, false, 2);//4列 网络进入房间
        }
    }

    /*
     * 进入房间
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBoardMessage(final ObjectEvent.BoardMessageEvent event) {
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
        initColor(event);

        if (!event.state.equals(Constant.BACCARAT_BET)) {
            onClick(false);
            ll_poker.setVisibility(View.GONE);
        } else {
            currentMoney = Double.parseDouble(tv_money.getText().toString());
        }
        if (event.boardMessageList.size() > 0) {
            tv_game_name.setText(mContext.getResources().getString(R.string.dt_game) + event.roomName);
            deskId = event.deskId;
            tv_code.setText(getResources().getString(R.string.game_code) + ":" + String.valueOf(event.deskId) + boardMessageList.size());
            initMessages();
            Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                    gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 1,
                    bankColor, playColor, tieColor, 0, false, 2);//4列 网络进入房间
            ask(0, true, false, 0, false);
        }
        String _1 = "0";
        String _2 = "0";
        String _3 = "0";
        if (isPeopleNum == 0) {
            _1 = String.valueOf(playerIng);
            _2 = String.valueOf(bankerIng);
            _3 = String.valueOf(tieIng);
        } else if (isPeopleNum == 1) {
            _1 = String.valueOf(playerIng) + "/" + 0;
            _2 = String.valueOf(bankerIng) + "/" + 0;
            _3 = String.valueOf(tieIng) + "/" + 0;
        }
        tv_play_date.setText(_1);
        tv_bank_date.setText(_2);
        tv_tie_date.setText(_3);
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
            playerIng = event.scoreList.get(1);
            bankerIng = event.scoreList.get(0);
            tieIng = event.scoreList.get(2);
            String _2 = String.valueOf(playerIng) + "/" + event.betPeopleList.get(1);
            String _1 = String.valueOf(bankerIng) + "/" + event.betPeopleList.get(0);
            String _3 = String.valueOf(tieIng) + "/" + String.valueOf(event.betPeopleList.get(2));
            tv_play_date.setText(_2);
            tv_bank_date.setText(_1);
            tv_tie_date.setText(_3);
            currentBetScore.clear();
            currentBetScore.addAll(event.scoreList);
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
//            currentBetScore.add(0);
//            currentBetScore.add(0);
            isAllRemain = false;//恢复下注所有金额
            currentMoney = Double.parseDouble(tv_money.getText().toString());
            //先隐藏
//            rl_player_pair_chip.setVisibility(View.GONE);
//            rl_player_pair_chip1.setVisibility(View.GONE);
//            rl_banker_pair_chip.setVisibility(View.GONE);
//            rl_banker_pair_chip1.setVisibility(View.GONE);
            rl_tie_chip.setVisibility(View.GONE);
            rl_player_chip.setVisibility(View.GONE);
//            rl_player_chip1.setVisibility(View.GONE);
//            rl_player_chip2.setVisibility(View.GONE);
            rl_banker_chip.setVisibility(View.GONE);
//            rl_banker_chip1.setVisibility(View.GONE);
//            rl_banker_chip2.setVisibility(View.GONE);
            LogUtil.i("下注_command" + perPlayerPair + " " + isChip);
            if (perPlayerPair != 0 || perBankerPair != 0 || perPlayer != 0 || perTie != 0 || perBanker != 0) {
                isChip = true;
                isChipClick(false, true, R.drawable.button_bet_disable_bg, R.drawable.button_bet_normal_bg, R.string.repeat);
            } else {
                isChip = false;
                isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            }

            ToastUtil.getInstance().showToast(mContext, mContext.getResources().getString(R.string.bet_start));
            isChipSuccess = 0;//新一局 代表未下注成功
            SoundPoolUtil.getInstance().play("50");//已开局，请下注
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
        } else if (event.state.equals("RESULT")) {
            onClick(false);
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            ll_poker.setVisibility(View.VISIBLE);
            ToastUtil.getInstance().showToast(mContext, mContext.getResources().getString(R.string.bet_stop));
            SoundPoolUtil.getInstance().play("52");//停止下注
            //清空处理
            rl_jet_player_layout.setBackgroundResource(0);
            rl_jet_banker_layout.setBackgroundResource(0);
            rl_jet_player.setBackgroundResource(0);
            rl_jet_banker.setBackgroundResource(0);
            tv_jet_player_value.setText("0");
            tv_jet_banker_value.setText("0");
            iv_jet_player_01.setBackgroundResource(0);
//            iv_jet_player_02.setBackgroundResource(0);
//            iv_jet_player_03.setBackgroundResource(0);
            iv_jet_banker_01.setBackgroundResource(0);
//            iv_jet_banker_02.setBackgroundResource(0);
//            iv_jet_banker_03.setBackgroundResource(0);
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
//            String _4 = 0 + "/" + 0;
//            String _5 = 0 + "/" + 0;
            tv_play_date.setText(_1);
            tv_bank_date.setText(_2);
            tv_tie_date.setText(_3);
//            tv_player_pair_date.setText(_4);
//            tv_banker_pair_date.setText(_5);
        } else if (event.state.equals(Constant.BACCARAT_CREATED)) {//新的一轮开始
            boardMessageList.clear();
            initMessages();
            if (isExpand == 0) {
                Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                        gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 1,
                        bankColor, playColor, tieColor, 0, false, 2);//4列 加一局
            } else if (isExpand == 2) {
                Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                        gv_left, gv_right_top, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 1,
                        bankColor, playColor, tieColor, 0, false, 2);//11列 加一局
            }
        } else if (event.state.equals(Constant.BACCARAT_FAULT)) {
            ToastUtil.show(mContext, "机械臂异常");
        } else if (event.state.equals(Constant.BACCARAT_INVALID)) {
            ToastUtil.show(mContext, "本局作废");
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
        if (event.pos.equals("04")) {//乐虎只有一张牌 03和04 1-虎
            _calPlayer.clear();
            _calBanker.clear();
            iv_jet_player_01.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calPlayer.add(event.card);
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
            iv_jet_banker_01.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            _calBanker.add(event.card);
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
        if (event.win != -1) {
            List<String> player = event.playCardList;
            List<String> banker = event.bankCardList;
            final int playScore = BaccaratUtil.getInstance().getDtCardScore(player.get(0));
            final int bankScore = BaccaratUtil.getInstance().getDtCardScore(banker.get(0));
            final boolean isTie = (playScore == bankScore);
            if (boardMessageList.size() + 1 == event.round) {
                boardMessageList.add(-1);
                boardMessageList.set(event.round - 1, event.win);//
            }

            initMessages();
            if (isExpand == 0) {
                Algorithm.getInstance().drawRoad(beadRoadListShort, bigRoadListShort, bigEyeRoadListShort, smallRoadListShort, cockroachRoadListShort,
                        gv_left_short, gv_right_top_short, gv_right_middle_short, gv_right_bottom_1_short, gv_right_bottom_2_short, mContext, 1,
                        bankColor, playColor, tieColor, 0, false, 2);//4列 加一局
            } else if (isExpand == 2) {
                Algorithm.getInstance().drawRoad(beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                        gv_left, gv_right_top, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 1,
                        bankColor, playColor, tieColor, 0, false, 2);//11列 加一局
            }
            ask(0, true, false, 0, false);
            //1.庄几点 闲几点 一遍 先报庄几点再报闲几点
            //2.庄赢/闲赢 2遍 和局 1遍
            BaccaratUtil.sayDtDot(0, bankScore);
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaccaratUtil.sayDtDot(1, playScore);
                }
            }, 1200);
            if (playScore > bankScore) {
                rl_play_light.setVisibility(View.VISIBLE);
                FlashHelper.getInstance().startFlick(rl_play_light, 4);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaccaratUtil.sayDtDot(1, isTie);
                    }
                }, 3500);

            } else if (playScore < bankScore) {
                rl_bank_light.setVisibility(View.VISIBLE);
                FlashHelper.getInstance().startFlick(rl_bank_light, 4);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaccaratUtil.sayDtDot(0, isTie);
                    }
                }, 3500);

            } else {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaccaratUtil.sayDtDot(-1, isTie);//和局
                    }
                }, 3500);
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
        isSame = false;
        tv_money.setText(event.availableAmount);
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
        chipScore.put(0, 0);
        chipScore.put(1, 0);
        chipScore.put(2, 0);
        playerScore = 0;
        tieScore = 0;
        bankerScore = 0;
        LogUtil.i("当前下注=3" + currentBetScore.toString() + " perBanker=" + perBanker + " perPlayer=" + perPlayer + " perTie=" + perTie);
        currentBetScore.set(0, perBanker);
        currentBetScore.set(1, perPlayer);
        currentBetScore.set(2, perTie);
        LogUtil.i("当前下注=4" + currentBetScore.toString() + " perBanker=" + perBanker + " perPlayer=" + perPlayer + " perTie=" + perTie);
        currentChip2();
        SoundPoolUtil.getInstance().play("51");//下注成功
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
            isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
            currentChip();
            chipScore.put(0, 0);
            chipScore.put(1, 0);
            chipScore.put(2, 0);
//            chipScore.put(3, 0);
//            chipScore.put(4, 0);
            isAllRemain = false;
        }
    }

    private void initColor(ObjectEvent.BoardMessageEvent event) {
        bankColor = Color.parseColor(event.color[0]);
        playColor = Color.parseColor(event.color[1]);
        tieColor = Color.parseColor(event.color[2]);
        GradientDrawable drawableBank = (GradientDrawable) iv_result_banker.getBackground();
        drawableBank.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit1), bankColor);
        GradientDrawable drawableBank2 = (GradientDrawable) iv_result_banker2.getBackground();
        drawableBank2.setColor(bankColor);
        GradientDrawable drawablePlay = (GradientDrawable) iv_result_player.getBackground();
        drawablePlay.setStroke(mContext.getResources().getDimensionPixelOffset(R.dimen.unit1), playColor);
        GradientDrawable drawablePlay2 = (GradientDrawable) iv_result_player2.getBackground();
        drawablePlay2.setColor(playColor);
        tv_dragon.setTextColor(bankColor);//直接对#032222设置值
        tv_tiger.setTextColor(playColor);
        tv_tie.setTextColor(tieColor);
        tv_push_dragon.setTextColor(bankColor);
        tv_push_tiger.setTextColor(playColor);
        tv_in_game_banker.setTextColor(bankColor);
        tv_in_game_player.setTextColor(playColor);
        tv_in_game_tie.setTextColor(tieColor);
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
//                mContext.startActivity(new Intent(mContext, DtListActivity.class));
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
        DtLiveSocketController.getInstance().disconnectSocket();
    }
}