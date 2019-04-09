package com.purity.yu.gameplatform.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Bank;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.request.RequestParams;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<Bank> dataList;
    private OnItemClickListener mOnItemClickListener = null;
    private LayoutInflater layoutInflater;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public BankAdapter(Context context, List<Bank> dataList) {
        this.mContext = context;
        this.dataList = dataList;
        layoutInflater = ((Activity) mContext).getWindow().getLayoutInflater();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.ll_title.setBackgroundResource(R.drawable.select_bank1);
        } else
            holder.ll_title.setBackgroundResource(R.drawable.select_bank2);
        holder.tv_name.setText(dataList.get(position).bank_username);
        holder.tv_bank_account.setText(dataList.get(position).bank_account);
        holder.tv_bank_name.setText(dataList.get(position).bank_name);
        holder.tv_branch_name.setText(dataList.get(position).mobile);
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDeleteMessageDialog(dataList.get(position).id);
            }
        });
    }

    private void initDeleteMessageDialog(final int id) {
        View layout12 = layoutInflater.inflate(R.layout.dialog_delete_message, null);
        final AlertDialog dialog = Util.getInstance().setDialog(mContext, R.style.dialog, layout12, 0.45f, 0.0f, 0.6f, 0, 0, Gravity.CENTER, Gravity.CENTER_HORIZONTAL, 0);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Util.getInstance().init(mContext);
                Util.getInstance().hideSystemNavigationBar();
            }
        });
        TextView tv_cancel = layout12.findViewById(R.id.tv_cancel);
        TextView tv_confirm = layout12.findViewById(R.id.tv_confirm);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRemove(id, dialog);
            }
        });
    }

    private void postRemove(int id, final AlertDialog dialog) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE) + Constant.BANK_REMOVE + "?token=" + token, new RequestParams(map)),//perfect
                new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        JSONObject json = null;
                        JSONObject __data = null;
                        try {
                            json = new JSONObject(s);
                            int code = json.optInt("code");
                            String data = json.optString("data");
                            if (code != 0) {
                                ToastUtil.show(mContext, data);
                                return;
                            }
                            dialog.dismiss();
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
        TextView tv_name;
        LinearLayout ll_title;
        TextView tv_bank_account;
        TextView tv_bank_name;
        TextView tv_branch_name;
        TextView tv_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            ll_title = (LinearLayout) itemView.findViewById(R.id.ll_title);
            tv_bank_account = (TextView) itemView.findViewById(R.id.tv_bank_account);
            tv_bank_name = (TextView) itemView.findViewById(R.id.tv_bank_name);
            tv_branch_name = (TextView) itemView.findViewById(R.id.tv_branch_name);
            tv_delete = (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }

    /**
     * 定义点击事件的接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}