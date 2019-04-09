package com.purity.yu.gameplatform.controler;

import android.content.Context;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class DemoController {
    private Context mContext;
    private static DemoController instance = null;

    public void init(Context context) {
        mContext = context;

    }

    public static DemoController getInstance() {
        if (null == instance) {
            synchronized (DemoController.class) {
                if (null == instance) {
                    instance = new DemoController();
                }
            }
        }
        return instance;
    }
}
