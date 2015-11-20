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
	
	private static final int CONNECT= 1;      //��ȡ��Ϣ�ɹ��ͱ��Ϊ1
	
    HttpURLConnection httpUrlConnection;
    
    String address;                            //�����洢��MusicTopFragment����������ַ
   
    URL url;
    
    String response;
    
    Handler handler = new Handler() {
    	@Override
    	public void handleMessage(Message msg) {  //�������������ɹ�!!!
    		super.handleMessage(msg);
    		
			Log.e("Request", "handleMessage�ڲ�!!!");

    		if(msg.what == CONNECT)               
    		{
    	    	response = (String) msg.obj;                                   //�����д�����
				Log.e("RequestHttpUrlConnection" , "���е�����!!!");
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
    
    public RequestHttpUrlConnection(String address) {           //��MusicTopFragment�д��ݲ�������������
    	
    	this.address = address;
    	response = null;                                       //�������ʧ�ܣ���ط���null�������ڵ��÷�����null�ж�!!!
	} 
    
    
    /**
     * ������url��ַ����������
     */
    public void RequestInternetConnection()
    {
    	httpUrlConnection = null;
		Log.e("RequestHttpUrlConnection" , "���е�����!!!");

        new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Toast.makeText(MyApplication.getContext() , "������������", Toast.LENGTH_LONG).show();
					url = new URL(address);
					httpUrlConnection = (HttpURLConnection) url.openConnection();
					httpUrlConnection.setRequestMethod("GET");
					httpUrlConnection.setReadTimeout(8000);
					httpUrlConnection.setConnectTimeout(8000);           //���ó����˹涨��ʱ�仹û������������
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
					handler.sendMessage(msg);                  //��������ϻ�ȡ��Ϣ�ɹ����򷵻ص�MusicTopF�е�handler��!!!
					Log.e("RequestHttpUrlConnection" , "���е�����!!!");

					
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					httpUrlConnection.disconnect();
					Log.e("RequestHttpUrlConnection" , "���е�finally�ڲ�!!!");

				}
			}
		}).start();
    	
    	
    }

    
    public String getReponse()         //���ش������ϻ�ȡ����Ϣ!!!
    {
    	return response;
    }

}
