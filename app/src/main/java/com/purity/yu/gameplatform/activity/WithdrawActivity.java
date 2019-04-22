package com.purity.yu.gameplatform.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.purity.yu.gameplatform.adapter.BankAdapter;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Bank;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.http.HttpRequest;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

import static com.gangbeng.basemodule.http.HttpParse.parseArrayObject;

@ContentView(R.layout.activity_withdraw)
public class WithdrawActivity extends BaseActivity {
    private static final String TAG = "WithdrawActivity";
    @BindView(R.id.tv_free)
    public TextView tv_free;
    @BindView(R.id.tv_select_bank)
    public TextView tv_select_bank;
    @BindView(R.id.tv_withdraw)
    public TextView tv_withdraw;
    @BindView(R.id.tv_clean)
    public TextView tv_clean;
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
    @BindView(R.id.ll_withdraw)
    public LinearLayout ll_withdraw;
    @BindView(R.id.ll_bank_branch)
    public LinearLayout ll_bank_branch;
    private Context mContext;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = this;
        EventBus.getDefault().register(this);
        getFree(mContext, tv_free);
        initEvent();
        int id = SharedPreUtil.getInstance(mContext).getInt("withdraw_state_id");
        if (id != 0) {
            getWithdrawFind(id, tv_withdraw);
        }
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.VERSION) == 1) {
            ll_withdraw.setBackgroundResource(R.drawable.bg_gameroom);
            ll_bank_branch.setVisibility(View.GONE);
        } else {
            ll_withdraw.setBackgroundResource(R.drawable.start_bg);
            ll_bank_branch.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventMarqueeText(final ObjectEvent.MarqueeTextEvent event) {
        LogUtil.i("审核成功=取款吗" + event.message);
        if (event.money != null) {
            ProtocolUtil.getInstance().depositState(tv_withdraw, 0, "取款");
            SharedPreUtil.getInstance(mContext).saveParam("withdraw_state", 0);
        }
    }

    private void initEvent() {
        tv_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_your_name.setText("");
                et_bank_number.setText("");
                et_withdraw.setText("");
                et_bank_name.setText("");
                et_phone.setText("");
                et_bank_branch.setText("");
                id = -1;
                et_your_name.setEnabled(true);
                et_bank_number.setEnabled(true);
                et_bank_branch.setEnabled(true);
                et_bank_name.setEnabled(true);
                et_phone.setEnabled(true);
            }
        });
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
                RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                RelativeLayout rl_data = view.findViewById(R.id.rl_data);
                TextView tv_no_data = view.findViewById(R.id.tv_no_data);
                TextView tv_branch_name = view.findViewById(R.id.tv_branch_name);
                View include = view.findViewById(R.id.include);
                if (SharedPreUtil.getInstance(mContext).getInt(Constant.VERSION) == 1) {
                    tv_branch_name.setVisibility(View.GONE);
                    include.setVisibility(View.GONE);
                } else {
                    tv_branch_name.setVisibility(View.VISIBLE);
                    include.setVisibility(View.VISIBLE);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final AlertDialog mAlertDialog = builder.create();
                mAlertDialog.show();
                mAlertDialog.getWindow().setContentView(view);
                getBankList(mContext, rl_data, tv_no_data, recyclerView, mAlertDialog, et_your_name, et_bank_number, et_bank_name, et_phone, et_bank_branch);
            }
        });
        tv_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isFastClick(mContext, 3000)) {
                    if (verifyWithdraw()) {
                        withdraw(mContext, et_your_name,
                                et_bank_number, et_bank_name, et_bank_branch, et_pay_pwd, et_phone, et_withdraw,
                                tv_withdraw);
                    }
                } else {
                    ToastUtil.show(mContext, "操作频繁，请稍后再试！");
                }
            }
        });
    }

    private int id = -1;
    private List<Bank> bankList = new ArrayList<>();
    private BankAdapter bankAdapter;

    public void getBankList(final Context mContext, final RelativeLayout rl_data, final TextView tv_no_data, final RecyclerView recyclerView, final AlertDialog alertDialog,
                            final EditText et_your_name, final EditText et_bank_number, final EditText et_bank_name, final EditText et_phone, final EditText et_bank_branch) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.BANK_LIST + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("银行卡列表=" + json);
                            int code = json.optInt("code");
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.username_existed);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            String _data = json.optString("data");
                            bankList.clear();//清除,每一页都是新的
                            bankList = parseArrayObject(_data, "items", Bank.class);
                            initAdapter(mContext, recyclerView, alertDialog, et_your_name, et_bank_number, et_bank_name, et_phone, et_bank_branch, bankList);
                            if (bankList.size() > 0) {
                                tv_no_data.setVisibility(View.GONE);
                                rl_data.setVisibility(View.VISIBLE);
                            } else {
                                tv_no_data.setVisibility(View.VISIBLE);
                                rl_data.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initAdapter(final Context mContext, RecyclerView recyclerView, final AlertDialog alertDialog, final EditText et_your_name, final EditText et_bank_number, final EditText et_bank_name, final EditText et_phone, final EditText et_bank_branch, final List<Bank> bankList) {
        bankAdapter = new BankAdapter(mContext, bankList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(bankAdapter);
        bankAdapter.setOnItemClickListener(new BankAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                id = bankList.get(position).id;
                et_your_name.setText(bankList.get(position).bank_username);
                et_bank_number.setText(bankList.get(position).bank_account);
                et_bank_branch.setText(bankList.get(position).bank_branch);
                et_bank_name.setText(bankList.get(position).bank_name);
                et_phone.setText(bankList.get(position).mobile);
                et_your_name.setEnabled(false);
                et_bank_number.setEnabled(false);
                et_bank_branch.setEnabled(false);
                et_bank_name.setEnabled(false);
                et_phone.setEnabled(false);
                alertDialog.dismiss();
            }
        });
    }

    public void withdraw(final Context mContext, final EditText et_your_name,
                         final EditText et_bank_number, final EditText et_bank_name, final EditText et_bank_branch, final EditText et_pay_pwd, final EditText et_phone, final EditText et_withdraw,
                         final TextView btn_withdraw) {
        //先新建银行卡再充值
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("bank_username", et_your_name.getText().toString());
        map.put("bank_account", et_bank_number.getText().toString());
        map.put("bank_name", et_bank_name.getText().toString());
        map.put("branch_name", et_bank_branch.getText().toString());
        map.put("password_pay", et_pay_pwd.getText().toString());
        map.put("mobile", et_phone.getText().toString());
        LogUtil.i("提现 添加银行卡=" + map.toString());
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.ADD_CARD + "?token=" + token)
                .addParam("bank_username", et_your_name.getText().toString())
                .addParam("bank_account", et_bank_number.getText().toString())
                .addParam("bank_name", et_bank_name.getText().toString())
                .addParam("branch_name", et_bank_branch.getText().toString())
                .addParam("password_pay", et_pay_pwd.getText().toString())
                .addParam("mobile", et_phone.getText().toString())
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("提现 添加银行卡=" + json + " id=" + id);
                            int code = json.optInt("code");
                            if (code == 0) {
                                String _data = json.optString("data");
                                __data = new JSONObject(_data);

                                int user_bank_id = __data.optInt("id");
                                if (id != -1) {
                                    addCard(id, et_your_name,
                                            et_bank_number, et_bank_name, et_bank_branch, et_pay_pwd, et_phone, et_withdraw,
                                            btn_withdraw);
                                } else
                                    addCard(user_bank_id, et_your_name,
                                            et_bank_number, et_bank_name, et_bank_branch, et_pay_pwd, et_phone, et_withdraw,
                                            btn_withdraw);
                            } else {
                                ToastUtil.showToast(mContext, "提现失败" + result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void addCard(int id, final EditText et_your_name,
                         final EditText et_bank_number, final EditText et_bank_name, final EditText et_bank_branch, final EditText et_pay_pwd, final EditText et_phone, final EditText et_withdraw,
                         final TextView btn_withdraw) {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("apply_amount", et_withdraw.getText().toString());
        map.put("user_bank_id", String.valueOf(id));
        LogUtil.i("提现 开始提现=" + map.toString() + " id=" + id);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.WITHDRAW + "?token=" + token)
                .addParam("apply_amount", et_withdraw.getText().toString())
                .addParam("user_bank_id", String.valueOf(id))
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("提现 开始提现 json=" + json);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            __data = new JSONObject(data);
                            int id = __data.optInt("id");
                            if (code == 0) {
                                ToastUtil.showToast(mContext, "提现" + et_withdraw.getText().toString() + "，请等待管理员审核通过");
                                SharedPreUtil.getInstance(mContext).saveParam("withdraw_state", 1);
                                SharedPreUtil.getInstance(mContext).saveParam("withdraw_state_id", id);
                                ProtocolUtil.getInstance().depositState(btn_withdraw, 1, "取款");
                                et_your_name.setText("");
                                et_bank_number.setText("");
                                et_withdraw.setText("");
                                et_bank_name.setText("");
                                et_phone.setText("");
                            } else {
                                ToastUtil.showToast(mContext, "提现失败" + data);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onResultFault(int code, String hint) {
                        super.onResultFault(code, hint);
                        LogUtil.i("提现 失败 code=" + code + " hint=" + hint);
                    }
                });
    }

    public void getWithdrawFind(final int id, final TextView btn_withdraw) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.WITHDRAW_FIND + "?token=" + token + "&id=" + id)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("取款 审核id=" + id + " " + json);
                            int code = json.optInt("code");
                            if (code != 0) {
                                return;
                            }
                            int state = json.optInt("data");
                            ProtocolUtil.getInstance().depositState(btn_withdraw, state, "取款");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getFree(Context mContext, final TextView tv_free) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.USER_ACCOUNT + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("额度获取=" + json);
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            double free_amount = __data.optDouble("free_amount");
                            double fee_rate = __data.optDouble("fee_rate");
                            tv_free.setText("您当前可用额度为" + String.valueOf(free_amount) + ",超出额度按" + fee_rate + "收取。");
                        } catch (Exception e) {
                            e.printStackTrace();
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
