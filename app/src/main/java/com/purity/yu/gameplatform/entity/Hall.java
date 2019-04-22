package com.purity.yu.gameplatform.entity;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 百家乐大厅
 */

public class Hall implements Serializable {
    public String id;
    //    public String waitressAvatar;//坐庄人头像
//    public String waitressName;//坐庄人名称
    public String roomId;//机器编号
    public String betLimitRed;//下注限红
    public List<Player> playerList = new ArrayList<>();//玩家
    public String banker;//庄
    public String player;//闲
    public int players;//人数
    public String ties;//和
    public int second;//倒计时
    public String state;//状态
    @SerializedName("peoplenumber")
    public int peopleNumber;//人数
    @SerializedName("gameroom")
    public String gameRoom;//房间号
    public int peopleNum;
    public String[] color;

    public List<Integer> boardMessageList = new ArrayList<>();
    public List<String> cardList = new ArrayList<>();

    /**
     * 百家乐大厅
     *
     * @param roomId
     * @param state
     * @param second
     * @param peopleNumber
     * @param boardMessageList
     */
    public Hall(String roomId, String state, int second, int peopleNumber, List<Integer> boardMessageList, int peopleNum, String[] color) {
        this.roomId = roomId;
        this.state = state;
        this.second = second;
        this.peopleNumber = peopleNumber;
        this.boardMessageList.addAll(boardMessageList);
        this.peopleNum = peopleNum;
        this.color = color;
    }

    /**
     * 街机大厅
     *
     * @param gameRoom
     * @param peopleNumber
     * @param playerList
     */
    public Hall(String gameRoom, int peopleNumber, List<Player> playerList) {
        this.gameRoom = gameRoom;
        this.peopleNumber = peopleNumber;
        this.playerList = playerList;
    }

    public Hall(String roomId, String state, int second, int peopleNumber, List<Integer> boardMessageList, List<String> cardList) {
        this.roomId = roomId;
        this.state = state;
        this.second = second;
        this.peopleNumber = peopleNumber;
        this.boardMessageList.addAll(boardMessageList);
        this.cardList = cardList;
    }

}
