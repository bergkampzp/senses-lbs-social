package com.amapv2.cn.apis;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;

import com.amap.api.maps.AMap;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amapv2.cn.apis.util.AMapUtil;
import com.example.lokal.R;

/**
 * UI settings一些选项设置响应事件
 */
public class UiSettingsDemoActivity extends FragmentActivity {
	private AMap aMap;
	private UiSettings mUiSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_settings_demo);
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
				mUiSettings = aMap.getUiSettings();
			}
		}
	}

	/**
	 * 设置地图默认的缩放按钮是否显示
	 */
	public void setZoomButtonsEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setZoomControlsEnabled(((CheckBox) v).isChecked());
		}

	}

	/**
	 * 设置地图默认的定位按钮是否显示
	 */
	public void setMyLocationButtonEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setMyLocationButtonEnabled(((CheckBox) v).isChecked());
		}

	}

	/**
	 * 设置地图是否可以自动定位
	 */
	public void setMyLocationLayerEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			return;
		}
		aMap.setMyLocationEnabled(((CheckBox) v).isChecked());
	}

	/**
	 * 设置地图是否可以手势滑动
	 */
	public void setScrollGesturesEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setScrollGesturesEnabled(((CheckBox) v).isChecked());
		}

	}

	/**
	 * 设置地图是否可以手势缩放大小
	 */
	public void setZoomGesturesEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setZoomGesturesEnabled(((CheckBox) v).isChecked());
		}

	}

	/**
	 * 设置地图是否可以倾斜
	 */
	public void setTiltGesturesEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setTiltGesturesEnabled(((CheckBox) v).isChecked());
		}

	}

	/**
	 * 设置地图是否可以旋转
	 */
	public void setRotateGesturesEnabled(View v) {
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings.setRotateGesturesEnabled(((CheckBox) v).isChecked());
		}
	}
}
