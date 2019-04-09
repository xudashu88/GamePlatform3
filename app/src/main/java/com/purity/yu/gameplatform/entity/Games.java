package com.purity.yu.gameplatform.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class Games implements Serializable {
    /**
     * 游戏编号
     */
    public long id;
    /**
     * 游戏名称
     */
    public String gameName;
    /**
     * 游戏类型
     */
    public long skuId ;
    /**
     * 创建者
     */
    public long createBy ;
    /**
     * 最小金币
     */
    public int minCoin ;
    /**
     * 最大金币
     */
    public int max_coin ;
    /**
     * 最小下注金币
     */
    public int minBetCoin ;
    /**
     * 最大局数
     */
    public int maxDesk ;

    public List<GamePlay> gamePlays=new ArrayList<>();
}
