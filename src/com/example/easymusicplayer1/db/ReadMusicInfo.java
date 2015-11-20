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
	
	private ArrayList<String> musicUrlList;          //���ڴ洢���ֵ����߲��ŵ�ַ!!!
	
	private ArrayList<String> musicDownUrlList;      //���ڴ洢���ֵ����ص�ַ
	
	private ArrayList<Integer> musicAlbumidList;          //���ڴ洢���ֵ�ר��id
    
	private ArrayList<Integer> musicSecondsList;          //���ڴ洢���ֵ��ܲ���ʱ��
    
	private ArrayList<Integer> musicSingerIdList;         //���ڴ洢���ֵĸ���id
    
	private ArrayList<String> musicSingerNameList;        //���ڴ洢���ָ�������
    
	private ArrayList<Integer> musicSongIdList;           //���ڴ洢���ֵ�id
    
	private ArrayList<String> musicSongNameList;          //���ڴ洢���ֵ�����
	
	MusicTop musicTop;
	
	
	public ReadMusicInfo() {
		musicDb = new MusicSQLiteOpenHelper(MyApplication.getContext() , "MusicStore.db" , null , 1);      
		db = musicDb.getWritableDatabase();             //���У������ݿ⣬���ߣ������µ����ݿ�
		musicTop = new MusicTop();
	} 
	
	/**
	 * ���԰����и��������кϲ�!!!
	 * 
	 */
	
	public ArrayList<String> getMusicName()
	{
		String [] columns = {"songname"};
		musicNameList = new ArrayList<String>();              //..........��Ȼ�����ǳ�ʼ���ˣ����� !!!!!!!!
		Cursor cursor = db.query("Music", columns , null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			do{
				
				String songName = cursor.getString(cursor.getColumnIndex("songname"));         //�����ݿ�����ȡ����������
				//System.out.println(songName + "\n");
				//Log.e("MainActivity", songName);
				musicNameList.add(songName);                   //���������ԭ����������Ϊnull������add��ȥ!!!
			}while(cursor.moveToNext());
		}
		return musicNameList;
	}
	
	
	public ArrayList<String> getMusicDownUrl()
	{
		String [] columns = {"downUrl"};
		musicDownUrlList = new ArrayList<String>();
		Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //�߸�����
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
		Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //�߸�����
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
		Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //�߸�����
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
		Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //�߸�����
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
		   Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //�߸�����
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
		 Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //�߸�����
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
	    Cursor cursor = db.query("Music", columns, null, null, null, null, null);       //�߸�����
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