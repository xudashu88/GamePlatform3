package com.chuangku.gameplatform3.activity;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.adapter.BaccaratAdapter;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseListActivity;
import com.chuangku.gameplatform3.base.Constant;
import com.chuangku.gameplatform3.entity.Baccarat;
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
public class BaccaratListActivity extends BaseListActivity {

    private static final String TAG = "LotteryListActivity";
    @BindView(R.id.ll_back)
    public LinearLayout ll_back;
    @BindView(R.id.tv_switch_on)
    public TextView tv_switch_on;
    @BindView(R.id.tv_switch_off)
    public TextView tv_switch_off;
    //    @BindView(R.id.tv_switch_on)
//    public TextView tv_switch_on;
//    @BindView(R.id.tv_switch_on)
//    public TextView tv_switch_on;
//    @BindView(R.id.tv_switch_on)
//    public TextView tv_switch_on;
    private Context context;
    private List<Baccarat> baccaratList;
    private List<Baccarat> baccaratListAll;
    private BaccaratAdapter adapter;

    @Override
    protected void initView() {
        super.initView();
        this.context = BaccaratListActivity.this;
        baccaratList = new ArrayList<>();
        baccaratListAll = new ArrayList<>();
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
        adapter = new BaccaratAdapter(context, baccaratList);
        recyclerView.setAdapter(adapter);
    }

    private void postLotteryLog(final int page) {

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

    @Override
    protected List getData() {
        return baccaratListAll;
    }

    @Override
    protected void requestData(int page) {
        postLotteryLog(page);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                onFinish();
            }
        }, 1000);
    }

    public void onFinish() {
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
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
