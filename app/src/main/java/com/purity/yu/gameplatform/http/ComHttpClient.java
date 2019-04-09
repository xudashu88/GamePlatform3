package com.purity.yu.gameplatform.http;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ComHttpClient {
    private static final String TAG = "ComHttpClient";
    public OkHttpClient client;

    public ComHttpClient() {
        init();
    }

    private static int READ_TIMEOUT = 30000;
    private static int CONNECT_TIMEOUT = 30000;
    private static int CONNECTION_POOL_MAX_IDEL = 20;
    private static int CONNECTION_POOL_KEEP_ALIVE = 20;

    // 忽略Https证书
    public void init() {
        ConnectionPool connectionPool = new ConnectionPool(CONNECTION_POOL_MAX_IDEL, CONNECTION_POOL_KEEP_ALIVE, TimeUnit.MINUTES);
        TrustAllManager trustAllManager = new TrustAllManager();
        this.client = new OkHttpClient().newBuilder()
                .connectionPool(connectionPool)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .sslSocketFactory(createTrustAllSSLFactory(trustAllManager), trustAllManager)
                .hostnameVerifier(createTrustAllHostnameVerifier())
                .retryOnConnectionFailure(true).build();
    }

    protected SSLSocketFactory createTrustAllSSLFactory(TrustAllManager trustAllManager) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{trustAllManager}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        return ssfFactory;
    }

    //获取HostnameVerifier
    protected HostnameVerifier createTrustAllHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    private void requestPost(HashMap<String, String> paramsMap) {
        try {
            String baseUrl = "http://43.249.206.212/api/deposit/apply";
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            // 请求的参数转换为byte数组
            byte[] postData = params.getBytes();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();
            dos.close();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 0) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream());
                System.out.println("Post方式请求成功===" + result);
                Log.e(TAG, "Post方式请求成功，result--->" + result);
            } else {
                Log.e(TAG, "Post方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        FormBody formBody = new FormBody
                .Builder()
                .add("token", "QrL5LCJMs7aUjS9nJfRmReOWKQbFbuHc_1543803838")
                .add("pay_channel", "1")
                .add("pay_info", "22")
                .add("pay_username", "111111")
                .add("pay_nickname", "pay_nickname")
                .add("apply_amount", "555")
//                .add("type", "0")
                .build();

        Request request = new Request.Builder()
//                .url("http://103.231.167.85:8888/user/login").post(formBody)
//                .url(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE)++ Constant.LOGIN).post(formBody)
//                .url("https://103.231.167.85:8888/apidoc/index.html").post(formBody)
//                .url("http://43.249.206.212/api/login").post(formBody)
                .url("http://43.249.206.212/api/deposit/apply").post(formBody)

                .build();
//        Call call = new ComHttpClient().client.newCall(request);
////        if(call.execute().code()==0){
//        Response response = call.execute();
//        // call.execute().body().string()
//        System.out.println(response.body().string());
////        }else {
////            System.out.println("未连接");
////        }
        OkHttpClient okHttpClient = new OkHttpClient();
        //Form表单格式的参数传递
        FormBody formBody1 = new FormBody.Builder()
//                .add("token", "Uet7mBzvq0on7yMqYi6CKq47qGBmh6Zw_1543835364")
                .add("pay_channel", "1")
                .add("pay_info", "22")
                .add("pay_username", "111111")
                .add("pay_nickname", "pay_nickname")
                .add("apply_amount", "555")
                .build();
        Request request1 = new Request.Builder().post(formBody1)//Post请求的参数传递
                .url("http://43.249.206.212/api/deposit/apply?token=VvYPMkp8owdFolVqSdnlR3j-zGt7wt6J_1543803683")
                .build();

//        FormBody formBody1 = new FormBody.Builder().add("username", "111111")
//                .add("password", "111111")
//                .build();
//        Request request1 = new Request.Builder().post(formBody1)//Post请求的参数传递
//                .url("http://www.wanandroid.com/user/login")
//                .build();
        okHttpClient.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //此方法运行在子线程中，不能在此方法中进行UI操作。
                String result = response.body().string();
                System.out.println(result);
                response.body().close();
            }
        });
    }
}