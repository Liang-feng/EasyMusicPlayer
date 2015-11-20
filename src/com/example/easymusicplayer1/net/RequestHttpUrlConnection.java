package com.example.easymusicplayer1.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.easymusicplayer1.activity.MusicTopFragment;
import com.example.easymusicplayer1.utility.MyApplication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class RequestHttpUrlConnection {
	
	private static final int CONNECT= 1;      //获取信息成功就标记为1
	
    HttpURLConnection httpUrlConnection;
    
    String address;                            //用来存储从MusicTopFragment传递来的网址
   
    URL url;
    
    String response;
    
    Handler handler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {  //如果想网络请求成功!!!
    		super.handleMessage(msg);
    		
			Log.e("Request", "handleMessage内部!!!");

    		if(msg.what == CONNECT)               
    		{
    	    	response = (String) msg.obj;                                   //这里有错？？？
				Log.e("RequestHttpUrlConnection" , "运行到这里!!!");
				System.out.println("response        : " + response);
				Log.e("Request", response);
    		}
    	}
    	
    	@Override
    	public void dispatchMessage(Message msg) {
    		// TODO Auto-generated method stub
    		super.dispatchMessage(msg);
    	}
	};
    
    public RequestHttpUrlConnection(String address) {           //从MusicTopFragment中传递参数过来！！！
    	
    	this.address = address;
    	response = null;                                       //如果请求失败，则回返回null，有助于调用方进行null判断!!!
	} 
    
    
    /**
     * 向音乐url地址，请求数据
     */
    public void RequestInternetConnection()
    {
    	httpUrlConnection = null;
		Log.e("RequestHttpUrlConnection" , "运行到这里!!!");

        new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Toast.makeText(MyApplication.getContext() , "发送网络请求", Toast.LENGTH_LONG).show();
					url = new URL(address);
					httpUrlConnection = (HttpURLConnection) url.openConnection();
					httpUrlConnection.setRequestMethod("GET");
					httpUrlConnection.setReadTimeout(8000);
					httpUrlConnection.setConnectTimeout(8000);           //设置超过了规定的时间还没有与网络连接
					InputStream in = httpUrlConnection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					
					StringBuilder response = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null)
					{
						response.append(line);
					}
					
					Message msg = new Message();
					msg.what = CONNECT;
					msg.obj = response;
					handler.sendMessage(msg);                  //如果从网上获取消息成功，则返回到MusicTopF中的handler中!!!
					Log.e("RequestHttpUrlConnection" , "运行到这里!!!");

					
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					httpUrlConnection.disconnect();
					Log.e("RequestHttpUrlConnection" , "运行到finally内部!!!");

				}
			}
		}).start();
    	
    	
    }

    
    public String getReponse()         //返回从网络上获取的信息!!!
    {
    	return response;
    }

}
