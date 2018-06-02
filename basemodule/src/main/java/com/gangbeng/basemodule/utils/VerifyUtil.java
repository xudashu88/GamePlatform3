package com.gangbeng.basemodule.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式验证类
 * Created by Administrator on 2018/4/18.
 */

public class VerifyUtil {
    public static boolean isMobile(String mobile) {
        String reg = "[1][345789]\\d{9}$";
        if (TextUtils.isEmpty(mobile)) {
            return false;
        } else {
            return mobile.matches(reg);
        }
    }

    /**
     * 判断一个字符串是否含有数字
     * @param content
     * @return
     */
    public static boolean hasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }
}
