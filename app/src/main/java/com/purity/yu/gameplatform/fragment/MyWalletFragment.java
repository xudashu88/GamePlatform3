package com.purity.yu.gameplatform.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.BankAdapter;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Bank;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.http.HttpRequest;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.ImgUtils;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.widget.HintDialog;
import com.purity.yu.gameplatform.widget.popup.PopupWindow.CommonPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.request.RequestParams;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;

import static com.gangbeng.basemodule.http.HttpParse.parseArrayObject;

@ContentView(R.layout.fragment_card)
public class MyWalletFragment extends BaseFragment implements CommonPopupWindow.ViewInterface {
    private static final String TAG = "MyWalletFragment";
    @BindView(R.id.iv_pay_code)
    public ImageView iv_pay_code;
    @BindView(R.id.tv_pay_code)
    public TextView tv_pay_code;
    @BindView(R.id.tv_pay_method)
    public TextView tv_pay_method;
    @BindView(R.id.tv_free)
    public TextView tv_free;
    @BindView(R.id.tv_select_bank)
    public TextView tv_select_bank;
    @BindView(R.id.tv_method)
    public TextView tv_method;

    @BindView(R.id.tv_pay)
    public TextView tv_pay;
    @BindView(R.id.tv_game_account)
    public TextView tv_game_account;
    @BindView(R.id.et_pay_nickname)
    public EditText et_pay_nickname;
    @BindView(R.id.et_pay_account)
    public EditText et_pay_account;
    @BindView(R.id.et_pay_number)
    public EditText et_pay_number;
    @BindView(R.id.et_deposit)
    public EditText et_deposit;

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
    @BindView(R.id.et_bank_branch)
    public EditText et_bank_branch;
    @BindView(R.id.et_pay_pwd)
    public EditText et_pay_pwd;

