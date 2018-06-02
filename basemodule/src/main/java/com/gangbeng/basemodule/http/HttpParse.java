package com.gangbeng.basemodule.http;


import com.gangbeng.basemodule.utils.GsonUtil;
import com.gangbeng.basemodule.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 转换服务器的json数据
 */

public class HttpParse {

    public static <T> T parseObject(String content, Class<T> mClass) {
        return GsonUtil.getGson().fromJson(content, mClass);
    }

    public static <T> List<T> parseArrayObject(String content, String key, Class<T> mClass) {
        try {
            JSONObject json = new JSONObject(content);
            String value = json.optString(key);
            Type type = new ListParameterizedType(mClass);
            return GsonUtil.getGson().fromJson(value, type);
        } catch (JSONException e) {
            LogUtil.t(e);
        }
        return null;
    }

    public static <T> List<T> parseArrayObject(String content, Class<T> mClass) {
        Type type = new ListParameterizedType(mClass);
        return GsonUtil.getGson().fromJson(content, type);
    }

}
