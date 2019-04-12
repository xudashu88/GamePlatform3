package com.purity.yu.gameplatform.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.DepositActivity;
import com.purity.yu.gameplatform.activity.WithdrawActivity;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

@ContentView(R.layout.fragment_my_wallet)
public class MyWalletFragment extends BaseFragment {
    private static final String TAG = "MyWalletFragment";
    @BindView(R.id.tv_deposit)
    public TextView tv_deposit;
    @BindView(R.id.tv_withdraw)
    public TextView tv_withdraw;
    private Context mContext;

    @Override
    protected void initView(View view) {
        mContext = getActivity();
    }

    @OnClick({R.id.tv_deposit, R.id.tv_withdraw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_deposit:
                mContext.startActivity(new Intent(mContext, DepositActivity.class));
                break;
            case R.id.tv_withdraw:
                mContext.startActivity(new Intent(mContext, WithdrawActivity.class));
                break;
        }
    }
}