    private Context mContext;
    private CommonPopupWindow popupWindow;
    private int payChannel = 0; //1-微信 2-支付宝 3-银联
    private List<Bank> bankList = new ArrayList<>();
    private BankAdapter bankAdapter;
    private int id = -1;

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        EventBus.getDefault().register(this);
        tv_game_account.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
        getPayCode();
        getPayBank();
        getFree();
        initEvent();
        int id = SharedPreUtil.getInstance(mContext).getInt("deposit_state_id");
        if (id != 0) {
            getDepositFind(id);
        }
        tv_pay_code.setTypeface(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventMarqueeText(final ObjectEvent.MarqueeTextEvent event) {
        LogUtil.i("审核成功=存款吗" + event.message);
        if (event.money != null) {
            depositState(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventVerifyState(final ObjectEvent.VerifyStateEvent event) {
        LogUtil.i("审核取消" + event.state + "存款" + event.type);
        if (event.type == 1 && event.state == 0) {
            depositState(2);
        }
    }

    private void depositState(int state) {
        if (state == 1) {
            tv_pay.setText("审核中");
            tv_pay.setClickable(false);
            tv_pay.setBackgroundColor(mContext.getResources().getColor(R.color.btn));
        } else {
            tv_pay.setText("存款");
            tv_pay.setClickable(true);
            tv_pay.setBackground(mContext.getResources().getDrawable(R.drawable.select_common_btn));
        }
    }

    private void initEvent() {
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
                getBankList(rl_data, tv_no_data, recyclerView, mAlertDialog);
            }
        });
        //http://103.231.167.85:8888/admin/index.html 用户上分
        iv_pay_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });
        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isFastClick(3000)) {
                    if (verifyPay()) {
                        pay();
                    }
                } else {
                    ToastUtil.show(mContext, "操作频繁，请稍后再试！");
                }
            }
        });
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyWithdraw()) {
                    withdraw();
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

    private void initAdapter(RecyclerView recyclerView, final AlertDialog alertDialog) {
        bankAdapter = new BankAdapter(getActivity(), bankList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(bankAdapter);
        bankAdapter.setOnItemClickListener(new BankAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                id = bankList.get(position).id;
                et_your_name.setText(bankList.get(position).bank_username);
                et_bank_number.setText(bankList.get(position).bank_account);
                et_bank_name.setText(bankList.get(position).bank_name);
                et_bank_branch.setText(bankList.get(position).branch_name);
                alertDialog.dismiss();
            }
        });
    }

    private void saveImage() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), iv_pay_code.getId());
        Bitmap bitmap = ((BitmapDrawable) iv_pay_code.getDrawable()).getBitmap();
        boolean isSaveSuccess = ImgUtils.saveImageToGallery(mContext, bitmap);
        if (isSaveSuccess) {
            Toast.makeText(mContext, "已成功保存至" + SharedPreUtil.getInstance(mContext).getString("IMAGE_URL"), Toast.LENGTH_SHORT).show();
            hintDialog("操作流程：\n" +
                    "1.微信点击扫一扫，右上角点击后选择从相册选取二维码。\n" +
                    "2.支付宝点击扫一扫，右上角的相册。");
        } else {
            Toast.makeText(mContext, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBankList(final RelativeLayout rl_data, final TextView tv_no_data, final RecyclerView recyclerView, final AlertDialog alertDialog) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.BANK_LIST + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.username_existed);
                                ToastUtil.show(mContext, error);
                                return;
                            }

                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            bankList.clear();//清除,每一页都是新的
                            bankList = parseArrayObject(_data, "items", Bank.class);
                            initAdapter(recyclerView, alertDialog);
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
//        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.BANK_LIST + "?token=" + token, null),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        JSONObject __data = null;
//                        try {
//                            json = new JSONObject(s);
//                            int code = json.optInt("code");
//                            if (code != 0) {
//                                String error = mContext.getResources().getString(R.string.username_existed);
//                                ToastUtil.show(mContext, error);
//                                return;
//                            }
//
//                            String _data = json.optString("data");
//                            __data = new JSONObject(_data);
//                            bankList.clear();//清除,每一页都是新的
//                            bankList = parseArrayObject(_data, "items", Bank.class);
//                            initAdapter(recyclerView, alertDialog);
//                            if (bankList.size() > 0) {
//                                tv_no_data.setVisibility(View.GONE);
//                                rl_data.setVisibility(View.VISIBLE);
//                            } else {
//                                tv_no_data.setVisibility(View.VISIBLE);
//                                rl_data.setVisibility(View.GONE);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");
//                    }
//                })));
    }

    private void getDepositFind(final int id) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.DEPOSIT_FIND + "?token=" + token + "&id=" + id)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("存款 审核id=" + id + " " + json);
                            int code = json.optInt("code");
                            if (code != 0) {
                                return;
                            }
                            int state = json.optInt("data");
                            depositState(state);
                            if (state == 1) {//1 审核中

                            } else if (state == 2) {//2 审核通过

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.DEPOSIT_FIND + "?token=" + token + "&id=" + id, null),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        JSONObject __data = null;
//                        try {
//                            json = new JSONObject(s);
//                            LogUtil.i("存款 审核id=" + id + " " + json);
//                            int code = json.optInt("code");
//                            if (code != 0) {
//                                return;
//                            }
//                            int state = json.optInt("data");
//                            depositState(state);
//                            if (state == 1) {//1 审核中
//
//                            } else if (state == 2) {//2 审核通过
//
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");
//                    }
//                })));
    }

    private void hintDialog(String hint) {
        final HintDialog myDialog = new HintDialog(mContext);
        myDialog.setContent(hint);
        myDialog.show();
        myDialog.setAttributes();//需先显示，然后才能查找控件
        myDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
    }

    private boolean verifyPay() {
        String gameAccount = tv_game_account.getText().toString();
        String payAccount = et_pay_account.getText().toString();
//        String payNickname = et_pay_nickname.getText().toString();
//        String payNumber = et_pay_number.getText().toString();
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
//        if (TextUtils.isEmpty(payNumber)) {
//            ToastUtil.showToast(mContext, "订单号不能为空");
//            return false;
//        }
//        if (!BaccaratUtil.getInstance().isNumber(payNumber)) {
//            ToastUtil.showToast(mContext, "订单号只能为纯数字");
//            return false;
//        }
//        if (et_pay_number.getText().toString().length() != 5) {
//            ToastUtil.showToast(mContext, "请输入订单号后5位");
//            return false;
//        }
        if (TextUtils.isEmpty(deposit)) {
            ToastUtil.showToast(mContext, "存款金额不能为空");
            return false;
        }
        if (!BaccaratUtil.getInstance().isNumber(deposit)) {
            ToastUtil.showToast(mContext, "存款金额只能为纯数字");
            return false;
        }
//        if (TextUtils.isEmpty(payNickname)) {
//            ToastUtil.showToast(mContext, "昵称不能为空");
//            return false;
//        }
        return true;
    }

    private boolean verifyWithdraw() {
        String yourName = et_your_name.getText().toString();
        String bankNumber = et_bank_number.getText().toString();
        String withdraw = et_withdraw.getText().toString();
        String bankName = et_bank_name.getText().toString();
        String auditRemark = et_bank_branch.getText().toString();
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
        if (TextUtils.isEmpty(auditRemark)) {
            ToastUtil.showToast(mContext, "银行分行不能为空");
            return false;
        }
        if (!BaccaratUtil.getInstance().isNumber(withdraw)) {
            ToastUtil.showToast(mContext, "提现金额只能为纯数字");
            return false;
        }
        return true;
    }

    private void getPayCode() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.PAY_CODE + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);

                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            String icon = __data.optString("icon");
                            iv_pay_code.setVisibility(View.VISIBLE);
