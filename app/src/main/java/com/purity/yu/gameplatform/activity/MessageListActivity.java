package com.purity.yu.gameplatform.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.MessageAdapter;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseListActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Message;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.http.HttpRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gangbeng.basemodule.http.HttpParse.parseArrayObject;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/7/19.
 */
@ContentView(R.layout.activity_message_list)
public class MessageListActivity extends BaseListActivity {

    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.tv_no_data)
    public TextView tv_no_data;
    @BindView(R.id.rl_data)
    public RelativeLayout rl_data;
    private Context mContext;
    private MessageAdapter adapter;
    private List<Message> recordList;
    private List<Message> recordListAll;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mContext = this;
        recordList = new ArrayList<>();
        recordListAll = new ArrayList<>();
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BET0, 0);
        requestData(1);
        initAdapter();
        EventBus.getDefault().register(this);
    }

    private void initAdapter() {
        adapter = new MessageAdapter(mContext, recordListAll);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
//        recyclerView.setRefresh(false);//禁止下拉刷新，是為了防止顯示多個牌面
    }

    /*
     * 进入大厅
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(final ObjectEvent.notifyItemChangedEvent event) {
        requestData(1);
    }

    @Override
    protected List getData() {
        return recordListAll;
    }

    @Override
    protected void requestData(final int page) {
        postMessageList(page);
    }

    private void postMessageList(final int page) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.MESSAGE_LIST + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.username_existed);
                                ToastUtil.show(mContext, error);
                                return;
                            }

                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            recordList.clear();//清除,每一页都是新的
                            recordList = parseArrayObject(_data, "items", Message.class);
                            String meta = __data.optString("_meta");
                            JSONObject _meta = new JSONObject(meta);
                            total = _meta.optInt("totalCount");

                            if (page == 1) {
                                recordListAll.clear();//下拉刷新,要清除总集合
                            }
                            recordListAll.addAll(recordList);
                            adapter.updateList(recordListAll);
                            if (recordList.size() > 0) {
                                tv_no_data.setVisibility(View.GONE);
                                rl_data.setVisibility(View.VISIBLE);
                            } else {
                                tv_no_data.setVisibility(View.VISIBLE);
                                rl_data.setVisibility(View.GONE);
                            }
                            onFinish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        super.onRefresh(isRefresh);
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

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                SharedPreUtil.getInstance(mContext).saveParam(Constant.BET0, 0);
                finish();
                break;
        }
    }
}
