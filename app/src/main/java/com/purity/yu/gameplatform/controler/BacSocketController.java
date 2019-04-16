package com.purity.yu.gameplatform.controler;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.BacActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Liquidation;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.BaccaratUtil;

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
 * 功能：百家乐房间
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class BacSocketController {
    private Context mContext;
    private static BacSocketController instance = null;
    private Socket mSocket;
    private Socket mSocketProdict;
    private Timer timer;

    String roomId;
    String token;
    List<Integer> boardMessageList = new ArrayList<>();
    TextView tv_time;
    RelativeLayout ll_poker;
    ImageView iv_jet_banker_01;
    ImageView iv_jet_banker_02;
    ImageView iv_jet_banker_03;
    TextView tv_jet_banker_value;
    ImageView iv_jet_player_01;
    ImageView iv_jet_player_02;
    ImageView iv_jet_player_03;
    TextView tv_jet_player_value;
    RelativeLayout rl_player_pair;
    RelativeLayout rl_banker_pair;
    RelativeLayout rl_player;
    RelativeLayout rl_tie;
    RelativeLayout rl_banker;
    TextView tv_online_count;
    RelativeLayout rl_show;
    TextView tv_show;
    List<String> _calPlayer = new ArrayList<>();
    List<String> _calBanker = new ArrayList<>();

    public void init(Context context) {
        mContext = context;
        timer = new Timer(true);
        try {
            mSocket = IO.socket(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.SOCKET_ROOM));
            mSocketProdict = IO.socket(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.SOCKET_PRODICT));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static BacSocketController getInstance() {
        if (null == instance) {
            instance = new BacSocketController();
        }
        return instance;
    }

    public void copyWidget(List<Integer> boardMessageList, TextView tv_time, RelativeLayout ll_poker, ImageView iv_jet_banker_01, ImageView iv_jet_banker_02, ImageView iv_jet_banker_03,
                           TextView tv_jet_banker_value, ImageView iv_jet_player_01, ImageView iv_jet_player_02, ImageView iv_jet_player_03, TextView tv_jet_player_value, String roomId,
                           RelativeLayout rl_player_pair, RelativeLayout rl_banker_pair, RelativeLayout rl_player, RelativeLayout rl_tie, RelativeLayout rl_banker,
                           TextView tv_online_count, RelativeLayout rl_show, TextView tv_show) {
        this.boardMessageList = boardMessageList;
        this.tv_time = tv_time;
        this.ll_poker = ll_poker;
        this.iv_jet_banker_01 = iv_jet_banker_01;
        this.iv_jet_banker_02 = iv_jet_banker_02;
        this.iv_jet_banker_03 = iv_jet_banker_03;
        this.iv_jet_player_01 = iv_jet_player_01;
        this.iv_jet_player_02 = iv_jet_player_02;
        this.iv_jet_player_03 = iv_jet_player_03;
        this.tv_jet_banker_value = tv_jet_banker_value;
        this.tv_jet_player_value = tv_jet_player_value;
        this.roomId = roomId;
        this.rl_player_pair = rl_player_pair;
        this.rl_banker_pair = rl_banker_pair;
        this.rl_player = rl_player;
        this.rl_tie = rl_tie;
        this.rl_banker = rl_banker;
        this.tv_online_count = tv_online_count;
        this.rl_show = rl_show;
        this.tv_show = tv_show;
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
//                perOnePerformance();// 有多个socket连接
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

    public void connectSocketProdict() {
        if (mSocketProdict != null) {
            mSocketProdict.on(Socket.EVENT_CONNECT, onConnectProdict);
            mSocketProdict.on(Socket.EVENT_DISCONNECT, onDisconnectProdict);
            mSocketProdict.on(Socket.EVENT_CONNECT_ERROR, onConnectErrorProdict);
            mSocketProdict.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectErrorProdict);
            mSocketProdict.connect();
        }
    }

    public Socket getSocket() {
        if (mSocket != null && mSocket.connected()) {
            return mSocket;
        }
        return null;
    }

    public Socket getSocketProdict() {
        if (mSocketProdict != null && mSocketProdict.connected()) {
            return mSocketProdict;
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
            ((BacActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.i("socket_command=" + args[0].toString());
                    JSONObject json = null;
                    try {
                        //测试用例
                        json = new JSONObject(args[0].toString());
//                        json = new JSONObject(_comment);
                        String command = json.optString("command");

                        switch (command) {
                            case "people_number":
                                try {
                                    int peopleNumber = json.optInt("data");
                                    tv_online_count.setText(String.valueOf(peopleNumber));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "SynchronizeResult":
                                try {
                                    JSONArray _borderMessage = json.getJSONArray("boardMessages");
                                    List<Integer> boardMessageList = new ArrayList<>();
                                    List<Integer> maxScoreList = new ArrayList<>();
                                    for (int j = 0; j < _borderMessage.length(); j++) {
                                        JSONObject __borderMessage = _borderMessage.getJSONObject(j);
                                        String result = __borderMessage.optString("win");
                                        int _maxScore = __borderMessage.optInt("maxScore");
                                        int _win = Integer.parseInt(result);
                                        if (_win != -1) {
                                            boardMessageList.add(_win);
                                        }
                                        maxScoreList.add(_maxScore);
                                    }
                                    postEventSyn(boardMessageList, maxScoreList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "betscore"://当前局每个位置下注成功的分数
                                LogUtil.i("当前局下注分数 socket_command=" + json);
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
                                LogUtil.i("下注成功 socket_command=" + json);
                                try {
                                    int playScore = 0, bankScore = 0, tieScore = 0, playPairScore = 0, bankPairScore = 0, bigScore = 0, smallScore = 0;
                                    String availableAmount = json.optString("availableAmount");
                                    String bets = json.optString("bets");//闲 庄 和 闲对 庄对 小 大
                                    String scores = json.optString("score");//闲 庄 和 闲对 庄对 小 大
                                    int[] scoreArr;
                                    LogUtil.i("当前下注=-0" + bets + " score=" + scores);
                                    if (!TextUtils.isEmpty(bets)) {
                                        int[] betsArr = new Gson().fromJson(bets, int[].class);
                                        if (!TextUtils.isEmpty(scores)) {
                                            scoreArr = new Gson().fromJson(scores, int[].class);
                                        } else {
                                            scoreArr = new int[0];
                                        }
                                        playScore = betsArr[0];
                                        bankScore = betsArr[1];
                                        tieScore = betsArr[2];
                                        playPairScore = betsArr[3];
                                        bankPairScore = betsArr[4];
                                        smallScore = betsArr[5];
                                        bigScore = betsArr[6];
                                        postEventChip(playScore, bankScore, tieScore, playPairScore, bankPairScore, bigScore, smallScore, availableAmount, scoreArr);
//                                        mSocket.emit("dobet", "{'bets':[" + chipScore.get(1) + "," + chipScore.get(0) + "," + chipScore.get(2) +
//                                                "," + chipScore.get(4) + "," + chipScore.get(3) + "," + chipScore.get(5) + "," + chipScore.get(6) + "],'freeCommission':'" + free + "'}");
                                        mSocketProdict.emit("prodict_score", "{'room':" + roomId + ",'player':" + token +
                                                ",'score':[" + playScore + "," + bankScore + "," + tieScore + "," + playPairScore + "," + bankPairScore + ",0,0],'clean':1}");
                                    }
                                    StringBuffer sb = new StringBuffer();
                                    String _bet_successful = mContext.getResources().getString(R.string.bet_successful);
                                    if (bankScore != 0) {
                                        String _bank = mContext.getResources().getString(R.string.banker);
                                        sb.append(_bank + ":" + bankScore + " " + _bet_successful + "\n");
                                    }
                                    if (playScore != 0) {
                                        String _play = mContext.getResources().getString(R.string.player);
                                        sb.append(_play + ":" + playScore + "" + _bet_successful + "\n");
                                    }
                                    if (tieScore != 0) {
                                        String _tie = mContext.getResources().getString(R.string.tie);
                                        sb.append(_tie + ":" + tieScore + "" + _bet_successful + "\n");
                                    }
                                    if (playPairScore != 0) {
                                        String _playPair = mContext.getResources().getString(R.string.play_type_player_pair);
                                        sb.append(_playPair + ":" + playPairScore + "" + _bet_successful + "\n");
                                    }
                                    if (bankPairScore != 0) {
                                        String _bankPair = mContext.getResources().getString(R.string.play_type_banker_pair);
                                        sb.append(_bankPair + ":" + bankPairScore + "" + _bet_successful + "\n");
                                    }
                                    BaccaratUtil.getInstance().toast(rl_show, tv_show, sb.toString(), 2000);
                                    break;
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }
                            case "nobet"://下注失败
                                ToastUtil.getInstance().showLong(mContext, mContext.getResources().getString(R.string.bet_fail));
                                break;
                            case "betlimit":
                                LogUtil.i("betlimit socket_command=" + json);
                                try {
                                    int addBet = json.optInt("addBet");
                                    postEventLimit(addBet);
                                    BaccaratUtil.getInstance().toast(rl_show, tv_show, mContext.getResources().getString(R.string.bet_over_limit));
                                    break;
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }
                            case "switch_result"://发牌
                                LogUtil.i("switch_result socket_command=" + json);
                                try {
                                    String card = json.getString("card");
                                    String pos = json.getString("pos");//1闲的第一张 2庄的第一张 3闲的第二张 4庄的第二张 5闲的第三张 6庄的第三张
                                    int bankCardValue = json.optInt("bankscore");
                                    int playCardValue = json.optInt("playscore");
                                    int maxScore = json.optInt("maxScore");
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
                                LogUtil.i("result socket_command=" + json);
                                try {
                                    String _bankCard = json.optString("bankCard");
                                    String _playCard = json.optString("playCard");
                                    List<String> bankCardList = BaccaratUtil.str2List2(_bankCard);
                                    List<String> playCardList = BaccaratUtil.str2List2(_playCard);
                                    int resultWin = json.optInt("win");
                                    int round = json.optInt("round");
                                    int resultSkyCard = json.optInt("skyCard");
                                    int resultMaxScore = json.optInt("maxScore");
                                    //测试 start
//                                int _random = new MathRandomUtil().PercentageRandom();
//                                LogUtil.i("最终结果加入珠路 _command=" + _random);
//                                boardMessageList.add(_random);
                                    //测试 end
                                    postEventResult(bankCardList, playCardList, resultWin, round, resultMaxScore, resultSkyCard);
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            case "liquidation"://清算
                                LogUtil.i("清算 socket_command=" + json);
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
                                    String _bankCardHis = json.optString("bankCard");
                                    String _playCardHis = json.optString("playCard");
                                    String stateHis = json.optString("state");
                                    int skyCard = json.optInt("skyCard");
                                    int betSecond = json.optInt("betSecond");
                                    int downtownTime = json.optInt("second");
                                    int peoplenumber = json.optInt("peopleNumber");
                                    String roomConfig = json.optString("roomConfig");
                                    JSONObject _roomConfig = new JSONObject(roomConfig);
                                    int isPeopleNum = _roomConfig.optInt("peopleNum");//1 显示人数 0不显示人数
                                    int betsRemind = _roomConfig.optInt("BetsRemind");//1 提醒下注 0不提醒下注
                                    JSONArray _borderMessage = json.getJSONArray("boardMessages");
                                    List<Integer> boardMessageList = new ArrayList<>();
                                    List<Integer> maxScoreList = new ArrayList<>();
                                    for (int j = 0; j < _borderMessage.length(); j++) {
                                        JSONObject __borderMessage = _borderMessage.getJSONObject(j);
                                        String result = __borderMessage.optString("win");
                                        int _maxScore = __borderMessage.optInt("maxScore");
                                        int _win = Integer.parseInt(result);
                                        if (_win != -1) {
                                            boardMessageList.add(_win);
                                        }
                                        maxScoreList.add(_maxScore);
                                    }

//                                boardMessageList = BaccaratUtil.str2ListReInt(_borderMessage);
                                    //测试用例
//                                boardMessageList = BaccaratUtil.getInstance().initBoardMessage(72, boardMessageList);
//                                maxScoreList = BaccaratUtil.getInstance().initMaxScore(56, maxScoreList);
                                    postEventJoinRoom(boardMessageList, maxScoreList, _goldcoins, roomid, roomName, _betLimitStr, _maxBetStr, _minBetStr, stateHis, downtownTime, skyCard, isPeopleNum, betSecond, betsRemind, peoplenumber);

                                    if (stateHis.equals(Constant.BACCARAT_RESULT)) {
                                        onClick(false);
                                        tv_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.unit13));
                                        tv_time.setText("发牌中");
                                        _calBanker.clear();
                                        _calPlayer.clear();
                                        List<String> bankCardListHis = new ArrayList<>();
                                        bankCardListHis.clear();
                                        bankCardListHis.addAll(BaccaratUtil.str2List2(_bankCardHis));
                                        List<String> playCardListHis = new ArrayList<>();
                                        playCardListHis.clear();
                                        playCardListHis.addAll(BaccaratUtil.str2List2(_playCardHis));
                                        ll_poker.setVisibility(View.VISIBLE);
                                        if (bankCardListHis.size() == 3) {
                                            iv_jet_banker_01.setBackgroundResource(BaccaratUtil.switchCard(bankCardListHis.get(0)));
                                            iv_jet_banker_02.setBackgroundResource(BaccaratUtil.switchCard(bankCardListHis.get(1)));
                                            iv_jet_banker_03.setBackgroundResource(BaccaratUtil.switchCard(bankCardListHis.get(2)));
                                            _calBanker.add(bankCardListHis.get(0));
                                            _calBanker.add(bankCardListHis.get(1));
                                            _calBanker.add(bankCardListHis.get(2));
                                            tv_jet_banker_value.setText(String.valueOf(BaccaratUtil.getBacCardScore(_calBanker)));
                                        } else if (bankCardListHis.size() == 2) {
                                            iv_jet_banker_01.setBackgroundResource(BaccaratUtil.switchCard(bankCardListHis.get(0)));
                                            iv_jet_banker_02.setBackgroundResource(BaccaratUtil.switchCard(bankCardListHis.get(1)));
                                            _calBanker.add(bankCardListHis.get(0));
                                            _calBanker.add(bankCardListHis.get(1));
                                            tv_jet_banker_value.setText(String.valueOf(BaccaratUtil.getBacCardScore(_calBanker)));
                                        } else if (bankCardListHis.size() == 1) {
                                            iv_jet_banker_01.setBackgroundResource(BaccaratUtil.switchCard(bankCardListHis.get(0)));
                                            _calBanker.add(bankCardListHis.get(0));
                                            tv_jet_banker_value.setText(String.valueOf(BaccaratUtil.getBacCardScore(_calBanker)));
                                        }
                                        if (playCardListHis.size() == 3) {
                                            iv_jet_player_01.setBackgroundResource(BaccaratUtil.switchCard(playCardListHis.get(0)));
                                            iv_jet_player_02.setBackgroundResource(BaccaratUtil.switchCard(playCardListHis.get(1)));
                                            iv_jet_player_03.setBackgroundResource(BaccaratUtil.switchCard(playCardListHis.get(2)));
                                            _calPlayer.add(playCardListHis.get(0));
                                            _calPlayer.add(playCardListHis.get(1));
                                            _calPlayer.add(playCardListHis.get(2));
                                            tv_jet_player_value.setText(String.valueOf(BaccaratUtil.getBacCardScore(_calPlayer)));
                                        } else if (playCardListHis.size() == 2) {
                                            iv_jet_player_01.setBackgroundResource(BaccaratUtil.switchCard(playCardListHis.get(0)));
                                            iv_jet_player_02.setBackgroundResource(BaccaratUtil.switchCard(playCardListHis.get(1)));
                                            _calPlayer.add(playCardListHis.get(0));
                                            _calPlayer.add(playCardListHis.get(1));
                                            tv_jet_player_value.setText(String.valueOf(BaccaratUtil.getBacCardScore(_calPlayer)));
                                        } else if (playCardListHis.size() == 1) {
                                            iv_jet_player_01.setBackgroundResource(BaccaratUtil.switchCard(playCardListHis.get(0)));
                                            _calPlayer.add(playCardListHis.get(0));
                                            tv_jet_player_value.setText(String.valueOf(BaccaratUtil.getBacCardScore(_calPlayer)));
                                        }
                                    } else if (stateHis.equals(Constant.BACCARAT_BET)) {
                                        int _second = json.optInt("second");
                                        tv_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(com.gangbeng.basemodule.R.dimen.unit18));
                                        tv_time.setText(String.valueOf(_second));
                                    } else if (stateHis.equals(Constant.BACCARAT_WAIT) || stateHis.equals(Constant.BACCARAT_OVER)) {
                                        tv_time.setText("请等待");
                                    } else if (stateHis.equals(Constant.BACCARAT_INNINGS_END)) {
                                        tv_time.setText(mContext.getResources().getString(R.string.shuffling));
                                    } else if (stateHis.equals(Constant.BACCARAT_CREATED)) {
                                        boardMessageList.clear();
                                        postEventJoinRoom(boardMessageList, maxScoreList, _goldcoins, roomid, roomName, _betLimitStr, _maxBetStr, _minBetStr, stateHis, downtownTime, skyCard, isPeopleNum, betSecond, betsRemind, peoplenumber);
                                    }
                                    break;
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    /**
     * 下注
     *
     * @param clickable true 可下注
     */
    private void onClick(boolean clickable) {
        rl_player_pair.setClickable(clickable);
        rl_banker_pair.setClickable(clickable);
        rl_player.setClickable(clickable);
        rl_tie.setClickable(clickable);
        rl_banker.setClickable(clickable);
    }

    private void postEventFillCard(String fill) {
        ObjectEvent.FillCardEvent event = new ObjectEvent.FillCardEvent();
        event.fill = fill;
        EventBus.getDefault().post(event);
    }

    private void postEventSyn(List<Integer> boardMessageList, List<Integer> maxScoreList) {
        ObjectEvent.BoardMessageEvent event = new ObjectEvent.BoardMessageEvent();
        event.boardMessageList = boardMessageList;
        event.maxScoreList = maxScoreList;
        EventBus.getDefault().post(event);
    }

    private void postEventJoinRoom(List<Integer> boardMessageList, List<Integer> maxScoreList, String _goldCoins, String roomId, String roomName,
                                   String[] betLimitList, String[] maxBetList, String[] minBetList,
                                   String state, int downtownTime, int skyCard, int isPeopleNum, int betSecond, int betsRemind, int peoplenumber) {
        ObjectEvent.BoardMessageEvent event = new ObjectEvent.BoardMessageEvent();
        event.boardMessageList = boardMessageList;
        event.maxScoreList = maxScoreList;
        event.betLimitList = betLimitList;
        event.maxBetList = maxBetList;
        event.minBetList = minBetList;
        event.goldCoins = _goldCoins;
        event.roomId = roomId;
        event.roomName = roomName;
        event.state = state;
        event.downtownTime = downtownTime;
        event.skyCard = skyCard;
        event.isPeopleNum = isPeopleNum;
        event.betSecond = betSecond;
        event.betsRemind = betsRemind;
        event.peoplenumber = peoplenumber;
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

    private void postEventResult(List<String> bankCardList, List<String> playCardList, int win, int round, int resultMaxScore, int skyCard) {
        ObjectEvent.ResultEvent event = new ObjectEvent.ResultEvent();
        event.bankCardList = bankCardList;
        event.playCardList = playCardList;
        event.win = win;
        event.round = round;
        event.resultMaxScore = resultMaxScore;
        event.skyCard = skyCard;
        EventBus.getDefault().post(event);
    }

    private void postEventState(String state, int second) {
        ObjectEvent.StateEvent event = new ObjectEvent.StateEvent();
        event.state = state;
        event.second = second;
        EventBus.getDefault().post(event);
    }

    private void postEventChip(int playScore, int bankScore, int tieScore, int playPairScore, int bankPairScore, int bigScore, int smallScore, String availableAmount, int[] scoreArr) {
        ObjectEvent.ChipEvent event = new ObjectEvent.ChipEvent();
        event.playScore = playScore;
        event.bankScore = bankScore;
        event.tieScore = tieScore;
        event.playPairScore = playPairScore;
        event.bankPairScore = bankPairScore;
        event.bigScore = bigScore;
        event.smallScore = smallScore;
        event.availableAmount = availableAmount;
        event.scoreArr = scoreArr;
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

    //todo http://127.0.0.1:8888/admin/backend/points?score=1000000&&type=0&&userid=530051197705388032 上分
    private void conn() {
        if (TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN_SOCKET))) {
            ToastUtil.show(mContext, "未登录");
        } else {
            token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN_SOCKET);
            if (mSocket != null && mSocket.connected()) {//启动 http://localhost:8080/user/startroom
//                String _token = "534332329808625664";
//                roomId = "10000000000000001";
                mSocket.emit("joinroom", "{'token':'" + token + "','room':'" + roomId + "'}");
            }
        }
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            conn();
        }
    };

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
            ToastUtil.show(mContext, mContext.getResources().getString(R.string.disconnect));
            conn();
        }
    };

    private void connProdict() {
        if (mSocketProdict != null && mSocketProdict.connected()) {
            mSocketProdict.emit("joinroom", "{'monitor':'" + token + "','room':'" + roomId + "'}");
            LogUtil.i("mSocketProdict 0=" + mSocketProdict);
        }
    }

    private Emitter.Listener onConnectProdict = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            connProdict();
        }
    };

    private Emitter.Listener onDisconnectProdict = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            ToastUtil.show(mContext, "已断开服务器Pro");
            connProdict();
        }
    };

    private Emitter.Listener onConnectErrorProdict = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            ToastUtil.show(mContext, mContext.getResources().getString(R.string.disconnect));
            connProdict();
        }
    };
}