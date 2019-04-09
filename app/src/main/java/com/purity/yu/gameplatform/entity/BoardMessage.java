package com.purity.yu.gameplatform.entity;


import java.io.Serializable;

/**
 * 遊戲
 */

public class BoardMessage implements Serializable {
    public int win;//0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
}
