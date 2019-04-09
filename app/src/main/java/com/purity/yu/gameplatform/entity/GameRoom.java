package com.purity.yu.gameplatform.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class GameRoom implements Serializable {
    /**
     * 房间编号
     */
    public String id;

    /**
     * 玩法编号
     */
    public String gamePlayId;

    /**
     * 房间名称
     */
    public String name;

    /**
     * 房间编码
     */
    public String code;

    /**
     * 创建时间
     */
    public long createTime;

    /**
     * 房间最大人数
     */
    public int maxPlayers;

    /**
     * 创建者
     */
    public String createBy;

    /**
     * 当前状态 0未开启 1已开启 2正在维护
     */
    public int status;

    /**
     * 最大限红
     */
    public List<Integer> maxBet=new ArrayList<>();

    /**
     * 最小限红
     */
    public List<Integer> minBet=new ArrayList<>();

    /**
     * 投注限红
     */
    public String betLimit;

    public String[] rtmp;//默认局域网ip
    public String[] dxRtmp;//电信
    public String[] ydRtmp;//移动
    public String[] color;

    /**
     * 识别端ip
     */
    public String commandIp;

    /**
     * 识别端编号
     */
    public String commandCode;

    /**
     * 倒计时
     */
    public int betSecond;

    public List<Integer> betLimitList;
    public List<Integer> maxBetList;
    public List<Integer> minBetList;
}
