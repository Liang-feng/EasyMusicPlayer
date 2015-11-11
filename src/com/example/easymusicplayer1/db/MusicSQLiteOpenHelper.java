package com.example.easymusicplayer1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.Toast;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicSQLiteOpenHelper extends SQLiteOpenHelper {

	Context myContext;       //   ��������Toast����Ϣ��ʾ���!!!
	
	public static final String CREATE_MUSIC = "create table Music ("
			+ "id integer primary key autoincrement , "
			+ "albumid integer , "
			+ "downUrl text , "
			+ "seconds integer , "
			+ "singerid integer , "
			+ "singername text , "
		    + "songid integer , "
			+ "songname text , "
		    + "url text ) ";
	
	public MusicSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		myContext = context; 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_MUSIC);      //�������ֱ�
		Toast.makeText(myContext, "�ɹ������������ݿ�", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  //���ݿ����ʱ������!!!
		// TODO Auto-generated method stub
        switch(oldVersion)
        {
        case 1:
        case 2:       
        }
	}

}
