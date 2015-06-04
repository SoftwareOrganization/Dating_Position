package com.bmob.im.demo;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.baidu.mapapi.utils.route.RouteParaOption.EBusStrategyType;
import com.bmob.im.demo.bean.DatePosition;
import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.ui.BaseActivity;
import com.bmob.im.demo.view.HeaderLayout.onRightImageButtonClickListener;
import com.bmob.im.demo.view.dialog.DialogTips;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DateDetailsActivity extends BaseActivity implements OnClickListener{
	
	TextView tv_date_creator, tv_date_name, tv_date_desc, tv_date_time, tv_date_position, tv_date_member;
	Button btn_delete, btn_exit, btn_start;

	DatePosition date;
	
	User currentUser;
	
	StringBuilder date_member = new StringBuilder();
	private boolean flag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_details);
		
		initView();
	}
	
	private void initView() {
		initTopBarForBoth("聚会详情", R.drawable.icon_edit, new onRightImageButtonClickListener() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				if (!currentUser.getObjectId().equals(date.getCreatorObjectId())) {
					ShowToast("您不是聚会创建者，没有权限修改聚会信息");
				}else {
					// 进入聚会修改界面
					Intent intent = new Intent();
					intent.setClass(DateDetailsActivity.this, EditDateActivity.class);
					intent.putExtra("date", date);
					startActivity(intent);
				}
			}
		});
		
		currentUser = CustomApplcation.getInstance().getCurrentuser();
		
		date = (DatePosition) getIntent().getSerializableExtra("date");
		
		tv_date_creator = (TextView) findViewById(R.id.date_details_date_creator);
		tv_date_name = (TextView) findViewById(R.id.date_details_date_name);
		tv_date_desc = (TextView) findViewById(R.id.date_details_date_desc);
		tv_date_time = (TextView) findViewById(R.id.date_details_date_time);
		tv_date_position = (TextView) findViewById(R.id.date_details_date_position);
		tv_date_member = (TextView) findViewById(R.id.date_details_date_member);
		
		btn_delete = (Button) findViewById(R.id.delete_date);
		btn_exit = (Button) findViewById(R.id.exit_date);
		btn_start = (Button) findViewById(R.id.start_date);
		
		btn_delete.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		btn_start.setOnClickListener(this);
		
		if (date.getCreatorObjectId().equals(currentUser.getObjectId())) {
			btn_exit.setVisibility(View.GONE);
		}else {
			btn_delete.setVisibility(View.GONE);
			btn_start.setVisibility(View.GONE);
		}
		
		tv_date_creator.setText(date.getCreatorUsername());
		tv_date_name.setText(date.getDateName());
		tv_date_desc.setText(date.getDateDesc());
		tv_date_time.setText(date.getDateTime().getDate());
		tv_date_position.setText(date.getDatePosition());
		
		
		
		BmobQuery<User> query = new BmobQuery<User>();
		query.addWhereRelatedTo("datemember", new BmobPointer(date));
		query.findObjects(DateDetailsActivity.this, new FindListener<User>() {
			
			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				for (Iterator<User> iterator = arg0.iterator(); iterator.hasNext();) {
					User user = (User) iterator.next();
					date_member.append(user.getUsername()).append(" ");
				}
				
				tv_date_member.setText(date_member.toString());
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast(R.string.network_tips);
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.delete_date:
			showDeleteDialog();
			break;
			
		case R.id.exit_date:
			showExitDialog();
			break;
			
		case R.id.start_date:
			showStartDialog();
			break;

		default:
			break;
		}
	}
	
	private void showDeleteDialog() {
		DialogTips tips = new DialogTips(DateDetailsActivity.this, "提示", "确认删除本次聚会？", "确认", true, true);
		tips.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 删除聚会
				DatePosition datePosition = new DatePosition();
				datePosition.setObjectId(date.getObjectId());
				datePosition.delete(DateDetailsActivity.this, new DeleteListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						ShowToast("删除成功");
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowToast(R.string.network_tips);
					}
				});
			}
		});
		
		tips.show();
	}
	
	private void showExitDialog() {
		DialogTips tips = new DialogTips(DateDetailsActivity.this, "提示", "确认退出本次聚会？", "确认", true, true);
		tips.SetOnSuccessListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				BmobRelation relation = new BmobRelation();
				relation.remove(currentUser);
				DatePosition datePosition = new DatePosition();
				datePosition.setObjectId(date.getObjectId());
				datePosition.setDatemember(relation);
				datePosition.update(DateDetailsActivity.this, new UpdateListener() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						ShowToast("退出成功");
						finish();
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowToast(R.string.network_tips);
					}
				});
			}
			
			
		});
		
		tips.show();
	}
	
	private void showStartDialog() {
		DialogTips tips = new DialogTips(DateDetailsActivity.this, "提示", "确认开始本次聚会？", "确认", true, true);
		tips.SetOnSuccessListener(new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				// 开始聚会
				// 通知聚会成员开始聚会
				// 天安门坐标
			    double mLat1 = Double.parseDouble(CustomApplcation.getInstance().getLatitude());
			    double mLon1 = Double.parseDouble(CustomApplcation.getInstance().getLongtitude());
			    // 百度大厦坐标
			    BmobGeoPoint point = date.getLocation();
			    double mLat2 = point.getLatitude();
			    double mLon2 = point.getLongitude();
			    LatLng pt_start = new LatLng(mLat1, mLon1);
			    LatLng pt_end = new LatLng(mLat2, mLon2);
			    // 构建 route搜索参数以及策略，起终点也可以用name构造
			    RouteParaOption para = new RouteParaOption()
			        .startPoint(pt_start)
			        .endPoint(pt_end)
			        .busStrategyType(EBusStrategyType.bus_recommend_way);
			    try {
			        BaiduMapRoutePlan.openBaiduMapTransitRoute(para, DateDetailsActivity.this);
				} catch (Exception e) {
			        e.printStackTrace();
			    }
			    //结束调启功能时调用finish方法以释放相关资源
			    // BaiduMapRoutePlan.finish(DateDetailsActivity.this);
			}
			
			
		});
		
		tips.show();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if (flag  == true) {
			updateView();
		}
		
		flag = true;
	}
	
	public void updateView() {
		
		BmobQuery<DatePosition> query = new BmobQuery<DatePosition>();
		query.addWhereEqualTo("objectId", date.getObjectId());
		query.findObjects(DateDetailsActivity.this, new FindListener<DatePosition>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast(R.string.network_tips);
			}

			@Override
			public void onSuccess(List<DatePosition> arg0) {
				// TODO Auto-generated method stub
				if (arg0.size() > 0) {
					date = arg0.get(0);
					
					tv_date_name.setText(date.getDateName());
					tv_date_desc.setText(date.getDateDesc());
					tv_date_time.setText(date.getDateTime().getDate());
					tv_date_position.setText(date.getDatePosition());
					
					
					BmobQuery<User> query = new BmobQuery<User>();
					query.addWhereRelatedTo("datemember", new BmobPointer(date));
					query.findObjects(DateDetailsActivity.this, new FindListener<User>() {
						
						@Override
						public void onSuccess(List<User> arg0) {
							// TODO Auto-generated method stub
							date_member = null;
							date_member = new StringBuilder();
							for (Iterator<User> iterator = arg0.iterator(); iterator.hasNext();) {
								User user = (User) iterator.next();
								date_member.append(user.getUsername()).append(" ");
							}
							
							tv_date_member.setText(date_member.toString());
						}
						
						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							ShowToast(R.string.network_tips);
						}
					});
				}
			}
		});
		
		
	}
}
