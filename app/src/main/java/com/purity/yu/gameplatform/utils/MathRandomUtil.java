package com.purity.yu.gameplatform.utils;

import java.util.Random;

public class MathRandomUtil {
    //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
    /**
     * 0出现的概率为%40
     */
    public static double rate0 = 0.40;
    /**
     * 1出现的概率为%40
     */
    public static double rate1 = 0.40;
    /**
     * 2出现的概率为%2
     */
    public static double rate2 = 0.02;
    /**
     * 3出现的概率为%2
     */
    public static double rate3 = 0.02;
    /**
     * 4出现的概率为%2
     */
    public static double rate4 = 0.02;
    /**
     * 5出现的概率为%2
     */
    public static double rate5 = 0.02;
    public static double rate6 = 0.02;
    public static double rate7 = 0.02;
    public static double rate8 = 0.02;
    public static double rate9 = 0.02;
    public static double rate10 = 0.02;
    public static double rate11 = 0.02;


    /**
     * Math.random()产生一个double型的随机数，判断一下
     * 例如0出现的概率为%50，则介于0到0.50中间的返回0
     *
     * @return int
     */
    public int PercentageRandom() {
        double randomNumber;
        randomNumber = Math.random();
        if (randomNumber >= 0 && randomNumber <= rate0) {
            return 0;
        } else if (randomNumber >= rate0 / 100 && randomNumber <= rate0 + rate1) {
            return 1;
        } else if (randomNumber >= rate0 + rate1
                && randomNumber <= rate0 + rate1 + rate2) {
            return 2;
        } else if (randomNumber >= rate0 + rate1 + rate2
                && randomNumber <= rate0 + rate1 + rate2 + rate3) {
            return 3;
        } else if (randomNumber >= rate0 + rate1 + rate2 + rate3
                && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4) {
            return 4;
        } else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4
                && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4 + rate5) {
            return 5;
        } else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 + rate5
                && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6) {
            return 6;
        } else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6
                && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7) {
            return 7;
        } else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7
                && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7 + rate8) {
            return 8;
        } else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7 + rate8
                && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7 + rate8 + rate9) {
            return 9;
        } else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7 + rate8 + rate9
                && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7 + rate8 + rate9 + rate10) {
            return 10;
        } else if (randomNumber >= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7 + rate8 + rate9 + rate10
                && randomNumber <= rate0 + rate1 + rate2 + rate3 + rate4 + rate5 + rate6 + rate7 + rate8 + rate9 + rate10 + rate11) {
            return 11;
        }

        return -1;
    }

    public int scoreRandom(Random random) {
        return random.nextInt(10);
    }

    public static double formatDouble1(double d) {
        return (double) Math.round(d * 100) / 100;
    }

    public static String subString(String str) {
        String[] string = str.split("\\.");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(string[0]);
        if (string[1].length() > 2) {
            stringBuffer.append("." + string[1].substring(0, 2));
        } else {
            stringBuffer.append("." + string[1]);
        }
        return stringBuffer.toString();
    }

    /**
     * 测试主程序
     */
    public static void main(String[] args) {
        int i = 0;
        MathRandomUtil a = new MathRandomUtil();
        int[] arr = new int[100];
        int count0 = 0, count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0, count8 = 0, count9 = 0, count10 = 0, count11 = 0;
        for (i = 0; i < arr.length; i++) {
            arr[i] = a.PercentageRandom();
            if (arr[i] == 0) {
                count0++;
            } else if (arr[i] == 1) {
                count1++;
            } else if (arr[i] == 2) {
                count2++;
            } else if (arr[i] == 3) {
                count3++;
            } else if (arr[i] == 4) {
                count4++;
            } else if (arr[i] == 5) {
                count5++;
            } else if (arr[i] == 6) {
                count6++;
            } else if (arr[i] == 7) {
                count7++;
            } else if (arr[i] == 8) {
                count8++;
            } else if (arr[i] == 9) {
                count9++;
            } else if (arr[i] == 10) {
                count10++;
            } else if (arr[i] == 11) {
                count11++;
            }
        }
//        System.out.println("count0=" + count0 + " count1=" + count1 + " count2=" + count2 + " count3=" + count3 + " count4=" + count4 + " count5=" + count5 + " count5=" + count6 + " count6=" + count6 + " count7=" + count7 + " count8=" + count8 + " count9=" + count9 + " count10=" + count10 + " count11=" + count11);
    }
}