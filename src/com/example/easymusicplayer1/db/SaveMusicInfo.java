package com.example.easymusicplayer1.db;

import com.example.easymusicplayer1.model.MusicTop;
import com.example.easymusicplayer1.utility.MyApplication;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * �������ҳ�ϻ�ȡ�����ݣ����浽���ݿ���!!!
 * 
 * @author feng
 *
 */
public class SaveMusicInfo {

	private static SQLiteDatabase sqliteDataBase;

	private static MusicSQLiteOpenHelper musicSqliteOpenHelper;

	public static void saveMusicInfo(MusicTop music) {
		// �������ݿ� //������ݿ�汾����Ϊ
		musicSqliteOpenHelper = new MusicSQLiteOpenHelper(MyApplication.getContext(), "MusicStore.db", null, 1);
		sqliteDataBase = musicSqliteOpenHelper.getWritableDatabase();

		if (music.getSongName() != null) // ��ֹ�ڶ�ȡ���ݿ��е���������ʱ����nullԪ�أ����ظ�ֵ��ArrayList��arrayList�����п�Ԫ�أ���nullԪ��!!!
		{
			Log.e("SaveMusicInfo" , music.getSongName());
            System.out.println(music.getSongName() + "\n");
			// װ�ؽ�Ҫ�洢�����ݿ����������
			ContentValues contentValues = new ContentValues();
			contentValues.put("albumid", music.getAlbumId());
			contentValues.put("downUrl", music.getDownUrl());
			contentValues.put("singerid", music.getSingerId());
			contentValues.put("singername", music.getSingerName());
			contentValues.put("songid", music.getSongId());
			contentValues.put("songname", music.getSongName());
			contentValues.put("url", music.getUrl());
			contentValues.put("seconds", music.getSeconds());
			sqliteDataBase.insert("Music", null, contentValues); // �������ݵ�����
			// Toast.makeText(MyApplication.getContext() , "�������ݵ����ݿ�",
			// Toast.LENGTH_LONG).show();
		}
	}
}