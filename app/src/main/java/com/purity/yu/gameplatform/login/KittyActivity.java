package com.purity.yu.gameplatform.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.http.HttpRequest;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.PermissionsUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.util.HashMap;

import avi.AVLoadingIndicatorView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

@ContentView(R.layout.activity_kitty)
public class KittyActivity extends BaseActivity {
    //todo https://www.jianshu.com/p/54aad110bbdc https://ci.appveyor.com/project/Perfare/assetstudio/branch/master/artifacts 提取unity资源
    //https://blog.csdn.net/u013278099/article/details/50881431 android实现文字渐变效果和歌词进度的效果
    @BindView(R.id.rl_home)
    RelativeLayout rl_home;
    @BindView(R.id.tv_WeChat)
    TextView tv_WeChat;
    @BindView(R.id.tv_account)
    TextView tv_account;
    @BindView(R.id.tv_to_register)
    TextView tv_to_register;
    @BindView(R.id.tv_exit)
    TextView tv_exit;

    @BindView(R.id.rl_account)
    RelativeLayout rl_account;
    @BindView(R.id.et_user)
    EditText et_user;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_ip)
    EditText et_ip;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.tv_login)
    TextView tv_login;

    @BindView(R.id.rl_register)
    RelativeLayout rl_register;
    @BindView(R.id.et_register_user)
    EditText et_register_user;
    @BindView(R.id.et_register_password)
    EditText et_register_password;
    @BindView(R.id.et_register_code)
    EditText et_register_code;
    @BindView(R.id.et_register_ip)
    EditText et_register_ip;
    @BindView(R.id.tv_register_cancel)
    TextView tv_register_cancel;
    @BindView(R.id.tv_register_login)
    TextView tv_register_login;

    @BindView(R.id.rl_we_chat)
    RelativeLayout rl_we_chat;
    @BindView(R.id.et_we_chat_code)
    EditText et_we_chat_code;
    @BindView(R.id.et_ip_we_chat)
    EditText et_ip_we_chat;
    @BindView(R.id.tv_cancel_we_chat)
    TextView tv_cancel_we_chat;
    @BindView(R.id.tv_login_we_chat)
    TextView tv_login_we_chat;

    @BindView(R.id.rl_ip)
    RelativeLayout rl_ip;
    @BindView(R.id.et_add_ip)
    EditText et_add_ip;
    @BindView(R.id.tv_ip_cancel)
    TextView tv_ip_cancel;
    @BindView(R.id.tv_add_ip)
    TextView tv_add_ip;

    @BindView(R.id.rl_show)
    RelativeLayout rl_show;
    @BindView(R.id.tv_show)
    TextView tv_show;
    @BindView(R.id.rl_show_belle)
    RelativeLayout rl_show_belle;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    private Context mContext;
    private int page = 0;//0-主界面 1-微信登录界面 2-账号登录界面
    private String etUser;
    private String etPassword;
    private String etIp;
    private String etRegisterUser;
    private String etRegisterPassword;
    private String etRegisterCode;
    private String etAddIp;
    private String etWeChatCode;
    private String etIpWeChat;
    private String promoCode;

    @Override
    protected void initView(Bundle savedInstanceState) {
        mContext = this;
        PermissionsUtil.checkAndRequestPermissions(this);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.VERSION, 1);
        initShare();
    }

    private void initShare() {
        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL))) {
            LogUtil.i("邀请码=" + etWeChatCode + "---" + SharedPreUtil.getInstance(mContext).getString(Constant.PROMO_CODE));
            if (SharedPreUtil.getInstance(mContext).getInt(Constant.LOGIN_TYPE) == 0) {
                et_user.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
                et_password.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_PWD));
            }
            et_we_chat_code.setText(SharedPreUtil.getInstance(mContext).getString(Constant.PROMO_CODE));
            et_register_code.setText(SharedPreUtil.getInstance(mContext).getString(Constant.PROMO_CODE));
            et_ip.setText(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL));
            et_ip_we_chat.setText(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL));
            et_register_ip.setText(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL));
