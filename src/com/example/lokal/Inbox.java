package com.example.lokal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;

import com.example.lokal.Localmsg_list.OnRefreshListener;
import com.senses.threader.Loadinbox;
import com.technotalkative.loadwebimage.imageutils.InboxAdapter;
import com.technotalkative.loadwebimage.imageutils.LazyAdapter;

public class Inbox extends Activity {

	InboxAdapter adapter;
	//LazyAdapter adapter;
	Localmsg_list inbox_list;
	Intent intent=new Intent();
	int num=1;
	String user_id;
	Context ctx = Inbox.this; 
	List<Map<String, Object>> list =  new ArrayList<Map<String,Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox);
		inbox_list = (Localmsg_list) findViewById(R.id.myInboxListLayout);
		final Button btnLoadMore = new Button(this);
		btnLoadMore.setText("Load More");
		btnLoadMore.setBackgroundResource(R.drawable.more_bg);
		inbox_list.addFooterView(btnLoadMore);
		//adapter=new SimpleAdapter(Inbox.this,getData(),R.layout.inbox_list,new String[]{"img","content","info","msgid"},new int[]{R.id.img,R.id.content,R.id.info,R.id.msgid});
		adapter=new InboxAdapter(Inbox.this, getData("standard"));
		inbox_list.setAdapter(adapter);
		inbox_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				List<Map<String, Object>> list =  new ArrayList<Map<String,Object>>();

				Map<String, String> map = (Map<String, String>) arg0.getItemAtPosition(arg2);
				//String test=map.get("content");
				intent.putExtra("msgid",map.get("msgid"));
				intent.putExtra("rec_id", map.get("user_id"));
				intent.putExtra("content", map.get("content"));
				intent.setClass(Inbox.this,Comment.class);  
                startActivity(intent);
			}
		});
		btnLoadMore.setOnClickListener(new View.OnClickListener() {
			//List<Map<String, Object>> list2 =  new ArrayList<Map<String,Object>>();
			@Override
			public void onClick(View arg0) {		
				//这儿是本Activity名加this
				//adapter=new SimpleAdapter(Inbox.this,getData(),R.layout.inbox_list,new String[]{"img","content","info","msgid"},new int[]{R.id.img,R.id.content,R.id.info,R.id.msgid});
				//adapter=new LazyAdapter(Inbox.this, getData());
				btnLoadMore.setText("Loading...");
//				adapter=new InboxAdapter(Inbox.this, getData());
//				inbox_list.setAdapter(adapter);
				getData("standard");
				adapter.notifyDataSetChanged();
				btnLoadMore.setText("Load More");
			}
		});
		inbox_list.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						//data.addFirst("刷新后的内容");
						
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						list.clear();
						//BaseAdapter adapter;
						adapter.notifyDataSetChanged();
						inbox_list.onRefreshComplete();
						//adapter=new SimpleAdapter(Inbox.this,getData(),R.layout.inbox_list,new String[]{"img","content","info","msgid"},new int[]{R.id.img,R.id.content,R.id.info,R.id.msgid});
						adapter=new InboxAdapter(Inbox.this, getData("refresh"));
						inbox_list.setAdapter(adapter);
						
						
//						list.clear();
//						getData("refresh");
//						adapter.notifyDataSetChanged();
			//			localmsg_list.onRefreshComplete();
			//			adapter=new LazyAdapter(Localmsg.this, getData("refresh"));
						
			//			localmsg_list.setAdapter(adapter);
					}

				}.execute();
			}
		});
	}
	
	//SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
	
	
	public List<Map<String, Object>> getData(String act) {
		Map<String, Object> map;

		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		
		if(act=="refresh"){
			num=1;
		}
		Loadinbox loadInbox=new Loadinbox();
		loadInbox.num=num;
		loadInbox.user_id=sp.getString("ID_KEY","none");;
		loadInbox.start();
		String jsonlist=loadInbox.msgjson;
		num++;
		try {
			JSONArray jay = new JSONArray(jsonlist);
			for (int i = 0; i < jay.length(); i++) {
				JSONObject temp=(JSONObject)jay.get(i);

				map=new HashMap<String, Object>();
				map.put("img", temp.getString("face"));
				map.put("content", temp.getString("username")+" : "+temp.getString("content"));
				map.put("info", "info");
				map.put("user_id", temp.getString("user_id"));
				//map.put("msgimg", temp.getString("img"));
				map.put("msgid", temp.getString("msgid"));
				list.add(map);				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
		return list;
		}
}
