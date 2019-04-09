package com.purity.yu.gameplatform.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.widget.CountDownProgressBar;

import butterknife.BindView;

@ContentView(R.layout.activity_start)
public class StartActivity extends BaseActivity {

    @BindView(R.id.pb_horizontal)
    ProgressBar pb_horizontal;

    private CountDownProgressBar countDownProgressBar;//countDown = new CountDown(mHandler, btn_register_smscode);
    private int second = Constant.MAX_SECOND;
    //记得移除所有消息，避免内存泄露
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constant.WHAT_COUNT_DOWN) {
                second--;
                if (second > 0) {
                    countDownProgressBar.countDown(pb_horizontal);
                } else {    // 倒数完毕
                    second = Constant.MAX_SECOND;
                    startActivity(new Intent(StartActivity.this, TabHostActivity.class));
                    finish();
                }
            }
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        countDownProgressBar = new CountDownProgressBar(mHandler);
        countDownProgressBar.countDown(pb_horizontal);
        ProtocolUtil.getInstance().getGameList();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
