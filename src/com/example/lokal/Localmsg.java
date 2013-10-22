package com.example.lokal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lokal.Localmsg_list.OnRefreshListener;
import com.senses.threader.LoadLocalmsg;
import com.senses.threader.MsgRefresh;
import com.technotalkative.loadwebimage.imageutils.LazyAdapter;

// implements OnScrollListener
public class Localmsg extends Activity implements OnScrollListener {
	private WebView mWebView;       
    private Handler mHandler = new Handler();
    Localmsg_list localmsg_list;
    //private ListView localmsg_list;
    LazyAdapter adapter;
    public int num=1;
    
    Intent intent=new Intent();
    List<Map<String, Object>> list =  new ArrayList<Map<String,Object>>();
 
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
//		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
//		LinearLayout.LayoutParams.FILL_PARENT);
		localmsg_list.setCacheColorHint(Color.WHITE);
		//myListLayout.addView(localmsg_list,localLayoutParams);
		// adapter=new SimpleAdapter(this,getData(),R.layout.localmsg_list,new String[]{"img","content","info","msgid","msgimg"},new int[]{R.id.img,R.id.content,R.id.info,R.id.msgid,R.id.msgimg});
		adapter=new LazyAdapter(Localmsg.this, getData("standard"));
		localmsg_list.setAdapter(adapter);
    	super.onStart();
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localmsg);
		/************************************GET PHONE NUMBER 
		TelephonyManager mTelephonyMgr;  
	    mTelephonyMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);   
	    Toast.makeText(Localmsg.this, mTelephonyMgr.toString()+"toasted",Toast.LENGTH_SHORT).show();
	    ************************************/

//		SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
//		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
//		items.add(new SatelliteMenuItem(4, R.drawable.ic_1));
//		items.add(new SatelliteMenuItem(4, R.drawable.ic_3));
//		items.add(new SatelliteMenuItem(4, R.drawable.ic_4));
//		items.add(new SatelliteMenuItem(3, R.drawable.ic_5));
//		items.add(new SatelliteMenuItem(2, R.drawable.ic_6));
//		items.add(new SatelliteMenuItem(1, R.drawable.ic_2));
//		menu.addItems(items);


		
		
		//mWebView = (WebView) findViewById(R.id.localmsg_web);      
		ImageButton writebtn=(ImageButton)findViewById(R.id.writebtn1);
		ImageButton takephoto=(ImageButton)findViewById(R.id.takephoto);

		writebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				intent.setClass(Localmsg.this, Write.class);  
                startActivity(intent);  

			}
		});
		takephoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Localmsg.this).setTitle("选择功能").setItems(R.array.arrcontent, new DialogInterface.OnClickListener(){
					
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
							intent.setClass(Localmsg.this, Camera.class);
							startActivity(intent);
							break;
						case 2:
							Intent getVideoByCamera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
							getVideoByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//设置图片质量
							
							startActivityForResult(getVideoByCamera, 4);
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
		
		
		localmsg_list = (Localmsg_list) findViewById(R.id.myListLayout);
		
		localmsg_list.setDividerHeight(20);
		//LinearLayout myListLayout=(LinearLayout)this.findViewById(R.id.myListLayout);
	//	localmsg_list=new ListView(this);
		final Button btnLoadMore = new Button(this);
		btnLoadMore.setText("Load More");
		btnLoadMore.setBackgroundResource(R.drawable.more_bg);
		localmsg_list.addFooterView(btnLoadMore);
		
		btnLoadMore.setOnClickListener(new View.OnClickListener() {
			//List<Map<String, Object>> list2 =  new ArrayList<Map<String,Object>>();
			@Override
			public void onClick(View arg0) {		
				//这儿是本Activity名加this
				//SimpleAdapter adapter2=new SimpleAdapter(Localmsg.this,getData(),R.layout.localmsg_list,new String[]{"img","content","info","msgid","msgimg"},new int[]{R.id.img,R.id.content,R.id.info,R.id.msgid,R.id.msgimg});
				//adapter=new LazyAdapter(Localmsg.this, getData());
				btnLoadMore.setText("Loading...");
				getData("standard");
				adapter.notifyDataSetChanged();
				//localmsg_list.setAdapter(adapter);
				btnLoadMore.setText("Load More");
			}
		});
		
		
		
		


		
		localmsg_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
//				TextView ext_txt=(TextView)findViewById(R.id.content);
//				String ext_str=ext_txt.getText().toString();
				List<Map<String, Object>> list =  new ArrayList<Map<String,Object>>();

				Map<String, String> map = (Map<String, String>) arg0.getItemAtPosition(arg2);
				//String test=map.get("content");
				intent.putExtra("msgid",map.get("msgid"));
				intent.putExtra("rec_id", map.get("user_id"));
				intent.putExtra("content", map.get("content"));
				intent.setClass(Localmsg.this,Comment.class);  
                startActivity(intent);
				//Toast.makeText(Localmsg.this, test,Toast.LENGTH_SHORT).show();
			}
		});
		localmsg_list.setonRefreshListener(new OnRefreshListener() {
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
						//BaseAdapter adapter;
						list.clear();
						adapter.notifyDataSetChanged();
						localmsg_list.onRefreshComplete();
						adapter=new LazyAdapter(Localmsg.this, getData("refresh"));
						
						localmsg_list.setAdapter(adapter);
					}

				}.execute();
			}
		});
	}

	

	
	public List<Map<String, Object>> getData(String act) {
		Map<String, Object> map;
//		LoadData jsondemo=new LoadData();
//		String jsonlist=jsondemo.getMsgBeans(); 		//旧方式   已改为通过线程获取
//		List<CityBean> cList=new ArrayList<CityBean>();
		String jsonlist;
		if(act=="refresh"){
//			MsgRefresh msgRefresh=new MsgRefresh();
//			msgRefresh.start();
//			Log.v("getdata", "refresh");
//			jsonlist=msgRefresh.msgjson;
			num=1;
			Log.v("getdata","num is 1 now");
		}
		Log.v("thenum", "num"+num);
			LoadLocalmsg loadLocalmsg=new LoadLocalmsg();
			Log.v("getdata", "standard");
			Log.v("test", "num is:"+num);
			loadLocalmsg.num=num;
			loadLocalmsg.start();
			jsonlist=loadLocalmsg.msgjson;
			num++;
			if(act=="refresh"){
				num=2;
			}
		
		try {
			JSONArray jay = new JSONArray(jsonlist);
			for (int i = 0; i < jay.length(); i++) {
				JSONObject temp=(JSONObject)jay.get(i);
//				CityBean city=new CityBean();
				//LoadImage bitmap=
						//new LoadImage(temp.getString("img"));
//				new LoadImage("http://10.254.8.113/lokal/himg/demo.jpg");
//				ImageView imageView=(ImageView)findViewById(R.id.msgimg);
//				ImageCall_Back resId =null;
//				
//				new AsyncImageLoader().loadBitmap(imageView,"http://10.254.8.113/lokal/himg/demo.jpg",resId);
				map=new HashMap<String, Object>();
				map.put("msgid", temp.getString("msgid"));
				map.put("user_id", temp.get("user_id"));
				map.put("img", temp.getString("face"));
				map.put("content", temp.getString("username")+" : "+temp.getString("content"));
				map.put("info", temp.getString("info")+"@"+temp.getString("address"));
				map.put("msgimg", temp.getString("img"));
//				map.put("info", temp.getString("msgid"));
				list.add(map);				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
		return list;
		}
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK  && requestCode==3){
			
	}
	}

}




