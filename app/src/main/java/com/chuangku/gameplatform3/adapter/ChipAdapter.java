package com.chuangku.gameplatform3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;
import com.chuangku.gameplatform3.entity.Chip;
import com.chuangku.gameplatform3.entity.Game;
import com.chuangku.gameplatform3.widget.ChoiceItemLayout;

import java.util.ArrayList;
import java.util.List;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ViewHolder> {

    private Context mContext;
    //    int[] new_chipNumberArr = {10, 20, 50, 100, 200, 500, 1000, 5000, 10000, 50000};
//    int[] new_chipImgArr = {R.drawable.new_chip_01, R.drawable.new_chip_02, R.drawable.new_chip_03, R.drawable.new_chip_04,
//            R.drawable.new_chip_05, R.drawable.new_chip_06, R.drawable.new_chip_07, R.drawable.new_chip_08, R.drawable.new_chip_09, R.drawable.new_chip_10};
    private List<Chip> chipList;

    public ChipAdapter(Context context, List<Chip> chipList) {
        this.mContext = context;
        this.chipList = chipList;
    }

    public void updateData(ArrayList<Chip> data) {
        this.chipList = data;
        notifyDataSetChanged();
    }

    @Override
    public ChipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip, parent, false);
        ChipAdapter.ViewHolder viewHolder = new ChipAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChipAdapter.ViewHolder holder, final int position) {
        Chip chip = chipList.get(position);
        holder.tv_new_chip.setText("" + chip.chipNumber);
        holder.iv_new_chip.setBackground(mContext.getResources().getDrawable(chip.chipImg));

        ChoiceItemLayout layout = (ChoiceItemLayout) holder.itemView;
        layout.setChecked(chip.isCheck);

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, position);
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
        TextView tv_new_chip;
        LinearLayout ll_chip_item;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_new_chip = (TextView) itemView.findViewById(R.id.tv_chip);
            iv_new_chip = (ImageView) itemView.findViewById(R.id.iv_chip);
            ll_chip_item = (LinearLayout) itemView.findViewById(R.id.ll_chip_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}