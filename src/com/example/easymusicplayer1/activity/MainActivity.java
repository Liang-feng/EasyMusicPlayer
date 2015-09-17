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
	
	public static String playOrder = "ORDER_PLAY";            //����˳��,Ĭ���ǰ�˳�򲥷�
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initMusicTitle();          //��ȡ��������
		
		
		adapter = new ArrayAdapter<String>(MainActivity.this , android.R.layout.simple_list_item_1 , musicTitleList);
		musicTitleListView = (ListView)findViewById(R.id.music_title_list_view);
		musicTitleListView.setAdapter(adapter);
		
		initMusicUrl();            //��ȡ����·��
		initMusicDuration();       //��ȡ����ʱ��
		initMusicId();             //�����ݿ��л�ȡ���������ݿ��е�Id

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
                intent.putExtra("music_title_position" , position);             //���ݵ�ǰ����������ListView��item��λ�ã���������ִ��˳�򲥷ţ�����
                intent.putStringArrayListExtra("music_title_list" , musicTitleList);         //����musicTitleList������˳���������
                intent.putStringArrayListExtra("music_url_list" , musicUrlList);
                intent.putIntegerArrayListExtra("music_duration_list" , musicDurationList);
                
	    		startActivity(intent);
	    	}
	    });

	    
	    //����Item��ʾ���˵�
        musicTitleListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){
        	@Override
        	public void onCreateContextMenu(ContextMenu menu , View v
        			, ContextMenuInfo menuInfo)
        	{
        		menu.add(0 , 0 , 0 , "ɾ��");
        	}
        });

	}
	

	
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
	    //�����й�ListView��item���й���Ϣ 
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();   
        //��ȡ��ǰListView�е��item���id
		int index = (int) info.id;              
		
		File file = new File(musicUrlList.get(index));

		
		switch(item.getItemId())
		{
		case 0:          //����˵����е�"ɾ��"
			
			if(file.exists() && file.isFile())
			{
				//ɾ�������ļ�!!!    �Լ�ɾ�����ݿ��е�������Ϣ��Ϊ�˲�������app���´�ʱ��һ����ʾ��ListView��!!!
	      		if(file.delete() && music.deleteMusicFromMediaStore(new String[]{ musicIdList.get(index)}) )              
	     		{
		      		Toast.makeText(MainActivity.this , "��ɾ������ : "+musicTitleList.get(index) , Toast.LENGTH_SHORT).show();
		      		//��������Ĳ���������ɾ��������ListView�е�item�����ƣ��Ͳ������·��������!!
		      		musicTitleList.remove(index);            //ɾ��ListView�ĵ�index��item  ���Ҫ����Toast�ĺ��棬��Ȼ�������,�ᱨ��!!!
		      		musicUrlList.remove(index);              //ɾ��musicUrlList���йظ�����·����Ϣ
		      		musicIdList.remove(index);               //ɾ��musicIdList���йظ�����id
		      		musicDurationList.remove(index);         //ɾ��musicDurationList���йظ�����ʱ��
		      		
		      		adapter.notifyDataSetChanged();          //֪ͨadapter�����ݸı�
		      		
		      		//musicTitleListView.invalidate();         
	      		}
	       		else
	     		{
	      			Toast.makeText(MainActivity.this , "ɾ������ʧ��" , Toast.LENGTH_LONG).show();
	     		}
			}
			else
			{
				Toast.makeText(MainActivity.this , "�ļ�������" , Toast.LENGTH_SHORT).show();
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

	
	//����action Bar�����������ϵĲ˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main , menu);
		return true;
	}

	// ����˵��ϵ�item��ͻ���ôκ���!!!
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		
		int id = item.getItemId();
		
		if (id == R.id.action_settings)          //����ѭ��
		{
			playOrder = "SINGLE_CIRCLE";
			return true;
		}
		else if(id == R.id.play_order_item)        //��ѭ�򲥷�
		{
			playOrder = "ORDER_PLAY";
			return true;
		}
		else if(id == R.id.play_random_item)          //�������
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
