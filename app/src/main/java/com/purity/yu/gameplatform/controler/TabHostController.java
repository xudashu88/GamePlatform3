package com.purity.yu.gameplatform.controler;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.BetRecordListActivity;
import com.purity.yu.gameplatform.activity.MessageListActivity;
import com.purity.yu.gameplatform.activity.QuotaRecordListActivity;
import com.purity.yu.gameplatform.activity.SettingActivity;
import com.purity.yu.gameplatform.activity.SuggestionActivity;
import com.purity.yu.gameplatform.activity.TabHostActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.LocalHtmlWebViewActivity;
import com.purity.yu.gameplatform.fragment.ArcadeFragment;
import com.purity.yu.gameplatform.fragment.ContactServeFragment;
import com.purity.yu.gameplatform.fragment.HotNewFragment;
import com.purity.yu.gameplatform.fragment.LiveFragment;
import com.purity.yu.gameplatform.fragment.MyWalletFragment;
import com.purity.yu.gameplatform.fragment.SecurityAccountFragment;
import com.purity.yu.gameplatform.fragment.WithDrawFragment;
import com.purity.yu.gameplatform.service.MusicService;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.utils.SoundPoolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class TabHostController {
    private Context mContext;
    private static TabHostController instance = null;
    private long lastOnClickTime = 0;
    private LayoutInflater layoutInflater;

    LinearLayout ll_hot_new;
    LinearLayout ll_live;
    LinearLayout ll_arcade;
    LinearLayout ll_card;
    LinearLayout ll_security_account;
    LinearLayout ll_WeChat_link;
    TextView tv_hot_new;
    TextView tv_live;
    TextView tv_arcade;
    TextView tv_card;
    TextView tv_card2;
    TextView tv_security_account;
    TextView tv_WeChat_link;
    TextView tv_service_web;
    TextView tv_deposit;
    TextView tv_withdraw;
    TextView tv_recycle;

    LinearLayout ll_back;
    ImageView iv_sound_switch;
    LinearLayout ll_user_info;
    TextView tv_nickname;
    TextView tv_money;
    ImageView iv_bac_set;
    Timer timer;

    private List<TextView> textViewList = new ArrayList<>();
    private Fragment currentFragment = new Fragment();

    //当前显示的fragment
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    private FragmentManager fragmentManager;
    public int currentIndex = 0;
    private List<Fragment> fragments = new ArrayList<>();
    private TextView tv_dot;

    public void init(Context context) {
        this.mContext = context;
        layoutInflater = ((TabHostActivity) mContext).getWindow().getLayoutInflater();
        fragmentManager = ((TabHostActivity) mContext).getSupportFragmentManager();
    }

    public static TabHostController getInstance() {
        if (null == instance) {
            instance = new TabHostController();
        }
        return instance;
    }

    public void copyWidget(LinearLayout ll_hot_new, LinearLayout ll_live, LinearLayout ll_arcade, LinearLayout ll_card, LinearLayout ll_security_account,LinearLayout ll_WeChat_link,
                           TextView tv_hot_new, TextView tv_live, TextView tv_arcade, TextView tv_card,TextView tv_card2, TextView tv_security_account,TextView tv_WeChat_link,
                           LinearLayout ll_back, ImageView iv_sound_switch, LinearLayout ll_user_info, TextView tv_nickname, TextView tv_money, ImageView iv_bac_set, Bundle savedInstanceState,
            /*TextView tv_service_web*/TextView tv_deposit, TextView tv_withdraw, TextView tv_recycle,
                           Timer timer) {
        this.ll_hot_new = ll_hot_new;
        this.ll_live = ll_live;
        this.ll_arcade = ll_arcade;
        this.ll_card = ll_card;
        this.ll_security_account = ll_security_account;
        this.ll_WeChat_link = ll_WeChat_link;
        this.tv_hot_new = tv_hot_new;
        this.tv_live = tv_live;
        this.tv_arcade = tv_arcade;
        this.tv_card = tv_card;
        this.tv_card2 = tv_card2;
        this.tv_security_account = tv_security_account;
        this.tv_WeChat_link = tv_WeChat_link;
        this.ll_back = ll_back;
        this.iv_sound_switch = iv_sound_switch;
        this.ll_user_info = ll_user_info;
        this.tv_nickname = tv_nickname;
        this.tv_money = tv_money;
        this.iv_bac_set = iv_bac_set;
        this.tv_service_web = tv_service_web;
        this.tv_deposit = tv_deposit;
        this.tv_withdraw = tv_withdraw;
        this.tv_recycle = tv_recycle;
        this.timer = timer;
        LogUtil.i("HotNewFragment TabHostController" + savedInstanceState);
        if (savedInstanceState != null) { // “内存重启”时调用
            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);
            fragments.removeAll(fragments);
            fragments.add(fragmentManager.findFragmentByTag(0 + ""));
            fragments.add(fragmentManager.findFragmentByTag(1 + ""));
            fragments.add(fragmentManager.findFragmentByTag(2 + ""));
            fragments.add(fragmentManager.findFragmentByTag(3 + ""));
            fragments.add(fragmentManager.findFragmentByTag(4 + ""));
            fragments.add(fragmentManager.findFragmentByTag(5 + ""));
            //恢复fragment页面
            restoreFragment();
        } else {      //正常启动时调用
            fragments.add(new HotNewFragment());
            fragments.add(new LiveFragment());
            fragments.add(new ArcadeFragment());
            fragments.add(new MyWalletFragment());
            fragments.add(new WithDrawFragment());
            fragments.add(new SecurityAccountFragment());
            fragments.add(new ContactServeFragment());
            showFragment();
            switchFragment(new HotNewFragment()).commit();//应用内部切换语言后，第一个要显示
        }
        addTextView(tv_hot_new);
        addTextView(tv_live);
        addTextView(tv_arcade);
        addTextView(tv_card);
        addTextView(tv_card2);
        addTextView(tv_security_account);
        addTextView(tv_WeChat_link);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.WHERE_SET, 0);//从新开始（从DtListActivity中的语言选择后归来）
        initEvent();
    }

    private void initEvent() {
        tv_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new MyWalletFragment()).commit();
                toMyWallet();
            }
        });
        tv_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new MyWalletFragment()).commit();
                toMyWallet();
            }
        });
        tv_recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolUtil.getInstance().postHJRecycle(tv_money);
            }
        });
