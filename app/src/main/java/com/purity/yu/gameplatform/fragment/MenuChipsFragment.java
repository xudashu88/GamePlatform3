package com.purity.yu.gameplatform.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.google.gson.Gson;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.widget.HintDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

@ContentView(R.layout.fragment_menu_chips)
public class MenuChipsFragment extends BaseFragment {
    @BindView(R.id.rl_roulette)
    RelativeLayout rl_roulette;
    @BindView(R.id.rl_sicbo)
    RelativeLayout rl_sicbo;
    @BindView(R.id.rl_dt)
    RelativeLayout rl_dt;
    @BindView(R.id.v_roulette)
    View v_roulette;
    @BindView(R.id.v_sicbo)
    View v_sicbo;
    @BindView(R.id.v_dt)
    View v_dt;
    @BindView(R.id.ll_roulette)
    LinearLayout ll_roulette;
    @BindView(R.id.ll_roulette_1)
    LinearLayout ll_roulette_1;
    @BindView(R.id.ll_sicbo)
    LinearLayout ll_sicbo;
    @BindView(R.id.ll_sicbo_1)
    LinearLayout ll_sicbo_1;
    @BindView(R.id.ll_dt)
    LinearLayout ll_dt;
    @BindView(R.id.ll_dt_1)
    LinearLayout ll_dt_1;

    @BindView(R.id.rl_rou_10)
    RelativeLayout rl_rou_10;
    @BindView(R.id.rl_rou_20)
    RelativeLayout rl_rou_20;
    @BindView(R.id.rl_rou_50)
    RelativeLayout rl_rou_50;
    @BindView(R.id.rl_rou_100)
    RelativeLayout rl_rou_100;
    @BindView(R.id.rl_rou_200)
    RelativeLayout rl_rou_200;

    @BindView(R.id.rl_sic_10)
    RelativeLayout rl_sic_10;
    @BindView(R.id.rl_sic_20)
    RelativeLayout rl_sic_20;
    @BindView(R.id.rl_sic_50)
    RelativeLayout rl_sic_50;
    @BindView(R.id.rl_sic_100)
    RelativeLayout rl_sic_100;
    @BindView(R.id.rl_sic_200)
    RelativeLayout rl_sic_200;

    @BindView(R.id.rl_dt_10)
    RelativeLayout rl_dt_10;
    @BindView(R.id.rl_dt_20)
    RelativeLayout rl_dt_20;
    @BindView(R.id.rl_dt_50)
    RelativeLayout rl_dt_50;
    @BindView(R.id.rl_dt_100)
    RelativeLayout rl_dt_100;
    @BindView(R.id.rl_dt_200)
    RelativeLayout rl_dt_200;

    @BindView(R.id.rl_rou_500)
    RelativeLayout rl_rou_500;
    @BindView(R.id.rl_rou_1000)
    RelativeLayout rl_rou_1000;
    @BindView(R.id.rl_rou_5000)
    RelativeLayout rl_rou_5000;
    @BindView(R.id.rl_rou_10000)
    RelativeLayout rl_rou_10000;
    @BindView(R.id.rl_rou_50000)
    RelativeLayout rl_rou_50000;

    @BindView(R.id.rl_sic_500)
    RelativeLayout rl_sic_500;
    @BindView(R.id.rl_sic_1000)
    RelativeLayout rl_sic_1000;
    @BindView(R.id.rl_sic_5000)
    RelativeLayout rl_sic_5000;
    @BindView(R.id.rl_sic_10000)
    RelativeLayout rl_sic_10000;
    @BindView(R.id.rl_sic_50000)
    RelativeLayout rl_sic_50000;

    @BindView(R.id.rl_dt_500)
    RelativeLayout rl_dt_500;
    @BindView(R.id.rl_dt_1000)
    RelativeLayout rl_dt_1000;
    @BindView(R.id.rl_dt_5000)
    RelativeLayout rl_dt_5000;
    @BindView(R.id.rl_dt_10000)
    RelativeLayout rl_dt_10000;
    @BindView(R.id.rl_dt_50000)
    RelativeLayout rl_dt_50000;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;

