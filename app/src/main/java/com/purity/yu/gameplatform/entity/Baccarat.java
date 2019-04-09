package com.purity.yu.gameplatform.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 百家乐
 */

public class Baccarat implements Serializable {
    public String id;
    public String waitressAvatar;//坐庄人头像
    public String waitressName;//坐庄人名称
    public String roomId;//机器编号
    public String roomName;//房间名称
    public String[] rtmp;//局域网rtmp
    public String[] ydRtmp;//移动rtmp
    public String[] dxRtmp;//电信rtmp
    public String[] color;//颜色

    public String betLimitRed;//下注限红
    public String peopleNum;//人数
    public String banker;//庄
    public String player;//闲
    public String ties;//和
    public String settling;//结算
    public String[] url;//视频地址 高清视频和普通视频
    public int betSecond;//倒计时
    public List<Integer> betLimitList;
    public List<Integer> maxBetList=new ArrayList<>();
    public List<Integer> minBetList=new ArrayList<>();
}
