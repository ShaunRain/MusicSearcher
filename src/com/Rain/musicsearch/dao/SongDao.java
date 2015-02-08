package com.Rain.musicsearch.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Rain.musicsearch.MusicSQLiteOpenHelper;
import com.Rain.musicsearch.domain.Song;

public class SongDao {
	private MusicSQLiteOpenHelper helper;
	
	public SongDao(Context context) {
		helper = new MusicSQLiteOpenHelper(context);
	}
	
	public void create(String track, String artist, String album, String time, int cover) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("insert into song (track,artist,album,time,cover) values (?,?,?,?,?)", new Object[]{track,artist,album,time,cover});
		db.close();
	}
	
	/*
	 * 添加各种增删改查等方法
	 * 
	 */
	
	//查找方法
	public Song findBytrack(String track) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Song song;
		Cursor cursor = db.rawQuery("select * from song where track=?", new String[]{track});
		boolean result = cursor.moveToNext();
		if(result) {

		String artist = cursor.getString(cursor.getColumnIndex("artist"));
		String album = cursor.getString(cursor.getColumnIndex("album"));
		String time = cursor.getString(cursor.getColumnIndex("time"));
		int cover = cursor.getInt(cursor.getColumnIndex("cover"));
		song = new Song( track, artist, album, time, cover);
		}else {
			song = new Song(null, null, null, null, -1);
		}
		
		cursor.close();
		db.close();
		return song;
	}
	
	public void update(String track, String newartist, String newalbum, String newtime) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("update song set artist=?,album=?,time=? where track=?", new Object[]{
				newartist,newalbum,newtime,track
		});
		db.close();
	}
	
	public void delete(String track) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from song where track = ?", new Object[]{track});
		db.close();
	}
	
	public void add(String track, String artist, String album, String time, int cover){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("insert into song (track,artist,album,time,cover) values (?,?,?,?,?)", new Object[]{track,artist,album,time,cover});
		db.close();
	}
	
	public ArrayList<Song> findall(){
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<Song> songs = new ArrayList<Song>();
		Cursor cursor = db.rawQuery("select * from song", null);
		while(cursor.moveToNext()){
			String track = cursor.getString(cursor.getColumnIndex("track"));
			String artist = cursor.getString(cursor.getColumnIndex("artist"));
			String album = cursor.getString(cursor.getColumnIndex("album"));
			String time = cursor.getString(cursor.getColumnIndex("time"));
			int cover = cursor.getInt(cursor.getColumnIndex("cover"));
			Song s = new Song(track, artist, album, time, cover);
			songs.add(s);
		}
		cursor.close();
		db.close();
		return songs;
	}

}
