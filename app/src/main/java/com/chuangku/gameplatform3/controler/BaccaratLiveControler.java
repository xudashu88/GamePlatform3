package com.chuangku.gameplatform3.controler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.activity.BaccaratListActivity;
import com.chuangku.gameplatform3.activity.BaccaratLiveActivity;
import com.chuangku.gameplatform3.activity.UserActivity;
import com.chuangku.gameplatform3.adapter.ChipAdapter;
import com.chuangku.gameplatform3.base.Constant;
import com.chuangku.gameplatform3.entity.Chip;
import com.chuangku.gameplatform3.entity.Game;
import com.chuangku.gameplatform3.fragment.HotNewFragment;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/6/2.
 */
public class BaccaratLiveControler {
    private Context mContext;
    private static BaccaratLiveControler instance = null;
    private LayoutInflater layoutInflater;
    private ChipAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Chip> chipList;

    TextView tv_table_limits;
    RelativeLayout rl_dragon_bonus;
    RecyclerView rv_new_chip;
    TextView tv_switch_on;
    TextView tv_switch_off;


    public void init(Context context) {
        mContext = context;
        layoutInflater = ((Activity) mContext).getWindow().getLayoutInflater();
    }

    public static BaccaratLiveControler getInstance() {
        if (null == instance) {
            instance = new BaccaratLiveControler();
        }
        return instance;
    }

    //上到下，左到右
    public void copyWidget(TextView tv_table_limits, TextView tv_switch_on, TextView tv_switch_off, RelativeLayout rl_dragon_bonus, RecyclerView rv_new_chip) {
        this.tv_table_limits = tv_table_limits;
        this.tv_switch_on = tv_switch_on;
        this.tv_switch_off = tv_switch_off;
        this.rl_dragon_bonus = rl_dragon_bonus;
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
        tv_table_limits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBetLimitDialog();
            }
        });
        rl_dragon_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDragonHelpDialog();
            }
        });
        int toggle_on = SharedPreUtil.getInstance(mContext).getInt(Constant.COMMISSION_ON);
        int toggle_off = SharedPreUtil.getInstance(mContext).getInt(Constant.COMMISSION_OFF);
        if (0 == toggle_on || toggle_on == 1) {
            tv_switch_on.setVisibility(View.VISIBLE);
            tv_switch_off.setVisibility(View.GONE);
        } else {
            tv_switch_off.setVisibility(View.VISIBLE);
            tv_switch_on.setVisibility(View.GONE);
        }
        if (toggle_off == 1) {
            tv_switch_on.setVisibility(View.GONE);
            tv_switch_off.setVisibility(View.VISIBLE);
        } else {
            tv_switch_off.setVisibility(View.GONE);
            tv_switch_on.setVisibility(View.VISIBLE);
        }
        tv_switch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_switch_on.setVisibility(View.GONE);
                tv_switch_off.setVisibility(View.VISIBLE);
                SharedPreUtil.getInstance(mContext).saveParam(Constant.COMMISSION_ON, 1);
            }
        });
        tv_switch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_switch_on.setVisibility(View.VISIBLE);
                tv_switch_off.setVisibility(View.GONE);
                SharedPreUtil.getInstance(mContext).saveParam(Constant.COMMISSION_OFF, 1);
            }
        });
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
                ToastUtil.show(""+position);
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

    private void initDragonHelpDialog() {
        View layout12 = layoutInflater.inflate(R.layout.dialog_bonus, null);
        Util.getInstance().setDialog(mContext, R.style.dialog, layout12, 0.65f, 0f, 0.6f, 0, 0, Gravity.CENTER, Gravity.CENTER_HORIZONTAL);
    }

    private void initBetLimitDialog() {
//        View layout12 = layoutInflater.inflate(R.layout.dialog_bet_limit, null);
//        Util.getInstance().setDialog(mContext, R.style.dialog, layout12, 0.35f, 0f, 0.0f, 0, 0, Gravity.CENTER, Gravity.CENTER_HORIZONTAL);

        final View layout = layoutInflater.inflate(R.layout.dialog_bet_limit, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.dialog).setView(layout);
        AlertDialog alertDialogVirtualKeyboard = builder.create();
        alertDialogVirtualKeyboard.show();
        Window window = alertDialogVirtualKeyboard.getWindow();


        //设置dialog弹出时的动画效果，从屏幕左侧向右弹出
        window.setWindowAnimations(R.style.virtualKeyboardDialogAnim);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.TOP | Gravity.LEFT);

        WindowManager m = ((Activity) mContext).getWindowManager();
        Display display = m.getDefaultDisplay();
        lp.width = (int) (display.getWidth() * 0.26f);
        lp.height = (int) (display.getHeight() * 0.45f);
        lp.y=150;
        lp.x=500;
        lp.dimAmount = 0.0f;
        window.setAttributes(lp);
    }
}
