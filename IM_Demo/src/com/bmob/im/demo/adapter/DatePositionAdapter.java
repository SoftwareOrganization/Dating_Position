package com.bmob.im.demo.adapter;

import java.util.List;

import cn.bmob.push.lib.service.mine;

import com.bmob.im.demo.R;
import com.bmob.im.demo.bean.DatePosition;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Parcelable.Creator;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DatePositionAdapter extends BaseAdapter {
	
	private List<DatePosition> mData;
	private Context mContext;
	public DatePositionAdapter(List<DatePosition> mData, Context mContext) {
		super();
		this.mData = mData;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}   

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.date_position_list_item, null);
		}
		
		ImageView date_creator = (ImageView) convertView.findViewById(R.id.creator_avatar);
		TextView tv_date_name = (TextView) convertView.findViewById(R.id.date_name);
		TextView tv_date_time = (TextView) convertView.findViewById(R.id.date_time);
		TextView tv_date_desc = (TextView) convertView.findViewById(R.id.date_tdesc);
		
		ImageLoader.getInstance().displayImage(mData.get(position).getCreatorAvatar(), date_creator);
		tv_date_name.setText(mData.get(position).getDateName());
		tv_date_time.setText(mData.get(position).getDateTime().getDate());
		tv_date_desc.setText(mData.get(position).getDateDesc());
		
		return convertView;
	}
	
	public void updateData(List<DatePosition> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

}
