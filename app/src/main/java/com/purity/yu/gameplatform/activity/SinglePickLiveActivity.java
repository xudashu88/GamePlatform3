package com.purity.yu.gameplatform.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
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
import com.purity.yu.gameplatform.baccarat.YCHGridView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.controler.SinglePickLiveAnimControler;
import com.purity.yu.gameplatform.controler.SinglePickLiveController;
import com.purity.yu.gameplatform.controler.SinglePickLiveInTimeController;
import com.purity.yu.gameplatform.controler.SinglePickLiveSocketController;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.Liquidation;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.Algorithm;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.SoundPoolUtil;
import com.purity.yu.gameplatform.widget.ChipView;
import com.purity.yu.gameplatform.widget.HintDialog;
import com.purity.yu.gameplatform.widget.PercentCircleAntiClockwise;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.socket.client.Socket;

@ContentView(R.layout.activity_single_pick_live)
public class SinglePickLiveActivity extends BaseActivity {

    @BindView(R.id.root_video)
    RelativeLayout root_video;
    @BindView(R.id.sv)
    SurfaceView sv;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_game_name)
    TextView tv_game_name;
    @BindView(R.id.tv_table_limits)
    TextView tv_table_limits;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.pcac)
    public PercentCircleAntiClockwise pcac;
    @BindView(R.id.rv_chip)
    public RecyclerView rv_chip;
    @BindView(R.id.rl_in_time_layout)
    public RelativeLayout rl_in_time_layout;
    @BindView(R.id.rl_content)
    public RelativeLayout rl_content;
    @BindView(R.id.rl_just_in_time)
    public RelativeLayout rl_just_in_time;
    @BindView(R.id.rl_road)
    public RelativeLayout rl_road;

    @BindView(R.id.gv_left)
    public YCHGridView gv_left;
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

    @BindView(R.id.tv_spade_count)
    TextView tv_spade_count;
    @BindView(R.id.tv_hearts_count)
    TextView tv_hearts_count;
    @BindView(R.id.tv_clubs_count)
    TextView tv_clubs_count;
    @BindView(R.id.tv_diamonds_count)
    TextView tv_diamonds_count;
    @BindView(R.id.tv_king_count)
    TextView tv_king_count;

    @BindView(R.id.tv_spade_date)
    TextView tv_spade_date;
    @BindView(R.id.tv_hearts_date)
    TextView tv_hearts_date;
    @BindView(R.id.tv_clubs_date)
    TextView tv_clubs_date;
    @BindView(R.id.tv_diamonds_date)
    TextView tv_diamonds_date;
    @BindView(R.id.tv_king_date)
    TextView tv_king_date;

    @BindView(R.id.rl_spade)
    RelativeLayout rl_spade;
    @BindView(R.id.rl_hearts)
    RelativeLayout rl_hearts;
    @BindView(R.id.rl_clubs)
    RelativeLayout rl_clubs;
    @BindView(R.id.rl_diamonds)
    RelativeLayout rl_diamonds;
    @BindView(R.id.rl_king)
    RelativeLayout rl_king;

    @BindView(R.id.rl_spade_chip)
    RelativeLayout rl_spade_chip;
    @BindView(R.id.cv_spade_chip)
    ChipView cv_spade_chip;
    @BindView(R.id.tv_spade_integer)
    TextView tv_spade_integer;

    @BindView(R.id.rl_hearts_chip)
    RelativeLayout rl_hearts_chip;
    @BindView(R.id.cv_hearts_chip)
    ChipView cv_hearts_chip;
    @BindView(R.id.tv_hearts_integer)
    TextView tv_hearts_integer;

    @BindView(R.id.rl_clubs_chip)
    RelativeLayout rl_clubs_chip;
    @BindView(R.id.cv_clubs_chip)
    ChipView cv_clubs_chip;
    @BindView(R.id.tv_clubs_integer)
    TextView tv_clubs_integer;

    @BindView(R.id.rl_diamonds_chip)
    RelativeLayout rl_diamonds_chip;
    @BindView(R.id.cv_diamonds_chip)
    ChipView cv_diamonds_chip;
    @BindView(R.id.tv_diamonds_integer)
    TextView tv_diamonds_integer;

    @BindView(R.id.rl_king_chip)
    RelativeLayout rl_king_chip;
    @BindView(R.id.cv_king_chip)
    ChipView cv_king_chip;
    @BindView(R.id.tv_king_integer)
    TextView tv_king_integer;
    @BindView(R.id.rl_table)
    RelativeLayout rl_table;
    @BindView(R.id.iv_jet_player)
    ImageView iv_jet_player;
    @BindView(R.id.ll_poker)
    LinearLayout ll_poker;

    @BindView(R.id.rl_video_select1)
    RelativeLayout rl_video_select1;
    @BindView(R.id.iv_video_select1)
    ImageView iv_video_select1;

    private GestureDetector gestureDetector;
    private Baccarat baccarat;
    private Context mContext;
    private String roomId;
    private Socket mSocket;
    private int playerIng = 0, bankerIng, tieIng, playerPairIng = 0, bankerPairIng = 0;//实时下注积分
    private int playerScore = 0, bankerScore = 0, tieScore = 0, playerPairScore = 0, bankerPairScore = 0;//当前局每个位置下注积分
    private int perPlayer = 0, perBanker = 0, perTie = 0, perPlayerPair = 0, perBankerPair = 0;//每局每个位置下注总积分
    private double currentMoney;
    private boolean isChip = false;//默认未保存下注记录
    private int isChipSuccess = 0;//默认0下注不成功 1下注成功
    private boolean isSame = false;//默认不续押
    private boolean isAllRemain = false;//默认未全押
    public List<Integer> betLimitList = new ArrayList<>();
    public List<Integer> maxBetList = new ArrayList<>();
    public List<Integer> minBetList = new ArrayList<>();
    public List<Integer> currentBetScore = new ArrayList<>();
    private Map<Integer, Integer> chipScore = new HashMap<>();//0-黑桃 1-红桃 2-梅花 3-方块 4-王
    private List<Integer> boardMessageList = new ArrayList<>();//原始数据
    private List<String> cardList = new ArrayList<>();
    private DisplayMetrics displayMetrics;
    private int noBetCount = 0;
    private int betSecond;
    private int isPeopleNum = -1;
    private int betsRemind = -1;

    //3.8 3.8 4.0.4.0 20 黑红梅方王
    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = SinglePickLiveActivity.this;
        tv_nickname.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_VIDEO_PLAYING, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.WHERE_SETED, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, 0);//进入房间未下注状态
        tv_game_name.setText(getResources().getString(R.string.single_pick_game));
        roomId = getIntent().getStringExtra("roomId");
        betSecond = getIntent().getIntExtra("betSecond", 25);
        SinglePickLiveSocketController.getInstance().init(this);
        SinglePickLiveSocketController.getInstance().copyWidget(boardMessageList, pcac, roomId);

        SinglePickLiveSocketController.getInstance().connectSocket();
        EventBus.getDefault().register(this);

        SinglePickLiveController.getInstance().init(this);
        SinglePickLiveController.getInstance().copyWidget(rv_chip);
        SinglePickLiveInTimeController.getInstance().init(this);
        SinglePickLiveInTimeController.getInstance().copyWidget(rl_in_time_layout, rl_content, rl_just_in_time, rl_road,
                gv_left, rl_statistic,
                rl_ex_in, iv_ex,
                ll_back, tv_game_name, rl_table);

        baccarat = (Baccarat) getIntent().getSerializableExtra("baccarat");
        SinglePickLiveAnimControler.getInstance().init(this);
        SinglePickLiveAnimControler.getInstance().copyWidget(tv_table_limits, rl_in_time_layout, iv_ex, rl_video_select, iv_video_select, sv, root_video, ll_back, iv_bac_set,
                baccarat,rl_video_select1,iv_video_select1);

        gestureDetector = new GestureDetector(mContext, SinglePickLiveInTimeController.getInstance().new MyGestureListener(this));
        initEvent();

        displayMetrics = mContext.getResources().getDisplayMetrics();
        LogUtil.i("displayMetrics.widthPixels=" + displayMetrics.widthPixels);
        setNav(View.INVISIBLE);

    }

    private void setNav(int visible) {
        ll_back.setVisibility(visible);
        tv_game_name.setVisibility(visible);
        rl_table.setVisibility(visible);
    }

    private void initEvent() {
        //  2018/11/28 庄-黑桃 闲-红桃 和-梅花 庄对-方块 闲对-王
        rl_spade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectChipNumber = SharedPreUtil.getInstance(mContext).getInt(Constant.SELECT_CHIP_NUMBER);
                bankerScore += selectChipNumber;
                perBanker += selectChipNumber;
                if (Double.parseDouble(tv_money.getText().toString()) < 10) {
                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.balance_not_enough));
                    return;
                }
                int _currentMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (_currentMoney < selectChipNumber) {
                    if (isAllRemain) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        return;
                    }
