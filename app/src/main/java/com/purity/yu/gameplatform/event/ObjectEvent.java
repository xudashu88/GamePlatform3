package com.purity.yu.gameplatform.event;

import com.purity.yu.gameplatform.entity.Hall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */

public class ObjectEvent {
    public static class BoardMessageEvent {
        public List<Integer> boardMessageList = new ArrayList<>();//0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
        public List<String> cardList = new ArrayList<>();
        public String goldCoins;
        public String roomId;
        public String roomName;
        public String[] betLimitList;
        public String[] maxBetList;
        public String[] minBetList;
        public String[] color;
        public String state;
        public int deskId;//历史记录了多少轮
        public int downtownTime;
        public int skyCard;
        public int betSecond;//总倒计时
        public int isPeopleNum;//1 显示人数 0不显示人数
        public int betsRemind;
        public int peoplenumber;//在线人数
        public String[] lanRtmpAddressArr;
        public String[] dxRtmpAddressArr;
        public String[] ydRtmpAddressArr;
        public List<Integer> maxScoreList;//只有8和9是天牌
    }

    public static class BetRecordEvent {
        public List<Integer> scoreList;//下注分数 0闲 1庄 2和 3闲对 4庄对
        public List<Integer> betPeopleList;//下注人数 0闲 1庄 2和 3闲对 4庄对
    }

    public static class betScoreEvent {
        public List<Integer> scoreList;//当前成功下注分数 0闲 1庄 2和 3闲对 4庄对
    }

    public static class ResultEvent {
        public List<String> bankCardList;
        public List<String> playCardList;
        public int win = -1;//0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
        public int round;
        public String card;
        public int resultMaxScore;
        public int skyCard;
    }

    public static class SwitchResultEvent {
        public String card;//不同花色用前半字节表示，后半字节表示具体的数，10，J,Q,K 分别对应16进制的A,B,C,D 例如 红桃A:21 黑桃J:1B
        public String pos;//这张牌的位置 1闲的第一张 2庄的第一张 3闲的第二张 4庄的第二张 5闲的第三张 6庄的第三张
        public int bankCardValue;
        public int playCardValue;
    }

    public static class StateEvent {
        public String state;//BET:下注阶段 RESULT:开牌阶段 OVER:清算阶段 WAITTING:同步识别端
        public int second;//服务器是15秒倒计时
    }

    public static class ChipEvent {
        public int playScore;
        public int bankScore;
        public int tieScore;
        public int playPairScore;
        public int bankPairScore;
        public int bigScore;
        public int smallScore;
        public String availableAmount;
        public int[] scoreArr;
    }

    public static class LimitBetEvent {
        public int limitBetScore;
    }

    public static class BaccaratLottyEvent {//百家乐列表
        public List<Hall> hallList;
//        public List<Integer> boardMessageList;//0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
    }

    public static class CreditsAmountEvent {
        public double creditsAmount;//信用额度
        public double creditsRate;//超出信用额度支付的手续费
    }

    public static class FillCardEvent {
        public String fill;//01 庄补牌  02闲补牌
    }

    public static class BureauEvent {
        public String bureau;
    }

    public static class MoneyChangeEvent {
        public String money;
    }

    public static class notifyItemChangedEvent {
        public int position;
    }

    public static class UpperMoneyEvent {
        public String money;
        public String upperScore;
        public String type;
    }

    public static class MarqueeTextEvent {
        public String message;
        public String money;
    }

    public static class VerifyStateEvent {
        public int type;//1 存款 2取款
        public int state;//0 审核取消 1审核中 2审核通过
    }
}
