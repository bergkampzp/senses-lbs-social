package com.senses.threader;

import com.senses.common.LoadData;

public class MsgRefresh extends Thread {

	public static String msgjson;
	public static int num;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		msgjson=new LoadData().RefreshMsg();
		
	}
}
