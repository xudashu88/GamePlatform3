package com.chuangku.gameplatform3.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.annotation.ContentView;
import com.chuangku.gameplatform3.base.BaseActivity;
import com.chuangku.gameplatform3.controler.LoginControler;
import com.chuangku.gameplatform3.widget.NetStateDialog;
import com.chuangku.languagemodule.CommSharedUtil;
import com.chuangku.languagemodule.LanguageType;
import com.chuangku.languagemodule.MultiLanguageUtil;
import com.sevenheaven.gesturelock.GestureLock;

import butterknife.BindView;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.imgBtn_language)
    ImageButton imgBtn_language;
    @BindView(R.id.et_user)
    EditText et_user;
    @BindView(R.id.rl_history_user)
    RelativeLayout rl_history_user;
    @BindView(R.id.ll_user_pwd)
    LinearLayout ll_user_pwd;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.tv_gesture)
    TextView tv_gesture;
    @BindView(R.id.toggle_btn_select_type)
    ToggleButton toggle_btn_select_type;
    @BindView(R.id.tv_keyboard)
    TextView tv_keyboard;
    @BindView(R.id.gesture_lock)
    GestureLock gesture_lock;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_try_play)
    TextView tv_try_play;

    NetWorkStateReceiver netWorkStateReceiver;
    private Context mContext;

    @Override
    protected void initView() {
        this.mContext = LoginActivity.this;
        LoginControler.getInstance().init(this);
        LoginControler.getInstance().copyWidget(tv_version, imgBtn_language, et_user, rl_history_user, ll_user_pwd, et_password, tv_gesture, toggle_btn_select_type, tv_keyboard,
                gesture_lock, tv_login, tv_try_play);
        initLanguage();

    }

    private void initLanguage() {
        int languageType = CommSharedUtil.getInstance(this).getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_EN) {
            imgBtn_language.setBackground(getResources().getDrawable(R.drawable.english));
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            imgBtn_language.setBackground(getResources().getDrawable(R.drawable.simplified_chinese));
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            imgBtn_language.setBackground(getResources().getDrawable(R.drawable.tradition_chinese));
        }
    }

    //在onResume()方法注册
    @Override
    protected void onResume() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        unregisterReceiver(netWorkStateReceiver);
        super.onPause();
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
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!=null){
                    final NetworkInfo.State state2= connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                    if (NetworkInfo.State.CONNECTED == state2) {
                        success = true;
                    }
                }

                if (!success) {
                    //android.view.WindowManager$BadTokenException: Unable to add window -- token null is not for an application
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

        public void netStateDialog(String hint, final boolean success) {

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
}
