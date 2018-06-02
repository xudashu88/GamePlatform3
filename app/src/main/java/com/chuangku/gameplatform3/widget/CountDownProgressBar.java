package com.chuangku.gameplatform3.widget;

import android.os.Handler;
import android.widget.ProgressBar;

import com.gangbeng.basemodule.base.Constant;

/**
 * 进度条倒计时 3s
 * Created by ych on 2018/5/17.
 */
public class CountDownProgressBar {
    private Handler handler;

    public CountDownProgressBar(Handler handler) {
        this.handler = handler;
    }
    public void countDown(ProgressBar pb) {
        handler.sendEmptyMessageDelayed(Constant.WHAT_COUNT_DOWN, 500);
        pb.incrementProgressBy(40);
    }
}
