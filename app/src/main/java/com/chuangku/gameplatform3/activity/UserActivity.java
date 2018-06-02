package com.chuangku.gameplatform3.activity;

import android.widget.ImageView;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseActivity;
import com.chuangku.gameplatform3.controler.UserControler;

import butterknife.BindView;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/21.
 */
@ContentView(R.layout.activity_user)
public class UserActivity extends BaseActivity {
    @BindView(R.id.iv_close)
    ImageView iv_close;

    @Override
    protected void initView() {
        UserControler.getInstance().init(this);
        UserControler.getInstance().copyWidget(iv_close);
    }


}
