package com.chuangku.gameplatform3.controler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.activity.SetLanguageActivity;
import com.chuangku.gameplatform3.activity.StartActivity;
import com.chuangku.gameplatform3.widget.HintDialog;
import com.chuangku.gameplatform3.widget.NexusStyleLockView;
import com.sevenheaven.gesturelock.GestureLock;
import com.sevenheaven.gesturelock.GestureLockView;

/**
 * 登录
 * Created by Administrator on 2018/5/15.
 */

public class LoginControler {
    private Context mContext;
    private static LoginControler instance = null;
    TextView tv_version;
    ImageButton imgBtn_language;
    EditText et_user;
    RelativeLayout rl_history_user;
    LinearLayout ll_user_pwd;
    EditText et_password;
    TextView tv_gesture;
    ToggleButton toggle_btn_select_type;
    TextView tv_keyboard;
    GestureLock gesture_lock;
    TextView tv_login;
    TextView tv_try_play;

    public void init(Context context) {
        mContext = context;

    }

    public static LoginControler getInstance() {
        if (null == instance) {
            instance = new LoginControler();
        }
        return instance;
    }

    public void copyWidget(TextView tv_version, ImageButton imgBtn_language, EditText et_user, RelativeLayout rl_history_user, LinearLayout ll_user_pwd, EditText et_password,
                           TextView tv_gesture, ToggleButton toggle_btn_select_type, TextView tv_keyboard, GestureLock gesture_lock, TextView tv_login, TextView tv_try_play) {
        this.tv_version = tv_version;
        this.imgBtn_language = imgBtn_language;
        this.et_user = et_user;
        this.rl_history_user = rl_history_user;
        this.ll_user_pwd = ll_user_pwd;
        this.et_password = et_password;
        this.tv_gesture = tv_gesture;
        this.toggle_btn_select_type = toggle_btn_select_type;
        this.tv_keyboard = tv_keyboard;
        this.gesture_lock = gesture_lock;
        this.tv_login = tv_login;
        this.tv_try_play = tv_try_play;
        initGesture();
        initEvent();
    }

    private void initEvent() {
        imgBtn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SetLanguageActivity.class));
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()) {
                    if (et_user.getText().toString().equals("123") && et_password.getText().toString().equals("123")) {
                        mContext.startActivity(new Intent(mContext, StartActivity.class));
                        ((Activity) mContext).finish();
                    }
                }
            }
        });
        tv_try_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, StartActivity.class));
                ((Activity) mContext).finish();
            }
        });
        toggle_btn_select_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean _toggle = toggle_btn_select_type.isChecked();
                if (_toggle == true) {
                    tv_gesture.setTextColor(mContext.getResources().getColor(R.color.mainGolden));
                    tv_keyboard.setTextColor(mContext.getResources().getColor(R.color.mainBlack));
                    ll_user_pwd.setVisibility(View.GONE);
                    gesture_lock.setVisibility(View.VISIBLE);
                    tv_login.setVisibility(View.GONE);
                } else {
                    tv_gesture.setTextColor(mContext.getResources().getColor(R.color.mainBlack));
                    tv_keyboard.setTextColor(mContext.getResources().getColor(R.color.mainGolden));
                    ll_user_pwd.setVisibility(View.VISIBLE);
                    gesture_lock.setVisibility(View.GONE);
                    tv_login.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean verify() {
        String _username = et_user.getText().toString();
        String _pwd = et_password.getText().toString();

        if (TextUtils.isEmpty(_username)) {
            hintDialog(mContext.getResources().getString(R.string.error_code_4_iv));
            return false;
        }
        if (TextUtils.isEmpty(_pwd)) {
            hintDialog(mContext.getResources().getString(R.string.password_alert));
            return false;
        }
        return true;
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


    private void initGesture() {
        gesture_lock.setAdapter(new GestureLock.GestureLockAdapter() {
            /**
             * 手势解锁的宽高数量
             */
            @Override
            public int getDepth() {
                return 3;
            }

            /**
             * 正确的解锁手势
             */
            @Override
            public int[] getCorrectGestures() {
                return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
            }

            /**
             * 最大可重试次数
             */
            @Override
            public int getUnmatchedBoundary() {
                return 500;
            }

            /**
             * block之前的间隔大小
             */
            @Override
            public int getBlockGapSize() {
                return 5;
            }

            /**
             * block的样式
             * @param context
             * @param position
             * @return
             */
            @Override
            public GestureLockView getGestureLockViewInstance(Context context, int position) {
                return new NexusStyleLockView(context);
            }
        });

        gesture_lock.setOnGestureEventListener(new GestureLock.OnGestureEventListener() {

            @Override
            public void onGestureEvent(boolean matched) {
                if (matched == true) {
                    mContext.startActivity(new Intent(mContext, StartActivity.class));
                    ((Activity) mContext).finish();
                }else {
                    hintDialog(mContext.getResources().getString(R.string.error_code_4_iv));
                }
                gesture_lock.clear();
            }

            @Override
            public void onUnmatchedExceedBoundary() {
//                Toast.makeText(LoginActivity.this, "输入5次错误!30秒后才能输入", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBlockSelected(int position) {
                Log.i("login", "onBlockSelected: " + position);
            }
        });
    }
}
