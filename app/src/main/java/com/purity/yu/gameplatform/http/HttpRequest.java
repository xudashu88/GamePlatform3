package com.purity.yu.gameplatform.http;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.gangbeng.basemodule.http.OkHttpBuilder;
import com.gangbeng.basemodule.http.OkHttpManger;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yucaihua 2018.11.24
 */

public class HttpRequest {

    public static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static OkHttpBuilder request(String key) {
        OkHttpBuilder builder = OkHttpManger.getInstance().getBuilder().url(key);
        LogUtil.i("请求完整路径=" + key);
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
            ToastUtil.show(mContext, "连接超时");
            LogUtil.i("请求异常=" + request.toString() + " 异常信息=" + e.getMessage());
        }

        @Override
        @CallSuper
        public void onSuccess(String s) {
            onResultFinish(s);
            String data;
            try {
                JSONObject jsonObject = new JSONObject(s);
                int status = jsonObject.optInt("code");//todo status
                data = jsonObject.optString("data");
                if (status == 0) { //todo 原来200
                    onResultOk(s);
                } /*else if (status == 403) {
                    LogUtil.e(status + "  未登录");
                    ToastUtil.show(mContext, "请登录");
                } */ else {
                    onResultFault(status, data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public abstract void onResultOk(String result);

        public void onResultFault(int code, String hint) {
            ToastUtil.show(mContext, hint);
            LogUtil.i("请求失败= code" + code + " hint=" + hint);
        }

        public void onResultFinish(String result) {
            LogUtil.i("请求完成=" + result);
        }

    }

}
