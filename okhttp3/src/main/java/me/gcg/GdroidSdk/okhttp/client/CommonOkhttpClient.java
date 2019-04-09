package me.gcg.GdroidSdk.okhttp.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import me.gcg.GdroidSdk.okhttp.HttpsUtils;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.response.CommonFileCallback;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Created by gechuanguang on 2017/2/17.
 * 邮箱：1944633835@qq.com
 *
 * @function 创建okHttpclient对象，并且配置支持https，以及发送请求
 */
public class CommonOkhttpClient {

    private static final int TIME_OUT = 800;//连接超时时间 原来是30ms 改为60ms
    private static OkHttpClient mOkHttpClient = null;
    private static ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    //为mOkHttpClient去配置参数  类加载的时候开始创建静态代码块，并且只执行一次
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.cookieJar(new CookieJar() {//不传token，传请求回来的COOKIE信息
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });
        okHttpClientBuilder.followRedirects(true); //设置重定向 其实默认也是true

        /*--添加请求头  这个看个人需求 --*/
//        okHttpClientBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request()
//                        .newBuilder()
//                        .addHeader("User-Agent", "Android—Mobile") // 标明发送本次请求的客户端
//                        .build();
//                return chain.proceed(request);
//            }
//        });

        //添加https支持
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        /**
         * trust all the https point
         */
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        mOkHttpClient = okHttpClientBuilder.build();
    }

    /**
     * 发送具体的http/https的请求
     *
     * @param request
     * @param commonCallback
     * @return Call
     */
    public static Call sendRequest(Request request, CommonJsonCallback commonCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commonCallback);
        return call;
    }

    /**
     * 发送具体的http/https的请求
     *
     * @param request
     * @param commonCallback
     * @return Call
     */
    public static Call sendRequest(Request request, Callback commonCallback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commonCallback);
        return call;
    }


    /**
     * 文件下载
     *
     * @param request
     * @param handle
     * @return
     */
    public static Call downloadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }
}
