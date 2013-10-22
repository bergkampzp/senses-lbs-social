package com.example.lokal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.senses.config.Config;

import android.util.Log;
import android.widget.Toast;

class Upload extends Thread {
	public String newName;
	public String uploadFile;
//	public String actionUrl = "http://10.254.8.113/lokal/upload.php";
	public String actionUrl;//使用GET传递图片信息到数据库
	public Map<String,String> map=new HashMap<String,String>();
	public void run(){
		
		
		try {
			String response=sendFile(actionUrl, uploadFile, newName);
		//	Toast.makeText(new Camera(), response, Toast.LENGTH_LONG).show();
			Log.v("img upload", response);
			
			String data_r="Uploaded";
			int s=response.indexOf(data_r);
			//Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
			if(s!=-1){
				postRequest(new Config().BASEURI+"/msgpost.php", map);
			}else{
				//Toast.makeText(getApplicationContext(), "Failed!Please check your network", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}
	
	
	public static String sendFile(String urlPath, String filePath,
			String newName) throws Exception {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		URL url = new URL(urlPath);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		/* ����Input��Output����ʹ��Cache */
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		/* ���ô��͵�method=POST */
		con.setRequestMethod("POST");
		/* setRequestProperty */

		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		con.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ boundary);
		/* ����DataOutputStream */
		DataOutputStream ds = new DataOutputStream(con.getOutputStream());
		ds.writeBytes(twoHyphens + boundary + end);
		ds.writeBytes("Content-Disposition: form-data; "
				+ "name=\"file\";filename=\"" + newName + "\"" + end);
		ds.writeBytes(end);

		/* ȡ���ļ���FileInputStream */
		FileInputStream fStream = new FileInputStream(filePath);
		/* ����ÿ��д��1024bytes */
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int length = -1;
		/* ���ļ���ȡ����������� */
		while ((length = fStream.read(buffer)) != -1) {
			/* ������д��DataOutputStream�� */
			ds.write(buffer, 0, length);
		}
		ds.writeBytes(end);
		ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

		/* close streams */
		fStream.close();
		ds.flush();

		/* ȡ��Response���� */
		InputStream is = con.getInputStream();
		int ch;
		StringBuffer b = new StringBuffer();
		while ((ch = is.read()) != -1) {
			b.append((char) ch);
		}
		/* �ر�DataOutputStream */
		ds.close();
		return b.toString();
	}
	
	
	public static String postRequest(String urlPath,Map<String,String> map) throws Exception
	{
		StringBuilder builder=new StringBuilder(); //ƴ���ַ�
		//�ó���ֵ
		if(map!=null && !map.isEmpty())
		{
			for(Map.Entry<String, String> param:map.entrySet())
			{
				builder.append(param.getKey()).append('=').append(URLEncoder.encode(param.getValue(), "utf-8")).append('&');
			}
			builder.deleteCharAt(builder.length()-1);
		}
		//�����Content-Length: �����URL�Ķ�������ݳ���
		byte b[]=builder.toString().getBytes();
		URL url=new URL(urlPath);
		HttpURLConnection con=(HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setReadTimeout(5*1000);
		con.setDoOutput(true);//���������
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//��������
		con.setRequestProperty("Content-Length",String.valueOf(b.length));//����
		OutputStream outStream=con.getOutputStream();
		outStream.write(b);//д�����
		outStream.flush();//ˢ���ڴ�
		outStream.close();
		//״̬���ǲ��ɹ�
		if(con.getResponseCode()==200)
		{
			
			//
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
			String lines;
			lines = reader.readLine();
			String str=con.getInputStream().toString();
//	        while ((lines = reader.readLine()) != null){
//	        	//lines = new String(lines.getBytes(), "utf-8");
//	            //System.out.println(lines);
//	        }
			
			//****************
			
			return lines;
			//****************
		}
		//return false; 
		String str="nodata";
		return str;
		
	}
	
	}