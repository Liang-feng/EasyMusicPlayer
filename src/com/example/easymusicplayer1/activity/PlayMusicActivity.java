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

	static ImageView play_pause; // �����š�ͼƬ

	ImageView playlist; // ��ʾ�����б�ġ�ͼƬ��

	static Boolean play = false; // ������¼��ǰ��ͼƬ�ǡ����š�ͼƬ��������ͣ��ͼƬ��

	static MediaPlayer mediaPlayer = null;        

	Intent intent; // ���ڻ�ȡ��MyMusicFragment�е�intent

	static String musicName;

	static String musicUrl;

	static int musicDuration;

	private GestureDetector gestureDetector;

	static int musicPosition; // ������ListView��item�ϵ�λ�ã���MainActivity���ݹ������������ڽ���˳�򲥷�!!!

	private static ArrayList<String> musicTitleList; // �洢��������

	private static ArrayList<String> musicUrlList; // �洢�����ļ���·��

	private static ArrayList<Integer> musicDurationList; // �洢���ֵĲ���ʱ��

	String playOrder = MainActivity.playOrder; // ÿ�δ�MyMusicFragmentѡ��ʱ �� ��ȡ����˳��
												// , Ҫ��Ҫ���Ǹ�һ�£��ĳɾ�̬����������new
												// һ�����������ú������أ���

	ActionBar actionBar;

	MusicForegroundService1.MusicBinder musicBinder; // ����ͨ��musicBinderʵ������service�еĺ�������service���в���

	Intent serviceIntent; // ���ڰ󶨷���

	ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			musicBinder = (MusicBinder) service; // ��ȡ����service�Ķ���
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music);
		
		Log.e("MainActivity" , "PlayMusicActivity����");

		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		musicTitle = (TextView) findViewById(R.id.music_title);
		play_pause = (ImageView) findViewById(R.id.music_play);
		playlist = (ImageView) findViewById(R.id.music_playlist);

		
		play_pause.setOnClickListener(new OnClickListener() { // ���õ��ͼƬʱ��ļ����¼�
			//�����ˣ�ò��Ҳû��bug�����廹Ҫ����һ�²�֪��!!!
			@Override
			public void onClick(View v) {
				if (play == true) {
					play_pause.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp); // ���������ͼƬ����ͼƬ�û�Ϊ����ͣ��ͼƬ
					play = false; // ��ǵ�ǰ��ͼƬΪ����ͣ��ͼƬ
					
					if (!mediaPlayer.isPlaying()) {
						mediaPlayer.start();
					}

					musicBinder.changeMusicPlayOrPausePic(play); // ǰ̨����ͼƬ ��
																	// PlayMusicActivity�еĲ���ͼƬͬ��
				} else if (play == false) {
					play_pause.setImageResource(R.drawable.ic_play_circle_outline_white_48dp); // �������ͣͼƬ����ͼƬ�û�Ϊ�����š�ͼƬ
					play = true; // ��ǵ�ǰ��ͼƬΪ�����š�ͼƬ

					//slowDecreaseVolume();             //�ڵ�����Ű�ťʱ������������������!!!

					if (mediaPlayer.isPlaying()) // ����������ڲ��ţ�����ͣ����
					{
						mediaPlayer.pause();
					}

					musicBinder.changeMusicPlayOrPausePic(play); // ǰ̨����ͼƬ ��PlayMusicActivity�еĲ���ͼƬͬ��
				}
			}
		});

		
		// ��ȡ����ʵ��
		gestureDetector = new GestureDetector(PlayMusicActivity.this);

		readDataFromActivity(); // �� ������PlayMusicActivity���Ļ ��ȡ������������

		musicTitle.setText(musicName);

		Toast.makeText(PlayMusicActivity.this, musicName, Toast.LENGTH_LONG).show();

		initMediaPlayer(); // ׼���ò���
		mediaPlayer.start(); // ��ʼ��������

		
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() { // ���������Ƿ񲥷Ž���
			//�Ѿ�����һ�Σ�ò��û��bug�ˣ�����Ļ�Ҫ����һ�²�֪��!!!
			@Override
			public void onCompletion(MediaPlayer mp) {

				try {
					Thread.sleep(1000); // �������������ͣһ��
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (playOrder == "SINGLE_CIRCLE") // ��������˵���ѭ�� 
				{
					mediaPlayer.start(); // ��ֱ�Ӽ���������
				} else if (playOrder == "ORDER_PLAY") // �����һ�ײ��ܲ��ŵ�������һ�����ܲ��ŵ������ļ����൱�ڣ�������ϣ�����ǰ̨������PlayMusicActivity��ͬ���仯��������!!!
				{
					if (musicPosition == musicTitleList.size() - 1) // ������ŵ������һ�ף������´ӵ�һ�׸貥��!!!
					{
						musicPosition = -1;
					}

					mediaPlayer.reset(); // ����mediaPlayer����Ϊ�������г���û�����еĻ����������Ҳ��뵽Ҫ��������mediaPlayer!!!
					//mediaPlayer.release();         //�ͷ���Դ
					musicUrl = musicUrlList.get(musicPosition + 1);
					musicName = musicTitleList.get(musicPosition + 1);
					musicDuration = musicDurationList.get(musicPosition + 1);

					musicPosition = musicPosition + 1;

					musicTitle.setText(musicName);

					initMediaPlayer(); // ׼��������һ������
					mediaPlayer.start(); // ��ʼ��������!!!

					// ���ݲ�������Ϊ�˸���musicPosition��λ�ã�musicTitleListò�ƶ����ˣ�MyMusicFragment���õĻ��Ͳ�����
					musicBinder.changeMusicTitle(musicTitleList, musicPosition); // ��˳�򲥷ŵ���һ�׸������޸�ǰ̨�������������!!!

				} else if (playOrder == "RANDOM_PLAY") {
					musicPosition = (int) (Math.random() * (musicTitleList.size() - 1));

					mediaPlayer.reset(); // ����mediaPlayer����Ϊ�������г���û�����еĻ����������Ҳ��뵽Ҫ��������mediaPlayer!!!
					//mediaPlayer.release();         //�ͷ���Դ        Ҳ�ͷŵ���mediaPlayerʵ�������Իᱨ��!!!

					musicUrl = musicUrlList.get(musicPosition);
					musicName = musicTitleList.get(musicPosition);
					musicDuration = musicDurationList.get(musicPosition);

					musicTitle.setText(musicName + "  time: " + musicDuration);

					initMediaPlayer(); // ׼��������һ���������
					mediaPlayer.start(); // ��ʼ��������!!!

					musicBinder.changeMusicTitle(musicTitleList, musicPosition); // ��������ŵ���һ�׸������޸�ǰ̨�������������!!!

				} else if (playOrder == "ONCE_PLAY") {
					// ֻ����һ�Σ�������󲻽����κβ���!!!
					play = true;
					play_pause.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);
					musicBinder.changeMusicPlayOrPausePic(play);
				}

			}
		});

		// ��MusicForegroundService1�� ��Ϊ�˿����ڸ�����˳�򲥷ţ�������ѭ�����ţ��������������ʱ�����Ըı�ǰ̨����ĸ�������
		serviceIntent = new Intent(PlayMusicActivity.this, MusicForegroundService1.class);
		bindService(serviceIntent, serviceConnection, 0);
	}
	
	

	 public static  int playLastMusic()
	 {
		if (musicPosition - 1 <= 0) // ���Ŀǰ�ڲ��ŵ�һ�׸裬�����һ�׵Ļ�����ô�Ͳ������һ��
		{
			musicPosition = musicTitleList.size();
		}

		mediaPlayer.reset();                   // ����mediaPlayer����Ϊ�������г���û�����еĻ����������Ҳ��뵽Ҫ��������mediaPlayer!!!
		musicUrl = musicUrlList.get(musicPosition - 1);
		musicName = musicTitleList.get(musicPosition - 1);
		musicDuration = musicDurationList.get(musicPosition - 1);

		musicPosition -= 1;

		musicTitle.setText(musicName);

		initMediaPlayer(); // ׼��������һ������
		mediaPlayer.start(); // ��ʼ��������!!!

		// ���úò��ŵ�ͼƬ�����ԭ��Ϊ����ͼƬ ����ô�������������Ļ����ͻ����ͼƬ���ң����ź���ͣ�����޷���ȷʵ��
		play = false;
		play_pause.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);

		return musicPosition;         //����musicPosition��ǰ̨���������ı䵱ǰ����������
	}
	
	
	public static int playNextMusic() // ������һ�׸�
	{
		if (musicPosition == musicTitleList.size() - 1) // ���Ŀǰ�ڲ��ŵ�һ�׸裬�����һ�׵Ļ�����ô�Ͳ������һ��
		{
			musicPosition = -1;
		}

		mediaPlayer.reset(); // ����mediaPlayer����Ϊ�������г���û�����еĻ����������Ҳ��뵽Ҫ��������mediaPlayer!!!
		musicUrl = musicUrlList.get(musicPosition + 1);
		musicName = musicTitleList.get(musicPosition + 1);
		musicDuration = musicDurationList.get(musicPosition + 1);

		musicPosition += 1;
		musicTitle.setText(musicName);

		initMediaPlayer(); // ׼��������һ������
		mediaPlayer.start(); // ��ʼ��������!!!

		// ���úò��ŵ�ͼƬ�����ԭ��Ϊ����ͼƬ ����ô�������������Ļ����ͻ����ͼƬ���ң����ź���ͣ�����޷���ȷʵ��
		play = false;
		play_pause.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);

		return musicPosition;         //����musicPosition��ǰ̨���������ı䵱ǰ����������
	}

	static public void playOrPauseMusic() // ������ͣ���߲�������
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
	public void readDataFromActivity() // ������PlayMusicActivity��Activity�ж�ȡ����
	{
		// ��ȡ��MainActivity����������
		intent = getIntent(); // һ��Ҫ�ں��������ʼ��!!!�����ʼ���Ͳ�һ���ˣ����������!!!
		// �� ������PlayMusicActivity���Ļ ��ȡ������������
		Bundle bundle = intent.getExtras();
		musicName = bundle.getString("music_title");
		musicUrl = intent.getStringExtra("music_url");
		musicDuration = intent.getIntExtra("music_duration", 0);
		musicPosition = intent.getIntExtra("music_title_position", 0);
		musicTitleList = intent.getStringArrayListExtra("music_title_list");
		musicUrlList = intent.getStringArrayListExtra("music_url_list");
		musicDurationList = intent.getIntegerArrayListExtra("music_duration_list");

	}

	
	// no bug ׼���ò�������
	public static void initMediaPlayer() {
		try {
		    if(mediaPlayer == null)
			{
				mediaPlayer = new MediaPlayer();
			}
			mediaPlayer.setDataSource(musicUrl); // ָ����Ƶ�ļ���·��
			mediaPlayer.prepare(); // ��MediaPlayer���뵽׼��״̬
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// �� ��ǰactivity�����ٵ�ʱ��   no bug
	@Override
	protected void onDestroy() {
		// ���������ʱ��ֹͣ�������֣������ͷ��й���Դ
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.stop();           // ֹͣ����
			//��Mediaplayer�����ٱ�ʹ��ʱ����õ���release����������������ͷţ�ʹ�䴦�ڽ���״̬����ʱ�����ܱ�ʹ��
			mediaPlayer.release();//�ͷ���mediaPlayer��ص���Դ �� �ͷź�һ��Ҫ����Ϊnull// Set the MediaPlayer to null to avoid IlLegalStateException 
		    mediaPlayer = null;
		}
		
		unbindService(serviceConnection);   //���service
		stopService(serviceIntent);         //ֹͣ����
	}

	
	@Override
	public void finish() // ���˳�activity��ʱ��͵��ã����߻Ӧ�ùرյ�ʱ��Ҳ�����!!!
	{
		super.finish();
		// �����˳�ʱ�Ķ���
		overridePendingTransition(R.animator.from_left_in, R.animator.toward_right_out);
	}

	
	public static void slowDecreaseVolume()           //�ڰ�����ͣ��ť�����˳����ֽ���ʱ,������������!!!
	{
		new Thread(new Runnable() {            //�������̣߳��������˳�PlayMusicActivityʱ������������������
			 
			@Override
			public void run() {
				
				for(float i = 0.1f; i<=1.0; i += 0.1)
				{
	    			mediaPlayer.setVolume(1-i , 1-i);        //������������������������
	    			try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		try {                    //�������㹻��ʱ����������!!! ������ô��ʱ���ִ�������onBack()
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
		// ���������һ�����ʱ��
		if (e1.getX() - e2.getX() < -120) {
			
			slowDecreaseVolume();      //������������ �� �ڻ����˳���ʱ��         //�ڵ�����Ű�ť�Ļ���û���Ч������Ҫ��һ�µ�ǰ��������ʲô����ʱ��������!!!!
			
			this.onBackPressed(); // �൱�ڰ�����back��
	
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