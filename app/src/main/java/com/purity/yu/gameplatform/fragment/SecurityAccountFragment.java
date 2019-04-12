package com.purity.yu.gameplatform.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.http.HttpRequest;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;

@ContentView(R.layout.fragment_security_account)
public class SecurityAccountFragment extends BaseFragment {
    private static final String TAG = "SecurityAccountFragment";

    @BindView(R.id.et_old_login_pwd)
    public EditText et_old_login_pwd;
    @BindView(R.id.et_new_login_pwd)
    public EditText et_new_login_pwd;
    @BindView(R.id.et_confirm_login)
    public EditText et_confirm_login;
    @BindView(R.id.tv_modify_login)
    public TextView tv_modify_login;

    @BindView(R.id.et_old_pay_pwd)
    public EditText et_old_pay_pwd;
    @BindView(R.id.et_new_pay_pwd)
    public EditText et_new_pay_pwd;
    @BindView(R.id.et_confirm_pay)
    public EditText et_confirm_pay;
    @BindView(R.id.tv_modify_pay)
    public TextView tv_modify_pay;

    private Context mContext;

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        initEvent();
    }

    private void initEvent() {
        tv_modify_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyLoginPwd()) {
                    postModifyLoginPwd();
                }
            }
        });
        tv_modify_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyPayPwd()) {
                    postModifyPayPwd();
                }
            }
        });
    }

    private boolean verifyLoginPwd() {
        String oldLogin = et_old_login_pwd.getText().toString();
        String newLogin = et_new_login_pwd.getText().toString();
        String confirmLogin = et_confirm_login.getText().toString();

        if (TextUtils.isEmpty(oldLogin)) {
            ToastUtil.showToast(mContext, "旧登录密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(newLogin)) {
            ToastUtil.showToast(mContext, "新登录不能为空");
            return false;
        }
        if (TextUtils.isEmpty(confirmLogin)) {
            ToastUtil.showToast(mContext, "确认登录密码不能为空");
            return false;
        }
        if (!newLogin.equals(confirmLogin)) {
            ToastUtil.showToast(mContext, "新密码和确认新密码不相同");
            return false;
        }
        return true;
    }

    private boolean verifyPayPwd() {
        String oldLogin = et_old_pay_pwd.getText().toString();//登录密码
        String newLogin = et_new_pay_pwd.getText().toString();
        String confirmLogin = et_confirm_pay.getText().toString();

        if (TextUtils.isEmpty(oldLogin)) {
            ToastUtil.showToast(mContext, "登录密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(newLogin)) {
            ToastUtil.showToast(mContext, "新支付密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(confirmLogin)) {
            ToastUtil.showToast(mContext, "确认支付密码不能为空");
            return false;
        }
        if (!newLogin.equals(confirmLogin)) {
            ToastUtil.showToast(mContext, "新支付密码和确认新支付密码不相同");
            return false;
        }
        return true;
    }

    private void postModifyLoginPwd() {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        //todo 2018.12.4 token必须放在url后面(如果有签名也是)，其他参数放在body里面
        HashMap<String, String> map = new HashMap<>();
        map.put("old_password", et_old_login_pwd.getText().toString());
        map.put("password", et_new_login_pwd.getText().toString());
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.USER_PASSWORD + "?token=" + token)
                .addParam("old_password", et_old_login_pwd.getText().toString())
                .addParam("password", et_new_login_pwd.getText().toString())
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            String _data = json.optString("data");
                            if (code == 0) {
                                ToastUtil.showToast(mContext, "修改登录密码成功");
                            } else {
                                ToastUtil.showToast(mContext, "修改登录密码失败");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void postModifyPayPwd() {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        //todo 2018.12.4 token必须放在url后面(如果有签名也是)，其他参数放在body里面
        HashMap<String, String> map = new HashMap<>();
        map.put("password", et_old_pay_pwd.getText().toString());//登录密码。不是旧的支付密码
        map.put("password_pay", et_new_pay_pwd.getText().toString());
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.USER_PASSWORD_PAY + "?token=" + token)
                .addParam("password", et_old_pay_pwd.getText().toString())
                .addParam("password_pay", et_new_pay_pwd.getText().toString())
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            String _data = json.optString("data");
                            if (code == 0) {
                                ToastUtil.showToast(mContext, "修改支付密码成功");
                            } else {
                                ToastUtil.showToast(mContext, "修改支付密码失败");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
