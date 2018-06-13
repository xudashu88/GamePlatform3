package com.chuangku.gameplatform3.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.activity.BaccaratLiveActivity;
import com.chuangku.gameplatform3.entity.Baccarat;
import com.chuangku.gameplatform3.widget.PercentCircleAntiClockwise;
import com.gangbeng.basemodule.base.CommonRecyclerViewAdapter;
import com.gangbeng.basemodule.base.CommonRecyclerViewHolder;

import java.util.List;

public class BaccaratAdapter extends CommonRecyclerViewAdapter<Baccarat> {

    private Context mContext;

    public BaccaratAdapter(Context context, List<Baccarat> data) {
        super(context, data);
        this.mContext = context;
    }

    @Override
    public void convert(CommonRecyclerViewHolder h, final Baccarat entity, final int position) {
        final PercentCircleAntiClockwise pcac = h.getView(R.id.pcac);
        GridView gv_left = h.getView(R.id.gv_left);
        GridView gv_right_top = h.getView(R.id.gv_right_top);
        GridView gv_right_middle = h.getView(R.id.gv_right_middle);
        GridView gv_right_bottom_1 = h.getView(R.id.gv_right_bottom_1);
        GridView gv_right_bottom_2 = h.getView(R.id.gv_right_bottom_2);
        final ImageView iv_bg = h.getView(R.id.iv_bg);
        gv_left.setAdapter(new GridLeftAdapter(mContext));
        gv_right_top.setAdapter(new GridRightTopAdapter(mContext));
        gv_right_middle.setAdapter(new GridRightMiddleAdapter(mContext));
        gv_right_bottom_1.setAdapter(new GridRightBottom1Adapter(mContext));
        gv_right_bottom_2.setAdapter(new GridRightBottom2Adapter(mContext));

//        final TextView tv_account = h.getView(R.id.tv_account);
//        final TextView tv_createTime = h.getView(R.id.tv_createTime);
//        final TextView tv_integral = h.getView(R.id.tv_integral);
//        tv_nickName.setText(entity.nickName);
//        tv_account.setText("ID: "+entity.id);
//        tv_createTime.setText(Util.formartLongToTime(entity.createTime));
        pcac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pcac.setTargetPercent(0);
                pcac.reInitView();
            }
        });
        iv_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, BaccaratLiveActivity.class));
            }
        });
    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_baccarat_1;
    }
}