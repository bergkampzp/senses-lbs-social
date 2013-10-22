package com.example.lokal;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.senses.config.Config;

public class Camera extends Activity implements OnClickListener,AMapLocationListener{
ImageView galleryImage;
Button openGallery;
Button cameraButton;
Bitmap bitmap;
String filenameString;
String user_id;



//for location
private TextView show;
private EditText txt;
private String latitude;
private String longtitude;
private String address;
Map<String,String> map=new HashMap<String,String>();//用集合来做，比字符串拼接来得直观
Button uploadBtn;

MyHandler myHandler;
	private LocationManagerProxy mAMapLocManager = null;
	private TextView myLocation;
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			myLocation.setText((String) msg.obj);
//		}
//	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        galleryImage = (ImageView) findViewById(R.id.galleryImage);
        openGallery = (Button) findViewById(R.id.gallerySelect);
        cameraButton = (Button) findViewById(R.id.cameraSelect);
        openGallery.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        
        mAMapLocManager = LocationManagerProxy.getInstance(this);
    }

    public void onClick(View v){   
switch(v.getId()){
    
case R.id.gallerySelect:
    
Intent intent = new Intent();

intent.setType("image/*");

intent.setAction(Intent.ACTION_GET_CONTENT);

startActivityForResult(intent,3);
    
break;
    
case R.id.cameraSelect:
    
Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);//设置图片质量

startActivityForResult(getImageByCamera, 4);
    
break;

    
}
    }

    @Override

protected void onActivityResult(int requestCode, int resultCode, Intent data) {

if(resultCode == RESULT_OK  && requestCode==3){
Uri uri = data.getData();
ContentResolver cr = getContentResolver();
try {
InputStream in = cr.openInputStream(uri);
String newuri=uri.toString();


new Upload().start();

Toast.makeText(getApplicationContext(), newuri, Toast.LENGTH_LONG).show();


bitmap = BitmapFactory.decodeStream(in);
galleryImage.setImageBitmap(bitmap);
} catch (FileNotFoundException e) {
e.printStackTrace();
}
}else if (resultCode == RESULT_OK  && requestCode==4){

try{
Bundle extras = data.getExtras();
bitmap = (Bitmap) extras.get("data");
galleryImage.setImageBitmap(bitmap);
int timeString=Math.round(new Date().getTime()/1000);
Context ctx = Camera.this; 
SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
user_id=sp.getString("ID_KEY", "NONE");
filenameString=user_id+"_MSGIMG_"+timeString+".jpg";
File imgdemo=new File("/sdcard/"+filenameString);
BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(imgdemo));
bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
bos.flush();
bos.close();
}catch(Exception e){
e.printStackTrace();
}

uploadBtn=(Button)findViewById(R.id.uploadImg);

uploadBtn.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		uploadBtn.setClickable(false);
		uploadBtn.setText("上传中,请稍等...");
		// TODO Auto-generated method stub
		EditText imgabout=(EditText)findViewById(R.id.imgAbout);
		String imgaboutString=imgabout.getText().toString();
		
//		Map<String,String> map=new HashMap<String,String>();//用集合来做，比字符串拼接来得直观
		map.put("uid", user_id);
		map.put("img", filenameString);
		map.put("content", imgaboutString);
		map.put("latitude", latitude);
		map.put("longtitude", longtitude);
		map.put("address", address);
/*		
		Upload updemo=new Upload();
		updemo.map=map;
		
	//	updemo.actionUrl=new Config().BASEURI+"/upload.php?id="+user_id+"&filename="+filenameString+"&imgabout="+imgaboutString;
		updemo.actionUrl=new Config().BASEURI+"/upload.php";
		updemo.uploadFile="/sdcard/"+filenameString;
		updemo.newName=filenameString;
		updemo.start();
*/
		myHandler=new MyHandler(Camera.this.getMainLooper());
        
        MyThread m = new MyThread();
    //    m.urlPath=urlPath;
         m.map2=map;
        new Thread(m).start();
	}
});
}
    }
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
		Double geoLat = location.getLatitude();
		Double geoLng = location.getLongitude();
		String cityCode = "";
		String desc = "";
		Bundle locBundle = location.getExtras();
		if (locBundle != null) {
			cityCode = locBundle.getString("citycode");
			address=desc = locBundle.getString("desc");
		}
//		String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
//				+ "\n精    度    :" + location.getAccuracy() + "米"
//				+ "\n城市编码:" + cityCode + "\n位置描述:" + desc);
		latitude=geoLat+"";
		longtitude=geoLng+"";
//		
//		Message msg = new Message();
//		msg.obj = str;
//		if (handler != null) {
//			handler.sendMessage(msg);
//		}
	}
}



class MyHandler extends Handler {
    public MyHandler() {
    }

    public MyHandler(Looper L) {
        super(L);
    }

    // 必须重写这个方法，用于处理message
    @Override
    public void handleMessage(Message msg) {
        // 这里用于更新UI
        Bundle b = msg.getData();
        String response=b.getString("response");
		String data_r="success";
		int s=response.indexOf(data_r);
		//Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
		if(s!=-1){
			Log.v("the upload","intent.......");
			Intent intent=new Intent();
			Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_LONG).show();
			intent.setClass(Camera.this,Dashboard.class);  
            startActivity(intent); 
            finish();
		}else{
			Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

		}

    }
}

class MyThread implements Runnable {
	String urlPath;
	Map<String,String> map2=new HashMap<String,String>();
    public void run() {
    	
    	try {
    		
    		Upload updemo=new Upload();
//    		updemo.map=map;
    		
    	//	updemo.actionUrl=new Config().BASEURI+"/upload.php?id="+user_id+"&filename="+filenameString+"&imgabout="+imgaboutString;
//    		updemo.actionUrl=new Config().BASEURI+"/upload.php";
//    		updemo.uploadFile="/sdcard/"+filenameString;
//    		updemo.newName=filenameString;
    		String response=updemo.sendFile(new Config().BASEURI+"/upload.php", "/sdcard/"+filenameString,filenameString);
    		
    		String data_r="Uploaded";
			int s=response.indexOf(data_r);
			Log.v("the upload","up.......");
			//Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
			if(s!=-1){
				String status=updemo.postRequest(new Config().BASEURI+"/msgpost.php", map2);
    			Message msg = myHandler.obtainMessage();
    			Bundle b = new Bundle();
    			b.putString("response", status);
    			msg.setData(b);
    			myHandler.sendMessage(msg);
    			Log.v("the upload","post.......");
			}else{
				//Toast.makeText(getApplicationContext(), "Failed!Please check your network", Toast.LENGTH_LONG).show();
			}

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

	

}
