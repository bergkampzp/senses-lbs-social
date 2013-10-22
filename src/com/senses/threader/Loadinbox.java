package com.senses.threader;

import com.senses.common.LoadData;

public class Loadinbox extends Thread {
 
	public static String msgjson;
	public static int num;
	public static String user_id;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		msgjson=new LoadData().getInboxBeans(num,user_id);
		
	}
}
