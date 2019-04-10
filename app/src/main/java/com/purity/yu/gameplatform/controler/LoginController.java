package com.purity.yu.gameplatform.controler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.google.gson.Gson;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.SetLanguageActivity;
import com.purity.yu.gameplatform.activity.StartActivity;
import com.purity.yu.gameplatform.activity.SuggestionActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.GamePlay;
import com.purity.yu.gameplatform.entity.GameRoom;
import com.purity.yu.gameplatform.entity.Games;
import com.purity.yu.gameplatform.http.HttpRequest;
import com.purity.yu.gameplatform.login.KittyActivity;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.ProtocolUtil;
import com.purity.yu.gameplatform.widget.HintDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import avi.AVLoadingIndicatorView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.request.RequestParams;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;

/**
 * 登录
 * Created by Administrator on 2018/5/15.
 */

public class LoginController {
    private static final String TAG = "LoginController";
    private Context mContext;
    private static LoginController instance = null;
    TextView tv_version;
    ImageButton imgBtn_language;
    EditText et_user;
    EditText et_ip;
    LinearLayout ll_user_pwd;
    EditText et_password;
    TextView tv_login;
    TextView tv_register;
    AVLoadingIndicatorView avi;
    TextView tv_service_online;
    TextView tv_login_out;
    ImageView iv_wechat;
    private String username;
    private String pwd;
    private long lastOnClickTime = 0;
    private String ip;
    List<GameRoom> gameRoomList;

    public void init(Context context) {
        mContext = context;
        gameRoomList = new ArrayList<>();
    }

    public static LoginController getInstance() {
        if (null == instance) {
            instance = new LoginController();
        }
        return instance;
    }

    public void copyWidget(TextView tv_version, ImageButton imgBtn_language, EditText et_user, LinearLayout ll_user_pwd, EditText et_password,
                           TextView tv_login, TextView tv_register,
                           AVLoadingIndicatorView avi, EditText et_ip,
                           TextView tv_service_online, TextView tv_login_out, ImageView iv_wechat) {
        this.tv_version = tv_version;
        this.imgBtn_language = imgBtn_language;
        this.et_user = et_user;
        this.ll_user_pwd = ll_user_pwd;
        this.et_password = et_password;
        this.tv_login = tv_login;
        this.tv_register = tv_register;
        this.avi = avi;
        this.et_ip = et_ip;
        this.tv_service_online = tv_service_online;
        this.tv_login_out = tv_login_out;
        this.iv_wechat = iv_wechat;
        tv_version.setText("20190408");
        initEvent();
        if (!TextUtils.isEmpty(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME))) {
            et_user.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_NAME));
            et_password.setText(SharedPreUtil.getInstance(mContext).getString(Constant.USER_PWD));
        }
    }

    private void initEvent() {
        tv_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, KittyActivity.class));
            }
        });
        tv_service_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SuggestionActivity.class));
            }
        });
        tv_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolUtil.getInstance().postLogOutSys();
            }
        });
        imgBtn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, SetLanguageActivity.class));
                ((Activity) mContext).finish();
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()) {
                    long time = SystemClock.uptimeMillis();
                    if (time - lastOnClickTime <= 5000) {
                        return;
                    } else {
                        lastOnClickTime = time;
                        avi.show();
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.LOGIN_TYPE, 0);
                        postLoginIn(username, pwd, et_ip, avi, "");
                    }
                }
            }
        });
        iv_wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_ip.getText().toString())) {
                    hintDialog(mContext.getResources().getString(R.string.ip_alert));
                    return;
                } else
                    loginThird();
            }
        });
        et_ip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //转换成正确的ip
