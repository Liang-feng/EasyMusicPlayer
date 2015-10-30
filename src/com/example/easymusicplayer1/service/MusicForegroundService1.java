package com.example.easymusicplayer1.service;

import java.util.ArrayList;

import com.example.easymusicplayer1.R;
import com.example.easymusicplayer1.activity.PlayMusicActivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MusicForegroundService1 extends Service {

	Notification notification;
	
	MusicBinder musicBinder;
	
	final static String CLICK_LAST = "com.example.eastmusicplayer1.music_last";
	
	final static String CLICK_NEXT = "com.example.eastmusicplayer1.music_next";
	
	final static String CLICK_PLAY_OR_PAUSE = "com.example.eastmusicplayer1.music_play_or_pause";

	RemoteViews views;
	
	Boolean play = false;          //������ǵ�ǰ��ͼƬ�Ƿ��ǲ���ͼƬ��������ͣͼƬ
	
	Service serviceContext;
	
	ArrayList<String> musicTitleList;
	
	int musicTitlePosition;
	
	
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if(intent.getAction().equals(CLICK_LAST))
			{
				//�������Ѿ���PlayMusicActivity�뱾�����musicTitlePositionͬ���ˣ�����ǰ̨������������ƾͲ������!!!
				musicTitlePosition = PlayMusicActivity.playLastMusic();//������һ�׸�   , ���ҷ��ظ�����λ�ã�����ǰ̨��PlayMusicActivity�Ľ����ϵĸ���������ͬ 
			    views.setTextViewText(R.id.title_text_view , musicTitleList.get(musicTitlePosition));   //�޸��������� , ���ɾ���˸������᲻�����?????
				views.setImageViewResource(R.id.play_and_pause , R.drawable.ic_pause_black_48dp);   //�ò��ź���ͣ����ȷʵ��
				
				play = false;
			}
			else if(intent.getAction().equals(CLICK_NEXT))
			{
				musicTitlePosition = PlayMusicActivity.playNextMusic();   //������һ�׸��������ҷ��ظ�����λ�ã�����ǰ̨��PlayMusicActivity�Ľ����ϵĸ���������ͬ
		    		
				views.setTextViewText(R.id.title_text_view , musicTitleList.get(musicTitlePosition));   //�޸���������
				views.setImageViewResource(R.id.play_and_pause , R.drawable.ic_pause_black_48dp);   //�ò��ź���ͣ����ȷʵ��
				
			    play = false;
			}
			else if(intent.getAction().equals(CLICK_PLAY_OR_PAUSE))        //no bug
			{ 
				PlayMusicActivity.playOrPauseMusic();         //��ͣ���߲��Ÿ���
                
				if(play == true)
				{
	                //PlayMusicActivity.slowDecreaseVolume();         //�ٵ����ͣ��ť��ʱ�򣬻�����������!!!
	                
		    		views.setImageViewResource(R.id.play_and_pause , R.drawable.ic_pause_black_48dp);
		    		play  = false;
		    		
				}
				else if(play == false)
				{
					views.setImageViewResource(R.id.play_and_pause , R.drawable.ic_play_arrow_black_48dp);
				    play = true;
				}
				
		
			}
			
			serviceContext.startForeground(1 , notification);     //����������ǰ̨�����idҪ������onCreate()��������ǰ̨�����idһ�£���Ȼ�����һ��һ����
		}
	};
	
	
	
	public class MusicBinder extends Binder
	{       
		
		//MyMusicFragment , PlayMusicActivity ��  service�Ľ��� , ����ʹ�øտ�ʼǰ̨service�������������û�������ĸ���!!!
		public void changeMusicTitle(ArrayList<String> musicTitleList1 , int musicTitlePosition1)
		{
			musicTitleList = musicTitleList1;              //���ܴ�MyMusicFragment������������
			musicTitlePosition = musicTitlePosition1;
			//Toast.makeText(getApplicationContext(),  "���ݴ��ݳɹ���ô��" + musicTitleList , Toast.LENGTH_SHORT).show();
			
			views.setTextViewText(R.id.title_text_view , musicTitleList.get(musicTitlePosition));
            serviceContext.startForeground(1 , notification);
		}
		
		
		public void changeMusicPlayOrPausePic(Boolean play1)   //��PlayMusicActivity���á� PlayMusicActivity�еĲ���ͼƬ �� ǰ̨���ŵ�ͼƬ ͬ��
		{
			if(play1 == true)
			{
		    	views.setImageViewResource(R.id.play_and_pause , R.drawable.ic_play_arrow_black_48dp);
			}
			else if(play1 == false)
			{
				views.setImageViewResource(R.id.play_and_pause, R.drawable.ic_pause_black_48dp);
			}
			
            serviceContext.startForeground(1 , notification);
            play = play1;           //ͳһ��ǵ�ǰ��ͼƬ�ǲ��Ż�����ͣ

		}
		
		
	}
	
	//no bug
	@Override
	public IBinder onBind(Intent intent) {		//�ƺ�����ͨ�����ݲ�������������static������ʹ�ã����������ʱ��!!!������һ�������ȣ���onBind�Ĳ���intent??????????/
		
		musicBinder = new MusicBinder();
		return musicBinder;
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		

		Log.e("MainActivity" , "�����ڲ�");
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(CLICK_LAST);
		intentFilter.addAction(CLICK_NEXT);
		intentFilter.addAction(CLICK_PLAY_OR_PAUSE);
		this.registerReceiver(receiver, intentFilter);
		
		
		Intent intent = new Intent(this , PlayMusicActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this , 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT);
		
		Intent lastIntent = new Intent(CLICK_LAST);
		Intent nextIntent = new Intent(CLICK_NEXT);
		Intent playOrPauseIntent = new Intent(CLICK_PLAY_OR_PAUSE);
		PendingIntent lastPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, lastIntent, PendingIntent.FLAG_UPDATE_CURRENT);    //flag ����:���PendingIntent�Ѿ����ڣ����������Ǹı���extra������
		PendingIntent nextPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent playOrPausePendingIntet = PendingIntent.getBroadcast(getApplicationContext(), 0, playOrPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	
		
		
		views = new RemoteViews(getPackageName(), R.layout.music_foreground_layout) ;
		
		views.setOnClickPendingIntent(R.id.last, lastPendingIntent);
		views.setOnClickPendingIntent(R.id.next, nextPendingIntent);
		views.setOnClickPendingIntent(R.id.play_and_pause 	, playOrPausePendingIntet);
		
		
		notification = new Notification.Builder(this)
				.setSmallIcon(R.drawable.ic_music_note_black_48dp)
				//.setContent(views)
				.setContentIntent(pendingIntent)
				.build();
		
		notification.bigContentView = views;
		
		serviceContext = this;          //������MusicBinder
		this.startForeground(1 , notification);              //����������ǰ̨�����idҪ������onReceive������������ǰ̨�����idһ�£���Ȼ�����һ��һ����
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(receiver);  //Unregister a previously registered BroadcastReceiver.All filters that have been registered for this BroadcastReceiver will be removed.

	}

}
