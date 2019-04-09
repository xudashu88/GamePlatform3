package com.xmplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.daniulive.smartplayer.SmartPlayerJniV2;
import com.eventhandle.NTSmartEventCallbackV2;
import com.eventhandle.NTSmartEventID;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.TabHostActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.controler.ArcadeLiveSocketController;
import com.purity.yu.gameplatform.service.MusicService;
import com.videoengine.NTRenderer;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by ych on 2017/7/18.
 */

public class RobotFishActivity extends AppCompatActivity {
    private static final String TAG = "RobotFishActivity";
    private Context mContext;
    private SurfaceView sv;
    private int mAngle = 1;
    private boolean mOpenSound = true;
    private List<ImageView> imageViewList = new ArrayList<>();
    private ImageView iv_1;
    private ImageView iv_2;
    private ImageView iv_3;
    private ImageView iv_4;
    private ImageView iv_5;
    private ImageView iv_6;
    private ImageView iv_7;
    private ImageView iv_8;
    private ImageView iv_9;
    private ImageView iv_10;

    private String tag;
    private String ip;
    private RockerView rockerView;
    int mCurrentDirection = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initWhere();
        CreateView();
        autoPlayer();//在控件上播放
        initEvent();
        init(mContext);
        connectSocket();
    }

    private void initEvent() {
        rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        rockerView.setOnShakeListener(RockerView.DirectionMode.DIRECTION_8, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {
//                mCurrentDirection = -1;
            }

            @Override
            public void direction(RockerView.Direction direction) {
                final int tmpDirection;
                switch (direction) {
                    case DIRECTION_LEFT:
                        mSocket.emit("catchFishMove", "{'pos':3}");
                        break;
                    case DIRECTION_UP:
                    case DIRECTION_UP_RIGHT:
                    case DIRECTION_UP_LEFT:
                        mSocket.emit("catchFishMove", "{'pos':1}");
                        break;
                    case DIRECTION_RIGHT:
                        mSocket.emit("catchFishMove", "{'pos':4}");
                        break;
                    case DIRECTION_DOWN:
                    case DIRECTION_DOWN_RIGHT:
                    case DIRECTION_DOWN_LEFT:
                        mSocket.emit("catchFishMove", "{'pos':2}");
                        break;
                    default:
                        tmpDirection = -1;
                        break;
                }
            }

            @Override
            public void onFinish() {
//                mCurrentDirection = -1;
            }
        });
    }

    private void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View main = getLayoutInflater().inflate(R.layout.activity_robot_fish, null);
        main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(main);
        mContext = this;
        tag = this.getIntent().getStringExtra("tag");
        ip = this.getIntent().getStringExtra("ip");
        SharedPreUtil.getInstance(mContext).saveParam("ROBOT_FISH_SIT", tag);
        switchURL = ip;
        sv = (SurfaceView) findViewById(R.id.sv);
        rockerView = (RockerView) findViewById(R.id.rockerView);

        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        stopService(new Intent(this, MusicService.class));
        if (mService != null) {
            mService.pauseMusic();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mService.pauseMusic();
                }
            }, 600);
        }
    }

    private void initWhere() {
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_2 = (ImageView) findViewById(R.id.iv_2);
        iv_3 = (ImageView) findViewById(R.id.iv_3);
        iv_4 = (ImageView) findViewById(R.id.iv_4);
        iv_5 = (ImageView) findViewById(R.id.iv_5);
        iv_6 = (ImageView) findViewById(R.id.iv_6);
        iv_7 = (ImageView) findViewById(R.id.iv_7);
        iv_8 = (ImageView) findViewById(R.id.iv_8);
        iv_9 = (ImageView) findViewById(R.id.iv_9);
        iv_10 = (ImageView) findViewById(R.id.iv_10);
        addImageView(iv_1);
        addImageView(iv_2);
        addImageView(iv_3);
        addImageView(iv_4);
        addImageView(iv_5);
        addImageView(iv_6);
        addImageView(iv_7);
        addImageView(iv_8);
        addImageView(iv_9);
        addImageView(iv_10);
        isShowIv(View.VISIBLE);
    }

    private void addImageView(ImageView iv) {
        imageViewList.add(iv);
    }

    private void isShowIv(int vis) {
        for (ImageView iv : imageViewList) {
            String _tag = iv.getTag().toString().split("_")[1];
            if (tag.equals(_tag)) {
                iv.setVisibility(vis);
            }
        }
    }

    public void onSellEnhance(View view) {
        if (mSocket.connected()) {
            isShowIv(View.GONE);
            mSocket.emit("catchFishCommand", "{'type':1}");
        }
    }

    public void onSellSend(View view) {
        if (mSocket.connected()) {
            isShowIv(View.GONE);
            mSocket.emit("catchFishCommand", "{'type':2}");
        }

    }

    public void onExitClick(View v) {
//        libPlayer.SmartPlayerClose(playerHandle);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        libPlayer.SmartPlayerStopPlay(playerHandle);
        isShowIv(View.GONE);
        if (SharedPreUtil.getInstance(this).getInt(Constant.MUSIC_SWITCH) == 2) {
            startService(new Intent(this, MusicService.class));
        }
        startActivity(new Intent(mContext, TabHostActivity.class));
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        libPlayer.SmartPlayerStopPlay(playerHandle);
    }

    public void onMirrorClick(View v) {
        mAngle = (mAngle + 1) % 2;
        isShowIv(View.GONE);
    }

    public void onSoundClick(View v) {
        mOpenSound = !mOpenSound;
        if (mOpenSound) {
            v.setBackgroundResource(R.drawable.off);
        } else {
            v.setBackgroundResource(R.drawable.on);
        }
        isShowIv(View.GONE);
    }

    public void onScore1000Click(View v) {
        isShowIv(View.GONE);
        mSocket.emit("catchFishRecharge", "{'amount':1000}");
    }

    public void onScore10000Click(View v) {
        isShowIv(View.GONE);
        mSocket.emit("catchFishRecharge", "{'amount':10000}");
    }

    public void onScore100000Click(View v) {
        isShowIv(View.GONE);

    }

    public void onSettlementClick(View v) {
        isShowIv(View.GONE);
        mSocket.emit("catchFishWithdraw", "");
    }

    //---------------------daniu start------------------------
    private SurfaceView sSurfaceView = null;
    private long playerHandle = 0;
    private SmartPlayerJniV2 libPlayer = null;
    private boolean isHardwareDecoder = Constant.DANIU_HARDWARE;//true 硬件解码  false 软件解码

    private int playBuffer = 0; // 默认200ms 已改为1000
    private boolean isLowLatency = true; // 超低延时，默认不开启  已改为开启
    private boolean isFastStartup = true; // 是否秒开, 默认true
    private int rotate_degrees = 0;//视频顺时针旋转角度

    private boolean switchUrlFlag = false;
    //    private String switchURL = "rtmp://live.hkstv.hk.lxdns.com/live/hks";//默认播放
    private String switchURL = Constant.FISH2;//默认播放
    private boolean isPlaying = false;//默认未播放

    private boolean CreateView() {
        libPlayer = new SmartPlayerJniV2();
        sSurfaceView = sv;
        if (sSurfaceView == null) {
            sSurfaceView = NTRenderer.CreateRenderer(mContext, false);
        }
        if (sSurfaceView == null) {
            return false;
        }
        return true;
    }

    private void autoPlayer() {
        Log.i(TAG, "Start playback stream++");
        InitAndSetConfig();//初始化参数
        // 如果第二个参数设置为null，则播放纯音频
        libPlayer.SmartPlayerSetSurface(playerHandle, sv);
        libPlayer.SmartPlayerSetAudioOutputType(playerHandle, 0);

        if (isHardwareDecoder) {
            Log.i(TAG, "check isHardwareDecoder: " + isHardwareDecoder);
            int hwChecking = libPlayer.SetSmartPlayerVideoHWDecoder(playerHandle, isHardwareDecoder ? 1 : 0);
            Log.i(TAG, "[daniulive] hwChecking: " + hwChecking);
        }

        libPlayer.SmartPlayerSetLowLatencyMode(playerHandle, isLowLatency ? 1 : 0);
        libPlayer.SmartPlayerSetRotation(playerHandle, rotate_degrees);
        int iPlaybackRet = libPlayer.SmartPlayerStartPlay(playerHandle);//playerHandle 547740596352 547740595584
        if (iPlaybackRet != 0) { //非0  就不能直接播放
            Log.e(TAG, "StartPlayback strem failed..");
            return;
        }
        isPlaying = true;
    }

    private void start() {
        if (playerHandle != 0) {
            libPlayer.SmartPlayerSwitchPlaybackUrl(playerHandle, switchURL);
        }
    }

    private void InitAndSetConfig() {
        playerHandle = libPlayer.SmartPlayerOpen(mContext);
        if (switchURL == null) {
            initNullBackground();
            return;
        }
        if (playerHandle == 0) {
            return;
        }
        libPlayer.SetSmartPlayerEventCallbackV2(playerHandle, new EventHandleV2());
        libPlayer.SmartPlayerSetBuffer(playerHandle, playBuffer);
        libPlayer.SmartPlayerSetFastStartup(playerHandle, isFastStartup ? 1 : 0);

        libPlayer.SmartPlayerSetUrl(playerHandle, switchURL);
    }

    class EventHandleV2 implements NTSmartEventCallbackV2 {
        @Override
        public void onNTSmartEventCallbackV2(long handle, int id, long param1, long param2, String param3, String param4, Object param5) {
            switch (id) {
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STARTED:
                    Log.i(TAG, "开始。。");
                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTING:
                    Log.i(TAG, "连接中。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTION_FAILED:
                    Log.i(TAG, "连接失败。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTED:
                    Log.i(TAG, "连接成功。。");
                    if (SharedPreUtil.getInstance(mContext).getInt(Constant.VIDEO_PLAYING) == 0) {
                        initDefaultBackground();
                    } else
                        initNullBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_DISCONNECTED:
                    Log.i(TAG, "连接断开。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STOP:
                    Log.i(TAG, "停止播放。。");
                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_RESOLUTION_INFO:
                    Log.i(TAG, "分辨率信息: width: " + param1 + ", height: " + param2 + "   isPlaying=");

                    initNullBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_NO_MEDIADATA_RECEIVED:
                    Log.i(TAG, "收不到媒体数据，可能是url错误。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_SWITCH_URL:
                    Log.i(TAG, "切换播放URL。。");
//                    initDefaultBackground();
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_START_BUFFERING:
                    Log.i(TAG, "Start_Buffering");
                    break;

                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_BUFFERING:
                    Log.i(TAG, "Buffering:" + param1 + "%");
                    break;
                case NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STOP_BUFFERING:
                    Log.i(TAG, "Stop_Buffering");
                    break;
            }
        }
    }

    private void initDefaultBackground() {
        ((RobotFishActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sSurfaceView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_baccarat_video));
            }
        });
    }

    private void initNullBackground() {
        ((RobotFishActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sSurfaceView.setBackground(null);
            }
        });
    }

    //---------------------daniu end------------------------


    //--------------------------------------------Service start--------------------------------------------------
    private MusicService mService = null;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) binder;
            mService = musicBinder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };
    //--------------------------------------------Service end--------------------------------------------------

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
                        break;
                    case "catchfish_uncommand":
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
