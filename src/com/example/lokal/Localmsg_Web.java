package com.example.lokal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;

import com.senses.config.Config;


public class Localmsg_Web extends Activity implements OnScrollListener {

    public int num=0;
    
    Intent intent=new Intent();
    List<Map<String, Object>> list =  new ArrayList<Map<String,Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localmsg);
		

		//mWebView = (WebView) findViewById(R.id.localmsg_web);      
		ImageButton writebtn=(ImageButton)findViewById(R.id.writebtn1);
		ImageButton takephoto=(ImageButton)findViewById(R.id.takephoto);
//		WebView localmsg_web=(WebView)findViewById(R.id.localmsg_web);

		writebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				intent.setClass(Localmsg_Web.this, Write.class);  
                startActivity(intent);  

			}
		});
		takephoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Localmsg_Web.this).setTitle("选择功能").setItems(R.array.arrcontent, new DialogInterface.OnClickListener(){
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
//						new AlertDialog.Builder(Localmsg.this).setMessage("you clicked"+which).setNeutralButton("cancel", new DialogInterface.OnClickListener() {
//							
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								// TODO Auto-generated method stub
//								
//							}
//						}).show();
						switch (which) {
						case 0:
							intent.setClass(Localmsg_Web.this, Camera.class);
							startActivity(intent);
							break;

						default:
							break;
						}
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();
			}
		});
//		localmsg_web.getSettings().setJavaScriptEnabled(true);  
//		localmsg_web.setWebViewClient(new WebViewClient(){
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				// TODO Auto-generated method stub
//				return super.shouldOverrideUrlLoading(view, url);
//			}
//			
//		});
		/*
		localmsg_web.addJavascriptInterface(new Object() {       
            public void clickOnAndroid() {       
                mHandler.post(new Runnable() {       
                    public void run() {       
                        mWebView.loadUrl("javascript:wave()");       
                    }       
                });       
            }       
        }, "demo");
		*/
//		localmsg_web.loadUrl(new Config().BASEURI+"/localmsg_web.php");
//
//		
		
	}
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}




