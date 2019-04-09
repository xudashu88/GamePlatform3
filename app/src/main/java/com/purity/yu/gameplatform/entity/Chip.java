package com.purity.yu.gameplatform.entity;


import android.widget.ImageView;

import java.io.Serializable;

/**
 * 筹码
 */

public class Chip implements Serializable {
    public String id;
    public int chipNumber;//筹码大小
    public int chipImg;//筹码图片
    public boolean isCheck;
    public ImageView imageView;
    public Chip(){}
    public Chip(int chipNumber){
        this.chipNumber = chipNumber;
    }
}
