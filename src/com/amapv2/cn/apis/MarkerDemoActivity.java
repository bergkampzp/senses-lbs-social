package com.amapv2.cn.apis;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amapv2.cn.apis.util.AMapUtil;
import com.amapv2.cn.apis.util.Constants;
import com.amapv2.cn.apis.util.ToastUtil;
import com.example.lokal.R;

/**
 * AMapV2地图中简单介绍一些Marker的用法.
 */
public class MarkerDemoActivity extends FragmentActivity implements
		OnMarkerClickListener, OnInfoWindowClickListener {
	private MarkerOptions mOptions;

	class CustomInfoWindowAdapter implements InfoWindowAdapter {
		private final RadioGroup mOptions;
		private final View mWindow;
		private final View mContents;

		CustomInfoWindowAdapter() {
			mWindow = getLayoutInflater().inflate(R.layout.custom_info_window,
					null);
			mContents = getLayoutInflater().inflate(
					R.layout.custom_info_contents, null);
			mOptions = (RadioGroup) findViewById(R.id.custom_info_window_options);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
				return null;
			}
			render(marker, mWindow);
			return mWindow;
		}

		@Override
		public View getInfoContents(Marker marker) {
			if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
				return null;
			}
			render(marker, mContents);
			return mContents;
		}

		private void render(Marker marker, View view) {
			int badge;
			if (marker.equals(CHENGDU)) {
				//badge = R.drawable.badge_qld;
			} else if (marker.equals(XIAN)) {
				//badge = R.drawable.badge_nsw;
			} else {
				badge = 0;
			}
			//((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);
			String title = marker.getTitle();
			TextView titleUi = ((TextView) view.findViewById(R.id.title));
			if (title != null) {
				SpannableString titleText = new SpannableString(title);
				titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
						titleText.length(), 0);
				titleUi.setText(titleText);
			} else {
				titleUi.setText("");
			}
			String snippet = marker.getSnippet();
			TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
			if (snippet != null) {
				SpannableString snippetText = new SpannableString(snippet);
				snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0,
						10, 0);
				snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12,
						21, 0);
				snippetUi.setText(snippetText);
			} else {
				snippetUi.setText("");
			}
		}
	}

	private AMap aMap;
	private Marker XIAN;
	private Marker CHENGDU;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marker_demo);
		init();
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
		addMarkersToMap();
		aMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
		aMap.setOnMarkerClickListener(this);
		aMap.setOnInfoWindowClickListener(this);
	}

	private void addMarkersToMap() {
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.738495,
				108.020207), 5.5f));// 把所有标注的marker点就显示在地图可见区域
		CHENGDU = aMap.addMarker(new MarkerOptions()
				.position(Constants.CHENGDU).title("成都市")
				.snippet("成都市:30.658601, 104.064855")
				.icon(BitmapDescriptorFactory.defaultMarker()));

		mOptions = new MarkerOptions();
		mOptions.position(Constants.XIAN);
		mOptions.title("西安市").snippet("西安市：34.341568, 108.940174");
		mOptions.icon(BitmapDescriptorFactory.fromAsset("arrow.png"));// 从
		// assets中读取图片，显示的图片会比原始图片小一点
		// mOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow));//
		// 从drawable中读取图片，正常显示图片
		// mOptions.icon(BitmapDescriptorFactory.fromPath("/sdcard/iqg/arrow.png"));//从手机卡中绝对路径读取图片

		// mOptions.icon(BitmapDescriptorFactory.fromFile("/sdcard/iqg/arrow.png"));//
		// 从手机卡中绝对路径读取图片
		XIAN = aMap.addMarker(mOptions);
		drawMarkers();//
	}

	/**
	 * 绘制系统默认的10种marker背景图片
	 */
	public void drawMarkers() {
		LatLng marker1 = new LatLng(39.24426, 100.18322);
		LatLng marker2 = new LatLng(39.24426, 104.18322);
		LatLng marker3 = new LatLng(39.24426, 108.18322);
		LatLng marker4 = new LatLng(39.24426, 112.18322);
		LatLng marker5 = new LatLng(39.24426, 116.18322);
		LatLng marker6 = new LatLng(36.24426, 100.18322);
		LatLng marker7 = new LatLng(36.24426, 104.18322);
		LatLng marker8 = new LatLng(36.24426, 108.18322);
		LatLng marker9 = new LatLng(36.24426, 112.18322);
		LatLng marker10 = new LatLng(36.24426, 116.18322);
		aMap.addMarker(new MarkerOptions()
				.position(marker1)
				.title("Marker1 ")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		aMap.addMarker(new MarkerOptions()
				.position(marker2)
				.title("Marker2 ")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		aMap.addMarker(new MarkerOptions()
				.position(marker3)
				.title("Marker3 ")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
		aMap.addMarker(new MarkerOptions()
				.position(marker4)
				.title("Marker4 ")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
		aMap.addMarker(new MarkerOptions()
				.position(marker5)
				.title("Marker5 ")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
		aMap.addMarker(new MarkerOptions()
				.position(marker6)
				.title("Marker6 ")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
		aMap.addMarker(new MarkerOptions()
				.position(marker7)
				.title("Marker7 ")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		aMap.addMarker(new MarkerOptions()
				.position(marker8)
				.title("Marker8 ")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
		aMap.addMarker(new MarkerOptions()
				.position(marker9)
				.title("Marker9 ")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
		aMap.addMarker(new MarkerOptions()
				.position(marker10)
				.title("Marker10 ")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
	}

	/**
	 * 清空地图上所有已经标注的marker
	 */
	public void onClearMap(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			aMap.clear();
		}
	}

	/**
	 * 重新标注所有的marker
	 */
	public void onResetMap(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			aMap.clear();
			addMarkersToMap();
		}
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {
		if (marker.equals(XIAN)) {
			if (AMapUtil.checkReady(this, aMap)) {
				jumpPoint(marker);
			}
		}
		return false;
	}

	/**
	 * marker点击时跳动一下
	 */
	public void jumpPoint(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(Constants.XIAN);
		startPoint.offset(0, -100);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1500;

		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * Constants.XIAN.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * Constants.XIAN.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		ToastUtil.show(this, "你点击了Info  Window");
	}

}
