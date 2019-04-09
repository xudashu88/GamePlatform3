package com.purity.yu.gameplatform.widget;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by MQ on 2016/12/16.
 */

public class DpUtil {
    public static Context mContext;
    public static void init(Context context) {
        mContext = context;
    }
    /**
     * dp转换成px
     *
     * @param dp      dp
     * @return px值
     */
    public static float dp2px( float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    /**
     * sp转换成px
     *
     * @param context Context
     * @param sp      sp
     * @return px值
     */
    public static float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context Context
     * @return 屏幕宽度（像素）
     */
    public static int getScreenSizeWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context Context
     * @return 屏幕高度（像素）
     */
    public static int getScreenSizeHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
}
