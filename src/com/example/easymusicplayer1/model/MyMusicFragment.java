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

	private Music music;                  // ���ڻ�ȡmusicTitleList

	private Context context;              // �����ģ������������ݣ�����ͼƬ��!!!

	private Intent serviceIntent = null;  // ��������service��intent
	
	private Intent intent = null ;        //��������PlayMusicActivity
	
	private int musicTitlePosition;                 //���ڱ�Ǹ�����ListView��item���ϵ�λ�ã�������ǰ̨�����ж�λ���������

	private MusicForegroundService1.MusicBinder musicBinder;

	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {       // activity��service�󶨳ɹ�ʱ�ͻ����
			musicBinder = (MusicBinder) service; // ��service��ͨ������
            //��һ�ν���PlayMusicActivity�Ľ���ʱ������ǰ̨�������������
			musicBinder.changeMusicTitle(musicTitleList , musicTitlePosition); // ͨ������MusicBinder��changeMusicTitle��������service���в���!!!
			Log.e("MainActivity" , "�󶨷���");

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		linearLayout = initLayout();        //��ʼ��Tab��ҵ����֡��Ľ���  , ���ֻ��е������г���!!! 
		
		
		new Thread() {                      // �������̣߳���ʵ����Ҳû�£����������治����,��Ҫ��Ϊ����ϰһ���߳�!!!  ��Music���л�ȡmusicUrl��musicDuration��musicId
			@Override
			public void run() {
				initMusicUrl(); // ��ȡ����·��
				initMusicDuration(); // ��ȡ����ʱ��
				initMusicId(); // �����ݿ��л�ȡ���������ݿ��е�Id
			}
		}.start();

			
		musicTitleListView.setOnItemClickListener(new OnItemClickListener() {    //no bug ���õ��ListView�ϵĸ��� 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				musicTitlePosition = position;

				transferDate(position);        //��ListView�ϵ�item���λ��Ϊ�����������ݰ�װ��׼�����ݵ�PlayMusicActivity

				startActivity(intent);         // ����playMusicActivity�����������ֵĽ���

				beginService();               //�������󶨷����Լ��������ݵ�servic�� �������з���Ȼ��Ű󶨷��񣬵���serviceConnection�е�onReceive����
				Log.e("MainActivity" , "���е��� ");

			}
		});

		
		musicTitleListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() { // ����Item��ʾ���˵�
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, "ɾ��");         //�����˵��ɾ����
			}
		});

		return linearLayout;
	}

	//no bug
	private void initMusicTitleList() {        //��ʼ������ȡmusic��title�б�

		music = new Music(); // ��Ҫ���ǳ�ʼ��
		musicTitleList = new ArrayList<String>();
		musicTitleList = new ArrayList<String>(music.getMusicTitle());
	}
	
	
    //no bug
	@Override
	public boolean onContextItemSelected(MenuItem item) {        //���ڳ���ListView��item��ʱ���ֲ˵���ɾ��
		// �����й�ListView��item���й���Ϣ
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		
		int index = (int) info.id;      // ��ȡ��ǰListView�е��item���id

		File file = new File(musicUrlList.get(index));

		switch (item.getItemId()) {
		case 0: // ����˵����е�"ɾ��"

			if (file.exists() && file.isFile()) {
				// ɾ�������ļ�!!! �Լ�ɾ�����ݿ��е�������Ϣ��Ϊ�˲�������app���´�ʱ��һ����ʾ��ListView��!!!
				if (file.delete() && music.deleteMusicFromMediaStore(new String[] { musicIdList.get(index) })) {
					Toast.makeText(getActivity(), "��ɾ������ : " + musicTitleList.get(index), Toast.LENGTH_SHORT).show();
					// ��������Ĳ���������ɾ��������ListView�е�item�����ƣ��Ͳ������·��������!!
					musicTitleList.remove(index); // ɾ��ListView�ĵ�index��item
													// ���Ҫ����Toast�ĺ��棬��Ȼ�������,�ᱨ��!!!
					musicUrlList.remove(index); // ɾ��musicUrlList���йظ�����·����Ϣ
					musicIdList.remove(index); // ɾ��musicIdList���йظ�����id
					musicDurationList.remove(index); // ɾ��musicDurationList���йظ�����ʱ��

					adapter.notifyDataSetChanged(); // ֪ͨadapter�����ݸı�

				} else {
					Toast.makeText(getActivity(), "ɾ������ʧ��", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getActivity(), "�ļ�������", Toast.LENGTH_SHORT).show();
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
	           	// ʹ��javaҲ�����ò���
				LinearLayout linearLayout = new LinearLayout(getActivity());
				// android:layout_width = "match_parent" android:layout_height ="match_parent"
				LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				linearLayout.setLayoutParams(layoutParams);

				// �����Ƕ�listView���в���
				initMusicTitleList(); // ��ʼ��musicTitleList
				adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, musicTitleList);
				musicTitleListView = new ListView(getActivity());
				musicTitleListView.setAdapter(adapter);
				
				// ����listView��width��height
				LayoutParams listViweLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				musicTitleListView.setLayoutParams(listViweLayoutParams);
				// ������ͼ
				linearLayout.addView(musicTitleListView); // ������˼ӣ�С�ģ��ֲ���û��������!!!
				
				return linearLayout;
	}
	

	public void initMusicId() {          //��ȡmusic��id
		setMusicIdList(new ArrayList<String>());
		setMusicIdList(new ArrayList<String>(music.getMusicId()));
	}

	
	public void initMusicDuration() {         //��ȡmusic�Ĳ���ʱ��
		musicDurationList = new ArrayList<Integer>();
		musicDurationList = new ArrayList<Integer>(music.getMusicDuration());
	}

	public void initMusicUrl() {              //��ȡmusic�ļ���·��
		musicUrlList = new ArrayList<String>();
		musicUrlList = new ArrayList<String>(music.getMusicUrl());
	}

	public ArrayList<String> getMusicIdList() {        //����music��id�б� �� ����ʲô��������
		return musicIdList;
	}

	public void setMusicIdList(ArrayList<String> musicIdList) {        //����music��id��List , ����ʲô������
		this.musicIdList = musicIdList;
	}
	
	
	
	
	private void transferDate(int position) {       //no bug ׼�������ݴ��ݸ�PlayMusicActivity

		intent = new Intent(getActivity(), PlayMusicActivity.class); 
		Bundle bundle = new Bundle();
		bundle.putString("music_title", musicTitleList.get(position));
		intent.putExtras(bundle);
		intent.putExtra("music_url", musicUrlList.get(position));
		intent.putExtra("music_duration", musicDurationList.get(position).intValue());
		intent.putExtra("music_title_position", position); // ���ݵ�ǰ����������ListView��item��λ�ã���������ִ��˳�򲥷ţ�����
		intent.putStringArrayListExtra("music_title_list", musicTitleList); // ����musicTitleList������˳���������
		intent.putStringArrayListExtra("music_url_list", musicUrlList);
		intent.putIntegerArrayListExtra("music_duration_list", musicDurationList);
	}
	
	
	private void beginService() {           //�������󶨷����Լ��������ݵ�service
		
		context = MyApplication.getContext();
		serviceIntent = new Intent(context , MusicForegroundService1.class);

		//serviceIntent.putExtra("music_title" , musicTitleList);                //�Ѹ��������б��ݵ�service������ǰ̨service�и������Ƶı仯
		//serviceIntent.putExtra("music_title_position" , musicTitlePosition);//��������ListView��item���ϵ�λ�ã�������ǰ̨�����ж�λ���������

		context.startService(serviceIntent);          //��������
		context.bindService(serviceIntent, serviceConnection , 0);          //�󶨷���
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (serviceIntent != null) {
			// ���ڳ����˳�ʱ���˳�service
			context.stopService(serviceIntent);        // �޷�ȷ��serviceIntentΪ��Ϊ�գ�����Ҫ�����ж����
			context.unbindService(serviceConnection);

		}
	}

}