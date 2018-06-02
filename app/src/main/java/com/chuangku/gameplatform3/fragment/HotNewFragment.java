package com.chuangku.gameplatform3.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.activity.BaccaratListActivity;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseFragment;
import com.chuangku.gameplatform3.entity.Game;

import java.util.ArrayList;

import butterknife.BindView;

@ContentView(R.layout.fragment_hot_new)
public class HotNewFragment extends BaseFragment {

    @BindView(R.id.rv)
    RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void initView(View view) {
        mLayoutManager = new GridLayoutManager(getActivity(), 5);
        mAdapter = new MyAdapter(getData());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<Game> getData() {
        ArrayList<Game> data = new ArrayList<>();
        Game game1 = new Game();
        game1.gameAvatar = getActivity().getResources().getDrawable(R.drawable.icon_link_aghh);
        game1.gameName = getActivity().getResources().getString(R.string.ba_game);
        game1.gameDot = 11;
        data.add(game1);
        Game game2 = new Game();
        game2.gameAvatar = getActivity().getResources().getDrawable(R.drawable.lobbyicon_bac);
        game2.gameName = getActivity().getResources().getString(R.string.dt_game);
        game2.gameDot = 10;
        data.add(game2);
        Game game3 = new Game();
        game3.gameAvatar = getActivity().getResources().getDrawable(R.drawable.lobbyicon_multibac);
        game3.gameName = getActivity().getResources().getString(R.string.link_game);
        game3.gameDot = 8;
        data.add(game3);
        Game game4 = new Game();
        game4.gameAvatar = getActivity().getResources().getDrawable(R.drawable.lobbyicon_roulette);
        game4.gameName = getActivity().getResources().getString(R.string.fish_hunter);
        game4.gameDot = 7;
        data.add(game4);
        Game game5 = new Game();
        game5.gameAvatar = getActivity().getResources().getDrawable(R.drawable.lobbyicon_sicbo);
        game5.gameName = getActivity().getResources().getString(R.string.fruit_slot);
        game5.gameDot = 0;
        data.add(game5);
        Game game6 = new Game();
        game6.gameAvatar = getActivity().getResources().getDrawable(R.drawable.lobbyicon_vip);
        game6.gameName = getActivity().getResources().getString(R.string.high_roller);
        game6.gameDot = 18;
        data.add(game6);

        return data;
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<Game> mData;

        public MyAdapter(ArrayList<Game> data) {
            this.mData = data;
        }

        public void updateData(ArrayList<Game> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            if (mData.size() <= 0) {
                return -1;
            }
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // 绑定数据
            holder.tv_games_name.setText(mData.get(position).gameName);
            if (position % 2 == 0) {
                Glide.with(getActivity())
                        .load(R.drawable.icon_link_aghh)
                        .into(holder.iv_game_avatar);
            } else {
                Glide.with(getActivity())
                        .load(R.drawable.lobbyicon_sicbo)
                        .into(holder.iv_game_avatar);
            }

            if (mData.get(position).gameDot == 0) {
                holder.tv_dot.setVisibility(View.GONE);
            } else {
                holder.tv_dot.setText("" + mData.get(position).gameDot);
            }
            holder.iv_game_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), BaccaratListActivity.class));
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
