package com.gangbeng.basemodule.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gangbeng.basemodule.R;

/**
 * Toast
 */

public class ToastUtil {
    public static Context mContext;
    private static Toast mToast;

    public static void init(Context context) {
        mContext = context;
    }

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static Handler getMainHandler() {
        return mHandler;
    }

    /**
     * 判断当前线程是否是主线程
     *
     * @return true表示当前是在主线程中运行
     */
    public static boolean isUIThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static void runOnUIThread(Runnable run) {
        if (isUIThread()) {
            run.run();
        } else {
            mHandler.post(run);
        }
    }

    /**
     * 可以在子线程中调用
     */
    public static void show(final String msg) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {//为null再创建，不用等待
                    mToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                    View view = mToast.getView();
                    mToast.setView(view);
                }
                mToast.setText(msg);//这里必需设置，否则下次显示一直是上次的值
                mToast.show();
            }
        });
    }

    public static void showToast(final String info) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(mContext, info, Toast.LENGTH_LONG);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                    LinearLayout layout = (LinearLayout) mToast.getView();
                    layout.setBackgroundResource(R.drawable.bg_black_stroke_golden);
                    TextView tv = (TextView) mToast.getView().findViewById(android.R.id.message);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(50, 2, 50, 2);
                    tv.setLayoutParams(lp);
                    tv.setTextColor(Color.WHITE);
                    tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    tv.setTextSize(16);
                } else {
                    mToast.setText(info);
                }
                mToast.show();
            }
        });
    }

    public static void showLong(final String msg) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {//为null再创建，不用等待
                    mToast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                }
                mToast.setText(msg);//这里必需设置，否则下次显示一直是上次的值
                mToast.show();
            }
        });
    }
}
