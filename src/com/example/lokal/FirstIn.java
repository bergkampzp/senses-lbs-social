package com.example.lokal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FirstIn extends Activity {

	private ViewPager viewPager;
	private PagerTitleStrip pagerTitleStrip;
	private List<View> list;
	private List<String> titleList;
	Intent intent=new Intent();
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstin);
        
       Button startuse=(Button)findViewById(R.id.startuse);
       startuse.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			intent.setClass(FirstIn.this, Login.class);  
            startActivity(intent);  
            finish();
		}
	});
        
        viewPager = (ViewPager) this.findViewById(R.id.viewpager);
//        pagerTitleStrip = (PagerTitleStrip) this.findViewById(R.id.pagertiltle);
        View init1=LayoutInflater.from(FirstIn.this).inflate(
        		R.layout.init_1,null);
        View init2=LayoutInflater.from(FirstIn.this).inflate(
        		R.layout.init_2,null);
        View init3=LayoutInflater.from(FirstIn.this).inflate(
        		R.layout.init_3,null);
        list = new ArrayList<View>();
        list.add(init1);
        list.add(init2);
        list.add(init3);
        titleList =new ArrayList<String>();
        titleList.add("init1");
        titleList.add("init2");
        titleList.add("init3");
        
        viewPager.setAdapter(new MyAdapter());
        
    }
    class MyAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
		
		public void destroyItem(ViewGroup container, int position,Object object){
			//super.destroyItem(container, position, object);
			((ViewPager)container).removeView(list.get(position));
		}
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return super.getPageTitle(position);
		}
		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub
			((ViewPager)container).addView(list.get(position));
			//return super.instantiateItem(container, position);
			return list.get(position);
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			//return false;
			return arg0==arg1;
		}
    	
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
}