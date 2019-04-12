package com.purity.yu.gameplatform.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.google.gson.Gson;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.StartActivity;
import com.purity.yu.gameplatform.activity.TabHostActivity;
import com.purity.yu.gameplatform.adapter.BankAdapter;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.Bank;
import com.purity.yu.gameplatform.entity.Game2;
import com.purity.yu.gameplatform.entity.GamePlay;
import com.purity.yu.gameplatform.entity.GameRoom;
import com.purity.yu.gameplatform.entity.Games;
import com.purity.yu.gameplatform.entity.Notice;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.http.HttpRequest;
import com.purity.yu.gameplatform.login.LoginActivity;
import com.purity.yu.gameplatform.login.RegisterActivity;
import com.purity.yu.gameplatform.widget.HintDialog;
import com.purity.yu.gameplatform.widget.MarqueeTextView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import avi.AVLoadingIndicatorView;
import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;

import static com.gangbeng.basemodule.http.HttpParse.parseArrayObject;

public class ProtocolUtil {
    private Context mContext;
    private static ProtocolUtil instance = null;
    private NotificationManager mNotificationManager;
    List<GameRoom> gameRoomList = new ArrayList<>();
    List<Game2> gameList = new ArrayList<>();

    public void init(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static ProtocolUtil getInstance() {
        if (null == instance) {
            instance = new ProtocolUtil();
        }
        return instance;
    }

    //----------------------------------存款--------------------------------------
    public void getPayCode(final Context mContext, final ImageView iv_pay_code) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.PAY_CODE + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);

                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            String icon = __data.optString("icon");
                            iv_pay_code.setVisibility(View.VISIBLE);
                            String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
                            String icon_pre = base.substring(0, base.length() - 4);
                            icon.replace("\\", "");
                            LogUtil.i("二维码" + json + " base=" + base + " icon_pre=" + icon_pre + " icon=" + icon);
                            //  http://103.112.29.145/admin/uploads/images/20190330/QQ%E6%88%AA%E5%9B%BE20190330112557.png
                            Glide.with(mContext).load(icon_pre + icon).into(iv_pay_code);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getPayBank(Context mContext, final TextView tv_pay_code) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.PAY_BANK + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            String bank_username = __data.optString("bank_username");
                            String bank_account = __data.optString("bank_account");
                            String bank_name = __data.optString("bank_name");
                            String province = __data.optString("province");
                            String city = __data.optString("city");
                            tv_pay_code.setVisibility(View.VISIBLE);
                            String content = bank_username + "\n" + bank_account + "\n" + province + city + " " + bank_name;
                            tv_pay_code.setText(content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getDepositFind(Context mContext, final int id, final TextView tv_pay) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.DEPOSIT_FIND + "?token=" + token + "&id=" + id)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("存款 审核id=" + id + " " + json);
                            int code = json.optInt("code");
                            if (code != 0) {
                                return;
                            }
                            int state = json.optInt("data");
                            depositState(tv_pay, state, "存款");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void pay(int payChannel, EditText et_pay_number, EditText et_pay_account, EditText et_pay_nickname, final EditText et_deposit, final TextView tv_pay) {
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
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            //{{base_url}}/deposit/find?token={{token}}&&id=3 data=1审核中 data=2审核通过 data=0审核取消
                            String data = json.optString("data");
                            __data = new JSONObject(data);
                            int id = __data.optInt("id");
                            int code = json.optInt("code");
                            if (code == 0) {
                                ToastUtil.showToast(mContext, "存款" + et_deposit.getText().toString() + "，请等待管理员审核通过");
                                SharedPreUtil.getInstance(mContext).saveParam("deposit_state", 1);
                                SharedPreUtil.getInstance(mContext).saveParam("deposit_state_id", id);
                                LogUtil.i("存款 1 id=" + id);
                                ProtocolUtil.getInstance().depositState(tv_pay, 1, "存款");
                                et_deposit.setText("");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void saveImage(Context mContext, ImageView iv_pay_code) {
        Bitmap bitmap = ((BitmapDrawable) iv_pay_code.getDrawable()).getBitmap();
        boolean isSaveSuccess = ImgUtils.saveImageToGallery(mContext, bitmap);
        if (isSaveSuccess) {
            Toast.makeText(mContext, "已成功保存至" + SharedPreUtil.getInstance(mContext).getString("IMAGE_URL"), Toast.LENGTH_SHORT).show();
            hintDialog(mContext, "操作流程：\n" +
                    "1.微信点击扫一扫，右上角点击后选择从相册选取二维码。\n" +
                    "2.支付宝点击扫一扫，右上角的相册。");
        } else {
            Toast.makeText(mContext, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    public void depositState(TextView tv_pay, int state, String btn) {
        if (state == 1) {
            tv_pay.setText("审核中");
            tv_pay.setClickable(false);
            tv_pay.setBackgroundColor(mContext.getResources().getColor(R.color.btn));
        } else {
            tv_pay.setText(btn);
            tv_pay.setClickable(true);
            tv_pay.setBackground(mContext.getResources().getDrawable(R.drawable.select_common_btn));
        }
    }

    //----------------------------------取款--------------------------------------
    private int id = -1;
    private List<Bank> bankList = new ArrayList<>();
    private BankAdapter bankAdapter;

    public void getBankList(final Context mContext, final RelativeLayout rl_data, final TextView tv_no_data, final RecyclerView recyclerView, final AlertDialog alertDialog,
                            final EditText et_your_name, final EditText et_bank_number, final EditText et_bank_name, final EditText et_phone) {
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
                            initAdapter(mContext, recyclerView, alertDialog, et_your_name, et_bank_number, et_bank_name, et_phone, bankList);
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

    private void initAdapter(final Context mContext, RecyclerView recyclerView, final AlertDialog alertDialog, final EditText et_your_name, final EditText et_bank_number, final EditText et_bank_name, final EditText et_phone, final List<Bank> bankList) {
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
                et_bank_name.setText(bankList.get(position).bank_name);
                et_phone.setText(bankList.get(position).mobile);
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
                .addParam("branch_name", "")
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
        LogUtil.i("提现 开始提现=" + map.toString());
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
                                depositState(btn_withdraw, 1, "取款");
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
                            depositState(btn_withdraw, state, "取款");
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

    //----------------------------------大厅--------------------------------------
    private void postEventMarqueeText(String message, String money) {
        ObjectEvent.MarqueeTextEvent event = new ObjectEvent.MarqueeTextEvent();
        event.message = message;
        event.money = money;
        EventBus.getDefault().post(event);
    }

    private void postEventMarqueeText(String message, MarqueeTextView marquee) {
        initMarquee2(message, marquee);
    }

    private void postEventCreditsAmount(double creditsAmount, double creditsRate) {
        ObjectEvent.CreditsAmountEvent event = new ObjectEvent.CreditsAmountEvent();
        event.creditsAmount = creditsAmount;
        event.creditsRate = creditsRate;
        EventBus.getDefault().post(event);
    }

    private void initMarquee2(String message, MarqueeTextView marquee) {
//        DrawableCompat.setTint(DrawableCompat.wrap(yellowSpeaker.getDrawable().mutate()), getResources().getColor(R.color.yellow));
//        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.ADDS))) {
//            marqueeList = Arrays.asList(SharedPreUtil.getInstance(mContext).getString(Constant.ADDS), "");
//        }
        marquee.setText(message);
        if (message.length() > 70) {
            marquee.setRndDuration(15500);
        } else if (message.length() > 60) {
            marquee.setRndDuration(14000);
        } else if (message.length() > 50) {
            marquee.setRndDuration(12500);
        } else if (message.length() > 40) {
            marquee.setRndDuration(11000);
        } else if (message.length() > 30) {
            marquee.setRndDuration(9500);
        } else if (message.length() > 20) {
            marquee.setRndDuration(8000);
        } else if (message.length() > 10) {
            marquee.setRndDuration(6500);
        } else {
            marquee.setRndDuration(2600);
        }
        LogUtil.i("系统通告2 滚动时间=" + marquee.getRndDuration() + " 滚动字符长度=" + message.length());
        marquee.startScroll();
    }

    public void getGold(final String message, final TextView tv_money, final Context mContext) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        HttpRequest.request(base + Constant.USER_INFO + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        JSONObject __user;
                        try {
                            json = new JSONObject(result);
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
                });
    }

    private void getFree() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.USER_ACCOUNT + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
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
                });
    }

    List<Notice> noticeList = new ArrayList<>();

    public void getSysNoticeList(final Context mContext, final MarqueeTextView marquee) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.SYS_NOTICE_LIST + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            if (code != 0) {
                                return;
                            }
                            String _data = json.optString("data");
                            LogUtil.i("系统通告=" + json);
                            noticeList.clear();//清除,每一页都是新的
                            noticeList = parseArrayObject(_data, "items", Notice.class);

                            final String message = noticeList.get(noticeList.size() - 1).content;
                            ((TabHostActivity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initMarquee2(message, marquee);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private int receiverSysNotice = 0;

    public void getNotice(final Context mContext, final TextView textView, final MarqueeTextView marquee) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        HttpRequest.request(base + Constant.NOTICE + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            if (code != 0) {
                                return;
                            }
                            String data = json.optString("data");
                            LogUtil.i("通知消息" + json);
                            if (data.length() == 2) {
                                receiverSysNotice = 0;
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
                                receiverSysNotice++;
                                if (receiverSysNotice == 1) {
                                    postEventMarqueeText(message, marquee);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getNoRead(final Context mContext, final TextView tv_dot) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        HttpRequest.request(base + Constant.NO_READ + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
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
                });
    }

    public void getAccount2(final TextView tv_online_count, final TextView tv_profit, final TextView tv_bet_count) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        HttpRequest.request(base + Constant.USER_ACCOUNT + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
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
                });
    }

    public void postLoginOut(final Context mContext, final Timer timer) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
//            System.exit(0);
            cleanSharedPre(mContext);
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(intent);
            ((TabHostActivity) mContext).finish();
            return;
        }
        ProtocolUtil.getInstance().postLogOut(mContext, timer);
    }

    public void postLogOut(final Context mContext, final Timer timer) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.LOGIN_OUT + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        try {
                            JSONObject json = null;
                            JSONObject __data = null;
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code != 0) {
                                ToastUtil.show(mContext, data);
                                return;
                            } else {
                                if (timer != null) {
                                    timer.cancel();//离开主页暂停获取消息轮询
                                }
                                cleanSharedPre(mContext);
                                ((TabHostActivity) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String ok = mContext.getResources().getString(R.string.exit_successfully);
                                        ToastUtil.show(mContext, ok);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);
                                        ((TabHostActivity) mContext).finish();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 登陆界面的退出 直接关闭应用
     * android:excludeFromRecents="true"
     * android:launchMode="singleInstance"
     * 需求:监听APP强制关闭，在Service中的onTaskRemove这个方法在安卓8.0上不调用，就不能请求退出操作
     * 目的:不会在最近任务列表中显示 2019.3.27
     */
    public void postLogOutSys(final Context mContext) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            cleanSharedPre(mContext);
            System.exit(0);
            return;
        } else {
            HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.LOGIN_OUT + "?token=" + token)
                    .executeGetParams(new HttpRequest.HttpCallBack() {
                        @Override
                        public void onResultOk(String result) {
                            try {
                                JSONObject json;
                                json = new JSONObject(result);
                                int code = json.optInt("code");
                                String data = json.optString("data");
                                if (code != 0) {
                                    ToastUtil.show(mContext, data);
                                    return;
                                } else {
                                    cleanSharedPre(mContext);
                                    System.exit(0);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    public void cleanSharedPre(Context mContext) {
        SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN, "");
        SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN_SOCKET, "");
        SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_ID, "");

        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_ROOM_COUNT, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.DT_ROOM_COUNT, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.SINGLE_ROOM_COUNT, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_BAC_LIST, "");
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_DT_LIST, "");
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_SINGLE_LIST, "");

        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE, "");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant._BASE, "");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_LOBBY, "");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_ROOM, "");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_PRODICT, "");
    }

    public void postHJRecycle(final TextView tv_money) {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("game_type", "");
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.HJ_RECYCLE + "?token=" + token + "&amount=0")
                .addParam("game_type", "")
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("回收" + json);
                            int code = json.optInt("code");
                            double data = json.optDouble("data");
                            ;
                            if (code == 0) {
                                if (data > 0) {
                                    ToastUtil.showToast(mContext, "回收" + data + "成功");
                                    tv_money.setText(String.valueOf(data));
                                    getAccount();//回收后，信用额度也跟着变
                                } else {
                                    ToastUtil.showToast(mContext, "已回收");
                                }
                            } else {
                                ToastUtil.showToast(mContext, "提现失败" + data);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getAccount() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.USER_ACCOUNT + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            String _data = json.optString("data");
                            LogUtil.i("getAccount=" + _data);
                            __data = new JSONObject(_data);
                            double free_amount = __data.optDouble("free_amount");
                            double fee_rate = __data.optDouble("fee_rate");
//                            tv_free.setText("您当前可用额度为" + String.valueOf(free_amount) + ",超出额度按" + fee_rate + "收取。");
                            postEventCreditsAmount(free_amount, fee_rate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //----------------------------------银行卡列表--------------------------------------
    public void postRemove(final Context mContext, int id, final android.support.v7.app.AlertDialog dialog) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.BANK_REMOVE + "?token=" + token)
                .addParam("id", String.valueOf(id))
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code != 0) {
                                ToastUtil.show(mContext, data);
                                return;
                            }
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //----------------------------------消息列表--------------------------------------
    public void postRemove(final Context mContext, int id, final android.support.v7.app.AlertDialog dialog, final int position) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.MESSAGE_REMOVE + "?token=" + token)
                .addParam("id", String.valueOf(id))
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code != 0) {
                                ToastUtil.show(mContext, data);
                                return;
                            }
                            dialog.dismiss();
                            ObjectEvent.notifyItemChangedEvent event = new ObjectEvent.notifyItemChangedEvent();
                            event.position = position;
                            EventBus.getDefault().post(event);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void postLook(final Context mContext, int id, final ImageView imageView) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.MESSAGE_DETAIL + "?token=" + token + "&id=" + id)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code != 0) {
                                ToastUtil.show(mContext, data);
                                return;
                            }
                            String _data = json.optString("data");
                            imageView.setVisibility(View.GONE);
                            imageView.setBackgroundResource(0);
                            int unRead = SharedPreUtil.getInstance(mContext).getInt("unRead");
                            SharedPreUtil.getInstance(mContext).saveParam("unRead", --unRead);
                            ToastUtil.show(mContext, "朕已阅");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //----------------------------------登录--------------------------------------
    public void postLoginIn(final Context mContext, final String username, final String pwd, final EditText et_ip, final AVLoadingIndicatorView avi, final String nickname) {
        String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(et_ip) + ServiceIpConstant.BASE_AFTER;
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", pwd);
        map.put("deviceid", ProtocolUtil.getInstance().getDeviceId());
        HttpRequest.request(baseUrl + Constant.LOGIN)
                .addParam("username", username)
                .addParam("password", pwd)
                .addParam("deviceid", ProtocolUtil.getInstance().getDeviceId())
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        try {
                            JSONObject json;
                            json = new JSONObject(result);
                            LogUtil.i("登录1json" + json);
                            int code = json.optInt("code");
                            String token = json.optString("data");
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                if (token.equals("该账号已经登录")) {
                                    ToastUtil.show(mContext, token);
                                    avi.hide();
                                    return;
                                } else {
                                    ToastUtil.show(mContext, token);
                                    avi.hide();
                                    return;
                                }
                            }
                            avi.hide();
                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.login_succeeded));
                            save(mContext, pwd, token, username, nickname, et_ip.getText().toString());
                        } catch (Exception e) {
                            avi.hide();
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void postRegister(final Context mContext, final String user, final String pwd, final EditText et_ip, final String nickname, final AVLoadingIndicatorView avi) {
        final String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(et_ip) + ServiceIpConstant.BASE_AFTER;
        HashMap<String, String> map = new HashMap<>();
        map.put("username", user);
        map.put("password", pwd);
        map.put("promo_code", "12346");//邀请码默认写死
        map.put("deviceid", ProtocolUtil.getInstance().getDeviceId());
        map.put("nickname", nickname);
        LogUtil.i("微信注册=" + map.toString());
        HttpRequest.request(baseUrl + Constant.REGISTER)
                .addParam("username", user)
                .addParam("password", pwd)
                .addParam("promo_code", "12346")
                .addParam("deviceid", ProtocolUtil.getInstance().getDeviceId())
                .addParam("nickname", nickname)
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        try {
                            JSONObject json;
                            json = new JSONObject(result);
                            LogUtil.i("注册1json" + json);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code == 400) {
                                LogUtil.i("微信已注册=" + nickname);
                                postLoginIn(mContext, user, pwd, et_ip, avi, nickname);
                                return;
                            }
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            avi.hide();
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_PWD, pwd);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_NAME, nickname);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN, data);
                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.register_succeeded));
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE_URL, et_ip.getText().toString());
                            String _ip = BaccaratUtil.getInstance().changeIp(et_ip.getText().toString());
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE, ServiceIpConstant.BASE_BEFORE + _ip + "/api");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant._BASE, ServiceIpConstant.BASE_BEFORE + _ip + ":8888/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_LOBBY, ServiceIpConstant.BASE_BEFORE + _ip + ":9081/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_ROOM, ServiceIpConstant.BASE_BEFORE + _ip + ":9082/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_PRODICT, ServiceIpConstant.BASE_BEFORE + _ip + ":9080/");
                            avi.hide();
                            mContext.startActivity(new Intent(mContext, StartActivity.class));
                            ((Activity) mContext).finish();
                            postLoginInfo(mContext);
                        } catch (Exception e) {
                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.ip_invalid));
                            avi.hide();
                            e.printStackTrace();
                        }
                    }
                });
    }

    //----------------------------------登录注册提前获取登录信息--------------------------------------
    public void save(Context mContext, String pwd, String token, String username, String nickname, String et_ip) {
        SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_PWD, pwd);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN, token);
        LogUtil.i("微信方式" + SharedPreUtil.getInstance(mContext).getInt(Constant.LOGIN_TYPE));
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.LOGIN_TYPE) == 0) {
            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_NAME, username);
        } else
            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_NAME, nickname);
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE_URL, et_ip);
        String _ip = BaccaratUtil.getInstance().changeIp(et_ip);
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE, ServiceIpConstant.BASE_BEFORE + _ip + "/api");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant._BASE, ServiceIpConstant.BASE_BEFORE + _ip + ":8888/");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_LOBBY, ServiceIpConstant.BASE_BEFORE + _ip + ":9081/");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_ROOM, ServiceIpConstant.BASE_BEFORE + _ip + ":9082/");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_PRODICT, ServiceIpConstant.BASE_BEFORE + _ip + ":9080/");
        mContext.startActivity(new Intent(mContext, StartActivity.class));
        ((Activity) mContext).finish();
        LogUtil.i("登录获取房间列表信息 1");
        ProtocolUtil.getInstance().postLoginInfo(mContext);
    }

    public void postLoginInfo(final Context mContext) {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        //上分，-1不上分，0上所有的分，>0 上具体分数
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        LogUtil.i("获取登陆信息=" + base + Constant.LOGIN_INFO + "?token=" + token + "&game_type=jxb&amount=-1");
        HttpRequest.request(base + Constant.LOGIN_INFO + "?token=" + token + "&game_type=jxb&amount=-1")
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("登录2json" + json);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            __data = new JSONObject(data);

                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail_jxb);
                                ToastUtil.show(mContext, data);
                                return;
                            }
                            String token = __data.optString("token");
                            String userId = __data.optString("userid");
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN_SOCKET, token);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_ID, userId);
                            getGameRoomCount(mContext, token, userId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getGameRoomCount(final Context mContext, final String token, final String userId) {
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant._BASE);
        gameRoomList.clear();//添加之前清空数据
        HttpRequest.request(base + Constant.LOGIN_VALIDATE + "?auth=" + token + "&userid=" + userId)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("登录获取房间列表信息 3" + json);
                            int code = json.optInt("code");
                            if (code == -1) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            List<Games> gamesList = new ArrayList<>();
                            gamesList.clear();
                            JSONArray data_array = __data.getJSONArray("games");
                            for (int i = 0; i < data_array.length(); i++) {
                                Games games = new Games();
                                JSONObject _json = data_array.getJSONObject(i);
                                String gameName = _json.optString("gameName");
                                games.gameName = gameName;
                                JSONArray _gamePlays = _json.getJSONArray("gamePlays");
                                for (int j = 0; j < _gamePlays.length(); j++) {
                                    GamePlay gamePlay = new GamePlay();
                                    JSONObject _gamePlays_json = _gamePlays.getJSONObject(j);
                                    int minCoin = _gamePlays_json.optInt("minCoin");
                                    gamePlay.minCoin = minCoin;
                                    games.gamePlays.add(gamePlay);
                                    JSONArray _gameRooms = _gamePlays_json.getJSONArray("gameRooms");

                                    for (int k = 0; k < _gameRooms.length(); k++) {
                                        GameRoom gameRoom = new GameRoom();
                                        JSONObject _gameRooms_json = _gameRooms.getJSONObject(k);
                                        String _id = _gameRooms_json.optString("id");
                                        String name = _gameRooms_json.optString("name");
                                        int status = _gameRooms_json.optInt("status");
                                        int betSecond = _gameRooms_json.optInt("betSecond");
                                        String _code = _gameRooms_json.optString("code");
                                        String minBet = _gameRooms_json.optString("minBet");
                                        String maxBet = _gameRooms_json.optString("maxBet");
                                        String betLimit = _gameRooms_json.optString("betLimit");
                                        String roomConfig = _gameRooms_json.optString("roomConfig");
                                        JSONObject _roomConfig = new JSONObject(roomConfig);
                                        String lan_rtmpAddress = _roomConfig.optString("lan_rtmpAddress");
                                        String[] lan_rtmpAddressArr = new Gson().fromJson(lan_rtmpAddress, String[].class);
                                        String dx_rtmpAddress = _roomConfig.optString("dx_rtmpAddress");
                                        String[] dx_rtmpAddressArr = new Gson().fromJson(dx_rtmpAddress, String[].class);
                                        String yd_rtmpAddress = _roomConfig.optString("yd_rtmpAddress");
                                        String[] yd_rtmpAddressArr = new Gson().fromJson(yd_rtmpAddress, String[].class);
                                        String color = _roomConfig.optString("color");
                                        String[] colorArr = new Gson().fromJson(color, String[].class);
                                        gameRoom.rtmp = lan_rtmpAddressArr;
                                        gameRoom.dxRtmp = dx_rtmpAddressArr;
                                        gameRoom.ydRtmp = yd_rtmpAddressArr;
                                        gameRoom.color = colorArr;
                                        int[] minBetInt = new Gson().fromJson(minBet, int[].class);
                                        int[] maxBetInt = new Gson().fromJson(maxBet, int[].class);
                                        List<Integer> minBetList = new ArrayList<>();
                                        List<Integer> maxBetList = new ArrayList<>();
                                        for (int a = 0; a < minBetInt.length; a++) {
                                            minBetList.add(minBetInt[a]);
                                        }
                                        for (int a = 0; a < maxBetInt.length; a++) {
                                            maxBetList.add(maxBetInt[a]);
                                        }
                                        gameRoom.minBet.clear();
                                        gameRoom.maxBet.clear();
                                        gameRoom.minBet.addAll(minBetList);
                                        gameRoom.maxBet.addAll(maxBetList);
                                        gameRoom.id = _id;
                                        gameRoom.name = name;
                                        gameRoom.status = status;
                                        gameRoom.code = _code;
                                        gameRoom.betLimit = betLimit;
                                        gameRoom.betSecond = betSecond;
                                        gameRoomList.add(gameRoom);
                                    }
                                }
                                gamesList.add(games);
                            }
                            getGameRoomCount(mContext);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getGameRoomCount(final Context mContext) {
        int bacCount = 0, dtCount = 0, singleCount = 0, catchFish = 0;
        List<Baccarat> baccaratList = new ArrayList<>();
        List<Baccarat> dtList = new ArrayList<>();
        List<Baccarat> singleList = new ArrayList<>();
        if (gameRoomList.size() > 0) {
            for (int i = 0; i < gameRoomList.size(); i++) {
                if (gameRoomList.get(i).code.equals("baccarat")) {
                    bacCount++;
                    Baccarat baccarat = new Baccarat();
                    baccarat.roomId = gameRoomList.get(i).id;
                    baccarat.roomName = gameRoomList.get(i).name;
                    baccarat.rtmp = gameRoomList.get(i).rtmp;
                    baccarat.ydRtmp = gameRoomList.get(i).ydRtmp;
                    baccarat.dxRtmp = gameRoomList.get(i).dxRtmp;
                    baccarat.color = gameRoomList.get(i).color;
                    baccarat.betSecond = gameRoomList.get(i).betSecond;
                    if (gameRoomList.get(i).minBet != null) {
                        baccarat.minBetList.addAll(gameRoomList.get(i).minBet);
                        baccarat.maxBetList.addAll(gameRoomList.get(i).maxBet);
                    }
                    baccaratList.add(baccarat);
                } else if (gameRoomList.get(i).code.equals("dragonTiger")) {
                    dtCount++;
                    Baccarat baccarat = new Baccarat();
                    baccarat.roomId = gameRoomList.get(i).id;
                    baccarat.roomName = gameRoomList.get(i).name;
                    baccarat.rtmp = gameRoomList.get(i).rtmp;
                    baccarat.ydRtmp = gameRoomList.get(i).ydRtmp;
                    baccarat.dxRtmp = gameRoomList.get(i).dxRtmp;
                    baccarat.color = gameRoomList.get(i).color;
                    baccarat.betSecond = gameRoomList.get(i).betSecond;
                    if (gameRoomList.get(i).minBet != null) {
                        baccarat.minBetList.addAll(gameRoomList.get(i).minBet);
                        baccarat.maxBetList.addAll(gameRoomList.get(i).maxBet);
                    }
                    dtList.add(baccarat);
                } else if (gameRoomList.get(i).code.equals("duel")) {
                    singleCount++;
                    Baccarat baccarat = new Baccarat();
                    baccarat.roomId = gameRoomList.get(i).id;
                    baccarat.roomName = gameRoomList.get(i).name;
                    baccarat.rtmp = gameRoomList.get(i).rtmp;
                    baccarat.ydRtmp = gameRoomList.get(i).ydRtmp;
                    baccarat.dxRtmp = gameRoomList.get(i).dxRtmp;
                    baccarat.betSecond = gameRoomList.get(i).betSecond;
                    if (gameRoomList.get(i).minBet != null) {
                        baccarat.minBetList.addAll(gameRoomList.get(i).minBet);
                        baccarat.maxBetList.addAll(gameRoomList.get(i).maxBet);
                    }
                    singleList.add(baccarat);
                } else {
                    catchFish++;
                }
            }
        }
        LogUtil.i("登录获取房间列表信息 4 bacCount=" + bacCount + " dtCount=" + dtCount + " singleCount=" + singleCount + " catchFish=" + catchFish);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_ROOM_COUNT, bacCount);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.DT_ROOM_COUNT, dtCount);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.SINGLE_ROOM_COUNT, singleCount);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.FISH_ROOM_COUNT, catchFish);