//                    if (changeIp()) return false;
                }
                return false;
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.默认可以显示注册 2.点击注册前获取商户号 3.通过APP_INFO的open_register为1可注册
                if (TextUtils.isEmpty(et_ip.getText().toString())) {
                    hintDialog(mContext.getResources().getString(R.string.ip_alert));
                    return;
                } else {
                    ProtocolUtil.getInstance().getAppInfo(et_ip);
                }
            }
        });
    }


    private boolean verify() {
        username = et_user.getText().toString();
        pwd = et_password.getText().toString();
        ip = et_ip.getText().toString();
        if (TextUtils.isEmpty(username)) {
            hintDialog(mContext.getResources().getString(R.string.error_code_4_iv));
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            hintDialog(mContext.getResources().getString(R.string.password_alert));
            return false;
        }
        if (TextUtils.isEmpty(ip)) {
            hintDialog(mContext.getResources().getString(R.string.ip_alert));
            return false;
        }
        return true;
    }

    public void postLoginIn(final String username, final String pwd, final EditText et_ip, final AVLoadingIndicatorView avi, final String nickname) {
        String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(et_ip) + ServiceIpConstant.BASE_AFTER;
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", pwd);
        map.put("deviceid", ProtocolUtil.getInstance().getDeviceId());
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(baseUrl + Constant.LOGIN, new RequestParams(map), mContext),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject json = null;
                            JSONObject __data = null;
                            json = new JSONObject(result);
                            LogUtil.i("登录1json" + json);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                if (data.equals("该账号已经登录")) {
                                    ToastUtil.show(mContext, data);
                                    avi.hide();
                                    return;
                                } else {
                                    ToastUtil.show(mContext, data);
                                    avi.hide();
                                    return;
                                }
                            }
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_PWD, pwd);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN, data);
                            LogUtil.i("微信方式" + SharedPreUtil.getInstance(mContext).getInt(Constant.LOGIN_TYPE));
                            if (SharedPreUtil.getInstance(mContext).getInt(Constant.LOGIN_TYPE) == 0) {
                                SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_NAME, username);
                            } else
                                SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_NAME, nickname);

                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.login_succeeded));
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE_URL, et_ip.getText().toString());
                            String _ip = BaccaratUtil.getInstance().changeIp(et_ip.getText().toString());
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE, ServiceIpConstant.BASE_BEFORE + _ip + "/api");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant._BASE, ServiceIpConstant.BASE_BEFORE + _ip + ":8888/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_LOBBY, ServiceIpConstant.BASE_BEFORE + _ip + ":9081/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_ROOM, ServiceIpConstant.BASE_BEFORE + _ip + ":9082/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_PRODICT, ServiceIpConstant.BASE_BEFORE + _ip + ":9080/");
                            avi.hide();
                            mContext.startActivity(new Intent(mContext, StartActivity.class));
                            ((Activity) mContext).finish();
                            postLoginInfo();
                        } catch (Exception e) {
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

    public void postLoginInfo() {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        //上分，-1不上分，0上所有的分，>0 上具体分数
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        LogUtil.i("获取登陆信息=" + base + Constant.LOGIN_INFO + "?token=" + token + "&game_type=jxb&amount=-1");
        HttpRequest.request(base + Constant.LOGIN_INFO + "?token=" + token + "&game_type=jxb&amount=-1")
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                            LogUtil.i("登录2json" + json);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            __data = new JSONObject(data);

                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail_jxb);
                                ToastUtil.show(mContext, data);
                                return;
                            }
                            String token = __data.optString("token");
                            String userId = __data.optString("userid");
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN_SOCKET, token);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_ID, userId);
                            getGameRoomCount(token, userId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(base + Constant.LOGIN_INFO + "?token=" + token + "&game_type=jxb&amount=-1", null),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        JSONObject __data = null;
//                        try {
//                            json = new JSONObject(s);
//                            LogUtil.i("登录2json" + json);
//                            int code = json.optInt("code");
//                            String data = json.optString("data");
//                            __data = new JSONObject(data);
//
//                            if (code != 0) {
//                                String error = mContext.getResources().getString(R.string.login_fail_jxb);
//                                ToastUtil.show(mContext, data);
//                                return;
//                            }
//                            String token = __data.optString("token");
//                            String userId = __data.optString("userid");
//                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN_SOCKET, token);
//                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_ID, userId);
//                            getGameRoomCount(token, userId);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");
//                    }
//                })));
    }

    private void getGameRoomCount(final String token, final String userId) {
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant._BASE);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant._BASE) + Constant.LOGIN_VALIDATE + "?auth=" + token + "&userid=" + userId)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        JSONObject __user = null;

                        try {
                            json = new JSONObject(result);
                            LogUtil.i("登录3json" + json);
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
                                        String color = _roomConfig.optString("color");
                                        String[] colorArr = new Gson().fromJson(color, String[].class);
                                        gameRoom.rtmp = lan_rtmpAddressArr;
                                        gameRoom.dxRtmp = dx_rtmpAddressArr;
                                        gameRoom.ydRtmp = yd_rtmpAddressArr;
                                        gameRoom.color = colorArr;
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
                });
