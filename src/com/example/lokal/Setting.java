package com.example.lokal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.senses.config.Config;
import com.technotalkative.loadwebimage.imageutils.ImageLoader;

public class Setting extends Activity {
	Intent intent = new Intent();
	public ImageLoader imageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		Button logoutButton=(Button)findViewById(R.id.logout);
		Button exitButton=(Button)findViewById(R.id.exit);
		Button updateButton=(Button)findViewById(R.id.update_check);
		ImageView s_face=(ImageView)findViewById(R.id.s_face);
		TextView s_name=(TextView)findViewById(R.id.s_name);
		TextView s_time=(TextView)findViewById(R.id.s_time);
	//	imageLoader.DisplayImage(new Config().BASEURI+"/himg/face.jpg",s_face);
		Context ctx = Setting.this; 
		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		String username=sp.getString("USERNAME_KEY", "NONE");
		String time=sp.getString("TIME", "NONE");
		s_name.setText(username);
		s_time.setText(time+"加入");
		logoutButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Context ctx = Setting.this;
		        SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		        //存入数据
		        Editor editor = sp.edit();
		        editor.remove("USERNAME_KEY");
		        editor.remove("ID_KEY");
		        editor.commit();
				
				// TODO Auto-generated method stub
				intent.setClass(Setting.this, Login.class);
				startActivity(intent);
				finish();
			}
		});
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			    intent.setAction("ExitApp");
			    sendBroadcast(intent);
			    finish();
			}

		});
		updateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "已是最新版本", Toast.LENGTH_LONG).show();
			}
		});
	}
}
