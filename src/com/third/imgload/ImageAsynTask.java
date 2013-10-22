package com.third.imgload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.third.imgload.AsyncImageLoader.ImageCall_Back;

class ImageAsynTask extends AsyncTask<Void, Void, Drawable>

{

	public String url = "";
	private ImageView view = null;
     private ImageCall_Back Call_back=null;
	public ImageAsynTask(String url, ImageView view,ImageCall_Back Call_back) {
		this.url = url;
		this.view = view;
		this.Call_back=Call_back;

	}

	@Override
	protected Drawable doInBackground(Void... params) {

		return loadImages(this.url);
	}

	public byte[] Bitmap2Bytes(Bitmap bm) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	protected void onPostExecute(Drawable result) {

		super.onPostExecute(result);
		if (result == null) {

			return;
		}
		
		this.Call_back.imageLoad();
		BitmapDrawable bd = (BitmapDrawable) result;

		Bitmap bitmap = bd.getBitmap();

		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inJustDecodeBounds = true;
		options.inSampleSize = ImageSize.computeSampleSize(options, -1,
				bitmap.getWidth() * bitmap.getHeight());
		options.inJustDecodeBounds = false;

		byte[] byBitmap = Bitmap2Bytes(bitmap);

		bitmap = BitmapFactory.decodeByteArray(byBitmap, 0, byBitmap.length,
				options);

		// imageCache.put(this.url, new SoftReference<Bitmap>(bitmap));

		File dir = new File("/mnt/sdcard/hunuoImage/");
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File bitmapFile = new File("/mnt/sdcard/hunuoImage/"
				+ this.url.substring(this.url.lastIndexOf("/") + 1));
		if (!bitmapFile.exists()) {
			try {
				bitmapFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(bitmapFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		view.setBackgroundDrawable(result);

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	public Drawable loadImages(String url) {
		try {
			
		URL ul=	new URL(url);
		if(ul!=null)
		{
			 InputStream  in=	HttpUtils.getStreamFromURL(url);
			if(in!=null)
			{
				
				return Drawable.createFromStream(in,"liao");
			
			}
			else
			{
				return null;
				
			}
		}
					
		
                            
		} catch (IOException e) {
     
			e.printStackTrace();
		}
		return null;
	}
}