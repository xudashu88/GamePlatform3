package com.purity.yu.gameplatform.controler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.purity.yu.gameplatform.activity.UserActivity;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class UserController {
    private Context mContext;
    private static UserController instance = null;

    private ImageView iv_close;
    ImageView iv_msg;

    public void init(Context context) {
        mContext = context;
    }

    public static UserController getInstance() {
        if (null == instance) {
            instance = new UserController();
        }
        return instance;
    }

    public void copyWidget(ImageView iv_close,ImageView iv_msg) {
        this.iv_close = iv_close;
        this.iv_msg = iv_msg;
        initEvent();
    }

    private void initEvent() {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserActivity) mContext).finish();
            }
        });
        iv_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String data="http://103.231.167.85:8888/mobile/index.html";
                intent.setData(Uri.parse(data));
                mContext.startActivity(intent);
            }
        });
    }
}
