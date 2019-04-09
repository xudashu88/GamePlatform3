package com.purity.yu.gameplatform.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.purity.yu.gameplatform.R;
import com.purity.yu.gameplatform.entity.Chip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 筹码堆叠
 */
public class ChipingAdapter extends RecyclerView.Adapter<ChipingAdapter.ViewHolder> {

    private Context mContext;
    private List<Chip> chipList;

    public ChipingAdapter(Context context, List<Chip> chipList) {
        this.mContext = context;
        this.chipList = chipList;
    }

    public void updateData(List<Chip> data) {
        this.chipList = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chiping, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    /**
     * 10 20 50 100 200 500 1000 5000 10000 50000
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Chip chip = chipList.get(position);
        holder.iv_chip_number.setBackground(mContext.getResources().getDrawable(getNumber(chip.chipNumber)));
    }

    private int getNumber(int number) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(10, R.drawable.new_chip_10);
        map.put(20, R.drawable.new_chip_20);
        map.put(50, R.drawable.new_chip_50);
        map.put(100, R.drawable.new_chip_100);
        map.put(200, R.drawable.new_chip_200);
        map.put(500, R.drawable.new_chip_500);
        map.put(5000, R.drawable.new_chip_5000);
        map.put(10000, R.drawable.new_chip_10000);
        map.put(50000, R.drawable.new_chip_50000);
        return map.get(number);
    }

    @Override
    public int getItemCount() {
        return chipList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_chip_number;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_chip_number = (ImageView) itemView.findViewById(R.id.iv_chip_number);
        }
    }
}