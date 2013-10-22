package com.amapv2.cn.apis;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amapv2.cn.apis.util.AMapUtil;
import com.amapv2.cn.apis.util.Constants;
import com.amapv2.cn.apis.util.ToastUtil;
import com.example.lokal.R;

/**
 * AMapV2地图简单显示，添加一个marker并且对marker响应点击事件
 */
public class BasicMapActivity extends FragmentActivity implements
		OnMarkerClickListener {
	private AMap aMap;
	private Marker defaultMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_demo);
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
		// 对地图添加一个marker
		defaultMarker = aMap.addMarker(new MarkerOptions()
				.position(Constants.FANGHENG).title("方恒").snippet("方恒国际中心大楼A座")
				.icon(BitmapDescriptorFactory.defaultMarker()));
		aMap.getUiSettings().setZoomControlsEnabled(true);// 设置系统默认缩放按钮可见
		aMap.setOnMarkerClickListener(this);// 对marker添加点击监听器
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		if (marker.equals(defaultMarker)) {
			ToastUtil.show(BasicMapActivity.this, marker.getSnippet());
		}
		return false;
	}
}
