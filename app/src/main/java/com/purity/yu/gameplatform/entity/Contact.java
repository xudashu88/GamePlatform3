package com.purity.yu.gameplatform.entity;

import java.io.Serializable;

public class Contact implements Serializable {
    public int id;
    public String url;//客服二维码图片地址
    public String remark;//备注  包含微信号

    public Contact() {
    }

    public Contact(int id, String url, String remark) {
        this.id = id;
        this.url = url;
        this.remark = remark;
    }
}
