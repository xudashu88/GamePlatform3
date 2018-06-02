package com.gangbeng.basemodule.http;

import com.squareup.okhttp.MultipartBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class NetElement {
    public NetElement() {
    }

    public abstract void parseJSON(JSONObject var1) throws JSONException;

    public abstract void parseFormat(MultipartBuilder var1);

    public abstract String parseValue();
}