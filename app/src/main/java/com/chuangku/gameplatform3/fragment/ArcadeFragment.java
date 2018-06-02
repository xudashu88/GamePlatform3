package com.chuangku.gameplatform3.fragment;

import android.os.Bundle;
import android.view.View;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseFragment;

@ContentView(R.layout.fragment_arcade)
public class ArcadeFragment extends BaseFragment {

    public static ArcadeFragment newInstance(String text){
        ArcadeFragment fragmentCommon=new ArcadeFragment();
        Bundle bundle=new Bundle();
        bundle.putString("text",text);
        fragmentCommon.setArguments(bundle);
        return fragmentCommon;
    }

    @Override
    protected void initView(View view) {

    }
}
