package com.purity.yu.gameplatform.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.google.gson.Gson;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.activity.TabHostActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Baccarat;
import com.purity.yu.gameplatform.entity.Game2;
import com.purity.yu.gameplatform.entity.GamePlay;
import com.purity.yu.gameplatform.entity.GameRoom;
import com.purity.yu.gameplatform.entity.Games;
import com.purity.yu.gameplatform.event.ObjectEvent;
import com.purity.yu.gameplatform.http.HttpRequest;
import com.purity.yu.gameplatform.login.KittyActivity;
import com.purity.yu.gameplatform.login.LoginActivity;
import com.purity.yu.gameplatform.login.RegisterActivity;
import com.purity.yu.gameplatform.widget.HintDialog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.request.RequestParams;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;

import static com.gangbeng.basemodule.http.HttpParse.parseArrayObject;

public class ProtocolUtil {
    private Context mContext;
    private static ProtocolUtil instance = null;
    private NotificationManager mNotificationManager;
    List<GameRoom> gameRoomList = new ArrayList<>();
    List<Game2> gameList = new ArrayList<>();

    public void init(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static ProtocolUtil getInstance() {
        if (null == instance) {
            instance = new ProtocolUtil();
        }
        return instance;
    }

    public void postHJRecycle(final TextView tv_money) {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("game_type", "");
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.HJ_RECYCLE + "?token=" + token + "&amount=0", new RequestParams(map)),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(s);
                            LogUtil.i("回收" + json);
                            int code = json.optInt("code");
                            double data = json.optDouble("data");
                            ;
                            if (code == 0) {
                                if (data > 0) {
                                    ToastUtil.showToast(mContext, "回收" + data + "成功");
                                    tv_money.setText(String.valueOf(data));
                                    getAccount();//回收后，信用额度也跟着变
                                } else {
                                    ToastUtil.showToast(mContext, "已回收");
                                }
                            } else {
                                ToastUtil.showToast(mContext, "提现失败" + data);
                            }

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

    private void getAccount() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.USER_ACCOUNT + "?token=" + token, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(s);
                            String _data = json.optString("data");
                            LogUtil.i("getAccount=" + _data);
                            __data = new JSONObject(_data);
                            double free_amount = __data.optDouble("free_amount");
                            double fee_rate = __data.optDouble("fee_rate");
//                            tv_free.setText("您当前可用额度为" + String.valueOf(free_amount) + ",超出额度按" + fee_rate + "收取。");
                            postEventCreditsAmount(free_amount, fee_rate);
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

    private void postEventCreditsAmount(double creditsAmount, double creditsRate) {
        ObjectEvent.CreditsAmountEvent event = new ObjectEvent.CreditsAmountEvent();
        event.creditsAmount = creditsAmount;
        event.creditsRate = creditsRate;
        EventBus.getDefault().post(event);
    }


    public String getDeviceId() {
        String deviceId = null;
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            deviceId = tm.getDeviceId();//354765081029985 NJ6R18305006145
            String android_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);//8bc5b2e6141fb866 fd52d7703ddb864a
            LogUtil.i("设备信息 手机卡 imei=" + deviceId + " 序列号=" + android_id);
        }
        return deviceId;
    }

    public void postLoginInfo() {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        //上分，-1不上分，0上所有的分，>0 上具体分数
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        LogUtil.i("获取登陆信息=" + base + Constant.LOGIN_INFO + "?token=" + token + "&game_type=jxb&amount=-1");
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(base + Constant.LOGIN_INFO + "?token=" + token + "&game_type=jxb&amount=-1", null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(s);
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
                            LogUtil.i("登陆接口 1_1" + json);
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
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant._BASE);
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant._BASE) + Constant.LOGIN_VALIDATE + "?auth=" + token + "&userid=" + userId, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        JSONObject __user = null;

                        try {
                            json = new JSONObject(s);
                            LogUtil.i("验证登录信息" + json);
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

    public void getAppInfo(EditText et_ip) {
        String baseUrl = ServiceIpConstant.BASE_BEFORE + BaccaratUtil.getInstance().changeIp(et_ip) + ServiceIpConstant.BASE_AFTER;
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE_URL, et_ip.getText().toString());
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(baseUrl + Constant.APP_INFO, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(s);
                            LogUtil.i("是否注册=" + json);
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            int open_register = __data.optInt("open_register");
                            if (open_register == 1) {//1开放注册 其他关闭注册
                                mContext.startActivity(new Intent(mContext, RegisterActivity.class));
                                ((Activity) mContext).finish();
                            } else {
                                ToastUtil.show(mContext, "需要注册，请联系管理员");
                                return;
                            }
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


    public void getGameList() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.GAME_LIST + "?token=" + token)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        try {
                            json = new JSONObject(result);
                            String _data = json.optString("data");
                            gameList = parseArrayObject(_data, "items", Game2.class);
                            for (int i = 0; i < gameList.size(); i++) {
                                String verify = gameList.get(i).platformCode + gameList.get(i).gameNameEn;
                                if (verify.equals("JXB" + "baccarat")) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.BAC_ROOM_COUNT);
                                } else if (verify.equals("JXB" + "dragonTiger")/*gameList.get(i).gameName.equals("龙虎")*/) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.DT_ROOM_COUNT);
                                } else if (verify.equals("JXB" + "dragonTiger2")/*gameList.get(i).gameName.equals("龙虎")*/) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.DT_ROOM_COUNT);
                                } else if (verify.equals("JXB" + "duel")/*gameList.get(i).gameName.equals("单挑")*/) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.SINGLE_ROOM_COUNT);
                                } else if (verify.equals("JXB" + "macau")/*gameList.get(i).gameName.equals("澳门五路")*/) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.BAC_ROOM_COUNT);
                                } else if (verify.equals("JXB" + "macau.")/*gameList.get(i).gameName.equals("澳门五路.")*/) {
                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.BAC_ROOM_COUNT);
                                }
                            }
                            SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_GAME_LIST, new Gson().toJson(gameList));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
