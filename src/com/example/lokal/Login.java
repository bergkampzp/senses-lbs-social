package com.example.lokal;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.senses.common.HttpTsang;
import com.senses.config.Config;
import com.senses.services.InboxService;

public class Login extends Activity {
	Intent intent=new Intent();
	private Handler handler;
	MyHandler myHandler;
	public static String status;
	public Button login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Context ctx = Login.this; 
		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		String username=sp.getString("USERNAME_KEY", "NONE");
		String user_id=sp.getString("ID_KEY","NONE");
		if(username!="NONE"&&user_id!="NONE"){
			intent.setClass(Login.this, Dashboard.class); 
		}
//		Toast.makeText(getApplicationContext(), user_id+username, Toast.LENGTH_LONG).show();
		login=(Button)findViewById(R.id.login_btn);
		Button login_testButton=(Button)findViewById(R.id.login_test);
		login_testButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				intent.setClass(Login.this, Dashboard.class);  
                startActivity(intent);
			}
		});
		Button toRegister=(Button)findViewById(R.id.to_register);
/*		handler=new Handler(){
			public void handleMessage(Message msg){
			     
			String message=(String)msg.obj;//obj不一定是String类，可以是别的类，看用户具体的应用
			  //根据message中的信息对主线程的UI进行改动
			  //……                                                      }
			}
		};
*/
		toRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.setClass(Login.this, Register.class);  
                startActivity(intent);
			}
		});
		
		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				login.setClickable(false);//点击一次后不可点
				login.setText("登录中...");
				
				// TODO Auto-generated method stub
				EditText mail=(EditText)findViewById(R.id.login_mail);
				EditText password=(EditText)findViewById(R.id.login_password);
				String mail_str=mail.getText().toString();
				String password_str=password.getText().toString();
				String urlPath=new Config().BASEURI+"/login.php";
				Map<String,String> map=new HashMap<String,String>();//用集合来做，比字符串拼接来得直观
				map.put("act", "register");
				map.put("mail", mail_str);;
				map.put("password",password_str);
				try {

					 myHandler=new MyHandler(Login.this.getMainLooper());
			         
			          MyThread m = new MyThread();
			          m.urlPath=urlPath;
			          m.map=map;
			          new Thread(m).start();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
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
			String data_r="right";
			int s=response.indexOf(data_r);
			//Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
			if(s!=-1){
				
				
				
				
				
				String a[] = response.split("right");  
	//			Toast.makeText(getApplicationContext(), a[0], Toast.LENGTH_LONG).show();
		        
				
				Context ctx = Login.this;
		        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		        //存入数据
		        Editor editor = sp.edit();
		        editor.putString("USERNAME_KEY", a[1]);
		        editor.putString("ID_KEY", a[0]);
		        editor.putString("FACE", a[2]);
		        editor.putString("TIME", a[3]);
		        editor.commit();
				intent.setClass(Login.this,Dashboard.class);  
                startActivity(intent); 
                finish();
			}else{
				Toast.makeText(getApplicationContext(), "密码错误!", Toast.LENGTH_LONG).show();
				login.setClickable(true);//点击一次后不可点
				login.setText("登录");
			}

        }
    }

    class MyThread implements Runnable {
    	String urlPath;
    	Map<String,String> map=new HashMap<String,String>();
        public void run() {
        	
        	try {
    			HttpTsang httpTsang=new HttpTsang();
    			status=httpTsang.postRequest(urlPath,map);
    			Message msg = myHandler.obtainMessage();
    			Bundle b = new Bundle();
    			b.putString("response", status);
    			msg.setData(b);
    			myHandler.sendMessage(msg);

    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
    }

	
	
}
