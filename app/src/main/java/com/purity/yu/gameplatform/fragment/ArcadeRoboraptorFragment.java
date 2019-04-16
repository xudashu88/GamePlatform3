package com.purity.yu.gameplatform.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;
import com.xmplayer.RobotFishActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 街机 渔乐世界
 */
@ContentView(R.layout.fragment_arcade_roboraptor)
public class ArcadeRoboraptorFragment extends BaseFragment {

    @BindView(R.id.puyu_seat_1)
    ImageView puyu_seat_1;
    @BindView(R.id.puyu_seat_2)
    ImageView puyu_seat_2;
    @BindView(R.id.puyu_seat_3)
    ImageView puyu_seat_3;
    @BindView(R.id.puyu_seat_4)
    ImageView puyu_seat_4;
    @BindView(R.id.puyu_seat_5)
    ImageView puyu_seat_5;
    @BindView(R.id.puyu_seat_6)
    ImageView puyu_seat_6;
    @BindView(R.id.puyu_seat_7)
    ImageView puyu_seat_7;
    @BindView(R.id.puyu_seat_8)
    ImageView puyu_seat_8;
    @BindView(R.id.puyu_seat_9)
    ImageView puyu_seat_9;
    @BindView(R.id.puyu_seat_10)
    ImageView puyu_seat_10;

    private List<ImageView> imageViewList = new ArrayList<>();
    private List<String> _strList = new ArrayList<>();
    private Context mContext;
    private float scaleX = 0.0f;
    private float scaleY = 0.0f;

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        addImageView(puyu_seat_1);
        addImageView(puyu_seat_2);
        addImageView(puyu_seat_3);
        addImageView(puyu_seat_4);
        addImageView(puyu_seat_5);
        addImageView(puyu_seat_6);
        addImageView(puyu_seat_7);
        addImageView(puyu_seat_8);
        addImageView(puyu_seat_9);
        addImageView(puyu_seat_10);
        initTableView();
    }
    private void initTableView() {
        String _table = SharedPreUtil.getInstance(getActivity()).getString(Constant.ROBORAPTOR);
        List<String> _strList = Util.stringToList(_table);
        for (ImageView iv : imageViewList) {
            String _tag = (String) iv.getTag();
            if (_strList.size() > 0) {
                for (int i = 0; i < _strList.size(); i++) {
                    if (_tag.equals(_strList.get(i))) {
                        iv.setBackgroundResource(R.drawable.arcade_puyu_seat);
                    }
                }
            }
        }
    }

    private void addImageView(ImageView iv) {
        imageViewList.add(iv);
        setOnClick(iv);
    }

    private void setOnClick(ImageView iv) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _table = SharedPreUtil.getInstance(getActivity()).getString(Constant.ROBORAPTOR);
                _strList = Util.stringToList(_table);

                ImageView _iv = (ImageView) v;
                if (_iv != null) {
                    _iv.setBackgroundResource(R.drawable.arcade_puyu_seat);
                    String tag = (String) _iv.getTag();
                    for (String str : _strList) {
                        if (str.equals(tag)) {
//                            ToastUtil.showToast(mContext.getResources().getString(R.string.playing));
//                            return;
                        }
                    }
//                    Intent intent = new Intent(mContext, XMPlayerActivity.class);
//                    intent.putExtra("tag", tag);
//                    intent.putExtra("ip", Constant.ROBORAPTOR_IP);

                    Intent intent = new Intent(mContext, RobotFishActivity.class);
                    intent.putExtra("tag", tag);
                    intent.putExtra("ip", Constant.FISH_5);
                    mContext.startActivity(intent);
                    //UnsupportedOperationException
                    List<String> list = new ArrayList<>(_strList);//由於Arrays.asList返回的List没有重写add和remove方法，所以需要转换成常规的ArrayList
                    list.add(tag);
                    StringBuffer sb = new StringBuffer();
                    for (String str : list) {
                        sb.append(str + ",");
                    }
                    SharedPreUtil.getInstance(getActivity()).saveParam(Constant.ROBORAPTOR, sb.toString());
                }
            }
        });
    }
}
