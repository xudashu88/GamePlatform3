package com.purity.yu.gameplatform.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

@ContentView(R.layout.activity_withdraw)
public class WithdrawActivity extends BaseActivity {
    private static final String TAG = "WithdrawActivity";
    @BindView(R.id.tv_free)
    public TextView tv_free;
    @BindView(R.id.tv_select_bank)
    public TextView tv_select_bank;
    @BindView(R.id.btn_withdraw)
    public TextView btn_withdraw;
    @BindView(R.id.et_your_name)
    public EditText et_your_name;
    @BindView(R.id.et_bank_number)
    public EditText et_bank_number;
    @BindView(R.id.et_withdraw)
    public EditText et_withdraw;
    @BindView(R.id.et_bank_name)
    public EditText et_bank_name;
    @BindView(R.id.et_phone)
    public EditText et_phone;
    @BindView(R.id.et_bank_branch)
    public EditText et_bank_branch;
    @BindView(R.id.et_pay_pwd)
    public EditText et_pay_pwd;
    @BindView(R.id.ll_back)
    public LinearLayout ll_back;
    private Context mContext;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = this;
        EventBus.getDefault().register(this);
        ProtocolUtil.getInstance().getFree(mContext, tv_free);
        initEvent();
        int id = SharedPreUtil.getInstance(mContext).getInt("withdraw_state_id");
        if (id != 0) {
            ProtocolUtil.getInstance().getWithdrawFind(id, btn_withdraw);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventMarqueeText(final ObjectEvent.MarqueeTextEvent event) {
        LogUtil.i("审核成功=取款吗" + event.message);
        if (event.money != null) {
            ProtocolUtil.getInstance().depositState(btn_withdraw, 0, "取款");
            SharedPreUtil.getInstance(mContext).saveParam("withdraw_state", 0);
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
        tv_select_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在这里使用新的视图
                View view = LayoutInflater.from(mContext).inflate(R.layout.bank_list, null);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
                RelativeLayout rl_data = view.findViewById(R.id.rl_data);
                TextView tv_no_data = view.findViewById(R.id.tv_no_data);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final AlertDialog mAlertDialog = builder.create();
                mAlertDialog.show();
                mAlertDialog.getWindow().setContentView(view);
                ProtocolUtil.getInstance().getBankList(mContext,rl_data, tv_no_data, recyclerView, mAlertDialog, et_your_name, et_bank_number, et_bank_name, et_phone);
            }
        });
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isFastClick(3000)) {
                    if (verifyWithdraw()) {
                        ProtocolUtil.getInstance().withdraw(mContext, et_your_name,
                                et_bank_number, et_bank_name, et_bank_branch, et_pay_pwd, et_phone, et_withdraw,
                                btn_withdraw);
                    }
                } else {
                    ToastUtil.show(mContext, "操作频繁，请稍后再试！");
                }
            }
        });
    }

    private boolean verifyWithdraw() {
        String yourName = et_your_name.getText().toString();
        String bankNumber = et_bank_number.getText().toString();
        String withdraw = et_withdraw.getText().toString();
        String bankName = et_bank_name.getText().toString();
        String phone = et_phone.getText().toString();
//        String auditRemark = et_bank_branch.getText().toString();
        if (TextUtils.isEmpty(yourName)) {
            ToastUtil.showToast(mContext, "姓名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(bankNumber)) {
            ToastUtil.showToast(mContext, "银行卡号不能为空");
            return false;
        }
        if (TextUtils.isEmpty(withdraw)) {
            ToastUtil.showToast(mContext, "提现金额不能为空");
            return false;
        }
        if (TextUtils.isEmpty(bankName)) {
            ToastUtil.showToast(mContext, "银行名称不能为空");
            return false;
        }
        if (isPhone(phone) == true) {
            return true;
        } else if (isPhone(phone) == false) {
            return false;
        }
//        if (TextUtils.isEmpty(auditRemark)) {
//            ToastUtil.showToast(mContext, "银行分行不能为空");
//            return false;
//        }
        if (!BaccaratUtil.getInstance().isNumber(withdraw)) {
            ToastUtil.showToast(mContext, "提现金额只能为纯数字");
            return false;
        }
        return true;
    }

    public boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            ToastUtil.showToast(mContext, "手机号应为11位数");
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {
                ToastUtil.showToast(mContext, "请填入正确的手机号");
            }
            return isMatch;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventCreditsAmount(final ObjectEvent.CreditsAmountEvent event) {
        LogUtil.i("信用额度变化acceptEventCreditsAmount");
        tv_free.setText("您当前可用额度为" + String.valueOf(event.creditsAmount) + ",超出额度按" + event.creditsRate + "收取。");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
