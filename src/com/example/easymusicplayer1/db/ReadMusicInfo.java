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
	
	private ArrayList<String> musicUrlList;          //用于存储音乐的在线播放地址!!!
	
	private ArrayList<String> musicDownUrlList;      //用于存储音乐的下载地址
	
	private ArrayList<Integer> musicAlbumidList;          //用于存储音乐的专辑id
    
	private ArrayList<Integer> musicSecondsList;          //用于存储音乐的总播放时间
    
	private ArrayList<Integer> musicSingerIdList;         //用于存储音乐的歌手id
    
	private ArrayList<String> musicSingerNameList;        //用于存储音乐歌手名称
    
	private ArrayList<Integer> musicSongIdList;           //用于存储音乐的id
    
	private ArrayList<String> musicSongNameList;          //用于存储音乐的名称
	
	MusicTop musicTop;
	
	
	public ReadMusicInfo() {
		musicDb = new MusicSQLiteOpenHelper(MyApplication.getContext() , "MusicStore.db" , null , 1);      
		db = musicDb.getWritableDatabase();             //若有，打开数据库，否者，创建新的数据库
		musicTop = new MusicTop();
	} 
	
	/**
	 * 可以把下列各函数进行合并!!!
	 * 
	 */
	
	public ArrayList<String> getMusicName()
	{
		String [] columns = {"songname"};
		musicNameList = new ArrayList<String>();              //..........居然有忘记初始化了，该死 !!!!!!!!
		Cursor cursor = db.query("Music", columns , null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do{
				
				String songName = cursor.getString(cursor.getColumnIndex("songname"));         //从数据库中提取出音乐名称
				//System.out.println(songName + "\n");
				//Log.e("MainActivity", songName);
				musicNameList.add(songName);                   //报错在这里，原因是音乐名为null，不能add进去!!!
			}while(cursor.moveToNext());
		}
		return musicNameList;
	}
	
	
	public ArrayList<String> getMusicDownUrl()
	{
		String [] columns = {"downUrl"};
		musicDownUrlList = new ArrayList<String>();
		Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //七个参数
		if(cursor.moveToFirst())
		{
			do
			{
				String downUrl = cursor.getString(cursor.getColumnIndex("downUrl"));
				//System.out.println(downUrl + "\n");
				musicDownUrlList.add(downUrl);
			}while(cursor.moveToNext());
		}
		return musicDownUrlList;
	}
	
	public ArrayList<Integer> getMusicAlbumId()
	{
		String[] columns = {"albumid"};
		musicAlbumidList = new ArrayList<Integer>();          
		Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //七个参数
		if(cursor.moveToFirst())
		{
			do
			{
				String albumId = cursor.getString(cursor.getColumnIndex("albumid"));
				//System.out.println(albumId + "\n");
				musicDownUrlList.add(albumId);
			}while(cursor.moveToNext());
		}
	    return musicAlbumidList;
	}
	
	public ArrayList<Integer> getMusicSeconds()
	{
		String[] columns = {"seconds"};
	    musicSecondsList = new ArrayList<Integer>();    
		Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //七个参数
		if(cursor.moveToFirst())
		{
			do
			{
				Integer seconds = cursor.getInt(cursor.getColumnIndex("seconds"));
				//System.out.println(seconds + "\n");
				musicSecondsList.add(seconds);
			}while(cursor.moveToNext());
		}
	    return musicSecondsList;
	}
	
	public ArrayList<Integer> getMusicSingerId()
	{
		String[] columns = {"singerid"};
		musicSingerIdList = new ArrayList<Integer>();     
		Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //七个参数
		if(cursor.moveToFirst())
		{
			do
			{
				Integer singerid = cursor.getInt(cursor.getColumnIndex("singerid"));
				//System.out.println(singerid + "\n");
				musicSingerIdList.add(singerid);
			}while(cursor.moveToNext());
		}
	    return musicSingerIdList;
	}
	
	public ArrayList<String> getMusicSingerName()
	{
    	   String[] columns = {"singername"};
		   musicSingerNameList = new ArrayList<String>();       
		   Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //七个参数
			if(cursor.moveToFirst())
			{
				do
				{
					String singername = cursor.getString(cursor.getColumnIndex("singername"));
					//System.out.println(singername + "\n");
					musicSingerNameList.add(singername);
				}while(cursor.moveToNext());
			}
		   return musicSingerNameList;
	}
	
	public ArrayList<Integer> getMusicSongId()
	{
 	   String[] columns = {"songid"};
		musicSongIdList = new ArrayList<Integer>();
		 Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //七个参数
			if(cursor.moveToFirst())
			{
				do
				{
					Integer songid = cursor.getInt(cursor.getColumnIndex("songid"));
					//System.out.println(songid + "\n");
					musicSongIdList.add(songid);
				}while(cursor.moveToNext());
			}
	    return musicSongIdList;
	}
	
	public ArrayList<String> getMusicSongName()
	{
	 	String[] columns = {"songname"};
	    musicSongNameList = new ArrayList<String>();
	    Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //七个参数
		if(cursor.moveToFirst())
		{
			do
			{
				String songname = cursor.getString(cursor.getColumnIndex("songname"));
				//System.out.println(songname + "\n");
				musicSongNameList.add(songname);
			}while(cursor.moveToNext());
		}
        return musicSongNameList;
	}
	
	public ArrayList<String> getMusicUrl()
	{
		String [] columns = {"url"};
		musicUrlList = new ArrayList<String>();
		Cursor cursor = db.query("Music", columns , null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do
			{
				String songUrl = cursor.getString(cursor.getColumnIndex("url"));
				//System.out.println(songUrl + "\n");
				musicUrlList.add(songUrl);
			}while(cursor.moveToNext());
		}
		return musicUrlList;
	}

}