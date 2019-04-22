package com.purity.yu.gameplatform.entity;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 筹码
 */

public class Player implements Serializable {
    //    public String id;
    @SerializedName("playuser")
    public String playUser;//玩家编号
    public String bei;
    public String money;
    public int position;//玩家在打鱼机的位置

    public Player() {
    }

    public Player(String playUser, String bei, String money, int position) {
        this.playUser = playUser;
        this.bei = bei;
        this.money = money;
        this.position = position;
    }
}
