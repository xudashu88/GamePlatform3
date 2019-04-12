package com.purity.yu.gameplatform.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.twowaygallery.TwoWayAdapterView;
import com.purity.twowaygallery.flow.CoverFlow;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.ArcadeAdapter;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;

import butterknife.BindView;

/**
 * 街机--左边菜单
 */
@ContentView(R.layout.fragment_arcade)
public class ArcadeFragment extends BaseFragment {

    @BindView(R.id.gallery_vertical)
    CoverFlow gallery_vertical;
    @BindView(R.id.iv_arrow_top)
    ImageView iv_arrow_top;
    @BindView(R.id.iv_arrow_bottom)
    ImageView iv_arrow_bottom;
    @BindView(R.id.tv_room_name)
    TextView tv_room_name;
    @BindView(R.id.frame_layout)
    FrameLayout frame_layout;
    @BindView(R.id.rl_select_fish_rome)
    RelativeLayout rl_select_fish_rome;

    private Context mContext;
    private ArcadeAdapter mAdapter;

    private Fragment currentFragment = new Fragment();
    private int mImageViewArray[] = {R.drawable.arcade_0, R.drawable.arcade_1, R.drawable.arcade_2,
            R.drawable.arcade_3, R.drawable.arcade_4, R.drawable.arcade_5};

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        int fishRome = SharedPreUtil.getInstance(mContext).getInt(Constant.FISH_ROOM_COUNT);
        fishRome = 1;
        if (fishRome == 0) {
            tv_room_name.setText("暂未开放，敬请期待！");
            rl_select_fish_rome.setVisibility(View.GONE);
            frame_layout.setVisibility(View.GONE);
        } else {
            mAdapter = new ArcadeAdapter(getActivity(), mImageViewArray);
            gallery_vertical.setAdapter(mAdapter);
            initEvent();
        }
    }

    private void initEvent() {
        gallery_vertical.setOnItemSelectedListener(new TwoWayAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TwoWayAdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    iv_arrow_top.setVisibility(View.VISIBLE);
                    iv_arrow_bottom.setVisibility(View.INVISIBLE);
                } else if (position == (mImageViewArray.length - 1)) {
                    iv_arrow_top.setVisibility(View.INVISIBLE);
                    iv_arrow_bottom.setVisibility(View.VISIBLE);
                } else {
                    iv_arrow_top.setVisibility(View.VISIBLE);
                    iv_arrow_bottom.setVisibility(View.VISIBLE);
                }
                if (position == 0) {
                    switchFragment(new ArcadeCrocodileFragment()).commit();//鳄鱼公园 测试
                    tv_room_name.setText("互联网");
                    SharedPreUtil.getInstance(getActivity()).saveParam("ROBOT_FISH_ROOM", "10000000000000004");
                } else if (position == 1) {
                    switchFragment(new ArcadeCrazyFragment()).commit();//愤怒的小鸟
                    tv_room_name.setText("局域网");
                    SharedPreUtil.getInstance(getActivity()).saveParam("ROBOT_FISH_ROOM", "10000000000000004");
                } else if (position == 2) {
                    switchFragment(new ArcadeNullFragment()).commit();//马上有钱
                    tv_room_name.setText("房间" + (position + 1));
                } else if (position == 3) {
                    switchFragment(new ArcadeBeautyFragment()).commit();//美女野兽
                    tv_room_name.setText("房间" + (position + 1));
                } else if (position == 4) {
                    switchFragment(new ArcadeTestFragment()).commit();//渔乐世界
                    tv_room_name.setText("房间" + (position + 1));
                } else if (position == 5) {
                    switchFragment(new ArcadeRoboraptorFragment()).commit();//海王2
                    tv_room_name.setText("房间" + (position + 1));
                }
            }

            @Override
            public void onNothingSelected(TwoWayAdapterView<?> parent) {
            }
        });
    }

    //Fragment优化
    public FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.frame_layout, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }
}
