package com.purity.yu.gameplatform.entity;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 额度记录
 */
public class QuotaRecords implements Serializable {
    @SerializedName("trade_no")
    public String transactionNo;//交易单号
    @SerializedName("created_at")
    public String operateTime;//操作时间(美东)
    @SerializedName("switch")//1收入 2支出
    public String recordType;//类别
    @SerializedName("trade_type_id")//1存款 2取款 3上分 4下分 5人工增加 6 人工减少
    public String tradeTypeId;//类别
    public String amount;//根据switch判断是收入还是支出
    //    @SerializedName("amount")
    public String outcome;//支出
    @SerializedName("after_amount")
    public String afterTransaction;//交易后额度
}
