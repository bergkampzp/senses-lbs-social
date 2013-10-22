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
import com.senses.common.LoadData;
import com.senses.config.Config;

public class Register extends Activity {
	Intent intent=new Intent();
	MyHandler myHandler;
	String username_str;
	Button register;
	EditText username;
	EditText password;
	EditText mail;
	String mail_str;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		register=(Button)findViewById(R.id.registerbtn);
		mail=(EditText)findViewById(R.id.mail);
		username=(EditText)findViewById(R.id.username);
		password=(EditText)findViewById(R.id.password);
		
		password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){//获得焦点  
		            //在这里可以对获得焦点进行处理  
		        }else{//失去焦点  
		            //在这里可以对输入的文本内容进行有效的验证  
		        	register.setClickable(true);
		        	register.setText("注册");
		        }  
			}
		});
		mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){//获得焦点  
		            //在这里可以对获得焦点进行处理  
		        }else{//失去焦点  
		            //在这里可以对输入的文本内容进行有效的验证  
		        	register.setClickable(true);
		        	register.setText("注册");
		        }  
			}
		});
		username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){//获得焦点  
		            //在这里可以对获得焦点进行处理  
		        }else{//失去焦点  
		            //在这里可以对输入的文本内容进行有效的验证  
		        	register.setClickable(true);
		        	register.setText("注册");
		        }  
			}
		});
		register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				register.setClickable(false);//点击一次后不可点
				register.setText("注册中...");
				// TODO Auto-generated method stub
				
				
				
				mail_str=mail.getText().toString();
				
				username_str=username.getText().toString();
				String password_str=password.getText().toString();
				if(mail_str.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")&&username_str!=null&&password_str!=null){
					String urlPath=new Config().BASEURI+"/register.php";
					Map<String,String> map=new HashMap<String,String>();//用集合来做，比字符串拼接来得直观
					map.put("act", "register");
					map.put("mail", mail_str);
					map.put("username",username_str);
					map.put("password",password_str);
					try {
	
						 myHandler=new MyHandler(Register.this.getMainLooper());
				         
				          MyThread m = new MyThread();
				          m.urlPath=urlPath;
				          m.map=map;
				          new Thread(m).start();
	
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					if(!mail_str.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")){
						Toast.makeText(getApplicationContext(), "邮件格式不正确", Toast.LENGTH_LONG).show();
					}
					if(username_str==""&&password_str==""){
						Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_LONG).show();
					}
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

			String data_r="Registered";
			String data_r2="Messagesentbytsang";
			int s=response.indexOf(data_r);
			int s2=response.indexOf(data_r2);
			//Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
			if(s2!=-1){
				Toast.makeText(getApplicationContext(), "Welcome to Senses!", Toast.LENGTH_LONG).show();
			}
			if(s!=-1){
				
				String a[] = response.split("Registered");  
	//			Toast.makeText(getApplicationContext(), a[0], Toast.LENGTH_LONG).show();
				
				Context ctx = Register.this;       
		        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		        //存入数据
		        Editor editor = sp.edit();
		        editor.putString("USERNAME_KEY", username_str);
		        editor.putString("ID_KEY", a[0]);
		        editor.commit();
				intent.setClass(Register .this,PicCutActivity.class);  
                startActivity(intent); 
                finish();
			}else{
				Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
				register.setClickable(true);//点击一次后不可点
				register.setText("注册");
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
    			String sendGmail_response=new LoadData().sendGmail(username_str,mail_str);
    			Message msg = myHandler.obtainMessage();
    			Bundle b = new Bundle();
    			b.putString("response", status+sendGmail_response);
    			msg.setData(b);
    //			Log.v("the response", "tttttttttttttttt");
    			myHandler.sendMessage(msg);

    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
    }
	

}