//                            tv_pay_code.setVisibility(View.GONE);
                            String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
                            String icon_pre = base.substring(0, base.length() - 4);
                            icon.replace("\\", "");
                            LogUtil.i("二维码" + json + " base=" + base + " icon_pre=" + icon_pre + " icon=" + icon);
//http://103.112.29.145/admin/uploads/images/20190330/QQ%E6%88%AA%E5%9B%BE20190330112557.png
                            Glide.with(mContext).load(icon_pre + icon).into(iv_pay_code);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.PAY_CODE + "?token=" + token, null),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        JSONObject __data = null;
//                        try {
//                            json = new JSONObject(s);
//
//                            String _data = json.optString("data");
//                            __data = new JSONObject(_data);
//                            String icon = __data.optString("icon");
//                            iv_pay_code.setVisibility(View.VISIBLE);
////                            tv_pay_code.setVisibility(View.GONE);
//                            String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
//                            String icon_pre = base.substring(0, base.length() - 4);
//                            icon.replace("\\", "");
//                            LogUtil.i("二维码" + json + " base=" + base + " icon_pre=" + icon_pre + " icon=" + icon);
////http://103.112.29.145/admin/uploads/images/20190330/QQ%E6%88%AA%E5%9B%BE20190330112557.png
//                            Glide.with(mContext).load(icon_pre + icon).into(iv_pay_code);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");
//                    }
//                })));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventCreditsAmount(final ObjectEvent.CreditsAmountEvent event) {
        LogUtil.i("信用额度变化acceptEventCreditsAmount");
        tv_free.setText("您当前可用额度为" + String.valueOf(event.creditsAmount) + ",超出额度按" + event.creditsRate + "收取。");
    }

    private void getFree() {
        LogUtil.i("额度=");
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.USER_ACCOUNT + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.USER_ACCOUNT + "?token=" + token, null),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        JSONObject __data = null;
//                        try {
//                            json = new JSONObject(s);
//                            LogUtil.i("额度=" + json);
//                            String _data = json.optString("data");
//                            __data = new JSONObject(_data);
//                            double free_amount = __data.optDouble("free_amount");
//                            double fee_rate = __data.optDouble("fee_rate");
//                            tv_free.setText("您当前可用额度为" + String.valueOf(free_amount) + ",超出额度按" + fee_rate + "收取。");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");
//                    }
//                })));
    }

    private void getPayBank() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.PAY_BANK + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            String bank_username = __data.optString("bank_username");
                            String bank_account = __data.optString("bank_account");
                            String bank_name = __data.optString("bank_name");
                            String province = __data.optString("province");
                            String city = __data.optString("city");
                            //李四 131313123
                            //四川成都建设银行