//        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant._BASE) + Constant.LOGIN_VALIDATE + "?auth=" + token + "&userid=" + userId, null),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        JSONObject __data = null;
//                        JSONObject __user = null;
//
//                        try {
//                            json = new JSONObject(s);
//                            LogUtil.i("登录3json" + json);
//                            int code = json.optInt("code");
//                            if (code == -1) {
//                                String error = mContext.getResources().getString(R.string.login_fail);
//                                ToastUtil.show(mContext, error);
//                                return;
//                            }
//                            String _data = json.optString("data");
//                            __data = new JSONObject(_data);
//                            List<Games> gamesList = new ArrayList<>();
//                            gamesList.clear();
//                            JSONArray data_array = __data.getJSONArray("games");
//                            for (int i = 0; i < data_array.length(); i++) {
//                                Games games = new Games();
//                                JSONObject _json = data_array.getJSONObject(i);
//                                String gameName = _json.optString("gameName");
//                                games.gameName = gameName;
//                                JSONArray _gamePlays = _json.getJSONArray("gamePlays");
//                                for (int j = 0; j < _gamePlays.length(); j++) {
//                                    GamePlay gamePlay = new GamePlay();
//                                    JSONObject _gamePlays_json = _gamePlays.getJSONObject(j);
//                                    int minCoin = _gamePlays_json.optInt("minCoin");
//                                    gamePlay.minCoin = minCoin;
//                                    games.gamePlays.add(gamePlay);
//                                    JSONArray _gameRooms = _gamePlays_json.getJSONArray("gameRooms");
//                                    for (int k = 0; k < _gameRooms.length(); k++) {
//                                        GameRoom gameRoom = new GameRoom();
//                                        JSONObject _gameRooms_json = _gameRooms.getJSONObject(k);
//                                        String _id = _gameRooms_json.optString("id");
//                                        String name = _gameRooms_json.optString("name");
//                                        int status = _gameRooms_json.optInt("status");
//                                        int betSecond = _gameRooms_json.optInt("betSecond");
//                                        String _code = _gameRooms_json.optString("code");
//                                        String minBet = _gameRooms_json.optString("minBet");
//                                        String maxBet = _gameRooms_json.optString("maxBet");
//                                        String betLimit = _gameRooms_json.optString("betLimit");
//                                        String roomConfig = _gameRooms_json.optString("roomConfig");
//                                        JSONObject _roomConfig = new JSONObject(roomConfig);
//                                        String lan_rtmpAddress = _roomConfig.optString("lan_rtmpAddress");
//                                        String[] lan_rtmpAddressArr = new Gson().fromJson(lan_rtmpAddress, String[].class);
//                                        String dx_rtmpAddress = _roomConfig.optString("dx_rtmpAddress");
//                                        String[] dx_rtmpAddressArr = new Gson().fromJson(dx_rtmpAddress, String[].class);
//                                        String yd_rtmpAddress = _roomConfig.optString("yd_rtmpAddress");
//                                        String[] yd_rtmpAddressArr = new Gson().fromJson(yd_rtmpAddress, String[].class);
//                                        String color = _roomConfig.optString("color");
//                                        String[] colorArr = new Gson().fromJson(color, String[].class);
//                                        gameRoom.rtmp = lan_rtmpAddressArr;
//                                        gameRoom.dxRtmp = dx_rtmpAddressArr;
//                                        gameRoom.ydRtmp = yd_rtmpAddressArr;
//                                        gameRoom.color = colorArr;
//                                        int[] minBetInt = new Gson().fromJson(minBet, int[].class);
//                                        int[] maxBetInt = new Gson().fromJson(maxBet, int[].class);
//                                        List<Integer> minBetList = new ArrayList<>();
//                                        List<Integer> maxBetList = new ArrayList<>();
//                                        for (int a = 0; a < minBetInt.length; a++) {
//                                            minBetList.add(minBetInt[a]);
//                                        }
//                                        for (int a = 0; a < maxBetInt.length; a++) {
//                                            maxBetList.add(maxBetInt[a]);
//                                        }
//                                        gameRoom.minBet.clear();
//                                        gameRoom.maxBet.clear();
//                                        gameRoom.minBet.addAll(minBetList);
//                                        gameRoom.maxBet.addAll(maxBetList);
//                                        gameRoom.id = _id;
//                                        gameRoom.name = name;
//                                        gameRoom.status = status;
//                                        gameRoom.code = _code;
//                                        gameRoom.betLimit = betLimit;
//                                        gameRoom.betSecond = betSecond;
//                                        gameRoomList.add(gameRoom);
//                                    }
//                                }
//                                gamesList.add(games);
//                            }
//                            getGameRoomCount();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(OkHttpException e) {
//                        ToastUtil.show(mContext, "连接超时");
//
//                    }
//                })));
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
                    baccarat.color = gameRoomList.get(i).color;
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
                    baccarat.color = gameRoomList.get(i).color;
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

    private void hintDialog(String hint) {
        final HintDialog myDialog = new HintDialog(mContext);
        myDialog.setContent(hint);
        myDialog.show();
        myDialog.setAttributes(0.5f, 0.3f);//需先显示，然后才能查找控件
        myDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
    }

    private void loginThird() {
        avi.show();
        ShareSDK.setEnableAuthTag(true);//SDK登录+标签必须设置成true
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.authorize();

        wechat.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                System.out.println("------------失败" + arg2.toString());
                LogUtil.i("微信登录 失败=" + arg2.toString());
                arg2.printStackTrace();
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                String nickname = arg0.getDb().get("nickname");
                String gender = arg0.getDb().get("gender");
                String icon = arg0.getDb().get("icon").replace("\\", "");
                String userID = arg0.getDb().get("userID");//owXUB1jYwfQ0nG__zrT4xR31qmJ4
                String openid = arg0.getDb().get("openid");//owXUB1jYwfQ0nG__zrT4xR31qmJ4
                String unionid = arg0.getDb().get("unionid");//oZ-KJ0_HiZ0nNRGgRn41IELJAToE
                String export = arg0.getDb().exportData();

                String _arg2 = arg2.toString();
                LogUtil.i("微信登录 成功= unionid=" + unionid + " nickname=" + nickname + " gender=" + gender + " icon=" + icon + " export=" + export + " _arg2=" + arg0.getDb().toString());
                SharedPreUtil.getInstance(mContext).saveParam(Constant.WECHAT_ICON, icon);
                userID = userID.substring(0, 15);//后台只能16个字符 且至少4个字符
                SharedPreUtil.getInstance(mContext).saveParam(Constant.LOGIN_TYPE, 1);//第三方登录和注册
                postRegister(userID, unionid, et_ip, nickname);
//                postLoginIn(nickname, unionid, et_ip, avi);
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                System.out.println("-------取消");
                LogUtil.i("微信登录 取消");
            }
        });
        wechat.SSOSetting(false);//设置客户端授权
