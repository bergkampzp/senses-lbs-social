package com.senses.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.lokal.Localmsg;

class LoadImg_NULL extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>>{

    @Override
    protected HashMap<String, Object> doInBackground(HashMap<String, Object>... hm) {

        InputStream iStream=null;
        String imgUrl = (String) hm[0].get("image");
        int position = (Integer) hm[0].get("position");

        URL url;
        try {
            url = new URL(imgUrl);

            // Creating an http connection to communicate with url
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            // Getting Caching directory
            File cacheDirectory = new Localmsg().getCacheDir();

            // Temporary file to store the downloaded image
            File tmpFile = new File(cacheDirectory.getPath() + "/wpta_"+position+".png");

            // The FileOutputStream to the temporary file
            FileOutputStream fOutStream = new FileOutputStream(tmpFile);

            // Creating a bitmap from the downloaded inputstream
            Bitmap b = BitmapFactory.decodeStream(iStream);

            // Writing the bitmap to the temporary file as png file
            b.compress(Bitmap.CompressFormat.PNG,100, fOutStream);

            // Flush the FileOutputStream
            fOutStream.flush();

           //Close the FileOutputStream
           fOutStream.close();

            // Create a hashmap object to store image path and its position in the listview
            HashMap<String, Object> hmBitmap = new HashMap<String, Object>();
            // Returning the HashMap object containing the image path and position
            return hmBitmap;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}