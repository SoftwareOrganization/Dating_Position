package com.bmob.im.demo;

import cn.bmob.im.util.BmobLog;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bmob.im.demo.ui.BaseActivity;
import com.bmob.im.demo.view.HeaderLayout.onRightImageButtonClickListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ChooseDatePositionActivity extends BaseActivity implements OnGetGeoCoderResultListener{
	
	// ��λ���
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		BitmapDescriptor mCurrentMarker;

		MapView mMapView;
		BaiduMap mBaiduMap;
		
		LatLng selectPt;

		private BaiduReceiver mReceiver;// ע��㲥�����������ڼ��������Լ���֤key

		GeoCoder mSearch = null; // ����ģ�飬��Ϊ�ٶȶ�λsdk�ܹ��õ���γ�ȣ�����ȴ�޷��õ��������ϸ��ַ�������Ҫ��ȡ�����뷽ʽȥ�����˾�γ�ȴ���ĵ�ַ

		static BDLocation lastLocation = null;

		BitmapDescriptor bdgeo = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_date_position);
		
		initView();
	}
	
	private void initView() {
		initTopBarForBoth("ѡ��ۻ�ص�", R.drawable.icon_create_date_save, new onRightImageButtonClickListener() {
			
			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				// ����ۻ�ص㣬��������Ϣ
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putDouble("lat", selectPt.latitude);
				bundle.putDouble("log", selectPt.longitude);
				intent.putExtras(bundle);
				ChooseDatePositionActivity.this.setResult(RESULT_OK, intent);
				ChooseDatePositionActivity.this.finish();
			}
		});
		
		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		
		mBaiduMap = mMapView.getMap();
		//�������ż���
		mBaiduMap.setMaxAndMinZoomLevel(18, 13);
		// ע�� SDK �㲥������
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new BaiduReceiver();
		registerReceiver(mReceiver, iFilter);
		
		initLocClient();

		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		OnMapClickListener listener = new OnMapClickListener() {  
		    /** 
		    * ��ͼ�����¼��ص����� 
		    * @param point ����ĵ������� 
		    */  
		    public void onMapClick(LatLng point){  
		    	ShowToast("" + point.latitude + "," + point.longitude);
		    	selectPt = point;
		    	mBaiduMap.clear();
		    	
		    	// �ڵ�ͼ�ϱ���������
		    	//����Maker�����  
		    	LatLng pt = new LatLng(point.latitude, point.longitude);  
		    	//����Markerͼ��  
		    	BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    	    .fromResource(R.drawable.icon_map_marker);  
		    	//����MarkerOption�������ڵ�ͼ�����Marker  
		    	OverlayOptions option = new MarkerOptions()  
		    	    .position(pt)  
		    	    .icon(bitmap);  
		    	//�ڵ�ͼ�����Marker������ʾ  
		    	mBaiduMap.addOverlay(option);
		    }  
		    /** 
		    * ��ͼ�� Poi �����¼��ص����� 
		    * @param poi ����� poi ��Ϣ 
		    */  
		    public boolean onMapPoiClick(MapPoi poi){  
		    	
		    	return false;
		    }  
		};
		
		mBaiduMap.setOnMapClickListener(listener);
	}

	private void initLocClient() {
//		 ������λͼ��
		//mBaiduMap.setMyLocationEnabled(true);
		//mBaiduMap.setMyLocationData(new MyLocationData(
				//MyLocationData.LocationMode.NORMAL, true, null));
		// ��λ��ʼ��
		
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setProdName("bmobim");// ���ò�Ʒ��
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		option.setOpenGps(true);
		option.setIsNeedAddress(true);
		option.setIgnoreKillProcess(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
		if (mLocClient != null && mLocClient.isStarted())
		    mLocClient.requestLocation();

		if (lastLocation != null) {
			// ��ʾ�ڵ�ͼ��
			LatLng ll = new LatLng(lastLocation.getLatitude(),
					lastLocation.getLongitude());
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
		}
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;

			if (lastLocation != null) {
				if (lastLocation.getLatitude() == location.getLatitude()
						&& lastLocation.getLongitude() == location
						.getLongitude()) {
					BmobLog.i("��ȡ������ͬ");// �����������ȡ���ĵ���λ����������ͬ�ģ����ٶ�λ
					mLocClient.stop();
					return;
				}
			}
			lastLocation = location;
			
			BmobLog.i("lontitude = " + location.getLongitude() + ",latitude = "
					+ location.getLatitude() + ",��ַ = "
					+ lastLocation.getAddrStr());

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());
			String address = location.getAddrStr();
			if (address != null && !address.equals("")) {
				lastLocation.setAddrStr(address);
			} else {
				// ��Geo����
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
			}
			// ��ʾ�ڵ�ͼ��
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			//���ð�ť�ɵ��
			mHeaderLayout.getRightImageButton().setEnabled(true);
		}

	}

	/**
	 * ����㲥�����࣬���� SDK key ��֤�Լ������쳣�㲥
	 */
	public class BaiduReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				ShowToast("key ��֤����! ���� AndroidManifest.xml �ļ��м�� key ����");
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				ShowToast("�������");
			}
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
			ShowToast("��Ǹ��δ���ҵ����");
			return;
		}
		BmobLog.i("������õ��ĵ�ַ��" + result.getAddress());
		lastLocation.setAddrStr(result.getAddress());
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
		lastLocation = null;
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if(mLocClient!=null && mLocClient.isStarted()){
			// �˳�ʱ���ٶ�λ
			mLocClient.stop();
		}
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		// ȡ������ SDK �㲥
		unregisterReceiver(mReceiver);
		super.onDestroy();
		// ���� bitmap ��Դ
		bdgeo.recycle();
	}
}
