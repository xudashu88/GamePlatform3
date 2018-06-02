package com.gangbeng.basemodule.Widgets;

import android.os.Handler;
import android.widget.TextView;

import com.gangbeng.basemodule.R;
import com.gangbeng.basemodule.base.Constant;

/**
 * 倒计时
 * Created by ych on 2017/6/15.
 */
public class CountDown {
    private Handler handler;

    public CountDown(Handler handler, TextView tv) {
        this.handler = handler;
    }

    private int second = Constant.MAX_SECOND;
    //记得移除所有消息，避免内存泄露 mHandler.removeCallbacksAndMessages(null);
    //handleMessage必须放在对应的Activity中，否则无效
    /**
     * 倒计时60秒
     */
    public void countDown(TextView tv) {
        if(second>0){
            tv.setText("" + (second--) + "s");
        }else {//只有第一次获取时会初始化60秒，第二次就是上次的0，导致为负数
            second=Constant.MAX_SECOND;
            tv.setText("" + (second--) + "s");
        }

        handler.sendEmptyMessageDelayed(Constant.WHAT_COUNT_DOWN, 1000);
    }

    /**
     * 获取验证码成功 倒计时入口
     */
    public void getSmsCodeSuccess(TextView tv) {
        tv.setBackgroundResource(R.drawable.radius_transparent_bg_3);
        tv.setEnabled(false);    // 重发按钮不可用
        countDown(tv);                    // 倒计时60秒
    }
}
