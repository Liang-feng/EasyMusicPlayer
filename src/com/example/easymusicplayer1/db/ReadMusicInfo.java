package com.example.easymusicplayer1.db;

import java.util.ArrayList;

import com.example.easymusicplayer1.model.MusicTop;
import com.example.easymusicplayer1.utility.MyApplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReadMusicInfo {
	
	MusicSQLiteOpenHelper musicDb;                 //用于数据库
	
	SQLiteDatabase db;
	
	private ArrayList<String> musicNameList;         //用于存储音乐名称
	
	MusicTop musicTop;
	
	
	public ReadMusicInfo() {
		musicDb = new MusicSQLiteOpenHelper(MyApplication.getContext() , "MusicStore.db" , null , 1);      
		db = musicDb.getWritableDatabase();             //若有，打开数据库，否者，创建新的数据库
		musicTop = new MusicTop();
	} 
	
	
	public ArrayList<String> getMusicName()
	{
		String [] columns = {"songname"};
		musicNameList = new ArrayList<String>();              //..........居然有忘记初始化了，该死 !!!!!!!!
		Cursor cursor = db.query("Music", columns , null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do{
				
				String songName = cursor.getString(cursor.getColumnIndex("songname"));         //从数据库中提取出音乐名称
				System.out.println(songName + "\n");
				//Log.e("MainActivity", songName);
				musicNameList.add(songName);                   //报错在这里，原因是音乐名为null，不能add进去!!!
			}while(cursor.moveToNext());
		}
		return musicNameList;
	}

}