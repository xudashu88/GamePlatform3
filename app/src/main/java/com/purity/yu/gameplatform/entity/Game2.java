package com.purity.yu.gameplatform.entity;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 遊戲
 */

public class Game2 implements Serializable {
    public String id;
    public int gameDot;//遊戲台數
    @SerializedName("game_icon_url")
    public String gameIconUrl;
    @SerializedName("game_name")
    public String gameName;//遊戲名稱
    @SerializedName("game_type_id")
    public String gameTypeId;//遊戲名稱
    @SerializedName("game_name_en")
    public String gameNameEn;//遊戲英文名稱
    @SerializedName("platform_code")
    public String platformCode;//平台代码
}
