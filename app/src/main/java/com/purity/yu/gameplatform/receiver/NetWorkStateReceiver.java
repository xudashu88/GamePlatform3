package com.purity.yu.gameplatform.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.widget.NetStateDialog;

/**
 * 监听网络连接状态
 * Created by yucaihua on 2018/5/18.
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

        } else {
            boolean success = false;
            /**
             * 获得网络连接服务
             */
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo.State state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (NetworkInfo.State.CONNECTED == state) {
                success = true;
            }
            state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            if (NetworkInfo.State.CONNECTED == state) {
                success = true;
            }
            if (!success) {
//                netStateDialog(mContext.getResources().getString(R.string.network_not_available), success);
                final NetStateDialog myDialog = new NetStateDialog(context.getApplicationContext());
                myDialog.setContent(context.getResources().getString(R.string.network_not_available));
                myDialog.show();
                myDialog.setAttributes();//需先显示，然后才能查找控件
                myDialog.setOnPositiveListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.exit(0);
                        myDialog.dismiss();
                    }
                });
                myDialog.setOnNegativeListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myDialog.dismiss();
                    }
                });
            } else {
//                Toast.makeText(context,"网络连接成功",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void netStateDialog(String hint, final boolean success) {

    }
}
