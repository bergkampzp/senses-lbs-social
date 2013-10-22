package com.senses.threader;

import java.util.HashMap;
import java.util.Map;

import com.senses.common.HttpTsang;

public class MsgSend extends Thread {

	public static String urlPath="http://10.254.8.113/lokal/msgpost.php";
	public Map<String,String> map=new HashMap<String,String>();//用集合来做，比字符串拼接来得直观
	public static String status;
	public void run() {
		try {
			HttpTsang httpTsang=new HttpTsang();
			status=httpTsang.postRequest(urlPath,map);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
