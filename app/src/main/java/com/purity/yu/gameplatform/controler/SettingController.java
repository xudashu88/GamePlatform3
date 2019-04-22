package com.purity.yu.gameplatform.controler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.BaccaratListActivity;
import com.purity.yu.gameplatform.activity.DtListActivity;
import com.purity.yu.gameplatform.activity.SettingActivity;
import com.purity.yu.gameplatform.activity.TabHostActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.fragment.MenuChipsFragment;
import com.purity.yu.gameplatform.fragment.MenuLanguageFragment;
import com.purity.yu.gameplatform.fragment.MenuMusicFragment;
import com.purity.yu.gameplatform.fragment.MenuOtherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class SettingController {
    private Context mContext;
    private static SettingController instance = null;
    private List<TextView> textViewList = new ArrayList<>();
    private Fragment currentFragment = new Fragment();

    RelativeLayout rl_menu_title;
    RelativeLayout rl_menu_chips;
    RelativeLayout rl_menu_language;
    RelativeLayout rl_menu_music;
    RelativeLayout rl_menu_other;
    TextView tv_menu_chips;
    TextView tv_menu_language;
    TextView tv_menu_music;
    TextView tv_menu_other;

    public void init(Context context) {
        mContext = context;
    }

    public static SettingController getInstance() {
        if (null == instance) {
            instance = new SettingController();
        }
        return instance;
    }

    public void copyWidget(RelativeLayout rl_menu_title, RelativeLayout rl_menu_chips, RelativeLayout rl_menu_language, RelativeLayout rl_menu_music, RelativeLayout rl_menu_other,
                           TextView tv_menu_chips, TextView tv_menu_language, TextView tv_menu_music, TextView tv_menu_other) {
        this.rl_menu_title = rl_menu_title;
        this.rl_menu_chips = rl_menu_chips;
        this.rl_menu_language = rl_menu_language;
        this.rl_menu_music = rl_menu_music;
        this.rl_menu_other = rl_menu_other;
        this.tv_menu_chips = tv_menu_chips;
        this.tv_menu_language = tv_menu_language;
        this.tv_menu_music = tv_menu_music;
        this.tv_menu_other = tv_menu_other;
        addTextView(tv_menu_chips);
        addTextView(tv_menu_language);
        addTextView(tv_menu_music);
        addTextView(tv_menu_other);
        switchFragment(new MenuChipsFragment()).commit();
        initEvent();
        initView();
    }

    private void initView() {
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.WHERE_SET) == 4) {
            rl_menu_language.setVisibility(View.GONE);
        }
    }

    private void initEvent() {
        rl_menu_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int _whereSeted = SharedPreUtil.getInstance(mContext).getInt(Constant.WHERE_SETED);

                if (_whereSeted == 1) {
                    mContext.startActivity(new Intent(mContext, TabHostActivity.class));
                } else if (_whereSeted == 2) {
                    mContext.startActivity(new Intent(mContext, BaccaratListActivity.class));
                } else if (_whereSeted == 3) {
                    mContext.startActivity(new Intent(mContext, DtListActivity.class));
                }
                ((SettingActivity) mContext).finish();
            }
        });
    }

    private void addTextView(TextView tv) {
        textViewList.add(tv);
        setOnClick(tv);
    }

    private void setOnClick(TextView tv) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int _size = textViewList.size();
                for (int i = 0; i < _size; i++) {
                    ((RelativeLayout) textViewList.get(i).getParent()).setBackgroundResource(R.color.menu);
                }
                TextView _tv = (TextView) v;
                if (_tv != null) {//选中姿态
                    String _tvTag = (String) _tv.getTag();
                    ((RelativeLayout) _tv.getParent()).setBackgroundResource(R.color.menuPress);
                    if (_tvTag.equals("tv_menu_chips")) {
                        switchFragment(new MenuChipsFragment()).commit();//每次都创建新的
                    } else if (_tvTag.equals("tv_menu_language")) {
                        switchFragment(new MenuLanguageFragment()).commit();
                    } else if (_tvTag.equals("tv_menu_music")) {
                        switchFragment(new MenuMusicFragment()).commit();
                    } else if (_tvTag.equals("tv_menu_other")) {
                        switchFragment(new MenuOtherFragment()).commit();
                    }
                }
            }
        });
    }

    //Fragment优化
    public FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = ((SettingActivity) mContext).getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fragment, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }
}
