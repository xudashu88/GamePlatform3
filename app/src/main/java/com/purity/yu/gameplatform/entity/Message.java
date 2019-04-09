package com.purity.yu.gameplatform.entity;

import java.io.Serializable;

public class Message implements Serializable {
    public int id;
    public String title;
    public String content;
    public String is_deleted;
    public String sender_name;
    public String created_at;
    public int is_read;//0未读 1已读
}
