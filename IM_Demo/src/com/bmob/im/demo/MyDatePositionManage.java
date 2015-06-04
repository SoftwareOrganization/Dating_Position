package com.bmob.im.demo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.listener.FindListener;

import com.bmob.im.demo.adapter.DatePositionAdapter;
import com.bmob.im.demo.bean.DatePosition;
import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.ui.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug.FlagToString;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MyDatePositionManage extends BaseActivity implements OnItemClickListener{
	
	ListView mList;
	DatePositionAdapter mAdapter;
	List<DatePosition> mData;
	
	User currentUser;
	
	Boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_date_position_manage);
		
		initView();
	}
	
	private void initView() {
		initTopBarForLeft("聚会管理");
		
		currentUser = CustomApplcation.getInstance().getCurrentuser();
		
		mList = (ListView) findViewById(R.id.date_list);
		mData = new ArrayList<DatePosition>();
		mAdapter = new DatePositionAdapter(mData, MyDatePositionManage.this);
		mList.setAdapter(mAdapter);
		
		mList.setOnItemClickListener(this);
		
		fetchData();
	}
	
	public void fetchData() {
		
		BmobQuery<DatePosition> query = new BmobQuery<DatePosition>();
		ShowLog("正在获取数据");
		query.addWhereRelatedTo("myDates", new BmobPointer(currentUser));
		query.findObjects(MyDatePositionManage.this, new FindListener<DatePosition>() {
			
			@Override
			public void onSuccess(List<DatePosition> arg0) {
				// TODO Auto-generated method stub
				mData.addAll(arg0);
				if (arg0.size() > 0) {
					ShowLog("数量为："  + arg0.size());
					
					mAdapter.updateData(mData);
				}
				
				else {
					ShowLog("没有查询到数据");
					mAdapter.updateData(mData);
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		DatePosition date = (DatePosition) mAdapter.getItem(position);
		Intent intent = new Intent();
		intent.setClass(MyDatePositionManage.this, DateDetailsActivity.class);
		intent.putExtra("date", date);
		startActivity(intent);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if (flag == true) {
			mData.clear();
			fetchData();
		}
		
		flag = true;
	}
}
