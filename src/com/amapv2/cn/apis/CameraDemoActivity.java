package com.amapv2.cn.apis;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.MarkerOptions;
import com.amapv2.cn.apis.util.AMapUtil;
import com.amapv2.cn.apis.util.Constants;
import com.example.lokal.R;

/**
 * AMapV2地图中简单介绍一些Camera的用法.
 */
public class CameraDemoActivity extends FragmentActivity {
	private static final int SCROLL_BY_PX = 30;
	static final CameraPosition ZHONGGUANCUN = new CameraPosition.Builder()
			.target(Constants.ZHONGGUANCUN).zoom(18).bearing(70).tilt(0)
			.build();
	static final CameraPosition LUJIAZUI = new CameraPosition.Builder()
			.target(Constants.SHANGHAI).zoom(18).bearing(0).tilt(70).build();
	private AMap aMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_demo);
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
		aMap.addMarker(new MarkerOptions()
				.position(Constants.FANGHENG)
				.snippet("方恒国际中心大楼")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
	}

	/**
	 * 点击“去中关村”按钮响应事件
	 */
	public void onGoToZhongguancun(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			changeCamera(CameraUpdateFactory.newCameraPosition(ZHONGGUANCUN));
		}

	}

	/**
	 * 点击“去陆家嘴”按钮响应事件
	 */
	public void onGoToLujiazui(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			changeCamera(CameraUpdateFactory.newCameraPosition(LUJIAZUI));
		}

	}

	/**
	 * 点击地图缩小按钮响应事件
	 */
	public void onZoomIn(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			changeCamera(CameraUpdateFactory.zoomIn());
		}
	}

	/**
	 * 点击地图放大按钮响应事件
	 */
	public void onZoomOut(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			changeCamera(CameraUpdateFactory.zoomOut());
		}
	}

	/**
	 * 点击向左移动按钮响应事件，camera将向左边移动
	 */
	public void onScrollLeft(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			changeCamera(CameraUpdateFactory.scrollBy(-SCROLL_BY_PX, 0));
		}

	}

	/**
	 * 点击向右移动按钮响应事件，camera将向右边移动
	 */
	public void onScrollRight(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			changeCamera(CameraUpdateFactory.scrollBy(SCROLL_BY_PX, 0));
		}

	}

	/**
	 * 点击向上移动按钮响应事件，camera将向上边移动
	 */
	public void onScrollUp(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			changeCamera(CameraUpdateFactory.scrollBy(0, -SCROLL_BY_PX));
		}

	}

	/**
	 * 点击向下移动按钮响应事件，camera将向下边移动
	 */
	public void onScrollDown(View view) {
		if (AMapUtil.checkReady(this, aMap)) {
			changeCamera(CameraUpdateFactory.scrollBy(0, SCROLL_BY_PX));
		}

	}

	private void changeCamera(CameraUpdate update) {
		aMap.moveCamera(update);
	}
}
