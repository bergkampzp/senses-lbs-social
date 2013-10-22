package com.amapv2.cn.apis;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amapv2.cn.apis.util.AMapUtil;
import com.amapv2.cn.apis.util.Constants;
import com.example.lokal.R;

/**
 * AMapV2地图中简单介绍一些Polyline的用法.
 */
public class PolylineDemoActivity extends FragmentActivity implements
		OnSeekBarChangeListener {
	private static final int WIDTH_MAX = 50;
	private static final int HUE_MAX = 360;
	private static final int ALPHA_MAX = 255;

	private AMap aMap;
	private Polyline mMutablePolyline;
	private SeekBar mColorBar;
	private SeekBar mAlphaBar;
	private SeekBar mWidthBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.polyline_demo);
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		mColorBar = (SeekBar) findViewById(R.id.hueSeekBar);
		mColorBar.setMax(HUE_MAX);
		mColorBar.setProgress(0);

		mAlphaBar = (SeekBar) findViewById(R.id.alphaSeekBar);
		mAlphaBar.setMax(ALPHA_MAX);
		mAlphaBar.setProgress(255);

		mWidthBar = (SeekBar) findViewById(R.id.widthSeekBar);
		mWidthBar.setMax(WIDTH_MAX);
		mWidthBar.setProgress(10);
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// 绘制一条折线
		aMap.addPolyline((new PolylineOptions())
				.add(Constants.HUHEHAOTE, Constants.BEIJING, Constants.HAERBIN)
				.color(Color.RED).width(5));
		// 绘制一个封闭的多边形
		mMutablePolyline = aMap.addPolyline((new PolylineOptions())
				.add(Constants.CHENGDU, Constants.XIAN, Constants.ZHENGZHOU,
						Constants.SHANGHAI, Constants.CHENGDU).width(5)
				.color(Color.BLUE));
		PolylineOptions options = new PolylineOptions();
		int radius = 5;
		int numPoints = 100;
		double phase = 2 * Math.PI / numPoints;
		for (int i = 0; i <= numPoints; i++) {
			options.add(new LatLng(Constants.XINING.latitude + radius
					* Math.sin(i * phase), Constants.XINING.longitude + radius
					* Math.cos(i * phase)));
		}
		int color = Color.HSVToColor(mAlphaBar.getProgress(), new float[] {
				mColorBar.getProgress(), 1, 1 });
		aMap.addPolyline(options.color(color).width(mWidthBar.getProgress()));
		mColorBar.setOnSeekBarChangeListener(this);
		mAlphaBar.setOnSeekBarChangeListener(this);
		mWidthBar.setOnSeekBarChangeListener(this);
		aMap.moveCamera(CameraUpdateFactory.newLatLng(Constants.XINING));
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	/**
	 * Polyline中对填充颜色，透明度，画笔宽度设置响应事件
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (mMutablePolyline == null) {
			return;
		}
		if (seekBar == mColorBar) {
			mMutablePolyline.setColor(Color.HSVToColor(
					Color.alpha(mMutablePolyline.getColor()), new float[] {
							progress, 1, 1 }));
		} else if (seekBar == mAlphaBar) {
			float[] prevHSV = new float[3];
			Color.colorToHSV(mMutablePolyline.getColor(), prevHSV);
			mMutablePolyline.setColor(Color.HSVToColor(progress, prevHSV));
		} else if (seekBar == mWidthBar) {
			mMutablePolyline.setWidth(progress);
		}
	}
}
