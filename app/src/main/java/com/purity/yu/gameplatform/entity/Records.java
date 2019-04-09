package com.purity.yu.gameplatform.entity;


import android.view.View;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 记录
 */
public class Records implements Serializable {
    @SerializedName("record_id")
    public String billNo;//订单号
    @SerializedName("bet_at")
    public String betTime;//下注时间
    @SerializedName("period_round")
    public String gameCode;//局号
    @SerializedName("game_type")
    public String playType;//玩法 1-龙(龙虎) 2-虎(龙虎)  3-庄(百家乐) 4-闲-(百家乐) 5-庄龙宝(百家乐) 6-闲(百家乐)
    @SerializedName("bet_amount")
    public String totalBets;//下注
    @SerializedName("profit")
    public String dividend;//派彩
    @SerializedName("banker_cards")
    public String bankCard;//莊家
    @SerializedName("player_cards")
    public String playCard;//閑家
     @SerializedName("game_result")
    public String gameResult;//牌局結果信息 全字符匹配
    @SerializedName("bet_record")
    public String betRecord;//下注类型 "small,big",
    @SerializedName("bet_record_amount")
    public String betRecordAmount;//对应下注金额

    public String replay;//视频重播
    public int isBacSelect = View.GONE;//是否显示百家乐牌面
    public int isDtSelect = View.GONE;//是否显示乐虎牌面

    public String transactionNo;//交易单号
    public String operateTime;//操作时间(美东)
    public String recordType;//类别
    public String income;//收入
    public String outcome;//支出
    public String afterTransaction;//交易后额度
}
