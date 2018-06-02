package com.chuangku.gameplatform3.entity;


import java.io.Serializable;

/**
 * 百家乐
 */

public class Baccarat implements Serializable {
    public String id;
    public String waitressAvatar;//坐庄人头像
    public String waitressName;//坐庄人名称
    public String machineName;//机器编号
    public String betLimitRed;//下注限红
    public String peopleNum;//人数
    public String banker;//庄
    public String player;//闲
    public String ties;//和
    public String settling;//结算
}
