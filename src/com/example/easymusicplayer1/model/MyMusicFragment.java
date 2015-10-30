package com.example.easymusicplayer1.model;

import java.io.File;
import java.util.ArrayList;

import com.example.easymusicplayer1.activity.PlayMusicActivity;
import com.example.easymusicplayer1.service.MusicForegroundService1;
import com.example.easymusicplayer1.service.MusicForegroundService1.MusicBinder;
import com.example.easymusicplayer1.utility.MyApplication;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;

public class MyMusicFragment extends Fragment {
	
	private LinearLayout linearLayout = null;

	private ListView musicTitleListView;

	private ArrayAdapter<String> adapter;

	private ArrayList<String> musicTitleList;

	private ArrayList<String> musicUrlList;

	private ArrayList<Integer> musicDurationList;

	private ArrayList<String> musicIdList;

	private Music music;                  // 用于获取musicTitleList

	private Context context;              // 上下文，用于引用数据，比如图片等!!!

	private Intent serviceIntent = null;  // 用于启动service的intent
	
	private Intent intent = null ;        //用于启动PlayMusicActivity
	
	private int musicTitlePosition;                 //用于标记歌曲在ListView的item项上的位置，用于在前台服务中定位输出歌曲名

	private MusicForegroundService1.MusicBinder musicBinder;

	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {       // activity与service绑定成功时就会调用
			musicBinder = (MusicBinder) service; // 与service沟通的桥梁
            //第一次进入PlayMusicActivity的界面时候，设置前台服务的音乐名称
			musicBinder.changeMusicTitle(musicTitleList , musicTitlePosition); // 通过调用MusicBinder的changeMusicTitle函数来对service进行操作!!!
			Log.e("MainActivity" , "绑定服务");

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		linearLayout = initLayout();        //初始化Tab项“我的音乐”的界面  , 把手机中的音乐列出来!!! 
		
		
		new Thread() {                      // 增加子线程，其实不加也没事，看起来界面不卡顿,主要是为了练习一下线程!!!  从Music类中获取musicUrl，musicDuration，musicId
			@Override
			public void run() {
				initMusicUrl(); // 获取音乐路径
				initMusicDuration(); // 获取音乐时长
				initMusicId(); // 从数据库中获取音乐在数据库中的Id
			}
		}.start();

			
		musicTitleListView.setOnItemClickListener(new OnItemClickListener() {    //no bug 设置点击ListView上的歌曲 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				musicTitlePosition = position;

				transferDate(position);        //已ListView上的item项的位置为参数，把数据包装，准备传递到PlayMusicActivity

				startActivity(intent);         // 启动playMusicActivity，即播放音乐的界面

				beginService();               //启动，绑定服务，以及传递数据到servic， 是先运行服务，然后才绑定服务，调用serviceConnection中的onReceive函数
				Log.e("MainActivity" , "运行到此 ");

			}
		});

		
		musicTitleListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() { // 长按Item显示出菜单
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, "删除");         //创建菜单项“删除”
			}
		});

		return linearLayout;
	}

	//no bug
	private void initMusicTitleList() {        //初始化，获取music的title列表

		music = new Music(); // 不要忘记初始化
		musicTitleList = new ArrayList<String>();
		musicTitleList = new ArrayList<String>(music.getMusicTitle());
	}
	
	
    //no bug
	@Override
	public boolean onContextItemSelected(MenuItem item) {        //用于长按ListView的item项时出现菜单，删除
		// 返回有关ListView的item的有关信息
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
		int index = (int) info.id;      // 获取当前ListView中点击item项的id

		File file = new File(musicUrlList.get(index));

		switch (item.getItemId()) {
		case 0: // 点击菜单项中的"删除"

			if (file.exists() && file.isFile()) {
				// 删除音乐文件!!! 以及删除数据库中的音乐信息，为了不让其在app重新打开时再一次显示在ListView上!!!
				if (file.delete() && music.deleteMusicFromMediaStore(new String[] { musicIdList.get(index) })) {
					Toast.makeText(getActivity(), "已删除歌曲 : " + musicTitleList.get(index), Toast.LENGTH_SHORT).show();
					// 进行下面的操作后，这样删除歌曲后，ListView中的item项上移，就不会出现路径错误了!!
					musicTitleList.remove(index); // 删除ListView的第index项item
													// 这个要放在Toast的后面，不然输出不了,会报错!!!
					musicUrlList.remove(index); // 删除musicUrlList中有关歌曲的路径信息
					musicIdList.remove(index); // 删除musicIdList中有关歌曲的id
					musicDurationList.remove(index); // 删除musicDurationList中有关歌曲的时长

					adapter.notifyDataSetChanged(); // 通知adapter有数据改变

				} else {
					Toast.makeText(getActivity(), "删除歌曲失败", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getActivity(), "文件不存在", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}

		return super.onContextItemSelected(item);

	}
	
	 
	//no bug
	public LinearLayout initLayout()
	{
	           	// 使用java也能配置布局
				LinearLayout linearLayout = new LinearLayout(getActivity());
				// android:layout_width = "match_parent" android:layout_height ="match_parent"
				LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				linearLayout.setLayoutParams(layoutParams);

				// 下面是对listView进行操作
				initMusicTitleList(); // 初始化musicTitleList
				adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, musicTitleList);
				musicTitleListView = new ListView(getActivity());
				musicTitleListView.setAdapter(adapter);
				
				// 设置listView的width和height
				LayoutParams listViweLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				musicTitleListView.setLayoutParams(listViweLayoutParams);
				// 设置视图
				linearLayout.addView(musicTitleListView); // 差点忘了加，小心，怪不得没东西出来!!!
				
				return linearLayout;
	}
	

	public void initMusicId() {          //获取music的id
		setMusicIdList(new ArrayList<String>());
		setMusicIdList(new ArrayList<String>(music.getMusicId()));
	}

	
	public void initMusicDuration() {         //获取music的播放时间
		musicDurationList = new ArrayList<Integer>();
		musicDurationList = new ArrayList<Integer>(music.getMusicDuration());
	}

	public void initMusicUrl() {              //获取music文件的路径
		musicUrlList = new ArrayList<String>();
		musicUrlList = new ArrayList<String>(music.getMusicUrl());
	}

	public ArrayList<String> getMusicIdList() {        //返回music的id列表 ， 用于什么？？？？
		return musicIdList;
	}

	public void setMusicIdList(ArrayList<String> musicIdList) {        //设置music的id的List , 用于什么？？？
		this.musicIdList = musicIdList;
	}
	
	
	
	
	private void transferDate(int position) {       //no bug 准备把数据传递给PlayMusicActivity

		intent = new Intent(getActivity(), PlayMusicActivity.class); 
		Bundle bundle = new Bundle();
		bundle.putString("music_title", musicTitleList.get(position));
		intent.putExtras(bundle);
		intent.putExtra("music_url", musicUrlList.get(position));
		intent.putExtra("music_duration", musicDurationList.get(position).intValue());
		intent.putExtra("music_title_position", position); // 传递当前音乐名称在ListView的item上位置，用来方便执行顺序播放！！！
		intent.putStringArrayListExtra("music_title_list", musicTitleList); // 传递musicTitleList，用于顺序，随机播放
		intent.putStringArrayListExtra("music_url_list", musicUrlList);
		intent.putIntegerArrayListExtra("music_duration_list", musicDurationList);
	}
	
	
	private void beginService() {           //启动，绑定服务，以及传递数据到service
		
		context = MyApplication.getContext();
		serviceIntent = new Intent(context , MusicForegroundService1.class);

		//serviceIntent.putExtra("music_title" , musicTitleList);                //把歌曲名称列表传递到service，用于前台service中歌曲名称的变化
		//serviceIntent.putExtra("music_title_position" , musicTitlePosition);//歌曲名在ListView的item项上的位置，用于在前台服务中定位输出歌曲名

		context.startService(serviceIntent);          //启动服务
		context.bindService(serviceIntent, serviceConnection , 0);          //绑定服务
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (serviceIntent != null) {
			// 用于程序退出时，退出service
			context.stopService(serviceIntent);        // 无法确定serviceIntent为不为空，所以要加上判断语句
			context.unbindService(serviceConnection);

		}
	}

}