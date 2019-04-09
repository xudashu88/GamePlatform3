package com.purity.yu.gameplatform.controler;

import android.content.Context;
import android.text.TextUtils;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.SinglePickListActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Hall;
import com.purity.yu.gameplatform.event.ObjectEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * 功能：单挑大厅socket
 * 描述：
 * Create by purity on 2018/11/24.
 */
public class SinglePickListSocketController {
    private Context mContext;
    private static SinglePickListSocketController instance = null;
    private Socket mSocket;
    //3.8 3.8 4.0.4.0 20 0-黑 1-红 2-梅 3-方 4-王 win=0-4


    {
        try {
            mSocket = IO.socket(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.SOCKET_LOBBY));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void init(Context context) {
        mContext = context;
    }

    public static SinglePickListSocketController getInstance() {
        if (null == instance) {
            instance = new SinglePickListSocketController();
        }
        return instance;
    }

    public void connectSocket() {
        if (mSocket != null) {
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on("hall", OnCommand);
            mSocket.connect();
        }
    }

    public void disconnectSocket() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off(Socket.EVENT_CONNECT, onConnect);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off("hall", OnCommand);
        }
    }

    private Emitter.Listener OnCommand = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ((SinglePickListActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.i("进入大厅6 ok啦");
                    LogUtil.i("socket_command=" + args[0].toString());
                    JSONObject json;
                    String roomId = "";
                    String state = "";
                    int second = 0;
                    int people_number = 0;
                    String _borderMessage = "";
                    try {
                        String str = args[0].toString();
                        json = new JSONObject(str);
                        List<Hall> hallList = new ArrayList<>();
                        hallList.clear();
                        JSONArray data_array = json.getJSONArray("record");
                        for (int i = 0; i < data_array.length(); i++) {
                            JSONObject _json = data_array.getJSONObject(i);
                            roomId = _json.getString("gameroom");//房间id
                            state = _json.getString("state");//BET:下注阶段 RESULT:开牌阶段 OVER:清算阶段 WAITTING:同步识别端
                            second = _json.optInt("second");//20s
                            people_number = _json.optInt("peoplenumber");//当前房间人数
                            String roomConfig = _json.optString("roomConfig");
                            JSONObject _roomConfig = new JSONObject(roomConfig);
                            int isPeopleNum = _roomConfig.optInt("peopleNum");//1 显示人数 0不显示人数
                            JSONArray boardMessage = _json.getJSONArray("boardMessage");
                            List<Integer> boardMessageList = new ArrayList<>();//原始数据
                            List<String> cardList = new ArrayList<>();//原始数据
                            for (int j = 0; j < boardMessage.length(); j++) {
                                JSONObject _boardMessage = boardMessage.getJSONObject(j);
                                int win = _boardMessage.optInt("win");//3.8 3.8 4.0.4.0 20 0-黑 1-红 2-梅 3-方 4-王
                                String card = _boardMessage.optString("card");
                                if (win != -1) {
                                    boardMessageList.add(win);
                                    if (TextUtils.isEmpty(card)) {
                                        cardList.add("00");
                                    } else
                                        cardList.add(card);
                                }
                            }
                            //测试用例 黑 红 梅 方 王
                            LogUtil.i("isPeopleNum=单挑-" + isPeopleNum + " people_number=" + people_number);
                            Hall hall = new Hall(roomId, state, second, people_number, boardMessageList, cardList);
                            hallList.add(hall);
                        }
                        postEventBaccaratLotty(hallList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private void postEventBaccaratLotty(List<Hall> hallList) {
        ObjectEvent.BaccaratLottyEvent event = new ObjectEvent.BaccaratLottyEvent();
        event.hallList = hallList;
        EventBus.getDefault().post(event);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            conn();
        }
    };

    private void conn() {
        if (TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN))) {
            ToastUtil.show(mContext, "未登录");
        } else {
            if (mSocket != null && mSocket.connected()) {//启动 http://localhost:8080/user/startroom
                mSocket.emit("joingame", "{'game':'duel'}");//进入单挑大厅
                LogUtil.i("进入大厅3_2onConnect");
            }
        }
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ToastUtil.show(mContext, "已断开服务器");
            LogUtil.i("进入大厅3_4onDisconnect");
            conn();
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ToastUtil.show(mContext, mContext.getResources().getString(R.string.disconnect));
            LogUtil.i("进入大厅3_3onConnectError");
            conn();
        }
    };
}