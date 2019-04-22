package com.purity.yu.gameplatform.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.widget.popup.PopupWindow.CommonPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

@ContentView(R.layout.activity_deposit)
public class DepositActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {
    private static final String TAG = "DepositActivity";

    @BindView(R.id.ll_back)
    public LinearLayout ll_back;
    @BindView(R.id.tv_pay_method)
    public TextView tv_pay_method;
    @BindView(R.id.tv_game_account)
    public TextView tv_game_account;
    @BindView(R.id.et_pay_account)
    public EditText et_pay_account;
    @BindView(R.id.et_deposit)
    public EditText et_deposit;
    @BindView(R.id.et_pay_nickname)
    public EditText et_pay_nickname;
    @BindView(R.id.et_pay_number)
    public EditText et_pay_number;
    @BindView(R.id.tv_pay)
    public TextView tv_pay;
    @BindView(R.id.tv_pay_code)
    public TextView tv_pay_code;
    @BindView(R.id.tv_method)
    public TextView tv_method;
    @BindView(R.id.iv_pay_code)
    public ImageView iv_pay_code;
    @BindView(R.id.ll_deposit)
    public LinearLayout ll_deposit;
    @BindView(R.id.ll_pay_nickname)
    public LinearLayout ll_pay_nickname;
    @BindView(R.id.ll_pay_number)
    public LinearLayout ll_pay_number;

    private Context mContext;
    private CommonPopupWindow popupWindow;
    private int payChannel = 0; //1-微信 2-支付宝 3-银联

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = this;
        EventBus.getDefault().register(this);
        tv_game_account.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
        ProtocolUtil.getInstance().getPayCode(mContext, iv_pay_code);
        initEvent();
        int id = SharedPreUtil.getInstance(mContext).getInt("deposit_state_id");
        if (id != 0) {
            ProtocolUtil.getInstance().getDepositFind(mContext, id, tv_pay);
        }
        tv_pay_code.setTypeface(null);
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.VERSION) == 1) {
            ll_deposit.setBackgroundResource(R.drawable.bg_gameroom);
            ll_pay_nickname.setVisibility(View.GONE);
            ll_pay_number.setVisibility(View.GONE);
        } else {
            ll_deposit.setBackgroundResource(R.drawable.start_bg);
            ll_pay_nickname.setVisibility(View.VISIBLE);
            ll_pay_number.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventMarqueeText(final ObjectEvent.MarqueeTextEvent event) {
        LogUtil.i("审核成功=存款吗" + event.message);
        if (event.money != null) {
            ProtocolUtil.getInstance().depositState(tv_pay, 0, "存款");
        }
    }

    private void initEvent() {
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TabHostActivity.class));
                finish();
            }
        });
        //http://103.231.167.85:8888/admin/index.html 用户上分
        iv_pay_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolUtil.getInstance().saveImage(mContext, iv_pay_code);
            }
        });
        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isFastClick(mContext,3000)) {
                    if (verifyPay()) {
                        ProtocolUtil.getInstance().pay(payChannel, et_pay_number, et_pay_account, et_pay_nickname, et_deposit, tv_pay);
                    }
                } else {
                    ToastUtil.show(mContext, "操作频繁，请稍后再试！");
                }
            }
        });
        tv_pay_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downPopupWindow(v);
            }
        });
    }


    private boolean verifyPay() {
        String gameAccount = tv_game_account.getText().toString();
        String payAccount = et_pay_account.getText().toString();
        String payNumber = et_pay_number.getText().toString();
        String deposit = et_deposit.getText().toString();
        if (payChannel == 0) {
            ToastUtil.showToast(mContext, "请选择支付方式");
            return false;
        }
        if (TextUtils.isEmpty(gameAccount)) {
            ToastUtil.showToast(mContext, "游戏账号不能为空");
            return false;
        }
        if (TextUtils.isEmpty(payAccount)) {
            ToastUtil.showToast(mContext, "账号不能为空");
            return false;
        }
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.VERSION) == 1) {

        } else {
            if (TextUtils.isEmpty(payNumber)) {
                ToastUtil.showToast(mContext, "订单号不能为空");
                return false;
            }
            if (!BaccaratUtil.getInstance().isNumber(payNumber)) {
                ToastUtil.showToast(mContext, "订单号只能为纯数字");
                return false;
            }
            if (et_pay_number.getText().toString().length() != 5) {
                ToastUtil.showToast(mContext, "请输入订单号后5位");
                return false;
            }
        }
        if (TextUtils.isEmpty(deposit)) {
            ToastUtil.showToast(mContext, "存款金额不能为空");
            return false;
        }
        if (!BaccaratUtil.getInstance().isNumber(deposit)) {
            ToastUtil.showToast(mContext, "存款金额只能为纯数字");
            return false;
        }
        return true;
    }


    private void downPopupWindow(View v) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.pop_pay_method)
                .setWidthAndHeight(mContext.getResources().getDimensionPixelOffset(R.dimen.unit88), mContext.getResources().getDimensionPixelOffset(R.dimen.unit90))
                .setAnimationStyle(R.style.AnimDown)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();
        popupWindow.showAsDropDown(v);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.pop_pay_method:
                LinearLayout ll_alipay = (LinearLayout) view.findViewById(R.id.ll_alipay);
                LinearLayout ll_root = (LinearLayout) view.findViewById(R.id.ll_root);
                LinearLayout ll_wechat_pay = (LinearLayout) view.findViewById(R.id.ll_wechat_pay);
                LinearLayout ll_bank_pay = (LinearLayout) view.findViewById(R.id.ll_bank_pay);
                if (SharedPreUtil.getInstance(mContext).getInt(Constant.VERSION) == 1) {
                    ll_wechat_pay.setVisibility(View.GONE);
                    ll_root.setWeightSum(2);
                } else {
                    ll_wechat_pay.setVisibility(View.VISIBLE);
                    ll_root.setWeightSum(3);
                }
                ll_alipay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payChannel = 2;
                        tv_pay_method.setText("支付宝");
                        tv_method.setText("支付宝支付");
                        popupWindow.dismiss();
                        ProtocolUtil.getInstance().getPayCode(mContext, iv_pay_code);
                    }
                });
                ll_wechat_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payChannel = 3;
                        tv_pay_method.setText("微信");
                        tv_method.setText("微信支付");
                        popupWindow.dismiss();
                        ProtocolUtil.getInstance().getPayCode(mContext, iv_pay_code);
                    }
                });
                ll_bank_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payChannel = 1;
                        tv_pay_method.setText("银行卡");
                        popupWindow.dismiss();
                        ProtocolUtil.getInstance().getPayBank(mContext, tv_pay_code);
                    }
                });
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
