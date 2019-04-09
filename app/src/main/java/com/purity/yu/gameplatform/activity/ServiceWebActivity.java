package com.purity.yu.gameplatform.activity;

import android.os.Bundle;

import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.widget.webView.X5WebView;

import butterknife.BindView;

@ContentView(R.layout.activity_webview)
public class ServiceWebActivity extends BaseActivity {

    @BindView(R.id.webView)
    X5WebView webView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        webView.loadUrl(Constant.SERVICE_WEB_URL);
    }
}
