package com.example.lokal;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.Toast;

import com.amapv2.cn.apis.LocationSourceDemoActivity;
import com.senses.services.InboxService;

public class Dashboard extends Activity implements OnCheckedChangeListener {
	
	private TabHost tabHost;
    private Intent localmsg;
    private Intent focus;
    private Intent notification;
    private Intent collect;
    private Intent settings;
    RadioButton notificaion;

    private DataReceiver receiver;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //启动后台服务查询inbox
        
        
        setContentView(R.layout.activity_dashboard);
 //       Intent intent=new Intent(this,InboxService.class);

 //       startService(new Intent(this, InboxService.class));
     // tabHost = getTabHost();
        tabHost = (TabHost) findViewById(R.id.my_tabhost);
        notificaion=(RadioButton)findViewById(R.id.radio_button_score);
        LocalActivityManager groupActivity = new LocalActivityManager(this,false);
        groupActivity.dispatchCreate(savedInstanceState);
        tabHost.setup(groupActivity);
        initIntent();
        addSpec();
        ((RadioGroup) findViewById(R.id.tab_radiogroup))
               .setOnCheckedChangeListener(this);
    }
       
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	IntentFilter filter = new IntentFilter("com.senses.services.noti_rec");
        receiver = new DataReceiver();
    	this.registerReceiver(receiver, filter);
 //   	Log.v("count", "the start");
    	//Intent intent = new Intent();
    	try {
 //   		Log.v("count","try-1");
//    		intent.setAction("com.senses.services.InboxService");
//    		Dashboard.this.startService(intent);
    		startService(new Intent(this, InboxService.class));
//    		Log.v("count","try-2");
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("count","try-3");
		}
		
		
//    	startService(new Intent(this, InboxService.class));
    	super.onStart();
    }
        
        
        
    
    /**
     * 初始化各个tab标签对应的intent
     */

	private void initIntent() {
       localmsg = new Intent(this, Localmsg.class);
       focus = new Intent(this, Inbox.class);
       notification = new Intent(this, Todo.class);
       collect = new Intent(this, LocationSourceDemoActivity.class);//地图
       settings = new Intent(this, Setting.class);
    }
    /**
     * 为tabHost添加各个标签项
     */
    private void addSpec() {
		tabHost.addTab(this.buildTagSpec("tab_study", R.string.app_name,
		              R.drawable.settings, localmsg));
		tabHost.addTab(this.buildTagSpec("tab_score", R.string.app_name,
		              R.drawable.settings, focus));
       tabHost.addTab(this.buildTagSpec("tab_fee", R.string.app_name,
              R.drawable.settings, notification));
       tabHost.addTab(this.buildTagSpec("tab_certificate",
              R.string.app_name, R.drawable.settings,
              collect));
       tabHost.addTab(this.buildTagSpec("tab_more", R.string.app_name,
              R.drawable.settings,settings));
    }
    /**
     * 自定义创建标签项的方法
     * @param tagName 标签标识
     * @param tagLable 标签文字
     * @param icon 标签图标
     * @param content 标签对应的内容
     * @return
     */
    private TabHost.TabSpec buildTagSpec(String tagName, int tagLable,
           int icon, Intent content) {
       return tabHost
              .newTabSpec(tagName)
              .setIndicator(getResources().getString(tagLable),
                     getResources().getDrawable(icon)).setContent(content);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        return true;
    }

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
	       switch (checkedId) {
	       case R.id.radio_button_study:
	           tabHost.setCurrentTabByTag("tab_study");
	           break;
	       case R.id.radio_button_score:
	           tabHost.setCurrentTabByTag("tab_score");
	           break;
	       case R.id.radio_button_certificate:
	           tabHost.setCurrentTabByTag("tab_certificate");
	           break;
	       case R.id.radio_button_fee:
	           tabHost.setCurrentTabByTag("tab_fee");
	           break;
	       case R.id.radio_button_more:
	           tabHost.setCurrentTabByTag("tab_more");
	           break;
	       }
	}

 
	private class DataReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
//			Log.v("count", "at datareciver");
			String noti_count = intent.getStringExtra("noti_count");
//			Log.v("count", "at datareciver2"+noti_count);
//			Toast.makeText(getApplicationContext(), noti_count+"haha", Toast.LENGTH_LONG).show();
//			Log.v("count", "at datareciver3"+noti_count);
			notificaion.setText(noti_count);
		}
		
	}

}