//                    _currentMoney = _currentMoney - (total - selectChipNumber);
                    if (_currentMoney < selectChipNumber && !isAllRemain) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_spade_chip.addChip(integer);
                        }
                        rl_spade_chip.setVisibility(View.VISIBLE);
                        int currentValue = perBanker - selectChipNumber + Math.abs(_currentMoney);
                        tv_spade_integer.setText(String.valueOf(currentValue));
                        chipScore.put(0, (bankerScore - selectChipNumber + _currentMoney));
                        rl_spade_chip.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_spade_chip.getLayoutParams();
                        if (cv_spade_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_spade_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_spade_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_spade_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_spade_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_spade_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_spade_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_spade_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_spade_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_spade_integer.setLayoutParams(tvParams);
                    }
                    emitChip();
                } else {
                    if (maxBetList.size() > 0 && (bankerScore > maxBetList.get(0) || perBanker > maxBetList.get(0))) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
                        return;
                    }
                    if (!isSame) {
                        chipScore.put(0, bankerScore);//每局下注分数
                    } else {
                        chipScore.put(0, perBanker);//续押分数
                    }
                    if (cv_spade_chip.getChipNumSize() < 15) {
                        rl_spade_chip.setVisibility(View.VISIBLE);
                        cv_spade_chip.addChip(selectChipNumber);
                        tv_spade_integer.setText(String.valueOf(perBanker));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_spade_chip.getLayoutParams();
                        if (cv_spade_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_spade_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_spade_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_spade_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_spade_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_spade_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_spade_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_spade_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_spade_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_spade_integer.setLayoutParams(tvParams);
                    }
                    emitChip();
                }
            }
        });
        rl_hearts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectChipNumber = SharedPreUtil.getInstance(mContext).getInt(Constant.SELECT_CHIP_NUMBER);
                playerScore += selectChipNumber;
                perPlayer += selectChipNumber;
                if (Double.parseDouble(tv_money.getText().toString()) < 10) {
                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.balance_not_enough));
                    return;
                }
                int _currentMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (_currentMoney < selectChipNumber) {
                    if (isAllRemain) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        return;
                    }
                    if (_currentMoney < selectChipNumber && !isAllRemain) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_spade_chip.addChip(integer);
                        }
                        rl_hearts_chip.setVisibility(View.VISIBLE);
                        int currentValue = perPlayer - selectChipNumber + Math.abs(_currentMoney);
                        tv_hearts_integer.setText(String.valueOf(currentValue));
                        chipScore.put(1, (playerScore - selectChipNumber + _currentMoney));
                        rl_hearts_chip.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_hearts_chip.getLayoutParams();
                        if (cv_hearts_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_hearts_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_hearts_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_hearts_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_hearts_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_hearts_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_hearts_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_hearts_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_hearts_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_hearts_integer.setLayoutParams(tvParams);
                    }
                    emitChip();
                } else {
                    if (maxBetList.size() > 0 && (playerScore > maxBetList.get(0) || perPlayer > maxBetList.get(0))) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
                        return;
                    }
                    if (!isSame) {
                        chipScore.put(1, playerScore);//每局下注分数
                    } else {
                        chipScore.put(1, perPlayer);//续押分数
                    }
                    if (cv_hearts_chip.getChipNumSize() < 15) {
                        rl_hearts_chip.setVisibility(View.VISIBLE);
                        cv_hearts_chip.addChip(selectChipNumber);
                        tv_hearts_integer.setText(String.valueOf(perPlayer));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_hearts_chip.getLayoutParams();
                        if (cv_hearts_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_hearts_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_hearts_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_hearts_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_hearts_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_hearts_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_hearts_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_hearts_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_hearts_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_hearts_integer.setLayoutParams(tvParams);
                    }
                    emitChip();
                }
            }
        });
        rl_clubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectChipNumber = SharedPreUtil.getInstance(mContext).getInt(Constant.SELECT_CHIP_NUMBER);
                tieScore += selectChipNumber;
                perTie += selectChipNumber;
                if (Double.parseDouble(tv_money.getText().toString()) < 10) {
                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.balance_not_enough));
                    return;
                }
                int _currentMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (_currentMoney < selectChipNumber) {
                    if (isAllRemain) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        return;
                    }
                    if (_currentMoney < selectChipNumber && !isAllRemain) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_clubs_chip.addChip(integer);
                        }
                        rl_clubs_chip.setVisibility(View.VISIBLE);
                        int currentValue = perTie - selectChipNumber + Math.abs(_currentMoney);
                        tv_clubs_integer.setText(String.valueOf(currentValue));
                        chipScore.put(2, (tieScore - selectChipNumber + _currentMoney));
                        rl_clubs_chip.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_clubs_chip.getLayoutParams();
                        if (cv_clubs_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_clubs_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_clubs_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_clubs_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_clubs_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_clubs_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_clubs_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_clubs_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_clubs_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_clubs_integer.setLayoutParams(tvParams);
                    }
                    emitChip();
                } else {
                    if (maxBetList.size() > 0 && (tieScore > maxBetList.get(0) || perTie > maxBetList.get(0))) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
                        return;
                    }
                    if (!isSame) {
                        chipScore.put(2, tieScore);//每局下注分数
                    } else {
                        chipScore.put(2, perTie);//续押分数
                    }
                    if (cv_clubs_chip.getChipNumSize() < 15) {
                        rl_clubs_chip.setVisibility(View.VISIBLE);
                        cv_clubs_chip.addChip(selectChipNumber);
                        tv_clubs_integer.setText(String.valueOf(perTie));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_clubs_chip.getLayoutParams();
                        if (cv_clubs_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_clubs_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_clubs_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_clubs_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_clubs_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_clubs_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_clubs_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_clubs_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_clubs_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_clubs_integer.setLayoutParams(tvParams);
                    }
                    emitChip();
                }
            }
        });
        rl_diamonds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectChipNumber = SharedPreUtil.getInstance(mContext).getInt(Constant.SELECT_CHIP_NUMBER);
                bankerPairScore += selectChipNumber;
                perBankerPair += selectChipNumber;
                if (Double.parseDouble(tv_money.getText().toString()) < 10) {
                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.balance_not_enough));
                    return;
                }
                int _currentMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (_currentMoney < selectChipNumber) {
                    if (isAllRemain) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        return;
                    }
                    if (_currentMoney < selectChipNumber && !isAllRemain) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_diamonds_chip.addChip(integer);
                        }
                        rl_diamonds_chip.setVisibility(View.VISIBLE);
                        int currentValue = perBankerPair - selectChipNumber + Math.abs(_currentMoney);
                        tv_diamonds_integer.setText(String.valueOf(currentValue));
                        chipScore.put(3, (bankerPairScore - selectChipNumber + _currentMoney));
                        rl_diamonds_chip.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_diamonds_chip.getLayoutParams();
                        if (cv_diamonds_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_diamonds_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_diamonds_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_diamonds_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_diamonds_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_diamonds_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_diamonds_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_diamonds_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_diamonds_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_diamonds_integer.setLayoutParams(tvParams);
                    }
                    emitChip();
                } else {
                    if (maxBetList.size() > 0 && (bankerPairScore > maxBetList.get(0) || perBankerPair > maxBetList.get(0))) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
                        return;
                    }
                    if (!isSame) {
                        chipScore.put(3, bankerPairScore);//每局下注分数
                    } else {
                        chipScore.put(3, perBankerPair);//续押分数
                    }
                    if (cv_diamonds_chip.getChipNumSize() < 15) {
                        rl_diamonds_chip.setVisibility(View.VISIBLE);
                        cv_diamonds_chip.addChip(selectChipNumber);
                        tv_diamonds_integer.setText(String.valueOf(perBankerPair));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_diamonds_chip.getLayoutParams();
                        if (cv_diamonds_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_diamonds_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_diamonds_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_diamonds_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_diamonds_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_diamonds_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_diamonds_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_diamonds_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_diamonds_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_diamonds_integer.setLayoutParams(tvParams);
                    }
                    emitChip();
                }
            }
        });
        rl_king.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectChipNumber = SharedPreUtil.getInstance(mContext).getInt(Constant.SELECT_CHIP_NUMBER);
                playerPairScore += selectChipNumber;
                perPlayerPair += selectChipNumber;
                if (Double.parseDouble(tv_money.getText().toString()) < 10) {
                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.balance_not_enough));
                    return;
                }
                int _currentMoney = Integer.parseInt(tv_money.getText().toString().split("\\.")[0]);
                if (_currentMoney < selectChipNumber) {
                    if (isAllRemain) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        return;
                    }
                    if (_currentMoney < selectChipNumber && !isAllRemain) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_all_remain));
                        isAllRemain = true;
                        List<Integer> _playerChip = new ArrayList<>();
                        _playerChip.addAll(BaccaratUtil.addChip(Math.abs(_currentMoney)));
                        for (Integer integer : _playerChip) {
                            cv_king_chip.addChip(integer);
                        }
                        rl_king_chip.setVisibility(View.VISIBLE);
                        int currentValue = perPlayerPair - selectChipNumber + Math.abs(_currentMoney);
                        tv_king_integer.setText(String.valueOf(currentValue));
                        chipScore.put(4, (playerPairScore - selectChipNumber + _currentMoney));
                        rl_king_chip.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_king_chip.getLayoutParams();
                        if (cv_king_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_king_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_king_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_king_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_king_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_king_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_king_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_king_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_king_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_king_integer.setLayoutParams(tvParams);
                    }
                    emitChip();
                } else {
                    if (maxBetList.size() > 0 && (playerPairScore > maxBetList.get(0) || perPlayerPair > maxBetList.get(0))) {
                        ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
                        return;
                    }
                    if (!isSame) {
                        chipScore.put(4, playerPairScore);//每局下注分数
                    } else {
                        chipScore.put(4, perPlayerPair);//续押分数
                    }
                    if (cv_king_chip.getChipNumSize() < 15) {
                        rl_king_chip.setVisibility(View.VISIBLE);
                        cv_king_chip.addChip(selectChipNumber);
                        tv_king_integer.setText(String.valueOf(perPlayerPair));

                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_king_chip.getLayoutParams();
                        if (cv_king_chip.getChipNumSize() < 2) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit25);
                        } else if (cv_king_chip.getChipNumSize() < 4) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit30);
                        } else if (cv_king_chip.getChipNumSize() < 6) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit34);
                        } else if (cv_king_chip.getChipNumSize() < 8) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit38);
                        } else if (cv_king_chip.getChipNumSize() < 10) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit42);
                        } else if (cv_king_chip.getChipNumSize() < 12) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit46);
                        } else if (cv_king_chip.getChipNumSize() < 14) {
                            layoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.unit50);
                        }
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        rl_king_chip.setLayoutParams(layoutParams);
                        RelativeLayout.LayoutParams tvParams = (RelativeLayout.LayoutParams) tv_king_integer.getLayoutParams();
                        tvParams.addRule(RelativeLayout.ABOVE);
                        tv_king_integer.setLayoutParams(tvParams);
                    }
                    emitChip();
                }
            }
        });
    }

    private void emitChip() {
        mSocket = SinglePickLiveSocketController.getInstance().getSocket();
        if (mSocket != null && mSocket.connected()) {
            for (int i = 0; i < 5; i++) {
                if (chipScore.get(i) == null) {
                    chipScore.put(i, 0);
                }
            }
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK, chipScore.get(0));
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY, chipScore.get(1));
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_TIE, chipScore.get(2));
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK_PAIR, chipScore.get(3));
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY_PAIR, chipScore.get(4));
            LogUtil.i("socket_command emitChip chipScore.get(0)" + chipScore.get(0) + " chipScore.get(1)=" + chipScore.get(1) + " chipScore.get(2)=" + chipScore.get(2) +
                    " chipScore.get(3)=" + chipScore.get(3) + " chipScore.get(4)=" + chipScore.get(4) + " chipScore.get(5)=" + chipScore.get(5));
            mSocket.emit("dobet", "{'bets':[" + chipScore.get(0) + "," + chipScore.get(1) + "," + chipScore.get(2) + "," + chipScore.get(3) + "," + chipScore.get(4) + "]}");
        }
    }

    /*
     * 进入房间
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBoardMessage(final ObjectEvent.BoardMessageEvent event) {
        tv_money.setText(event.goldCoins);
        betSecond = getIntent().getIntExtra("betSecond", 25);
        isPeopleNum = event.isPeopleNum;
        betsRemind = event.betsRemind;
        betLimitList.clear();
        maxBetList.clear();
        minBetList.clear();
        cardList.clear();
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
//        boardMessageList.clear();
//        boardMessageList.addAll(event.boardMessageList);
        cardList.addAll(event.cardList);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BET_LIMIT, betLimitList.toString());
        SharedPreUtil.getInstance(mContext).saveParam(Constant.MAX_BET, maxBetList.toString());
        SharedPreUtil.getInstance(mContext).saveParam(Constant.MIN_BET, minBetList.toString());
        if (!event.state.equals(Constant.BACCARAT_BET)) {
            onClick(false);
            iv_jet_player.setBackgroundResource(0);
            ll_poker.setVisibility(View.GONE);
        }
        if (event.boardMessageList.size() > 0) {
            tv_game_name.setText(mContext.getResources().getString(R.string.single_pick_game) + event.roomName);
            initMessages();
            Algorithm.getInstance().drawSinglePick(event.cardList, gv_left, mContext, tv_game_name);
        }
        String _1 = "0";
        String _2 = "0";
        String _3 = "0";
        String _4 = "0";
        String _5 = "0";
        if (isPeopleNum == 0) {
            _1 = String.valueOf(bankerIng);
            _2 = String.valueOf(playerIng);
            _3 = String.valueOf(tieIng);
            _4 = String.valueOf(playerPairIng);
            _5 = String.valueOf(bankerPairIng);
        } else if (isPeopleNum == 1) {
            _1 = String.valueOf(bankerIng) + "/" + 0;
            _2 = String.valueOf(playerIng) + "/" + 0;
            _3 = String.valueOf(tieIng) + "/" + 0;
            _4 = String.valueOf(playerPairIng) + "/" + 0;
            _5 = String.valueOf(bankerPairIng) + "/" + 0;
        }
        tv_spade_date.setText(_1);
        tv_hearts_date.setText(_2);
        tv_clubs_date.setText(_3);
        tv_diamonds_date.setText(_4);
        tv_king_date.setText(_5);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initSound();//在低端机上 加载assets资源，超级耗时
            }
        }, 200);
    }

    /*
     * 状态 BET:下注阶段 RESULT:开牌阶段 OVER:清算阶段 WAITTING:同步识别端
     * */
    List<String> _calPlayer = new ArrayList<>();
    List<String> _calBanker = new ArrayList<>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventState(final ObjectEvent.StateEvent event) {
        if (event.state.equals("BET")) {
            iv_jet_player.setBackgroundResource(0);
            ll_poker.setVisibility(View.GONE);
            onClick(true);
            currentBetScore.clear();
            currentBetScore.add(0);
            currentBetScore.add(0);
            currentBetScore.add(0);
            currentBetScore.add(0);
            currentBetScore.add(0);
            chipScore.put(0, 0);
            chipScore.put(1, 0);
            chipScore.put(2, 0);
            chipScore.put(3, 0);
            chipScore.put(4, 0);
            cv_spade_chip.addChip(0);
            cv_hearts_chip.addChip(0);
            cv_clubs_chip.addChip(0);
            cv_diamonds_chip.addChip(0);
            cv_king_chip.addChip(0);
            perBanker = 0;
            perPlayer = 0;
            perTie = 0;
            perBankerPair = 0;
            perPlayerPair = 0;
            isAllRemain = false;//恢复下注所有金额
            currentMoney = Double.parseDouble(tv_money.getText().toString());
            //先隐藏
            rl_spade_chip.setVisibility(View.GONE);
            rl_hearts_chip.setVisibility(View.GONE);
            rl_clubs_chip.setVisibility(View.GONE);
            rl_diamonds_chip.setVisibility(View.GONE);
            rl_king_chip.setVisibility(View.GONE);
            ToastUtil.getInstance().showToast(mContext, mContext.getResources().getString(R.string.bet_start));
            isChipSuccess = 0;//新一局 代表未下注成功
            SoundPoolUtil.getInstance().play("50");//已开局，请下注
            Constant.SEND_CARD = betSecond;
            pcac.setAllTime(Constant.SEND_CARD);
            pcac.setTargetPercent(0);
            pcac.reInitView();
//            pcac.setCurrentPercent(event.second);
//            pcac.setCurrentAngle(event.second * (360 / 15));
        } else if (event.state.equals("RESULT")) {
            ll_poker.setVisibility(View.VISIBLE);
            onClick(false);
            ToastUtil.getInstance().showToast(mContext, mContext.getResources().getString(R.string.bet_stop));
            SoundPoolUtil.getInstance().play("52");//停止下注
            //清空处理
            _calPlayer.clear();
            _calBanker.clear();
            if(betsRemind==1){
                outGame();
            }
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_TIE, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_PLAY_PAIR, 0);
            SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_BANK_PAIR, 0);

        } else if (event.state.equals("OVER")) {
            onClick(false);
        } else if (event.state.equals("WAITTING")) {
            onClick(false);
        } else if (event.state.equals(Constant.BACCARAT_INNINGS_END)) {//当前轮结束
            iv_jet_player.setBackgroundResource(0);
            ll_poker.setVisibility(View.GONE);
            ToastUtil.showToast(mContext, Constant.BACCARAT_INNINGS_END);
            pcac.setCurrentPercent(-1);//当前轮结束  没有改为  洗牌中
            pcac.update();//重新刷新一下就可以啦
            String _1 = 0 + "/" + 0;
            String _2 = 0 + "/" + 0;
            String _3 = 0 + "/" + 0;
            String _4 = 0 + "/" + 0;
            String _5 = 0 + "/" + 0;
            tv_spade_date.setText(_1);
            tv_hearts_date.setText(_2);
            tv_clubs_date.setText(_3);
            tv_diamonds_date.setText(_4);
            tv_king_date.setText(_5);
        } else if (event.state.equals(Constant.BACCARAT_CREATED)) {//新的一轮开始
            iv_jet_player.setBackgroundResource(0);
            ll_poker.setVisibility(View.GONE);
            boardMessageList.clear();
            initMessages();
            Algorithm.getInstance().drawSinglePick(cardList, gv_left, mContext, tv_game_name);
        }
    }

    /*
     * 初始化牌局(牌局的下注积分数 0黑桃 1红桃 2梅花 3方块 4王)
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventBetRecord(final ObjectEvent.BetRecordEvent event) {
        if (event.scoreList.size() > 0) {
            playerIng = event.scoreList.get(1);
            bankerIng = event.scoreList.get(0);
            tieIng = event.scoreList.get(2);
            playerPairIng = event.scoreList.get(3);
            bankerPairIng = event.scoreList.get(4);
            String _1 = "0";
            String _2 = "0";
            String _3 = "0";
            String _4 = "0";
            String _5 = "0";
            if (isPeopleNum == 0) {
                _1 = String.valueOf(bankerIng);
                _2 = String.valueOf(playerIng);
                _3 = String.valueOf(tieIng);
                _4 = String.valueOf(playerPairIng);
                _5 = String.valueOf(bankerPairIng);
            } else if (isPeopleNum == 1) {
                _1 = String.valueOf(bankerIng) + "/" + event.betPeopleList.get(0);
                _2 = String.valueOf(playerIng) + "/" + event.betPeopleList.get(1);
                _3 = String.valueOf(tieIng) + "/" + String.valueOf(event.betPeopleList.get(2));
                _4 = String.valueOf(playerPairIng) + "/" + String.valueOf(event.betPeopleList.get(3));
                _5 = String.valueOf(bankerPairIng) + "/" + String.valueOf(event.betPeopleList.get(4));
            }
            tv_spade_date.setText(_1);
            tv_hearts_date.setText(_2);
            tv_clubs_date.setText(_3);
            tv_diamonds_date.setText(_4);
            tv_king_date.setText(_5);
            currentBetScore.clear();
            currentBetScore.addAll(event.scoreList);
        }
    }

    /*
     * 一局结束，将这局结果放入珠路 04K57-KH8WG-7KADL-HZZAA
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventResult(final ObjectEvent.ResultEvent event) {
        if (event.win != -1) {
            cardList.add(event.card);
            if (boardMessageList.size() + 1 == event.round) {
                boardMessageList.add(-1);
                boardMessageList.set(event.round - 1, event.win);//
            }
            ll_poker.setVisibility(View.VISIBLE);
            iv_jet_player.setBackgroundResource(BaccaratUtil.switchCard(event.card));
            initMessages();
            Algorithm.getInstance().drawSinglePick(cardList, gv_left, mContext, tv_game_name);
        }
    }

    /*
     * 发牌 黑桃 红桃 草花 方块 1,2,3,4 10-a j-b q-c k-d
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventSwitchResult(final ObjectEvent.SwitchResultEvent event) {
        ll_poker.setVisibility(View.VISIBLE);
        iv_jet_player.setBackgroundResource(BaccaratUtil.switchCard(event.card));
    }

    /**
     * 下注成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventChip(final ObjectEvent.ChipEvent event) {
        chipScore.put(0, 0);
        chipScore.put(1, 0);
        chipScore.put(2, 0);
        chipScore.put(3, 0);
        chipScore.put(4, 0);
        playerPairScore = 0;
        bankerPairScore = 0;
        playerScore = 0;
        tieScore = 0;
        bankerScore = 0;
        isSame = false;
        tv_money.setText(event.availableAmount);
        perPlayer = event.playScore;
        perBanker = event.bankScore;
        perTie = event.tieScore;
        perPlayerPair = event.playPairScore;
        perBankerPair = event.bankPairScore;
        currentBetScore.set(0, perPlayer);
        currentBetScore.set(1, perBanker);
        currentBetScore.set(2, perTie);
        currentBetScore.set(3, perPlayerPair);
        currentBetScore.set(4, perBankerPair);

        LogUtil.i("socket_command 下注成功 chipScore.get(0)" + chipScore.get(0) + " chipScore.get(1)=" + chipScore.get(1) + " chipScore.get(2)=" + chipScore.get(2) +
                " chipScore.get(3)=" + chipScore.get(3) + " chipScore.get(4)=" + chipScore.get(4) + " chipScore.get(5)=" + chipScore.get(5));
        SoundPoolUtil.getInstance().play("51");//下注成功
//        isChipClick(false, false, R.drawable.button_bet_disable_bg, R.drawable.button_bet_disable_bg, R.string.confirm);
        isChipSuccess = 1;//当前局下注成功 就不能中途退出房间
        noBetCount=0;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, isChipSuccess);
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

    /**
     * 超过限红
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventLimitBet(final ObjectEvent.LimitBetEvent event) {
        if (event.limitBetScore > 0) {
            chipScore.put(0, 0);
            chipScore.put(1, 0);
            chipScore.put(2, 0);
            chipScore.put(3, 0);
            chipScore.put(4, 0);
            isAllRemain = false;
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

    /**
     * 下注
     *
     * @param clickable true 可下注
     */
    private void onClick(boolean clickable) {
        rl_spade.setClickable(clickable);
        rl_hearts.setClickable(clickable);
        rl_clubs.setClickable(clickable);
        rl_diamonds.setClickable(clickable);
        rl_king.setClickable(clickable);
    }

    private void initMessages() {
        int spade = 0;
        int hearts = 0;
        int clubs = 0;
        int diamonds = 0;
        int king = 0;

        if (boardMessageList.size() > 0) {
            for (int j = 0; j < boardMessageList.size(); j++) {
                if (boardMessageList.get(j) == 0) {
                    spade++;
                }
                if (boardMessageList.get(j) == 1) {
                    hearts++;
                }
                if (boardMessageList.get(j) == 2) {
                    clubs++;
                }
                if (boardMessageList.get(j) == 3) {
                    diamonds++;
                }
                if (boardMessageList.get(j) == 4) {
                    king++;
                }
            }
        } else {
            spade = 0;
            hearts = 0;
            clubs = 0;
            diamonds = 0;
            king = 0;
        }
        tv_spade_count.setText(String.valueOf(spade));
        tv_hearts_count.setText(String.valueOf(hearts));
        tv_clubs_count.setText(String.valueOf(clubs));
        tv_diamonds_count.setText(String.valueOf(diamonds));
        tv_king_count.setText(String.valueOf(king));
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
//                new android.os.Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        SoundPoolUtil.getInstance().play("49");
//                    }
//                }, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            SinglePickLiveAnimControler.getInstance().initBacDataVideo(baccarat);
        }
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
    }
}