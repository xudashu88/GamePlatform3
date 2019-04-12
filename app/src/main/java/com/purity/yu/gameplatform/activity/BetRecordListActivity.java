package com.purity.yu.gameplatform.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.LogUtil;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.TimeUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.BetRecordAdapter;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseListActivity;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Records;
import com.purity.yu.gameplatform.http.HttpRequest;
import com.purity.yu.gameplatform.widget.popup.PopupWindow.CommonPopupWindow;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gangbeng.basemodule.http.HttpParse.parseArrayObject;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/7/19.
 */
@ContentView(R.layout.activity_bet_record_list)
public class BetRecordListActivity extends BaseListActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.rl_select_date)
    public RelativeLayout rl_select_date;
    @BindView(R.id.tv_select_date)
    public TextView tv_select_date;
    @BindView(R.id.iv_select_date)
    public ImageView iv_select_date;

    @BindView(R.id.rl_select_game)
    public RelativeLayout rl_select_game;
    @BindView(R.id.tv_select_game)
    public TextView tv_select_game;
    @BindView(R.id.iv_select_game)
    public ImageView iv_select_game;

    @BindView(R.id.tv_no_data)
    public TextView tv_no_data;
    @BindView(R.id.rl_data)
    public RelativeLayout rl_data;
    private Context mContext;
    private CommonPopupWindow popupWindow;
    private CommonPopupWindow popupWindowGame;
    private BetRecordAdapter adapter;
    private List<Records> recordList;
    private List<Records> recordListAll;
    private int dateType = 0;
    private String gameType = "";

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mContext = this;
        recordList = new ArrayList<>();
        recordListAll = new ArrayList<>();
        SharedPreUtil.getInstance(mContext).saveParam(Constant.BET0, 0);
        initEvent();
        requestData(1);
        initAdapter();
        LogUtil.i("data-今天" + TimeUtil.getToday());
        LogUtil.i("data-昨天" + TimeUtil.getYesterday());
        LogUtil.i("data-最近一星期" + TimeUtil.getRecentWeek());

    }

    private void initAdapter() {
        adapter = new BetRecordAdapter(mContext, recordListAll);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefresh(false);//禁止下拉刷新，是為了防止顯示多個牌面
    }

    private void initEvent() {
        rl_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_select_date.setRotation(180f);//图片向下旋转
                downPopupWindow(v);
            }
        });
        rl_select_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_select_game.setRotation(180f);//图片向下旋转
                downPopupWindowGame(v);
            }
        });
    }

    private void downPopupWindowGame(View v) {
        if (popupWindowGame != null && popupWindowGame.isShowing()) return;
        popupWindowGame = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.pop_select_game)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();
        popupWindowGame.showAsDropDown(v);
        popupWindowGame.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_select_game.setRotation(0f);//图片向上旋转
            }
        });
    }

    private void downPopupWindow(View v) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.pop_select_date)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setViewOnclickListener(this)
                .setOutsideTouchable(true)
                .create();
        popupWindow.showAsDropDown(v);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_select_date.setRotation(0f);//图片向上旋转
            }
        });
    }

    @Override
    protected List getData() {
        return recordListAll;
    }

    @Override
    protected void requestData(final int page) {
        postBetRecord(dateType, page, gameType);
    }

    private void postBetRecord(final int type, final int page, String gameType) {
        String startDate = "";
        String endDate = "";
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        if (type == 0) {
            startDate = TimeUtil.getToday();
            endDate = startDate;
        } else if (type == 1) {
            startDate = TimeUtil.getYesterday();
            endDate = TimeUtil.getToday();
        } else {
            startDate = TimeUtil.getRecentWeek();
            endDate = TimeUtil.getToday();
        }
        if (gameType.equals("")) {
            gameType = "";
        } else if (gameType.equals("baccarat")) {
            gameType = "baccarat";
        } else if (gameType.equals("dragon_tiger")) {
            gameType = "dragon_tiger";
        } else if (gameType.equals("duel")) {
            gameType = "duel";
        }
        LogUtil.i("startDate=" + startDate + " endDate=" + endDate);
        HttpRequest.request(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.BET_RECORD + "?token=" + token + "&startDate=" + startDate + "&endDate=" + endDate + "&page=" + page + "&game_type=" + gameType)
                .executeGetParams(new HttpRequest.HttpCallBack() {
                    @Override
                    public void onResultOk(String result) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(result);
                            int code = json.optInt("code");
                            if (code != 0) {
                                String error = mContext.getResources().getString(R.string.username_existed);
                                ToastUtil.show(mContext, error);
                                return;
                            }

                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            recordList.clear();//清除,每一页都是新的
                            recordList = parseArrayObject(_data, "items", Records.class);
                            LogUtil.i("下注记录=" + _data);
                            String meta = __data.optString("_meta");
                            JSONObject _meta = new JSONObject(meta);
                            total = _meta.optInt("totalCount");
                            pageCount = _meta.optInt("pageCount");

                            if (page == 1) {
                                recordListAll.clear();//下拉刷新,要清除总集合
                            }
                            recordListAll.addAll(recordList);
                            adapter.updateList(recordListAll);
                            if (recordList.size() > 0) {
                                tv_no_data.setVisibility(View.GONE);
                                rl_data.setVisibility(View.VISIBLE);
                            } else {
                                tv_no_data.setVisibility(View.VISIBLE);
                                rl_data.setVisibility(View.GONE);
                            }
                            onFinish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onRefresh(boolean isRefresh) {
        super.onRefresh(isRefresh);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                onFinish();
            }
        }, 1000);
    }

    public void onFinish() {
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                SharedPreUtil.getInstance(mContext).saveParam(Constant.BET0, 0);
                finish();
                break;
        }
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.pop_select_date:
                TextView tv_today = (TextView) view.findViewById(R.id.tv_today);
                TextView tv_yesterday = (TextView) view.findViewById(R.id.tv_yesterday);
                TextView tv_last_week = (TextView) view.findViewById(R.id.tv_last_week);
                tv_today.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_select_date.setText(mContext.getResources().getString(R.string.today));
                        dateType = 0;
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.BET0, 0);
                        recordListAll.clear();
                        requestData(1);
                        tv_no_data.setVisibility(View.GONE);
                        rl_data.setVisibility(View.VISIBLE);
                        popupWindow.dismiss();
                    }
                });
                tv_yesterday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_select_date.setText(mContext.getResources().getString(R.string.yesterday));
                        dateType = 1;
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.BET0, 0);
                        recordListAll.clear();
                        requestData(1);
                        tv_no_data.setVisibility(View.VISIBLE);
                        rl_data.setVisibility(View.GONE);
                        popupWindow.dismiss();
                    }
                });
                tv_last_week.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_select_date.setText(mContext.getResources().getString(R.string.last_week));
                        dateType = 2;
                        SharedPreUtil.getInstance(mContext).saveParam(Constant.BET0, 0);
                        recordListAll.clear();
                        requestData(1);
                        tv_no_data.setVisibility(View.GONE);
                        rl_data.setVisibility(View.VISIBLE);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.layout.pop_select_game:
                TextView tv_all = (TextView) view.findViewById(R.id.tv_all);
                TextView tv_bac = (TextView) view.findViewById(R.id.tv_bac);
                TextView tv_dt = (TextView) view.findViewById(R.id.tv_dt);
                TextView tv_duel = (TextView) view.findViewById(R.id.tv_duel);
                tv_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_select_game.setText(mContext.getResources().getString(R.string.all_game));
                        gameType = "";
                        recordListAll.clear();
                        requestData(1);
                        tv_no_data.setVisibility(View.GONE);
                        rl_data.setVisibility(View.VISIBLE);
                        popupWindowGame.dismiss();
                    }
                });
                tv_bac.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_select_game.setText(mContext.getResources().getString(R.string.ba_game));
                        gameType = "baccarat";
                        recordListAll.clear();
                        requestData(1);
                        tv_no_data.setVisibility(View.GONE);
                        rl_data.setVisibility(View.VISIBLE);
                        popupWindowGame.dismiss();
                    }
                });
                tv_dt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_select_game.setText(mContext.getResources().getString(R.string.dt_game));
                        gameType = "dragon_tiger";
                        recordListAll.clear();
                        requestData(1);
                        tv_no_data.setVisibility(View.GONE);
                        rl_data.setVisibility(View.VISIBLE);
                        popupWindowGame.dismiss();
                    }
                });
                tv_duel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_select_game.setText(mContext.getResources().getString(R.string.single_pick_game));
                        gameType = "duel";
                        recordListAll.clear();
                        requestData(1);
                        tv_no_data.setVisibility(View.GONE);
                        rl_data.setVisibility(View.VISIBLE);
                        popupWindowGame.dismiss();
                    }
                });
                break;
        }
    }
}
