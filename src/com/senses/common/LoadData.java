package com.senses.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.R.integer;
import android.util.Log;

import com.senses.config.Config;

public class LoadData {

	public String getMsgBeans(int num){
		//List<CityBean> clist=null;
		try {
			String url=new Config().BASEURI+"/json.php?num="+num;
			HttpPost request=new HttpPost(url);
			HttpResponse response = new DefaultHttpClient().execute(request);
			String str=EntityUtils.toString(response.getEntity());
			//clist=getCList(str);
			return str;
		} catch (Exception e) {
			// TODO: handle exception
			return "none";
		}
		
		//return clist;
	}
	public String RefreshMsg(){
		//List<CityBean> clist=null;
		try {
			String url=new Config().BASEURI+"/json.php";
			HttpPost request=new HttpPost(url);
			HttpResponse response = new DefaultHttpClient().execute(request);
			String str=EntityUtils.toString(response.getEntity());
			//clist=getCList(str);
			return str;
		} catch (Exception e) {
			// TODO: handle exception
			return "none";
		}
		
		//return clist;
	}
	public String getInboxBeans(int num,String user_id) {
		try {
			String url=new Config().BASEURI+"/inbox.php?num="+num+"&user_id="+user_id;
			HttpPost request=new HttpPost(url);
			HttpResponse response = new DefaultHttpClient().execute(request);
			String str=EntityUtils.toString(response.getEntity());
			//clist=getCList(str);
			return str;
		} catch (Exception e) {
			// TODO: handle exception
			return "none";
		}
		
	}
	public String sendGmail(String username,String email) {
		try {
			String url="https://mysenses.ap01.aws.af.cm/mail2.php?username="+username+"&email="+email;
			HttpPost request=new HttpPost(url);
			HttpResponse response = new DefaultHttpClient().execute(request);
			String str=EntityUtils.toString(response.getEntity());
			//clist=getCList(str);
			Log.v("sendmail",str);
			return str;
		} catch (Exception e) {
			// TODO: handle exception
			return "none";
		}
		
	}
}

