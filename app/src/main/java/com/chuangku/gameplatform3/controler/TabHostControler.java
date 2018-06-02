package com.chuangku.gameplatform3.controler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.activity.TabHostActivity;
import com.chuangku.gameplatform3.activity.UserActivity;
import com.chuangku.gameplatform3.fragment.ArcadeFragment;
import com.chuangku.gameplatform3.fragment.CardFragment;
import com.chuangku.gameplatform3.fragment.HotNewFragment;
import com.chuangku.gameplatform3.fragment.LiveFragment;
import com.gangbeng.basemodule.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class TabHostControler {
    private Context mContext;
    private static TabHostControler instance = null;
    private long lastOnClickTime = 0;

    LinearLayout ll_hot_new;
    LinearLayout ll_live;
    LinearLayout ll_arcade;
    LinearLayout ll_card;
    TextView tv_hot_new;
    TextView tv_live;
    TextView tv_arcade;
    TextView tv_card;
    TextView tv_online_count;

    LinearLayout ll_back;
    ImageView iv_msg;
    ImageView iv_avatar;
    LinearLayout ll_user_info;
    TextView tv_nickname;
    TextView tv_money;
    ImageView iv_set;


    private List<TextView> textViewList = new ArrayList<>();
    private Fragment currentFragment = new Fragment();

    public void init(Context context) {
        this.mContext = context;

    }

    public static TabHostControler getInstance() {
        if (null == instance) {
            instance = new TabHostControler();
        }
        return instance;
    }

    public void copyWidget(LinearLayout ll_hot_new, LinearLayout ll_live, LinearLayout ll_arcade, LinearLayout ll_card, TextView tv_hot_new, TextView tv_live, TextView tv_arcade, TextView tv_card,
                           TextView tv_online_count, LinearLayout ll_back, ImageView iv_msg, ImageView iv_avatar, LinearLayout ll_user_info, TextView tv_nickname, TextView tv_money, ImageView iv_set) {
        this.ll_hot_new = ll_hot_new;
        this.ll_live = ll_live;
        this.ll_arcade = ll_arcade;
        this.ll_card = ll_card;
        this.tv_hot_new = tv_hot_new;
        this.tv_live = tv_live;
        this.tv_arcade = tv_arcade;
        this.tv_card = tv_card;
        this.tv_online_count = tv_online_count;
        this.ll_back = ll_back;
        this.iv_msg = iv_msg;
        this.iv_avatar = iv_avatar;
        this.ll_user_info = ll_user_info;
        this.tv_nickname = tv_nickname;
        this.tv_money = tv_money;
        this.iv_set = iv_set;
        addTextView(tv_hot_new);
        addTextView(tv_live);
        addTextView(tv_arcade);
        addTextView(tv_card);
        switchFragment(new HotNewFragment()).commit();
        initEvent();
    }

    private void initEvent() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long time = System.currentTimeMillis();
                if (time - lastOnClickTime > 2000) {
                    Toast.makeText(mContext, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
                    lastOnClickTime = time;
                    return;
                } else {
                    ((TabHostActivity)mContext).finish();
                }
            }
        });
        iv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mContext.startActivity(new Intent(mContext, UserActivity.class));
            }
        });
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, UserActivity.class));
            }
        });
        ll_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, UserActivity.class));
            }
        });
        iv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("再按一次退出iv_set");
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
                    ((LinearLayout) textViewList.get(i).getParent()).setBackground(mContext.getResources().getDrawable(R.color.transparent_background));//透明背景色
                    textViewList.get(i).setTextColor(mContext.getResources().getColor(R.color.mainGolden));
                }
                TextView _tv = (TextView) v;
                if (_tv != null) {//选中姿态
                    String _tvTag = (String) _tv.getTag();
                    ((LinearLayout) _tv.getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.lobby_menu_select));
                    _tv.setTextColor(mContext.getResources().getColor(R.color.mainGoldenDeep));
                    if (_tvTag.equals("tv_hot_new")) {
                        switchFragment(new HotNewFragment()).commit();//每次都创建新的
                    } else if (_tvTag.equals("tv_live")) {
                        switchFragment(new LiveFragment()).commit();
                    } else if (_tvTag.equals("tv_arcade")) {
                        switchFragment(new ArcadeFragment()).commit();
                    } else if (_tvTag.equals("tv_card")) {
                        switchFragment(new CardFragment()).commit();
                    }
                }
            }
        });
    }

    //Fragment优化
    public FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = ((TabHostActivity) mContext).getSupportFragmentManager().beginTransaction();
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
