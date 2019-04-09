package com.purity.yu.gameplatform.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.BaccaratRVAdapter;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.controler.BaccaratListSocketController;
import com.purity.yu.gameplatform.controler.TabHostController;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.ProtocolUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import avi.AVLoadingIndicatorView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：百家乐列表
 * 描述：
 * Create by purity on 2018/5/25.
 */
@ContentView(R.layout.activity_baccarat_list)
//@Xml(layouts = "activity_baccarat_list")
public class BaccaratListActivity extends BaseActivity {

    private static final String TAG = "BaccaratListActivity";
    @BindView(R.id.ll_back)
    public LinearLayout ll_back;
    @BindView(R.id.ll_title)
    public LinearLayout ll_title;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_game_table_num)
    public TextView tv_game_table_num;
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
    private List<Baccarat> baccaratList = new ArrayList<>();
    private BaccaratRVAdapter adapter;

    private String totalTable;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = BaccaratListActivity.this;
        initView();
    }

    protected void initView() {
        avi.show();
        tv_nickname.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        totalTable = mContext.getResources().getString(R.string.total_table);
        EventBus.getDefault().register(this);

        String _gameList = SharedPreUtil.getInstance(mContext).getString(Constant.PRE_BAC_LIST);
        LogUtil.i("BaccaratListActivity_gameList=" + _gameList);
        List<Baccarat> list = new Gson().fromJson(_gameList, new TypeToken<List<Baccarat>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            baccaratList.clear();
            baccaratList.addAll(list);
            String sFinal = String.format(totalTable, list.size());
            tv_game_table_num.setText(sFinal);
            initAdapter();
            ProtocolUtil.getInstance().postLoginInfo(tv_money);
        }
        initEvent();
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

    private void initEvent() {
        iv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.getInstance(mContext).saveParam(Constant.WHERE_SET, 4);
                TabHostController.getInstance().initMenu(mContext, 0, new Timer());
            }
        });
    }

    private void initAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        adapter = new BaccaratRVAdapter(mContext, baccaratList);
        rv_baccarat.setLayoutManager(mLayoutManager);
        rv_baccarat.setAdapter(adapter);
        BaccaratListSocketController.getInstance().init(mContext);
        BaccaratListSocketController.getInstance().connectSocket();
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

    /*
     * 余额
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventMoneyChange(final ObjectEvent.MoneyChangeEvent event) {
        tv_money.setText(event.money);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        BaccaratListSocketController.getInstance().disconnectSocket();//后退是重新打开？？
        SharedPreUtil.getInstance(mContext).saveParam("times", 0);//退出房间清零
        avi.stopAnimation();
    }
}
