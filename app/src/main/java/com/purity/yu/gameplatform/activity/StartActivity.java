package com.purity.yu.gameplatform.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.widget.CountDownProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

//@ContentView(R.layout.activity_start)
public class StartActivity extends AppCompatActivity {

    @BindView(R.id.pb_horizontal)
    ProgressBar pb_horizontal;
    @BindView(R.id.iv_start)
    ImageView iv_start;
    private Context mContext;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.VERSION) == 1) {
            setContentView(R.layout.activity_start);
        } else {
            setContentView(R.layout.activity_start_i18n);
        }
        mContext = StartActivity.this;
        ButterKnife.bind(this);
        countDownProgressBar = new CountDownProgressBar(mHandler);
        countDownProgressBar.countDown(pb_horizontal);
        ProtocolUtil.getInstance().getGameList();
        if (SharedPreUtil.getInstance(mContext).getInt(Constant.VERSION) == 1) {
            iv_start.setBackgroundResource(R.drawable.pic_bg);
        } else {
            iv_start.setBackgroundResource(R.drawable.start_bg);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
