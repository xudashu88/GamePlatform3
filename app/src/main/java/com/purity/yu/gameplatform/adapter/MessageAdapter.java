package com.purity.yu.gameplatform.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.base.CommonRecyclerViewAdapter;
import com.gangbeng.basemodule.base.CommonRecyclerViewHolder;
import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.gangbeng.basemodule.utils.ToastUtil;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.base.ServiceIpConstant;
import com.purity.yu.gameplatform.entity.Message;
import com.purity.yu.gameplatform.event.ObjectEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.gcg.GdroidSdk.okhttp.client.CommonOkhttpClient;
import me.gcg.GdroidSdk.okhttp.exception.OkHttpException;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataHandle;
import me.gcg.GdroidSdk.okhttp.listener.DisposeDataListener;
import me.gcg.GdroidSdk.okhttp.request.CommonRequest;
import me.gcg.GdroidSdk.okhttp.request.RequestParams;
import me.gcg.GdroidSdk.okhttp.response.CommonJsonCallback;

public class MessageAdapter extends CommonRecyclerViewAdapter<Message> {
    private static final String TAG = "MessageAdapter";
    private Context mContext;
    private List<Message> messageList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public MessageAdapter(Context context, List<Message> data) {
        super(context, data);
        this.mContext = context;
        this.messageList = data;
        layoutInflater = ((Activity) mContext).getWindow().getLayoutInflater();
    }

    public void update(List<Message> hallList) {
        this.messageList.clear();
        this.messageList.addAll(hallList);
        notifyDataSetChanged();
    }

    @Override
    public void convert(CommonRecyclerViewHolder h, final Message entity, final int position) {
        final LinearLayout ll_title = h.getView(R.id.ll_title);
        final TextView tv_title = h.getView(R.id.tv_title);
        final TextView tv_content = h.getView(R.id.tv_content);
        final TextView tv_sender = h.getView(R.id.tv_sender);
        final TextView tv_send_time = h.getView(R.id.tv_send_time);
        final TextView tv_look = h.getView(R.id.tv_look);
        final TextView tv_delete = h.getView(R.id.tv_delete);
        RelativeLayout rl_new_message = h.getView(R.id.rl_new_message);
        final ImageView iv_new_message = h.getView(R.id.iv_new_message);
        if (position % 2 == 0) {
            ll_title.setBackgroundResource(R.color.betRecord0);
        } else
            ll_title.setBackgroundResource(R.color.betRecord1);
        if (entity.is_read == 0) {
            iv_new_message.setVisibility(View.VISIBLE);
            iv_new_message.setBackgroundResource(R.drawable.triangle);
        } else {
            iv_new_message.setVisibility(View.GONE);
            iv_new_message.setBackgroundResource(0);
        }
        tv_title.setText(entity.title);
        tv_content.setText(entity.content);
        tv_sender.setText(entity.sender_name);
        String time = Util.formatLongToTime(entity.created_at, "yyyy-MM-dd \n HH:mm:ss");
        tv_send_time.setText(time);
        tv_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMessageDetailDialog(entity.id);
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDeleteMessageDialog(entity.id, position);
            }
        });
        rl_new_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLook(entity.id, iv_new_message);
            }
        });
    }

    private void initMessageDetailDialog(int id) {
        View layout12 = layoutInflater.inflate(R.layout.dialog_message_detail, null);
        AlertDialog dialog = Util.getInstance().setDialog(mContext, R.style.dialog, layout12, 0.65f, 0f, 0.6f, 0, 0, Gravity.CENTER, Gravity.CENTER_HORIZONTAL, 0);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Util.getInstance().init(mContext);
                Util.getInstance().hideSystemNavigationBar();
            }
        });
        TextView tv_title = layout12.findViewById(R.id.tv_title);
        TextView tv_content = layout12.findViewById(R.id.tv_content);
        TextView tv_sender = layout12.findViewById(R.id.tv_sender);
        TextView tv_send_time = layout12.findViewById(R.id.tv_send_time);
//        postLook(id/*, tv_title, tv_content, tv_sender, tv_send_time*/);
    }

    private void initDeleteMessageDialog(final int id, final int position) {
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
                postRemove(id, dialog, position);
            }
        });
    }

    private void postRemove(int id, final AlertDialog dialog, final int position) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE)+Constant.MESSAGE_REMOVE + "?token=" + token, new RequestParams(map)),//perfect
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
//                            updateList(messageList);
//                            notifyItemChanged(position);
                            ObjectEvent.notifyItemChangedEvent event = new ObjectEvent.notifyItemChangedEvent();
                            event.position = position;
                            EventBus.getDefault().post(event);
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
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

    private void postLook(int id, final ImageView imageView) {
        String token = SharedPreUtil.getInstance(mContext).getString(Constant.USER_TOKEN);
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(SharedPreUtil.getInstance(mContext).getString(ServiceIpConstant.BASE)+Constant.MESSAGE_DETAIL + "?token=" + token + "&id=" + id/* + "&startDate=" + startDate + "&endDate=" + endDate*/, null),//perfect
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
                            String _data = json.optString("data");
                            __data = new JSONObject(_data);
                            imageView.setVisibility(View.GONE);
                            imageView.setBackgroundResource(0);
                            int unRead = SharedPreUtil.getInstance(mContext).getInt("unRead");
                            SharedPreUtil.getInstance(mContext).saveParam("unRead", --unRead);
                            ToastUtil.show(mContext, "朕已阅");
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_message;
    }
}