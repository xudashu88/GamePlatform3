package com.purity.yu.gameplatform.controler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.SetLanguageActivity;
import com.purity.yu.gameplatform.activity.SuggestionActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.entity.GameRoom;
import com.purity.yu.gameplatform.login.KittyActivity;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.widget.HintDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import avi.AVLoadingIndicatorView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录
 * Created by Administrator on 2018/5/15.
 */

public class LoginController {
    private static final String TAG = "LoginController";
    private Context mContext;
    private static LoginController instance = null;
    TextView tv_version;
    ImageButton imgBtn_language;
    EditText et_user;
    EditText et_ip;
    LinearLayout ll_user_pwd;
    EditText et_password;
    TextView tv_login;
    TextView tv_register;
    AVLoadingIndicatorView avi;
    TextView tv_service_online;
    TextView tv_login_out;
    ImageView iv_wechat;
    private String username;
    private String pwd;
    private long lastOnClickTime = 0;
    private String ip;
    List<GameRoom> gameRoomList;

    public void init(Context context) {
        mContext = context;
        gameRoomList = new ArrayList<>();
    }

    public static LoginController getInstance() {
        if (null == instance) {
            instance = new LoginController();
        }
        return instance;
    }

    public void copyWidget(TextView tv_version, ImageButton imgBtn_language, EditText et_user, LinearLayout ll_user_pwd, EditText et_password,
                           TextView tv_login, TextView tv_register,
                           AVLoadingIndicatorView avi, EditText et_ip,
                           TextView tv_service_online, TextView tv_login_out, ImageView iv_wechat) {
        this.tv_version = tv_version;
        this.imgBtn_language = imgBtn_language;
        this.et_user = et_user;
        this.ll_user_pwd = ll_user_pwd;
        this.et_password = et_password;
        this.tv_login = tv_login;
        this.tv_register = tv_register;
        this.avi = avi;
        this.et_ip = et_ip;
        this.tv_service_online = tv_service_online;
        this.tv_login_out = tv_login_out;
        this.iv_wechat = iv_wechat;
        tv_version.setText("20190408");
        initEvent();
        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME))) {
            et_user.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
            et_password.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_PWD));
        }
    }

    private void initEvent() {
        tv_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, KittyActivity.class));
            }
        });
        tv_service_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SuggestionActivity.class));
            }
        });
        tv_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolUtil.getInstance().postLogOutSys(mContext);
            }
        });
        imgBtn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SetLanguageActivity.class));
                ((Activity) mContext).finish();
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()) {
                    long time = SystemClock.uptimeMillis();
                    if (time - lastOnClickTime <= 5000) {
                        return;
                    } else {
                        lastOnClickTime = time;
                        avi.show();
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.LOGIN_TYPE, 0);
                        ProtocolUtil.getInstance().postLoginIn(mContext, username, pwd, et_ip, avi, "");
                    }
                }
            }
        });
        iv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_ip.getText().toString())) {
                    hintDialog(mContext.getResources().getString(R.string.ip_alert));
                    return;
                } else
                    loginThird();
            }
        });
        et_ip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //转换成正确的ip
//                    if (changeIp()) return false;
                }
                return false;
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.默认可以显示注册 2.点击注册前获取商户号 3.通过APP_INFO的open_register为1可注册
                if (TextUtils.isEmpty(et_ip.getText().toString())) {
                    hintDialog(mContext.getResources().getString(R.string.ip_alert));
                    return;
                } else {
                    ProtocolUtil.getInstance().getAppInfo(et_ip);
                }
            }
        });
    }


    private boolean verify() {
        username = et_user.getText().toString();
        pwd = et_password.getText().toString();
        ip = et_ip.getText().toString();
        if (TextUtils.isEmpty(username)) {
            hintDialog(mContext.getResources().getString(R.string.error_code_4_iv));
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            hintDialog(mContext.getResources().getString(R.string.password_alert));
            return false;
        }
        if (TextUtils.isEmpty(ip)) {
            hintDialog(mContext.getResources().getString(R.string.ip_alert));
            return false;
        }
        return true;
    }


    public void hintDialog(String hint) {
        final HintDialog myDialog = new HintDialog(mContext);
        myDialog.setContent(hint);
        myDialog.show();
        myDialog.setAttributes(0.5f, 0.3f);//需先显示，然后才能查找控件
        myDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
    }

    private void loginThird() {
        avi.show();
        ShareSDK.setEnableAuthTag(true);//SDK登录+标签必须设置成true
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.authorize();

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
                ProtocolUtil.getInstance().postRegister(mContext, userID, unionid, et_ip, nickname, avi);
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
}
