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
	
    ArrayList<String> musicNameTopList;           //用于存储音乐标题
    
    MusicSQLiteOpenHelper musicDb;                //用于操作数据库
    
    ReadMusicInfo readMusicInfo;
    
    
    public MusicTopFragment() {
		// TODO Auto-generated constructor stub
	}
    
    
    @Override                //刚开始搞错了，写成了，onCreate函数，怪不得没东西出来，草！！！
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	
    	LinearLayout linearLayout =  initView();            //初始化界面
    	return linearLayout;
    }


	/**
	 * 界面，获取控件
	 */
	private LinearLayout initView() {

		Log.e("MusicTopFragment" , "测试请求的数据先返回，还是这里先执行!!!");
		LinearLayout linearLayout = new LinearLayout(getActivity());           //新建布局
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT);   
		linearLayout.setLayoutParams(layoutParams);               //为布局设置属性
		
	/*	Button button = new Button(getActivity());
		button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT));
		linearLayout.addView(button);
		button.setText("打开");
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity() , SecondActivity.class);
				startActivity(intent);
			}
		});*/
		initMusicNameTopListDate();                               //初始化，ListView将要显示的数据
		
		lv_musicTop = new ListView(getActivity());                //新建listView
		lv_musicTop.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.WRAP_CONTENT));  //为listView设置宽度，高度属性
		adapter = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_list_item_1 , musicNameTopList);
		lv_musicTop.setAdapter(adapter);                          //为listView设置adapter
		
		linearLayout.addView(lv_musicTop);                        //忘记add进去了。。。。。晕

		
		/*TextView text = new TextView(getActivity());
		text.setText("what the hell?");

		LayoutParams textViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.WRAP_CONTENT);
		text.setLayoutParams(textViewLayoutParams);
		
		linearLayout.addView(text);*/
		
		
		Log.e("MainActivity" , "运行到这里" +musicNameTopList);

		return linearLayout;
	}

	/**
	 * 从数据库中读取数据
	 */
	private void initMusicNameTopListDate() {

		Log.e("MainActivity" , "运行到这里");

		readMusicInfo = new ReadMusicInfo();
		musicNameTopList = new ArrayList<String>();
		musicNameTopList = readMusicInfo.getMusicName();          //从数据库中读取音乐名称
		
          
		if(musicNameTopList == null)
		{
			musicNameTopList.add("从网络请求数据来不及响应");
		}
		
    	   //	Log.e("MainActivity" , musicNameTopList.toString());
		/*if(musicNameTopList.isEmpty())                       //如果从数据库中读取，失败，那么就从网络上读取！！！
		{
			
		}*/
	}
}
