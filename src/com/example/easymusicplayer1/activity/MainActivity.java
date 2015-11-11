package com.example.easymusicplayer1.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;

import com.example.easymusicplayer1.R;
import com.example.easymusicplayer1.model.Music;
import com.example.easymusicplayer1.model.MusicTopFragment;
import com.example.easymusicplayer1.model.MyMusicFragment;
import com.example.easymusicplayer1.utility.DataDipose;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class MainActivity extends Activity{
	
	private Music music = new Music();
	
	private ActionBar actionBar;
	
	private MyMusicFragment myMusicFragment;
	
	private MusicTopFragment myMusicTopFragment;
	
	public static String playOrder = "ORDER_PLAY";            //播放顺序,默认是按顺序播放
	
	String data;
	
	Context mContext;
	
	/**
	 * 调用第三方API函数
	 */
	final AsyncHttpResponseHandler resHandler=new AsyncHttpResponseHandler(){
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
			//鍋氫竴浜涘紓甯稿鐞�
			e.printStackTrace();
		}
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {       //从网络上请求数据成功就调用这个!!!
			try { 
				data = new String(responseBody,"utf-8");
				long b=System.currentTimeMillis();
		    	//long a=(Long) txt.getTag();
				System.out.println("response is :"+new String(responseBody,"utf-8"));
				//System.out.println("used time is :"+(b-a));
				
				DataDipose.dealString(data);      //解析返回来的数据，并且存储到数据库里面!!!

				//鍦ㄦ瀵硅繑鍥炲唴瀹瑰仛澶勭悊
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
	}};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_LEFT_ICON);       //有actionBar，那么这句就不能要
		setContentView(R.layout.activity_main);
		
		mContext = MainActivity.this;
		initActionBar();       //对ActionBar属性进行相关设置，以及增加Tab导航，tab导航的监听事件
		
		/**
		 * 开启子线程，用于从网络获取数据!!!
		 */
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				requestDataFromInternet();            //提前开始进行网络请求，防止反应不过来，arrayList为空,而报错!!!

			}
		}).start();
	}
	
	
	/**
	 * 对ActionBar属性进行相关设置，以及增加Tab导航，tab导航的监听事件
	 */
	private void initActionBar() {
		   actionBar = getActionBar();
	        actionBar.setDisplayShowTitleEnabled(false);         //使App名称不可见,美观
			actionBar.setDisplayShowHomeEnabled(false);          //使actionBar上最左边的图片不见
			
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);       //设置导航模式为：Tab导航

			
			ActionBar.TabListener tabListener = new ActionBar.TabListener() {    //监听Tab的点击情况
				
				@Override
				public void onTabUnselected(Tab tab, FragmentTransaction ft) {
					// TODO Auto-generated method stub
					switch(tab.getPosition())
					{
					case 0:
						ft.detach(myMusicFragment);  //销毁MyMusicFragment上的布局，不然点击另外的tab项，还是会出现“我的音乐”Tab项上的布局!!!
						//ft.remove(myMusicFragment);
						break;
					case 1:
						ft.detach(myMusicTopFragment);
						break;
					}
					
				}
				
				//hava bug
				@Override
				public void onTabSelected(Tab tab, FragmentTransaction ft) {
					// TODO Auto-generated method stub
					switch(tab.getPosition())
					{
					case 0:
						myMusicFragment = new MyMusicFragment();             
						ft.add(android.R.id.content , myMusicFragment);      //这里可能要修改一下！！！       //话说R.id.content是什么???
						//requestDataFromInternet();                //在“我的音乐”tab项时就开始向网络请求数据,防止在点击“音乐馆”Tab项时，反应不过来,从网络上请求数据，如果点击“点击音乐馆tab项”
						break;
					case 1:
						myMusicTopFragment = new MusicTopFragment();
						ft.add(android.R.id.content , myMusicTopFragment);
						
						break;
					case 2:
						break;
					}
					
				}
				
				@Override
				public void onTabReselected(Tab tab, FragmentTransaction ft) {
					// TODO Auto-generated method stub
					
				}
			};

			
			setTab(tabListener);     //设置Tab导航项 ， 以及把Tabadd进ActionBar中！！！
	}

	/**
	 * 调用第三方API函数，从网络上获取音乐数据!!!
	 */
	public void requestDataFromInternet()
	{
		 new com.example.easymusicplayer1.showapi.ShowApiRequest( "http://route.showapi.com/213-4", "11961", "1a1ee362464b4cd6beb9f69c43787f86")
		 .setResponseHandler(resHandler)
		  //.addTextPara("keyword", "昨夜小楼又东风")
		 .addTextPara("topid" , "5")
		 .post();
		 
	}


	//创建action Bar（操作栏）上的菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main , menu);       //在R.menu.main中设置了随机播放，顺序播放等的菜单项！！！
		return true;
	}

	// 点击菜单上的item后就会调用此函数!!!    no bug
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		
		int id = item.getItemId();
		
		if (id == R.id.action_settings)          //单曲循环
		{
			playOrder = "SINGLE_CIRCLE";
			return true;
		}
		else if(id == R.id.play_order_item)        //按循序播放
		{
			playOrder = "ORDER_PLAY";
			return true;
		}
		else if(id == R.id.play_random_item)          //随机播放
		{
			playOrder = "RANDOM_PLAY";
		}
		else if(id == R.id.play_once_item)
		{
			playOrder = "ONCE_PLAY";
		}
		return super.onOptionsItemSelected(item);
	}

	
	//只供内部调用  , 被弃用了该怎么办？？？可能还要修改!!! 
	/**
	 * Tab导航的相关设置，tab导航的事件监听，以及把tab导航添加到ActionBar上
	 * @param tabListener
	 */
	@SuppressWarnings("deprecation")
	private void setTab(ActionBar.TabListener tabListener)     //用于新建Tab导航，以及tab的相关设置!!!
	{
		@SuppressWarnings("deprecation")
		Tab tab1 = actionBar.newTab();
		tab1.setText("我的音乐");
		tab1.setTabListener(tabListener);
		Tab tab2 = actionBar.newTab();
		tab2.setText("音乐馆");
		tab2.setTabListener(tabListener);
		Tab tab3 = actionBar.newTab();
		tab3.setText("待开发");
		tab3.setTabListener(tabListener);
		
		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		actionBar.addTab(tab3);
	}


	@Override
	protected void onDestroy() {              //摧毁活动时，删除数据库，避免数据过多!!!

		this.deleteDatabase("/data/data/com.example.easymusicplayer1/databases/MusicStore.db");
		super.onDestroy();
	}
}
