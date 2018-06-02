package com.gangbeng.basemodule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.base.Constant;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okio.ByteString;

/**
 * Created by Administrator on 2018/1/18.
 */

public class Util {
    private Context mContext;
    private static Util instance = null;

    public void init(Context context) {
        mContext = context;
    }

    public static Util getInstance() {
        if (null == instance) {
            instance = new Util();
        }
        return instance;
    }
    public static String getId(Context context) {
        String id = null;
        try {
            id = SharedPreUtil.getInstance(context).getParam(Constant.USER_ID, String.class);
        } catch (Exception e) {
            id = String.valueOf(SharedPreUtil.getInstance(context).getParam(Constant.USER_ID, Integer.class));
        }
        return id;
    }



    public static String getToken(Context context) {
        return SharedPreUtil.getInstance(context).getParam(Constant.USER_TOKEN, String.class);
    }

    public static String encrypt(String pwd) {
        if (pwd.isEmpty()) {
            return "";
        }
        pwd = getMD5(pwd);
        LogUtil.e("md5", pwd);
        char[] target = new char[pwd.length()];
        for (int i = 0; i < target.length; ++i) {
            char c = (char) (pwd.charAt(i) - 3);
            target[i] = c;
        }
        return new String(target) + "-" + getNowUnixTime();
    }

    public static String getMD5(String val) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5bytes = messageDigest.digest(val.getBytes("UTF-8"));
            return ByteString.of(md5bytes).hex();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static long getNowUnixTime() {
        Date date = new Date();
        long time = date.getTime() / 1000L;
        return time;
    }

    /**
     * Long类型秒值时间转换成 2017年7月07日 00:00 HH:mm
     */
    public static String formartLongToTime(String time, String formart) {
//        String _time = String.valueOf(time);
        if (!TextUtils.isEmpty(time)) {
//            "yyyy-MM-dd HH:mm"
            SimpleDateFormat format = new SimpleDateFormat(formart);
            time = format.format(new Date(Long.valueOf(time) * 1000));
        }
        return time;
    }

    //InputMethodManager.HIDE_NOT_ALWAYS
    public static void hidenInputMethod(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 显示输入法
     */
    public static void showInputMethod(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getApplicationContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        //同时再使用该方法之前，view需要获得焦点，可以通过requestFocus()方法来设定。
//        view.requestFocus();
        inputMethodManager.showSoftInput(view, inputMethodManager.SHOW_FORCED);
    }

    /**
     * 设置TabLayout下划线宽度（最小宽度为最宽的TextView）
     */
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
        }
    }

    /*
    * 字符串非空null
    * */
    public static boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || str.length() == 0 || str.equals("null") || str.equals("0"))
            return true;
        else
            return false;
    }

    public static void setTextValue(String value, TextView textView) {
        if (isEmpty(value)) {
            textView.setText("0");
        } else
            textView.setText(value);
    }

    /**
     * @desc 设置对话框
     * @parm context 上下文
     * @parm themeResId 对话框样式
     * @parm addView 自定义对话框布局
     * @parm width 屏幕宽度的倍数
     * @parm height 屏幕高度的倍数
     * @parm dimAmount 背景透明度(0.6f)
     * @parm screenWidth 左上角X
     * @parm screenHeight 左上角Y
     * @parm gravity1 居中17 不设置为0
     * @parm gravity2 居上48
     */
    public static AlertDialog setDialog(Context context, int themeResId, View addView, float width, float height, float dimAmount,
                                        int screenWidth, int screenHeight, int gravity1, int gravity2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, themeResId).setView(addView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setGravity(gravity1 | gravity2);
        WindowManager.LayoutParams lp;
        lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = dimAmount;
        WindowManager m = ((Activity) context).getWindowManager();
        Display d = m.getDefaultDisplay();
        if (width != 0.0f) {
            lp.width = (int) (d.getWidth() * width);
        }
        if (height != 0.0f) {
            lp.height = (int) (d.getHeight() * height);
        }
        if (screenWidth != 0) {
            lp.x = screenWidth; // 新位置X坐标
        }
        if (screenHeight != 0) {
            lp.y = screenHeight; // 新位置Y坐标
        }
        alertDialog.getWindow().setAttributes(lp);
        return alertDialog;
    }

}
