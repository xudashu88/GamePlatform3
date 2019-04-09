package com.purity.yu.gameplatform.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class PhoneBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果是拨打电话
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            //拨打电话会优先,收到此广播. 再收到 android.intent.action.PHONE_STATE 的 TelephonyManager.CALL_STATE_OFFHOOK 状态广播;

            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

        } else {
            // 如果是来电
            TelephonyManager tManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            //电话的状态
            switch (tManager.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    //等待接听状态
                    Intent intent1 = new Intent();
                    intent1.setAction("MUSIC_STOP");
                    context.sendBroadcast(intent1);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //接听状态

                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //挂断状态
                    Intent intent3 = new Intent();
                    intent3.setAction("MUSIC_START");
                    context.sendBroadcast(intent3);
                    break;
            }
        }
    }
}

