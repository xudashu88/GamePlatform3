package com.chuangku.gameplatform3.entity;


import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 遊戲
 */

public class Game implements Serializable {
    public String id;
    @SerializedName("create_time")
    public Drawable gameAvatar;//遊戲頭像
    @SerializedName("staff_name")
    public int gameDot;//遊戲台數
    public String gameName;//遊戲名稱
}
