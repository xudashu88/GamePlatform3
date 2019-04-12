package com.purity.yu.gameplatform.controler;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.SinglePickLiveActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Liquidation;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.widget.PercentCircleAntiClockwise;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * 功能：单挑房间
 * 描述：
 * Create by purity on 2018/11/24.
 */
public class SinglePickLiveSocketController {
    private Context mContext;
    private static SinglePickLiveSocketController instance = null;
    private Socket mSocket;
    private Timer timer;

    String roomId;
    List<Integer> boardMessageList = new ArrayList<>();
    private List<String> cardList = new ArrayList<>();
    PercentCircleAntiClockwise pcac;

    public void init(Context context) {
        mContext = context;
        timer = new Timer(true);
        try {
            mSocket = IO.socket(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.SOCKET_ROOM));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static SinglePickLiveSocketController getInstance() {
        if (null == instance) {
            instance = new SinglePickLiveSocketController();
        }
        return instance;
    }

    public void copyWidget(List<Integer> boardMessageList, PercentCircleAntiClockwise pcac, String roomId) {
        this.boardMessageList = boardMessageList;
        this.pcac = pcac;
        this.roomId = roomId;
    }

    public void connectSocket() {
        if (mSocket != null) {
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on("command", OnCommand);
            mSocket.connect();
            if (!mSocket.connected()) {
//                perOnePerformance();
            } else {
                if (timer != null) {
                    timer.cancel();
                }
            }
        }
    }

    private void perOnePerformance() {
        TimerTask task = new TimerTask() {
            public void run() {
                connectSocket();
            }
        };
        timer.schedule(task, 1000, 2000);
    }

    public Socket getSocket() {
        if (mSocket != null && mSocket.connected()) {
            return mSocket;
        }
        return null;
    }

    public void disconnectSocket() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off(Socket.EVENT_CONNECT, onConnect);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off("command", OnCommand);
        }
    }

    private Emitter.Listener OnCommand = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((SinglePickLiveActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("socket_command=", args[0].toString());
                    JSONObject json  ;
                    try {
                        json = new JSONObject(args[0].toString());
                        String command = json.optString("command");
                        switch (command) {
                            case "betscore"://当前局每个位置下注成功的分数
                                try {
                                    //防止字段错误或者结构变化
                                    String data = json.optString("data");//{"score":[0,0,0],"betPeople":[[],[],[]],"bets":[0,0,0]}
                                    JSONObject _data = new JSONObject(data);
                                    String betScore = _data.optString("bets");
                                    int[] betscoreInt = new Gson().fromJson(betScore, int[].class);
                                    List<Integer> betscoreList = new ArrayList<>();
                                    for (int i = 0; i < betscoreInt.length; i++) {
                                        betscoreList.add(betscoreInt[i]);
                                    }
                                    postEventBetScore(betscoreList);
                                    break;
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                }
                            case "points":
                                try {
                                    String upperScore = json.optString("score");
                                    String money = json.optString("money");
                                    String type = json.optString("type");//0上分 1下分
                                    postEventUpperMoney(upperScore, money, type);
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            case "dobet"://下注成功
                                try {
                                    int playScore = SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_PLAY);
                                    int bankScore = SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_BANK);
                                    int tieScore = SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_TIE);
                                    int playPairScore = SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_PLAY_PAIR);
                                    int bankPairScore = SharedPreUtil.getInstance(mContext).getInt(Constant.CHIP_BANK_PAIR);
                                    String availableAmount = json.optString("availableAmount");
                                    //发post  减少积分
                                    postEventChip(playScore, bankScore, tieScore, playPairScore, bankPairScore, availableAmount);
                                    StringBuffer sb = new StringBuffer();
                                    String _bet_successful = mContext.getResources().getString(R.string.bet_successful);
                                    if (bankScore != 0) {
                                        String _bank = mContext.getResources().getString(R.string.spade);
                                        sb.append(_bank + ":" + bankScore + " " + _bet_successful + "\n");
                                    }
                                    if (playScore != 0) {
                                        String _play = mContext.getResources().getString(R.string.hearts);
                                        sb.append(_play + ":" + playScore + "" + _bet_successful + "\n");
                                    }
                                    if (tieScore != 0) {
                                        String _tie = mContext.getResources().getString(R.string.clubs);
                                        sb.append(_tie + ":" + tieScore + "" + _bet_successful + "\n");
                                    }
                                    if (playPairScore != 0) {
                                        String _playPair = mContext.getResources().getString(R.string.king);
                                        sb.append(_playPair + ":" + playPairScore + "" + _bet_successful + "\n");
                                    }
                                    if (bankPairScore != 0) {
                                        String _bankPair = mContext.getResources().getString(R.string.diamonds);
                                        sb.append(_bankPair + ":" + bankPairScore + "" + _bet_successful + "\n");
                                    }
                                    ToastUtil.getInstance().showToast(mContext, sb.toString());
                                    break;
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }
                            case "nobet"://下注失败
                                ToastUtil.getInstance().showLong(mContext, mContext.getResources().getString(R.string.bet_fail));
                                break;
                            case "betlimit":
                                try {
                                    int addBet = json.optInt("addBet");
                                    postEventLimit(addBet);
                                    ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.bet_over_limit));
                                    break;
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }
                            case "switch_result"://发牌
                                try {
                                    String card = json.getString("card");
                                    String pos = json.getString("pos");//1闲的第一张 2庄的第一张 3闲的第二张 4庄的第二张 5闲的第三张 6庄的第三张
                                    int bankCardValue = json.getInt("dragonscore");
                                    int playCardValue = json.getInt("tigercore");
                                    postEventLSwitchResult(card, pos, bankCardValue, playCardValue);
                                    break;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            case "state"://倒计时
                                try {
                                    String state = json.optString("state");//BET:下注阶段 RESULT:开牌阶段 OVER:清算阶段 WAITTING:同步识别端
                                    int second = json.optInt("second");//服务器是15秒倒计时
                                    postEventState(state, second);
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            case "result"://最终结果加入珠路0
                                try {
                                    int resultWin = json.optInt("win");
                                    String resultCard = json.optString("card");
                                    int round = json.optInt("round");
                                    int maxScore = json.optInt("maxScore");//待用

                                    //测试 start
//                                int _random = new MathRandomUtil().PercentageRandom();
//                                LogUtil.i("最终结果加入珠路 _command=" + _random);
//                                boardMessageList.add(_random);
                                    //测试 end
                                    postEventResult(resultWin, resultCard, round);
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            case "liquidation"://清算
                                try {
                                    String username = json.optString("username");
                                    String userId = json.optString("userid");
                                    int ratio = json.optInt("ratio");
                                    String score = json.optString("score");
                                    String balance = json.optString("balance");
                                    boolean win = json.optBoolean("win");
                                    postEventLiquidation(new Liquidation(userId, username, ratio, win, balance, score));
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            case "betrecord":// 下注积分数/人数
                                try {
                                    String _score = json.optString("score");
                                    String _betPeople = json.optString("betPeople");
                                    int[] _scoreInt = new Gson().fromJson(_score, int[].class);
                                    int[] _betPeopleInt = new Gson().fromJson(_betPeople, int[].class);
                                    List<Integer> _scoreList = new ArrayList<>();
                                    List<Integer> _betPeopleList = new ArrayList<>();
                                    for (int i = 0; i < _scoreInt.length; i++) {
                                        _scoreList.add(_scoreInt[i]);
                                        _betPeopleList.add(_betPeopleInt[i]);
                                    }
                                    postEventBetRecord(_scoreList, _betPeopleList);
                                    break;
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                }
                            case "fillcard"://补牌 01庄补 02闲补
                                try {
                                    String fill = json.optString("pos");
                                    postEventFillCard(fill);
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            case "joinroom"://加入房间后 ，只调用一次
                                try {
                                    String _betLimit = json.optString("betlimit");
                                    String _maxBet = json.optString("maxbet");
                                    String _minBet = json.optString("minbet");
                                    String[] _betLimitStr = new Gson().fromJson(_betLimit, String[].class);
                                    String[] _maxBetStr = new Gson().fromJson(_maxBet, String[].class);
                                    String[] _minBetStr = new Gson().fromJson(_minBet, String[].class);
                                    String player = json.optString("player");
                                    JSONObject _player = new JSONObject(player);
                                    String _goldcoins = _player.optString("availableAmount");
                                    String roomid = _player.optString("roomid");
                                    String roomName = json.optString("name");
                                    String stateHis = json.optString("state");
                                    int betSecond = json.optInt("betSecond");
                                    String roomConfig = json.optString("roomConfig");
                                    JSONObject _roomConfig = new JSONObject(roomConfig);
                                    int isPeopleNum = _roomConfig.optInt("peopleNum");//1 显示人数 0不显示人数
                                    int betsRemind = _roomConfig.optInt("BetsRemind");//1 提醒下注 0不提醒下注
                                    boardMessageList.clear();
                                    cardList.clear();
                                    JSONArray boardMessage = json.getJSONArray("boardMessages");
                                    for (int j = 0; j < boardMessage.length(); j++) {
                                        JSONObject _boardMessage = boardMessage.getJSONObject(j);
                                        int _win = _boardMessage.optInt("win");
                                        String cardVal = _boardMessage.optString("card");
                                        if (_win != -1) {
                                            boardMessageList.add(_win);
                                            if (TextUtils.isEmpty(cardVal)) {
                                                cardList.add("00");
                                            } else
                                                cardList.add(cardVal);
                                        }

                                    }
                                    //黑 红 梅 方 王
                                    //测试用例 黑 红 梅 方 王
                                    postEventJoinRoom(boardMessageList, _goldcoins, roomid, roomName, _betLimitStr, _maxBetStr, _minBetStr, stateHis, cardList, betSecond, isPeopleNum,betsRemind);
                                    if (stateHis.equals(Constant.BACCARAT_BET)) {
                                        int _second = json.optInt("second");
                                        pcac.setCurrentPercent(_second);
                                    } else if (stateHis.equals(Constant.BACCARAT_WAIT) || stateHis.equals(Constant.BACCARAT_OVER)) {
                                        pcac.setCurrentPercent(0);
                                    } else if (stateHis.equals(Constant.BACCARAT_INNINGS_END)) {
                                        pcac.setCurrentPercent(-1);
                                    } else if (stateHis.equals(Constant.BACCARAT_CREATED)) {
                                        boardMessageList.clear();
                                        postEventJoinRoom(boardMessageList, _goldcoins, roomid, roomName, _betLimitStr, _maxBetStr, _minBetStr, stateHis, cardList, betSecond, isPeopleNum,betsRemind);
                                    } else if (stateHis.equals(Constant.BACCARAT_RESULT)) {
                                        pcac.setCurrentPercent(0);
                                    }
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private void postEventFillCard(String fill) {
        ObjectEvent.FillCardEvent event = new ObjectEvent.FillCardEvent();
        event.fill = fill;
        EventBus.getDefault().post(event);
    }

    private void postEventJoinRoom(List<Integer> boardMessageList, String _goldCoins, String roomId, String roomName, String[] betLimitList, String[] maxBetList, String[] minBetList,
                                   String state, List<String> cardList, int betSecond, int isPeopleNum,int betsRemind) {
        ObjectEvent.BoardMessageEvent event = new ObjectEvent.BoardMessageEvent();
        event.boardMessageList = boardMessageList;
        event.betLimitList = betLimitList;
        event.maxBetList = maxBetList;
        event.minBetList = minBetList;
        event.goldCoins = _goldCoins;
        event.roomId = roomId;
        event.roomName = roomName;
        event.state = state;
        event.cardList = cardList;
        event.betSecond = betSecond;
        event.isPeopleNum = isPeopleNum;
        event.betsRemind = betsRemind;
        EventBus.getDefault().post(event);
    }

    private void postEventBetRecord(List<Integer> scoreList, List<Integer> betPeopleList) {
        ObjectEvent.BetRecordEvent event = new ObjectEvent.BetRecordEvent();
        event.scoreList = scoreList;
        event.betPeopleList = betPeopleList;
        EventBus.getDefault().post(event);
    }

    private void postEventBetScore(List<Integer> scoreList) {
        ObjectEvent.betScoreEvent event = new ObjectEvent.betScoreEvent();
        event.scoreList = scoreList;
        EventBus.getDefault().post(event);
    }

    private void postEventLiquidation(Liquidation liquidation) {
        EventBus.getDefault().post(liquidation);
    }

    private void postEventLSwitchResult(String card, String pos, int bankCardValue, int playCardValue) {
        ObjectEvent.SwitchResultEvent event = new ObjectEvent.SwitchResultEvent();
        event.card = card;
        event.pos = pos;
        event.bankCardValue = bankCardValue;
        event.playCardValue = playCardValue;
        EventBus.getDefault().post(event);
    }

    private void postEventResult(int win, String card, int round) {
        ObjectEvent.ResultEvent event = new ObjectEvent.ResultEvent();
        event.win = win;
        event.card = card;
        event.round = round;
        EventBus.getDefault().post(event);
    }

    private void postEventState(String state, int second) {
        ObjectEvent.StateEvent event = new ObjectEvent.StateEvent();
        event.state = state;
        event.second = second;
        EventBus.getDefault().post(event);
    }

    private void postEventChip(int playScore, int bankScore, int tieScore, int playPairScore, int bankPairScore, String availableAmount) {
        ObjectEvent.ChipEvent event = new ObjectEvent.ChipEvent();
        event.playScore = playScore;
        event.bankScore = bankScore;
        event.tieScore = tieScore;
        event.playPairScore = playPairScore;
        event.bankPairScore = bankPairScore;
        event.availableAmount = availableAmount;
        EventBus.getDefault().post(event);
    }

    private void postEventLimit(int addBet) {
        ObjectEvent.LimitBetEvent event = new ObjectEvent.LimitBetEvent();
        event.limitBetScore = addBet;
        EventBus.getDefault().post(event);
    }

    private void postEventUpperMoney(String upperScore, String money, String type) {
        ObjectEvent.UpperMoneyEvent upperMoneyEvent = new ObjectEvent.UpperMoneyEvent();
        upperMoneyEvent.upperScore = upperScore;
        upperMoneyEvent.money = money;
        upperMoneyEvent.type = type;
        EventBus.getDefault().post(upperMoneyEvent);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            conn();
        }
    };

    private void conn() {
        if (TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN_SOCKET))) {
            ToastUtil.show(mContext, "未登录");
        } else {
            if (mSocket != null && mSocket.connected()) {//启动 http://localhost:8080/user/startroom
                String _token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN_SOCKET);
//                roomId = "10000000000000008";
                mSocket.emit("joinroom", "{'token':'" + _token + "','room':'" + roomId + "'}");
            }
        }
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, 0);
            ToastUtil.show(mContext, "已断开服务器");
            conn();
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            SharedPreUtil.getInstance(mContext).saveParam(Constant.IS_CHIP_SUCCESS, 0);
            conn();
        }
    };
}