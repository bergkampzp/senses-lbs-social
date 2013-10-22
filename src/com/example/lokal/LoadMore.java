package com.example.lokal;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
public class LoadMore extends Activity {
 private MyAdapter adapter;
 private Handler handler = new Handler();
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.loadmore);
  adapter = new MyAdapter();
  ListView lsv = (ListView) findViewById(R.id.lsvData);
  View footView = getLayoutInflater().inflate(R.layout.loadmore_1, null);
  Button btnLoadMore = (Button) footView.findViewById(R.id.btnLoadMore);
  btnLoadMore.setOnClickListener(new OnClickListener() {
   public void onClick(View v) {
    handler.post(new Runnable() {
     public void run() {
      adapter.count += 10;
      adapter.notifyDataSetChanged();
     }
    });
   }
  });
  lsv.addFooterView(footView);
  lsv.setAdapter(adapter);
 }
 class MyAdapter extends BaseAdapter {
  int count = 10;
  public int getCount() {
   return count;
  }
  public Object getItem(int position) {
   return null;
  }
  public long getItemId(int position) {
   return 0;
  }
  public View getView(int position, View convertView, ViewGroup parent) {
   TextView tv;
   if (convertView == null) {
    tv = new TextView(LoadMore.this);
   } else {
    tv = (TextView) convertView;
   }
   tv.setText("Item ADD BY ME" + position);
   tv.setTextSize(20f);
   tv.setGravity(Gravity.LEFT);
   tv.setHeight(60);
   return tv;
  }
 }
}