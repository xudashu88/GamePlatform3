package com.chuangku.gameplatform3.entity;


import java.io.Serializable;

/**
 * 筹码
 */

public class Chip implements Serializable {
    public String id;
    public int chipNumber;//筹码大小
    public int chipImg;//筹码图片
    public boolean isCheck;
}
