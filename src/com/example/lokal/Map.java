package com.example.lokal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Map extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		WebView mapshow=(WebView)findViewById(R.id.mapview);
		WebSettings webSettings = mapshow.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		mapshow.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);
			}
			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onLoadResource(view, url);
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}
		});
		mapshow.loadUrl("file:///android_asset/amap.html");
	}
}