    private int isRoulette = 1;
    private List<RelativeLayout> relativeLayoutList = new ArrayList<>();
    private List<String> strList = new ArrayList<>();
    private List<String> holyDaySelectList = new ArrayList<>();
    private List<String> holyDayNoSelectList = new ArrayList<>();
    private List<String> macauSelectList = new ArrayList<>();
    private List<String> macauNoSelectList = new ArrayList<>();
    private List<String> dtSelectList = new ArrayList<>();
    private List<String> dtNoSelectList = new ArrayList<>();

    @Override
    protected void initView(View view) {
        initEvent();
        addRelativeLayoutAll();
        initData();
        if (TextUtils.isEmpty(SharedPreUtil.getInstance(getActivity()).getString(Constant.HOLY_DAY_CHIP))) {
            initDefault();
        } else {
            String holyDay = SharedPreUtil.getInstance(getActivity()).getString(Constant.HOLY_DAY_CHIP);
            String[] holyDayArr = new Gson().fromJson(holyDay, String[].class);
            for (int i = 0; i < holyDayArr.length; i++) {
                holyDaySelectList.add(holyDayArr[i]);
            }

            String holyDayNo = SharedPreUtil.getInstance(getActivity()).getString(Constant.HOLY_DAY_CHIP_NO);
            String[] holyDayArrNo = new Gson().fromJson(holyDayNo, String[].class);
            for (int i = 0; i < holyDayArrNo.length; i++) {
                holyDayNoSelectList.add(holyDayArrNo[i]);
            }

            String macau = SharedPreUtil.getInstance(getActivity()).getString(Constant.MACAU_CHIP);
            String[] macauArr = new Gson().fromJson(macau, String[].class);
            for (int i = 0; i < macauArr.length; i++) {
                macauSelectList.add(macauArr[i]);
            }

            String macauNo = SharedPreUtil.getInstance(getActivity()).getString(Constant.MACAU_CHIP_NO);
            String[] macauArrNo = new Gson().fromJson(macauNo, String[].class);
            for (int i = 0; i < macauArrNo.length; i++) {
                macauNoSelectList.add(macauArrNo[i]);
            }

            String dt = SharedPreUtil.getInstance(getActivity()).getString(Constant.DT_CHIP);
            String[] dtArr = new Gson().fromJson(dt, String[].class);
            for (int i = 0; i < dtArr.length; i++) {
                dtSelectList.add(dtArr[i]);
            }

            String dtNo = SharedPreUtil.getInstance(getActivity()).getString(Constant.DT_CHIP_NO);
            String[] dtArrNo = new Gson().fromJson(dtNo, String[].class);
            for (int i = 0; i < dtArrNo.length; i++) {
                dtNoSelectList.add(dtArrNo[i]);
            }
        }
        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.HOLY_DAY_CHIP, holyDaySelectList.toString());
        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.HOLY_DAY_CHIP_NO, holyDayNoSelectList.toString());
        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.MACAU_CHIP, macauSelectList.toString());
        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.MACAU_CHIP_NO, macauNoSelectList.toString());
        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.DT_CHIP, dtSelectList.toString());
        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.DT_CHIP_NO, dtNoSelectList.toString());
    }

    private void initDefault() {
        holyDaySelectList.add("rl_rou_20");
        holyDaySelectList.add("rl_rou_100");
        holyDaySelectList.add("rl_rou_500");
        holyDaySelectList.add("rl_rou_1000");
        holyDaySelectList.add("rl_rou_5000");
        macauSelectList.add("rl_sic_20");
        macauSelectList.add("rl_sic_100");
        macauSelectList.add("rl_sic_500");
        macauSelectList.add("rl_sic_1000");
        macauSelectList.add("rl_sic_5000");
        dtSelectList.add("rl_dt_20");
        dtSelectList.add("rl_dt_100");
        dtSelectList.add("rl_dt_500");
        dtSelectList.add("rl_dt_1000");
        dtSelectList.add("rl_dt_5000");

        holyDayNoSelectList.add("rl_rou_10");
        holyDayNoSelectList.add("rl_rou_50");
        holyDayNoSelectList.add("rl_rou_200");
        holyDayNoSelectList.add("rl_rou_10000");
        holyDayNoSelectList.add("rl_rou_50000");
        macauNoSelectList.add("rl_sic_10");
        macauNoSelectList.add("rl_sic_50");
        macauNoSelectList.add("rl_sic_200");
        macauNoSelectList.add("rl_sic_10000");
        macauNoSelectList.add("rl_sic_50000");
        dtNoSelectList.add("rl_dt_10");
        dtNoSelectList.add("rl_dt_50");
        dtNoSelectList.add("rl_dt_200");
        dtNoSelectList.add("rl_dt_10000");
        dtNoSelectList.add("rl_dt_50000");
    }

    private void initData() {
        for (int i = 0; i < 30; i++) {
            if (SharedPreUtil.getInstance(getActivity()).getInt(strList.get(i)) == 1) {
                if (relativeLayoutList.get(i).getTag().equals(strList.get(i))) {
                    relativeLayoutList.get(i).setBackgroundResource(0);
                }
            } else if (SharedPreUtil.getInstance(getActivity()).getInt(strList.get(i)) == 2) {
                if (relativeLayoutList.get(i).getTag().equals(strList.get(i))) {
                    relativeLayoutList.get(i).setBackgroundResource(R.drawable.bg_gray_stroke_golden_menu);
                }
            }
        }
    }

    private void addRelativeLayoutAll() {
        addRelativeLayout(rl_rou_10);
        addRelativeLayout(rl_rou_20);
        addRelativeLayout(rl_rou_50);
        addRelativeLayout(rl_rou_100);
        addRelativeLayout(rl_rou_200);
        addRelativeLayout(rl_sic_10);
        addRelativeLayout(rl_sic_20);
        addRelativeLayout(rl_sic_50);
        addRelativeLayout(rl_sic_100);
        addRelativeLayout(rl_sic_200);
        addRelativeLayout(rl_dt_10);
        addRelativeLayout(rl_dt_20);
        addRelativeLayout(rl_dt_50);
        addRelativeLayout(rl_dt_100);
        addRelativeLayout(rl_dt_200);

        addRelativeLayout(rl_rou_500);
        addRelativeLayout(rl_rou_1000);
        addRelativeLayout(rl_rou_5000);
        addRelativeLayout(rl_rou_10000);
        addRelativeLayout(rl_rou_50000);
        addRelativeLayout(rl_sic_500);
        addRelativeLayout(rl_sic_1000);
        addRelativeLayout(rl_sic_5000);
        addRelativeLayout(rl_sic_10000);
        addRelativeLayout(rl_sic_50000);
        addRelativeLayout(rl_dt_500);
        addRelativeLayout(rl_dt_1000);
        addRelativeLayout(rl_dt_5000);
        addRelativeLayout(rl_dt_10000);
        addRelativeLayout(rl_dt_50000);

        strList.add("rl_rou_10");
        strList.add("rl_rou_20");
        strList.add("rl_rou_50");
        strList.add("rl_rou_100");
        strList.add("rl_rou_200");
        strList.add("rl_sic_10");
        strList.add("rl_sic_20");
        strList.add("rl_sic_50");
        strList.add("rl_sic_100");
        strList.add("rl_sic_200");
        strList.add("rl_dt_10");
        strList.add("rl_dt_20");
        strList.add("rl_dt_50");
        strList.add("rl_dt_100");
        strList.add("rl_dt_200");

        strList.add("rl_rou_500");
        strList.add("rl_rou_1000");
        strList.add("rl_rou_5000");
        strList.add("rl_rou_10000");
        strList.add("rl_rou_50000");
        strList.add("rl_sic_500");
        strList.add("rl_sic_1000");
        strList.add("rl_sic_5000");
        strList.add("rl_sic_10000");
        strList.add("rl_sic_50000");
        strList.add("rl_dt_500");
        strList.add("rl_dt_1000");
        strList.add("rl_dt_5000");
        strList.add("rl_dt_10000");
        strList.add("rl_dt_50000");
    }

    private void addRelativeLayout(RelativeLayout rl) {
        relativeLayoutList.add(rl);
        setOnClick(rl);
    }

    private void setOnClick(RelativeLayout rl) {
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout _tv = (RelativeLayout) v;
                if (_tv != null) {
                    String _tvTag = (String) _tv.getTag();
                    if (_tv.getBackground() != null) {
                        _tv.setBackgroundResource(0);
                        if (_tvTag.contains("rl_rou")) {
                            remove(holyDaySelectList, _tvTag);
                            holyDayNoSelectList.add(_tvTag);
                        } else if (_tvTag.contains("rl_sic")) {
                            remove(macauSelectList, _tvTag);
                            macauNoSelectList.add(_tvTag);
                        } else if (_tvTag.contains("rl_dt")){
                            remove(dtSelectList, _tvTag);
                            dtNoSelectList.add(_tvTag);
                        }
//                        SharedPreUtil.getInstance(getActivity()).saveParam(_tvTag, 1);
                    } else {
                        _tv.setBackgroundResource(R.drawable.bg_gray_stroke_golden_menu);
                        if (_tvTag.contains("rl_rou")) {
                            holyDaySelectList.add(_tvTag);
                            remove(holyDayNoSelectList, _tvTag);
                        } else if (_tvTag.contains("rl_sic")) {
                            macauSelectList.add(_tvTag);
                            remove(macauNoSelectList, _tvTag);
                        } else if (_tvTag.contains("rl_dt")){
                            dtSelectList.add(_tvTag);
                            remove(dtNoSelectList, _tvTag);
                        }
//                        SharedPreUtil.getInstance(getActivity()).saveParam(_tvTag, 2);
                    }
                }
            }
        });
    }

    public void remove(List<String> list, String target) {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals(target)) {
                iterator.remove();
            }
        }
    }

    private void initEvent() {
        rl_roulette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isRoulette == 1) {
                rouletteGone(View.VISIBLE, View.GONE, View.GONE);
//                }
            }
        });
        rl_sicbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isRoulette == 2) {
                rouletteGone(View.GONE, View.VISIBLE, View.GONE);
//                }
            }
        });
        rl_dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isRoulette == 3) {
                rouletteGone(View.GONE, View.GONE, View.VISIBLE);
//                }
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holyDaySelectList.size() != 5 || macauSelectList.size() != 5 || dtSelectList.size() != 5) {
                    hintDialog("请选择5种筹码");
                } else if (holyDaySelectList.size() == 5 && macauSelectList.size() == 5 && dtSelectList.size() == 5) {
                    for (int i = 0; i < holyDaySelectList.size(); i++) {
                        SharedPreUtil.getInstance(getActivity()).saveParam(holyDaySelectList.get(i), 2);
                        SharedPreUtil.getInstance(getActivity()).saveParam(macauSelectList.get(i), 2);
                        SharedPreUtil.getInstance(getActivity()).saveParam(dtSelectList.get(i), 2);
                        SharedPreUtil.getInstance(getActivity()).saveParam(holyDayNoSelectList.get(i), 1);
                        SharedPreUtil.getInstance(getActivity()).saveParam(macauNoSelectList.get(i), 1);
                        SharedPreUtil.getInstance(getActivity()).saveParam(dtNoSelectList.get(i), 1);
                        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.HOLY_DAY_CHIP, holyDaySelectList.toString());
                        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.MACAU_CHIP, macauSelectList.toString());
                        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.DT_CHIP, dtSelectList.toString());
                        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.HOLY_DAY_CHIP_NO, holyDayNoSelectList.toString());
                        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.MACAU_CHIP_NO, macauNoSelectList.toString());
                        SharedPreUtil.getInstance(getActivity()).saveParam(Constant.DT_CHIP_NO, dtNoSelectList.toString());
                    }
                    ToastUtil.show(getActivity(), "保存成功");
                }
            }
        });
    }

    private void hintDialog(String hint) {
        final HintDialog myDialog = new HintDialog(getActivity());
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

    private void rouletteGone(int visible1, int visible2, int visible3) {
        v_roulette.setVisibility(visible1);
        v_sicbo.setVisibility(visible2);
        v_dt.setVisibility(visible3);

        ll_roulette.setVisibility(visible1);
        ll_sicbo.setVisibility(visible2);
        ll_dt.setVisibility(visible3);

        ll_roulette_1.setVisibility(visible1);
        ll_sicbo_1.setVisibility(visible2);
        ll_dt_1.setVisibility(visible3);
    }
}
