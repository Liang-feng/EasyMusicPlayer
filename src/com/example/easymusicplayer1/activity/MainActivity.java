package com.example.easymusicplayer1.activity;

import java.io.File;
import java.util.ArrayList;

import com.example.easymusicplayer1.R;
import com.example.easymusicplayer1.model.Music;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	private ListView musicTitleListView;

	private ArrayAdapter<String> adapter;
	
	private ArrayList<String> musicTitleList;
	
	private ArrayList<String> musicUrlList;
	
	private ArrayList<Integer> musicDurationList;
	
	private ArrayList<String> musicIdList;
	
	Music music = new Music();
	
	public static String playOrder = "ORDER_PLAY";            //播放顺序,默认是按顺序播放
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initMusicTitle();          //获取音乐名称
		
		
		adapter = new ArrayAdapter<String>(MainActivity.this , android.R.layout.simple_list_item_1 , musicTitleList);
		musicTitleListView = (ListView)findViewById(R.id.music_title_list_view);
		musicTitleListView.setAdapter(adapter);
		
		initMusicUrl();            //获取音乐路径
		initMusicDuration();       //获取音乐时长
		initMusicId();             //从数据库中获取音乐在数据库中的Id

	    musicTitleListView.setOnItemClickListener(new OnItemClickListener(){
	    	@Override
	    	public void onItemClick(AdapterView<?> parent , View view , int position , long id)
	    	{
	    		Intent intent = new Intent(MainActivity.this , PlayMusicActivity.class);
	    		
	    		Bundle bundle = new Bundle();
	    		bundle.putString("music_title" , musicTitleList.get(position));
	    		intent.putExtras(bundle);
	    		intent.putExtra("music_url" , musicUrlList.get(position));
	    		intent.putExtra("music_duration", musicDurationList.get(position).intValue());
                intent.putExtra("music_title_position" , position);             //传递当前音乐名称在ListView的item上位置，用来方便执行顺序播放！！！
                intent.putStringArrayListExtra("music_title_list" , musicTitleList);         //传递musicTitleList，用于顺序，随机播放
                intent.putStringArrayListExtra("music_url_list" , musicUrlList);
                intent.putIntegerArrayListExtra("music_duration_list" , musicDurationList);
                
	    		startActivity(intent);
	    	}
	    });

	    
	    //长按Item显示出菜单
        musicTitleListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
        	@Override
        	public void onCreateContextMenu(ContextMenu menu , View v
        			, ContextMenuInfo menuInfo)
        	{
        		menu.add(0 , 0 , 0 , "删除");
        	}
        });

	}
	

	
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
	    //返回有关ListView的item的有关信息 
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();   
        //获取当前ListView中点击item项的id
		int index = (int) info.id;              
		
		File file = new File(musicUrlList.get(index));

		
		switch(item.getItemId())
		{
		case 0:          //点击菜单项中的"删除"
			
			if(file.exists() && file.isFile())
			{
				//删除音乐文件!!!    以及删除数据库中的音乐信息，为了不让其在app重新打开时再一次显示在ListView上!!!
	      		if(file.delete() && music.deleteMusicFromMediaStore(new String[]{ musicIdList.get(index)}) )              
	     		{
		      		Toast.makeText(MainActivity.this , "已删除歌曲 : "+musicTitleList.get(index) , Toast.LENGTH_SHORT).show();
		      		//进行下面的操作后，这样删除歌曲后，ListView中的item项上移，就不会出现路径错误了!!
		      		musicTitleList.remove(index);            //删除ListView的第index项item  这个要放在Toast的后面，不然输出不了,会报错!!!
		      		musicUrlList.remove(index);              //删除musicUrlList中有关歌曲的路径信息
		      		musicIdList.remove(index);               //删除musicIdList中有关歌曲的id
		      		musicDurationList.remove(index);         //删除musicDurationList中有关歌曲的时长
		      		
		      		adapter.notifyDataSetChanged();          //通知adapter有数据改变
		      		
		      		//musicTitleListView.invalidate();         
	      		}
	       		else
	     		{
	      			Toast.makeText(MainActivity.this , "删除歌曲失败" , Toast.LENGTH_LONG).show();
	     		}
			}
			else
			{
				Toast.makeText(MainActivity.this , "文件不存在" , Toast.LENGTH_SHORT).show();
			}
			break;
			default:
				break;
		}
		
		return super.onContextItemSelected(item);
		
	}
	
	public void initMusicId()
	{
		setMusicIdList(new ArrayList<String>());
		setMusicIdList(new ArrayList<String>(music.getMusicId()));
	}
	
	
	public void initMusicDuration()
	{
		musicDurationList = new ArrayList<Integer>();
		musicDurationList = new ArrayList<Integer>(music.getMusicDuration());
	}
	
	public void initMusicUrl()
	{
		musicUrlList = new ArrayList<String>();
		musicUrlList = new ArrayList<String>(music.getMusicUrl());
	}
	
	public void initMusicTitle()
	{
		musicTitleList = new ArrayList<String>();
		musicTitleList = new ArrayList<String>(music.getMusicTitle());

	}

	
	//创建action Bar（操作栏）上的菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main , menu);
		return true;
	}

	// 点击菜单上的item后就会调用次函数!!!
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
		
		return super.onOptionsItemSelected(item);
	}


	public ArrayList<String> getMusicIdList() {
		return musicIdList;
	}


	public void setMusicIdList(ArrayList<String> musicIdList) {
		this.musicIdList = musicIdList;
	}
}
