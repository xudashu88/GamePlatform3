package com.purity.yu.gameplatform.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.google.gson.Gson;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.baccarat.YHZGridView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.AlgorithmHoly;
import com.purity.yu.gameplatform.utils.BaccaratUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@ContentView(R.layout.dialog_ld)
public class LdRoadActivity extends BaseActivity {
    @BindView(R.id.tv_device)
    public TextView tv_device;
    @BindView(R.id.ld_room)
    public RelativeLayout ld_room;
    @BindView(R.id.tv_back)
    public TextView tv_back;
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
    @BindView(R.id.tv_in_game_total_value)
    public TextView tv_in_game_total_value;
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
    @BindView(R.id.tv_room_name)
    public TextView tv_room_name;
    @BindView(R.id.tv_baccarat_state)
    public TextView tv_baccarat_state;
    @BindView(R.id.tv_min_bet)
    public TextView tv_min_bet;
    @BindView(R.id.tv_max_bet)
    public TextView tv_max_bet;
    @BindView(R.id.tv_pair_total_limit)
    public TextView tv_pair_total_limit;
    @BindView(R.id.tv_tie_total_limit)
    public TextView tv_tie_total_limit;

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

    //等比拉伸
    @BindView(R.id.rl_contain)
    public RelativeLayout rl_contain;
    @BindView(R.id.fl_right_blank)
    public FrameLayout fl_right_blank;
    @BindView(R.id.fl_bottom_blank)
    public FrameLayout fl_bottom_blank;

    public List<Integer> betLimitList = new ArrayList<>();
    public List<Integer> maxBetList = new ArrayList<>();
    public List<Integer> minBetList = new ArrayList<>();
    //计算天牌 只有8和9
    private List<Integer> maxScoreList = new ArrayList<>();//赢家分数

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
    List<List<Integer>> bigEyeRoadList = new ArrayList<>();// 大眼路(展开)
    List<List<Integer>> bigEyeRoadListShort = new ArrayList<>();// 大眼路(缩小)

    //0庄 1闲
    List<List<Integer>> smallRoadListAll = new ArrayList<>();// 小路原始数据
    List<List<Integer>> smallRoadList = new ArrayList<>();// 小路(展开)
    List<List<Integer>> smallRoadListShort = new ArrayList<>();// 小路(缩小)

    //0庄 1闲
    List<List<Integer>> cockroachRoadListAll = new ArrayList<>();// 小强路原始数据
    List<List<Integer>> cockroachRoadList = new ArrayList<>();// 小强路(展开)
    List<List<Integer>> cockroachRoadListShort = new ArrayList<>();// 小强路(缩小)
    private DisplayMetrics displayMetrics;
    private Context mContext;
    private String roomName;
    private int skyCard;

