package com.example.lokal;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.senses.common.HttpTsang;
import com.senses.config.Config;

public class Write extends Activity implements AMapLocationListener {
	
	 private TextView show;
	 private EditText txt;
	 private String latitude;
	 private String longtitude;
	 private String address;
	 MyHandler myHandler;

	 ImageButton btn;
		private LocationManagerProxy mAMapLocManager = null;
		private TextView myLocation;
		private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				myLocation.setText((String) msg.obj);
			}
		};
	 

	 Intent intent=new Intent();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);
		show = (TextView)findViewById(R.id.textView1);
		txt =(EditText)findViewById(R.id.msgcontent);
		btn = (ImageButton)findViewById(R.id.sendbtn);
		ImageButton cancelbtn=(ImageButton)findViewById(R.id.cancelbtn);
		
		myLocation = (TextView) findViewById(R.id.myLocation);
		mAMapLocManager = LocationManagerProxy.getInstance(this);

		
		cancelbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				intent.setClass(Write.this, Dashboard.class);  
                startActivity(intent);  
                finish();
				
			}
		});
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				btn.setClickable(false);
				// TODO Auto-generated method stub
                Context ctx = Write.this; 
        		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        		String username=sp.getString("USERNAME_KEY", "NONE");
     //   		Toast.makeText(Localmsg.this, username,Toast.LENGTH_SHORT).show();
				EditText content=(EditText)findViewById(R.id.msgcontent);
				String constr=content.getText().toString();
				String urlPath=new Config().BASEURI+"/msgpost.php";
				Map<String,String> map=new HashMap<String,String>();//用集合来做，比字符串拼接来得直观
				map.put("latitude", latitude);
				map.put("longtitude", longtitude);
				map.put("img", "");//空的图片地址
				map.put("address", address);
				map.put("content", constr);
				map.put("uid", sp.getString("ID_KEY","none"));
				//map.put("uid", "test");
//				MsgSend msgSend=new MsgSend();
//				msgSend.urlPath=urlPath;
//				msgSend.map=map;
//				msgSend.start();
				try {

					 myHandler=new MyHandler(Write.this.getMainLooper());
			         
			          MyThread m = new MyThread();
			          m.urlPath=urlPath;
			          m.map=map;
			          new Thread(m).start();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				

/*				判断是否发送成功
 * 				String data=MsgSend.status;
				String data_r="msgsendsucceed";
				int s=data.indexOf(data_r);
				//Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
				if(s!=-1){
					Toast.makeText(getApplicationContext(), "消息发送成功", Toast.LENGTH_LONG).show();
					intent.setClass(Write.this,Dashboard.class);  
	                startActivity(intent); 
				}else{
					Toast.makeText(getApplicationContext(),"消息发送失败,请检查网络设置。", Toast.LENGTH_LONG).show();
				}
				
				*/
				//未判断   直接跳转
				intent.setClass(Write.this,Dashboard.class);  
                startActivity(intent); 
                finish();
				
			}
		});
		
	}
	class MyHandler extends Handler {
        public MyHandler() {
        }

        public MyHandler(Looper L) {
            super(L);
        }

        // 必须重写这个方法，用于处理message
        @Override
        public void handleMessage(Message msg) {
            // 这里用于更新UI
            Bundle b = msg.getData();
            String response=b.getString("response");

			String data_r="success";
			int s=response.indexOf(data_r);
			//Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
			if(s!=-1){
				Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_LONG).show();
				intent.setClass(Write.this,Dashboard.class);  
                startActivity(intent); 
                finish();
			}else{
				Toast.makeText(getApplicationContext(), "Failed!Please check your network", Toast.LENGTH_LONG).show();
				btn.setClickable(true);
			}

        }
    }

    class MyThread implements Runnable {
    	String urlPath;
    	Map<String,String> map=new HashMap<String,String>();
        public void run() {
        	
        	try {
    			HttpTsang httpTsang=new HttpTsang();
    			String status=httpTsang.postRequest(urlPath,map);
    			Message msg = myHandler.obtainMessage();
    			Bundle b = new Bundle();
    			b.putString("response", status);
    			msg.setData(b);
    			Log.v("the response", "tttttttttttttttt");
    			myHandler.sendMessage(msg);

    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
    }
	public boolean enableMyLocation() {
		boolean result = false;
		if (mAMapLocManager
				.isProviderEnabled(LocationProviderProxy.AMapNetwork)) {
			mAMapLocManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
			result = true;
		}
		return result;
	}
	
	public void disableMyLocation() {
		mAMapLocManager.removeUpdates(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		enableMyLocation();
	}
	
	@Override
	protected void onPause() {
		disableMyLocation();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destory();
		}
		mAMapLocManager = null;
		super.onDestroy();
	}
	
	public static boolean getRequest(String urlPath) throws Exception
	{
		URL url=new URL(urlPath);
		HttpURLConnection con=(HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setReadTimeout(5*1000);
		if(con.getResponseCode()==200)
		{
			return true;
		}
		return false;
	}
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				address=desc = locBundle.getString("desc");
			}
			String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
					+ "\n精    度    :" + location.getAccuracy() + "米"
					+ "\n城市编码:" + cityCode + "\n位置描述:" + desc);
			latitude=geoLat+"";
			longtitude=geoLng+"";
			
			Message msg = new Message();
			msg.obj = str;
			if (handler != null) {
				handler.sendMessage(msg);
			}
		}
	}
		


}