//                            iv_pay_code.setVisibility(View.GONE);
                            tv_pay_code.setVisibility(View.VISIBLE);
                            String content = bank_username + "\n" + bank_account + "\n" + province + city + " " + bank_name;
                            tv_pay_code.setText(content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.PAY_BANK + "?token=" + token, null),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        JSONObject __data = null;
//                        try {
//                            json = new JSONObject(s);
//                            String _data = json.optString("data");
//                            __data = new JSONObject(_data);
//                            String bank_username = __data.optString("bank_username");
//                            String bank_account = __data.optString("bank_account");
//                            String bank_name = __data.optString("bank_name");
//                            String province = __data.optString("province");
//                            String city = __data.optString("city");
//                            //李四 131313123
//                            //四川成都建设银行
////                            iv_pay_code.setVisibility(View.GONE);
//                            tv_pay_code.setVisibility(View.VISIBLE);
//                            String content = bank_username + "\n" + bank_account + "\n" + province + city + " " + bank_name;
//                            tv_pay_code.setText(content);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");
//                    }
//                })));
    }

    private void pay() {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("pay_channel", "" + payChannel);
        map.put("pay_info", et_pay_number.getText().toString());
        map.put("pay_username", et_pay_account.getText().toString());
        map.put("pay_nickname", et_pay_nickname.getText().toString());
        map.put("apply_amount", et_deposit.getText().toString());
        LogUtil.i("取款 参数=" + SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.PAY + "?token=" + token + map.toString());
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.PAY + "?token=" + token)
                .addParam("pay_channel", "" + payChannel)
                .addParam("pay_info", et_pay_number.getText().toString())
                .addParam("pay_username", et_pay_account.getText().toString())
                .addParam("pay_nickname", et_pay_nickname.getText().toString())
                .addParam("apply_amount", et_deposit.getText().toString())
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i(TAG, "存款 json=" + json);
                            //{{base_url}}/deposit/find?token={{token}}&&id=3 data=1审核中 data=2审核通过
                            String data = json.optString("data");
                            __data = new JSONObject(data);
                            int id = __data.optInt("id");
                            int code = json.optInt("code");
                            if (code == 0) {
                                ToastUtil.showToast(mContext, "存款" + et_deposit.getText().toString() + "，请等待管理员审核通过");
                                SharedPreUtil.getInstance(mContext).saveParam("deposit_state", 1);
                                SharedPreUtil.getInstance(mContext).saveParam("deposit_state_id", id);
                                LogUtil.i("存款 1 id=" + id);
                                depositState(1);
                                et_deposit.setText("");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.PAY + "?token=" + token, new RequestParams(map)),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        JSONObject __data = null;
