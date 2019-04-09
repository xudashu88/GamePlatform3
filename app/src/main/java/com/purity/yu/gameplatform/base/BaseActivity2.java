package com.purity.yu.gameplatform.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.chuangku.languagemodule.MultiLanguageUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.annotation.ContentViewUtils;

import butterknife.ButterKnife;

/**
 * Activity基类
 */
public abstract class BaseActivity2 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ContentViewUtils.inject(this);
        ButterKnife.bind(this);
        Util.getInstance().init(this);
        Util.getInstance().hideSystemNavigationBar();
        initView(savedInstanceState);
    }

    protected abstract void initView(Bundle savedInstanceState);

    long time = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出游戏", Toast.LENGTH_SHORT).show();
            time = System.currentTimeMillis();
        } else {
            finish();
            super.onBackPressed();
        }
    }

    /**
     * i18n
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    /**
     * activity.finish()之后，调用
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        Util.getInstance().init(this);
        Util.getInstance().hideSystemNavigationBar();//关闭虚拟键盘
    }
}
