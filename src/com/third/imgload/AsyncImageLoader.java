package com.third.imgload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.lang.ref.SoftReference;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.widget.ImageView;
import android.widget.ProgressBar;

public class AsyncImageLoader {
	/**
	 * 内存图片软引用缓冲
	 */
	private HashMap<String, SoftReference<Bitmap>> imageCache = null;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public Thread thread = null;

	public Bitmap loadBitmap(final ImageView imageView, final String imageURL,
			ImageCall_Back Call_back) {
		// 在内存缓存中，则返回Bitmap对象
		Bitmap bit = null;
		if (imageCache.containsKey(imageURL)) {
			SoftReference<Bitmap> reference = imageCache.get(imageURL);
			Bitmap bitmap = reference.get();
			if (bitmap != null) {
				bit = bitmap;
			}
		} else {
			/**
			 * 加上一个对本地缓存的查找
			 */
			String bitmapName = imageURL
					.substring(imageURL.lastIndexOf("/") + 1);
			File cacheDir = new File("/mnt/sdcard/hunuoImage/");
			File[] cacheFiles = cacheDir.listFiles();
			int i = 0;
			if (null != cacheFiles) {

				// begin
				for (; i < cacheFiles.length; i++) {

					if (bitmapName.equals(cacheFiles[i].getName())) {

						break;
					}

				}
				// end

				// begin
				if (i < cacheFiles.length) {
					Bitmap bitmap = BitmapFactory
							.decodeFile("/mnt/sdcard/hunuoImage/" + bitmapName);

					bit = bitmap;
				}

				// end
			}

		}
		if (bit == null) {
                // 在本地找不到图片到网络找图片
			new ImageAsynTask(imageURL, imageView, Call_back).execute();

		}
		return bit;
	}

	public interface ImageCall_Back {
		public void imageLoad();
	}

	public interface ImageNewCallBack {
		public void imageLoad(ImageView imageView, Bitmap bitmap);
	}
}