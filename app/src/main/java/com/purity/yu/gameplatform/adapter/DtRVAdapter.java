package com.purity.yu.gameplatform.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.DtLiveActivity;
import com.purity.yu.gameplatform.baccarat.TextCircleView;
import com.purity.yu.gameplatform.baccarat.YHZGridView;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.Hall;
import com.purity.yu.gameplatform.utils.Algorithm;
import com.purity.yu.gameplatform.widget.PercentCircleAntiClockwise;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DtRVAdapter extends RecyclerView.Adapter<DtRVAdapter.ViewHolder> {

    private List<Baccarat> mBaccaratList = new ArrayList<>();
    private List<Hall> mHallList = new ArrayList<>();
    //BET:0下注阶段 RESULT:1开牌阶段 OVER:2清算阶段 WAITTING:3同步识别端
    private Context mContext;
    private boolean isHall = false;//false 只加载一次大厅数据 true 根据状态=2在刷新界面
    private int bankColor = 0;
    private int playColor = 0;
    private int tieColor = 0;

    public DtRVAdapter(Context context, List<Baccarat> baccaratList) {
        this.mBaccaratList = baccaratList;
        this.mContext = context;
        SharedPreUtil.getInstance(mContext).saveParam("times", 0);
    }

    public void update(List<Hall> hallList) {
        this.mHallList.clear();
        this.mHallList.addAll(hallList);
        notifyDataSetChanged();
    }

    @Override
    public DtRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dt, parent, false);
        return new DtRVAdapter.ViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {//解决数据错乱问题，position不对应
        return position;
    }

    @Override
    public void onBindViewHolder(final DtRVAdapter.ViewHolder holder, final int position) {
        scale(holder);
        holder.tv_room.setText(mBaccaratList.get(position).roomName);
        holder.stakeLimitLabel.setText(mBaccaratList.get(position).minBetList.get(0) + "-" + mBaccaratList.get(position).maxBetList.get(0));
        holder.tv_bank.setText(mContext.getResources().getString(R.string.dragon));
        holder.tv_play.setText(mContext.getResources().getString(R.string.tiger));
        holder.tv_tie.setText(mContext.getResources().getString(R.string.tie));
        holder.iv_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Baccarat baccarat = mBaccaratList.get(position);
                Intent intent = new Intent(mContext, DtLiveActivity.class);
                intent.putExtra("baccarat", baccarat);
                intent.putExtra("roomId", mBaccaratList.get(position).roomId);
                intent.putExtra("roomName", mBaccaratList.get(position).roomName);
                intent.putExtra("betSecond", mBaccaratList.get(position).betSecond);
                mContext.startActivity(intent);
//                ((DtListActivity) mContext).finish();
            }
        });
        if (mHallList == null || mHallList.size() == 0) {//可见条目的个数就是要初始化的次数
            return;
        } else {
            int times = SharedPreUtil.getInstance(mContext).getInt("times");
            if (isHall == false && mHallList.size() > 0 /*&& mHallList.size() < position*/) {//初始化 第一次进入大厅 初始化所有数据
                init(holder, position, times);
            } else if (isHall && mHallList.size() > 0 /*&& mHallList.size() < position*/) {//新添加一局
                update(holder, position);
            }
        }
    }

    private void scale(ViewHolder holder) {
        int _rl_5, _ll_2;
        if (holder.rl_5.getWidth() > 0) {
            _rl_5 = holder.rl_5.getWidth();
            _ll_2 = holder.ll_2.getWidth();
            SharedPreUtil.getInstance(mContext).saveParam("_rl_5", _rl_5);
            SharedPreUtil.getInstance(mContext).saveParam("_ll_2", _ll_2);
        } else {
            _rl_5 = SharedPreUtil.getInstance(mContext).getInt("_rl_5");
            _ll_2 = SharedPreUtil.getInstance(mContext).getInt("_ll_2");
        }
        float _value = Float.parseFloat(getScaleX(_rl_5, _ll_2));//空白部分占要拉伸部分的比重
        float scaleX;
        scaleX = _value + 1.00f;
        int difference = _rl_5;
        if (difference > 5) {
            holder.ll_2.setPivotX(0f);
            holder.ll_2.setScaleX(scaleX);
        }
    }

    private String getScaleX(int a, int b) {
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format((float) a / b);
    }

    private void init(ViewHolder holder, int position, int times) {
        times++;
        SharedPreUtil.getInstance(mContext).saveParam("times", times);
        for (int i = 0; i < mHallList.size(); i++) {
            Hall hall = mHallList.get(i);
            //没有数据时也要处理
            if (hall.peopleNum == 1 && hall.peopleNumber > 0) {
                holder.tv_player.setVisibility(View.VISIBLE);
                holder.tv_player.setText(mContext.getResources().getString(R.string.players) + "" + hall.peopleNumber);
            } else {
                holder.tv_player.setVisibility(View.GONE);
            }
            if (hall.boardMessageList.size() > 0 && mBaccaratList.get(position).roomId.equals(hall.roomId)) {
                bankColor = Color.parseColor(hall.color[0]);
                playColor = Color.parseColor(hall.color[1]);
                tieColor = Color.parseColor(hall.color[2]);
                holder.tv_bank.setCirCleColor(bankColor);
                holder.tv_play.setCirCleColor(playColor);
                holder.tv_tie.setCirCleColor(tieColor);
                draw(holder, hall);
                if (hall.state.equals(Constant.BACCARAT_BET)) {
                    holder.pcac.setCurrentPercent(hall.second);
                    Constant.SEND_CARD = mBaccaratList.get(position).betSecond;
                    holder.pcac.setAllTime(Constant.SEND_CARD);
                } else if (hall.state.equals(Constant.BACCARAT_INNINGS_END)) {
                    holder.pcac.setCurrentPercent(-1);
//                    hall.boardMessageList.clear();
//                    draw(holder, hall);
                } else {
                    holder.pcac.setCurrentPercent(0);
                }
            } else if (mBaccaratList.get(position).roomId.equals(hall.roomId)) {
                holder.pcac.setCurrentPercent(-2);
            }
        }
        if (times == mHallList.size()) {//次数=一次同时加载几个房间
            isHall = true;
            SharedPreUtil.getInstance(mContext).saveParam("times", mHallList.size());
        }
    }

    private void draw(ViewHolder holder, Hall hall) {
        List<Integer> boardMessageList = new ArrayList<>();
        boardMessageList.clear();
        boardMessageList.addAll(hall.boardMessageList);
        //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
        // 0庄 1闲 2和 3庄和 4庄和2  5闲和 6闲和2 7庄和3 8闲和3 9和2 10和3  (只存庄和闲，用于大眼路，小路和小强路分析,因为只分析长度空位无关颜色 )
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
        int banker = 0;
        int player = 0;
        int tie = 0;
        if (boardMessageList.size() > 0) {
            for (int j = 0; j < boardMessageList.size(); j++) {
                if (boardMessageList.get(j) == 0 || boardMessageList.get(j) == 3 || boardMessageList.get(j) == 4 || boardMessageList.get(j) == 5) {
                    banker++;
                }
                if (boardMessageList.get(j) == 1 || boardMessageList.get(j) == 6 || boardMessageList.get(j) == 7 || boardMessageList.get(j) == 8) {
                    player++;
                }
                if (boardMessageList.get(j) == 2 || boardMessageList.get(j) == 9 || boardMessageList.get(j) == 10 || boardMessageList.get(j) == 11) {
                    tie++;
                }
            }
        } else {
            banker = 0;
            player = 0;
            tie = 0;
        }

        holder.tv_bank_count.setText(String.valueOf(banker));
        holder.tv_player_count.setText(String.valueOf(player));
        holder.tv_tie_count.setText(String.valueOf(tie));
        Algorithm.getInstance().initBigRoad(boardMessageList, new ArrayList<Integer>(), new ArrayList<Integer>(),
                bigRoadListAll, bigRoadList, bigRoadListShort,
                bigEyeRoadListAll, bigEyeRoadList, bigEyeRoadListShort,
                smallRoadListAll, smallRoadList, smallRoadListShort,
                cockroachRoadListAll, cockroachRoadList, cockroachRoadListShort, mContext, 2);//1房间 2大厅
        Algorithm.getInstance().drawRoad(boardMessageList, bigRoadList, bigEyeRoadList, smallRoadList, cockroachRoadList,
                holder.gv_left, holder.gv_right_top, holder.gv_right_middle, holder.gv_right_bottom_1, holder.gv_right_bottom_2, mContext, 1, bankColor, playColor, tieColor);
    }

    private void update(ViewHolder holder, int position) {
        for (int i = 0; i < mHallList.size(); i++) {
            Hall hall = mHallList.get(i);
            if (hall.boardMessageList.size() > 0 && mBaccaratList.get(position).roomId.equals(hall.roomId) && hall.state.equals(Constant.BACCARAT_WAIT)) {
                draw(holder, hall);
            } else if (hall.boardMessageList.size() == 0 && mBaccaratList.get(position).roomId.equals(hall.roomId)) {
                draw(holder, hall);
                holder.pcac.setCurrentPercent(-2);
            }
            if (hall.state.equals(Constant.BACCARAT_BET) && mBaccaratList.get(position).roomId.equals(hall.roomId)) {//倒计时
                Constant.SEND_CARD = mBaccaratList.get(position).betSecond;
                holder.pcac.setAllTime(Constant.SEND_CARD);
                holder.pcac.setTargetPercent(0);
                holder.pcac.reInitView();
            } else if (hall.state.equals(Constant.BACCARAT_WAIT) || hall.state.equals(Constant.BACCARAT_OVER) && mBaccaratList.get(position).roomId.equals(hall.roomId)) {//结算
                holder.pcac.setCurrentPercent(0);
            } else if (hall.state.equals(Constant.BACCARAT_INNINGS_END) && mBaccaratList.get(position).roomId.equals(hall.roomId)) {//洗牌
                holder.pcac.setCurrentPercent(-1);
                hall.boardMessageList.clear();
                draw(holder, hall);
            }
        }
    }


    @Override
    public int getItemCount() {
        return mBaccaratList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        PercentCircleAntiClockwise pcac;
        YHZGridView gv_left;
        YHZGridView gv_right_top;
        YHZGridView gv_right_middle;
        YHZGridView gv_right_middle_grid;
        YHZGridView gv_right_bottom_1;
        YHZGridView gv_right_bottom_1_grid;
        YHZGridView gv_right_bottom_2;
        YHZGridView gv_right_bottom_2_grid;
        ImageView iv_bg;
        TextView tv_room;
        TextView stakeLimitLabel;
        TextView tv_player;
        TextView tv_bank_count;
        TextView tv_player_count;
        TextView tv_tie_count;
        TextCircleView tv_bank;
        TextCircleView tv_play;
        TextCircleView tv_tie;
        LinearLayout ll_2;
        LinearLayout ll_3;
        LinearLayout ll_4;
        RelativeLayout rl_5;

        public ViewHolder(View itemView) {
            super(itemView);
            pcac = (PercentCircleAntiClockwise) itemView.findViewById(R.id.pcac);
            gv_left = (YHZGridView) itemView.findViewById(R.id.gv_left);
            gv_right_top = (YHZGridView) itemView.findViewById(R.id.gv_right_top);
            gv_right_middle = (YHZGridView) itemView.findViewById(R.id.gv_right_middle);
            gv_right_middle_grid = (YHZGridView) itemView.findViewById(R.id.gv_right_middle_grid);
            gv_right_bottom_1 = (YHZGridView) itemView.findViewById(R.id.gv_right_bottom_1);
            gv_right_bottom_1_grid = (YHZGridView) itemView.findViewById(R.id.gv_right_bottom_1_grid);
            gv_right_bottom_2 = (YHZGridView) itemView.findViewById(R.id.gv_right_bottom_2);
            gv_right_bottom_2_grid = (YHZGridView) itemView.findViewById(R.id.gv_right_bottom_2_grid);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
            tv_room = (TextView) itemView.findViewById(R.id.tv_room);
            stakeLimitLabel = (TextView) itemView.findViewById(R.id.stakeLimitLabel);
            tv_player = (TextView) itemView.findViewById(R.id.tv_player);
            tv_bank_count = (TextView) itemView.findViewById(R.id.tv_bank_count);
            tv_player_count = (TextView) itemView.findViewById(R.id.tv_player_count);
            tv_player = (TextView) itemView.findViewById(R.id.tv_player);
            tv_tie_count = (TextView) itemView.findViewById(R.id.tv_tie_count);
            tv_bank = (TextCircleView) itemView.findViewById(R.id.tv_bank);
            tv_play = (TextCircleView) itemView.findViewById(R.id.tv_play);
            tv_tie = (TextCircleView) itemView.findViewById(R.id.tv_tie);
            ll_2 = itemView.findViewById(R.id.ll_2);
            ll_3 = itemView.findViewById(R.id.ll_3);
            ll_4 = itemView.findViewById(R.id.ll_4);
            rl_5 = itemView.findViewById(R.id.rl_5);
        }
    }
}