//        getRoomCount();//獲取打魚的房間數
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_BAC_LIST, new Gson().toJson(baccaratList));
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_DT_LIST, new Gson().toJson(dtList));
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_SINGLE_LIST, new Gson().toJson(singleList));
    }

    //----------------------------------登录注册提前获取登录信息--------------------------------------

    //----------------------------------获取游戏内的分数--------------------------------------
    public void postLoginInfo(final Context mContext, final TextView tv_money) {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(base + Constant.LOGIN_INFO + "?token=" + token + "&game_type=jxb&amount=0", null),//perfect todo 在后面加了&amount=0能否解决分数翻倍的问题
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail_jxb);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            String data = json.optString("data");
                            __data = new JSONObject(data);
                            String token = __data.optString("token");
                            String userId = __data.optString("userid");
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN_SOCKET, token);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_ID, userId);
                            //http://103.112.29.145:8888/admin/backend/points?score=1000000&&type=0&&userid=536273706603773952 备份上分接口
                            getGold(mContext, token, userId, tv_money);
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

    public void getGold(final Context mContext, final String token, final String userId, final TextView tv_money) {
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant._BASE) + Constant.LOGIN_VALIDATE + "?auth=" + token + "&userid=" + userId)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        JSONObject __user;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            if (code == -1) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            String _user = __data.optString("user");
                            __user = new JSONObject(_user);
                            String goldcoins = __user.optString("availableAmount");
                            tv_money.setText(goldcoins);
                            LogUtil.i("getGold=" + json + " goldcoins=" + goldcoins);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public String getDeviceId() {
        String deviceId = null;
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            deviceId = tm.getDeviceId();//354765081029985 NJ6R18305006145
            String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);//8bc5b2e6141fb866 fd52d7703ddb864a
            LogUtil.i("设备信息 手机卡 imei=" + deviceId + " 序列号=" + android_id);
        }
        return deviceId;
    }

    public void getAppInfo(EditText et_ip) {
        String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(et_ip) + ServiceIpConstant.BASE_AFTER;
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE_URL, et_ip.getText().toString());
        HttpRequest.request(baseUrl + Constant.APP_INFO)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("是否注册=" + json);
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            int open_register = __data.optInt("open_register");
                            if (open_register == 1) {//1开放注册 其他关闭注册
                                mContext.startActivity(new Intent(mContext, RegisterActivity.class));
                                ((Activity) mContext).finish();
                            } else {
                                ToastUtil.show(mContext, "需要注册，请联系管理员");
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getGameList() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.GAME_LIST + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(result);
                            String _data = json.optString("data");
                            gameList = parseArrayObject(_data, "items", Game2.class);
                            for (int i = 0; i < gameList.size(); i++) {
                                String verify = gameList.get(i).platformCode + gameList.get(i).gameNameEn;
                                if (verify.equals("JXB" + "baccarat")) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.BAC_ROOM_COUNT);
                                } else if (verify.equals("JXB" + "dragonTiger")/*gameList.get(i).gameName.equals("龙虎")*/) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.DT_ROOM_COUNT);
                                } else if (verify.equals("JXB" + "dragonTiger2")/*gameList.get(i).gameName.equals("龙虎")*/) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.DT_ROOM_COUNT);
                                } else if (verify.equals("JXB" + "duel")/*gameList.get(i).gameName.equals("单挑")*/) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.SINGLE_ROOM_COUNT);
                                } else if (verify.equals("JXB" + "macau")/*gameList.get(i).gameName.equals("澳门五路")*/) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.BAC_ROOM_COUNT);
                                } else if (verify.equals("JXB" + "macau.")/*gameList.get(i).gameName.equals("澳门五路.")*/) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.BAC_ROOM_COUNT);
                                }
                            }
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_GAME_LIST, new Gson().toJson(gameList));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void notification(final String message) {
        ((TabHostActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final int PUSH_NOTIFICATION_ID = (0x001);
                final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
                final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";

//                                        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                    if (mNotificationManager != null) {
                        mNotificationManager.createNotificationChannel(channel);
                    }
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, PUSH_CHANNEL_ID);
                Intent notificationIntent = new Intent(mContext, TabHostActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
                builder.setContentTitle("系统公告")//设置通知栏标题
                        .setContentIntent(pendingIntent) //设置通知栏点击意图
//                                        .setSound(Uri.parse("android.resource://com.gangbeng.lazyperson/" + R.raw.suai));
                        .setContentText(message)
                        .setTicker(message) //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setSmallIcon(R.drawable.logo)//设置通知小ICON
                        .setChannelId(PUSH_CHANNEL_ID)
                        .setDefaults(Notification.DEFAULT_ALL);

                Notification notification = builder.build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                if (mNotificationManager != null) {
                    mNotificationManager.notify(PUSH_NOTIFICATION_ID, notification);
                }
            }
        });
    }


    public void hintDialog(Context mContext, String hint) {
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
}
