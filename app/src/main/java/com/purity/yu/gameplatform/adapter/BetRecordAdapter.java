package com.purity.yu.gameplatform.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.base.CommonRecyclerViewAdapter;
import com.gangbeng.basemodule.base.CommonRecyclerViewHolder;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.entity.Records;
import com.purity.yu.gameplatform.utils.BaccaratUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BetRecordAdapter extends CommonRecyclerViewAdapter<Records> {
    private Context mContext;
    private List<Records> recordsList;
    private List<String> betNo = new ArrayList<>();
    private int show = 0;

    public BetRecordAdapter(Context context, List<Records> data) {
        super(context, data);
        this.mContext = context;
        this.recordsList = data;
    }

    @Override
    public void convert(CommonRecyclerViewHolder h, final Records entity, final int position) {
        LogUtil.i(" BetRecordAdapter convert=" + recordsList.size());
        final LinearLayout ll_title = h.getView(R.id.ll_title);
        final LinearLayout ll_poker_bac = h.getView(R.id.ll_poker_bac);
        final LinearLayout ll_poker_dt = h.getView(R.id.ll_poker_dt);
        final TextView tv_bill_no = h.getView(R.id.tv_bill_no);
        final TextView tv_bet_time = h.getView(R.id.tv_bet_time);
        final TextView tv_game_code = h.getView(R.id.tv_game_code);
        final TextView tv_play_type = h.getView(R.id.tv_play_type);
        final TextView tv_total_bets = h.getView(R.id.tv_total_bets);
        final TextView tv_dividend = h.getView(R.id.tv_dividend);

        RelativeLayout rl_jet_player_layout = h.getView(R.id.rl_jet_player_layout);
        RelativeLayout rl_jet_player = h.getView(R.id.rl_jet_player);
        TextView tv_jet_player_value = h.getView(R.id.tv_jet_player_value);
        ImageView iv_jet_player_01 = h.getView(R.id.iv_jet_player_01);
        ImageView iv_jet_player_02 = h.getView(R.id.iv_jet_player_02);
        ImageView iv_jet_player_03 = h.getView(R.id.iv_jet_player_03);

        RelativeLayout rl_jet_banker_layout = h.getView(R.id.rl_jet_banker_layout);
        RelativeLayout rl_jet_banker = h.getView(R.id.rl_jet_banker);
        TextView tv_jet_banker_value = h.getView(R.id.tv_jet_banker_value);
        ImageView iv_jet_banker_01 = h.getView(R.id.iv_jet_banker_01);
        ImageView iv_jet_banker_02 = h.getView(R.id.iv_jet_banker_02);
        ImageView iv_jet_banker_03 = h.getView(R.id.iv_jet_banker_03);

        RelativeLayout rl_jet_player_layout_dt = h.getView(R.id.rl_jet_player_layout_dt);
        RelativeLayout rl_jet_player_dt = h.getView(R.id.rl_jet_player_dt);
        TextView tv_jet_player_value_dt = h.getView(R.id.tv_jet_player_value_dt);
        ImageView iv_jet_player_01_dt = h.getView(R.id.iv_jet_player_01_dt);

        RelativeLayout rl_jet_banker_layout_dt = h.getView(R.id.rl_jet_banker_layout_dt);
        RelativeLayout rl_jet_banker_dt = h.getView(R.id.rl_jet_banker_dt);
        TextView tv_jet_banker_value_dt = h.getView(R.id.tv_jet_banker_value_dt);
        ImageView iv_jet_banker_01_dt = h.getView(R.id.iv_jet_banker_01_dt);

        List<String> bankCardList = new ArrayList<>();
        List<String> playCardList = new ArrayList<>();
        List<String> calBanker = new ArrayList<>();
        List<String> calPlayer = new ArrayList<>();
        int bankerScore = 0;
        int playerScore = 0;

        bankCardList.clear();
        playCardList.clear();
        if (!TextUtils.isEmpty(entity.bankCard)) {
            bankCardList.addAll(string2List(entity.bankCard));
        }
        if (!TextUtils.isEmpty(entity.playCard)) {
            playCardList.addAll(string2List(entity.playCard));
        }

        if (bankCardList.size() == 3) {
            calBanker.clear();
            iv_jet_banker_01.setBackgroundResource(BaccaratUtil.switchCard(bankCardList.get(0)));
            iv_jet_banker_02.setBackgroundResource(BaccaratUtil.switchCard(bankCardList.get(1)));
            iv_jet_banker_03.setBackgroundResource(BaccaratUtil.switchCard(bankCardList.get(2)));
            calBanker.add(bankCardList.get(0));
            calBanker.add(bankCardList.get(1));
            calBanker.add(bankCardList.get(2));
            tv_jet_banker_value.setText(String.valueOf(BaccaratUtil.getBacCardScore(calBanker)));
        } else if (bankCardList.size() == 2) {
            calBanker.clear();
            iv_jet_banker_01.setBackgroundResource(BaccaratUtil.switchCard(bankCardList.get(0)));
            iv_jet_banker_02.setBackgroundResource(BaccaratUtil.switchCard(bankCardList.get(1)));
            iv_jet_banker_03.setBackgroundResource(0);
            calBanker.add(bankCardList.get(0));
            calBanker.add(bankCardList.get(1));
            tv_jet_banker_value.setText(String.valueOf(BaccaratUtil.getBacCardScore(calBanker)));
        } else if (bankCardList.size() == 1) {
            calBanker.clear();
            iv_jet_banker_01_dt.setBackgroundResource(BaccaratUtil.switchCard(bankCardList.get(0)));
            calBanker.add(bankCardList.get(0));
            tv_jet_banker_value_dt.setText(String.valueOf(BaccaratUtil.getBacCardScore(calBanker)));
        }
        if (playCardList.size() == 3) {
            calPlayer.clear();
            iv_jet_player_01.setBackgroundResource(BaccaratUtil.switchCard(playCardList.get(0)));
            iv_jet_player_02.setBackgroundResource(BaccaratUtil.switchCard(playCardList.get(1)));
            iv_jet_player_03.setBackgroundResource(BaccaratUtil.switchCard(playCardList.get(2)));
            calPlayer.add(playCardList.get(0));
            calPlayer.add(playCardList.get(1));
            calPlayer.add(playCardList.get(2));
            tv_jet_player_value.setText(String.valueOf(BaccaratUtil.getBacCardScore(calPlayer)));
        } else if (playCardList.size() == 2) {
            calPlayer.clear();
            iv_jet_player_01.setBackgroundResource(BaccaratUtil.switchCard(playCardList.get(0)));
            iv_jet_player_02.setBackgroundResource(BaccaratUtil.switchCard(playCardList.get(1)));
            iv_jet_player_03.setBackgroundResource(0);
            calPlayer.add(playCardList.get(0));
            calPlayer.add(playCardList.get(1));
            tv_jet_player_value.setText(String.valueOf(BaccaratUtil.getBacCardScore(calPlayer)));
        } else if (playCardList.size() == 1) {
            calPlayer.clear();
            iv_jet_player_01_dt.setBackgroundResource(BaccaratUtil.switchCard(playCardList.get(0)));
            calPlayer.add(playCardList.get(0));
            tv_jet_player_value_dt.setText(String.valueOf(BaccaratUtil.getBacCardScore(calPlayer)));
        }
        bankerScore = BaccaratUtil.getBacCardScore(calBanker);
        playerScore = BaccaratUtil.getBacCardScore(calPlayer);
        if (playerScore > bankerScore) {
            rl_jet_player_layout.setBackgroundResource(R.drawable.bg_trans_stroke_yellow);
            rl_jet_player_layout_dt.setBackgroundResource(R.drawable.bg_trans_stroke_yellow);
            rl_jet_player.setBackgroundResource(R.color.yellow);
            rl_jet_player_dt.setBackgroundResource(R.color.yellow);
            rl_jet_banker_layout.setBackgroundResource(0);
            rl_jet_banker_layout_dt.setBackgroundResource(0);
            rl_jet_banker.setBackgroundResource(0);
            rl_jet_banker_dt.setBackgroundResource(0);
        } else if (playerScore < bankerScore) {
            rl_jet_banker_layout.setBackgroundResource(R.drawable.bg_trans_stroke_yellow);
            rl_jet_banker_layout_dt.setBackgroundResource(R.drawable.bg_trans_stroke_yellow);
            rl_jet_banker.setBackgroundResource(R.color.yellow);
            rl_jet_banker_dt.setBackgroundResource(R.color.yellow);
            rl_jet_player_layout.setBackgroundResource(0);
            rl_jet_player_layout_dt.setBackgroundResource(0);
            rl_jet_player.setBackgroundResource(0);
            rl_jet_player_dt.setBackgroundResource(0);
        } else {
            rl_jet_player_layout.setBackgroundResource(0);
            rl_jet_player_layout_dt.setBackgroundResource(0);
            rl_jet_player.setBackgroundResource(0);
            rl_jet_player_dt.setBackgroundResource(0);
            rl_jet_banker_layout.setBackgroundResource(0);
            rl_jet_banker_layout_dt.setBackgroundResource(0);
            rl_jet_banker.setBackgroundResource(0);
            rl_jet_banker_dt.setBackgroundResource(0);
        }

        if (position % 2 == 0) {
            ll_title.setBackgroundResource(R.color.betRecord0);
        } else
            ll_title.setBackgroundResource(R.color.betRecord1);
        tv_total_bets.setText(entity.totalBets);
        double dividend = Double.parseDouble(entity.dividend);
        if (dividend < 0) {
            tv_dividend.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            tv_dividend.setTextColor(mContext.getResources().getColor(R.color.green));
        }
        tv_dividend.setText(entity.dividend);
        tv_bill_no.setText(entity.billNo);
        String time = Util.formatLongToTime(entity.betTime, "yyyy-MM-dd \n HH:mm:ss");
        tv_bet_time.setText(time);
        //todo 押注状态
        Map<String, String> map = new HashMap<>();
        String[] bet_record = entity.betRecord.split(",");
        String[] bet_record_amount = entity.betRecordAmount.split(",");
        for (int i = 0; i < bet_record.length; i++) {
            map.put(bet_record[i], bet_record_amount[i]);
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String _key = "";
            if (entry.getKey().equals("dragon")) {
                _key = mContext.getResources().getString(R.string.dragon);
            } else if (entry.getKey().equals("tiger")) {
                _key = mContext.getResources().getString(R.string.tiger);
            } else if (entry.getKey().equals("tie")) {
                _key = mContext.getResources().getString(R.string.tie);
            } else if (entry.getKey().equals("player")) {
                _key = mContext.getResources().getString(R.string.player);
            } else if (entry.getKey().equals("banker")) {
                _key = mContext.getResources().getString(R.string.banker);
            } else if (entry.getKey().equals("player_pair")) {
                _key = mContext.getResources().getString(R.string.player) + mContext.getResources().getString(R.string.pair);
            } else if (entry.getKey().equals("banker_pair")) {
                _key = mContext.getResources().getString(R.string.banker) + mContext.getResources().getString(R.string.pair);
            } else if (entry.getKey().equals("big")) {
                _key = mContext.getResources().getString(R.string.play_type_big);
            } else if (entry.getKey().equals("small")) {
                _key = mContext.getResources().getString(R.string.play_type_small);
            } else if (entry.getKey().equals("spade")) {
                _key = mContext.getResources().getString(R.string.spade);
            } else if (entry.getKey().equals("heart")) {
                _key = mContext.getResources().getString(R.string.hearts);
            } else if (entry.getKey().equals("club")) {
                _key = mContext.getResources().getString(R.string.clubs);
            } else if (entry.getKey().equals("diamond")) {
                _key = mContext.getResources().getString(R.string.diamonds);
            } else if (entry.getKey().equals("joker")) {
                _key = mContext.getResources().getString(R.string.king);
            }
            sb.append(_key + "(" + entry.getValue() + ") ");

        }
        tv_game_code.setText(sb.toString());
        /**
         *               "bet_record": "player,banker,tie,player_pair,banker_pair,small,big",
         *                 "bet_record_amount": "50,100,50,150,200,100,50",
         */


        if (entity.playType.equals("baccarat")) {
            tv_play_type.setText(mContext.getResources().getString(R.string.play_type_pb));
        }
        if (entity.playType.equals("dragon_tiger")) {
//            tv_play_type.setText(mContext.getResources().getString(R.string.play_type_bb));
            tv_play_type.setText(mContext.getResources().getString(R.string.play_type_dd));
        }
        if (entity.playType.equals("duel")) {
            tv_play_type.setText(mContext.getResources().getString(R.string.single_pick_game));
        }

        if (SharedPreUtil.getInstance(mContext).getInt(Constant.BET0) == 0) {
            if (position == 0) {
                if (entity.playType.equals("baccarat")) {
                    entity.isBacSelect = View.VISIBLE;
                    betNo.add(entity.billNo);
                } else if (entity.playType.equals("dragon_tiger")) {
                    entity.isDtSelect = View.VISIBLE;
                    betNo.add(entity.billNo);
                }
            }
        }
        ll_poker_dt.setVisibility(entity.isDtSelect);
        ll_poker_bac.setVisibility(entity.isBacSelect);
        LogUtil.i("ll_poker_dt.getVisibility()" + ll_poker_dt.getVisibility() + " ll_poker_bac.getVisibility()=" + ll_poker_bac.getVisibility() + " position=" + position);
        ll_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 2;
                SharedPreUtil.getInstance(mContext).saveParam(Constant.BET0, 1);
                if (ll_title.getTag() != null) {
                    flag = Integer.parseInt(ll_title.getTag().toString());
                }
                if (betNo.size() > 0) {
                    int size = betNo.size();
                    for (int i = 0; i < size; i++) {
                        if (entity.billNo.equals(betNo.get(size - 1))) {
                            show = 1;//点击了同一个列表项
                        } else {
                            show = 2;
                        }
                    }
                }
                betNo.add(entity.billNo);
                if (/*entity.playType == 1 || entity.playType == 2 || entity.playType == 3 || */entity.playType.equals("baccarat")) {
                    if (show == 1 && betNo.size() == 2) {
                        ll_title.setTag(1);
                        entity.isBacSelect = View.GONE;
                        ll_poker_bac.setVisibility(entity.isBacSelect);
                        return;
                    }
                    if (show == 1) {
                        if (flag == 1) {
                            ll_title.setTag(2);
                            entity.isBacSelect = View.VISIBLE;
                        } else {
                            ll_title.setTag(1);
                            entity.isBacSelect = View.GONE;
                        }
                        ll_poker_bac.setVisibility(entity.isBacSelect);
                    } else if (show == 2) {
                        for (Records records : recordsList) {
                            records.isBacSelect = View.GONE;
                            records.isDtSelect = View.GONE;
                            LogUtil.i("ll_poker_dt.getVisibility()1=" + ll_poker_dt.getVisibility() + " ll_poker_bac.getVisibility()1=" + ll_poker_bac.getVisibility());
                        }
                        entity.isBacSelect = View.VISIBLE;
                        notifyDataSetChanged();
                    }
                } else if (/*entity.playType == 5 || entity.playType == 4*/entity.playType.equals("dragon_tiger")) {
                    if (show == 1 && betNo.size() == 2) {
                        ll_title.setTag(1);
                        entity.isDtSelect = View.GONE;
                        ll_poker_dt.setVisibility(entity.isDtSelect);
                        return;
                    }
                    if (show == 1) {
                        if (flag == 1) {
                            ll_title.setTag(2);
                            entity.isDtSelect = View.VISIBLE;
                        } else {
                            ll_title.setTag(1);
                            entity.isDtSelect = View.GONE;
                        }
                        ll_poker_dt.setVisibility(entity.isDtSelect);
                    } else if (show == 2) {
                        for (Records records : recordsList) {
                            records.isDtSelect = View.GONE;
                            records.isBacSelect = View.GONE;
                        }
                        entity.isDtSelect = View.VISIBLE;
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private List<String> string2List(String str) {
        List<String> stringList = new ArrayList<>();
        String[] _str = str.split(",");
        for (int i = 0; i < _str.length; i++) {
            stringList.add(_str[i]);
        }
        return stringList;
    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_bet_record;
    }
}