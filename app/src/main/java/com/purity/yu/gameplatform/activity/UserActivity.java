package com.purity.yu.gameplatform.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.controler.UserController;

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
    @BindView(R.id.iv_msg)
    ImageView iv_msg;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_money)
    TextView tv_money;

    @Override
    protected void initView(Bundle savedInstanceState) {
        UserController.getInstance().init(this);
        UserController.getInstance().copyWidget(iv_close, iv_msg);
        tv_nickname.setText(SharedPreUtil.getInstance(this).getString(Constant.USER_NAME));
    }
}
