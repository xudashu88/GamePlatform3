package com.purity.yu.gameplatform.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.controler.TabHostController;
import com.purity.yu.gameplatform.entity.Bank;
import com.purity.yu.gameplatform.entity.Notice;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.MathRandomUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;

import static com.gangbeng.basemodule.http.HttpParse.parseArrayObject;

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
    @BindView(R.id.simpleMarqueeView)
    SimpleMarqueeView<String> simpleMarqueeView;
    @BindView(R.id.yellowSpeaker)
    ImageView yellowSpeaker;
    private Context mContext;
    private long lastOnClickTime = 0;
    private List<String> marqueeList = new ArrayList<>();
    SimpleMF<String> marqueeFactory;
    List<Notice> noticeList = new ArrayList<>();

    static {
        System.loadLibrary("SmartPlayer");//大牛直播
    }

    private Timer timer;

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
                tv_hot_new, tv_live, tv_arcade, tv_card, tv_card2, tv_security_account, tv_WeChat_link,
                ll_back, iv_sound_switch, ll_user_info, tv_nickname, tv_money, iv_bac_set, savedInstanceState, tv_deposit, tv_withdraw, tv_recycle, timer);
        getGold(null, tv_money, mContext);
        perFivePerformance();
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.SOUND_SWITCH) == 0) {
            iv_sound_switch.setBackgroundResource(R.drawable.on);
        } else {
            iv_sound_switch.setBackgroundResource(R.drawable.off);
        }
        marqueeFactory = new SimpleMF(mContext);
//        initMarquee();
        getSysNoticeList();
        tv_nickname.setTypeface(null);
    }

    private void initMarquee(String message) {
//        DrawableCompat.setTint(DrawableCompat.wrap(yellowSpeaker.getDrawable().mutate()), getResources().getColor(R.color.yellow));
//        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.ADDS))) {
//            marqueeList = Arrays.asList(SharedPreUtil.getInstance(mContext).getString(Constant.ADDS), "");
//        }
        marqueeList.clear();
        marqueeList = Arrays.asList(message, "");
        marqueeFactory.setData(marqueeList);
        simpleMarqueeView.setMarqueeFactory(marqueeFactory);
        simpleMarqueeView.startFlipping();
    }


    /**
     * 每5秒执行一次
     */
    private void perFivePerformance() {
        TimerTask task = new TimerTask() {
            public void run() {
                getNotice(tv_money, mContext);
                getNoRead(tv_dot);
                getAccount2(tv_online_count, tv_profit, tv_bet_count);
            }
        };
        timer.schedule(task, 1000, 5000);
    }

    private void getSysNoticeList() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.SYS_NOTICE_LIST + "?token=" + token, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");
                            if (code != 0) {
                                return;
                            }
                            String _data = json.optString("data");
                            LogUtil.i("系统通告=" + json);
                            noticeList.clear();//清除,每一页都是新的
                            noticeList = parseArrayObject(_data, "items", Notice.class);

                            final String content = noticeList.get(noticeList.size() - 1).content;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtil.i("系统通告2=" + content);
                                    initMarquee(content);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        ToastUtil.show(mContext, "连接超时");
                    }
                })));
    }

    //当前显示的fragment
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";

    public void getGold(final String message, final TextView tv_money, final Context mContext) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(base + Constant.USER_INFO + "?token=" + token, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        JSONObject __user = null;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");

                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            String account = __data.optString("account");
                            __user = new JSONObject(account);
                            final String available_amount = __user.optString("available_amount");
                            LogUtil.i("getGold=" + json + " available_amount=" + available_amount);
                            if (!TextUtils.isEmpty(available_amount)) {
//                                final double gold = MathRandomUtil.formatDouble1(Double.parseDouble(available_amount));
                                final String money = MathRandomUtil.subString(available_amount);
                                tv_money.setText(money);
                                ((TabHostActivity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!TextUtils.isEmpty(message)) {
                                            ProtocolUtil.getInstance().hintDialog(mContext, message + ".余额为:" + available_amount);
                                            postEventMarqueeText(message, money);
                                        }
                                    }
                                });

                                getFree();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        ToastUtil.show(mContext, "连接超时");
                    }
                })));
    }

    private void postEventMarqueeText(String message, String money) {
        ObjectEvent.MarqueeTextEvent event = new ObjectEvent.MarqueeTextEvent();
        event.message = message;
        event.money = money;
        EventBus.getDefault().post(event);
    }

    private void postEventMarqueeText(String message) {
        initMarquee(message);
    }

    private void getFree() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.USER_ACCOUNT + "?token=" + token, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(s);
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            double free_amount = __data.optDouble("free_amount");
                            double fee_rate = __data.optDouble("fee_rate");
