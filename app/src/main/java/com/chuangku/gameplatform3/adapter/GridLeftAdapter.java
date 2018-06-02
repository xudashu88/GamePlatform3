package com.chuangku.gameplatform3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chuangku.gameplatform3.R;

/**
 *
 */
public class GridLeftAdapter extends BaseAdapter {
	private Context mContext;


	public GridLeftAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return 60;
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
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_grid_left, parent, false);
		}
//		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
//		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
//		iv.setBackgroundResource(imgs[position]);

//		tv.setText(img_text[position]);
		return convertView;
	}

}
