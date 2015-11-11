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
			setContentView(R.layout.music_list);                        //û�����벼�֣��ֲ��ã�listViewΪ��!!!
			
			initMusicData();             //��ʼ����activit��listView��item�������
			
	        musicListView = (ListView) findViewById(R.id.msuic_list_view);
	        String[]music1 = new String[musicNameList.size()] ;     //{ musicNameList.get(0)};
	        
	        Log.e("MainActivity" , String.valueOf(musicNameList.size()));
	        
	       for(int i=0; i<musicNameList.size(); i++)
	       {
	    	   if(musicNameList.get(i) != null)                     // arrayList�в����п�Ԫ�أ���null����Ȼ�ᱨ��!!!
	        	   music1[i] = musicNameList.get(i);
	    	   else musicNameList.remove(i);
	       }
	       
	        																							//ΪʲôstrTest������??
		    adapter = new ArrayAdapter<String>(SecondActivity.this , android.R.layout.simple_list_item_1 , musicNameList ); 
		    musicListView.setAdapter(adapter);
	       // Log.e("MainActivity" , "���е�����");
	       
		}

		private void initMusicData() {     //�ȴ����ݿ��ȡ�������ݿ�û�У��������������!!!
			
			readMusicInfo = new ReadMusicInfo();
			musicNameList = new ArrayList<String>();          //�Σ������ǳ�ʼ����!!!
			Log.e("MainActivity" , "���е���");
			musicNameList = readMusicInfo.getMusicName();           //�����ݿ��л�ȡ��������
			Log.e("MainActivity" , "���е���");

			
			System.out.println(musicNameList);
			//TextView musicInfo = (TextView) findViewById(R.id.music_info_text_view);
			//musicInfo.setText(musicNameList.toString());
		}
		

}
