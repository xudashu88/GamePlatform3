package com.purity.yu.gameplatform.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.service.MusicService;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.SoundPoolUtil;

import butterknife.BindView;

@ContentView(R.layout.fragment_menu_music)
public class MenuMusicFragment extends BaseFragment {

    @BindView(R.id.tv_sound_switch_on)
    TextView tv_sound_switch_on;
    @BindView(R.id.tv_sound_switch_off)
    TextView tv_sound_switch_off;
    @BindView(R.id.tv_music_switch_on)
    TextView tv_music_switch_on;
    @BindView(R.id.tv_music_switch_off)
    TextView tv_music_switch_off;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.tv_volume)
    TextView tv_volume;

    @Override
    protected void initView(View view) {
        initEvent();
        initData();
    }

    private void initData() {
        int _soundSwitch = SharedPreUtil.getInstance(getActivity()).getInt(Constant.SOUND_SWITCH);
        int _musicSwitch = SharedPreUtil.getInstance(getActivity()).getInt(Constant.MUSIC_SWITCH);
        if (_soundSwitch == 0) {
            soundSwitch(View.VISIBLE, View.GONE);
        } else if (_soundSwitch == 1) {
            soundSwitch(View.GONE, View.VISIBLE);
        }
        if (_musicSwitch == 2) {
            musicSwitch(View.VISIBLE, View.GONE);
        } else if (_musicSwitch == 1) {
            musicSwitch(View.GONE, View.VISIBLE);
        }

        final String _musicVolume = SharedPreUtil.getInstance(getActivity()).getString(Constant.MUSIC_VOLUME);
        if (_musicVolume == null || _musicVolume.equals("")) {
            tv_volume.setText("100");
            seekBar.setProgress(100);
        } else {
            tv_volume.setText(_musicVolume);
            seekBar.setProgress(Integer.parseInt(_musicVolume));
        }
        if (SharedPreUtil.getInstance(getActivity()).getInt(Constant.MUSIC_SWITCH) == 2) {
            Intent intent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void initEvent() {
        tv_sound_switch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundSwitch(View.GONE, View.VISIBLE);
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.SOUND_SWITCH, 1);
                //音效关
                SoundPoolUtil.getInstance().release();
            }
        });
        tv_sound_switch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundSwitch(View.VISIBLE, View.GONE);
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.SOUND_SWITCH, 0);
                //音效开 还要根据国际化进行初始化
                BaccaratUtil.getInstance().openSound(getActivity());
            }
        });
        tv_music_switch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSwitch(View.GONE, View.VISIBLE);
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.MUSIC_SWITCH, 1);
                //背景音乐关
//                getActivity().stopService(new Intent(getActivity(), MusicService.class));
                if (mService != null) {
                    mService.pauseMusic();//只暂停
                }

            }
        });
        tv_music_switch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSwitch(View.VISIBLE, View.GONE);
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.MUSIC_SWITCH, 2);
                //背景音乐开
                if (mService == null) {
                    getActivity().startService(new Intent(getActivity(), MusicService.class));
                    Intent intent = new Intent(getActivity(), MusicService.class);
                    getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                } else {
                    mService.playMusic();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_volume.setText(Integer.toString(progress));
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.MUSIC_VOLUME, Integer.toString(progress));
                if (mBound) {
                    mService.setVolume((float) (progress / 100.0));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
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

    private void musicSwitch(int gone, int visible) {
        tv_music_switch_on.setVisibility(gone);
        tv_music_switch_off.setVisibility(visible);
    }

    private void soundSwitch(int gone, int visible) {
        tv_sound_switch_on.setVisibility(gone);
        tv_sound_switch_off.setVisibility(visible);
    }

}
