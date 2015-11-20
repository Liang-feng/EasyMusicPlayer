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
	
    ArrayList<String> musicNameTopList;           //���ڴ洢���ֱ���
    
    ArrayList<String> musicUrlList;               //���ڴ洢���ֵ����߲��ŵ�ַ!!!
    
    ArrayList<String> musicDownUrlList;           //���ڴ洢���ֵ����ص�ַ!!!
    
    ArrayList<Integer> musicAlbumidList;          //���ڴ洢���ֵ�ר��id
    
    ArrayList<Integer> musicSecondsList;          //���ڴ洢���ֵ��ܲ���ʱ��
    
    ArrayList<Integer> musicSingerIdList;         //���ڴ洢���ֵĸ���id
    
    ArrayList<String> musicSingerNameList;        //���ڴ洢���ָ�������
    
    ArrayList<Integer> musicSongIdList;           //���ڴ洢���ֵ�id
    
    ArrayList<String> musicSongNameList;          //���ڴ洢���ֵ�����

    MusicSQLiteOpenHelper musicDb;                //���ڲ������ݿ�
    
    ReadMusicInfo readMusicInfo;
    
    RequestHttpUrlConnection requestHttpUrlConnection;
    
    String response;
    
    int position1;                                 //��ǰ�������item���λ��!!!
    
	AlertDialog.Builder dialog;                    //�����ڡ����ֹݡ��������ʱ���֣���˵������Ƶ�dialog������
    
    private MusicForegroundService1.MusicBinder musicBinder;
    
    ServiceConnection serviceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			musicBinder = (MusicBinder) service;            // ��service��ͨ������
            //��һ�ν���PlayMusicActivity�Ľ���ʱ������ǰ̨�������������
			musicBinder.changeMusicTitle(musicNameTopList , position1); // ͨ������MusicBinder��changeMusicTitle��������service���в���!!!
			Log.e("MainActivity" , "�󶨷���");			
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
    
    
    @Override                //�տ�ʼ����ˣ�д���ˣ�onCreate�������ֲ���û�����������ݣ�����
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	LinearLayout linearLayout =  initView();            //��ʼ������
    	return linearLayout;
    }


	/**
	 * ���棬��ȡ�ؼ�
	 */
	private LinearLayout initView() {

		Log.e("MusicTopFragment" , "��������������ȷ��أ�����������ִ��!!!");
		LinearLayout linearLayout = new LinearLayout(getActivity());           //�½�����
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT);   
		linearLayout.setLayoutParams(layoutParams);               //Ϊ������������

		initMusicNameTopListDate();                               //��ʼ����ListView��Ҫ��ʾ������
		
		lv_musicTop = new ListView(getActivity());                //�½�listView
		lv_musicTop.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.WRAP_CONTENT));  //ΪlistView���ÿ�ȣ��߶�����
		adapter = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_list_item_1 , musicNameTopList);
		lv_musicTop.setAdapter(adapter);                          //ΪlistView����adapter

		
		/**
		 * �����������ˣ����ֹ��е����֣����ȼ�⵱ǰ�Ƿ�Ϊwifi�����£�������ʾ��
		 */
		lv_musicTop.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				
				position1 = position;
				initMusicUrl();                      //�����ݿ��л�ȡ��Ӧ��ǰitem�����ֵ����߲��ŵ�ַ������
				initMusicDownUrl();                      //�����ݿ��л�ȡ��Ӧ��ǰitem�����ֵ����ص�ַ����
				
				initDialog();                        //����������ֹݡ��ĸ���ʱ�����������ڲ˵����Զ���dialog!!!
			}

			
			private void initDialog() {           //��ʼ��dialog���������Խ����������!!!
				
				dialog = new AlertDialog.Builder(getActivity());
				//dialog.setTitle("��ѡ��");
				dialog.setCancelable(true);
				initMenuListView();                      //��ʼ��listVie
				dialog.setView(lv_menu);                 //Ϊdialog����һ��listView
				dialog.show();				
			}

			/**
			 * 
			 */
			private void initMenuListView() {
				
			    String [] menuItemStr = { "��������" , "���±߲�" }; 
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
								onlineListener();      //��������
								break;
							case 1:
								onlineListenerAndDownload();      //���±߲�
								break;
								default:
									break;
						}
					}
				});
				
			}

			
		});
		
		
		linearLayout.addView(lv_musicTop);                        //����add��ȥ�ˡ�����������
		

		return linearLayout;
	}

	/**
	 * �����ݿ��ж�ȡ����
	 */
	private void initMusicNameTopListDate() {

		musicNameTopList = readMusicInfo.getMusicName();          //�����ݿ��ж�ȡ��������
		
          
		if(musicNameTopList == null)
		{
			musicNameTopList.add("����������������������Ӧ");
		}
		
    	   //	Log.e("MainActivity" , musicNameTopList.toString());
		/*if(musicNameTopList.isEmpty())                       //��������ݿ��ж�ȡ��ʧ�ܣ���ô�ʹ������϶�ȡ������
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
	
	
	private void beginService() {           //�������󶨷����Լ��������ݵ�service
		
		Context context = MyApplication.getContext();
		Intent serviceIntent = new Intent(context , MusicForegroundService1.class);

		//serviceIntent.putExtra("music_title" , musicTitleList);                //�Ѹ��������б��ݵ�service������ǰ̨service�и������Ƶı仯
		//serviceIntent.putExtra("music_title_position" , musicTitlePosition);//��������ListView��item���ϵ�λ�ã�������ǰ̨�����ж�λ���������

		context.startService(serviceIntent);          //��������
		context.bindService(serviceIntent, serviceConnection , 0);          //�󶨷���
	}

	/**
	 * ������߲��ź󣬴�PlayMusicActivity�������йظ��������Լ����ŵ�ַ��ȥ������ǰ̨����!!!
	 */
	private void onlineListener() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity() , PlayMusicActivity.class);
		intent.putExtra("fragment" , "MusicTopFragment");                   //���ڸ�PlayMusicActivity�ж����ĸ�fragment��������
		intent.putExtra("music_name" , musicNameTopList.get(position1));
		intent.putExtra("music_url" , musicUrlList.get(position1));    
		intent.putStringArrayListExtra("music_title_list" , musicNameTopList);
		intent.putStringArrayListExtra("music_url_list", musicUrlList);
		startActivity(intent);
		beginService();
		
	}

	
	/**
	 *  �����dialog�ϵġ����±߲�����ʱ��, �����������ظ���
	 */
	private void onlineListenerAndDownload() {
	
		//���ظ���,����           ���ص�ַ��context����������          ��ȥ
		new DownloadMusicTask(musicDownUrlList.get(position1) , getActivity() , musicNameTopList.get(position1)).execute();
    
		/*	    {
			Intent intent = new Intent(getActivity() , SecondActivity.class);
			intent.putExtra("url" , musicDownUrlList.get(position1));
			startActivity(intent);
		}*/
	}

}
