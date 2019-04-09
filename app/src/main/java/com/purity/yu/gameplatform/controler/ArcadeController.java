package com.purity.yu.gameplatform.controler;

import android.content.Context;
import android.widget.ImageView;

import com.purity.twowaygallery.flow.CoverFlow;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.ArcadeAdapter;

/**
 * 功能：
 * 描述：
 * Create by purity on 2018/5/19.
 */
public class ArcadeController {
    private Context mContext;
    private static ArcadeController instance = null;
    private ArcadeAdapter mAdapter;

    private int mImageViewArray[] = {R.drawable.arcade_0, R.drawable.arcade_1, R.drawable.arcade_2,
            R.drawable.arcade_3, R.drawable.arcade_4, R.drawable.arcade_5};
    CoverFlow gallery_vertical;
    ImageView iv_arrow_top;
    ImageView iv_arrow_bottom;

    public void init(Context context) {
        mContext = context;
    }

    public static ArcadeController getInstance() {
        if (null == instance) {
            instance = new ArcadeController();
        }
        return instance;
    }

    public void copyWidget(CoverFlow gallery_vertical, ImageView iv_arrow_top, ImageView iv_arrow_bottom) {
        this.gallery_vertical = gallery_vertical;
        this.iv_arrow_top = iv_arrow_top;
        this.iv_arrow_bottom = iv_arrow_bottom;
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new ArcadeAdapter(mContext, mImageViewArray);
        gallery_vertical.setAdapter(mAdapter);
    }

}
