package com.purity.yu.gameplatform.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.chuangku.languagemodule.MultiLanguageUtil;
import com.purity.yu.gameplatform.annotation.ContentViewUtils;

import butterknife.ButterKnife;

public class BaseLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ContentViewUtils.inject(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }
}