//            et_ip.setEnabled(false);
//            et_ip_we_chat.setEnabled(false);
        }
    }

    private void initEdit() {
        etUser = et_user.getText().toString().trim();
        etPassword = et_password.getText().toString().trim();
        etIp = et_ip.getText().toString().trim();
        etRegisterUser = et_register_user.getText().toString().trim();
        etRegisterPassword = et_register_password.getText().toString().trim();
        etRegisterCode = et_register_code.getText().toString().trim();
        etWeChatCode = et_we_chat_code.getText().toString().trim();
        etIpWeChat = et_ip_we_chat.getText().toString().trim();
        etAddIp = et_add_ip.getText().toString().trim();
    }

    @OnClick({R.id.tv_WeChat, R.id.tv_account, R.id.tv_to_register, R.id.tv_cancel, R.id.tv_login, R.id.tv_register_cancel,
            R.id.tv_register_login, R.id.tv_cancel_we_chat, R.id.tv_login_we_chat, R.id.tv_ip_cancel, R.id.tv_add_ip,
            R.id.tv_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_WeChat:
                page = 1;
                initShare();
                animView(rl_home, rl_we_chat);
                break;
            case R.id.tv_account:
                page = 2;
                initShare();
                animView(rl_home, rl_account);
                break;
            case R.id.tv_to_register:
                String _ip = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL);
                if (TextUtils.isEmpty(_ip)) {
                    if (page == 0) {
                        animView(rl_home, rl_ip);
                    } else if (page == 1) {
                        animView(rl_we_chat, rl_ip);
                    } else if (page == 2) {
                        animView(rl_account, rl_ip);
                    }
                } else {
                    if (page == 0) {
                        animView(rl_home, rl_register);
                    } else if (page == 1) {
                        animView(rl_we_chat, rl_register);
                    } else if (page == 2) {
                        animView(rl_account, rl_register);
                    }
                }
                break;
            case R.id.tv_ip_cancel:
                animView(rl_ip, rl_home);
                break;
            case R.id.tv_add_ip:
                if (verify2()) {
                    getAppInfo(etAddIp);
                }
                break;
            case R.id.tv_cancel:
                page = 0;
                animView(rl_account, rl_home);
                break;
            case R.id.tv_login:
                if (verify1()) {
                    if (Util.isFastClick(mContext, 3000)) {
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.LOGIN_TYPE, 0);
                        postLoginIn(etUser, etPassword, etIp, avi, "");
                    } else {
                        ToastUtil.show(mContext, "操作频繁！请稍后再试。");
                    }
                }
                //直接账号登录
                break;
            case R.id.tv_register_cancel:
                page = 0;
                animView(rl_register, rl_home);
                break;
            case R.id.tv_register_login:
                //直接注册
                if (verify4()) {
                    if (Util.isFastClick(mContext, 5000)) {
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.LOGIN_TYPE, 0);//第三方登录和注册
                        promoCode = et_register_code.getText().toString().trim();
                        LogUtil.i("注册 商户号=" + SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL));
                        getAppInfo(et_register_ip);
                    }
                }
                break;
            case R.id.tv_cancel_we_chat:
                page = 0;
                animView(rl_we_chat, rl_home);
                break;
            case R.id.tv_login_we_chat:
                //直接微信登录
                if (verify3()) {
                    if (Util.isFastClick(mContext, 5000)) {
                        loginThird();
                    }
                }
                break;
            case R.id.tv_exit:
                ProtocolUtil.getInstance().postLogOutSys(mContext);
                break;
        }
    }

    private boolean verify1() {//账号登录
        initEdit();
        if (TextUtils.isEmpty(etUser)) {
            alpha("用户名不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(etPassword)) {
            alpha("密码不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(etIp)) {
            alpha("商户号不能为空!");
            return false;
        }
        return true;
    }

    private boolean verify2() {//添加商户号
        initEdit();
        if (TextUtils.isEmpty(etAddIp)) {
            alpha("商户号不能为空!");
            return false;
        }
        return true;
    }

    private boolean verify3() {//微信登录
        initEdit();
        if (TextUtils.isEmpty(etWeChatCode)) {
            alpha("邀请码不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(etIpWeChat)) {
            alpha("商户号不能为空!");
            return false;
        }
        return true;
    }

    private boolean verify4() {//账号注册
        initEdit();
        if (TextUtils.isEmpty(etRegisterUser)) {
            alpha("用户名不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(etRegisterPassword)) {
            alpha("密码不能为空!");
            return false;
        }
        if (TextUtils.isEmpty(etRegisterCode)) {
            alpha("邀请码不能为空!");
            return false;
        }
        return true;
    }


    public void postLoginIn(final String username, final String pwd, final String et_ip, final AVLoadingIndicatorView avi, final String nickname) {
        String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(et_ip) + ServiceIpConstant.BASE_AFTER;
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", pwd);
        map.put("deviceid", ProtocolUtil.getInstance().getDeviceId());
        avi.show();
        LogUtil.i("登录=" + baseUrl + map.toString());
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
                            ProtocolUtil.getInstance().save(mContext, pwd, token, username, nickname, et_ip);
                        } catch (Exception e) {
                            avi.hide();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onResultFault(int code, String hint) {
                        super.onResultFault(code, hint);
                        ToastUtil.show(mContext, hint);
                        avi.hide();
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        super.onFailure(request, e);
                        ToastUtil.show(mContext, "连接超时");
                        avi.hide();
                    }
                });
    }

    public void getAppInfo(final String et_ip) {
        avi.show();
        String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(et_ip) + ServiceIpConstant.BASE_AFTER;
        LogUtil.i("是否能注册1=" + baseUrl + Constant.APP_INFO);
        HttpRequest.request(baseUrl + Constant.APP_INFO)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("是否注册=" + json);
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            int open_register = __data.optInt("open_register");
                            if (open_register == 1) {//1开放注册 其他关闭注册
                                avi.hide();
                                SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE_URL, et_ip);
                                et_register_ip.setText(et_ip);
                                animView(rl_ip, rl_register);
                            } else {
                                avi.hide();
                                alpha("需要注册，请联系管理员!");
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Request request, Exception e) {
                        super.onFailure(request, e);
                        avi.hide();
                    }
                });
    }

    public void getAppInfo(final EditText et_ip) {
        avi.show();
        String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(et_ip) + ServiceIpConstant.BASE_AFTER;
        HttpRequest.request(baseUrl + Constant.APP_INFO)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json;
                        JSONObject __data;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("是否注册=" + json);
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            int open_register = __data.optInt("open_register");
                            if (open_register == 1) {//1开放注册 其他关闭注册
                                SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE_URL, et_ip.getText().toString());
                                postRegister(etRegisterUser, etRegisterPassword, SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL), "");
                            } else {
                                avi.hide();
                                alpha("需要注册，请联系管理员!");
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void loginThird() {
        avi.show();
        ShareSDK.setEnableAuthTag(true);//SDK登录+标签必须设置成true
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.authorize();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                avi.hide();
            }
        }, 4000);
        wechat.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                System.out.println("------------失败" + arg2.toString());
                LogUtil.i("微信登录 失败=" + arg2.toString());
                arg2.printStackTrace();
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                String nickname = arg0.getDb().get("nickname");
                String gender = arg0.getDb().get("gender");
                String icon = arg0.getDb().get("icon").replace("\\", "");
                String userID = arg0.getDb().get("userID");//owXUB1jYwfQ0nG__zrT4xR31qmJ4
                String openid = arg0.getDb().get("openid");//owXUB1jYwfQ0nG__zrT4xR31qmJ4
                String unionid = arg0.getDb().get("unionid");//oZ-KJ0_HiZ0nNRGgRn41IELJAToE
                String export = arg0.getDb().exportData();

                String _arg2 = arg2.toString();
                LogUtil.i("微信登录 成功= unionid=" + unionid + " nickname=" + nickname + " gender=" + gender + " icon=" + icon + " export=" + export + " _arg2=" + arg0.getDb().toString());
                SharedPreUtil.getInstance(mContext).saveParam(Constant.WECHAT_ICON, icon);
                userID = userID.substring(0, 15);//后台只能16个字符 且至少4个字符
                SharedPreUtil.getInstance(mContext).saveParam(Constant.LOGIN_TYPE, 1);//第三方登录和注册
                SharedPreUtil.getInstance(mContext).saveParam(Constant.PROMO_CODE, "" + etWeChatCode);
                promoCode = et_we_chat_code.getText().toString().trim();
                postRegister(userID, unionid, etIpWeChat, nickname);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                System.out.println("-------取消");
                LogUtil.i("微信登录 取消");
            }
        });
        wechat.SSOSetting(false);//设置客户端授权
