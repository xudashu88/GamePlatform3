package com.purity.yu.gameplatform.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gangbeng.basemodule.utils.SharedPreUtil;
import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.base.Constant;
import com.purity.yu.gameplatform.entity.Chip;
import com.purity.yu.gameplatform.widget.ChoiceItemLayout2;

import java.util.List;

public class ChipAdapter2 extends RecyclerView.Adapter<ChipAdapter2.ViewHolder> {

    private Context mContext;
    private List<Chip> chipList;

    public ChipAdapter2(Context context, List<Chip> chipList) {
        this.mContext = context;
        this.chipList = chipList;
    }

    public void updateData(List<Chip> data) {
        this.chipList = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip2, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Chip chip = chipList.get(position);
        holder.iv_new_chip.setBackground(mContext.getResources().getDrawable(chip.chipImg));
        ChoiceItemLayout2 layout = (ChoiceItemLayout2) holder.itemView;
        layout.setChecked(chip.isCheck);
        layout.anim(chip.isCheck,holder.iv_new_chip);

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, position, holder.iv_new_chip);
                    SharedPreUtil.getInstance(mContext).saveParam(Constant.CHIP_POSITION, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chipList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_new_chip;
        LinearLayout ll_chip_item;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_new_chip = (ImageView) itemView.findViewById(R.id.iv_chip);
            ll_chip_item = (LinearLayout) itemView.findViewById(R.id.ll_chip_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, ImageView imageView);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}