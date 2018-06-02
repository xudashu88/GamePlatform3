package com.gangbeng.basemodule.data;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 日期选择
 */

public class BirthdayData {
    //------------------------------------------日期三级联动开始---------------------------------------------------
    private ArrayList<Integer> arry_years = new ArrayList<>();
    private ArrayList<Integer> arry_months = new ArrayList<>();
    private int day;

    public void initYears() {
        arry_years.clear();
        int _i = getYear();
        for (int i = _i; i > 1980; i--) {
            arry_years.add(i);
        }
    }

    public void initMonths() {
        arry_months.clear();
        for (int i = 1; i <= 12; i++) {
            arry_months.add(i);
        }
    }

    private int getYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    /**
     * 计算每月多少天
     */
    private int calDays(int year, int month) {
        boolean leayyear;
        //2000是闰年 1900和2100不是闰年 其他能被4整除是闰年
        leayyear = ((year % 4) == 0) || (((year % 400) == 0) && ((year % 100) != 0));
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    day = 31;
                    break;
                case 2:
                    if (leayyear) {
                        day = 29;
                    } else {
                        day = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    day = 30;
                    break;
            }
        }
        return day;
    }

    private final Map<String, Map<String, List<String>>> threeDatas = new LinkedHashMap<>();

    private void init() {
        if (!threeDatas.isEmpty()) {
            return;
        }
        try {
            for (int i = 0; i < arry_years.size(); i++) {
                int myyear = arry_years.get(i);
                Map<String, List<String>> yearM = new LinkedHashMap<>();//用来保存 年-月
                if(myyear==getYear()){//获取当月
                    int _i=getMonth();
                    for(int m=1;m<=_i;m++){
                        arry_months.clear();
                        arry_months.add(m);
                        for (int j = 0; j < arry_months.size(); j++) {
                            int mymonth = arry_months.get(j);
                            List<String> mydays = new ArrayList<>();// 当前月的所有日
                            int myday = calDays(myyear, mymonth);
                            if(mymonth==getMonth()){//获取今日
                                int _ii=getDay();
                                myday=_ii;
                            }
                            for (int k = 1; k < myday + 1; k++) {
                                mydays.add(k + "日");//日
                            }
                            yearM.put(mymonth + "月", mydays);//月 日  LinkedHashMap 按顺序存
                        }
                    }
                }else {
                    initMonths();//其他年都为12个月
                    for (int j = 0; j < arry_months.size(); j++) {
                        int mymonth = arry_months.get(j);
                        List<String> mydays = new ArrayList<>();// 当前月的所有日
                        int myday = calDays(myyear, mymonth);
                        if(mymonth==getMonth()){//获取今日
                            int _i=getDay();
                            myday=_i;
                        }
                        for (int k = 1; k < myday + 1; k++) {
                            mydays.add(k + "日");//日
                        }
                        yearM.put(mymonth + "月", mydays);//月 日  LinkedHashMap 按顺序存
                    }
                }

                threeDatas.put(myyear + "年", yearM);//年 月
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Map<String, List<String>>> getAll() {
        init();//初始化地区
        return new LinkedHashMap<>(threeDatas);//怎么存就怎么取
    }
    //------------------------------------------日期三级联动结束---------------------------------------------------
}
