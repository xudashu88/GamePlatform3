package com.xmplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.service.MusicService;

import java.util.ArrayList;
import java.util.List;

import xmplayer.xiaoming.media.XMGLSurfaceView;
import xmplayer.xiaoming.media.XMMediaPlayerAPI;

/**
 * Created by bhb on 2017/8/25.
 */

public class XMPlayerActivity extends AppCompatActivity {
    private static String TAG = "xmplayer";
    private XMGLSurfaceView mVideoView;
    private int mAngle = 1;
    private boolean mOpenSound = true;
    int mCurrentDirection = -1;

    private ImageView iv_0;
    private ImageView iv_1;
    private ImageView iv_2;
    private ImageView iv_3;
    private ImageView iv_4;
    private ImageView iv_5;
    private ImageView iv_6;
    private ImageView iv_7;

    static {
        System.loadLibrary("xmplayer");
    }

    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View main = getLayoutInflater().inflate(R.layout.activity_xmplayer, null);
        main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(main);

        Button bntShellsSend = (Button) findViewById(R.id.bntSellSend);
        bntShellsSend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    XMMediaPlayerAPI.shellsSendDown();
                    Log.d(TAG, "onShellSendTouch:");
                    isShowIv(View.GONE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    XMMediaPlayerAPI.shellsSendUp();
                    Log.d(TAG, "onShellSendTouch:");
                }
                return false;
            }
        });

        Button bntShellsEnhance = (Button) findViewById(R.id.butSellEnhance);
        bntShellsEnhance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    XMMediaPlayerAPI.shellsEnhanceDown();
                    Log.d(TAG, "shellsEnhanceDown:");
                    isShowIv(View.GONE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    XMMediaPlayerAPI.shellsEnhanceUp();
                    Log.d(TAG, "shellsEnhanceUp:");
                }
                return false;
            }
        });

        mVideoView = (XMGLSurfaceView) findViewById(R.id.videoView);
        RockerView rockerViewLeft = (RockerView) findViewById(R.id.rockerView);
        if (rockerViewLeft != null) {
            rockerViewLeft.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
            rockerViewLeft.setOnShakeListener(RockerView.DirectionMode.DIRECTION_8, new RockerView.OnShakeListener() {
                @Override
                public void onStart() {
                    mCurrentDirection = -1;
                }

                @Override
                public void direction(RockerView.Direction direction) {
                    final int tmpDirection;
                    switch (direction) {
                        case DIRECTION_LEFT:
                            tmpDirection = XMMediaPlayerAPI.XM_ROCKER_TYPE.XM_ROCKER_LEFT.ordinal();
                            LogUtil.i("tmpDirection DIRECTION_LEFT");
                            break;
                        case DIRECTION_UP:
                        case DIRECTION_UP_RIGHT:
                        case DIRECTION_UP_LEFT:
                            tmpDirection = XMMediaPlayerAPI.XM_ROCKER_TYPE.XM_ROCKER_TOP.ordinal();
                            LogUtil.i("tmpDirection DIRECTION_UP_LEFT");
                            break;
                        case DIRECTION_RIGHT:
                            tmpDirection = XMMediaPlayerAPI.XM_ROCKER_TYPE.XM_ROCKER_RIGHT.ordinal();
                            LogUtil.i("tmpDirection DIRECTION_RIGHT");
                            break;
                        case DIRECTION_DOWN:
                        case DIRECTION_DOWN_RIGHT:
                        case DIRECTION_DOWN_LEFT:
                            tmpDirection = XMMediaPlayerAPI.XM_ROCKER_TYPE.XM_ROCKER_BUTTOM.ordinal();
                            LogUtil.i("tmpDirection DIRECTION_DOWN_LEFT");
                            break;
                        default:
                            tmpDirection = -1;
                            break;
                    }

                    if (mCurrentDirection != tmpDirection && tmpDirection != -1) {
                        if (mCurrentDirection != -1) {
                            XMMediaPlayerAPI.setRockerUp(mCurrentDirection);
                            Log.d(TAG, "setRockerUp type:" + mCurrentDirection);
                        }
                        XMMediaPlayerAPI.setRockerDown(tmpDirection);
                        mCurrentDirection = tmpDirection;
                        Log.d(TAG, "setRockerDown type:" + tmpDirection);
                    } else if (mCurrentDirection != tmpDirection && tmpDirection == -1) {
                        XMMediaPlayerAPI.setRockerUp(mCurrentDirection);
                        Log.d(TAG, "setRockerUp type:" + mCurrentDirection);
                        mCurrentDirection = -1;
                    }
                }

                @Override
                public void onFinish() {
                    if (mCurrentDirection != -1) {
                        XMMediaPlayerAPI.setRockerUp(mCurrentDirection);
                        Log.d(TAG, "setRockerUp type:" + mCurrentDirection);
                        mCurrentDirection = -1;
                    }
                }
            });
        }

        String _ip = this.getIntent().getStringExtra("ip");
        String ip = _ip.split(":")[0];
        int port = Integer.parseInt(_ip.split(":")[1]);
        tag = this.getIntent().getStringExtra("tag");
        //0右上 1上中 2左上 3左中 4左下 5下中 6右下 7右中
        XMMediaPlayerAPI.startPlay(ip, port, "xiaoming", "ali",
                "xm.token", "6", "android", "xmpalyerdemo", Integer.parseInt(tag));
        initWhere();

        RelativeLayout rl_xmplayer_root = (RelativeLayout) findViewById(R.id.rl_xmplayer_root);
        int _interview = R.dimen.unit40;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (displayMetrics.widthPixels < 2100 && displayMetrics.widthPixels > 2000 && displayMetrics.densityDpi == 480) {
            rl_xmplayer_root.setPadding(getResources().getDimensionPixelSize(_interview), 0, getResources().getDimensionPixelSize(_interview), 0);
        }

        Intent intent = new Intent(getApplicationContext(), MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
//        if (SharedPreUtil.getInstance(this).getInt(Constant.MUSIC_SWITCH) == 0) {
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
//        }
    }

    private List<ImageView> imageViewList = new ArrayList<>();

    private void initWhere() {
        iv_0 = (ImageView) findViewById(R.id.iv_0);
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        iv_2 = (ImageView) findViewById(R.id.iv_2);
        iv_3 = (ImageView) findViewById(R.id.iv_3);
        iv_4 = (ImageView) findViewById(R.id.iv_4);
        iv_5 = (ImageView) findViewById(R.id.iv_5);
        iv_6 = (ImageView) findViewById(R.id.iv_6);
        iv_7 = (ImageView) findViewById(R.id.iv_7);
        addImageView(iv_0);
        addImageView(iv_1);
        addImageView(iv_2);
        addImageView(iv_3);
        addImageView(iv_4);
        addImageView(iv_5);
        addImageView(iv_6);
        addImageView(iv_7);
    }

    private void addImageView(ImageView iv) {
        imageViewList.add(iv);
        isShowIv(View.VISIBLE);
    }

    private void isShowIv(int vis) {
        for (ImageView iv : imageViewList) {
            String _tag = iv.getTag().toString().split("_")[1];
            if (tag.equals(_tag)) {
                iv.setVisibility(vis);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onExitClick(View v) {
        XMMediaPlayerAPI.stopPlay();
        mVideoView.Destory();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isShowIv(View.GONE);
        if (SharedPreUtil.getInstance(this).getInt(Constant.MUSIC_SWITCH) == 2) {
            startService(new Intent(this, MusicService.class));
        }
        finish();
    }

    public void onMirrorClick(View v) {
        mAngle = (mAngle + 1) % 2;
        XMMediaPlayerAPI.setRotate(mAngle);
        isShowIv(View.GONE);
    }

    public void onSoundClick(View v) {
        mOpenSound = !mOpenSound;
        if (mOpenSound) {
            XMMediaPlayerAPI.openSound();
            v.setBackgroundResource(R.drawable.off);
        } else {
            XMMediaPlayerAPI.closeSound();
            v.setBackgroundResource(R.drawable.on);
        }
        isShowIv(View.GONE);
    }

    public void onScore1000Click(View v) {
        XMMediaPlayerAPI.addCharge(1000);
        isShowIv(View.GONE);
    }

    public void onScore10000Click(View v) {
        XMMediaPlayerAPI.addCharge(10000);
        isShowIv(View.GONE);
    }

    public void onScore100000Click(View v) {
        XMMediaPlayerAPI.addCharge(100000);
        isShowIv(View.GONE);
    }

    public void onSettlementClick(View v) {
        XMMediaPlayerAPI.settlement();
        isShowIv(View.GONE);
    }

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
}
