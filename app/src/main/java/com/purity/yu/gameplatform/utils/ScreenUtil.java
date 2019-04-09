package com.purity.yu.gameplatform.utils;

import android.content.res.Resources;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/6/15.
 */
public class ScreenUtil {
    public static float dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }
}
