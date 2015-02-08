package com.Rain.musicsearch.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Rain.musicsearch.MusicSQLiteOpenHelper;
import com.Rain.musicsearch.domain.Artist;
import com.Rain.musicsearch.domain.Song;

public class ArtistDao {
	private MusicSQLiteOpenHelper helper;

	public ArtistDao(Context context) {
		helper = new MusicSQLiteOpenHelper(context);
	}

	public void create(String name, String area, String company, String birth,int photo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into artists (name,area,company,birth,photo) values (?,?,?,?,?)",
				new Object[] { name, area, company, birth ,photo});
		db.close();
	}

	/*
	 * 添加各种增删改查等方法
	 */
	public Artist findByartist(String kashu) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Artist artist;
		Cursor cursor = db.rawQuery("select * from artists where name=?",
				new String[] { kashu });
		boolean result = cursor.moveToNext();
		if (result) {

			String name = cursor.getString(cursor.getColumnIndex("name"));
			String area = cursor.getString(cursor.getColumnIndex("area"));
			String company = cursor.getString(cursor.getColumnIndex("company"));
			String birth = cursor.getString(cursor.getColumnIndex("birth"));
			int photo = cursor.getInt(cursor.getColumnIndex("photo"));
			artist = new Artist(name, area, company, birth,photo);
		} else {
			artist = new Artist(null, null, null, null,-1);
		}

		cursor.close();
		db.close();
		return artist;
	}

	public String[] andSong(String kashu) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] tracks = new String[100];
		if (kashu != null) {
			int i = 0;
			Cursor cursor = db.rawQuery("select * from song where artist=?",
					new String[] { kashu });
			while (cursor.moveToNext()) {
				String track = cursor.getString(cursor.getColumnIndex("track"));
				tracks[i] = track;
				i++;
			}
			cursor.close();
		} else {
			tracks = null;
		}
		db.close();

		return tracks;
	}
	
	public String[] andAlbum(String kashu) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] albums = new String[100];
		if (kashu != null) {
			int i = 0;
			Cursor cursor = db.rawQuery("select * from albums where artist=?",
					new String[] { kashu });
			while (cursor.moveToNext()) {
				String album = cursor.getString(cursor.getColumnIndex("album"));
				albums[i] = album;
				i++;
			}
			cursor.close();
		} else {
			albums = null;
		}
		db.close();

		return albums;
	}

	public void update(String kashu, String newarea, String newcompany,
			String newbirth) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("update artists set area=?,company=?,birth=? where name=?",
				new Object[] { newarea, newcompany, newbirth, kashu });
		db.close();
	}

	public void delete(String kashu) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from artists where name = ?", new Object[] { kashu });
		db.close();
	}

	public void add(String kashu, String area, String company, String birth, int photo) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into artists (kashu,area,company,birth,photo) values (?,?,?,?,?)",
				new Object[] { kashu, area, company, birth, photo });
		db.close();
	}
	
	public ArrayList<Artist> findall(){
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<Artist> artists = new ArrayList<Artist>();
		Cursor cursor = db.rawQuery("select * from artists", null);
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String area = cursor.getString(cursor.getColumnIndex("area"));
			String company = cursor.getString(cursor.getColumnIndex("company"));
			String birth = cursor.getString(cursor.getColumnIndex("birth"));
			int photo = cursor.getInt(cursor.getColumnIndex("photo"));
			Artist a = new Artist(name, area, company, birth, photo);
			artists.add(a);
		}
		cursor.close();
		db.close();
		return artists;
	}
	
	

}
