package com.example.easymusicplayer1.model;

import java.io.File;
import java.util.ArrayList;

import com.example.easymusicplayer1.activity.PlayMusicActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
	
	private ListView musicTitleListView;

	private ArrayAdapter<String> adapter;
	
	private ArrayList<String> musicTitleList;
	
    private ArrayList<String> musicUrlList;
	
	private ArrayList<Integer> musicDurationList;
	
	private ArrayList<String> musicIdList;
	
	Music music;      //���ڻ�ȡmusicTitleList
	
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState)
	{
		LinearLayout linearLayout = new LinearLayout(getActivity());
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT);
		linearLayout.setLayoutParams(layoutParams);
		
		//�����Ƕ�listView���в���
		initMusicTitleList();         //��ʼ��musicTitleList
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1 , musicTitleList);
		musicTitleListView = new ListView(getActivity());
		musicTitleListView.setAdapter(adapter);
		//����listView��width��height
		LayoutParams listViweLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.WRAP_CONTENT);
		musicTitleListView.setLayoutParams(listViweLayoutParams);
		
		linearLayout.addView(musicTitleListView);      //������˼ӣ�С�ģ��ֲ���û��������!!!
		
		
		initMusicUrl();            //��ȡ����·��
		initMusicDuration();       //��ȡ����ʱ��
		initMusicId();             //�����ݿ��л�ȡ���������ݿ��е�Id
		
		
		
		 musicTitleListView.setOnItemClickListener(new OnItemClickListener(){
    	@Override
    	public void onItemClick(AdapterView<?> parent , View view , int position , long id)
    	{
    		Intent intent = new Intent(getActivity() , PlayMusicActivity.class);        //
    		 
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
		
		
		return linearLayout;
	}

	private void initMusicTitleList() {

		music = new Music();        //��Ҫ���ǳ�ʼ��
		musicTitleList = new ArrayList<String>();
		musicTitleList = new ArrayList<String>(music.getMusicTitle());
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
		      		Toast.makeText(getActivity() , "��ɾ������ : "+musicTitleList.get(index) , Toast.LENGTH_SHORT).show();
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
	      			Toast.makeText(getActivity() , "ɾ������ʧ��" , Toast.LENGTH_LONG).show();
	     		}
			}
			else
			{
				Toast.makeText(getActivity() , "�ļ�������" , Toast.LENGTH_SHORT).show();
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
	
	public ArrayList<String> getMusicIdList() {
		return musicIdList;
	}


	public void setMusicIdList(ArrayList<String> musicIdList) {
		this.musicIdList = musicIdList;
	}

}
