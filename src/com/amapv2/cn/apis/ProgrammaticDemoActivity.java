package com.amapv2.cn.apis;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.maps.AMap;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

/**
 * 通过Java代码添加一个AMap对象
 */
public class ProgrammaticDemoActivity extends FragmentActivity {

	private static final String MAP_FRAGMENT_TAG = "map";
	private AMap aMap;
	private SupportMapFragment mMapFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		mMapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentByTag(MAP_FRAGMENT_TAG);
		if (mMapFragment == null) {
			mMapFragment = SupportMapFragment.newInstance();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.add(android.R.id.content, mMapFragment,
					MAP_FRAGMENT_TAG);
			fragmentTransaction.commit();
		}

	}

	private void initMap() {
		if (aMap == null) {
			aMap = mMapFragment.getMap();
			aMap.addMarker(new MarkerOptions()
					.position(new LatLng(39.990770, 116.472220))
					.title("Marker").snippet("方恒国际中心大楼")
					.icon(BitmapDescriptorFactory.defaultMarker()));

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "添加地图");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 1) {
			initMap();
		}
		return super.onOptionsItemSelected(item);
	}
}
