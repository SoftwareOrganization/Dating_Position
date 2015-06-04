package com.bmob.im.demo;

import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.BaiduNaviManager;
import com.bmob.im.demo.ui.BaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

public class DateNaviActivity extends BaseActivity {
	
	private boolean mIsEngineInitSuccess = false;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_navi);
		
		//初始化导航引擎  
        BaiduNaviManager.getInstance().  
            initEngine(this, getSdcardDir(), mNaviEngineInitListener, "我的key",null);
		
		initView();
	}
	
	private void initView() {
		
	}
	
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {  
        public void engineInitSuccess() {  
            //导航初始化是异步的，需要一小段时间，以这个标志来识别引擎是否初始化成功，为true时候才能发起导航  
            mIsEngineInitSuccess = true;  
        }  
 
        public void engineInitStart() {  
        	
        }  
 
        public void engineInitFail() {  
        	
        }  
    };  
    
    private String getSdcardDir() {  
        if (Environment.getExternalStorageState().equalsIgnoreCase(  
                Environment.MEDIA_MOUNTED)) {  
            return Environment.getExternalStorageDirectory().toString();  
        }  
        return null;  
    }
	
}
