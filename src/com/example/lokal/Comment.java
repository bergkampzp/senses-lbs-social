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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lokal.Register.MyHandler;
import com.example.lokal.Register.MyThread;
import com.senses.common.HttpTsang;
import com.senses.common.LoadData;
import com.senses.config.Config;
import com.senses.threader.CommentSend;

public class Comment extends Activity {
	public String msgid;
	public String rec_id;
	Button commentbtn;
	MyHandler myHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		TextView c_content=(TextView)findViewById(R.id.content_c);
		TextView c_msgid=(TextView)findViewById(R.id.msgid);
		Intent intent=getIntent();
		msgid=intent.getStringExtra("msgid");
		rec_id=intent.getStringExtra("rec_id");
		c_msgid.setText(rec_id);
		c_content.setText(intent.getStringExtra("content"));
		commentbtn=(Button)findViewById(R.id.commentbtn);
		ImageButton cancelbtn=(ImageButton)findViewById(R.id.cancel_cmt);
		cancelbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Comment.this, Dashboard.class);  
                startActivity(intent);  
                finish();
				
			}
		});
		
		commentbtn.setOnClickListener(new View.OnClickListener() {
			
			
			
			@Override
			public void onClick(View arg0) {
				commentbtn.setClickable(false);//点击一次后不可点击    没有设置回复
				// TODO Auto-generated method stub
                Context ctx = Comment.this; 
        		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
     //   		Toast.makeText(Localmsg.this, username,Toast.LENGTH_SHORT).show();
				EditText content=(EditText)findViewById(R.id.commentcontent);
				
				String constr=content.getText().toString();
				
				String urlPath=new Config().BASEURI+"/commentpost.php";
				Map<String,String> map=new HashMap<String,String>();//用集合来做，比字符串拼接来得直观
				map.put("msgid", msgid);
				map.put("rec_id", rec_id);
				map.put("content", constr);
				map.put("uid", sp.getString("ID_KEY", "none"));
//				CommentSend commentSend=new CommentSend();
//				commentSend.urlPath=urlPath;
//				commentSend.map=map;
//				commentSend.start();
				
				
				myHandler=new MyHandler(Comment.this.getMainLooper());
		         
		          MyThread m = new MyThread();
		          m.urlPath=urlPath;
		          m.map=map;
		          new Thread(m).start();
		          
		          
				Intent intent=new Intent();
				intent.setClass(Comment.this,Dashboard.class);  
                startActivity(intent); 
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
		

		        Intent intent=new Intent();
				intent.setClass(Comment .this,Dashboard.class);
				Toast.makeText(Comment.this, "回复成功",Toast.LENGTH_SHORT).show();
                startActivity(intent); 
                finish();
			}else{
				Toast.makeText(getApplicationContext(), "回复失败，请检查网络连接!", Toast.LENGTH_LONG).show();
			//	register.setClickable(true);//点击一次后不可点
			//	register.setText("注册");
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
    //			Log.v("the response", "tttttttttttttttt");
    			myHandler.sendMessage(msg);

    			
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
    }
}