//        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.GAME_LIST + "?token=" + token, null),//perfect
//                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject json = null;
//                        try {
//                            json = new JSONObject(s);
//                            String _data = json.optString("data");
//                            gameList = parseArrayObject(_data, "items", Game2.class);
//                            for (int i = 0; i < gameList.size(); i++) {
//                                String verify = gameList.get(i).platformCode + gameList.get(i).gameNameEn;
//                                if (verify.equals("JXB" + "baccarat")) {
//                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.BAC_ROOM_COUNT);
//                                } else if (verify.equals("JXB" + "dragonTiger")/*gameList.get(i).gameName.equals("龙虎")*/) {
//                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.DT_ROOM_COUNT);
//                                } else if (verify.equals("JXB" + "dragonTiger2")/*gameList.get(i).gameName.equals("龙虎")*/) {
//                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.DT_ROOM_COUNT);
//                                } else if (verify.equals("JXB" + "duel")/*gameList.get(i).gameName.equals("单挑")*/) {
//                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.SINGLE_ROOM_COUNT);
//                                } else if (verify.equals("JXB" + "macau")/*gameList.get(i).gameName.equals("澳门五路")*/) {
//                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.BAC_ROOM_COUNT);
//                                } else if (verify.equals("JXB" + "macau.")/*gameList.get(i).gameName.equals("澳门五路.")*/) {
//                                    gameList.get(i).gameDot = SharedPreUtil.getInstance(mContext).getInt(Constant.BAC_ROOM_COUNT);
//                                }
//                            }
//                            SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_GAME_LIST, new Gson().toJson(gameList));
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

    public void postLoginInfo(final TextView tv_money) {
        final String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(base + Constant.LOGIN_INFO + "?token=" + token + "&game_type=jxb&amount=0", null),//perfect todo 在后面加了&amount=0能否解决分数翻倍的问题
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");
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
                            //http://103.112.29.145:8888/admin/backend/points?score=1000000&&type=0&&userid=536273706603773952 备份上分接口
                            getGold(token, userId, tv_money);
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

    public void getGold(final String token, final String userId, final TextView tv_money) {
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant._BASE) + Constant.LOGIN_VALIDATE + "?auth=" + token + "&userid=" + userId, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        JSONObject __user = null;
                        try {
                            json = new JSONObject(s);
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
                                        String rtmp = _gameRooms_json.optString("rtmpAddress");
                                        String[] rtmpStr = new Gson().fromJson(rtmp, String[].class);
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
                                        gameRoom.rtmp = rtmpStr;
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

                            String _user = __data.optString("user");
                            __user = new JSONObject(_user);
                            String goldcoins = __user.optString("availableAmount");
                            tv_money.setText(goldcoins);
                            LogUtil.i("getGold=" + json + " goldcoins=" + goldcoins);
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

    public void postLoginOut(final Context mContext, final Timer timer) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
//            System.exit(0);
            cleanSharedPre();
            Intent intent = new Intent(mContext, KittyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(intent);
            ((TabHostActivity) mContext).finish();
            return;
        }
        postLogOut(timer);
    }

    public void postLogOut(final Timer timer) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.LOGIN_OUT + "?token=" + token, null),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject json = null;
                            JSONObject __data = null;
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code != 0) {
                                ToastUtil.show(mContext, data);
                                return;
                            } else {
                                if (timer != null) {
                                    timer.cancel();//离开主页暂停获取消息轮询
                                }
                                String ok = mContext.getResources().getString(R.string.exit_successfully);
                                ToastUtil.show(mContext, ok);
                                cleanSharedPre();
                                Intent intent = new Intent(mContext, KittyActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(intent);
                                ((TabHostActivity) mContext).finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException e) {
                        ToastUtil.show(mContext, "退出失败");
                    }
                })));
    }

    /**
     * 登陆界面的退出 直接关闭应用
     * android:excludeFromRecents="true"
     * android:launchMode="singleInstance"
     * 需求:监听APP强制关闭，在Service中的onTaskRemove这个方法在安卓8.0上不调用，就不能请求退出操作
     * 目的:不会在最近任务列表中显示 2019.3.27
     */
    public void postLogOutSys() {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        if (TextUtils.isEmpty(token)) {
            cleanSharedPre();
            System.exit(0);
            return;
        } else {
            CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.LOGIN_OUT + "?token=" + token, null),//perfect
                    new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject json = null;
                                JSONObject __data = null;
                                json = new JSONObject(result);
                                int code = json.optInt("code");
                                String data = json.optString("data");
                                if (code != 0) {
                                    ToastUtil.show(mContext, data);
                                    return;
                                } else {
                                    cleanSharedPre();
                                    System.exit(0);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(OkHttpException e) {
                            ToastUtil.show(mContext, "退出失败");
                        }
                    })));
        }
    }

    public void notification(final String message) {
        ((TabHostActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final int PUSH_NOTIFICATION_ID = (0x001);
                final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
                final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";

//                                        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                    if (mNotificationManager != null) {
                        mNotificationManager.createNotificationChannel(channel);
                    }
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, PUSH_CHANNEL_ID);
                Intent notificationIntent = new Intent(mContext, TabHostActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
                builder.setContentTitle("系统公告")//设置通知栏标题
                        .setContentIntent(pendingIntent) //设置通知栏点击意图
//                                        .setSound(Uri.parse("android.resource://com.gangbeng.lazyperson/" + R.raw.suai));
                        .setContentText(message)
                        .setTicker(message) //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setSmallIcon(R.drawable.logo)//设置通知小ICON
                        .setChannelId(PUSH_CHANNEL_ID)
                        .setDefaults(Notification.DEFAULT_ALL);

                Notification notification = builder.build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                if (mNotificationManager != null) {
                    mNotificationManager.notify(PUSH_NOTIFICATION_ID, notification);
                }
            }
        });
    }

    private void cleanSharedPre() {
        SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN, "");
        SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_TOKEN_SOCKET, "");
        SharedPreUtil.getInstance(mContext).saveParam(Constant.USER_ID, "");

        SharedPreUtil.getInstance(mContext).saveParam(Constant.BAC_ROOM_COUNT, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.DT_ROOM_COUNT, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.SINGLE_ROOM_COUNT, 0);
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_BAC_LIST, "");
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_DT_LIST, "");
        SharedPreUtil.getInstance(mContext).saveParam(Constant.PRE_SINGLE_LIST, "");

        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.BASE, "");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant._BASE, "");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_LOBBY, "");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_ROOM, "");
        SharedPreUtil.getInstance(mContext).saveParam(ServiceIpConstant.SOCKET_PRODICT, "");
    }

    public void hintDialog(Context mContext, String hint) {
        final HintDialog myDialog = new HintDialog(mContext);
        myDialog.setContent(hint);
        myDialog.show();
        myDialog.setAttributes();//需先显示，然后才能查找控件
        myDialog.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
    }
}
