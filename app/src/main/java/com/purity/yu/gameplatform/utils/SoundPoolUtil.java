package com.purity.yu.gameplatform.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/7/10.
 */
public class SoundPoolUtil {
    private static SoundPoolUtil instance = null;
    HashMap<String, Integer> soundMap = new HashMap<>();
    private SoundPool mSoundPool = null;

    private SoundPoolUtil() {
    }

    public static SoundPoolUtil getInstance() {
        if (null == instance) {
            instance = new SoundPoolUtil();
        }
        return instance;
    }

    public void initCommon(Context context) throws Exception {
        String[] files = context.getResources().getAssets().list("sounds/common");
        mSoundPool = new SoundPool(files.length, AudioManager.STREAM_MUSIC, 0);
        for (int i = 0; i < files.length; i++) {
            if (files[i].split("\\.")[1].equals("wav") || files[i].split("\\.")[1].equals("mp3")) {//android 4.4莫名其妙多了一个以raw结尾的文件
                String filename = files[i].substring(0, files[i].indexOf("."));//获取文件名称
                soundMap.put(filename, mSoundPool.load(context.getAssets().openFd("sounds/common/" + files[i]), 1));
            }
        }
    }

    public void initBacZH(Context context) throws Exception {
        String[] files = context.getResources().getAssets().list("sounds/bac");
        mSoundPool = new SoundPool(files.length, AudioManager.STREAM_MUSIC, 0);
        for (int i = 0; i < files.length; i++) {
            if (files[i].split("\\.")[1].equals("wav") || files[i].split("\\.")[1].equals("mp3")) {
                String filename = files[i].substring(0, files[i].indexOf("."));//获取文件名称
                soundMap.put(filename, mSoundPool.load(context.getAssets().openFd("sounds/bac/" + files[i]), 1));
            }
        }
    }

    public void initBacEN(Context context) throws Exception {
        String[] files = context.getResources().getAssets().list("sounds/en/bac");
        mSoundPool = new SoundPool(files.length, AudioManager.STREAM_MUSIC, 0);
        for (int i = 0; i < files.length; i++) {
            if (files[i].split("\\.")[1].equals("wav") || files[i].split("\\.")[1].equals("mp3")) {
                String filename = files[i].substring(0, files[i].indexOf("."));//获取文件名称
                soundMap.put(filename, mSoundPool.load(context.getAssets().openFd("sounds/en/bac/" + files[i]), 1));
            }
        }
    }

    public void initDtZH(Context context) throws Exception {
        String[] files = context.getResources().getAssets().list("sounds/dt");
        mSoundPool = new SoundPool(files.length, AudioManager.STREAM_MUSIC, 0);
        soundMap.clear();
        for (int i = 0; i < files.length; i++) {
            if (files[i].split("\\.")[1].equals("wav") || files[i].split("\\.")[1].equals("mp3")) {
                String filename = files[i].substring(0, files[i].indexOf("."));//获取文件名称
                soundMap.put(filename, mSoundPool.load(context.getAssets().openFd("sounds/dt/" + files[i]), 1));
            }
        }
    }

    public void initDtEN(Context context) throws Exception {
        String[] files = context.getResources().getAssets().list("sounds/en/dt");
        mSoundPool = new SoundPool(files.length, AudioManager.STREAM_MUSIC, 0);
        soundMap.clear();
        for (int i = 0; i < files.length; i++) {
            if (files[i].split("\\.")[1].equals("wav") || files[i].split("\\.")[1].equals("mp3")) {
                String filename = files[i].substring(0, files[i].indexOf("."));//获取文件名称
                soundMap.put(filename, mSoundPool.load(context.getAssets().openFd("sounds/en/dt/" + files[i]), 1));
            }
        }
    }

    public void play(String soundId, int loop) {
        if (soundMap.get(soundId) == null) {//健壮性判断
            return;
        }
        mSoundPool.play(soundMap.get(soundId), 1, 1, 0, loop, 1);
    }

    public void play(String soundId) {
        if (soundMap.get(soundId) == null) {//健壮性判断
            return;
        }
        mSoundPool.play(soundMap.get(soundId), 1, 1, 0, 0, 1);
    }

    public void release() {
        if (mSoundPool != null)
            mSoundPool.release();
    }

}
