package com.example.easymusicplayer1.model;

import java.util.ArrayList;

import com.example.easymusicplayer1.activity.MainActivity;
import com.example.easymusicplayer1.activity.SecondActivity;
import com.example.easymusicplayer1.db.MusicSQLiteOpenHelper;
import com.example.easymusicplayer1.db.ReadMusicInfo;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MusicTopFragment extends Fragment {
	

	
	ListView lv_musicTop;
	
	ArrayAdapter<String> adapter;
	
    ArrayList<String> musicNameTopList;           //���ڴ洢���ֱ���
    
    MusicSQLiteOpenHelper musicDb;                //���ڲ������ݿ�
    
    ReadMusicInfo readMusicInfo;
    
    
    public MusicTopFragment() {
		// TODO Auto-generated constructor stub
	}
    
    
    @Override                //�տ�ʼ����ˣ�д���ˣ�onCreate�������ֲ���û�����������ݣ�����
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	
    	LinearLayout linearLayout =  initView();            //��ʼ������
    	return linearLayout;
    }


	/**
	 * ���棬��ȡ�ؼ�
	 */
	private LinearLayout initView() {

		Log.e("MusicTopFragment" , "��������������ȷ��أ�����������ִ��!!!");
		LinearLayout linearLayout = new LinearLayout(getActivity());           //�½�����
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT);   
		linearLayout.setLayoutParams(layoutParams);               //Ϊ������������
		
	/*	Button button = new Button(getActivity());
		button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT));
		linearLayout.addView(button);
		button.setText("��");
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity() , SecondActivity.class);
				startActivity(intent);
			}
		});*/
		initMusicNameTopListDate();                               //��ʼ����ListView��Ҫ��ʾ������
		
		lv_musicTop = new ListView(getActivity());                //�½�listView
		lv_musicTop.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.WRAP_CONTENT));  //ΪlistView���ÿ�ȣ��߶�����
		adapter = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_list_item_1 , musicNameTopList);
		lv_musicTop.setAdapter(adapter);                          //ΪlistView����adapter
		
		linearLayout.addView(lv_musicTop);                        //����add��ȥ�ˡ�����������

		
		/*TextView text = new TextView(getActivity());
		text.setText("what the hell?");

		LayoutParams textViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.WRAP_CONTENT);
		text.setLayoutParams(textViewLayoutParams);
		
		linearLayout.addView(text);*/
		
		
		Log.e("MainActivity" , "���е�����" +musicNameTopList);

		return linearLayout;
	}

	/**
	 * �����ݿ��ж�ȡ����
	 */
	private void initMusicNameTopListDate() {

		Log.e("MainActivity" , "���е�����");

		readMusicInfo = new ReadMusicInfo();
		musicNameTopList = new ArrayList<String>();
		musicNameTopList = readMusicInfo.getMusicName();          //�����ݿ��ж�ȡ��������
		
          
		if(musicNameTopList == null)
		{
			musicNameTopList.add("����������������������Ӧ");
		}
		
    	   //	Log.e("MainActivity" , musicNameTopList.toString());
		/*if(musicNameTopList.isEmpty())                       //��������ݿ��ж�ȡ��ʧ�ܣ���ô�ʹ������϶�ȡ������
		{
			
		}*/
	}
}
