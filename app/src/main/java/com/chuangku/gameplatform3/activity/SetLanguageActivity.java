package com.chuangku.gameplatform3.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseLanguageActivity;
import com.chuangku.gameplatform3.login.LoginActivity;
import com.chuangku.languagemodule.LanguageType;
import com.chuangku.languagemodule.MultiLanguageUtil;

import butterknife.BindView;
import butterknife.OnClick;

@ContentView(R.layout.activity_set_language)
public class SetLanguageActivity extends BaseLanguageActivity {

    @BindView(R.id.ll_simple_chinese)
    LinearLayout ll_simple_chinese;
    @BindView(R.id.ll_traditional_chinese)
    LinearLayout ll_traditional_chinese;
    @BindView(R.id.ll_english)
    LinearLayout ll_english;

    @OnClick({R.id.ll_simple_chinese, R.id.ll_traditional_chinese, R.id.ll_english})
    public void onClick(View view) {
        int selectedLanguage = 0;
        switch (view.getId()) {
            case R.id.ll_simple_chinese:
                selectedLanguage = LanguageType.LANGUAGE_CHINESE_SIMPLIFIED;
                break;
            case R.id.ll_traditional_chinese:
                selectedLanguage = LanguageType.LANGUAGE_CHINESE_TRADITIONAL;
                break;
            case R.id.ll_english:
                selectedLanguage = LanguageType.LANGUAGE_EN;
                break;
        }

        MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
        Intent intent = new Intent(SetLanguageActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
