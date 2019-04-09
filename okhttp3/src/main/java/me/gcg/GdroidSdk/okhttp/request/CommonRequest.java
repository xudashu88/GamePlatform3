package me.gcg.GdroidSdk.okhttp.request;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebSettings;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by gechuanguang on 2017/2/17.
 * 邮箱：1944633835@qq.com
 *
 * @function 接收请求参数，为我们生成Request对象
 */
public class CommonRequest {
    /**
     * @param url
     * @param params
     * @return 返回一个创建好post的Request对象
     */
    public static Request createPostRequest(String url, RequestParams params) {

        FormBody.Builder mFormBodybuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                // 将请求参数逐一添加到请求体中
                mFormBodybuilder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody mFormBody = mFormBodybuilder.build();
        Request request;
        if (url.contains("http://")) {
            request = new Request.Builder()
                    .url(url)
                    .post(mFormBody)
                    .build();
            if (mFormBody.size() == 3) {
                Log.i("对接皇家", "url=" + request.toString() + "---" + mFormBody.encodedName(2) + ":" + mFormBody.encodedValue(2) + "---" + mFormBody.encodedName(0) + ":" + mFormBody.encodedValue(0) + "---" + mFormBody.encodedName(1) + ":" + mFormBody.encodedValue(1));
            } else if (mFormBody.size() == 2) {
                Log.i("对接皇家", "url=" + request.toString() + "---" + mFormBody.encodedName(0) + ":" + mFormBody.encodedValue(0) + "---" + mFormBody.encodedName(1) + ":" + mFormBody.encodedValue(1));
            }
            return request;
        } else {
            request = new Request.Builder()
                    .url("http://" + url)
                    .post(mFormBody)
                    .build();
            if (mFormBody.size() == 3) {
                Log.i("对接皇家", "url=" + request.toString() + "---" + mFormBody.encodedName(2) + ":" + mFormBody.encodedValue(2) + "---" + mFormBody.encodedName(0) + ":" + mFormBody.encodedValue(0) + "---" + mFormBody.encodedName(1) + ":" + mFormBody.encodedValue(1));
            } else if (mFormBody.size() == 2) {
                Log.i("对接皇家", "url=" + request.toString() + "---" + mFormBody.encodedName(0) + ":" + mFormBody.encodedValue(0) + "---" + mFormBody.encodedName(1) + ":" + mFormBody.encodedValue(1));
            }
        }
        return request;
    }

    public static Request createPostRequest(String url, RequestParams params, Context context) {

        FormBody.Builder mFormBodybuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                // 将请求参数逐一添加到请求体中
                mFormBodybuilder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody mFormBody = mFormBodybuilder.build();
        Request request;
        request = new Request.Builder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", getUserAgent(context))
                .url(url)
                .post(mFormBody)
                .build();
        if (mFormBody.size() == 3) {
            Log.i("对接皇家", "url=" + request.toString() + "---" + mFormBody.encodedName(2) + ":" + mFormBody.encodedValue(2) + "---" + mFormBody.encodedName(0) + ":" + mFormBody.encodedValue(0) + "---" + mFormBody.encodedName(1) + ":" + mFormBody.encodedValue(1));
        } else if (mFormBody.size() == 2) {
            Log.i("对接皇家", "url=" + request.toString() + "---" + mFormBody.encodedName(0) + ":" + mFormBody.encodedValue(0) + "---" + mFormBody.encodedName(1) + ":" + mFormBody.encodedValue(1));
        }
        return request;
    }

    private static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @param url
     * @param params
     * @return 返回一个创建好get的Request对象
     */
    public static Request createGetRequest(String url, RequestParams params) {

        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                // 将请求参数逐一添加到请求体中
                urlBuilder.append(entry.getKey()).append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        }

        Request request;
        Log.i("对接皇家错误", "url=" + urlBuilder.substring(0, urlBuilder.length() - 1));
        if (urlBuilder.substring(0, urlBuilder.length() - 1).contains("http://")) {
            request = new Request.Builder()
                    .url(urlBuilder.substring(0, urlBuilder.length() - 1))
                    .get()
                    .build();
            Log.i("对接皇家", "url=" + request.toString());
            return request;
        } else {
            request = new Request.Builder()
                    .url("http://" + urlBuilder.substring(0, urlBuilder.length() - 1))
                    .get()
                    .build();
            Log.i("对接皇家", "url=" + request.toString());
        }
        return request;
    }

    /**
     * 文件上传请求
     *
     * @return
     */
    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    public static Request createMultiPostRequest(String url, RequestParams params) {

        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE, (File) entry.getValue()));
                } else if (entry.getValue() instanceof String) {

                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null, (String) entry.getValue()));
                }
            }
        }
        return new Request.Builder()
                .url(url)
                .post(requestBody.build())
                .build();
    }
}
