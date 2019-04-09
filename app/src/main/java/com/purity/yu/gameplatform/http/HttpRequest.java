package com.purity.yu.gameplatform.http;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.gangbeng.basemodule.http.OkHttpBuilder;
import com.gangbeng.basemodule.http.OkHttpManger;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
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
        OkHttpBuilder builder = OkHttpManger.getInstance().getBuilder().url(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + key);

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
            ToastUtil.show(mContext, "当前无网络，请连接后再试");
            return;
        }

        @Override
        @CallSuper
        public void onSuccess(String s) {
            onResultFinish(s);
            String data = null;
            try {
                JSONObject jsonObject = new JSONObject(s);
                int status = jsonObject.optInt("code");//todo status
                data = jsonObject.optString("data");
                if (status == 0) {//todo 原来200
                    LogUtil.v(LogUtil.jsonToLog(s));
                    onResultOk(s);
                } /*else if (status == 403) {
                    LogUtil.e(status + "  未登录");
                    ToastUtil.show(mContext, "请登录");
                } */ else {
                    onResultFault("", "" + data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public abstract void onResultOk(String result);

        public void onResultFault(String code, String hint) {
            ToastUtil.show(mContext, hint);
        }

        public void onResultFinish(String result) {
        }

    }

}
