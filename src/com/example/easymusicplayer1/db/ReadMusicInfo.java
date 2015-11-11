package com.example.easymusicplayer1.db;

import java.util.ArrayList;

import com.example.easymusicplayer1.model.MusicTop;
import com.example.easymusicplayer1.utility.MyApplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ReadMusicInfo {
	
	MusicSQLiteOpenHelper musicDb;                 //�������ݿ�
	
	SQLiteDatabase db;
	
	private ArrayList<String> musicNameList;         //���ڴ洢��������
	
	MusicTop musicTop;
	
	
	public ReadMusicInfo() {
		musicDb = new MusicSQLiteOpenHelper(MyApplication.getContext() , "MusicStore.db" , null , 1);      
		db = musicDb.getWritableDatabase();             //���У������ݿ⣬���ߣ������µ����ݿ�
		musicTop = new MusicTop();
	} 
	
	
	public ArrayList<String> getMusicName()
	{
		String [] columns = {"songname"};
		musicNameList = new ArrayList<String>();              //..........��Ȼ�����ǳ�ʼ���ˣ����� !!!!!!!!
		Cursor cursor = db.query("Music", columns , null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do{
				
				String songName = cursor.getString(cursor.getColumnIndex("songname"));         //�����ݿ�����ȡ����������
				System.out.println(songName + "\n");
				//Log.e("MainActivity", songName);
				musicNameList.add(songName);                   //���������ԭ����������Ϊnull������add��ȥ!!!
			}while(cursor.moveToNext());
		}
		return musicNameList;
	}

}