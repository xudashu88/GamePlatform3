package com.chuangku.gameplatform3.http;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.chuangku.gameplatform3.BuildConfig;
import com.chuangku.gameplatform3.base.Constant;
import com.gangbeng.basemodule.http.OkHttpBuilder;
import com.gangbeng.basemodule.http.OkHttpManger;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kang on 2016/11/10.
 */

public class HttpRequest {

    public static Context mContext;
    public static final String IMAGESITE = BuildConfig.ImageSite;

    public static void init(Context context) {
        mContext = context;
    }

    public static OkHttpBuilder request(String key) {
        OkHttpBuilder builder = OkHttpManger.getInstance().getBuilder().url(BuildConfig.Site);
        builder.addParam("do", key);
        return builder;
    }

    public static OkHttpBuilder requestWithUser(String key, Context context) {
        OkHttpBuilder builder = request(key);
        builder.addParam("user_id", Util.getId(context));
        builder.addParam("token", Util.getToken(context));
        return builder;
    }

    public abstract static class HttpCallBack extends OkHttpManger.ResultCallback {
        @Override
        @CallSuper
        public void onFailure(Request request, Exception e) {
//            onResultFinish(e.getMessage());
//            onResultFault("12345", "网络连接失败，请检查您的网络");
            ToastUtil.show("当前无网络，请连接后再试");
            return;
        }

        @Override
        @CallSuper
        public void onSuccess(String s) {
            onResultFinish(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 200) {
                    LogUtil.v(LogUtil.jsonToLog(s));
                    onResultOk(s);
                } else if (status == 403) {
                    LogUtil.e(status + "  未登录");
                    ToastUtil.show("请登录");
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_ID, "");
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN, "");
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_LEVEL, "");


//                    HomePageRegisterControl.getInstance().init(mContext);
//                    HomePageRegisterControl.getInstance().initLogin();
                } else {
                    String info = jsonObject.getString("info");
                    onResultFault(String.valueOf(status), info);
                }
            } catch (JSONException e) {
                onResultFault("123456", "服务器繁忙" + s);
                e.printStackTrace();
            }
        }

        public abstract void onResultOk(String result);

        public void onResultFault(String code, String hint) {
            LogUtil.e("onResultFault:" + code + "   " + hint);
            ToastUtil.show(hint);
        }

        public void onResultFinish(String result) {
        }

    }

}
