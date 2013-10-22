package com.senses.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpTsang {

	public String postRequest(String urlPath,Map<String,String> map) throws Exception
	{
		StringBuilder builder=new StringBuilder(); //拼接字符
		//拿出键值
		if(map!=null && !map.isEmpty())
		{
			for(Map.Entry<String, String> param:map.entrySet())
			{
				builder.append(param.getKey()).append('=').append(URLEncoder.encode(param.getValue(), "utf-8")).append('&');
			}
			builder.deleteCharAt(builder.length()-1);
		}
		//下面的Content-Length: 是这个URL的二进制数据长度
		byte b[]=builder.toString().getBytes();
		URL url=new URL(urlPath);
		HttpURLConnection con=(HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");
		con.setReadTimeout(5*1000);
		con.setDoOutput(true);//打开向外输出
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//内容类型
		con.setRequestProperty("Content-Length",String.valueOf(b.length));//长度
		OutputStream outStream=con.getOutputStream();
		outStream.write(b);//写入数据
		outStream.flush();//刷新内存
		outStream.close();
		//状态码是不成功
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
			
			return lines+"msgsendsucceed";
			//****************
		}
		//return false; 
		String str="nodata";
		return str;
		
	}
}

