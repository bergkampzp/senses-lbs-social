package com.example.lokal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;

public class Index extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences preferences;
		preferences=getSharedPreferences("count", MODE_WORLD_READABLE);
		int count = preferences.getInt("count", 0);
		Intent intent = new Intent();
		if (count == 0) {
	            
	            intent.setClass(Index.this, FirstIn.class);  
                startActivity(intent);  
                finish();
	    }else {
	    	intent.setClass(Index.this, Login.class);  
            startActivity(intent);  
            finish();
		}
		 Editor editor = preferences.edit();
	        //存入数据
	        editor.putInt("count", ++count);
	        //提交修改
	        editor.commit();
	}

}
