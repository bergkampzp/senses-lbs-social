package com.senses.threader;

import java.util.HashMap;
import java.util.Map;

import com.senses.common.HttpTsang;

public class CommentSend extends Thread {

	public String urlPath;
	public Map<String,String> map=new HashMap<String,String>();//用集合来做，比字符串拼接来得直观
	public void run() {
		try {
			HttpTsang httpTsang=new HttpTsang();
			httpTsang.postRequest(urlPath,map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
