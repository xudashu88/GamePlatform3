package com.purity.yu.gameplatform.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Contact;
import com.purity.yu.gameplatform.utils.ImgUtils;
import com.purity.yu.gameplatform.utils.ProtocolUtil;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<Contact> dataList;
    private OnItemClickListener mOnItemClickListener = null;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public ContactAdapter(Context context, List<Contact> dataList) {
        this.mContext = context;
        this.dataList = dataList;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_WeChat_link.setText(dataList.get(position).remark);
        String base = SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE);
        String icon_pre = base.substring(0, base.length() - 4);
        String url = dataList.get(position).url.replace("\\", "");
// http://103.112.29.145/admin/uploads/images/20190330/QQ%E6%88%AA%E5%9B%BE20190330112557.png
        Glide.with(mContext).load(icon_pre + url).into(holder.iv_WeChat_link);
        holder.iv_WeChat_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(holder.iv_WeChat_link);
            }
        });
    }

    private void saveImage(ImageView imageView) {
        if (imageView.getBackground() == null) {
            ToastUtil.show(mContext,"未上传图片");
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
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

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * 响应点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    /**
     * 设置listener事件并初始化
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_WeChat_link;
        ImageView iv_WeChat_link;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_WeChat_link = (TextView) itemView.findViewById(R.id.tv_WeChat_link);
            iv_WeChat_link = (ImageView) itemView.findViewById(R.id.iv_WeChat_link);
        }
    }

    /**
     * 定义点击事件的接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}