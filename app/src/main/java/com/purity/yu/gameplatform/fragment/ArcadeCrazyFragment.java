package com.purity.yu.gameplatform.fragment;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Hall;
import com.purity.yu.gameplatform.entity.Player;
import com.xmplayer.RobotFishActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * 街机 疯狂小鸟
 */
@ContentView(R.layout.fragment_arcade_crazy)
public class ArcadeCrazyFragment extends BaseFragment {

    @BindView(R.id.puyu_seat_1)
    ImageView puyu_seat_1;
    @BindView(R.id.puyu_seat_2)
    ImageView puyu_seat_2;
    @BindView(R.id.puyu_seat_3)
    ImageView puyu_seat_3;
    @BindView(R.id.puyu_seat_4)
    ImageView puyu_seat_4;
    @BindView(R.id.puyu_seat_5)
    ImageView puyu_seat_5;
    @BindView(R.id.puyu_seat_6)
    ImageView puyu_seat_6;
    @BindView(R.id.puyu_seat_7)
    ImageView puyu_seat_7;
    @BindView(R.id.puyu_seat_8)
    ImageView puyu_seat_8;
    @BindView(R.id.puyu_seat_9)
    ImageView puyu_seat_9;
    @BindView(R.id.puyu_seat_10)
    ImageView puyu_seat_10;
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<String> _strList = new ArrayList<>();
    private Context mContext;
    List<Hall> hallList = new ArrayList<>();
    List<String> positionList = new ArrayList<>();

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        addImageView(puyu_seat_1);
        addImageView(puyu_seat_2);
        addImageView(puyu_seat_3);
        addImageView(puyu_seat_4);
        addImageView(puyu_seat_5);
        addImageView(puyu_seat_6);
        addImageView(puyu_seat_7);
        addImageView(puyu_seat_8);
        addImageView(puyu_seat_9);
        addImageView(puyu_seat_10);
        init(getActivity());
        initTableView();
        connectSocket();
    }

    private void initTableView() {
        if (hallList.size() > 0) {
            for (int i = 0; i < hallList.size(); i++) {
                if (hallList.get(i).playerList.size() > 0 && hallList.get(i).gameRoom.equals("10000000000000004")) {
                    List<Player> playerList = hallList.get(0).playerList;//鳄鱼在第0个
                    for (int j = 0; j < playerList.size(); j++) {
                        positionList.add(""+playerList.get(i).position);
                    }
                }
            }
            for (final ImageView iv : imageViewList) {
                String _tag = (String) iv.getTag();
                if (positionList.size() > 0) {
                    for (int i = 0; i < positionList.size(); i++) {
                        if (_tag.equals("" + positionList.get(i))) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    iv.setBackgroundResource(R.drawable.arcade_puyu_seat);
                                }
                            });
                        }
                    }
                }
            }
        } else {
            String _table = SharedPreUtil.getInstance(getActivity()).getString(Constant.CROCILISK_PARK);
            List<String> _strList = Util.stringToList(_table);
            for (ImageView iv : imageViewList) {
                String _tag = (String) iv.getTag();
                if (_strList.size() > 0) {
                    for (int i = 0; i < _strList.size(); i++) {
                        if (_tag.equals(_strList.get(i))) {
                            iv.setBackgroundResource(R.drawable.arcade_puyu_seat);
                        }
                    }
                }
            }
        }
    }

    private void addImageView(ImageView iv) {
        imageViewList.add(iv);
        setOnClick(iv);
    }

    private void setOnClick(ImageView iv) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _table = SharedPreUtil.getInstance(getActivity()).getString(Constant.CRAZY_BIRD);
                _strList = Util.stringToList(_table);

                ImageView _iv = (ImageView) v;
                if (_iv != null) {
                    _iv.setBackgroundResource(R.drawable.arcade_puyu_seat);
                    String tag = (String) _iv.getTag();
                    for (String str : _strList) {
                        if (str.equals(tag)) {
//                            ToastUtil.showToast(mContext.getResources().getString(R.string.playing));
//                            return;
                        }
                    }
                    for (int i=0;i<positionList.size();i++) {
                        if (positionList.get(i).equals(tag)) {
                            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.playing));
                            return;
                        }
                    }
//                    Intent intent = new Intent(mContext, XMPlayerActivity.class);
//                    intent.putExtra("tag", tag);
//                    intent.putExtra("ip", Constant.CRAZY_BIRD_IP);
//                    mContext.startActivity(intent);

                    Intent intent = new Intent(mContext, RobotFishActivity.class);
                    intent.putExtra("tag", tag);
                    intent.putExtra("ip", Constant.FISH_1);
                    mContext.startActivity(intent);
                    //UnsupportedOperationException
                    List<String> list = new ArrayList<>(_strList);//由於Arrays.asList返回的List没有重写add和remove方法，所以需要转换成常规的ArrayList
                    list.add(tag);
                    StringBuffer sb = new StringBuffer();
                    for (String str : list) {
                        sb.append(str + ",");
                    }
                    SharedPreUtil.getInstance(getActivity()).saveParam(Constant.CRAZY_BIRD, sb.toString());
                }
            }
        });
    }

    private Socket mSocket;
    //0庄 1闲 2和 3庄赢 庄对 4 庄赢 闲对 5 庄赢 庄对闲对 6闲赢 庄对 7闲赢 闲对 8闲赢 庄对闲对 9和赢 庄对 10和赢 闲对 11和赢 庄对闲对
    private List<Player> playerList = new ArrayList<>();//原始数据

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
            mSocket.off("hall", OnCommand);
        }
    }

    private Emitter.Listener OnCommand = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.i("进入打鱼机大厅6 ok啦");
            LogUtil.i("进入打鱼机大厅6join_game_command" + args[0].toString());

            JSONObject json;
            String roomId = "";
            String state = "";
            int second = 0;
            int people_number = 0;
            try {
                String str = args[0].toString();
                json = new JSONObject(str);
                hallList.clear();

                JSONArray data_array = json.getJSONArray("record");
                for (int i = 0; i < data_array.length(); i++) {
                    JSONObject _json = data_array.getJSONObject(i);
                    roomId = _json.getString("gameroom");//房间id
                    people_number = _json.optInt("peoplenumber");//当前房间人数
                    JSONArray players = _json.getJSONArray("players");
                    Player player = null;
                    for (int j = 0; j < players.length(); j++) {
                        JSONObject _boardMessage = players.getJSONObject(j);
                        String playuser = _boardMessage.optString("playuser");
                        String bei = _boardMessage.optString("bei");
                        String money = _boardMessage.optString("money");
                        int position = _boardMessage.optInt("position");
                        player = new Player(playuser, bei, money, position);
                        playerList.add(player);
                    }
                    Hall hall = new Hall(roomId, people_number, playerList);
                    hallList.add(hall);
                }
                initTableView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            LogUtil.i("进入打鱼机大厅3_1onConnect");
            conn();
        }
    };

    private void conn() {
        if (TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN))) {
            ToastUtil.show(mContext, "未登录");
        } else {
            if (mSocket != null && mSocket.connected()) {//启动 http://localhost:8080/user/startroom
                mSocket.emit("joingame", "{game:'catchFish'}");
                LogUtil.i("进入打鱼机大厅3_2onConnect");
            }
        }
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ToastUtil.show(mContext, "已断开服务器");
            LogUtil.i("进入打鱼机大厅3_4onDisconnect");
            conn();
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ToastUtil.show(mContext, mContext.getResources().getString(R.string.disconnect));
            LogUtil.i("进入打鱼机大厅3_3onConnectError");
            conn();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(getActivity());
    }
}
