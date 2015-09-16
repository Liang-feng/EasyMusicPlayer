package com.example.easymusicplayer1.model;

import java.util.ArrayList;

import com.example.easymusicplayer1.utility.MyApplication;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class Music {
	

	Cursor cursor;

	ContentResolver contentResolver;

	Context context;

	ArrayList<String> musicTitleList;

	ArrayList<String> musicUrlList;

	ArrayList<Integer> musicDurationList;

	ArrayList<String> musicImageList;

	ArrayList<String> musicIdList;
	

	public Music() {
		context = MyApplication.getContext();
		contentResolver = context.getContentResolver();
		// cursor����֮����Ҫ�ر�ô������رյĻ���ÿ�δ����ݿ��ѯ�����Ǻ��鷳��!!!
		cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
	}

	// ɾ����ý�����ݿ����������Ƶ���Ϣ���ɣ���Ϊ���´�appʱ��������initMusicTitle()���������ʼ��musicTitleList����musicTitleList�ᱻadapter�õ�����ʾ��ListView�ϣ�
	// musicTitleList�е�Ԫ��Ҫ�벻��ʾ��ListView�ϣ�ɾ�����ݿ����������Ƽ��ɣ������Ĳ�ɾ��Ҳ�ɣ��е�©������
	public boolean deleteMusicFromMediaStore(String[] musicId) {
		// �������ƴ���������Ӧ��������ɾ�����ҵ����ݿ��Դ!!!
		contentResolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media._ID + " = ?",
				musicId);
		return true;
	}
	
	

	
	public ArrayList<String> getMusicId() 
	{
		musicIdList = new ArrayList<String>();
		if (cursor.moveToFirst()) 
		{
			do
			{
				String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
				musicIdList.add(id);
			} while (cursor.moveToNext());
		}
		return musicIdList;
	}
	

	public ArrayList<String> getMusicTitle()
{
		String musicName = null;
		musicTitleList = new ArrayList<String>();
		if (cursor.moveToFirst()) 
		{
			do 
			{
				musicName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
				musicTitleList.add(musicName);

			} while (cursor.moveToNext());
		}

		return musicTitleList;
	}

	public ArrayList<String> getMusicUrl() {
		musicUrlList = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			do {
				String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
				musicUrlList.add(url);
			} while (cursor.moveToNext());
		}
		return musicUrlList;
	}

	public ArrayList<Integer> getMusicDuration() {
		musicDurationList = new ArrayList<Integer>();
		if (cursor.moveToFirst()) {
			do {
				Integer integer = Integer
						.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
				musicDurationList.add(integer);
			} while (cursor.moveToNext());
		}
		return musicDurationList;
	}

	/*
	 * public ArrayList<String> getMusicImage() { musicImageList = new
	 * ArrayList<String>(); }
	 */
}
