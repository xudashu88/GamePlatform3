package com.purity.yu.gameplatform.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class GamePlay implements Serializable {
    /**
     * 玩法编号
     */
    public long id  ;

    /**
     * 游戏编号
     */
    public long gameId;

    /**
     * 创建者
     */
    public long createBy;

    /**
     * 创建时间
     */
    public long createTime;

    /**
     * 最小金币
     */
    public int minCoin;

    /**
     * 最大金币
     */
    public int maxCoin;

    /**
     * 最小下注金币
     */
    public int minBetCoin;

    /**
     * 最大局数
     */
    public int maxDesk;

    /**
     * 最小限红
     */
    public String minBet;

    /**
     * 最大限红
     */
    public String maxBet;

    /**
     *  投注限红
     */
    public String betLimit;

    public List<GameRoom> gameRooms=new ArrayList<>();
}
