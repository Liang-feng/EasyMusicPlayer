package com.example.easymusicplayer1.db;

import com.example.easymusicplayer1.model.MusicTop;
import com.example.easymusicplayer1.utility.MyApplication;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 保存从网页上获取的数据，保存到数据库上!!!
 * 
 * @author feng
 *
 */
public class SaveMusicInfo {

	private static SQLiteDatabase sqliteDataBase;

	private static MusicSQLiteOpenHelper musicSqliteOpenHelper;

	public static void saveMusicInfo(MusicTop music) {
		// 创建数据库 //搞错，数据库版本不可为
		musicSqliteOpenHelper = new MusicSQLiteOpenHelper(MyApplication.getContext(), "MusicStore.db", null, 1);
		sqliteDataBase = musicSqliteOpenHelper.getWritableDatabase();

		if (music.getSongName() != null) // 防止在读取数据库中的音乐名称时，有null元素，返回赋值给ArrayList，arrayList不能有空元素，即null元素!!!
		{
			Log.e("SaveMusicInfo" , music.getSongName());
            System.out.println(music.getSongName() + "\n");
			// 装载将要存储在数据库里面的数据
			ContentValues contentValues = new ContentValues();
			contentValues.put("albumid", music.getAlbumId());
			contentValues.put("downUrl", music.getDownUrl());
			contentValues.put("singerid", music.getSingerId());
			contentValues.put("singername", music.getSingerName());
			contentValues.put("songid", music.getSongId());
			contentValues.put("songname", music.getSongName());
			contentValues.put("url", music.getUrl());
			contentValues.put("seconds", music.getSeconds());
			sqliteDataBase.insert("Music", null, contentValues); // 插入数据到表中
			// Toast.makeText(MyApplication.getContext() , "保存数据到数据库",
			// Toast.LENGTH_LONG).show();
		}
	}
}