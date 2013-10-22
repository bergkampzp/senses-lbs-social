package com.example.lokal;

import com.amapv2.cn.apis.poisearch.PoisearchDemoActivity;
import com.amapv2.cn.apis.route.RouteDemoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Todo extends Activity {

	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todo);
		LinearLayout poisearch=(LinearLayout)findViewById(R.id.poisearch);
		LinearLayout route=(LinearLayout)findViewById(R.id.route);
		poisearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Todo.this, PoisearchDemoActivity.class);  
                startActivity(intent);  
			}
		});
		route.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(Todo.this, RouteDemoActivity.class);  
                startActivity(intent);  
			}
		});
	}
		
}