//        tv_service_web.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                String data = "http://103.231.167.85:8888/mobile/index.html";
//                intent.setData(Uri.parse(data));
//                mContext.startActivity(intent);
//            }
//        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long time = System.currentTimeMillis();
                if (time - lastOnClickTime > 2000) {
                    Toast.makeText(mContext, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
                    lastOnClickTime = time;
                    return;
                } else {
                    mContext.stopService(new Intent(mContext, MusicService.class));
                    ProtocolUtil.getInstance().postLoginOut(mContext, timer);
                }
            }
        });
        iv_sound_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 关闭音效和背景音乐 再按一次只打开音效
                if (SharedPreUtil.getInstance(mContext).getInt(Constant.SOUND_SWITCH) == 0) {
                    iv_sound_switch.setBackgroundResource(R.drawable.off);
                    SoundPoolUtil.getInstance().release();//关闭音效，默认开启
                    if (mService != null) {
                        mService.pauseMusic();//只暂停
                    }
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.SOUND_SWITCH, 1);
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.MUSIC_SWITCH, 1);
                } else if (SharedPreUtil.getInstance(mContext).getInt(Constant.SOUND_SWITCH) == 1) {
                    iv_sound_switch.setBackgroundResource(R.drawable.on);
                    BaccaratUtil.getInstance().openSound(mContext);
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.SOUND_SWITCH, 0);
//                    SharedPreUtil.getInstance(mContext).saveParam(Constant.MUSIC_SWITCH, 2);
                }
            }
        });
        ll_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, UserActivity.class));
            }
        });
        iv_bac_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.getInstance(mContext).saveParam(Constant.WHERE_SET, 1);
                initMenu(mContext, 0, timer);
            }
        });
    }

    //--------------------------------------------Service start--------------------------------------------------
    private MusicService mService = null;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) binder;
            mService = musicBinder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };
    //--------------------------------------------Service end--------------------------------------------------

    private void toMyWallet() {
        int _size = textViewList.size();
        for (int i = 0; i < _size; i++) {
            ((LinearLayout) textViewList.get(i).getParent()).setBackground(mContext.getResources().getDrawable(R.color.transparent_background));//透明背景色
            textViewList.get(i).setTextColor(mContext.getResources().getColor(R.color.mainGolden));
        }
        ((LinearLayout) tv_card.getParent()).setBackground(mContext.getResources().getDrawable(R.drawable.lobby_menu_select));
        tv_card.setTextColor(mContext.getResources().getColor(R.color.mainGoldenDeep));
    }

    public void initMenu(final Context mContext, final int selectBac, final Timer timer) {
        final View layout = layoutInflater.inflate(R.layout.dialog_menu, null);
        final AlertDialog menu = Util.setDialog(mContext, R.style.dialog, layout, 0.21f, 1f, 0.5f, 0, 0, Gravity.RIGHT, Gravity.TOP, R.style.AnimRight);
        RelativeLayout rl_menu_records = (RelativeLayout) layout.findViewById(R.id.rl_menu_records);
        RelativeLayout rl_menu_setting = (RelativeLayout) layout.findViewById(R.id.rl_menu_setting);
        RelativeLayout rl_menu_suggestion = (RelativeLayout) layout.findViewById(R.id.rl_menu_suggestion);
        RelativeLayout rl_menu_game_rule = (RelativeLayout) layout.findViewById(R.id.rl_menu_game_rule);
        RelativeLayout rl_menu_message = (RelativeLayout) layout.findViewById(R.id.rl_menu_message);
        RelativeLayout rl_menu_exit = (RelativeLayout) layout.findViewById(R.id.rl_menu_exit);
        tv_dot = (TextView) layout.findViewById(R.id.tv_dot);
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.WHERE_SET) != 1) {
            rl_menu_exit.setVisibility(View.GONE);
        }
        int unRead = SharedPreUtil.getInstance(mContext).getInt("unRead");
        if (unRead > 0) {
            tv_dot.setVisibility(View.VISIBLE);
            tv_dot.setText(String.valueOf(unRead));
        } else {
            tv_dot.setVisibility(View.GONE);
        }

        rl_menu_records.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View layout = layoutInflater.inflate(R.layout.dialog_menu_records, null);
                final AlertDialog records = Util.setDialog(mContext, R.style.dialog, layout, 0.21f, 1f, 0.5f, 0, 0, Gravity.RIGHT, Gravity.TOP, R.style.AnimRight);
                RelativeLayout rl_menu_title = (RelativeLayout) layout.findViewById(R.id.rl_menu_title);
                RelativeLayout rl_menu_bet_record = (RelativeLayout) layout.findViewById(R.id.rl_menu_bet_record);
                RelativeLayout rl_menu_quota_record = (RelativeLayout) layout.findViewById(R.id.rl_menu_quota_record);
                records.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        records.dismiss();
                        menu.dismiss();
                    }
                });
                rl_menu_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        records.hide();
                    }
                });
                rl_menu_bet_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, BetRecordListActivity.class));
                    }
                });
                rl_menu_quota_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, QuotaRecordListActivity.class));
                    }
                });

            }
        });
        rl_menu_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SettingActivity.class));
            }
        });
        rl_menu_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SuggestionActivity.class));
            }
        });
        rl_menu_game_rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, LocalHtmlWebViewActivity.class).putExtra("selectBac", selectBac));
            }
        });
        rl_menu_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MessageListActivity.class));
            }
        });
        rl_menu_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolUtil.getInstance().postLoginOut(mContext, timer);
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
                        currentIndex = 0;
                    } else if (_tvTag.equals("tv_live")) {
                        currentIndex = 1;//隐藏了
                    } else if (_tvTag.equals("tv_arcade")) {
                        currentIndex = 2;//隐藏了
                    } else if (_tvTag.equals("tv_card")) {
                        currentIndex = 3;
                    } else if (_tvTag.equals("tv_card2")) {
                        currentIndex = 4;
                    } else if (_tvTag.equals("tv_security_account")) {
                        currentIndex = 5;
                    }else if (_tvTag.equals("tv_WeChat_link")) {
                        currentIndex = 6;
                    }
                    showFragment();
                }
            }
        });
    }

    //Fragment优化  todo https://blog.csdn.net/adzcsx2/article/details/51788613  https://blog.csdn.net/fan7983377/article/details/51889269 切换多国语言导致Fragment被回收，出现切换错乱
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

    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //如果之前没有添加过
        if (!fragments.get(currentIndex).isAdded()) {
            transaction.hide(currentFragment).add(R.id.fragment, fragments.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag
        } else {
            transaction.hide(currentFragment).show(fragments.get(currentIndex));
        }
        currentFragment = fragments.get(currentIndex);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * 恢复fragment
     */
    private void restoreFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == currentIndex) {
                transaction.show(fragments.get(i));
            } else {
                transaction.hide(fragments.get(i));
            }
        }
//        transaction.addToBackStack(null);
        transaction.commit();
        //把当前显示的fragment记录下来
        currentFragment = fragments.get(currentIndex);
    }
}
