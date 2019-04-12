package com.purity.yu.gameplatform.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chuangku.languagemodule.MultiLanguageUtil;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentViewUtils;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.GameRoom;
import com.purity.yu.gameplatform.http.HttpRequest;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.PermissionsUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import avi.AVLoadingIndicatorView;
import butterknife.BindView;
import butterknife.ButterKnife;

//@ContentView(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.et_user)
    EditText et_user;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_invitation_code)
    EditText et_invitation_code;
    @BindView(R.id.tv_register)
    TextView tv_register;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_tv_service_web)
    TextView tv_tv_service_web;
    @BindView(R.id.et_ip)
    EditText et_ip;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private Context mContext;
    private String user;
    private String pwd;
    private String ip;
    private String promo_code;
    private long lastOnClickTime = 0;
    private DisplayMetrics displayMetrics;
    List<GameRoom> gameRoomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = RegisterActivity.this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        displayMetrics = mContext.getResources().getDisplayMetrics();
        ContentViewUtils.inject(this);
        gameRoomList = new ArrayList<>();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        if (width > height) {//横屏
            setContentView(R.layout.activity_register_sw);
        } else
            setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Util.getInstance().init(this);
        Util.getInstance().hideSystemNavigationBar();
        PermissionsUtil.checkAndRequestPermissions(this);
        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL))) {
            et_ip.setText(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL));
            ip = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL);
            et_invitation_code.setText(SharedPreUtil.getInstance(mContext).getString(Constant.PROMO_CODE));
        }
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()) {
                    long time = SystemClock.uptimeMillis();
                    if (time - lastOnClickTime <= 5000) {
                        return;
                    } else {
                        lastOnClickTime = time;
                        avi.show();
                        postRegister();
                    }
                }
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }
        });
        tv_tv_service_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String data = "http://103.231.167.85:8888/mobile/index.html";
                intent.setData(Uri.parse(data));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    private void postRegister() {
        final String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(ip) + ServiceIpConstant.BASE_AFTER;
        HashMap<String, String> map = new HashMap<>();
        map.put("username", user);
        map.put("password", pwd);
        map.put("promo_code", et_invitation_code.getText().toString());
        map.put("deviceid", ProtocolUtil.getInstance().getDeviceId());
        HttpRequest.request(baseUrl + Constant.REGISTER)
                .addParam("username", user)
                .addParam("password", pwd)
                .addParam("promo_code", et_invitation_code.getText().toString())
                .addParam("deviceid", ProtocolUtil.getInstance().getDeviceId())
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        try {
                            JSONObject json;
                            json = new JSONObject(result);
                            LogUtil.i("注册 postRegister=" + json);
                            int code = json.optInt("code");
                            String token = json.optString("data");
                            if (code == 400) {
                                ToastUtil.show(mContext, token);
                                avi.hide();
                                return;
                            }
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            avi.hide();
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.PROMO_CODE, "" + et_invitation_code.getText().toString());
                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.register_succeeded));
                            ProtocolUtil.getInstance().save(mContext, pwd, token, user, "", ip);
                        } catch (Exception e) {
                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.ip_invalid));
                            avi.hide();
                            e.printStackTrace();
                        }
                    }
                });
    }

    private boolean verify() {
        user = et_user.getText().toString();
        pwd = et_password.getText().toString();
//        ip = et_ip.getText().toString();
        ip = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL);
        promo_code = et_invitation_code.getText().toString();
        if (TextUtils.isEmpty(user.trim())) {
            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.username_not_null));
            return false;
        }
        if (TextUtils.isEmpty(pwd.trim())) {
            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.password_not_null));
            return false;
        }
//        if (TextUtils.isEmpty(ip)) {
//            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.ip_alert));
//            return false;
//        }
        return true;
    }
}
