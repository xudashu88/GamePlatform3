package com.gangbeng.basemodule.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/2/6.
 */

public class TimeUtil {
    /**
     * 日期转换
     *
     * @param time   日期字符串
     * @param format 转换格式
     * @return
     */
    public static String formatLongToTime(String time, String format) {
        if (!TextUtils.isEmpty(time)) {
            time = new SimpleDateFormat(format).format(new Date(Long.valueOf(time) * 1000));
        }
        return time;
    }

    /*
     * 20180211 星期日
     * */
    public static String getNowDayByWeek() {
        long currentTime = System.currentTimeMillis();
        String timeNow = new SimpleDateFormat("yyyyMMdd EEEE").format(currentTime);
        return timeNow;
    }

    public static String getToday() {
        long currentTime = System.currentTimeMillis();
        String timeNow = new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
        return timeNow;
    }

    public static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
        return yesterday;
    }

    public static String getRecentWeek() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -6);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
        return yesterday;
    }

    public static String getWeek() {
        //判断当前是星期几 星期日-星期六为国际上的一周
        Calendar cal = Calendar.getInstance();
        /*获取一周七天的值*/
        for (int i = 0; i < 7; i++) {
            /*获取当前日历的日期的星期数（1:星期天）*/
            int week_index = cal.get(Calendar.DAY_OF_WEEK);
            Date date = cal.getTime();
            /*日期格式化 yyyy-MM-dd M必须大写*/
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(date);
            LogUtil.i("data-"+week_index + "," + dateString);
            System.out.println(week_index + "," + dateString);
            /*将日历日期推后1天*/
            cal.add(cal.DATE, -1);
        }
        return "";
    }

    /*
     * 传入20180211 获取星期几
     * */
    public static String getWeek(String pTime) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "星期天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "星期一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "星期二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "星期三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "星期四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "星期五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "星期六";
        }
        return Week;
    }

    public static String formatData(String data) {
        int _data = Integer.parseInt(data);
        if (_data < 10) {
            return "0" + _data;
        } else
            return "" + _data;
    }

    public static String reFormatData(String data) {
        int _data = Integer.parseInt(data);
        if (_data < 10) {
            return data.replace("0", "");
        } else
            return data;
    }
}
