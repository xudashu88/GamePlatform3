package com.purity.yu.gameplatform.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.purity.yu.gameplatform.R;

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
    protected int total;//总条数判断有没有下一页
    protected int pageCount;//总页数判断有没有下一页
    protected int page = 1;

    protected abstract List getData();

    private Context mContext;

    @Override
    protected void initView(Bundle savedInstanceState) {
        this.mContext = BaseListActivity.this;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLoadingListener(this);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh(boolean isRefresh) {
        page = 1;
        if (isRefresh == true) {
            requestData(page);
        }
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMore() {
        LogUtil.i("上拉加载更多="+getData().size());
        if (getData() != null && getData().size() >= total) {
            new Handler().post(new Runnable() {
                public void run() {
                    ToastUtil.show(mContext, mContext.getResources().getString(R.string.no_more_data));
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
    public void onClickEmptyView() {
        requestData(page);
    }

    /**
     * 无数据
     */
    protected void onNoData() {
        if (getData().size() == 0) {
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
    protected void onNetError() {
        if (getData().size() == 0) {
            tv_no_data.setVisibility(View.GONE);
            recyclerView.setEmptyView(tv_no_net);
        }
    }

}
