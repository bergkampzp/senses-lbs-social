package com.example.lokal;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.lokal.Camera.MyHandler;
import com.example.lokal.Camera.MyThread;
import com.senses.config.Config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class PicCutActivity extends Activity implements OnClickListener {
    private ImageButton img_btn;
    private Button btn;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    MyHandler myHandler;
    public String faceString;
    String user_id;
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
    

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_upload);
        Button face_send=(Button)findViewById(R.id.face_send);
        Button noface=(Button)findViewById(R.id.noface);
        noface.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent noface=new Intent();
				noface.setClass(PicCutActivity.this,Dashboard.class);  
                startActivity(noface); 
			}
		});
        face_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	        	Toast.makeText(PicCutActivity.this, "ccccc",Toast.LENGTH_SHORT).show();
	        	myHandler=new MyHandler(PicCutActivity.this.getMainLooper());
	            
	            MyThread m = new MyThread();
	        //    m.urlPath=urlPath;
	        //     m.map2=map;
	            new Thread(m).start();
			}
		});
        init();
    }

    //初始化控件
    private void init() {
        img_btn = (ImageButton) findViewById(R.id.img_btn);
        btn = (Button) findViewById(R.id.btn);
        
        //为ImageButton和Button添加监听事件
        img_btn.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.img_btn:
            showDialog();
            break;

        case R.id.btn:
            showDialog();
            break;

        }

    }

    
    //提示对话框方法
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("头像设置")
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        // 调用系统的拍照功能
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }
                })
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
        case PHOTO_REQUEST_TAKEPHOTO:
            startPhotoZoom(Uri.fromFile(tempFile), 150);
            break;

        case PHOTO_REQUEST_GALLERY:
            if (data != null)
                startPhotoZoom(data.getData(), 150);
            break;

        case PHOTO_REQUEST_CUT:
            if (data != null)
				try {
					setPicToView(data);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) throws IOException {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            
            Context ctx = PicCutActivity.this; 
            SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
            user_id=sp.getString("ID_KEY", "NONE");
            
			try {
				faceString="face"+user_id+".jpeg";
				File imgdemo=new File(Environment.getExternalStorageDirectory(),faceString);
	            BufferedOutputStream bos;
				bos = new BufferedOutputStream(new FileOutputStream(imgdemo));
				photo.compress(Bitmap.CompressFormat.JPEG,100,bos);
	            bos.flush();
	            bos.close();
	            
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            Drawable drawable = new BitmapDrawable(photo);
            img_btn.setBackgroundDrawable(drawable);
        }
    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
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
    			Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
    			intent.setClass(PicCutActivity.this,Dashboard.class);  
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
//        		updemo.map=map;
        		
        	//	updemo.actionUrl=new Config().BASEURI+"/upload.php?id="+user_id+"&filename="+filenameString+"&imgabout="+imgaboutString;
//        		updemo.actionUrl=new Config().BASEURI+"/upload.php";
//        		updemo.uploadFile="/sdcard/"+filenameString;
//        		updemo.newName=filenameString;
        		String response=Upload.sendFile(new Config().BASEURI+"/upload.php", "/sdcard/"+faceString,faceString);
        		Log.v("face", faceString);
 /*       		Message msg = myHandler.obtainMessage();
    			Bundle b = new Bundle();
    			b.putString("response", response);
    			msg.setData(b);
    			myHandler.sendMessage(msg);
  */
        		String data_r="Uploaded";
    			int s=response.indexOf(data_r);
    			Log.v("the upload","up.......");
    			//Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
    			if(s!=-1){
    				String status=updemo.postRequest(new Config().BASEURI+"/register_face.php?face="+faceString+"&user_id="+user_id, map2);
        		
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