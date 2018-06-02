package com.chuangku.gameplatform3.controler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.activity.BaccaratListActivity;
import com.chuangku.gameplatform3.activity.UserActivity;
import com.chuangku.gameplatform3.adapter.ChipAdapter;
import com.chuangku.gameplatform3.entity.Chip;
import com.chuangku.gameplatform3.entity.Game;
import com.chuangku.gameplatform3.fragment.HotNewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/6/2.
 */
public class BaccaratLiveControler {
    private Context mContext;
    private static BaccaratLiveControler instance = null;
    private ChipAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Chip> chipList;

    public RecyclerView rv_new_chip;


    public void init(Context context) {
        mContext = context;

    }

    public static BaccaratLiveControler getInstance() {
        if (null == instance) {
            instance = new BaccaratLiveControler();
        }
        return instance;
    }

    public void copyWidget(RecyclerView rv_new_chip) {
        this.rv_new_chip = rv_new_chip;
        initDate();
        initEvent();
        initView();
    }

    private void initDate() {
        chipList = new ArrayList<>();
        Chip chip1 = new Chip();
        chip1.chipNumber = 10;
        chip1.chipImg = R.drawable.new_chip_01;
        chip1.isCheck = false;
        Chip chip2 = new Chip();
        chip2.chipNumber = 20;
        chip2.chipImg = R.drawable.new_chip_02;
        chip2.isCheck = false;
        Chip chip3 = new Chip();
        chip3.chipNumber = 50;
        chip3.chipImg = R.drawable.new_chip_03;
        chip3.isCheck = true;
        Chip chip4 = new Chip();
        chip4.chipNumber = 100;
        chip4.chipImg = R.drawable.new_chip_04;
        chip4.isCheck = false;
        Chip chip5 = new Chip();
        chip5.chipNumber = 200;
        chip5.chipImg = R.drawable.new_chip_05;
        chip5.isCheck = false;
        Chip chip6 = new Chip();
        chip6.chipNumber = 500;
        chip6.chipImg = R.drawable.new_chip_06;
        chip6.isCheck = false;
        Chip chip7 = new Chip();
        chip7.chipNumber = 1000;
        chip7.chipImg = R.drawable.new_chip_07;
        chip7.isCheck = false;
        Chip chip8 = new Chip();
        chip8.chipNumber = 5000;
        chip8.chipImg = R.drawable.new_chip_08;
        chip8.isCheck = false;
        Chip chip9 = new Chip();
        chip9.chipNumber = 10000;
        chip9.chipImg = R.drawable.new_chip_09;
        chip9.isCheck = false;
        Chip chip10 = new Chip();
        chip10.chipNumber = 50000;
        chip10.chipImg = R.drawable.new_chip_10;
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

    private void initEvent() {

    }

    protected void initView() {
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mAdapter = new ChipAdapter(mContext, chipList);
        rv_new_chip.setLayoutManager(mLayoutManager);
        rv_new_chip.setAdapter(mAdapter);

        //滚动到指定位置，显示在最顶部
        rv_new_chip.scrollToPosition(2);
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) rv_new_chip.getLayoutManager();
        mLayoutManager.scrollToPositionWithOffset(2, 0);

        mAdapter.setOnItemClickListener(new ChipAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < chipList.size(); i++) {
                    if (i == position) {
                        chipList.get(i).isCheck = true;
                    } else {
                        chipList.get(i).isCheck = false;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }


}
