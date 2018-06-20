package com.chuangku.gameplatform3.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.adapter.GridLeftAdapter;
import com.chuangku.gameplatform3.adapter.GridRightBottom1Adapter;
import com.chuangku.gameplatform3.adapter.GridRightBottom2Adapter;
import com.chuangku.gameplatform3.adapter.GridRightMiddleAdapter;
import com.chuangku.gameplatform3.adapter.GridRightTopAdapter;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseActivity;
import com.chuangku.gameplatform3.base.Constant;
import com.chuangku.gameplatform3.entity.Baccarat;
import com.chuangku.gameplatform3.widget.PercentCircleAntiClockwise;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：百家乐
 * 描述：
 * Create by purity on 2018/5/25.
 */
@ContentView(R.layout.activity_baccarat_list)
public class BaccaratListActivity extends BaseActivity {

    private static final String TAG = "LotteryListActivity";
    @BindView(R.id.ll_back)
    public LinearLayout ll_back;
    @BindView(R.id.ll_title)
    public LinearLayout ll_title;
    @BindView(R.id.rl_rv)
    public RelativeLayout rl_rv;
    @BindView(R.id.rv_baccarat)
    public RecyclerView rv_baccarat;

    @BindView(R.id.tv_switch_on)
    public TextView tv_switch_on;
    @BindView(R.id.tv_switch_off)
    public TextView tv_switch_off;
    private Context context;
    private List<Baccarat> baccaratList;
    private List<Baccarat> baccaratListAll;
    private BaccaratRVAdapter adapter;

    private DisplayMetrics displayMetrics;

    @Override
    protected void initView() {
        this.context = BaccaratListActivity.this;
        displayMetrics = context.getResources().getDisplayMetrics();
        baccaratList = new ArrayList<>();
        baccaratListAll = new ArrayList<>();
        int _interview = R.dimen.unit45;
        //魅族 1920x1014 3.0 华为M5 8.4英寸 三星S85.6英寸2076x1080 480
        LogUtil.i("widthPx="+displayMetrics.widthPixels+" height="+displayMetrics.heightPixels+" density="+displayMetrics.density+" dpi="+displayMetrics.densityDpi);
        if (displayMetrics.widthPixels < 2100&&displayMetrics.widthPixels>2000) {
            ll_title.setPadding(getResources().getDimensionPixelSize(_interview), 0, getResources().getDimensionPixelSize(_interview), 0);
            rl_rv.setPadding(getResources().getDimensionPixelSize(_interview), 0, getResources().getDimensionPixelSize(_interview), 0);
        }
        postLotteryLog(1);
        initAdapter();
        initEvent();
    }

    private void initEvent() {
        int toggle_on = SharedPreUtil.getInstance(this).getInt(Constant.TOGGLE_ON);
        int toggle_off = SharedPreUtil.getInstance(this).getInt(Constant.TOGGLE_OFF);
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
                SharedPreUtil.getInstance(BaccaratListActivity.this).saveParam(Constant.TOGGLE_ON, 1);
            }
        });
        tv_switch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_switch_on.setVisibility(View.VISIBLE);
                tv_switch_off.setVisibility(View.GONE);
                SharedPreUtil.getInstance(BaccaratListActivity.this).saveParam(Constant.TOGGLE_OFF, 1);
            }
        });
    }

    private void initAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        adapter = new BaccaratRVAdapter(context, baccaratListAll);
        rv_baccarat.setLayoutManager(mLayoutManager);
        rv_baccarat.setAdapter(adapter);
    }

    private void postLotteryLog(final int page) {
        baccaratListAll.clear();
        //test
        Baccarat baccarat = new Baccarat();
        baccarat.banker = "1";
        baccaratList.add(baccarat);
//        baccaratList.add(baccarat);
//        baccaratList.add(baccarat);
//        baccaratList.add(baccarat);
//        baccaratList.add(baccarat);
//        baccaratList.add(baccarat);
//        baccaratList.add(baccarat);
//        baccaratList.add(baccarat);
//        baccaratList.add(baccarat);
//        baccaratList.add(baccarat);
//        baccaratList.add(baccarat);
        baccaratListAll.addAll(baccaratList);
//        HttpRequest.requestWithUser(Constant.LOTTERY_LOG, context)
//                .addParam("page",page)
//                .executeFormPost(new HttpRequest.HttpCallBack() {
//                    @Override
//                    public void onResultOk(String result) {
//                        baccaratList.clear();//清除,每一页都是新的
//                        baccaratList = parseArrayObject(result, "list", Baccarat.class);
//                        try {
//                            JSONObject json = new JSONObject(result);
//                            String allRow = json.optString("total_rows");
//                            total = Integer.parseInt(allRow);
//                            if (page == 1) {
//                                baccaratListAll.clear();//下拉刷新,要清除总集合
//                            }
//                            baccaratListAll.addAll(baccaratList);//每一页的数据添加进总集合中,方便判断还有没有数据,正确提示"没有更多数据了"
//                            adapter.updateList(baccaratListAll);
//                            onFinish();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
    }

    class BaccaratRVAdapter extends RecyclerView.Adapter<BaccaratRVAdapter.ViewHolder> {

        private List<Baccarat> mData;
        private Context mContext;

        public BaccaratRVAdapter(Context context, List<Baccarat> data) {
            this.mData = data;
            this.mContext = context;
        }

        public void updateData(List<Baccarat> data) {
            this.mData = data;
            notifyDataSetChanged();
        }

        @Override
        public BaccaratRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baccarat_4, parent, false);
            return new BaccaratRVAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final BaccaratRVAdapter.ViewHolder holder, int position) {
            // 绑定数据
            holder.gv_left.setAdapter(new GridLeftAdapter(mContext));
            holder.gv_right_top.setAdapter(new GridRightTopAdapter(mContext));
            holder.gv_right_middle.setAdapter(new GridRightMiddleAdapter(mContext));
            holder.gv_right_bottom_1.setAdapter(new GridRightBottom1Adapter(mContext));
            holder.gv_right_bottom_2.setAdapter(new GridRightBottom2Adapter(mContext));
            holder.pcac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.pcac.setTargetPercent(0);
                    holder.pcac.reInitView();
                }
            });
            holder.iv_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, BaccaratLiveActivity.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            PercentCircleAntiClockwise pcac;
            GridView gv_left;
            GridView gv_right_top;
            GridView gv_right_middle;
            GridView gv_right_bottom_1;
            GridView gv_right_bottom_2;
            ImageView iv_bg;

            public ViewHolder(View itemView) {
                super(itemView);

                pcac = itemView.findViewById(R.id.pcac);
                gv_left = itemView.findViewById(R.id.gv_left);
                gv_right_top = itemView.findViewById(R.id.gv_right_top);
                gv_right_middle = itemView.findViewById(R.id.gv_right_middle);
                gv_right_bottom_1 = itemView.findViewById(R.id.gv_right_bottom_1);
                gv_right_bottom_2 = itemView.findViewById(R.id.gv_right_bottom_2);
                iv_bg = itemView.findViewById(R.id.iv_bg);
            }
        }
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
