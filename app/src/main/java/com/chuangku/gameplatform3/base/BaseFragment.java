package com.chuangku.gameplatform3.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chuangku.gameplatform3.annotation.ContentViewUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Fragment 基类
 */
public abstract class BaseFragment extends Fragment {

    public Toolbar toolbar;
    Unbinder unbinder;
    View rootView;

    public void setToolBar(Toolbar toolBar) {
        this.toolbar = toolBar;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected abstract void initView(View view);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = ContentViewUtils.inject(this);
        if (view != null) {
            unbinder = ButterKnife.bind(this, view);
            return view;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Context context;
        context = getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
