package com.purity.yu.gameplatform.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.controler.TabHostController;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.MathRandomUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.widget.MarqueeTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

@ContentView(R.layout.activity_tabhost)
//@Xml(layouts = "activity_tabhost")
public class TabHostActivity extends BaseActivity {
    @BindView(R.id.ll_hot_new)
    LinearLayout ll_hot_new;
    @BindView(R.id.ll_live)
    LinearLayout ll_live;
    @BindView(R.id.ll_arcade)
    LinearLayout ll_arcade;
    @BindView(R.id.ll_card)
    LinearLayout ll_card;
    @BindView(R.id.ll_security_account)
    LinearLayout ll_security_account;
    @BindView(R.id.ll_WeChat_link)
    LinearLayout ll_WeChat_link;
    @BindView(R.id.tv_hot_new)
    TextView tv_hot_new;
    @BindView(R.id.tv_live)
    TextView tv_live;
    @BindView(R.id.tv_arcade)
    TextView tv_arcade;
    @BindView(R.id.tv_card)
    TextView tv_card;
    @BindView(R.id.tv_card2)
    TextView tv_card2;
    @BindView(R.id.tv_security_account)
    TextView tv_security_account;
    @BindView(R.id.tv_WeChat_link)
    TextView tv_WeChat_link;
    @BindView(R.id.tv_online_count)
    TextView tv_online_count;
    @BindView(R.id.tv_profit)
    TextView tv_profit;
    @BindView(R.id.tv_bet_count)
    TextView tv_bet_count;
    @BindView(R.id.tv_deposit)
    TextView tv_deposit;
    @BindView(R.id.tv_withdraw)
    TextView tv_withdraw;
    @BindView(R.id.tv_recycle)
    TextView tv_recycle;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.iv_sound_switch)
    ImageView iv_sound_switch;
    @BindView(R.id.ll_user_info)
    LinearLayout ll_user_info;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.iv_bac_set)
    ImageView iv_bac_set;
    @BindView(R.id.tv_dot)
    TextView tv_dot;
    @BindView(R.id.iv_WeChat)
    ImageView iv_WeChat;
    @BindView(R.id.marquee)
    MarqueeTextView marquee;
    @BindView(R.id.yellowSpeaker)
    ImageView yellowSpeaker;
    @BindView(R.id.ll_tab_host)
    LinearLayout ll_tab_host;
    private Context mContext;
    private long lastOnClickTime = 0;
    private Timer timer;

    static {
        System.loadLibrary("SmartPlayer");//大牛直播
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = TabHostActivity.this;
        tv_nickname.setText(SharedPreUtil.getInstance(this).getString(Constant.USER_NAME));
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.LOGIN_TYPE) == 1) {
            Glide.with(mContext).load(SharedPreUtil.getInstance(mContext).getString(Constant.WECHAT_ICON)).into(iv_WeChat);
        } else {
            iv_WeChat.setBackground(null);

        }
        timer = new Timer(true);
        EventBus.getDefault().register(this);
        TabHostController.getInstance().init(this);
        TabHostController.getInstance().copyWidget(ll_hot_new, ll_live, ll_arcade, ll_card, ll_security_account, ll_WeChat_link,
                tv_hot_new, tv_live, tv_arcade, tv_card, tv_security_account, tv_WeChat_link,
                ll_back, iv_sound_switch, ll_user_info, tv_nickname, tv_money, iv_bac_set, savedInstanceState, tv_deposit, tv_withdraw, tv_recycle, timer);
        ProtocolUtil.getInstance().getGold(null, tv_money, mContext);
        perFivePerformance();
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.SOUND_SWITCH) == 0) {
            iv_sound_switch.setBackgroundResource(R.drawable.on);
        } else {
            iv_sound_switch.setBackgroundResource(R.drawable.off);
        }
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.VERSION) == 1) {
            ll_tab_host.setBackgroundResource(R.drawable.bg_gameroom);
            ll_arcade.setVisibility(View.GONE);
            ll_WeChat_link.setVisibility(View.VISIBLE);
        } else {
            ll_tab_host.setBackgroundResource(R.drawable.start_bg);
            ll_arcade.setVisibility(View.VISIBLE);
            ll_WeChat_link.setVisibility(View.GONE);
        }
        ProtocolUtil.getInstance().getSysNoticeList(mContext, marquee);
        tv_nickname.setTypeface(null);
    }

    /**
     * 每5秒执行一次
     */
    private void perFivePerformance() {
        TimerTask task = new TimerTask() {
            public void run() {
                ProtocolUtil.getInstance().getNotice(mContext, tv_money, marquee);
                ProtocolUtil.getInstance().getNoRead(mContext, tv_dot);
                ProtocolUtil.getInstance().getAccount2(tv_online_count, tv_profit, tv_bet_count);
            }
        };
        timer.schedule(task, 1000, 10000);
    }

    //当前显示的fragment
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //“内存重启”时保存当前的fragment名字
        outState.putInt(CURRENT_FRAGMENT, TabHostController.getInstance().currentIndex);
        super.onSaveInstanceState(outState);
    }

    /*
     * 余额
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventMoneyChange(final ObjectEvent.MoneyChangeEvent event) {
        final String money = MathRandomUtil.subString(event.money);
        tv_money.setText(event.money);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProtocolUtil.getInstance().getGold(null, tv_money, mContext);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        perFivePerformance();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        timer.cancel();//离开主页暂停获取消息轮询
    }

    @Override
    public void onBackPressed() {
        long time = System.currentTimeMillis();
        if (time - lastOnClickTime > 2000) {
            Toast.makeText(this, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
            lastOnClickTime = time;
            return;
        } else {
            ProtocolUtil.getInstance().postLogOutSys(mContext);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        timer.cancel();//离开主页暂停获取消息轮询
    }
}
