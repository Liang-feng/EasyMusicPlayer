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
		// cursor用完之后需要关闭么？如果关闭的话，每次从数据库查询东西是很麻烦的!!!
		cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
	}

	// 删除多媒体数据库中音乐名称的信息即可，因为重新打开app时，调用了initMusicTitle()函数，会初始化musicTitleList，而musicTitleList会被adapter用到，显示在ListView上，
	// musicTitleList中的元素要想不显示在ListView上，删除数据库中音乐名称即可，其它的不删除也可，有点漏洞！！
	public boolean deleteMusicFromMediaStore(String[] musicId) {
		// 音乐名称从哪里来就应该在哪里删除，找到数据库表源!!!
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