//        ShareSDK.setActivity(this);//抖音登录适配安卓9.0
        wechat.showUser(null);
    }

    private void postRegister(final String username, final String pwd, final String et_ip, final String nickname) {
        final String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(et_ip) + ServiceIpConstant.BASE_AFTER;
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", pwd);
        map.put("promo_code", promoCode);//邀请码默认写死
        map.put("deviceid", ProtocolUtil.getInstance().getDeviceId());
        map.put("nickname", nickname);
        LogUtil.i("微信注册1=" + map.toString());
        avi.show();
        HttpRequest.request(baseUrl + Constant.REGISTER)
                .addParam("username", username)
                .addParam("password", pwd)
                .addParam("promo_code", promoCode)
                .addParam("deviceid", ProtocolUtil.getInstance().getDeviceId())
                .addParam("nickname", nickname)
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        try {
                            JSONObject json;
                            json = new JSONObject(result);
                            LogUtil.i("微信注册1json" + json);
                            int code = json.optInt("code");
                            String token = json.optString("data");

                            if (code == 400) {
                                if (SharedPreUtil.getInstance(mContext).getInt(Constant.LOGIN_TYPE) == 0) {
                                    ToastUtil.show(mContext, token);
                                    return;
                                } else {
                                    LogUtil.i("微信已注册=" + nickname);
                                    postLoginIn(username, pwd, et_ip, avi, nickname);
                                }
                                return;
                            }
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            avi.hide();
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.PROMO_CODE, "" + promoCode);
                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.register_succeeded));
                            ProtocolUtil.getInstance().save(mContext, pwd, token, username, nickname, et_ip);
                        } catch (Exception e) {
                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.ip_invalid));
                            avi.hide();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onResultFault(int code, String hint) {
                        super.onResultFault(code, hint);
                        LogUtil.i("微信注册失败 onResultFault=" + code + " hint=" + hint);
                        if (SharedPreUtil.getInstance(mContext).getInt(Constant.LOGIN_TYPE) == 1) {
                            postLoginIn(username, pwd, et_ip, avi, nickname);
                        } else {
                            ToastUtil.show(mContext, hint);
                        }
                        return;
                    }
                });
    }

    private void alpha(String hint) {
        rl_show.setVisibility(View.VISIBLE);
        rl_show_belle.setVisibility(View.VISIBLE);
        tv_show.setText(hint);
        BaccaratUtil.getInstance().alphaShow(rl_show, 2000);
        BaccaratUtil.getInstance().alphaShow(rl_show_belle, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_show.setVisibility(View.GONE);
                rl_show_belle.setVisibility(View.GONE);
            }
        }, 2000);
    }

    private void animView(RelativeLayout rl_hide, RelativeLayout rl_show) {
        rl_hide.setVisibility(View.GONE);
        rl_show.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.BounceIn)
                .duration(600)
                .repeat(0)
                .pivot(YoYo.CENTER_PIVOT, YoYo.CENTER_PIVOT)
                .interpolate(new AccelerateInterpolator())
                .playOn(rl_show);
    }
}
