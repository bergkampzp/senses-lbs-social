package com.technotalkative.loadwebimage.imageutils;


import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lokal.R;
import com.senses.config.Config;

public class InboxAdapter extends BaseAdapter {
    
    private Activity activity;
    //private String[] data;
    //Map<String, Object> dataList;
    List<Map<String, Object>>list;  
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public InboxAdapter(Activity a, List<Map<String, Object>>list) {
        activity = a;
        //data=d;
        this.list=list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        //return data.length;
    	 return this.list!=null?this.list.size(): 0;
    }

    public Object getItem(int position) {
       // return position;
    	 return this.list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.inbox_list, null);

        TextView msgid=(TextView)vi.findViewById(R.id.msgid);
        TextView user_id=(TextView)vi.findViewById(R.id.user_id);
        TextView text=(TextView)vi.findViewById(R.id.content);
        ImageView image=(ImageView)vi.findViewById(R.id.img);
    //    ImageView msgimg=(ImageView)vi.findViewById(R.id.msgimg);
        msgid.setText(list.get(position).get("msgid").toString());
        user_id.setText(list.get(position).get("user_id").toString());
        text.setText(list.get(position).get("content").toString());
        imageLoader.DisplayImage(new Config().BASEURI+"/upload/"+list.get(position).get("img").toString(), image);
  //      int imgor=list.get(position).get("msgimg").toString().indexOf("MSGIMG");
//        if(imgor!=-1){
//        	imageLoader.DisplayImage(new Config().BASEURI+"/upload/"+list.get(position).get("msgimg").toString(), msgimg);
//        }
 //       Log.v("imgtest", list.get(position).get("msgimg").toString());
        return vi;
    }
}