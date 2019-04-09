package com.purity.yu.gameplatform.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.utils.BaccaratUtil;

//https://github.com/JimSeker/service
public class MusicService extends Service {
    private static MusicService instance = null;
    private MediaPlayer mediaPlayer;
    private IBinder binder = new MusicBinder();

    public class MusicBinder extends Binder {
        public MusicService getService() {
//            if (null == instance) {
//                synchronized (MusicService.class) {
//                    if (null == instance) {
//                        instance = new MusicService();
//                    }
//                }
//            }
//            return instance;
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        //是否循环播放
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        if (mediaPlayer == null) {
            return;
        }
        String _musicVolume = SharedPreUtil.getInstance(this).getString(Constant.MUSIC_VOLUME);
        //初始化音量
        if (TextUtils.isEmpty(_musicVolume)) {
            setVolume(1);
        } else
            setVolume((float) (Integer.parseInt(_musicVolume) / 100.0));


        // 添加来电监听事件
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); // 获取系统服务
        telManager.listen(new MobilePhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);

    }

    /**
     * @author wwj
     * 电话监听器类
     */
    private class MobilePhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: // 挂机状态
//                    LogUtil.i("电话监听器类 挂机状态");
                    playMusic();
//                    BaccaratUtil.getInstance().openSound(getApplication());
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:    //通话状态
                case TelephonyManager.CALL_STATE_RINGING:    //响铃状态
//                    LogUtil.i("电话监听器类 响铃状态");
                    pauseMusic();
                    BaccaratUtil.getInstance().closeSound();
                    break;
                default:
                    break;
            }
        }
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        return START_STICKY;
    }

    /**
     * 音量范围 0.0f---1.0f
     */
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }

    }

    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}