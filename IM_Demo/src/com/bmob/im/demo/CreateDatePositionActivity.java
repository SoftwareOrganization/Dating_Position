package com.bmob.im.demo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bmob.im.demo.bean.DatePosition;
import com.bmob.im.demo.bean.User;
import com.bmob.im.demo.ui.BaseActivity;
import com.bmob.im.demo.util.CollectionUtils;
import com.bmob.im.demo.view.HeaderLayout.onRightImageButtonClickListener;

import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class CreateDatePositionActivity extends BaseActivity implements OnClickListener, OnGetGeoCoderResultListener{
	
	private EditText et_date_name, et_date_desc, et_date_time, et_date_position, et_date_member;

	Boolean flag_choose_time = false, flag_choose_position = false, flag_choose_member = false;
	GeoCoder mSearch = null; // 搜索模块，因为百度定位sdk能够得到经纬度，但是却无法得到具体的详细地址，因此需要采取反编码方式去搜索此经纬度代表的地址
	
	Double lat;
	Double log;
	
	User currentuser;
	
	int year = 0, month = 0, day = 0, hour = 0, minit = 0;
	
	List<BmobChatUser> friends = new ArrayList<BmobChatUser>();
	ArrayList<String> items = new ArrayList<String>();
	
	ArrayList<String> selectUserName = new ArrayList<String>();
	
	DatePosition datePosition = new DatePosition();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_date_position);
		
		initView();
	}
	
	private void initView() {
		initTopBarForBoth("创建聚会", R.drawable.icon_create_date_save, new onRightImageButtonClickListener() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				// 保存聚会信息
				saveDate();
			}
		});
		
		et_date_name = (EditText) findViewById(R.id.create_date_position_name_et);
		et_date_desc = (EditText) findViewById(R.id.create_date_position_desc_et);
		et_date_time = (EditText) findViewById(R.id.create_date_position_time_et);
		et_date_position = (EditText) findViewById(R.id.create_date_position_position_et);
		et_date_member = (EditText) findViewById(R.id.create_date_position_member_et);
		
		et_date_time.setOnClickListener(this);
		et_date_position.setOnClickListener(this);
		et_date_member.setOnClickListener(this);
		
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		currentuser = CustomApplcation.getInstance().getCurrentuser();
		
		//initFriends();
	}
	
	public void initFriends() {
		new Thread(){
			@Override
			public void run(){
				CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(CreateDatePositionActivity.this).getContactList()));
				
				Map<String,BmobChatUser> users = CustomApplcation.getInstance().getContactList();
				//组装新的User
				friends = CollectionUtils.map2list(users);
				
				for (int i = 0; i < friends.size(); i++) {
					items.add(friends.get(i).getNick());
				}
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.create_date_position_time_et:
			showTimeSelectDialog();
			break;
			
		case R.id.create_date_position_position_et:
			Intent intent = new Intent();
			intent.setClass(CreateDatePositionActivity.this, ChooseDatePositionActivity.class);
			startActivityForResult(intent, 0);
			break;
			
		case R.id.create_date_position_member_et:
			showSelectMember();
			break;

		default:
			break;
		}
	}
	
	private void showTimeSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
        View view = View.inflate(this, R.layout.date_time_dialog, null); 
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker); 
        final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker); 
        builder.setView(view); 

        Calendar cal = Calendar.getInstance(); 
        cal.setTimeInMillis(System.currentTimeMillis()); 
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null); 

        timePicker.setIs24HourView(true); 
        timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY)); 
        timePicker.setCurrentMinute(Calendar.MINUTE); 
           
        builder.setTitle("选取聚会时间"); 
        builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() { 

            @Override 
            public void onClick(DialogInterface dialog, int which) { 

                StringBuffer sb = new StringBuffer(); 
                sb.append(String.format("%d-%02d-%02d",  
                        datePicker.getYear(),  
                        datePicker.getMonth() + 1, 
                        datePicker.getDayOfMonth())); 
                sb.append("  "); 
                sb.append(timePicker.getCurrentHour()) 
                .append(":").append(timePicker.getCurrentMinute()); 

                et_date_time.setText(sb); 
                flag_choose_time = true;
                
                year = datePicker.getYear();
                month = datePicker.getMonth();
                day = datePicker.getDayOfMonth();
                
                hour = timePicker.getCurrentHour();
                minit = timePicker.getCurrentMinute();
                
                dialog.cancel(); 
            } 
        }).show(); 
	}
	
	protected void onActivityResult(int requestCode, int resultCode,  
            Intent data){  
		switch (resultCode){  
		case RESULT_OK:  
			Bundle bundle = data.getExtras();  

			lat = bundle.getDouble("lat");
			log = bundle.getDouble("log");
			LatLng ll = new LatLng(lat, log);
			BDLocation location = new BDLocation();
			location.setLatitude(lat);
			location.setLongitude(log);
			String address = location.getAddrStr();
			if (address != null && !address.equals("")) {
				et_date_position.setText(address);
			} else {
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
			}
			ShowToast("" + lat + "," + log);
		}  
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			ShowToast("抱歉，未能找到结果");
			return;
		}
		BmobLog.i("反编码得到的地址：" + result.getAddress());
		et_date_position.setText(result.getAddress());
	} 
	
	public void showSelectMember(){    
		
		
		
		String[] multiChoiceItems = {"item1", "item2"};
		
		CustomApplcation.getInstance().setContactList(CollectionUtils.list2map(BmobDB.create(CreateDatePositionActivity.this).getContactList()));
		
		Map<String,BmobChatUser> users = CustomApplcation.getInstance().getContactList();
		//组装新的User
		friends.clear();
		items.clear();
		friends = CollectionUtils.map2list(users);
		ShowLog("好友人数：" + friends.size());
		for (Iterator<BmobChatUser> iterator = friends.iterator(); iterator.hasNext();) {
			BmobChatUser bmobChatUser = (BmobChatUser) iterator.next();
			items.add(bmobChatUser.getUsername());
		}
		
        //复选框默认值：false=未选;true=选中 ,各自对应items[i]  
        final boolean[] defaultSelectedStatus = {false,false};  
          
        final StringBuilder sb = new StringBuilder();  
        //创建对话框    
        new AlertDialog.Builder(CreateDatePositionActivity.this)    
        .setTitle("选择聚会成员")//设置对话框标题    
        .setMultiChoiceItems((String [])items.toArray(new String[items.size()]), defaultSelectedStatus, new OnMultiChoiceClickListener(){    
            @Override    
            public void onClick(DialogInterface dialog, int which,    
                    boolean isChecked) {    
                //来回重复选择取消，得相应去改变item对应的bool值，点击确定时，根据这个bool[],得到选择的内容   
                defaultSelectedStatus[which] = isChecked;  
            }    
        })  //设置对话框[肯定]按钮    
        .setPositiveButton("确定",new DialogInterface.OnClickListener() {  
              
            @Override  
            public void onClick(DialogInterface dialog, int which) { 
            	selectUserName.clear();
            	StringBuilder str = new StringBuilder();
                for(int i=0;i<defaultSelectedStatus.length;i++) {  
                    if(defaultSelectedStatus[i]) {  
                        selectUserName.add(items.get(i));
                        str.append(items.get(i));
                        str.append(" ");
                    }  
                }
                
                et_date_member.setText(str.toString());
                // TODO Auto-generated method stub    
            }  
        })  
        .setNegativeButton("取消", null)//设置对话框[否定]按钮    
        .show();    
    }
	
	@SuppressWarnings("deprecation")
	public void saveDate() {
		if (TextUtils.isEmpty(et_date_name.getText().toString())) {
			ShowToast("聚会名称不能为空");
			return;
		}
		
		if (TextUtils.isEmpty(et_date_desc.getText().toString())) {
			ShowToast("聚会描述不能为空");
			return;
			
			
		}
		
		if (TextUtils.isEmpty(et_date_time.getText().toString())) {
			ShowToast("请选择聚会时间");
			return;
		}
		
		if (TextUtils.isEmpty(et_date_position.getText().toString())) {
			ShowToast("请选择聚会地点");
			return;
		}
		
		if (TextUtils.isEmpty(et_date_member.getText().toString())) {
			ShowToast("请选择聚会成员");
			return;
		}
		
		
		datePosition.setDateName(et_date_name.getText().toString());
		datePosition.setDateDesc(et_date_desc.getText().toString());
		datePosition.setDateTime(new BmobDate(new Date(year, month, day, hour, minit)));
		datePosition.setLocation(new BmobGeoPoint(log, lat));
		datePosition.setCreatorObjectId(currentuser.getObjectId());
		datePosition.setCreatorAvatar(currentuser.getAvatar());
		datePosition.setCreatorUsername(currentuser.getUsername());
		datePosition.setDatePosition(et_date_position.getText().toString());
		ShowLog(" 选择日期的年："  + year);
		
		BmobQuery<User> query = new BmobQuery<User>();
		
		query.addWhereContainedIn("username", selectUserName);
		
		query.findObjects(CreateDatePositionActivity.this, new FindListener<User>() {
			
			@Override
			public void onSuccess(final List<User> arg0) {
				// TODO Auto-generated method stub
				if (arg0.size() > 0) {
					
					ShowLog("查询到的人数为：" + arg0.size());
					BmobRelation relation = new BmobRelation();
					relation.add(currentuser);
					for (int i = 0; i < arg0.size(); i++) {
						if (arg0.get(i) == null) {
							ShowLog("为空");
						}
						relation.add(arg0.get(i));
					}
					
					
					
					datePosition.setDatemember(relation);
					
					datePosition.save(CreateDatePositionActivity.this, new SaveListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							ShowToast("创建聚会成功");
							
							finish();
							
							BmobRelation myDate = new BmobRelation();
							myDate.add(datePosition);
							User updateUser = new User();
							updateUser.setMyDates(myDate);
							updateUser.update(CreateDatePositionActivity.this, currentuser.getObjectId(), new UpdateListener() {
								
								@Override
								public void onSuccess() {
									// TODO Auto-generated method stub
									// 发送消息推送
									for (Iterator<User> iterator = arg0.iterator(); iterator
											.hasNext();) {
										User user = (User) iterator.next();
										String jsonresult = "";
								        JSONObject object = new JSONObject();
								        try {
											
								        	JSONObject jsonObject1 = new JSONObject();
								        	jsonObject1.put("sound", "");
								        	jsonObject1.put("alert", currentuser.getUsername() + "邀请您加入聚会："  + et_date_name.getText().toString());
								        	jsonObject1.put("badge", 0);
								        	object.put("aps", jsonObject1);
								        	
								        	object.put("tag", "invite_join_date");
								        	object.put("fId", currentuser.getObjectId());
								        	object.put("fU", currentuser.getUsername());
								        	object.put("tId", user.getObjectId());
								        	
								        	jsonresult = object.toString();
								        	
								        	BmobChatManager.getInstance(CreateDatePositionActivity.this).sendJsonMessage(jsonresult, user.getObjectId(), new PushListener() {
												
												@Override
												public void onSuccess() {
													// TODO Auto-generated method stub
													ShowToast("邀请好友成功");										}
												
												@Override
												public void onFailure(int arg0, String arg1) {
													// TODO Auto-generated method stub
													ShowToast("邀请好友失败");
												}
											});
								        	
								        	
										} catch (JSONException e) {
											// TODO: handle exception
											e.printStackTrace();  
										}
									}
									finish();
								}
								
								@Override
								public void onFailure(int arg0, String arg1) {
									// TODO Auto-generated method stub
									ShowToast(R.string.network_tips);
								}
							});
							
							
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							ShowToast("创建聚会失败" + arg1);
							
							ShowLog("创建聚会失败" + arg1);
						}
					});
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast(R.string.network_tips);
			}
		});
	}
	
}
