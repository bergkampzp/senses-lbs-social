package com.amapv2.cn.apis.poisearch;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.search.core.AMapException;
import com.amap.api.search.core.LatLonPoint;
import com.amap.api.search.poisearch.PoiItem;
import com.amap.api.search.poisearch.PoiPagedResult;
import com.amap.api.search.poisearch.PoiSearch;
import com.amap.api.search.poisearch.PoiSearch.SearchBound;
import com.amap.api.search.poisearch.PoiTypeDef;
import com.amapv2.cn.apis.util.AMapUtil;
import com.amapv2.cn.apis.util.Constants;
import com.amapv2.cn.apis.util.ToastUtil;
import com.example.lokal.R;

/**
 * poisearch搜索介绍
 */
// LocationSource,
public class PoisearchDemoActivity extends FragmentActivity implements
		OnMarkerClickListener, InfoWindowAdapter, OnInfoWindowClickListener,AMapLocationListener{
	private AMap aMap;
	private TextView searchTextView;
	private String query = null;
	private PoiPagedResult result;
	private ProgressDialog progDialog = null;
	private Button btn;
	private int curpage = 1;
	private int cnt = 0;
	
	private String latitude;
	 private String longtitude;
	 private String address;
	 Double geoLat;
		Double geoLng;
		String cityCode;
		private LocationManagerProxy mAMapLocManager = null;
		
		
	
		private OnLocationChangedListener mListener;
		private LocationManagerProxy mAMapLocationManager;
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poisearch_demo);
		init();
		
		mAMapLocManager = LocationManagerProxy.getInstance(this);
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setUpMap();
			}
		}

	}

	private void setUpMap() {
		
		mAMapLocationManager = LocationManagerProxy
				.getInstance(PoisearchDemoActivity.this);
//		aMap.setLocationSource(this);
		aMap.setMyLocationEnabled(true);
		
		aMap.setOnMarkerClickListener(this);
		aMap.setInfoWindowAdapter(this);
		aMap.setOnInfoWindowClickListener(this);
		searchTextView = (TextView) findViewById(R.id.TextViewSearch);
		searchTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			//	Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_LONG).show();
				onSearchRequested();
			}

		});

		btn = (Button) findViewById(R.id.next);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (cnt >= curpage) {
					handler.sendMessage(Message.obtain(handler,
							Constants.POISEARCH_NEXT));
				}
			}

		});
	}

	@Override
	protected void onNewIntent(final Intent newIntent) {
		super.onNewIntent(newIntent);
		String ac = newIntent.getAction();
		if (Intent.ACTION_SEARCH.equals(ac)) {
			doSearchQuery(newIntent);
		}
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在搜索:\n" + query);
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	protected void doSearchQuery(Intent intent) {
		query = intent.getStringExtra(SearchManager.QUERY);
		SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
				PoisearchDemoActivity.this, MySuggestionProvider.AUTHORITY,
				MySuggestionProvider.MODE);
		suggestions.saveRecentQuery(query, null);
		curpage = 1;
		cnt = 0;
		showProgressDialog();// 显示进度框
		new Thread(new Runnable() {
			public void run() {
				try {
					PoiSearch poiSearch = new PoiSearch(
							PoisearchDemoActivity.this, new PoiSearch.Query(
									query, PoiTypeDef.All, cityCode)); // 设置搜索字符串，"010为城市区号"
					poiSearch.setPageSize(10);// 设置搜索每次最多返回结果数
//					poiSearch.setBound(new SearchBound(new LatLonPoint(
//							39.90403, 116.407525), 50000));// 设置搜索范围
					poiSearch.setBound(new SearchBound(new LatLonPoint(
							geoLat, geoLng), 50000));// 设置搜索范围
					result = poiSearch.searchPOI();
					if (result != null) {
						cnt = result.getPageCount();
					}
					handler.sendMessage(Message.obtain(handler,
							Constants.POISEARCH));
				} catch (AMapException e) {
					handler.sendMessage(Message
							.obtain(handler, Constants.ERROR));
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		return false;
	}

	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		marker.hideInfoWindow();
	}

	/**
	 * 一次性打印多个Marker出来
	 */
	private void addMarkers(List<PoiItem> poiItems) {
		for (int i = 0; i < poiItems.size(); i++) {
			aMap.addMarker(new MarkerOptions()
					.position(
							new LatLng(
									poiItems.get(i).getPoint().getLatitude(),
									poiItems.get(i).getPoint().getLongitude()))
					.title(poiItems.get(i).getTitle())
					.snippet(poiItems.get(i).getSnippet())
					.icon(BitmapDescriptorFactory.defaultMarker()));
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == Constants.POISEARCH) {
				dissmissProgressDialog();// 隐藏对话框
				try {
					if (result != null) {
						List<PoiItem> poiItems = result.getPage(1);
						if (poiItems != null && poiItems.size() > 0) {
							if (aMap == null) {
								return;
							}
							aMap.clear();
							aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									new LatLng(poiItems.get(0).getPoint()
											.getLatitude(), poiItems.get(0)
											.getPoint().getLongitude()), 15));
							addMarkers(poiItems);
							return;
						}
					}
					ToastUtil.show(getApplicationContext(), "无相关结果！");
				} catch (AMapException e) {
					ToastUtil.show(getApplicationContext(), "网络连接错误！");
				}
			} else if (msg.what == Constants.ERROR) {
				dissmissProgressDialog();// 隐藏对话框
				ToastUtil.show(getApplicationContext(), "搜索失败,请检查网络连接！");
			} else if (msg.what == Constants.POISEARCH_NEXT) {
				curpage++;
				try {
					List<PoiItem> poiItems = result.getPage(curpage);
					if (poiItems != null && poiItems.size() > 0) {
						if (aMap == null) {
							return;
						}
						aMap.clear();
						aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
								new LatLng(poiItems.get(0).getPoint()
										.getLatitude(), poiItems.get(0)
										.getPoint().getLongitude()), 15));
						addMarkers(poiItems);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	public boolean enableMyLocation() {
		boolean result = false;
		if (mAMapLocManager
				.isProviderEnabled(LocationProviderProxy.AMapNetwork)) {
			mAMapLocManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
			result = true;
		}
		return result;
	}
	
	public void disableMyLocation() {
		mAMapLocManager.removeUpdates(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		enableMyLocation();
	}
	
	@Override
	protected void onPause() {
		disableMyLocation();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mAMapLocManager != null) {
			mAMapLocManager.removeUpdates(this);
			mAMapLocManager.destory();
		}
		mAMapLocManager = null;
		super.onDestroy();
	}
	
	public static boolean getRequest(String urlPath) throws Exception
	{
		URL url=new URL(urlPath);
		HttpURLConnection con=(HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setReadTimeout(5*1000);
		if(con.getResponseCode()==200)
		{
			return true;
		}
		return false;
	}
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			geoLat = location.getLatitude();
			geoLng = location.getLongitude();
			cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				address=desc = locBundle.getString("desc");
			}
//			String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
//					+ "\n精    度    :" + location.getAccuracy() + "米"
//					+ "\n城市编码:" + cityCode + "\n位置描述:" + desc);
//			latitude=geoLat+"";
//			longtitude=geoLng+"";
//			
//			Message msg = new Message();
//			msg.obj = str;
//			if (handler != null) {
//				handler.sendMessage(msg);
//			}
		}
	}
//开始的时候进行定位
//	@Override
//	public void activate(OnLocationChangedListener listener) {
//		mListener = listener;
//		if (mAMapLocationManager == null) {
//			mAMapLocationManager = LocationManagerProxy.getInstance(this);
//		}
//		mAMapLocationManager.requestLocationUpdates(
//				LocationProviderProxy.AMapNetwork, 10, 5000, this);
//	}
//
//	/**
//	 * 停止定位
//	 */
//	@Override
//	public void deactivate() {
//		mListener = null;
//		mAMapLocationManager.removeUpdates(this);
//		mAMapLocationManager.destory();
//		mAMapLocationManager = null;
//	}



}
