package com.example.easymusicplayer1.activity;

import java.io.File;
import java.util.ArrayList;

import com.example.easymusicplayer1.R;
import com.example.easymusicplayer1.model.Music;
import com.example.easymusicplayer1.model.MyMusicFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	private Music music = new Music();
	
	private ActionBar actionBar;
	
	private MyMusicFragment myMusicFragment;
	
	public static String playOrder = "ORDER_PLAY";            //����˳��,Ĭ���ǰ�˳�򲥷�

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_LEFT_ICON);       //��actionBar����ô���Ͳ���Ҫ
		setContentView(R.layout.activity_main);

		
        //actionBar
        actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);         //ʹApp���Ʋ��ɼ�,����
		actionBar.setDisplayShowHomeEnabled(false);          //ʹactionBar������ߵ�ͼƬ����
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);       //���õ���ģʽΪ��Tab����

		
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {    //����Tab�ĵ�����
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				switch(tab.getPosition())
				{
				case 0:
					ft.detach(myMusicFragment);
					break;
				}
				
			}
			
			//hava bug
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				switch(tab.getPosition())
				{
				case 0:
					myMusicFragment = new MyMusicFragment();             
					ft.add(android.R.id.content , myMusicFragment);      //�������Ҫ�޸�һ�£�����       //��˵R.id.content��ʲô???
					break;
				case 1:
					break;
				case 2:
					break;
				}
				
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
		};

		
		setTab(tabListener);     //����Tab������ �� �Լ���Tabadd��ActionBar�У�����
		
	}
	
	
	
	//����action Bar�����������ϵĲ˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main , menu);       //��R.menu.main��������������ţ�˳�򲥷ŵȵĲ˵������
		return true;
	}

	// ����˵��ϵ�item��ͻ���ô˺���!!!    no bug
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
		else if(id == R.id.play_once_item)
		{
			playOrder = "ONCE_PLAY";
		}
		return super.onOptionsItemSelected(item);
	}

	
	//ֻ���ڲ�����  , �������˸���ô�죿�������ܻ�Ҫ�޸�!!! 
	@SuppressWarnings("deprecation")
	private void setTab(ActionBar.TabListener tabListener)     //�����½�Tab�������Լ�tab���������!!!
	{
		@SuppressWarnings("deprecation")
		Tab tab1 = actionBar.newTab();
		tab1.setText("�ҵ�����");
		tab1.setTabListener(tabListener);
		Tab tab2 = actionBar.newTab();
		tab2.setText("���ֹ�");
		tab2.setTabListener(tabListener);
		Tab tab3 = actionBar.newTab();
		tab3.setText("������");
		tab3.setTabListener(tabListener);
		
		actionBar.addTab(tab1);
		actionBar.addTab(tab2);
		actionBar.addTab(tab3);
	}


}
