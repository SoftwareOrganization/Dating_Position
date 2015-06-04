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
		
		//��ʼ����������  
        BaiduNaviManager.getInstance().  
            initEngine(this, getSdcardDir(), mNaviEngineInitListener, "�ҵ�key",null);
		
		initView();
	}
	
	private void initView() {
		
	}
	
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {  
        public void engineInitSuccess() {  
            //������ʼ�����첽�ģ���ҪһС��ʱ�䣬�������־��ʶ�������Ƿ��ʼ���ɹ���Ϊtrueʱ����ܷ��𵼺�  
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
