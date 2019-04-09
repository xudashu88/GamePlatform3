package com.purity.yu.gameplatform.adapter;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gangbeng.basemodule.base.CommonRecyclerViewAdapter;
import com.gangbeng.basemodule.base.CommonRecyclerViewHolder;
import com.gangbeng.basemodule.utils.Util;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.entity.QuotaRecords;

import java.util.ArrayList;
import java.util.List;

public class QuotaRecordAdapter extends CommonRecyclerViewAdapter<QuotaRecords> {
    private static final String TAG = "QuotaRecordAdapter";
    private Context mContext;
    private List<QuotaRecords> recordsList = new ArrayList<>();

    public QuotaRecordAdapter(Context context, List<QuotaRecords> data) {
        super(context, data);
        this.mContext = context;
        this.recordsList = data;
    }

    @Override
    public void convert(CommonRecyclerViewHolder h, final QuotaRecords entity, final int position) {
        final LinearLayout ll_title = h.getView(R.id.ll_title);
        final TextView tv_transaction_no = h.getView(R.id.tv_transaction_no);
        final TextView tv_operate_time = h.getView(R.id.tv_operate_time);
        final TextView tv_record_type = h.getView(R.id.tv_record_type);
        final TextView tv_income = h.getView(R.id.tv_income);
        final TextView tv_outcome = h.getView(R.id.tv_outcome);
        final TextView tv_after_transaction = h.getView(R.id.tv_after_transaction);
        if (position % 2 == 0) {
            ll_title.setBackgroundResource(R.color.betRecord0);
        } else
            ll_title.setBackgroundResource(R.color.betRecord1);
        tv_transaction_no.setText(entity.transactionNo);
        String time = Util.formatLongToTime(entity.operateTime, "yyyy-MM-dd \n HH:mm:ss");
        tv_operate_time.setText(time);
        /**
         * 下注类型
         * 1、下注
         * 2、清算
         * 3、充值
         * 4、提现
         * 5、返金
         */
        if (entity.tradeTypeId.equals("1")) {
            tv_record_type.setText(mContext.getResources().getString(R.string.deposit));
        } else if (entity.tradeTypeId.equals("2")) {
            tv_record_type.setText(mContext.getResources().getString(R.string.withdrawal));
        } else if (entity.tradeTypeId.equals("3")) {
            tv_record_type.setText(mContext.getResources().getString(R.string.upper_score));
        } else if (entity.tradeTypeId.equals("4")) {
            tv_record_type.setText(mContext.getResources().getString(R.string.sub_score));
        } else if (entity.tradeTypeId.equals("5")) {
            tv_record_type.setText(mContext.getResources().getString(R.string.manual_add));
        } else if (entity.tradeTypeId.equals("6")) {
            tv_record_type.setText(mContext.getResources().getString(R.string.manual_decrease));
        }
        if (entity.recordType.equals("1")) {
            tv_income.setText(entity.amount);
            tv_outcome.setText("");
        } else if (entity.recordType.equals("2")) {
            tv_outcome.setText(entity.amount);
            tv_income.setText("");
        }
        tv_after_transaction.setText(entity.afterTransaction);
    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_quota_record;
    }
}