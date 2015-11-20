package com.example.showapi;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.show.api.ShowApiRequest;

public class MainActivity extends Activity {
	
	String data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final TextView txt = (TextView) this.findViewById(R.id.textView1);
		Button myBtn = (Button) this.findViewById(R.id.button1);
		final AsyncHttpResponseHandler resHandler=new AsyncHttpResponseHandler(){
			public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
				//鍋氫竴浜涘紓甯稿鐞�
				e.printStackTrace();
			}
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					data = new String(responseBody,"utf-8");
					long b=System.currentTimeMillis();
					long a=(Long) txt.getTag();
					System.out.println("response is :"+new String(responseBody,"utf-8"));
					System.out.println("used time is :"+(b-a));
					txt.setText(data+new Date());
					//鍦ㄦ瀵硅繑鍥炲唴瀹瑰仛澶勭悊
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
		}};
		myBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				txt.setTag(System.currentTimeMillis());
				 new ShowApiRequest( "http://route.showapi.com/213-4", "11961", "1a1ee362464b4cd6beb9f69c43787f86")
				 .setResponseHandler(resHandler)
				  //.addTextPara("keyword", "昨夜小楼又东风")
				 .addTextPara("topid" , "5")
				 .post();
			}
		});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	/**
	 * 返回从网络上获取的数据
	 * @return
	 */
	public String getData()
	{
		return data;
	}
}
