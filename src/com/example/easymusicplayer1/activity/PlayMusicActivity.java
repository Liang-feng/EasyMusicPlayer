package com.example.easymusicplayer1.activity;

import java.util.ArrayList;

import com.example.easymusicplayer1.R;
import com.example.easymusicplayer1.service.MusicForegroundService1;
import com.example.easymusicplayer1.service.MusicForegroundService1.MusicBinder;
import com.example.easymusicplayer1.utility.MyApplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayMusicActivity extends Activity implements OnGestureListener

{

	static TextView musicTitle;

	// ImageView musicImageView;

	static ImageView play_pause; // “播放”图片

	ImageView playlist; // 显示播放列表的“图片”

	static Boolean play = false; // 用来记录当前的图片是“播放”图片，还是暂停“图片”

	static MediaPlayer mediaPlayer = null;        

	Intent intent; // 用于获取从MyMusicFragment中的intent

	static String musicName;

	static String musicUrl;

	static int musicDuration;

	private GestureDetector gestureDetector;

	static int musicPosition; // 音乐在ListView的item上的位置，从MainActivity传递过来，方便用于进行顺序播放!!!

	private static ArrayList<String> musicTitleList; // 存储音乐名称

	private static ArrayList<String> musicUrlList; // 存储音乐文件的路径

	private static ArrayList<Integer> musicDurationList; // 存储音乐的播放时长

	String playOrder = MainActivity.playOrder; // 每次从MyMusicFragment选歌时 ， 获取播放顺序
												// , 要不要考虑改一下，改成静态函数，或者new
												// 一个对象来调用函数返回？？

	ActionBar actionBar;

	MusicForegroundService1.MusicBinder musicBinder; // 可以通过musicBinder实例调用service中的函数，对service进行操作

	Intent serviceIntent; // 用于绑定服务

	ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			musicBinder = (MusicBinder) service; // 获取控制service的对象
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music);
		
		Log.e("MainActivity" , "PlayMusicActivity里面");

		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		musicTitle = (TextView) findViewById(R.id.music_title);
		play_pause = (ImageView) findViewById(R.id.music_play);
		playlist = (ImageView) findViewById(R.id.music_playlist);

		
		play_pause.setOnClickListener(new OnClickListener() { // 设置点击图片时候的监听事件
			//检查过了，貌似也没有bug，具体还要测试一下才知道!!!
			@Override
			public void onClick(View v) {
				if (play == true) {
					play_pause.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp); // 点击“播放图片”把图片置换为“暂停”图片
					play = false; // 标记当前的图片为“暂停”图片
					
					if (!mediaPlayer.isPlaying()) {
						mediaPlayer.start();
					}

					musicBinder.changeMusicPlayOrPausePic(play); // 前台播放图片 与
																	// PlayMusicActivity中的播放图片同步
				} else if (play == false) {
					play_pause.setImageResource(R.drawable.ic_play_circle_outline_white_48dp); // 点击“暂停图片”把图片置换为“播放”图片
					play = true; // 标记当前的图片为“播放”图片

					//slowDecreaseVolume();             //在点击播放按钮时，缓慢降低音乐声音!!!

					if (mediaPlayer.isPlaying()) // 如果歌曲正在播放，则暂停播放
					{
						mediaPlayer.pause();
					}

					musicBinder.changeMusicPlayOrPausePic(play); // 前台播放图片 与PlayMusicActivity中的播放图片同步
				}
			}
		});

		
		// 获取手势实例
		gestureDetector = new GestureDetector(PlayMusicActivity.this);

		readDataFromActivity(); // 从 “启动PlayMusicActivity”的活动 获取传递来的数据

		musicTitle.setText(musicName);

		Toast.makeText(PlayMusicActivity.this, musicName, Toast.LENGTH_LONG).show();

		initMediaPlayer(); // 准备好播放
		mediaPlayer.start(); // 开始播放音乐

		
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() { // 监听歌曲是否播放结束
			//已经检查过一次，貌似没有bug了，具体的还要测试一下才知道!!!
			@Override
			public void onCompletion(MediaPlayer mp) {

				try {
					Thread.sleep(1000); // 播放完歌曲后暂停一下
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (playOrder == "SINGLE_CIRCLE") // 如果设置了单曲循环 
				{
					mediaPlayer.start(); // 就直接继续播放了
				} else if (playOrder == "ORDER_PLAY") // 如果从一首不能播放的跳到，一个不能播放的音乐文件，相当于，播放完毕，所以前台服务，与PlayMusicActivity会同步变化歌曲名称!!!
				{
					if (musicPosition == musicTitleList.size() - 1) // 如果播放的是最后一首，则重新从第一首歌播放!!!
					{
						musicPosition = -1;
					}

					mediaPlayer.reset(); // 重置mediaPlayer，因为下面运行出错，没有这行的话，出错了我才想到要重新设置mediaPlayer!!!
					//mediaPlayer.release();         //释放资源
					musicUrl = musicUrlList.get(musicPosition + 1);
					musicName = musicTitleList.get(musicPosition + 1);
					musicDuration = musicDurationList.get(musicPosition + 1);

					musicPosition = musicPosition + 1;

					musicTitle.setText(musicName);

					initMediaPlayer(); // 准备播放下一首音乐
					mediaPlayer.start(); // 开始播放音乐!!!

					// 传递参数，是为了更新musicPosition的位置，musicTitleList貌似多余了，MyMusicFragment调用的话就不多余
					musicBinder.changeMusicTitle(musicTitleList, musicPosition); // 按顺序播放到下一首歌曲就修改前台服务的音乐名称!!!

				} else if (playOrder == "RANDOM_PLAY") {
					musicPosition = (int) (Math.random() * (musicTitleList.size() - 1));

					mediaPlayer.reset(); // 重置mediaPlayer，因为下面运行出错，没有这行的话，出错了我才想到要重新设置mediaPlayer!!!
					//mediaPlayer.release();         //释放资源        也释放掉了mediaPlayer实例，所以会报错!!!

					musicUrl = musicUrlList.get(musicPosition);
					musicName = musicTitleList.get(musicPosition);
					musicDuration = musicDurationList.get(musicPosition);

					musicTitle.setText(musicName + "  time: " + musicDuration);

					initMediaPlayer(); // 准备播放下一首随机音乐
					mediaPlayer.start(); // 开始播放音乐!!!

					musicBinder.changeMusicTitle(musicTitleList, musicPosition); // 按随机播放到另一首歌曲就修改前台服务的音乐名称!!!

				} else if (playOrder == "ONCE_PLAY") {
					// 只播放一次，播放完后不进行任何操作!!!
					play = true;
					play_pause.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
					musicBinder.changeMusicPlayOrPausePic(play);
				}

			}
		});

		// 与MusicForegroundService1绑定 ，为了可以在歌曲按顺序播放，或者是循环播放，或者是随机播放时，可以改变前台服务的歌曲名称
		serviceIntent = new Intent(PlayMusicActivity.this, MusicForegroundService1.class);
		bindService(serviceIntent, serviceConnection, 0);
	}
	
	

	 public static  int playLastMusic()
	 {
		if (musicPosition - 1 <= 0) // 如果目前在播放第一首歌，点击上一首的话，那么就播放最后一首
		{
			musicPosition = musicTitleList.size();
		}

		mediaPlayer.reset();                   // 重置mediaPlayer，因为下面运行出错，没有这行的话，出错了我才想到要重新设置mediaPlayer!!!
		musicUrl = musicUrlList.get(musicPosition - 1);
		musicName = musicTitleList.get(musicPosition - 1);
		musicDuration = musicDurationList.get(musicPosition - 1);

		musicPosition -= 1;

		musicTitle.setText(musicName);

		initMediaPlayer(); // 准备播放下一首音乐
		mediaPlayer.start(); // 开始播放音乐!!!

		// 设置好播放的图片，如果原来为播放图片 ，那么如果不设置下面的话，就会出现图片混乱，播放和暂停功能无法正确实现
		play = false;
		play_pause.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);

		return musicPosition;         //返回musicPosition给前台服务，用来改变当前的音乐名称
	}
	
	
	public static int playNextMusic() // 播放下一首歌
	{
		if (musicPosition == musicTitleList.size() - 1) // 如果目前在播放第一首歌，点击上一首的话，那么就播放最后一首
		{
			musicPosition = -1;
		}

		mediaPlayer.reset(); // 重置mediaPlayer，因为下面运行出错，没有这行的话，出错了我才想到要重新设置mediaPlayer!!!
		musicUrl = musicUrlList.get(musicPosition + 1);
		musicName = musicTitleList.get(musicPosition + 1);
		musicDuration = musicDurationList.get(musicPosition + 1);

		musicPosition += 1;
		musicTitle.setText(musicName);

		initMediaPlayer(); // 准备播放下一首音乐
		mediaPlayer.start(); // 开始播放音乐!!!

		// 设置好播放的图片，如果原来为播放图片 ，那么如果不设置下面的话，就会出现图片混乱，播放和暂停功能无法正确实现
		play = false;
		play_pause.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);

		return musicPosition;         //返回musicPosition给前台服务，用来改变当前的音乐名称
	}

	static public void playOrPauseMusic() // 控制暂停或者播放音乐
	{
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			play_pause.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
			play = true;
		} else if (!mediaPlayer.isPlaying()) {
			mediaPlayer.start();
			play_pause.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);
			play = false;
		}
	}

	// no bug
	public void readDataFromActivity() // 从启动PlayMusicActivity的Activity中读取数据
	{
		// 获取从MainActivity传来的数据
		intent = getIntent(); // 一定要在函数里面初始化!!!外面初始化就不一样了，详情见博客!!!
		// 从 “启动PlayMusicActivity”的活动 获取传递来的数据
		Bundle bundle = intent.getExtras();
		musicName = bundle.getString("music_title");
		musicUrl = intent.getStringExtra("music_url");
		musicDuration = intent.getIntExtra("music_duration", 0);
		musicPosition = intent.getIntExtra("music_title_position", 0);
		musicTitleList = intent.getStringArrayListExtra("music_title_list");
		musicUrlList = intent.getStringArrayListExtra("music_url_list");
		musicDurationList = intent.getIntegerArrayListExtra("music_duration_list");

	}

	
	// no bug 准备好播放音乐
	public static void initMediaPlayer() {
		try {
		    if(mediaPlayer == null)
			{
				mediaPlayer = new MediaPlayer();
			}
			mediaPlayer.setDataSource(musicUrl); // 指定音频文件的路径
			mediaPlayer.prepare(); // 让MediaPlayer进入到准备状态
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// 当 当前activity被销毁的时候   no bug
	@Override
	protected void onDestroy() {
		// 当活动结束的时候，停止播放音乐，并且释放有关资源
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.stop();           // 停止播放
			//当Mediaplayer对象不再被使用时，最好调用release（）方法对其进行释放，使其处于结束状态，此时它不能被使用
			mediaPlayer.release();//释放与mediaPlayer相关的资源 ， 释放后一定要设置为null// Set the MediaPlayer to null to avoid IlLegalStateException 
		    mediaPlayer = null;
		}
		
		unbindService(serviceConnection);   //解绑service
		stopService(serviceIntent);         //停止服务
	}

	
	@Override
	public void finish() // 当退出activity的时候就调用，或者活动应该关闭的时候也会调用!!!
	{
		super.finish();
		// 设置退出时的动画
		overridePendingTransition(R.animator.from_left_in, R.animator.toward_right_out);
	}

	
	public static void slowDecreaseVolume()           //在按下暂停按钮或者退出音乐界面时,慢慢降低声音!!!
	{
		new Thread(new Runnable() {            //开启子线程，用来在退出PlayMusicActivity时，慢慢降低音乐声音
			 
			@Override
			public void run() {
				
				for(float i = 0.1f; i<=1.0; i += 0.1)
				{
	    			mediaPlayer.setVolume(1-i , 1-i);        //设置音量，左声道和右声道
	    			try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		try {                    //给声音足够的时间慢慢降低!!! 运行这么多时间才执行下面的onBack()
			Thread.sleep(150);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// 当由左往右滑动的时候
		if (e1.getX() - e2.getX() < -120) {
			
			slowDecreaseVolume();      //慢慢降低声音 ， 在滑动退出的时候         //在点击播放按钮的话，没这个效果，需要查一下当前的音量是什么，有时间再做吧!!!!
			
			this.onBackPressed(); // 相当于按下了back键
	
			Intent intent = new Intent(PlayMusicActivity.this, MainActivity.class);
			startActivity(intent);
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.gestureDetector.onTouchEvent(event);
	}

}