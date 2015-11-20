package com.example.easymusicplayer1.activity;

import java.util.ArrayList;

import com.example.easymusicplayer1.db.MusicSQLiteOpenHelper;
import com.example.easymusicplayer1.db.ReadMusicInfo;
import com.example.easymusicplayer1.net.DownloadMusicTask;
import com.example.easymusicplayer1.net.RequestHttpUrlConnection;
import com.example.easymusicplayer1.service.MusicForegroundService1;
import com.example.easymusicplayer1.service.MusicForegroundService1.MusicBinder;
import com.example.easymusicplayer1.utility.MyApplication;

import android.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MusicTopFragment extends Fragment {
	
	ListView lv_musicTop;
	
	ListView lv_menu;
	
	ArrayAdapter<String> menuAdapter;
	
	ArrayAdapter<String> adapter;
	
    ArrayList<String> musicNameTopList;           //用于存储音乐标题
    
    ArrayList<String> musicUrlList;               //用于存储音乐的在线播放地址!!!
    
    ArrayList<String> musicDownUrlList;           //用于存储音乐的下载地址!!!
    
    ArrayList<Integer> musicAlbumidList;          //用于存储音乐的专辑id
    
    ArrayList<Integer> musicSecondsList;          //用于存储音乐的总播放时间
    
    ArrayList<Integer> musicSingerIdList;         //用于存储音乐的歌手id
    
    ArrayList<String> musicSingerNameList;        //用于存储音乐歌手名称
    
    ArrayList<Integer> musicSongIdList;           //用于存储音乐的id
    
    ArrayList<String> musicSongNameList;          //用于存储音乐的名称

    MusicSQLiteOpenHelper musicDb;                //用于操作数据库
    
    ReadMusicInfo readMusicInfo;
    
    RequestHttpUrlConnection requestHttpUrlConnection;
    
    String response;
    
    int position1;                                 //当前被点击的item项的位置!!!
    
	AlertDialog.Builder dialog;                    //用于在“音乐馆”点击音乐时出现，与菜单栏相似的dialog！！！
    
    private MusicForegroundService1.MusicBinder musicBinder;
    
    ServiceConnection serviceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			musicBinder = (MusicBinder) service;            // 与service沟通的桥梁
            //第一次进入PlayMusicActivity的界面时候，设置前台服务的音乐名称
			musicBinder.changeMusicTitle(musicNameTopList , position1); // 通过调用MusicBinder的changeMusicTitle函数来对service进行操作!!!
			Log.e("MainActivity" , "绑定服务");			
		}
	};
	
	private static Handler handler = new Handler() {
		
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			String response = (String) msg.obj;
			Log.e("MainActivity" , "response :   " + response);
			super.handleMessage(msg);
		}
	};
	
	
	public static Handler getHandler()
	{
		return handler;
	}
   
    public MusicTopFragment() {
		// TODO Auto-generated constructor stub
    	readMusicInfo = new ReadMusicInfo();
		musicNameTopList = new ArrayList<String>();
		musicUrlList = new ArrayList<String>();
		musicDownUrlList = new ArrayList<String>();
		musicAlbumidList = new ArrayList<Integer>();          
	    musicSecondsList = new ArrayList<Integer>();         
	    musicSingerIdList = new ArrayList<Integer>();          
	    musicSingerNameList = new ArrayList<String>();       
	    musicSongIdList = new ArrayList<Integer>();
	    musicSongNameList = new ArrayList<String>();
	}
    
    
    @Override                //刚开始搞错了，写成了，onCreate函数，怪不得没东西出来，草！！！
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	LinearLayout linearLayout =  initView();            //初始化界面
    	return linearLayout;
    }


	/**
	 * 界面，获取控件
	 */
	private LinearLayout initView() {

		Log.e("MusicTopFragment" , "测试请求的数据先返回，还是这里先执行!!!");
		LinearLayout linearLayout = new LinearLayout(getActivity());           //新建布局
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT);   
		linearLayout.setLayoutParams(layoutParams);               //为布局设置属性

		initMusicNameTopListDate();                               //初始化，ListView将要显示的数据
		
		lv_musicTop = new ListView(getActivity());                //新建listView
		lv_musicTop.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.WRAP_CONTENT));  //为listView设置宽度，高度属性
		adapter = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_list_item_1 , musicNameTopList);
		lv_musicTop.setAdapter(adapter);                          //为listView设置adapter

		
		/**
		 * 监听如果点击了，音乐馆中的音乐，则先检测当前是否为wifi条件下，弹出提示框，
		 */
		lv_musicTop.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				position1 = position;
				initMusicUrl();                      //从数据库中获取对应当前item项音乐的在线播放地址！！！
				initMusicDownUrl();                      //从数据库中获取对应当前item项音乐的下载地址！！
				
				initDialog();                        //当点击“音乐馆”的歌曲时，弹出类似于菜单的自定义dialog!!!
			}

			
			private void initDialog() {           //初始化dialog，对其属性进行相关设置!!!
				
				dialog = new AlertDialog.Builder(getActivity());
				//dialog.setTitle("请选择");
				dialog.setCancelable(true);
				initMenuListView();                      //初始化listVie
				dialog.setView(lv_menu);                 //为dialog设置一个listView
				dialog.show();				
			}

			/**
			 * 
			 */
			private void initMenuListView() {
				
			    String [] menuItemStr = { "在线试听" , "边下边播" }; 
				lv_menu = new ListView(getActivity());
				lv_menu.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.WRAP_CONTENT));
				menuAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1 , menuItemStr);
				lv_menu.setAdapter(menuAdapter);
				
				lv_menu.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						switch(position)
						{
							case 0:
								onlineListener();      //在线试听
								break;
							case 1:
								onlineListenerAndDownload();      //边下边播
								break;
								default:
									break;
						}
					}
				});
				
			}

			
		});
		
		
		linearLayout.addView(lv_musicTop);                        //忘记add进去了。。。。。晕
		

		return linearLayout;
	}

	/**
	 * 从数据库中读取数据
	 */
	private void initMusicNameTopListDate() {

		musicNameTopList = readMusicInfo.getMusicName();          //从数据库中读取音乐名称
		
          
		if(musicNameTopList == null)
		{
			musicNameTopList.add("从网络请求数据来不及响应");
		}
		
    	   //	Log.e("MainActivity" , musicNameTopList.toString());
		/*if(musicNameTopList.isEmpty())                       //如果从数据库中读取，失败，那么就从网络上读取！！！
		{
			
		}*/
	}
	
	
	
	private void initMusicUrl()
	{
		musicUrlList = readMusicInfo.getMusicUrl();
	}
	
	private void initMusicDownUrl()
	{
		musicDownUrlList = readMusicInfo.getMusicDownUrl();
	}
	
	
	private void beginService() {           //启动，绑定服务，以及传递数据到service
		
		Context context = MyApplication.getContext();
		Intent serviceIntent = new Intent(context , MusicForegroundService1.class);

		//serviceIntent.putExtra("music_title" , musicTitleList);                //把歌曲名称列表传递到service，用于前台service中歌曲名称的变化
		//serviceIntent.putExtra("music_title_position" , musicTitlePosition);//歌曲名在ListView的item项上的位置，用于在前台服务中定位输出歌曲名

		context.startService(serviceIntent);          //启动服务
		context.bindService(serviceIntent, serviceConnection , 0);          //绑定服务
	}

	/**
	 * 点击在线播放后，打开PlayMusicActivity，传递有关歌曲名，以及播放地址过去，并绑定前台服务!!!
	 */
	private void onlineListener() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity() , PlayMusicActivity.class);
		intent.putExtra("fragment" , "MusicTopFragment");                   //用于给PlayMusicActivity判断是哪个fragment启动了它
		intent.putExtra("music_name" , musicNameTopList.get(position1));
		intent.putExtra("music_url" , musicUrlList.get(position1));    
		intent.putStringArrayListExtra("music_title_list" , musicNameTopList);
		intent.putStringArrayListExtra("music_url_list", musicUrlList);
		startActivity(intent);
		beginService();
		
	}

	
	/**
	 *  当点击dialog上的“边下边播”的时候, 从网络上下载歌曲
	 */
	private void onlineListenerAndDownload() {
	
		//下载歌曲,传递           下载地址，context，音乐名称          过去
		new DownloadMusicTask(musicDownUrlList.get(position1) , getActivity() , musicNameTopList.get(position1)).execute();
    
		/*	    {
			Intent intent = new Intent(getActivity() , SecondActivity.class);
			intent.putExtra("url" , musicDownUrlList.get(position1));
			startActivity(intent);
		}*/
	}

}
