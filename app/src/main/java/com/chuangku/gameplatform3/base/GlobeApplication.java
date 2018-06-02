package com.chuangku.gameplatform3.base;

import android.app.Application;

import com.chuangku.gameplatform3.http.HttpRequest;
import com.chuangku.languagemodule.MultiLanguageUtil;
import com.gangbeng.basemodule.utils.ToastUtil;

public class GlobeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
        HttpRequest.init(this);
        MultiLanguageUtil.init(this);
//        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);            // 初始化 JPush
//        JPushInterface.getRegistrationID(getApplicationContext());
//        LogUtil.i("JPUSHID"+ JPushInterface.getRegistrationID(getApplicationContext()));
        /* Bugly SDK初始化
         * 参数1：上下文对象
         * 参数2：APPID，平台注册时得到,注意替换成你的appId
         * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
         */
//        Bugly.init(getApplicationContext(), "a0ec9a309b", false);
    }
}