//                        try {
//                            json = new JSONObject(s);
//                            LogUtil.i(TAG, "存款 json=" + json);
//                            //{{base_url}}/deposit/find?token={{token}}&&id=3 data=1审核中 data=2审核通过
//                            String data = json.optString("data");
//                            __data = new JSONObject(data);
//                            int id = __data.optInt("id");
//                            int code = json.optInt("code");
//                            if (code == 0) {
//                                ToastUtil.showToast(mContext, "存款" + et_deposit.getText().toString() + "，请等待管理员审核通过");
//                                SharedPreUtil.getInstance(mContext).saveParam("deposit_state", 1);
//                                SharedPreUtil.getInstance(mContext).saveParam("deposit_state_id", id);
//                                depositState(1);
//                                et_deposit.setText("");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");
//                    }
//                })));
    }

    private void withdraw() {
        //先新建银行卡再充值
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("bank_username", et_your_name.getText().toString());
        map.put("bank_account", et_bank_number.getText().toString());
        map.put("bank_name", et_bank_name.getText().toString());
        map.put("branch_name", et_bank_branch.getText().toString());
        map.put("password_pay", et_pay_pwd.getText().toString());
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.ADD_CARD + "?token=" + token)
                .addParam("bank_username", et_your_name.getText().toString())
                .addParam("bank_account", et_bank_number.getText().toString())
                .addParam("bank_name", et_bank_name.getText().toString())
                .addParam("branch_name", et_bank_branch.getText().toString())
                .addParam("password_pay", et_pay_pwd.getText().toString())
                .addParam("password_pay", et_pay_pwd.getText().toString())
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");

                            if (code == 0) {
                                String _data = json.optString("data");
                                __data = new JSONObject(_data);
                                int user_bank_id = __data.optInt("id");
                                if (id != -1) {
                                    addCard(id);
                                } else
                                    addCard(user_bank_id);
                            } else {
                                ToastUtil.showToast(mContext, "提现失败" + result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.ADD_CARD + "?token=" + token, new RequestParams(map)),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        JSONObject __data = null;
//                        try {
//                            json = new JSONObject(s);
//                            int code = json.optInt("code");
//
//                            if (code == 0) {
//                                String _data = json.optString("data");
//                                __data = new JSONObject(_data);
//                                int user_bank_id = __data.optInt("id");
//                                if (id != -1) {
//                                    addCard(id);
//                                } else
//                                    addCard(user_bank_id);
//                            } else {
//                                ToastUtil.showToast(mContext, "提现失败" + s);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");
//                    }
//                })));
    }

    private void addCard(int id) {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("apply_amount", et_withdraw.getText().toString());
        map.put("user_bank_id", String.valueOf(id));
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.WITHDRAW + "?token=" + token)
                .addParam("apply_amount", et_withdraw.getText().toString())
                .addParam("user_bank_id", String.valueOf(id))
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");

                            if (code == 0) {
                                ToastUtil.showToast(mContext, "提现" + et_withdraw.getText().toString() + "，请等待管理员审核通过");
                                et_withdraw.setText("");
                            } else {
                                ToastUtil.showToast(mContext, "提现失败" + result);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.WITHDRAW + "?token=" + token, new RequestParams(map)),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        JSONObject __data = null;
//                        try {
//                            json = new JSONObject(s);
//                            int code = json.optInt("code");
//
//                            if (code == 0) {
//                                ToastUtil.showToast(mContext, "提现" + et_withdraw.getText().toString() + "，请等待管理员审核通过");
//                                et_withdraw.setText("");
//                            } else {
//                                ToastUtil.showToast(mContext, "提现失败" + s);
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");
//                    }
//                })));
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
                LinearLayout ll_wechat_pay = (LinearLayout) view.findViewById(R.id.ll_wechat_pay);
                LinearLayout ll_bank_pay = (LinearLayout) view.findViewById(R.id.ll_bank_pay);
                ll_alipay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payChannel = 2;
                        tv_pay_method.setText("支付宝");
                        tv_method.setText("支付宝支付");
                        popupWindow.dismiss();
                        getPayCode();
                    }
                });
                ll_wechat_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payChannel = 3;
                        tv_pay_method.setText("微信");
                        tv_method.setText("微信支付");
                        popupWindow.dismiss();
                        getPayCode();
                    }
                });
                ll_bank_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        payChannel = 1;
                        tv_pay_method.setText("银行卡");
                        popupWindow.dismiss();
                        getPayBank();
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
