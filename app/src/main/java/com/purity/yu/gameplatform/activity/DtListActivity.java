package com.purity.yu.gameplatform.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.DtRVAdapter;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.controler.TabHostController;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.Hall;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.utils.ProtocolUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import avi.AVLoadingIndicatorView;
import butterknife.BindView;
import butterknife.OnClick;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * 功能：百家乐
 * 描述：
 * Create by purity on 2018/5/25.
 */
@ContentView(R.layout.activity_baccarat_list)
public class DtListActivity extends BaseActivity {

    private static final String TAG = "DtListActivity";
    @BindView(R.id.ll_back)
    public LinearLayout ll_back;
    @BindView(R.id.ll_title)
    public LinearLayout ll_title;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_game_table_num)
    public TextView tv_game_table_num;
    @BindView(R.id.rl_rv)
    public RelativeLayout rl_rv;
    @BindView(R.id.rv_baccarat)
    public RecyclerView rv_baccarat;
    @BindView(R.id.ll_good_road_sorting)
    public LinearLayout ll_good_road_sorting;
    @BindView(R.id.tv_game_name)
    public TextView tv_game_name;

    @BindView(R.id.tv_switch_on)
    public TextView tv_switch_on;
    @BindView(R.id.tv_switch_off)
    public TextView tv_switch_off;
    @BindView(R.id.iv_bac_set)
    ImageView iv_set;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private Context mContext;
    private List<Baccarat> baccaratList = new ArrayList<>();
    private DtRVAdapter adapter;

    private String totalTable;

    @Override
    protected void initView(Bundle savedInstanceState) {
        this.mContext = DtListActivity.this;
        avi.show();
        ll_good_road_sorting.setVisibility(View.INVISIBLE);
        tv_game_name.setText(getResources().getString(R.string.dt_game));
        tv_nickname.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
        totalTable = mContext.getResources().getString(R.string.total_table);
        EventBus.getDefault().register(this);

        String _gameList = SharedPreUtil.getInstance(mContext).getString(Constant.PRE_DT_LIST);
        List<Baccarat> list = new Gson().fromJson(_gameList, new TypeToken<List<Baccarat>>() {
        }.getType());
        if (list != null && list.size() > 0) {
            baccaratList.clear();
            baccaratList.addAll(list);
            String sFinal = String.format(totalTable, list.size());
            tv_game_table_num.setText(sFinal);
            initAdapter();
            ProtocolUtil.getInstance().postLoginInfo(mContext, tv_money);
        }
        initEvent();
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                startActivity(new Intent(mContext, TabHostActivity.class));
                finish();
                break;
        }
    }

    private void initEvent() {
        iv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.getInstance(mContext).saveParam(Constant.WHERE_SET, 4);
                TabHostController.getInstance().initMenu(mContext, 1, new Timer());
            }
        });
    }

    private void initAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        adapter = new DtRVAdapter(mContext, baccaratList);
        rv_baccarat.setItemViewCacheSize(baccaratList.size());//牛逼  https://blog.csdn.net/MeloDev/article/details/76691951 解决第一条数据消失的问题
        rv_baccarat.setLayoutManager(mLayoutManager);
        rv_baccarat.setAdapter(adapter);
        init(mContext);
        connectSocket();
    }

    //-----------------------------------------大厅开始---------------------------------------------------------
    private Socket mSocket;
    private Timer timer;
    private int i = 0;

    public void init(Context context) {
        mContext = context;
        timer = new Timer(true);
        try {
            mSocket = IO.socket(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.SOCKET_LOBBY));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    LogUtil.i("进入龙虎大厅6 ok啦" + (++i));
//                    LogUtil.i("socket_command=" + args[0].toString());
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
                            state = _json.getString("state");//BET:下注阶段 RESULT:开牌阶段 OVER:清算阶段 WAITTING:同步识别端
                            second = _json.optInt("second");//20s
                            people_number = _json.optInt("peoplenumber");//当前房间人数
                            String roomConfig = _json.optString("roomConfig");
                            JSONObject _roomConfig = new JSONObject(roomConfig);
                            String color = _roomConfig.optString("color");
                            String[] _colorStr = new Gson().fromJson(color, String[].class);
                            int isPeopleNum = _roomConfig.optInt("peopleNum");//1 显示人数 0不显示人数
                            JSONArray boardMessage = _json.getJSONArray("boardMessage");
                            List<Integer> boardMessageList = new ArrayList<>();//原始数据
                            for (int j = 0; j < boardMessage.length(); j++) {
                                JSONObject _boardMessage = boardMessage.getJSONObject(j);
                                int win = _boardMessage.optInt("win");
                                if (win != -1) {
                                    boardMessageList.add(win);
                                }
                            }
                            //测试用例

                            Hall hall = new Hall(roomId, state, second, people_number, boardMessageList, isPeopleNum, _colorStr);
                            hallList.add(hall);
                        }
                        if (i < 2) {//如果接受了多次 就强制只接受一个 中间也不要用EventBus在传一遍
                            avi.hide();
                            adapter.update(hallList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

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
                mSocket.emit("joingame", "{'game':'dragonTiger'}");
            }
        }
    }

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
        }
    };
//-----------------------------------------大厅结束---------------------------------------------------------

    /*
     * 余额
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void acceptEventMoneyChange(final ObjectEvent.MoneyChangeEvent event) {
        tv_money.setText(event.money);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        disconnectSocket();//后退是重新打开？？
        SharedPreUtil.getInstance(mContext).saveParam("times", 0);//退出房间清零
        avi.stopAnimation();
    }
}