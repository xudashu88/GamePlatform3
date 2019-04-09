package com.purity.yu.gameplatform.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chuangku.languagemodule.MultiLanguageUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentViewUtils;
import com.purity.yu.gameplatform.service.MusicService;
import com.purity.yu.gameplatform.utils.BaccaratUtil;

import butterknife.ButterKnife;

/**
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static Typeface typeface1;//https://github.com/forJrking/FontZip 字体提取

    //庄闲和点问路取消确定天开始游戏澳门五会员余额押注得分桌台限红在线人数菜单返回非免佣记录说明子未托管百家乐总下号厅视讯系统存款专区取安全账户联系客户设置规则息退出收微信当天投量利润次
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
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //横屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //竖屏
        getWindow().setBackgroundDrawable(null);//减少过度绘制次数 在<item name="android:windowBackground">@null</item>设置会出页面重影
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ContentViewUtils.inject(this);
        ButterKnife.bind(this);
        Util.getInstance().init(this);
        Util.getInstance().hideSystemNavigationBar();

        initView(savedInstanceState);
//        if (!isTaskRoot()) {
//            finish();
//            return;
//        }
        if (android.os.Build.VERSION.SDK_INT > 9) {//NetworkOnMainThreadException 在主线程中请求网络操作，将会抛出此异常
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        registerHomeKeyReceiver();
        // 添加来电监听事件
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); // 获取系统服务
        telManager.listen(new MobilePhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);

    }

    protected abstract void initView(Bundle savedInstanceState);

    @Override
    public void onBackPressed() {
        if (SharedPreUtil.getInstance(this).getInt(Constant.IS_CHIP_SUCCESS) != 0) {
            ToastUtil.showToast(this, this.getResources().getString(R.string.normal_lock_alert));
            return;
        } else {
            super.onBackPressed();
        }
    }

    /**
     * i18n
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    /**
     * activity.finish()之后，调用
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        Util.getInstance().init(this);
        Util.getInstance().hideSystemNavigationBar();//关闭虚拟键盘
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreUtil.getInstance(this).getInt(Constant.MUSIC_SWITCH) == 2) {
//            BaccaratUtil.getInstance().openSound(this);
//            startService(new Intent(this, MusicService.class));
            if (mService == null) {
                Intent intent = new Intent(this, MusicService.class);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }
            //延迟加载是为了mService可能没初始化成功，导致为null
            if (mService != null) {
                mService.playMusic();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mService != null) {
            registerHomeKeyReceiver();//屏幕不可见时，监听HOME键
        }
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        if (mService != null && mConnection != null && SharedPreUtil.getInstance(this).getInt(Constant.MUSIC_SWITCH) == 2) {
            unbindService(mConnection);
        }
        super.onDestroy();
    }

    //--------------------------------------------全局监听HOME键 start--------------------------------------------------
    private HomeKeyBroadCastReceiver mReceiver;
    private int count = 0;

    private void registerHomeKeyReceiver() {
        mReceiver = new HomeKeyBroadCastReceiver();
        // 注册广播，注意action:Intent.ACTION_CLOSE_SYSTEM_DIALOGS，添加这个action才能监听到按下Home键发出的广播
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    class HomeKeyBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BaccaratUtil.getInstance().closeSound();
            // 在这里处理HomeKey事件
            if (count == 0 && mService != null) {
                mService.pauseMusic();
            }
            count++;
            if (count == 4) {
                count = 0;
            }
        }
    }

    /**
     * 电话监听器类
     */
    private class MobilePhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 挂机状态
                    if (mService != null) {
                        mService.playMusic();
                    }
//                    BaccaratUtil.getInstance().openSound(getApplication());
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:    //通话状态
                case TelephonyManager.CALL_STATE_RINGING:    //响铃状态
                    if (mService != null) {
                        mService.pauseMusic();
                    }
                    BaccaratUtil.getInstance().closeSound();
                    break;
                default:
                    break;
            }
        }
    }

    //--------------------------------------------全局监听HOME键 start--------------------------------------------------


    //--------------------------------------------Service start--------------------------------------------------
    public MusicService mService = null;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) binder;
            mService = musicBinder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };
    //--------------------------------------------Service end--------------------------------------------------
}
