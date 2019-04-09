package com.purity.yu.gameplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.purity.yu.gameplatform.R;

public class ArcadeAdapter extends BaseAdapter {

    private Context mContext;
    private int mImageViewArray[] = {};

    public ArcadeAdapter(Context context, int[] mImageViewArray) {
        this.mContext = context;
        this.mImageViewArray = mImageViewArray;
    }

    @Override
    public int getCount() {
        return mImageViewArray.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_arcade_vertical, parent, false);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
//        ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
        holder.iv.setBackgroundResource(mImageViewArray[position]);
        return convertView;
    }

    static class ViewHolder {
        ImageView iv;
    }
}