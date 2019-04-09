package com.purity.yu.gameplatform.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chuangku.languagemodule.MultiLanguageUtil;
import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.google.gson.Gson;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.StartActivity;
import com.purity.yu.gameplatform.annotation.ContentViewUtils;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.GamePlay;
import com.purity.yu.gameplatform.entity.GameRoom;
import com.purity.yu.gameplatform.entity.Games;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.PermissionsUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import avi.AVLoadingIndicatorView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.request.RequestParams;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;

//@ContentView(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.et_user)
    EditText et_user;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_invitation_code)
    EditText et_invitation_code;
    @BindView(R.id.tv_register)
    TextView tv_register;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_tv_service_web)
    TextView tv_tv_service_web;
    @BindView(R.id.et_ip)
    EditText et_ip;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private Context mContext;
    private String user;
    private String pwd;
    private String ip;
    private String promo_code;
    private long lastOnClickTime = 0;
    private DisplayMetrics displayMetrics;
    List<GameRoom> gameRoomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = RegisterActivity.this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        displayMetrics = mContext.getResources().getDisplayMetrics();
        ContentViewUtils.inject(this);
        gameRoomList = new ArrayList<>();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        if (width > height) {//横屏
            setContentView(R.layout.activity_register_sw);
        } else
            setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Util.getInstance().init(this);
        Util.getInstance().hideSystemNavigationBar();
        PermissionsUtil.checkAndRequestPermissions(this);
        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL))) {
            et_ip.setText(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL));
            ip = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL);
            et_invitation_code.setText(SharedPreUtil.getInstance(mContext).getString(Constant.PROMO_CODE));
        }
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()) {
                    long time = SystemClock.uptimeMillis();
                    if (time - lastOnClickTime <= 5000) {
                        return;
                    } else {
                        lastOnClickTime = time;
                        avi.show();
                        postRegister();
                    }
                }
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }
        });
        tv_tv_service_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String data = "http://103.231.167.85:8888/mobile/index.html";
                intent.setData(Uri.parse(data));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

    private void postRegister() {
        //12346
        final String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(ip) + ServiceIpConstant.BASE_AFTER;
        HashMap<String, String> map = new HashMap<>();
        map.put("username", user);
        map.put("password", pwd);
        map.put("promo_code", et_invitation_code.getText().toString());
        map.put("deviceid", ProtocolUtil.getInstance().getDeviceId());
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(baseUrl + Constant.REGISTER, new RequestParams(map)),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject json = null;
                            JSONObject __data = null;
                            json = new JSONObject(result);
                            LogUtil.i("注册 postRegister=" + json);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code == 400) {
                                ToastUtil.show(mContext, data);
                                avi.hide();
                                return;
                            }
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_PWD, pwd);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_NAME, user);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN, data);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.PROMO_CODE, promo_code);
                            avi.hide();
                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.register_succeeded));
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE_URL, et_ip.getText().toString());
                            String _ip = BaccaratUtil.getInstance().changeIp(et_ip.getText().toString());
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE, ServiceIpConstant.BASE_BEFORE + _ip + "/api");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant._BASE, ServiceIpConstant.BASE_BEFORE + _ip + ":8888/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_LOBBY, ServiceIpConstant.BASE_BEFORE + _ip + ":9081/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_ROOM, ServiceIpConstant.BASE_BEFORE + _ip + ":9082/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_PRODICT, ServiceIpConstant.BASE_BEFORE + _ip + ":9080/");

                            mContext.startActivity(new Intent(mContext, StartActivity.class));
                            ((Activity) mContext).finish();
                            postLoginInfo();
                        } catch (Exception e) {
                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.ip_invalid));
                            avi.hide();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE_URL, "");
                        ToastUtil.show(mContext, "连接超时");
                        avi.hide();
                    }
                })));
    }

    private boolean verify() {
        user = et_user.getText().toString();
        pwd = et_password.getText().toString();
//        ip = et_ip.getText().toString();
        ip = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE_URL);
        promo_code = et_invitation_code.getText().toString();
        if (TextUtils.isEmpty(user.trim())) {
            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.username_not_null));
            return false;
        }
        if (TextUtils.isEmpty(pwd.trim())) {
            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.password_not_null));
            return false;
        }
