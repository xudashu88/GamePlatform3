package com.purity.yu.gameplatform.fragment;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chuangku.languagemodule.CommSharedUtil;
import com.chuangku.languagemodule.LanguageType;
import com.chuangku.languagemodule.MultiLanguageUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;

import butterknife.BindView;

/**
 * 乐虎游戏规则
 */
@ContentView(R.layout.fragment_bac_rule)
public class DtRuleFragment extends BaseFragment {

    @BindView(R.id.webView)
    public WebView myWebView;
    private String url = "";

    @Override
    protected void initView(View view) {
        initWebView();
    }

    private void initWebView() {
        int languageType = CommSharedUtil.getInstance(getActivity()).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_EN) {//3
            url = "file:///android_asset/DragonTiger_en.html";
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED||languageType == 0) {//1
            url = "file:///android_asset/DragonTiger_zh.html";
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL){//2
            url = "file:///android_asset/DragonTiger_zh_rTW.html";
        }
        try {
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            myWebView.getSettings().setAppCacheEnabled(false);
            myWebView.getSettings().setUseWideViewPort(true);
            myWebView.getSettings().setLoadWithOverviewMode(true);
            myWebView.destroyDrawingCache();
            myWebView.clearCache(false);
            String ua = myWebView.getSettings().getUserAgentString();
            myWebView.getSettings().setUserAgentString(ua + " wxhappwebview/1.1");
            myWebView.loadUrl(url);
            myWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                    view.loadUrl(url);
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }
}
