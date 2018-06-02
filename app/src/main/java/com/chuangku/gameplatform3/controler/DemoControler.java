package com.chuangku.gameplatform3.controler;

import android.content.Context;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class DemoControler {
    private Context mContext;
    private static DemoControler instance = null;

    public void init(Context context) {
        mContext = context;

    }

    public static DemoControler getInstance() {
        if (null == instance) {
            instance = new DemoControler();
        }
        return instance;
    }
}
