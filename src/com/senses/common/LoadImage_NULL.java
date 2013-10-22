package com.senses.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class LoadImage_NULL extends AsyncTask<String, integer, Bitmap> {
	
//	public ImageView imageView;
	public String imguri;
	public LoadImage_NULL(String uri){
	//	imageView=msgimg;
		imguri=uri;
	}
	
	public static InputStream GetImageByUrl(String uri) throws MalformedURLException {
		  URL url = new URL(uri);
		  URLConnection conn;
		  InputStream is;
		  try {
		   conn = url.openConnection();
		   conn.connect();
		   is = conn.getInputStream();

		   // 或者用如下方法

		   // is=(InputStream)url.getContent();
		   return is;
		  } catch (IOException e) {
		   e.printStackTrace();
		  }

		  return null;
		 }

	 public static Bitmap getHttpBitmap(){
	    	URL myFileURL;
	    	Bitmap bitmap=null;
	    	try{
	    		myFileURL = new URL("http://10.254.8.113/lokal/demo.jpg");
	    		//获得连接
	    		HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
	    		//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
	    		conn.setConnectTimeout(6000);
	    		//连接设置获得数据流
	    		conn.setDoInput(true);
	    		//不使用缓存
	    		conn.setUseCaches(false);
	    		//这句可有可无，没有影响
	    		//conn.connect();
	    		//得到数据流
	    		InputStream is = conn.getInputStream();
	    		
	    		
	    		
	    		
	    		//解析得到图片
	    		bitmap = BitmapFactory.decodeStream(is);
	    		//关闭数据流
	    		is.close();
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	
			return bitmap;
	    	
	    }
	
	public static Bitmap GetBitmapByUrl(String uri) {

		  Bitmap bitmap;  
		  InputStream is;
		  try {

		   is =  GetImageByUrl(uri); 

		   bitmap = BitmapFactory.decodeStream(is);
		   is.close();

		   return bitmap;

		  } catch (MalformedURLException e) {
		   e.printStackTrace();

		  } catch (IOException e) {
		   e.printStackTrace();
		  }

		  return null;
		 }
/*
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		// TODO Auto-generated method stub
		 if (null != imageView) {
				imageView.setImageBitmap(bitmap);
		 }
	}
	 public void setImageView(ImageView image) {
		   this.imageView = image;
		  }
		  */
	@Override
	protected Bitmap doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		//Bitmap bitmap = GetBitmapByUrl(imguri); 
		//Bitmap bitmap = GetBitmapByUrl("http://10.254.8.113/lokal/demo.jpg"); 
		Bitmap bitmap=getHttpBitmap();
		return bitmap;
	}
	

}