//        if (TextUtils.isEmpty(ip)) {
//            ToastUtil.showToast(mContext, mContext.getResources().getString(R.string.ip_alert));
//            return false;
//        }
        return true;
    }

    private void postLoginInfo() {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        //上分，-1不上分，0上所有的分，>0 上具体分数
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(base + Constant.LOGIN_INFO + "?token=" + token + "&game_type=jxb&amount=-1", null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");
                            LogUtil.i("注册 postLoginInfo=" + json);
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail_jxb);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            String data = json.optString("data");
                            __data = new JSONObject(data);
                            String token = __data.optString("token");
                            String userId = __data.optString("userid");
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN_SOCKET, token);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_ID, userId);
                            getGameRoomCount(token, userId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        ToastUtil.show(mContext, "连接超时");
                    }
                })));
    }

    private void getGameRoomCount(final String token, final String userId) {
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant._BASE) + Constant.LOGIN_VALIDATE + "?auth=" + token + "&userid=" + userId, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        JSONObject __user = null;

                        try {
                            json = new JSONObject(s);
                            LogUtil.i("注册 getGameRoomCount=" + json);
                            int code = json.optInt("code");
                            if (code == -1) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            List<Games> gamesList = new ArrayList<>();
                            gamesList.clear();
                            JSONArray data_array = __data.getJSONArray("games");
                            for (int i = 0; i < data_array.length(); i++) {
                                Games games = new Games();
                                JSONObject _json = data_array.getJSONObject(i);
                                String gameName = _json.optString("gameName");
                                games.gameName = gameName;
                                JSONArray _gamePlays = _json.getJSONArray("gamePlays");
                                for (int j = 0; j < _gamePlays.length(); j++) {
                                    GamePlay gamePlay = new GamePlay();
                                    JSONObject _gamePlays_json = _gamePlays.getJSONObject(j);
                                    int minCoin = _gamePlays_json.optInt("minCoin");
                                    gamePlay.minCoin = minCoin;
                                    games.gamePlays.add(gamePlay);
                                    JSONArray _gameRooms = _gamePlays_json.getJSONArray("gameRooms");
                                    for (int k = 0; k < _gameRooms.length(); k++) {
                                        GameRoom gameRoom = new GameRoom();
                                        JSONObject _gameRooms_json = _gameRooms.getJSONObject(k);
                                        String _id = _gameRooms_json.optString("id");
                                        String name = _gameRooms_json.optString("name");
                                        int status = _gameRooms_json.optInt("status");
                                        int betSecond = _gameRooms_json.optInt("betSecond");
                                        String _code = _gameRooms_json.optString("code");
                                        String minBet = _gameRooms_json.optString("minBet");
                                        String maxBet = _gameRooms_json.optString("maxBet");
                                        String betLimit = _gameRooms_json.optString("betLimit");
                                        String roomConfig = _gameRooms_json.optString("roomConfig");
                                        JSONObject _roomConfig = new JSONObject(roomConfig);
                                        String lan_rtmpAddress = _roomConfig.optString("lan_rtmpAddress");
                                        String[] lan_rtmpAddressArr = new Gson().fromJson(lan_rtmpAddress, String[].class);
                                        String dx_rtmpAddress = _roomConfig.optString("dx_rtmpAddress");
                                        String[] dx_rtmpAddressArr = new Gson().fromJson(dx_rtmpAddress, String[].class);
                                        String yd_rtmpAddress = _roomConfig.optString("yd_rtmpAddress");
                                        String[] yd_rtmpAddressArr = new Gson().fromJson(yd_rtmpAddress, String[].class);
                                        gameRoom.rtmp = lan_rtmpAddressArr;
                                        gameRoom.dxRtmp = dx_rtmpAddressArr;
                                        gameRoom.ydRtmp = yd_rtmpAddressArr;
                                        int[] minBetInt = new Gson().fromJson(minBet, int[].class);
                                        int[] maxBetInt = new Gson().fromJson(maxBet, int[].class);
                                        List<Integer> minBetList = new ArrayList<>();
                                        List<Integer> maxBetList = new ArrayList<>();
                                        for (int a = 0; a < minBetInt.length; a++) {
                                            minBetList.add(minBetInt[a]);
                                        }
                                        for (int a = 0; a < maxBetInt.length; a++) {
                                            maxBetList.add(maxBetInt[a]);
                                        }
                                        gameRoom.minBet.clear();
                                        gameRoom.maxBet.clear();
                                        gameRoom.minBet.addAll(minBetList);
                                        gameRoom.maxBet.addAll(maxBetList);
                                        gameRoom.id = _id;
                                        gameRoom.name = name;
                                        gameRoom.status = status;
                                        gameRoom.code = _code;
                                        gameRoom.betLimit = betLimit;
                                        gameRoom.betSecond = betSecond;
                                        gameRoomList.add(gameRoom);
                                    }
                                }
                                gamesList.add(games);
                            }
                            getGameRoomCount();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        ToastUtil.show(mContext, "连接超时");

                    }
                })));
    }

    private void getRoomCount() {
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant._BASE) + Constant.ROOM_COUNT + "?code=catchFish", null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");
                            if (code == -1) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            int _data = json.optInt("data");
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.FISH_ROOM_COUNT, _data);
                            getGameRoomCount();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        ToastUtil.show(mContext, "连接超时");

                    }
                })));
    }

    private void getGameRoomCount() {
        int bacCount = 0, dtCount = 0, singleCount = 0, catchFish = 0;
        List<Baccarat> baccaratList = new ArrayList<>();
        List<Baccarat> dtList = new ArrayList<>();
        List<Baccarat> singleList = new ArrayList<>();
        if (gameRoomList.size() > 0) {
            for (int i = 0; i < gameRoomList.size(); i++) {
                if (gameRoomList.get(i).code.equals("baccarat")) {
                    bacCount++;
                    Baccarat baccarat = new Baccarat();
                    baccarat.roomId = gameRoomList.get(i).id;
                    baccarat.roomName = gameRoomList.get(i).name;
                    baccarat.rtmp = gameRoomList.get(i).rtmp;
                    baccarat.ydRtmp = gameRoomList.get(i).ydRtmp;
                    baccarat.dxRtmp = gameRoomList.get(i).dxRtmp;
                    baccarat.betSecond = gameRoomList.get(i).betSecond;
                    if (gameRoomList.get(i).minBet != null) {
                        baccarat.minBetList.addAll(gameRoomList.get(i).minBet);
                        baccarat.maxBetList.addAll(gameRoomList.get(i).maxBet);
                    }
                    baccaratList.add(baccarat);
                } else if (gameRoomList.get(i).code.equals("dragonTiger")) {
                    dtCount++;
                    Baccarat baccarat = new Baccarat();
                    baccarat.roomId = gameRoomList.get(i).id;
                    baccarat.roomName = gameRoomList.get(i).name;
                    baccarat.rtmp = gameRoomList.get(i).rtmp;
                    baccarat.ydRtmp = gameRoomList.get(i).ydRtmp;
                    baccarat.dxRtmp = gameRoomList.get(i).dxRtmp;
                    baccarat.betSecond = gameRoomList.get(i).betSecond;
                    if (gameRoomList.get(i).minBet != null) {
                        baccarat.minBetList.addAll(gameRoomList.get(i).minBet);
                        baccarat.maxBetList.addAll(gameRoomList.get(i).maxBet);
                    }
                    dtList.add(baccarat);
                } else if (gameRoomList.get(i).code.equals("duel")) {
                    singleCount++;
                    Baccarat baccarat = new Baccarat();
                    baccarat.roomId = gameRoomList.get(i).id;
                    baccarat.roomName = gameRoomList.get(i).name;
                    baccarat.rtmp = gameRoomList.get(i).rtmp;
                    baccarat.ydRtmp = gameRoomList.get(i).ydRtmp;
                    baccarat.dxRtmp = gameRoomList.get(i).dxRtmp;
                    baccarat.betSecond = gameRoomList.get(i).betSecond;
                    if (gameRoomList.get(i).minBet != null) {
                        baccarat.minBetList.addAll(gameRoomList.get(i).minBet);
                        baccarat.maxBetList.addAll(gameRoomList.get(i).maxBet);
                    }
                    singleList.add(baccarat);
                } else {
                    catchFish++;
                }
            }
        }
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_ROOM_COUNT, bacCount);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.DT_ROOM_COUNT, dtCount);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.SINGLE_ROOM_COUNT, singleCount);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.FISH_ROOM_COUNT, catchFish);
//        getRoomCount();//獲取打魚的房間數
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_BAC_LIST, new Gson().toJson(baccaratList));
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_DT_LIST, new Gson().toJson(dtList));
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_SINGLE_LIST, new Gson().toJson(singleList));
    }
}
