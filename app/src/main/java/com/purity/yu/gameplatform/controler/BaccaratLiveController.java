package com.purity.yu.gameplatform.controler;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.ChipAdapter;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.entity.Chip;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/6/2.
 */
public class BaccaratLiveController {
    private Context mContext;
    private static BaccaratLiveController instance = null;
    private LayoutInflater layoutInflater;
    private ChipAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Chip> chipList;

    RelativeLayout rl_dragon_bonus;
    RecyclerView rv_new_chip;

    private int selectChipNumber;

    public void init(Context context) {
        mContext = context;
        layoutInflater = ((Activity) mContext).getWindow().getLayoutInflater();
    }

    public static BaccaratLiveController getInstance() {
        if (null == instance) {
            instance = new BaccaratLiveController();
        }
        return instance;
    }

    //上到下，左到右
    public void copyWidget(RelativeLayout rl_dragon_bonus, RecyclerView rv_new_chip) {
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
        chip1.chipImg = R.drawable.new_chip_10;
        chip1.isCheck = false;
        Chip chip2 = new Chip();
        chip2.chipNumber = 20;
        chip2.chipImg = R.drawable.new_chip_20;
        chip2.isCheck = false;
        Chip chip3 = new Chip();
        chip3.chipNumber = 50;
        chip3.chipImg = R.drawable.new_chip_50;
        chip3.isCheck = false;
        Chip chip4 = new Chip();
        chip4.chipNumber = 100;
        chip4.chipImg = R.drawable.new_chip_100;
        chip4.isCheck = false;
        Chip chip5 = new Chip();
        chip5.chipNumber = 200;
        chip5.chipImg = R.drawable.new_chip_200;
        chip5.isCheck = false;
        Chip chip6 = new Chip();
        chip6.chipNumber = 500;
        chip6.chipImg = R.drawable.new_chip_500;
        chip6.isCheck = false;
        Chip chip7 = new Chip();
        chip7.chipNumber = 1000;
        chip7.chipImg = R.drawable.new_chip_1000;
        chip7.isCheck = false;
        Chip chip8 = new Chip();
        chip8.chipNumber = 5000;
        chip8.chipImg = R.drawable.new_chip_5000;
        chip8.isCheck = false;
        Chip chip9 = new Chip();
        chip9.chipNumber = 10000;
        chip9.chipImg = R.drawable.new_chip_10000;
        chip9.isCheck = false;
        Chip chip10 = new Chip();
        chip10.chipNumber = 50000;
        chip10.chipImg = R.drawable.new_chip_50000;
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
        //点击对应下注区域 end
        rl_dragon_bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDragonHelpDialog();
            }
        });
    }

    protected void initView() {
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mAdapter = new ChipAdapter(mContext, chipList);
        rv_new_chip.setLayoutManager(mLayoutManager);
        rv_new_chip.setAdapter(mAdapter);

        //滚动到指定位置，显示在最顶部 默认2
        int chipPosition = 0;
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_POSITION) == 0) {
            chipPosition = 2;
        } else {
            chipPosition = SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_POSITION);
        }
        chipList.get(chipPosition).isCheck = true;
        mAdapter.updateData(chipList);
        rv_new_chip.scrollToPosition(chipPosition);
        selectChipNumber = chipList.get(chipPosition).chipNumber;
        SharedPreUtil.getInstance(mContext).saveParam(Constant.SELECT_CHIP_NUMBER, selectChipNumber);
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) rv_new_chip.getLayoutManager();
        mLayoutManager.scrollToPositionWithOffset(chipPosition, 0);

        mAdapter.setOnItemClickListener(new ChipAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < chipList.size(); i++) {
                    if (i == position) {
                        chipList.get(i).isCheck = true;
                        selectChipNumber = chipList.get(i).chipNumber;
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.SELECT_CHIP_NUMBER, selectChipNumber);
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
        AlertDialog dialog = Util.getInstance().setDialog(mContext, R.style.dialog, layout12, 0.65f, 0f, 0.6f, 0, 0, Gravity.CENTER, Gravity.CENTER_HORIZONTAL, 0);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Util.getInstance().init(mContext);
                Util.getInstance().hideSystemNavigationBar();
            }
        });
    }

}
