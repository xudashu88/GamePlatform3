package com.purity.yu.gameplatform.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.twowaygallery.TwoWayAdapterView;
import com.purity.twowaygallery.flow.CoverFlow;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.adapter.ArcadeAdapter;
import com.purity.yu.gameplatform.annotation.ContentView;
import com.purity.yu.gameplatform.base.BaseFragment;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.utils.BaccaratUtil;
import com.purity.yu.gameplatform.utils.ImgUtils;
import com.purity.yu.gameplatform.utils.ProtocolUtil;

import butterknife.BindView;

/**
 * 街机--左边菜单
 */
@ContentView(R.layout.fragment_contact_serve)
public class ContactServeFragment extends BaseFragment {

    @BindView(R.id.iv_WeChat_link)
    ImageView iv_WeChat_link;
    @BindView(R.id.tv_WeChat_link)
    TextView tv_WeChat_link;
    private Context mContext;

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        tv_WeChat_link.setText("AGkf0001");
        Glide.with(mContext).load(R.drawable.wechat_contact1).into(iv_WeChat_link);
        iv_WeChat_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });
    }

    private void saveImage() {
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), iv_pay_code.getId());
        Bitmap bitmap = ((BitmapDrawable) iv_WeChat_link.getDrawable()).getBitmap();
        boolean isSaveSuccess = ImgUtils.saveImageToGallery(mContext, bitmap);
        if (isSaveSuccess) {
            Toast.makeText(mContext, "已成功保存至" + SharedPreUtil.getInstance(mContext).getString("IMAGE_URL"), Toast.LENGTH_SHORT).show();
            ProtocolUtil.getInstance().hintDialog(mContext, "操作流程：\n" +
                    "1.微信点击扫一扫，右上角点击后选择从相册选取二维码。\n" +
                    "2.支付宝点击扫一扫，右上角的相册。");
        } else {
            Toast.makeText(mContext, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }
}
