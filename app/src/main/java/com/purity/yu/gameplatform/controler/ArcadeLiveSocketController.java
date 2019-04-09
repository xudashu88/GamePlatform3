package com.purity.yu.gameplatform.controler;

import android.content.Context;
import android.text.TextUtils;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * 功能：扑鱼房间socket
 * 描述：
 * Create by purity on 2018/11/21.
 */
public class ArcadeLiveSocketController {
    private Context mContext;
    private static ArcadeLiveSocketController instance = null;
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.SOCKET_ROOM));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void init(Context context) {
        mContext = context;
    }

    public static ArcadeLiveSocketController getInstance() {
        if (null == instance) {
            instance = new ArcadeLiveSocketController();
        }
        return instance;
    }

    public void connectSocket() {
        if (mSocket != null) {
            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on("command", OnCommand);
            mSocket.connect();
        }
    }

    public Socket getSocket() {
        if (mSocket.connected()) {
            return mSocket;
        }
        return mSocket;
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
            LogUtil.i("进入打鱼机房间6 ok啦");
            LogUtil.i("0join_game_command" + args[0].toString());
            JSONObject json;
            try {
                json = new JSONObject(args[0].toString());
                String command = json.optString("command");
                String data = json.optString("data");
                switch (command) {
                    case "catchfish_command"://当前局每个位置下注成功的分数
                        ToastUtil.show(mContext, "ok");
                        break;
                    case "catchfish_uncommand":
                        ToastUtil.show(mContext, "no");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.i("进入打鱼机房间3_1onConnect");
            conn();
        }
    };

    private void conn() {
        if (TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN))) {
            ToastUtil.show(mContext, "未登录");
        } else {
            if (mSocket != null && mSocket.connected()) {//启动 http://localhost:8080/user/startroom
                String _token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
                String position = SharedPreUtil.getInstance(mContext).getString("ROBOT_FISH_SIT");
                String roomId = SharedPreUtil.getInstance(mContext).getString("ROBOT_FISH_ROOM");
                mSocket.emit("joinroom", "{'token':'" + _token + "','room':'" + roomId + "','position':'" + position + "'}");
                LogUtil.i("进入打鱼机房间3_2onConnect");
            }
        }
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ToastUtil.show(mContext, "已断开服务器");
            LogUtil.i("进入打鱼机房间3_4onDisconnect");
            conn();
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ToastUtil.show(mContext, mContext.getResources().getString(R.string.disconnect));
            LogUtil.i("进入打鱼机房间3_3onConnectError");
            conn();
        }
    };
}
