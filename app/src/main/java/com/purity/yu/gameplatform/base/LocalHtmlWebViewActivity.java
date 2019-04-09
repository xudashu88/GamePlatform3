package com.purity.yu.gameplatform.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.fragment.BacRuleFragment;
import com.purity.yu.gameplatform.fragment.DtRuleFragment;
import com.purity.yu.gameplatform.widget.popup.PopupWindow.CommonPopupWindow;

import butterknife.BindView;
import butterknife.OnClick;

@ContentView(R.layout.activity_local_html_web_view)
public class LocalHtmlWebViewActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {


    @BindView(R.id.rl_back)
    public RelativeLayout rl_back;
    @BindView(R.id.rl_rule)
    public RelativeLayout rl_rule;
    @BindView(R.id.iv_rule)
    public ImageView iv_rule;

    private int selectBac = 0;
    private CommonPopupWindow popupWindow;
    private Context mContext;
    private Fragment currentFragment = new Fragment();

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = this;
        selectBac = getIntent().getIntExtra("selectBac",0);
        LogUtil.i("selectBac="+selectBac);
        if (selectBac == 0) {
            switchFragment(new BacRuleFragment()).commit();
        } else {
            switchFragment(new DtRuleFragment()).commit();
        }
        initEvent();
    }

    private void initEvent() {
        rl_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_rule.setRotation(0f);//图片向上旋转
                downPopupWindow(v);
            }
        });
    }

    private void downPopupWindow(View v) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.pop_game_rule)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setOutsideTouchable(true)
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAsDropDown(v);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_rule.setRotation(180f);//图片向上旋转
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.pop_game_rule:
                final LinearLayout ll_rule_bac = (LinearLayout) view.findViewById(R.id.ll_rule_bac);
                final LinearLayout ll_rule_dt = (LinearLayout) view.findViewById(R.id.ll_rule_dt);
                final TextView tv_rule_bac = (TextView) view.findViewById(R.id.tv_rule_bac);
                final TextView tv_rule_dt = (TextView) view.findViewById(R.id.tv_rule_dt);
                if (selectBac == 0) {
                    ll_rule_bac.setBackgroundResource(R.color.coffee);
                    tv_rule_bac.setTextColor(mContext.getResources().getColor(R.color.white));
//                    switchFragment(new BacRuleFragment()).commit();
                } else {
                    ll_rule_dt.setBackgroundResource(R.color.coffee);
                    tv_rule_dt.setTextColor(mContext.getResources().getColor(R.color.white));
                }
                ll_rule_bac.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectBac = 0;
                        switchFragment(new BacRuleFragment()).commit();
                        ll_rule_bac.setBackgroundResource(R.color.coffee);
                        tv_rule_bac.setTextColor(mContext.getResources().getColor(R.color.white));
                        ll_rule_dt.setBackgroundResource(R.color.ruleBg);
                        tv_rule_dt.setTextColor(mContext.getResources().getColor(R.color.mainGolden));
                    }
                });
                ll_rule_dt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectBac = 1;
                        switchFragment(new DtRuleFragment()).commit();
                        ll_rule_dt.setBackgroundResource(R.color.coffee);
                        tv_rule_dt.setTextColor(mContext.getResources().getColor(R.color.white));
                        ll_rule_bac.setBackgroundResource(R.color.ruleBg);
                        tv_rule_bac.setTextColor(mContext.getResources().getColor(R.color.mainGolden));
                    }
                });
                break;
        }
    }

    //Fragment优化
    public FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = ((LocalHtmlWebViewActivity) mContext).getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fragment, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }
}
