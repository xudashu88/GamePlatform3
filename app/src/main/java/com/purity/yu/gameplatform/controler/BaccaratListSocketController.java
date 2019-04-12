package com.purity.yu.gameplatform.controler;

import android.content.Context;
import android.text.TextUtils;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.google.gson.Gson;
import com.purity.yu.gameplatform.R;
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
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * 功能：百家乐大厅socket
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class BaccaratListSocketController {
    private Context mContext;
    private static BaccaratListSocketController instance = null;
    private Socket mSocket;
    private Timer timer;

    //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
//    {
//        try {
//            mSocket = IO.socket(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.SOCKET_LOBBY));
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void init(Context context) {
        mContext = context;
        timer = new Timer(true);
        try {
            mSocket = IO.socket(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.SOCKET_LOBBY));
            LogUtil.i("大厅-百家乐初始化" + mSocket);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static BaccaratListSocketController getInstance() {
        if (null == instance) {
            instance = new BaccaratListSocketController();
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
            if (!mSocket.connected()) {
                perOnePerformance();
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
            LogUtil.i("大厅-百家乐 socket_command=" + args[0].toString());
            JSONObject json;
            String roomId = "";
            String state = "";
            int second = 0;
            int people_number = 0;
            try {
                String str = args[0].toString();
                json = new JSONObject(str);
                List<Hall> hallList = new ArrayList<>();
                hallList.clear();
                JSONArray data_array = json.getJSONArray("record");
                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject _json = data_array.getJSONObject(i);
                    roomId = _json.getString("gameroom");//房间id
                    LogUtil.i("socket-roomId=" + roomId);
                    state = _json.getString("state");//BET:下注阶段 RESULT:开牌阶段 OVER:清算阶段 WAITTING:同步识别端
                    second = _json.optInt("second");//20s
                    people_number = _json.optInt("peoplenumber");//当前房间人数
                    String roomConfig = _json.optString("roomConfig");
                    JSONObject _roomConfig = new JSONObject(roomConfig);
                    String color = _roomConfig.optString("color");
                    String[] _colorStr = new Gson().fromJson(color, String[].class);
                    int isPeopleNum = _roomConfig.optInt("peopleNum");//1 显示人数 0不显示人数
                    JSONArray _borderMessage = _json.getJSONArray("boardMessage");
                    List<Integer> borderMessage = new ArrayList<>();
                    for (int j = 0; j < _borderMessage.length(); j++) {
                        JSONObject __borderMessage = _borderMessage.getJSONObject(j);
                        String result = __borderMessage.optString("win");
                        int _win = Integer.parseInt(result);
                        if (_win != -1) {
                            borderMessage.add(_win);
                        }
                    }
                    //测试用例
                    LogUtil.i("isPeopleNum=百家乐-" + isPeopleNum + " people_number=" + people_number);
                    Hall hall = new Hall(roomId, state, second, people_number, borderMessage, isPeopleNum, _colorStr);
                    hallList.add(hall);
                }
                //传一个List<Hall> 过去
//                        LogUtil.i("1join_game_command=", " hallList.size()=" + hallList.size());
                //测试 start
//                        boardMessageList.addAll(BaccaratUtil.getInstance().initBoardMessage(boardMessageList.size(), boardMessageList));
                //测试 end
                postEventBaccaratLotty(hallList);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            LogUtil.i("大厅-百家乐 onConnect");
            timer.cancel();
            conn();
        }
    };

    private void conn() {
        if (TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN))) {
            ToastUtil.show(mContext, "未登录");
        } else {
            if (mSocket != null && mSocket.connected()) {//启动 http://localhost:8080/user/startroom
                mSocket.emit("joingame", "{'game':'baccarat'}");
            }
        }
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.i("大厅-百家乐 onDisconnect");
            ToastUtil.show(mContext, "已断开服务器");
            conn();
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.i("大厅-百家乐 onConnectError");
            ToastUtil.show(mContext, mContext.getResources().getString(R.string.disconnect));
            conn();
        }
    };
}
