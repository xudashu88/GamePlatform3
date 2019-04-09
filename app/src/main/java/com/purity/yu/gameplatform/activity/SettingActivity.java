package com.purity.yu.gameplatform.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.controler.SettingController;

import butterknife.BindView;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/7/7.
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {
    @BindView(R.id.rl_menu_title)
    RelativeLayout rl_menu_title;
    @BindView(R.id.rl_menu_chips)
    RelativeLayout rl_menu_chips;
    @BindView(R.id.rl_menu_language)
    RelativeLayout rl_menu_language;
    @BindView(R.id.rl_menu_music)
    RelativeLayout rl_menu_music;
    @BindView(R.id.rl_menu_other)
    RelativeLayout rl_menu_other;
    @BindView(R.id.tv_menu_chips)
    TextView tv_menu_chips;
    @BindView(R.id.tv_menu_language)
    TextView tv_menu_language;
    @BindView(R.id.tv_menu_music)
    TextView tv_menu_music;
    @BindView(R.id.tv_menu_other)
    TextView tv_menu_other;


    @Override
    protected void initView(Bundle savedInstanceState) {
        SettingController.getInstance().init(this);
        SettingController.getInstance().copyWidget(rl_menu_title,rl_menu_chips,rl_menu_language,rl_menu_music,rl_menu_other,tv_menu_chips,tv_menu_language,tv_menu_music,tv_menu_other);
    }
}
