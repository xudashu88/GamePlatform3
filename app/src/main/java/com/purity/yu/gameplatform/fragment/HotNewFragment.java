package com.purity.yu.gameplatform.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.BaccaratListActivity;
import com.purity.yu.gameplatform.activity.BaccaratListActivity2;
import com.purity.yu.gameplatform.activity.DtListActivity;
import com.purity.yu.gameplatform.activity.DtListActivity2;
import com.purity.yu.gameplatform.activity.HolyDayListActivity;
import com.purity.yu.gameplatform.activity.MacauListActivity;
import com.purity.yu.gameplatform.activity.RoyalActivity;
import com.purity.yu.gameplatform.activity.SinglePickListActivity;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Game2;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.BaccaratUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;

import static com.gangbeng.basemodule.http.HttpParse.parseArrayObject;

@ContentView(R.layout.fragment_hot_new)
public class HotNewFragment extends BaseFragment {

    @BindView(R.id.rv)
    RecyclerView mRecyclerView;


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    List<Game2> gameList = new ArrayList<>();


    @Override
    protected void initView(View view) {
        mContext = getActivity();
        getGameList();
    }

    private void initAdapter() {
        mLayoutManager = new GridLayoutManager(getActivity(), 5);
        mAdapter = new HotNewAdapter(gameList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getGameList() {
        String _gameList = SharedPreUtil.getInstance(mContext).getString(Constant.PRE_GAME_LIST);
        List<Game2> list = new Gson().fromJson(_gameList, new TypeToken<List<Game2>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            gameList.clear();
            gameList.addAll(list);
            initAdapter();
        } else {
            gameList.clear();
            String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
            CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.GAME_LIST + "?token=" + token, null),//perfect
                    new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            JSONObject json = null;
                            try {
                                json = new JSONObject(s);
                                String _data = json.optString("data");
                                gameList = parseArrayObject(_data, "items", Game2.class);
                                for (int i = 0; i < gameList.size(); i++) {
                                    String verify = gameList.get(i).platformCode + gameList.get(i).gameNameEn;
                                    if (verify.equals("JXB" + "baccarat")) {
                                        gameList.get(i).gameDot = SharedPreUtil.getInstance(getActivity()).getInt(Constant.BAC_ROOM_COUNT);
                                    } else if (verify.equals("JXB" + "baccarat2")/*gameList.get(i).gameName.equals("百家乐2")*/) {
                                        gameList.get(i).gameDot = SharedPreUtil.getInstance(getActivity()).getInt(Constant.BAC_ROOM_COUNT);
                                    } else if (verify.equals("JXB" + "dragonTiger")/*gameList.get(i).gameName.equals("龙虎")*/) {
                                        gameList.get(i).gameDot = SharedPreUtil.getInstance(getActivity()).getInt(Constant.DT_ROOM_COUNT);
                                    } else if (verify.equals("JXB" + "dragonTiger2")/*gameList.get(i).gameName.equals("龙虎2")*/) {
                                        gameList.get(i).gameDot = SharedPreUtil.getInstance(getActivity()).getInt(Constant.DT_ROOM_COUNT);
                                    } else if (verify.equals("JXB" + "duel")/*gameList.get(i).gameName.equals("单挑")*/) {
                                        gameList.get(i).gameDot = SharedPreUtil.getInstance(getActivity()).getInt(Constant.SINGLE_ROOM_COUNT);
                                    } else if (verify.equals("JXB" + "macau")/*gameList.get(i).gameName.equals("澳门五路")*/) {
                                        gameList.get(i).gameDot = SharedPreUtil.getInstance(getActivity()).getInt(Constant.BAC_ROOM_COUNT);
                                    } else if (verify.equals("JXB" + "macau.")/*gameList.get(i).gameName.equals("澳门五路.")*/) {
                                        gameList.get(i).gameDot = SharedPreUtil.getInstance(getActivity()).getInt(Constant.BAC_ROOM_COUNT);
                                    }
                                }
                                initAdapter();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(OkHttpException e) {
                            ToastUtil.show(mContext, "连接超时");
                        }
                    })));
        }
    }

    class HotNewAdapter extends RecyclerView.Adapter<HotNewAdapter.ViewHolder> {

        private List<Game2> mData;

        public HotNewAdapter(List<Game2> data) {
            this.mData = data;
        }

        public void updateData(List<Game2> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public int getItemViewType(int position) {
            if (mData.size() <= 0) {
                return -1;
            }
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据
            holder.tv_games_name.setText(mData.get(position).gameName);
//            holder.iv_game_avatar.setBackground(mData.get(position).gameAvatar);
            Glide.with(mContext).load(mData.get(position).gameIconUrl).into(holder.iv_game_avatar);
            if (mData.get(position).gameDot == 0) {
                holder.tv_dot.setVisibility(View.GONE);
            } else {
                holder.tv_dot.setText("" + mData.get(position).gameDot);
            }
            holder.iv_game_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String _bac = getActivity().getResources().getString(R.string.ba_game);
                    String _dt = getActivity().getResources().getString(R.string.dt_game);
                    String _hj = getActivity().getResources().getString(R.string.hj_game);
                    String _holo = getActivity().getResources().getString(R.string.holy_day);
                    String _macao = getActivity().getResources().getString(R.string.macao);
                    String _single = getActivity().getResources().getString(R.string.single_pick_game);
                    String verify = mData.get(position).platformCode + mData.get(position).gameNameEn;
                    if (verify.equals("JXB" + "baccarat")) {
//                        getActivity().finish();//防止安卓4.4不执行自定义控件实现内部接口，必须要关闭上一个页面，由房间进入大厅启发而来
                        getActivity().startActivity(new Intent(getActivity(), BaccaratListActivity.class).putExtra("roomName", mData.get(position).gameName));
                    } else if (verify.equals("JXB" + "baccarat2")) {
//                        getActivity().finish();//防止安卓4.4不执行自定义控件实现内部接口，必须要关闭上一个页面，由房间进入大厅启发而来
                        getActivity().startActivity(new Intent(getActivity(), BaccaratListActivity2.class).putExtra("roomName", mData.get(position).gameName));
                    } else if (verify.equals("JXB" + "dragonTiger")) {
//                        getActivity().finish();//防止安卓4.4不执行自定义控件实现内部接口，必须要关闭上一个页面，由房间进入大厅启发而来
                        getActivity().startActivity(new Intent(getActivity(), DtListActivity.class).putExtra("roomName", mData.get(position).gameName));
                    } else if (verify.equals("JXB" + "dragonTiger2")) {
//                        getActivity().finish();//防止安卓4.4不执行自定义控件实现内部接口，必须要关闭上一个页面，由房间进入大厅启发而来
                        getActivity().startActivity(new Intent(getActivity(), DtListActivity2.class).putExtra("roomName", mData.get(position).gameName));
                    } else if (verify.equals("JXB" + "macau")) {
//                        getActivity().finish();//防止安卓4.4不执行自定义控件实现内部接口，必须要关闭上一个页面，由房间进入大厅启发而来
                        getActivity().startActivity(new Intent(getActivity(), HolyDayListActivity.class).putExtra("roomName", mData.get(position).gameName));
                    } else if (verify.equals("JXB" + "macau.")) {
//                        getActivity().finish();//防止安卓4.4不执行自定义控件实现内部接口，必须要关闭上一个页面，由房间进入大厅启发而来
                        getActivity().startActivity(new Intent(getActivity(), MacauListActivity.class).putExtra("roomName", mData.get(position).gameName));
                    } else if (verify.equals("HJ" + "baccarat")) {
                        if (BaccaratUtil.getInstance().isLogin()) {
                            getActivity().startActivity(new Intent(getActivity(), RoyalActivity.class).putExtra("roomName", mData.get(position).gameName));
                        }
                    } else if (verify.equals("JXB" + "duel")) {
//                        getActivity().finish();//防止安卓4.4不执行自定义控件实现内部接口，必须要关闭上一个页面，由房间进入大厅启发而来
                        getActivity().startActivity(new Intent(getActivity(), SinglePickListActivity.class).putExtra("roomName", mData.get(position).gameName));
                    } else {
                        ToastUtil.showToast(getActivity(), "暂未开放，敬请期待！");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_game_avatar;
            TextView tv_games_name;
            TextView tv_dot;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_games_name = (TextView) itemView.findViewById(R.id.tv_games_name);
                tv_dot = (TextView) itemView.findViewById(R.id.tv_dot);
                iv_game_avatar = (ImageView) itemView.findViewById(R.id.iv_game_avatar);
            }
        }
    }
}
