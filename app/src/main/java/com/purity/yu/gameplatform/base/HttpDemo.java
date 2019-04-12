package com.purity.yu.gameplatform.base;

import com.purity.yu.gameplatform.http.HttpRequest;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;

import org.json.JSONObject;

import java.util.HashMap;

import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.request.RequestParams;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;

public class HttpDemo {

    private void requestGet() {
        String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp("et_ip") + ServiceIpConstant.BASE_AFTER;
        HttpRequest.request(baseUrl + Constant.APP_INFO)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void requestPost() {
        String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp("et_ip") + ServiceIpConstant.BASE_AFTER;
        HttpRequest.request(baseUrl + Constant.LOGIN)
                .addParam("username", "username")
                .addParam("password", "password")
                .addParam("deviceid", ProtocolUtil.getInstance().getDeviceId())
                .executeFormPost(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        try {
                            JSONObject json = null;
                            JSONObject __data = null;
                            json = new JSONObject(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void createPostRequest() {
        HashMap<String, String> map = new HashMap<>();
        map.put("apply_amount", "");
        map.put("user_bank_id", "");
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest("url", new RequestParams(map)),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code == 0) {
                            } else {
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        System.out.println("连接超时");
                    }
                })));
    }

    private void createGetRequest() {
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest("url", null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code == 0) {
                            } else {
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        System.out.println("连接超时");
                    }
                })));
    }
}
