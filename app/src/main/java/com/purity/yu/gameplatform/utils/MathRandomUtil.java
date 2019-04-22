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
        if (string.length > 2) {
            if (string[1].length() > 2) {
                stringBuffer.append("." + string[1].substring(0, 2));
            } else {
                stringBuffer.append("." + string[1]);
            }
        }
        return stringBuffer.toString();
    }

    public static boolean isFive(int n, int m) {
        int num = m + (int) (Math.random() * (n + 1 - m));

        if (num % 5 != 0) {
            System.out.println("随机数=" + num + " 不满足");
            return false;
        }
        System.out.println("随机数=" + num + " 满足");
        return true;
    }

    /**
     * 测试主程序
     */
    public static void main(String[] args) {
        int randomPostDelayed1 = new Random().nextInt(5);//0-4
        int randomPostDelayed2 = new Random().nextInt(5) + 5;//5-9
        int randomPostDelayed3 = new Random().nextInt(5) + 10;//10-14
        int randomPostDelayed4 = new Random().nextInt(5) + 15;//15-19
        int randomPostDelayed5 = new Random().nextInt(5) + 20;//20-24
        int randomPostDelayed6 = new Random().nextInt(5) + 20;//1-5
        int randomPostDelayed7 = new Random().nextInt(5) + 20;//1-5
        int randomPostDelayed8 = new Random().nextInt(5) + 20;//1-5
        int randomPostDelayed9 = new Random().nextInt(5) + 20;//1-5
        System.out.println("randomPostDelayed1=" + randomPostDelayed1 + " randomPostDelayed2=" + randomPostDelayed2 +
                " randomPostDelayed3=" + randomPostDelayed3 + " randomPostDelayed4=" + randomPostDelayed4 +
                " randomPostDelayed5=" + randomPostDelayed5 + " randomPostDelayed6=" + randomPostDelayed6 +
                " randomPostDelayed7=" + randomPostDelayed7 + " randomPostDelayed8=" + randomPostDelayed8 +
                " randomPostDelayed9=" + randomPostDelayed9);
    }
}