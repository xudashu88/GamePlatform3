package com.purity.yu.gameplatform.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuangku.languagemodule.CommSharedUtil;
import com.chuangku.languagemodule.LanguageType;
import com.chuangku.languagemodule.MultiLanguageUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentViewUtils;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.controler.LoginController;
import com.purity.yu.gameplatform.utils.PermissionsUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.widget.NetStateDialog;

import avi.AVLoadingIndicatorView;
import butterknife.BindView;
import butterknife.ButterKnife;

//@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.imgBtn_language)
    ImageButton imgBtn_language;
    @BindView(R.id.et_user)
    EditText et_user;
    @BindView(R.id.et_ip)
    EditText et_ip;
    @BindView(R.id.ll_user_pwd)
    LinearLayout ll_user_pwd;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_register)
    TextView tv_register;
    @BindView(R.id.tv_service_online)
    TextView tv_service_online;
    @BindView(R.id.tv_login_out)
    TextView tv_login_out;
    @BindView(R.id.iv_wechat)
    ImageView iv_wechat;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private DisplayMetrics displayMetrics;
    private long lastOnClickTime = 0;
    public static Typeface typeface1;//https://github.com/forJrking/FontZip 字体提取
    NetWorkStateReceiver netWorkStateReceiver;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (typeface1 == null) {
            typeface1 = Typeface.createFromAsset(getAssets(), "fonts/fontzipMin1.ttf");//全局设置字体
        }
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);

                // 如果控件文字类型属于TextView，则加载第一种字体
                if (view != null && (view instanceof TextView)) {
                    ((TextView) view).setTypeface(typeface1);
                }

                // 如果控件文字类型属于EditText，则加载第二种字体
                if (view != null && (view instanceof EditText)) {
                    ((EditText) view).setTypeface(null);
                }
                return view;
            }
        });

        super.onCreate(savedInstanceState);
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        this.mContext = LoginActivity.this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        displayMetrics = mContext.getResources().getDisplayMetrics();
        ContentViewUtils.inject(this);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        if (width > height) {//横屏
            setContentView(R.layout.activity_login_sw);
        } else
            setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Util.getInstance().init(this);
        Util.getInstance().hideSystemNavigationBar();
        PermissionsUtil.checkAndRequestPermissions(this);

        initLanguage();//8196 9620 9288
        initBgm();
        LoginController.getInstance().init(this);
        LoginController.getInstance().copyWidget(tv_version, imgBtn_language, et_user, ll_user_pwd, et_password,
                tv_login, tv_register, avi, et_ip, tv_service_online, tv_login_out, iv_wechat);
        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL))) {
            et_ip.setText(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    private void initBgm() {
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.MUSIC_SWITCH) == 2) {
            //startService是为了其他页面也能播放背景音乐
//            startService(new Intent(this, MusicService.class));
            //bindService是为了调用服务中的方法设置背景音量
//            Intent intent = new Intent(this, MusicService.class);
//            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            //延迟加载是为了mService可能没初始化成功，导致为null
        }
    }

    private void initLanguage() {
        int languageType = CommSharedUtil.getInstance(this).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_EN) {
            imgBtn_language.setBackground(getResources().getDrawable(R.drawable.english));
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED || languageType == 0) {
            imgBtn_language.setBackground(getResources().getDrawable(R.drawable.simplified_chinese));
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            imgBtn_language.setBackground(getResources().getDrawable(R.drawable.tradition_chinese));
        }
    }

    //连接网络 在onResume()方法注册
    @Override
    protected void onResume() {
        super.onResume();
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);

    }

    //连接网络 onPause()方法注销 世界boss-410 2018.11.13 9:11
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(netWorkStateReceiver);
    }

    class NetWorkStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            } else {
                boolean success = false;
                /**
                 * 获得网络连接服务
                 */
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo.State state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                if (NetworkInfo.State.CONNECTED == state) {
                    success = true;
                }
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
                    final NetworkInfo.State state2 = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                    if (NetworkInfo.State.CONNECTED == state2) {
                        success = true;
                    }
                }

                if (!success) {
                    //android.viewdowManager$BadTokenException: Unable to add window -- token null is not for an application
//                netStateDialog(mContext.getResources().getString(R.string.network_not_available), success);
                    final NetStateDialog myDialog = new NetStateDialog(mContext);
                    myDialog.setContent(context.getResources().getString(R.string.network_not_available));
                    myDialog.show();
                    myDialog.setAttributes();//需先显示，然后才能查找控件
                    myDialog.setCanceledOnTouchOutside(false);

                    myDialog.setOnPositiveListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.exit(0);
                            myDialog.dismiss();
                        }
                    });
                    final boolean finalSuccess = success;
                    myDialog.setOnNegativeListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (finalSuccess) {
                                myDialog.dismiss();
                            } else {
                                refresh();
                            }
                        }
                    });
                } else {
//                Toast.makeText(context,"网络连接成功",Toast.LENGTH_LONG).show();
                }
            }
        }

        /**
         * 刷新
         */
        private void refresh() {
            finish();
            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        long time = System.currentTimeMillis();
        if (time - lastOnClickTime > 2000) {
            Toast.makeText(this, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
            lastOnClickTime = time;
            return;
        } else {
            ProtocolUtil.getInstance().postLogOutSys(mContext);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
