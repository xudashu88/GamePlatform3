package com.chuangku.gameplatform3.base;

import com.chuangku.gameplatform3.BuildConfig;

/**
 * Created by yucaihua on 2018/5/15.
 */

public class Constant {
    public static final String IMAGESITE = BuildConfig.ImageSite;
    public static final String IMAGESITE2 = BuildConfig.ImageSite2;
    public static final String USER_ID = "id";
    public static final String USER_TOKEN = "token";
    public static final String USER_LEVEL = "user_level";

    public static final String TOGGLE_ON = "toggle_on";//好路排序-開
    public static final String TOGGLE_OFF = "toggle_off";//好路排序-關
    public static final String COMMISSION_OFF = "commission_off";//非免佣-開
    public static final String COMMISSION_ON = "commission_on";//好路排序-關
    public static final String CHIP_POSITION = "chip_position";//选中的筹码位置
    public static final String IS_EXPAND = "is_expand";//
    /**
     * 60秒倒计时
     */
    public static final int MAX_SECOND = 3;
    /**
     * Handler倒计时状态码
     */
    public static final int WHAT_COUNT_DOWN = 0x001;
}