//                            tv_free.setText("您当前可用额度为" + String.valueOf(free_amount) + ",超出额度按" + fee_rate + "收取。");
                            postEventCreditsAmount(free_amount, fee_rate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        ToastUtil.show(mContext, "连接超时");
                    }
                })));
    }

    public void getNotice(final TextView textView, final Context mContext) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(base + Constant.NOTICE + "?token=" + token, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        JSONObject __user = null;
                        try {
                            json = new JSONObject(s);
                            LogUtil.i("通知消息" + json);
                            //{"code":0,"data":[[{"topic":"system-notice","message":"Test1"},{"topic":"system-notice","message":"Test2"},{"topic":"system-notice","message":"Test2"},{"topic":"system-notice","message":"Test3"}]]}
                            int code = json.optInt("code");
                            if (code != 0) {
//                                String error = mContext.getResources().getString(R.string.login_fail);
//                                ToastUtil.show(mContext, error);
                                return;
                            }
                            if (json.optString("data").contains("changeamount-success")) {
                                String message = new JSONObject(json.optString("data").replace("[", "").replace("]", "")).getString("message");
                                getGold(message, textView, mContext);
                            }
                            if (json.optString("data").contains("withdraw-success")) {
                                ToastUtil.getInstance().showToast(mContext, "取款申请成功");//todo 2019.12.31 后台需要在财务管理审核通过才行，以后加取款密码
                                String message = new JSONObject(json.optString("data").replace("[", "").replace("]", "")).getString("message");
                                getGold(message, textView, mContext);
                            }
                            if (json.optString("data").contains("deposit-success")) {
                                ToastUtil.getInstance().showToast(mContext, "存款申请成功");
                                String message = new JSONObject(json.optString("data").replace("[", "").replace("]", "")).getString("message");
                                getGold(message, textView, mContext);
                            }
                            if (json.optString("data").contains("system-notice")) {
                                //[[{"topic":"system-notice","message":"2019猪年快乐"}]]
                                String _data = json.optString("data").replace("[[", "").replace("]]", "");
                                __data = new JSONObject(_data);
                                String message = __data.optString("message");
                                LogUtil.i("通知消息 系统通告=" + message);
//                                ProtocolUtil.getInstance().notification(message);
                                postEventMarqueeText(message);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        ToastUtil.show(mContext, "连接超时");
                    }
                })));
    }

    public void getNoRead(final TextView tv_dot) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(base + Constant.NO_READ + "?token=" + token, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        JSONObject __user = null;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");
                            if (code != 0) {
                                return;
                            }
                            int data = json.optInt("data");
                            if (data > 9) {
                                tv_dot.setVisibility(View.VISIBLE);
                                tv_dot.setText("");
                                tv_dot.setWidth(mContext.getResources().getDimensionPixelOffset(R.dimen.font10));
                                tv_dot.setHeight(mContext.getResources().getDimensionPixelOffset(R.dimen.font10));
                            } else if (data > 0 && data < 10) {
                                tv_dot.setVisibility(View.VISIBLE);
                                tv_dot.setText(String.valueOf(data));
                            } else {
                                tv_dot.setVisibility(View.GONE);
                            }
                            SharedPreUtil.getInstance(mContext).saveParam("unRead", data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");//没有任何数据时 报错
                    }
                })));
    }

    public void getAccount2(final TextView tv_online_count, final TextView tv_profit, final TextView tv_bet_count) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(base + Constant.USER_ACCOUNT + "?token=" + token, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");
                            if (code != 0) {
                                return;
                            }
                            LogUtil.i("getAccount2" + json);
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            String bet_data = __data.optString("bet_data");
                            JSONObject _bet_data = new JSONObject(bet_data);//todo 测试服务未加
                            String bet_amount = _bet_data.optString("bet_amount");
                            String profit = _bet_data.optString("profit");
                            String bet_num = _bet_data.optString("bet_num");

                            tv_online_count.setText(bet_amount);
                            tv_profit.setText(profit);
                            tv_bet_count.setText(bet_num);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        ToastUtil.show(mContext, "连接超时");
                    }
                })));
    }

    private void postEventCreditsAmount(double creditsAmount, double creditsRate) {
        ObjectEvent.CreditsAmountEvent event = new ObjectEvent.CreditsAmountEvent();
        event.creditsAmount = creditsAmount;
        event.creditsRate = creditsRate;
        EventBus.getDefault().post(event);
    }

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
        getGold(null, tv_money, mContext);
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
            ProtocolUtil.getInstance().postLogOutSys();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        timer.cancel();//离开主页暂停获取消息轮询
    }
}