    @Override
    protected void initView(Bundle savedInstanceState) {
        this.mContext = LdRoadActivity.this;
        displayMetrics = mContext.getResources().getDisplayMetrics();
        boardMessageList = getIntent().getIntegerArrayListExtra("boardMessageList");
        maxScoreList = getIntent().getIntegerArrayListExtra("maxScoreList");
        roomName = getIntent().getStringExtra("roomName");
        skyCard = getIntent().getIntExtra("skyCard", 0);
        tv_room_name.setText(roomName);
        tv_in_game_gold_value.setText(String.valueOf(skyCard));
        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.MIN_BET))) {
            String minBet = SharedPreUtil.getInstance(mContext).getString(Constant.MIN_BET);//最小下注
            String betLimit = SharedPreUtil.getInstance(mContext).getString(Constant.BET_LIMIT);//这里取庄闲限红
            int[] minBetInt = new Gson().fromJson(minBet, int[].class);
            int[] betLimitInt = new Gson().fromJson(betLimit, int[].class);
            tv_min_bet.setText(String.valueOf(minBetInt[0]));
            tv_max_bet.setText(String.valueOf(betLimitInt[0]));
            tv_tie_total_limit.setText(String.valueOf(betLimitInt[1]));//和局总限/和的赔率
            tv_pair_total_limit.setText(String.valueOf(betLimitInt[2]));//对子总限/对子的赔率
        }
        //默认全为-1
        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_BIG_EYE,-1);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_SMALL,-1);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.ASK_COCKROACH,-1);
        initMessages();
        LogUtil.i("报错2"+maxScoreList.size()+"boardMessageList="+boardMessageList.size());
        AlgorithmHoly.getInstance().drawRoad(maxScoreList, beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 0);//11列 加一局
        EventBus.getDefault().register(this);
        initEvent();
        ask();
        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.BACCARAT_STATE))) {
            String state = SharedPreUtil.getInstance(mContext).getString(Constant.BACCARAT_STATE);
            if (state.equals(Constant.BACCARAT_BET)) {
                tv_baccarat_state.setText(mContext.getResources().getString(R.string.bet));
            } else if (state.equals(Constant.BACCARAT_RESULT)) {
                tv_baccarat_state.setText("发牌中");
            } else if (state.equals(Constant.BACCARAT_WAIT)) {
                tv_baccarat_state.setText("等待");
            } else if (state.equals(Constant.BACCARAT_INNINGS_END)) {
                tv_baccarat_state.setText("洗牌中");
            } else if (state.equals(Constant.BACCARAT_CREATED)) {
                tv_baccarat_state.setText("新开局");
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initIsometricStretching();
            }
        }, 100);
    }

    private void initIsometricStretching() {
        int right = fl_right_blank.getWidth();
        int bottom = fl_bottom_blank.getHeight();
        int containW = rl_contain.getWidth();
        int containH = rl_contain.getHeight();
        rl_contain.setPivotX(0f);//往右拉伸
        rl_contain.setPivotY(0f);//往下拉伸
        float scaleX = 0.0f;
        float scaleY = 0.0f;
        if (right > 0) {
            scaleX = Float.parseFloat(getScaleX(right, containW));
            scaleX = scaleX + 1.0f;
            if (scaleX < 0.02) {

            } else {
                rl_contain.setScaleX(scaleX);
            }

        }
        if (bottom > 0) {
            scaleY = Float.parseFloat(getScaleX(bottom, containH));
            scaleY = scaleY + 1.0f;
            if (scaleY < 0.02) {

            } else {
                rl_contain.setScaleY(scaleY);
            }

        }
        int _w560 = mContext.getResources().getDimensionPixelOffset(R.dimen.unit560);
        int w = displayMetrics.widthPixels;
        int h = displayMetrics.heightPixels;
        tv_device.setText("sY=" + scaleY + " sX=" + scaleX + " 560d=" + _w560 + " W=" + w + " H=" + h +
                " cW=" + containW + " cH=" + containH + " DPI=" + displayMetrics.densityDpi);
//        tv_baccarat_state.setText("R=" + right + "B=" + bottom + "SX=" + scaleX + "SY=" + scaleY + "C=" + rl_contain.getWidth());
    }

    private String getScaleX(int a, int b) {
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format((float) a / b);
    }

    private String subFloat(int a, float b) {
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format((float) (a - b));
    }

    private void initEvent() {
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ask() {
        boardMessageList1.clear();
        boardMessageList1.addAll(boardMessageList);
        boardMessageList1.add(0);
        LogUtil.i("initBigRoad bigEyeRoadListAll_1="+boardMessageList.size()+" boardMessageList1="+boardMessageList1.size());
        AlgorithmHoly.getInstance().initBigRoad(boardMessageList1, beadRoadList, beadRoadListShort,
                bigRoadListAll, bigRoadList, bigRoadListShort,
                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
                smallRoadListAll, smallRoadList, smallRoadListShort,
                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort, mContext, 1,true,
                iv_ask_bank_1,iv_ask_bank_2,iv_ask_bank_3,iv_ask_play_1,iv_ask_play_2,iv_ask_play_3 );//1房间 2大厅
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventState(final ObjectEvent.StateEvent event) {
        if (event.state.equals("BET")) {
            tv_baccarat_state.setText("下注");
        } else if (event.state.equals("RESULT")) {
            tv_baccarat_state.setText("发牌中");
        } else if (event.state.equals("OVER")) {
            tv_baccarat_state.setText("当局结束");
        } else if (event.state.equals("BACCARAT_WAIT")) {
            tv_baccarat_state.setText("等待");
        } else if (event.state.equals(Constant.BACCARAT_INNINGS_END)) {//当前轮结束
            tv_baccarat_state.setText("洗牌中");
        } else if (event.state.equals(Constant.BACCARAT_CREATED)) {//新的一轮开始
            tv_baccarat_state.setText("新开局");
        }
    }

    /*
     * 一局结束，将这局结果放入珠路 04K57-KH8WG-7KADL-HZZAA
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventResult(final ObjectEvent.ResultEvent event) {
        skyCard = event.skyCard;
        tv_in_game_gold_value.setText(skyCard);
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

            maxScoreList.add(event.resultMaxScore);
            initMessages();
            AlgorithmHoly.getInstance().drawRoad(maxScoreList, beadRoadList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                    gv_bead_road, gv_big_road, gv_right_middle, gv_right_bottom_1, gv_right_bottom_2, mContext, 0);//11列 加一局
            ask();
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
        if (maxScoreList.size() > 0) {
            for (int i = 0; i < maxScoreList.size(); i++) {
                if (maxScoreList.get(i) == 8 || maxScoreList.get(i) == 9) {
                    gold++;
                }
            }
        }

        tv_in_game_total_value.setText(String.valueOf(total));
        tv_in_game_banker_value.setText(String.valueOf(banker));
        tv_in_game_player_value.setText(String.valueOf(player));
        tv_in_game_tie_value.setText(String.valueOf(tie));
        tv_in_game_banker_pair_value.setText(String.valueOf(bankerPair));
        tv_in_game_player_pair_value.setText(String.valueOf(playerPair));

        AlgorithmHoly.getInstance().initBigRoad(boardMessageList, beadRoadList, beadRoadListShort,
                bigRoadListAll, bigRoadList, bigRoadListShort,
                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
                smallRoadListAll, smallRoadList, smallRoadListShort,
                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort, mContext, 1,false,
                iv_ask_bank_1,iv_ask_bank_2,iv_ask_bank_3,iv_ask_play_1,iv_ask_play_2,iv_ask_play_3);//1房间 2大厅
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
