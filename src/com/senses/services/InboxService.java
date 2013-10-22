package com.senses.services;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import com.senses.common.HttpTsang;
import com.senses.config.Config;

public class InboxService extends Service {
	public String count;
	public String url=new Config().BASEURI+"/inbox.php?act=notification";
//	MyHandler myHandler;
	Map<String,String> map=new HashMap<String,String>();//用集合来做，比字符串拼接来得直观
	private Handler handler;
	private boolean flag = true;
	private StopReceiver receiver;
	//RadioButton noti=(RadioButton)f
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		
		try {
/*
			 myHandler=new MyHandler(InboxService.this.getMainLooper());
	         
	          MyThread m = new MyThread();
	          m.urlPath=url;
	          m.map=map;
	          new Thread(m).start();
*/	          

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		
		super.onCreate();
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		IntentFilter filter = new IntentFilter("com.senses.services.noti_stop");
		receiver = new StopReceiver();
		this.registerReceiver(receiver, filter);
		sendData();
		return super.onStartCommand(intent, flags, startId);
	}
	
	public void sendData() {
		new Thread(){
			public void run() {
				Context ctx = InboxService.this;
				SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
				map.put("user_id", sp.getString("ID_KEY","none"));
				
				
				Intent intent = new Intent();
				intent.setAction("com.senses.services.noti_rec");
				HttpTsang httpTsang=new HttpTsang();
				String status = "0msgsendsucceed";
    			try {
					status=httpTsang.postRequest(url,map);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			String a[] = status.split("msgsendsucceed"); 
				while(flag) {
					intent.putExtra("noti_count", a[0]+"条新消息");
					InboxService.this.sendBroadcast(intent);
					try{
						System.out.println("sleep");
						Thread.currentThread().sleep(8000);
					}catch(Exception e) {
						System.out.println("error");
					}
				}
			}
		}.start();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(receiver);
		super.onDestroy();
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub

		//map.put("user_id", "user_id");
		
		try {
/*
			 myHandler=new MyHandler(InboxService.this.getMainLooper());
	         
	          MyThread m = new MyThread();
	          m.urlPath=url;
	          m.map=map;
	          new Thread(m).start();
*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onStart(intent, startId);
		
	}
	/*
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

            Log.v("count", "hahahah"+response);
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
	          
			intent.setAction("com.senses.services.noti_rec");
			while(flag) {
				intent.putExtra("noti_count", response);
				InboxService.this.sendBroadcast(intent);
				try{
					 Log.v("count", "aaaaaaaaaaaaaaaaa");
					//System.out.println("sleep");
					Thread.currentThread().sleep(1000);
				}catch(Exception e) {
					//System.out.println("error");
				}
			}
			
			
//			String data_r="success";
//			int s=response.indexOf(data_r);
//			//Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
//			if(s!=-1){
//				Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_LONG).show();
//
//			}else{
//				Toast.makeText(getApplicationContext(), "Failed!Please check your network", Toast.LENGTH_LONG).show();
//
//			}

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
    */
    private class StopReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int stop = intent.getIntExtra("stop", -1);
			if(stop == 1) {
				flag = false;
				stopSelf();
			}
		}
	}

	
}