//        ShareSDK.setActivity(this);//抖音登录适配安卓9.0
        wechat.showUser(null);
    }

    private void postRegister(final String user, final String pwd, final EditText et_ip, final String nickname) {
        //12346
        final String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(et_ip) + ServiceIpConstant.BASE_AFTER;
        HashMap<String, String> map = new HashMap<>();
        map.put("username", user);
        map.put("password", pwd);
        map.put("promo_code", "12346");//邀请码默认写死
        map.put("deviceid", ProtocolUtil.getInstance().getDeviceId());
        map.put("nickname", nickname);
        LogUtil.i("微信注册=" + map.toString());
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(baseUrl + Constant.REGISTER, new RequestParams(map)),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject json = null;
                            JSONObject __data = null;
                            json = new JSONObject(result);
                            LogUtil.i("注册1json" + json);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code == 400) {
                                LogUtil.i("微信已注册=" + nickname);
                                postLoginIn(user, pwd, et_ip, avi, nickname);
                                return;
                            }
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.login_fail);
                                ToastUtil.show(mContext, error);
                                return;
                            }
                            avi.hide();
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_PWD, pwd);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_NAME, nickname);
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN, data);
                            ToastUtil.show(mContext, mContext.getResources().getString(R.string.register_succeeded));
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE_URL, et_ip.getText().toString());
                            String _ip = BaccaratUtil.getInstance().changeIp(et_ip.getText().toString());
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE, ServiceIpConstant.BASE_BEFORE + _ip + "/api");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant._BASE, ServiceIpConstant.BASE_BEFORE + _ip + ":8888/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_LOBBY, ServiceIpConstant.BASE_BEFORE + _ip + ":9081/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_ROOM, ServiceIpConstant.BASE_BEFORE + _ip + ":9082/");
                            SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_PRODICT, ServiceIpConstant.BASE_BEFORE + _ip + ":9080/");
                            avi.hide();
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
}
