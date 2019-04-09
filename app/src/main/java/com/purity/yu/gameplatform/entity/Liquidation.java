package com.purity.yu.gameplatform.entity;


import java.io.Serializable;

/**
 * socket清算
 */

public class Liquidation implements Serializable {
    public String userId;//用户id
    public String userName;//用户昵称
    public int ratio;//0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
    public boolean win;//赢否
    public String balance;//剩余的金额
    public String score;//得到或者失去的金额

    public Liquidation(String userId, String userName, int ratio, boolean win, String balance, String score) {
        this.userId = userId;
        this.userName = userName;
        this.score = score;
        this.ratio = ratio;
        this.win = win;
        this.balance = balance;
    }
}
