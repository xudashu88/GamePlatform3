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
public class GridRightTopAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;

    public GridRightTopAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        layoutInflater = ((Activity) mContext).getWindow().getLayoutInflater();
    }

    @Override
    public int getCount() {
        return 180;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_right_top, parent, false);
            holder.tv_dot = convertView.findViewById(R.id.tv_dot);
            holder.tv_line = convertView.findViewById(R.id.tv_line);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView tv_dot;
        public TextView tv_line;
    }
}
