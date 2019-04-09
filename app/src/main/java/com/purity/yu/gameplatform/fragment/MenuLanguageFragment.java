package com.purity.yu.gameplatform.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chuangku.languagemodule.CommSharedUtil;
import com.chuangku.languagemodule.LanguageType;
import com.chuangku.languagemodule.MultiLanguageUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.SettingActivity;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.controler.SettingController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@ContentView(R.layout.fragment_menu_language)
public class MenuLanguageFragment extends BaseFragment {

    @BindView(R.id.rl_language_zh)
    RelativeLayout rl_language_zh;
    @BindView(R.id.rl_language_zh_tw)
    RelativeLayout rl_language_zh_tw;
    @BindView(R.id.rl_language_en)
    RelativeLayout rl_language_en;
    @BindView(R.id.iv_language_zh)
    ImageView iv_language_zh;
    @BindView(R.id.iv_language_zh_tw)
    ImageView iv_language_zh_tw;
    @BindView(R.id.iv_language_en)
    ImageView iv_language_en;

    private List<RelativeLayout> relativeLayoutList = new ArrayList<>();
    int selectedLanguage = 0;

    @Override
    protected void initView(View view) {
        addRelativeLayout(rl_language_zh);
        addRelativeLayout(rl_language_zh_tw);
        addRelativeLayout(rl_language_en);
        SettingController.getInstance().init(getActivity());
        initLanguage();
    }

    private void initLanguage() {
        int languageType = CommSharedUtil.getInstance(getActivity()).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_EN) {
            iv_language_en.setImageResource(R.drawable.language_tick);
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED || languageType == 0) {
            iv_language_zh.setImageResource(R.drawable.language_tick);
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            iv_language_zh_tw.setImageResource(R.drawable.language_tick);
        }
    }

    private void addRelativeLayout(RelativeLayout rl) {
        relativeLayoutList.add(rl);
        setOnClick(rl);
    }

    private void setOnClick(RelativeLayout rl) {
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout _tv = (RelativeLayout) v;
                for (int i = 0; i < relativeLayoutList.size(); i++) {
                    ImageView _iv = (ImageView) relativeLayoutList.get(i).getChildAt(1);
                    _iv.setImageResource(0);
                }
                if (_tv != null) {
                    String _tag = (String) _tv.getTag();
                    ImageView _iv = (ImageView) _tv.getChildAt(1);
                    _iv.setImageResource(R.drawable.language_tick);
                    if (_tag.equals("rl_language_zh")) {
                        selectedLanguage = LanguageType.LANGUAGE_CHINESE_SIMPLIFIED;
                    } else if (_tag.equals("rl_language_zh_tw")) {
                        selectedLanguage = LanguageType.LANGUAGE_CHINESE_TRADITIONAL;
                    } else if (_tag.equals("rl_language_en")) {
                        selectedLanguage = LanguageType.LANGUAGE_EN;
                    }
                    //由于选择语言后，SettingActivity已不是原来的Activity,这里需要通过标识，进到原来的Activity，走onCreate
                    int _whereSet = SharedPreUtil.getInstance(getActivity()).getInt(Constant.WHERE_SET);
                    if (_whereSet == 1) {
                        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.WHERE_SETED, 1);
                    } else if (_whereSet == 2) {
                        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.WHERE_SETED, 2);
                    } else if (_whereSet == 3) {
                        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.WHERE_SETED, 3);
                    }
                    MultiLanguageUtil.getInstance().updateLanguage(selectedLanguage);
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}
