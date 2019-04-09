package com.purity.yu.gameplatform.entity;

import java.io.Serializable;

public class Notice implements Serializable {
    public int id;
    public String content;//内容
    public String expire_at;//过期时间
    public String created_at;//创建时间

    public Notice() {
    }

    public Notice(String content, String expire_at, String created_at) {
        this.content = content;
        this.expire_at = expire_at;
        this.created_at = created_at;
    }
}
