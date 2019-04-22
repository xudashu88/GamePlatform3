package com.purity.yu.gameplatform.fragment;

import android.view.View;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;

import butterknife.BindView;

@ContentView(R.layout.fragment_menu_other)
public class MenuOtherFragment extends BaseFragment {

    @BindView(R.id.tv_video_switch_off)
    TextView tv_video_switch_off;
    @BindView(R.id.tv_video_switch_on)
    TextView tv_video_switch_on;
    @BindView(R.id.tv_push_switch_off)
    TextView tv_push_switch_off;
    @BindView(R.id.tv_push_switch_on)
    TextView tv_push_switch_on;
    @BindView(R.id.tv_robot_switch_off)
    TextView tv_robot_switch_off;
    @BindView(R.id.tv_robot_switch_on)
    TextView tv_robot_switch_on;

    @Override
    protected void initView(View view) {
        initEvent();
        initData();
    }

    private void initData() {
        int _videoSwitch = SharedPreUtil.getInstance(getActivity()).getInt(Constant.VIDEO_SWITCH);
        int _pushSwitch = SharedPreUtil.getInstance(getActivity()).getInt(Constant.PUSH_SWITCH);
        int _robotSwitch = SharedPreUtil.getInstance(getActivity()).getInt(Constant.ROBOT_SWITCH);
        if (_videoSwitch == 0) {
            videoSwitch(View.VISIBLE, View.GONE);
        } else if (_videoSwitch == 1) {
            videoSwitch(View.GONE, View.VISIBLE);
        }
        if (_pushSwitch == 0) {
            pushSwitch(View.VISIBLE, View.GONE);
        } else if (_pushSwitch == 1) {
            pushSwitch(View.GONE, View.VISIBLE);
        }
        if (_robotSwitch == 1) {
            robotSwitch(View.VISIBLE, View.GONE);
        } else if (_robotSwitch == 0) {
            robotSwitch(View.GONE, View.VISIBLE);
        }
    }

    private void initEvent() {
        tv_video_switch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoSwitch(View.GONE, View.VISIBLE);
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.VIDEO_SWITCH, 1);
            }
        });
        tv_video_switch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoSwitch(View.VISIBLE, View.GONE);
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.VIDEO_SWITCH, 0);
            }
        });
        tv_push_switch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushSwitch(View.GONE, View.VISIBLE);
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.PUSH_SWITCH, 1);
            }
        });
        tv_push_switch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushSwitch(View.VISIBLE, View.GONE);
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.PUSH_SWITCH, 0);
            }
        });
        tv_robot_switch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotSwitch(View.GONE, View.VISIBLE);
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.ROBOT_SWITCH, 0);//默认关
            }
        });
        tv_robot_switch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                robotSwitch(View.VISIBLE, View.GONE);
                SharedPreUtil.getInstance(getActivity()).saveParam(Constant.ROBOT_SWITCH, 1);//和其他2个不同 1才为开
            }
        });
    }

    private void videoSwitch(int gone, int visible) {
        tv_video_switch_on.setVisibility(gone);
        tv_video_switch_off.setVisibility(visible);
    }

    private void pushSwitch(int gone, int visible) {
        tv_push_switch_on.setVisibility(gone);
        tv_push_switch_off.setVisibility(visible);
    }

    private void robotSwitch(int gone, int visible) {
        tv_robot_switch_on.setVisibility(gone);
        tv_robot_switch_off.setVisibility(visible);
    }
}
