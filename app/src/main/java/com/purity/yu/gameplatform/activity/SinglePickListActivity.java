package com.purity.yu.gameplatform.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.baccarat.YCHGridView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.controler.SinglePickListSocketController;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.Hall;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.Algorithm;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.widget.PercentCircleAntiClockwise;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import avi.AVLoadingIndicatorView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 单挑（富士之尊） //3.8 3.8 4.0.4.0 20 黑红梅方王
 */
@ContentView(R.layout.activity_baccarat_list)
public class SinglePickListActivity extends BaseActivity {
    private static final String TAG = "SinglePickListActivity";
    @BindView(R.id.ll_back)
    public LinearLayout ll_back;
    @BindView(R.id.tv_game_table_num)
    public TextView tv_game_table_num;
    @BindView(R.id.ll_good_road_sorting)
    public LinearLayout ll_good_road_sorting;
    @BindView(R.id.tv_game_name)
    public TextView tv_game_name;
    @BindView(R.id.ll_title)
    public LinearLayout ll_title;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.rl_rv)
    public RelativeLayout rl_rv;
    @BindView(R.id.rv_baccarat)
    public RecyclerView rv_baccarat;

    @BindView(R.id.tv_switch_on)
    public TextView tv_switch_on;
    @BindView(R.id.tv_switch_off)
    public TextView tv_switch_off;
    @BindView(R.id.iv_bac_set)
    ImageView iv_set;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private Context mContext;
    private SinglePickRVAdapter adapter;
    private List<Baccarat> baccaratList = new ArrayList<>();
    private String totalTable;

    @Override
    protected void initView(Bundle savedInstanceState) {
        this.mContext = SinglePickListActivity.this;
        avi.show();

        ll_good_road_sorting.setVisibility(View.INVISIBLE);
        tv_game_name.setText(getResources().getString(R.string.single_pick_game));
        tv_nickname.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        totalTable = mContext.getResources().getString(R.string.total_table);
        EventBus.getDefault().register(this);

        String _gameList = SharedPreUtil.getInstance(mContext).getString(Constant.PRE_SINGLE_LIST);
        List<Baccarat> list = new Gson().fromJson(_gameList, new TypeToken<List<Baccarat>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            baccaratList.clear();
            baccaratList.addAll(list);
            String sFinal = String.format(totalTable, list.size());
            tv_game_table_num.setText(sFinal);
            initAdapter();
            ProtocolUtil.getInstance().postLoginInfo(mContext, tv_money);
        }

    }

    private void initAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        adapter = new SinglePickRVAdapter(mContext, baccaratList);
        rv_baccarat.setLayoutManager(mLayoutManager);
        rv_baccarat.setAdapter(adapter);
        SinglePickListSocketController.getInstance().init(this);
        SinglePickListSocketController.getInstance().connectSocket();
        avi.hide();
    }

    /*
     * 进入大厅
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventJoinGame(final ObjectEvent.BaccaratLottyEvent event) {
        if (event != null && event.hallList.size() > 0) {
            adapter.update(event.hallList);
        }
    }

    class SinglePickRVAdapter extends RecyclerView.Adapter<SinglePickRVAdapter.ViewHolder> {

        private List<Baccarat> mBaccaratList;
        private Context mContext;
        private List<Hall> mHallList = new ArrayList<>();
        private boolean isHall = false;//false 只加载一次大厅数据 true 根据状态=2在刷新界面

        public SinglePickRVAdapter(Context context, List<Baccarat> baccaratList) {
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
        public int getItemViewType(int position) {//解决数据错乱问题，position不对应
            return position;
        }

        @Override
        public int getItemCount() {
            return mBaccaratList == null ? 0 : mBaccaratList.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_pick, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final SinglePickRVAdapter.ViewHolder holder, final int position) {
            holder.tv_room.setText(mBaccaratList.get(position).roomName);
            holder.stakeLimitLabel.setText(mBaccaratList.get(position).minBetList.get(0) + "-" + mBaccaratList.get(position).maxBetList.get(0));
            holder.iv_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Baccarat baccarat = mBaccaratList.get(position);
                    Intent intent = new Intent(mContext, SinglePickLiveActivity.class);
                    intent.putExtra("baccarat", baccarat);
                    intent.putExtra("roomId", mBaccaratList.get(position).roomId);
                    intent.putExtra("roomName", mBaccaratList.get(position).roomName);
                    intent.putExtra("betSecond", mBaccaratList.get(position).betSecond);
                    mContext.startActivity(intent);
                }
            });
            if (mHallList == null || mHallList.size() == 0) {//可见条目的个数就是要初始化的次数
                return;
            } else {
                int times = SharedPreUtil.getInstance(mContext).getInt("times");
                if (isHall == false && mHallList.size() > 0) {//初始化 第一次进入大厅 初始化所有数据
                    init(holder, position, times);
                } else if (isHall && mHallList.size() > 0) {//新添加一局
                    update(holder, position);
                }
            }
        }

        private void init(SinglePickRVAdapter.ViewHolder holder, int position, int times) {
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
                    draw(holder, hall);
                    if (hall.state.equals(Constant.BACCARAT_BET)) {
                        holder.pcac.setCurrentPercent(hall.second);
                        Constant.SEND_CARD = mBaccaratList.get(position).betSecond;
                        holder.pcac.setAllTime(Constant.SEND_CARD);
                    } else if (hall.state.equals(Constant.BACCARAT_INNINGS_END)) {
                        holder.pcac.setCurrentPercent(-1);
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

        private void update(SinglePickRVAdapter.ViewHolder holder, int position) {
            for (int i = 0; i < mHallList.size(); i++) {
                Hall hall = mHallList.get(i);
                if (hall.boardMessageList.size() > 0 && mBaccaratList.get(position).roomId.equals(hall.roomId) && hall.state.equals(Constant.BACCARAT_WAIT)) {
                    draw(holder, hall);
                } else if (hall.boardMessageList.size() == 0 && mBaccaratList.get(position).roomId.equals(hall.roomId)) {
                    draw(holder, hall);
                    holder.pcac.setCurrentPercent(-2);
                }
                if (hall.state.equals(Constant.BACCARAT_BET) && mBaccaratList.get(position).roomId.equals(hall.roomId)) {//倒计时
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

        private void draw(SinglePickRVAdapter.ViewHolder holder, Hall hall) {
            List<Integer> boardMessageList = new ArrayList<>();
            List<String> cardList = new ArrayList<>();
            boardMessageList.clear();
            cardList.clear();
            boardMessageList.addAll(hall.boardMessageList);
            cardList.addAll(hall.cardList);
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

            holder.tv_spade_count.setText(String.valueOf(spade));
            holder.tv_hearts_count.setText(String.valueOf(hearts));
            holder.tv_clubs_count.setText(String.valueOf(clubs));
            holder.tv_diamonds_count.setText(String.valueOf(diamonds));
            holder.tv_king_count.setText(String.valueOf(king));

            Algorithm.getInstance().drawSinglePick(cardList, holder.gv_left, mContext, tv_game_name);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            PercentCircleAntiClockwise pcac;
            YCHGridView gv_left;
            ImageView iv_bg;
            TextView tv_room;
            TextView stakeLimitLabel;
            TextView tv_player;
            TextView tv_spade_count;
            TextView tv_hearts_count;
            TextView tv_clubs_count;
            TextView tv_diamonds_count;
            TextView tv_king_count;

            public ViewHolder(View itemView) {
                super(itemView);

                pcac = (PercentCircleAntiClockwise) itemView.findViewById(R.id.pcac);
                gv_left = (YCHGridView) itemView.findViewById(R.id.gv_left);
                iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
                tv_room = (TextView) itemView.findViewById(R.id.tv_room);
                stakeLimitLabel = (TextView) itemView.findViewById(R.id.stakeLimitLabel);
                tv_player = (TextView) itemView.findViewById(R.id.tv_player);
                tv_spade_count = (TextView) itemView.findViewById(R.id.tv_spade_count);
                tv_hearts_count = (TextView) itemView.findViewById(R.id.tv_hearts_count);
                tv_clubs_count = (TextView) itemView.findViewById(R.id.tv_clubs_count);
                tv_diamonds_count = (TextView) itemView.findViewById(R.id.tv_diamonds_count);
                tv_king_count = (TextView) itemView.findViewById(R.id.tv_king_count);
            }
        }
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                startActivity(new Intent(mContext, TabHostActivity.class));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        SinglePickListSocketController.getInstance().disconnectSocket();
        SharedPreUtil.getInstance(mContext).saveParam("times", 0);//退出房间清零
        avi.stopAnimation();
    }
}
