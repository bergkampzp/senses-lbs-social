package com.senses.threader;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.lokal.Login;
import com.senses.common.HttpTsang;

public class LoginReg extends Thread {

//	private Handler ctx=new Login().getHandler();
	private Context ctx=new Login();
	
	public static String urlPath="http://10.254.8.113/lokal/login.php";
	public Map<String,String> map=new HashMap<String,String>();//用集合来做，比字符串拼接来得直观
	public static String status;
	public void run() {
		try {
			HttpTsang httpTsang=new HttpTsang();
			status=httpTsang.postRequest(urlPath,map);
//			Message message = Message.obtain();
//			message.obj=status;
//			ctx.sendMessage(message);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
