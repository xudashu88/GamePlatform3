package com.purity.yu.gameplatform.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.widget.webView.X5WebView;

import butterknife.BindView;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/7/11.
 */
@ContentView(R.layout.activity_suggestion)
public class SuggestionActivity extends BaseActivity {
    @BindView(R.id.rl_back)
    RelativeLayout rl_back;
    @BindView(R.id.webView)
    X5WebView webView;
    private Context mContext;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = SuggestionActivity.this;
        initEvent();
        webView.loadUrl("http://kefu.ziyun.com.cn/vclient/chat/?websiteid=139750");
    }

    private void initEvent() {
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SuggestionActivity) mContext).finish();
            }
        });

    }
}
