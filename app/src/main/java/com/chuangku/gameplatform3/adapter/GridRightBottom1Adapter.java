package com.chuangku.gameplatform3.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;

/**
 *
 */
public class GridRightBottom1Adapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;

    public GridRightBottom1Adapter(Context mContext) {
        super();
        this.mContext = mContext;
        layoutInflater = ((Activity) mContext).getWindow().getLayoutInflater();
    }

    @Override
    public int getCount() {
        return 45;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_right_bottom1, parent, false);
            holder.tv_left_top = convertView.findViewById(R.id.tv_left_top);
            holder.tv_right_top = convertView.findViewById(R.id.tv_right_top);
            holder.tv_left_bottom = convertView.findViewById(R.id.tv_left_bottom);
            holder.tv_right_bottom = convertView.findViewById(R.id.tv_right_bottom);
            final ViewHolder _v = holder;
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    if (position < 45) {
						_v.tv_left_top.setBackground(mContext.getResources().getDrawable(R.drawable.solid_circle_blue));
						_v.tv_right_top.setBackground(mContext.getResources().getDrawable(R.drawable.solid_circle_blue));
						_v.tv_left_bottom.setBackground(mContext.getResources().getDrawable(R.drawable.solid_circle_red));
						_v.tv_right_bottom.setBackground(mContext.getResources().getDrawable(R.drawable.solid_circle_red));
//                    } else {
//						_v.tv_left_top.setBackground(mContext.getResources().getDrawable(R.drawable.img_line_blue));
//						_v.tv_right_top.setBackground(mContext.getResources().getDrawable(R.drawable.img_line_blue));
//						_v.tv_left_bottom.setBackground(mContext.getResources().getDrawable(R.drawable.img_line_red));
//						_v.tv_right_bottom.setBackground(mContext.getResources().getDrawable(R.drawable.img_line_red));
//                    }
                }
            });


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//		if(((OneMeasureGirdView)parent).isOnMeasure){
//			return convertView;
//		}
        return convertView;
    }

    public static class ViewHolder {
        public TextView tv_left_top;
        public TextView tv_right_top;
        public TextView tv_left_bottom;
        public TextView tv_right_bottom;
    }

}
