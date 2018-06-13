package com.chuangku.gameplatform3.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuangku.gameplatform3.R;

/**
 *https://blog.csdn.net/hacker_crazy/article/details/79023895 Gridview多次测绘导致界面卡顿解决方法
 */
public class GridLeftLiveAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater layoutInflater;

	public GridLeftLiveAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		layoutInflater = ((Activity) mContext).getWindow().getLayoutInflater();
	}

	@Override
	public int getCount() {
		return 66;
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
        ViewHolder holder=null;
		if (convertView == null) {
            holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_left_live, parent, false);
//			convertView =layoutInflater.inflate(R.layout.item_grid_left,  null);
//            holder.iv_item = convertView.findViewById(R.id.iv_item);
//            holder.tv_dot_left_top = convertView.findViewById(R.id.tv_dot_left_top);
//            holder.tv_dot_right_bottom = convertView.findViewById(R.id.tv_dot_right_bottom);
            convertView.setTag(holder);
		}else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if(((OneMeasureGirdView)parent).isOnMeasure){
//            return convertView;
//        }
		return convertView;
	}

	public static class ViewHolder{
	    public TextView tv_dot_left_top;
	    public TextView tv_dot_right_bottom;
	    public ImageView iv_item;
    }

}
