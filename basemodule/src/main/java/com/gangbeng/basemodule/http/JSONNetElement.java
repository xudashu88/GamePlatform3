package com.gangbeng.basemodule.http;

import com.squareup.okhttp.MultipartBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/*
* json元素
* */
public class JSONNetElement extends NetElement {
    String key;
    Object value;

    public JSONNetElement(String key, Object value) {
        this.key = key;
        this.value = value;
        if(!(value instanceof JSONObject) && !(value instanceof JSONArray)) {
            new Throwable("参数只能为JSONObject或JSONArray");
        }

    }

    public void parseJSON(JSONObject object) throws JSONException {
        object.put(this.key, this.value);
    }

    public void parseFormat(MultipartBuilder builder) {
        builder.addFormDataPart(this.key, this.value.toString());
    }

    public String parseValue() {
        return null;
    }

    public String toString() {
        return this.key + " = " + this.value.toString();
    }
}