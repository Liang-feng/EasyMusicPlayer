package com.example.easymusicplayer1.activity;

import java.util.ArrayList;

import com.example.easymusicplayer1.R;
import com.example.easymusicplayer1.db.ReadMusicInfo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SecondActivity extends Activity
{
		
		ListView musicListView;
		
		ArrayList<String>  musicNameList;
		
		ArrayAdapter<String> adapter ;
		
		ReadMusicInfo readMusicInfo;
	    
		String [] strTest = {"1" , "2"};

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.music_list);                        //没有引入布局，怪不得，listView为空!!!
			
			initMusicData();             //初始化本activit的listView的item项的数据
			
	        musicListView = (ListView) findViewById(R.id.msuic_list_view);
	        String[]music1 = new String[musicNameList.size()] ;     //{ musicNameList.get(0)};
	        
	        Log.e("MainActivity" , String.valueOf(musicNameList.size()));
	        
	       for(int i=0; i<musicNameList.size(); i++)
	       {
	    	   if(musicNameList.get(i) != null)                     // arrayList中不能有空元素，即null，不然会报错!!!
	        	   music1[i] = musicNameList.get(i);
	    	   else musicNameList.remove(i);
	       }
	       
	        																							//为什么strTest不出错??
		    adapter = new ArrayAdapter<String>(SecondActivity.this , android.R.layout.simple_list_item_1 , musicNameList ); 
		    musicListView.setAdapter(adapter);
	       // Log.e("MainActivity" , "运行到这里");
	       
		}

		private void initMusicData() {     //先从数据库读取，若数据库没有，则从网络上请求!!!
			
			readMusicInfo = new ReadMusicInfo();
			musicNameList = new ArrayList<String>();          //晕，又忘记初始化了!!!
			Log.e("MainActivity" , "运行到此");
			musicNameList = readMusicInfo.getMusicName();           //从数据库中获取音乐名称
			Log.e("MainActivity" , "运行到此");

			
			System.out.println(musicNameList);
			//TextView musicInfo = (TextView) findViewById(R.id.music_info_text_view);
			//musicInfo.setText(musicNameList.toString());
		}
		

}
