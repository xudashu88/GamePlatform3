package com.gangbeng.basemodule.http;

import com.gangbeng.basemodule.utils.LogUtil;
import com.squareup.okhttp.Request;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OkHttpBuilder {
    String url;
    Map<String, List<String>> heads = new HashMap();
    List<NetElement> elementList = new ArrayList();
    private Object bindParam = null;
    OkHttpBuilder.RequestLife requestLife;
    Object liftParam;
    boolean hasEncrypt = false;
    String secrect;

    public OkHttpBuilder() {
    }

    public OkHttpBuilder addParamArray(String key, String... value) {
        JSONArray array = new JSONArray();
        String[] var4 = value;
        int var5 = value.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String f = var4[var6];
            array.put(f);
        }

        this.addParamObject(key, new Object[]{array});
        return this;
    }

    public OkHttpBuilder addHead(String key, String... value) {
        if(this.heads.get(key) == null) {
            this.heads.put(key, new ArrayList());
        }

        ((List)this.heads.get(key)).addAll(Arrays.asList(value));
        return this;
    }

    public OkHttpBuilder addParam(String key, int... value) {
        int[] var3 = value;
        int var4 = value.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            int f = var3[var5];
            this.elementList.add(new PrimitiveNetElement(key, f));
        }
        return this;
    }

    public OkHttpBuilder addParam(String key, String... value) {
        String[] var3 = value;
        int var4 = value.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String f = var3[var5];
            this.elementList.add(new PrimitiveNetElement(key, f));
        }
        return this;
    }

    public OkHttpBuilder addParamObject(String key, Object... value) {
        Object[] var3 = value;
        int var4 = value.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Object f = var3[var5];
            this.elementList.add(new JSONNetElement(key, f));
        }

        return this;
    }

    public OkHttpBuilder addElement(NetElement element) {
        this.elementList.add(element);
        return this;
    }

    public OkHttpBuilder url(String url) {
        this.url = url;
        return this;
    }

    public OkHttpBuilder bindWithObject(Object object) {
        this.bindParam = object;
        return this;
    }

    public Object getBindParam() {
        return this.bindParam;
    }

    public void executeJsonPost(OkHttpManger.ResultCallback callback) {
        if(this.requestLife != null) {
            this.liftParam = this.requestLife.OnStart();
        }

        OkHttpManger.getInstance().executeJsonPost(this.url, this, new OkHttpBuilder.CallBack(callback));
    }

    public void executeFormPost(OkHttpManger.ResultCallback callback) {
        if(this.requestLife != null) {
            this.liftParam = this.requestLife.OnStart();
        }

        OkHttpManger.getInstance().enqueueFormPost(this.url, this, new OkHttpBuilder.CallBack(callback));
    }

    public void executeGetParams(OkHttpManger.ResultCallback callback) {
        if(this.requestLife != null) {
            this.liftParam = this.requestLife.OnStart();
        }

        OkHttpManger.getInstance().executeGet(this.url, this, new OkHttpBuilder.CallBack(callback));
    }

    public void setRequestLife(OkHttpBuilder.RequestLife requestLife) {
        this.requestLife = requestLife;
    }

    public boolean isHasEncrypt() {
        return this.hasEncrypt;
    }

    public void setHasEncrypt(boolean hasEncrypt) {
        this.hasEncrypt = hasEncrypt;
    }

    public String encrppt(String result) {
        if(!this.hasEncrypt) {
            return result;
        } else {
            try {
                Map e = null;
                e = AES.encrypt(result);
                this.secrect = (String)e.get("key");
                String key = RSA.encryptData((String)e.get("key"), Setting.RSAPublicKey);
                this.addHead("key", new String[]{URLEncoder.encode(key, "UTF-8")});
                return (String)e.get("date");
            } catch (Exception var4) {
                LogUtil.t("数据加密失败", var4);
                return "";
            }
        }
    }

    public String decrypt(String result) {
        return !this.hasEncrypt?result:AES.decrypt(this.secrect, result);
    }

    public String getUrl() {
        return this.url;
    }

    public Map<String, List<String>> getHeads() {
        return this.heads;
    }

    public List<NetElement> getElementList() {
        return this.elementList;
    }

    public interface RequestLife {
        Object OnStart();

        void OnFinish(Object var1, OkHttpBuilder var2, boolean var3, String var4);
    }

    private class CallBack extends OkHttpManger.ResultCallback {
        OkHttpManger.ResultCallback callback;

        public CallBack(OkHttpManger.ResultCallback callback) {
            this.callback = callback;
        }

        public void onFailure(Request request, Exception e) {
            if(this.callback != null) {
                if(OkHttpBuilder.this.requestLife != null) {
                    OkHttpBuilder.this.requestLife.OnFinish(OkHttpBuilder.this.liftParam, OkHttpBuilder.this, false, e.getMessage());
                }

                this.callback.onFailure(request, e);
            }

        }

        public void onSuccess(String content) {
            if(this.callback != null) {
                String result = OkHttpBuilder.this.decrypt(content);
                if(OkHttpBuilder.this.requestLife != null) {
                    OkHttpBuilder.this.requestLife.OnFinish(OkHttpBuilder.this.liftParam, OkHttpBuilder.this, true, result);
                }

                this.callback.onSuccess(result);
            }

        }
    }
}