package com.chuangku.gameplatform3.base;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 常用列表页基类
 */
public abstract class BaseListActivity extends BaseActivity implements XRecyclerView.LoadingListener {
    @Nullable
    @BindView(R.id.recyclerView)
    public XRecyclerView recyclerView;
    @BindView(R.id.tv_no_data)
    public TextView tv_no_data;
    @BindView(R.id.tv_no_net)
    public TextView tv_no_net;
    protected int total;
    protected int page = 1;

    protected abstract List getData();

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLoadingListener(this);

    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        requestData(page);
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        if(getData() != null && getData().size() >= total){
            new Handler().post(new Runnable() {
                public void run() {
                    ToastUtil.show("没有更多数据");
                    recyclerView.loadMoreComplete();
                }
            });
        } else {
            page++;
            requestData(page);
        }
    }

    /**
     * 请求数据
     */
    protected abstract void requestData(int page);

    /**
     * 点击空布局或者无网络布局
     */
    @OnClick({R.id.tv_no_data, R.id.tv_no_net})
    public void onClickEmptyView(){
        requestData(page);
    }

    /**
     * 无数据
     */
    protected void onNoData(){
        if(getData().size() == 0){
            tv_no_net.setVisibility(View.GONE);
            recyclerView.setEmptyView(tv_no_data);
        }
    }
    public void onLoadFinish() {
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
    }

    /**
     * 无网络
     */
    protected void onNetError(){
        if(getData().size() == 0){
            tv_no_data.setVisibility(View.GONE);
            recyclerView.setEmptyView(tv_no_net);
        }
    }

}
