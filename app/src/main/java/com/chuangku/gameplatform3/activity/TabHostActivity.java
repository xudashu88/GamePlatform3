package com.chuangku.gameplatform3.activity;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseActivity;
import com.chuangku.gameplatform3.base.BaseActivity2;
import com.chuangku.gameplatform3.controler.TabHostControler;

import butterknife.BindView;

@ContentView(R.layout.activity_tabhost)
public class TabHostActivity extends BaseActivity2 {
    @BindView(R.id.ll_hot_new)
    LinearLayout ll_hot_new;
    @BindView(R.id.ll_live)
    LinearLayout ll_live;
    @BindView(R.id.ll_arcade)
    LinearLayout ll_arcade;
    @BindView(R.id.ll_card)
    LinearLayout ll_card;
    @BindView(R.id.tv_hot_new)
    TextView tv_hot_new;
    @BindView(R.id.tv_live)
    TextView tv_live;
    @BindView(R.id.tv_arcade)
    TextView tv_arcade;
    @BindView(R.id.tv_card)
    TextView tv_card;
    @BindView(R.id.tv_online_count)
    TextView tv_online_count;

    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.iv_msg)
    ImageView iv_msg;
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.ll_user_info)
    LinearLayout ll_user_info;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.iv_set)
    ImageView iv_set;

    protected void initView() {
        TabHostControler.getInstance().init(this);
        TabHostControler.getInstance().copyWidget(ll_hot_new, ll_live, ll_arcade, ll_card, tv_hot_new, tv_live, tv_arcade, tv_card,
                tv_online_count,ll_back,iv_msg,iv_avatar,ll_user_info,tv_nickname,tv_money,iv_set);
    }
}
