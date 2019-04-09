package com.purity.yu.gameplatform.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.chuangku.languagemodule.MultiLanguageUtil;
import com.mob.MobSDK;
import com.purity.yu.gameplatform.http.HttpRequest;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.widget.DpUtil;
import com.tencent.smtt.sdk.QbSdk;

public class GlobeApplication extends Application {
    public static Application mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        DpUtil.init(this);
        HttpRequest.init(this);
        MultiLanguageUtil.init(this);
        BaccaratUtil.getInstance().init(this);
        ProtocolUtil.getInstance().init(this);
        MobSDK.init(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
                    @Override
                    public void onCoreInitFinished() {
                    }
                    @Override
                    public void onViewInitFinished(boolean b) {
                    }
                };
                QbSdk.initX5Environment(getApplicationContext(),cb);

            }
        }).start();
    }

    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }

}