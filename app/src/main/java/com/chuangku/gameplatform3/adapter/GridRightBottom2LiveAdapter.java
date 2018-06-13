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
 *
 */
public class GridRightBottom2LiveAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater layoutInflater;

	public GridRightBottom2LiveAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		layoutInflater = ((Activity) mContext).getWindow().getLayoutInflater();
	}

	@Override
	public int getCount() {
		return 51;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_right_bottom2_live, parent, false);
//			convertView =layoutInflater.inflate(R.layout.item_grid_right_bottom2,  null);
//            holder.tv_left_top = convertView.findViewById(R.id.tv_left_top);
//            holder.tv_right_top = convertView.findViewById(R.id.tv_right_top);
//            holder.tv_left_bottom = convertView.findViewById(R.id.tv_left_bottom);
//            holder.tv_right_bottom = convertView.findViewById(R.id.tv_right_bottom);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
//		if(((OneMeasureGirdView)parent).isOnMeasure){
//			return convertView;
//		}
		return convertView;
	}

	public static class ViewHolder{
		public TextView tv_left_top;
		public TextView tv_right_top;
		public ImageView tv_left_bottom;
		public ImageView tv_right_bottom;
	}

}
