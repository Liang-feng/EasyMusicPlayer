package com.example.easymusicplayer1.activity;

import java.util.ArrayList;

import com.example.easymusicplayer1.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayMusicActivity extends Activity 
    implements android.view.View.OnClickListener , OnGestureListener

{
	
	TextView musicTitle;
	
	ImageView musicImageView;
	
	Button play;
	
	Button pause;
	
	Button stop;
	
	MediaPlayer mediaPlayer = new MediaPlayer();
	
	Intent intent;
	
	String musicName;
	
	String musicUrl;
	
	int musicDuration;
		
	private GestureDetector gestureDetector;
	
	int musicPosition;           //音乐在ListView的item上的位置，从MainActivity传递过来，方便用于进行顺序播放!!!
	
	private ArrayList<String> musicTitleList;
	
	private ArrayList<String> musicUrlList;
	
	private ArrayList<Integer> musicDurationList;
			
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music);
	   		
		musicTitle = (TextView)findViewById(R.id.music_title_text_view);
	    play = (Button)findViewById(R.id.play_button);
	    pause = (Button)findViewById(R.id.pause_button);
	    stop = (Button)findViewById(R.id.stop_button);

	    play.setOnClickListener(this);
	    pause.setOnClickListener(this);
	    stop.setOnClickListener(this);
	    
	    //获取手势实例
	    gestureDetector = new GestureDetector(PlayMusicActivity.this);   
	    
	    //获取从MainActivity传来的数据
		intent = getIntent();      //一定要在函数里面初始化!!!外面初始化就不一样了，详情见博客!!!

		//从 “启动PlayMusicActivity”的活动  获取传递来的数据
	    Bundle bundle = intent.getExtras();
	    musicName = bundle.getString("music_title");
	    musicUrl = intent.getStringExtra("music_url");
	    musicDuration = intent.getIntExtra("music_duration", 0);
	    musicPosition = intent.getIntExtra("music_title_position" , 0);
	    musicTitleList = intent.getStringArrayListExtra("music_title_list");
	    musicUrlList = intent.getStringArrayListExtra("music_url_list");
	    musicDurationList = intent.getIntegerArrayListExtra("music_duration_list");
	    
	    musicTitle.setText(musicName + "  time: " + musicDuration);
	    
	    
        Toast.makeText(PlayMusicActivity.this , musicUrl , Toast.LENGTH_LONG).show();
	    //准备好播放
        initMediaPlayer();
	    mediaPlayer.start();          //开始播放音乐
	    
	    
	    //监听歌曲是否播放结束
	    mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp) {
				
				String playOrder = MainActivity.playOrder;           //获取播放顺序
				
				try 
				{
					Thread.sleep(2000);             //播放完歌曲后暂停一下
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
				if(playOrder == "SINGLE_CIRCLE")          //如果设置了单曲循环
				{
    				mediaPlayer.start();                    //就直接继续播放了
				}
				else if(playOrder == "ORDER_PLAY")        //如果设置了按顺序播放
				{
					if(musicPosition == musicTitleList.size() - 1)          //如果播放的是最后一首，则重新从第一首歌播放!!!
					{
						musicPosition = -1;
					}
					
					mediaPlayer.reset();             //重置mediaPlayer，因为下面运行出错，没有这行的话，出错了我才想到要重新设置mediaPlayer!!!
					musicUrl = musicUrlList.get(musicPosition+1);
					musicName = musicTitleList.get(musicPosition+1);
					musicDuration = musicDurationList.get(musicPosition+1);
					
					musicPosition = musicPosition + 1;
					
					musicTitle.setText(musicName + "  time: " + musicDuration);

					initMediaPlayer();           //准备播放下一首音乐
					mediaPlayer.start();         //开始播放音乐!!!
		
				}
				else if(playOrder == "RANDOM_PLAY")
				{
					musicPosition =  (int)(Math.random() * (musicTitleList.size()-1));
					
					mediaPlayer.reset();             //重置mediaPlayer，因为下面运行出错，没有这行的话，出错了我才想到要重新设置mediaPlayer!!!
					musicUrl = musicUrlList.get(musicPosition);
					musicName = musicTitleList.get(musicPosition);
					musicDuration = musicDurationList.get(musicPosition);
					
					musicTitle.setText(musicName + "  time: " + musicDuration);

					initMediaPlayer();           //准备播放下一首随机音乐
					mediaPlayer.start();         //开始播放音乐!!!
				}
				else if(playOrder == "ONCE_PLAY")
				{
					//只播放一次，播放完后不进行任何操作!!!
				}

			}
	    });
	}
	
	
	//准备好播放音乐
	public void initMediaPlayer()
	{
		try
		{
			mediaPlayer.setDataSource(musicUrl);    //指定音频文件的路径
			mediaPlayer.prepare();        //让MediaPlayer进入到准备状态
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.play_button:
			if(!mediaPlayer.isPlaying())        //播放音乐
			{
				mediaPlayer.start();
			}
			break;
		case R.id.pause_button:                 //暂停音乐
			if(mediaPlayer.isPlaying())
			{
				mediaPlayer.pause();
			}
			break;
		case R.id.stop_button:                   //停止音乐
			if(mediaPlayer.isPlaying())
			{
				mediaPlayer.reset();
				initMediaPlayer();
			}
			break;
			default:
				break;
		}
	}
	
	
	
	
	//当当前activity被销毁的时候
	@Override
	protected void onDestroy()
	{
		//当活动结束的时候，停止播放音乐，并且释放有关资源
		super.onDestroy();
		if(mediaPlayer != null)               
		{
			mediaPlayer.stop();
			mediaPlayer.release();
		}
	}
	
	@Override
	public void finish()       //当退出activity的时候就调用，或者活动应该关闭的时候也会调用!!!
	{
		super.finish();
		 //设置退出时的动画
		overridePendingTransition(R.animator.from_left_in , R.animator.toward_right_out);   
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
		//当由左往右滑动的时候
        if(e1.getX() - e2.getX() < -120)
        {
	    	this.onBackPressed();       //相当于按下了back键
        }
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return this.gestureDetector.onTouchEvent(event);
	}

}
