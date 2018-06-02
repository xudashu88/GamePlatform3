package com.chuangku.gameplatform3.controler;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.chuangku.gameplatform3.activity.UserActivity;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class UserControler {
    private Context mContext;
    private static UserControler instance = null;

    private ImageView iv_close;

    public void init(Context context) {
        mContext = context;

    }

    public static UserControler getInstance() {
        if (null == instance) {
            instance = new UserControler();
        }
        return instance;
    }

    public void copyWidget(ImageView iv_close) {
        this.iv_close = iv_close;
        initEvent();
    }

    private void initEvent() {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserActivity) mContext).finish();
            }
        });
    }
}
