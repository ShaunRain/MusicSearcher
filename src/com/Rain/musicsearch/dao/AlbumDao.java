package com.Rain.musicsearch.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Rain.musicsearch.MusicSQLiteOpenHelper;
import com.Rain.musicsearch.domain.Album;

public class AlbumDao {
	private MusicSQLiteOpenHelper helper;

	public AlbumDao(Context context) {
		helper = new MusicSQLiteOpenHelper(context);
	}

	public void create(String album, String artist, String date,
			String company, String stlye, String id, int cover) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into albums (album,artist,date,company,style,id,cover) values (?,?,?,?,?,?,?)",
				new Object[] { album, artist, date, company, stlye, id, cover });
		db.close();
	}

	/*
	 * 添加各种增删改查等方法
	 */
	public Album findByalbum(String arubamu) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Album album;
		Cursor cursor = db.rawQuery("select * from albums where album=?",
				new String[] { arubamu });
		boolean result = cursor.moveToNext();
		if (result) {

			String artist = cursor.getString(cursor.getColumnIndex("artist"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String company = cursor.getString(cursor.getColumnIndex("company"));
			String style = cursor.getString(cursor.getColumnIndex("style"));
			String id = cursor.getString(cursor.getColumnIndex("id"));
			int cover = cursor.getInt(cursor.getColumnIndex("cover"));
			album = new Album(arubamu, artist, date, company, style, id, cover);
		} else {
			album = new Album(null, null, null, null, null, null, -1);
		}

		cursor.close();
		db.close();
		return album;
	}

	public String[] andSong(String arubamu) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] tracks = new String[100];
		if (arubamu != null) {
			int i = 0;
			Cursor cursor = db.rawQuery("select * from song where album=?",
					new String[] { arubamu });
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

	public void update(String arubamu, String newartist, String newdate,
			String newcompany, String newstyle) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"update albums set artist=?,date=?,company=?,style=? ,id=? where album=?",
				new Object[] { newartist, newdate, newcompany, newstyle,
						arubamu });
		db.close();
	}

	public void delete(String arubamu) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from albums where album = ?",
				new Object[] { arubamu });
		/*db.execSQL("delete from song where album = ?",
				new Object[] { arubamu });*/
		db.close();
	}

	public void add(String arubamu, String artist, String date, String company,
			String style, String id, int cover) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into albums (arubamu,artist,date,company,style,id,cover) values (?,?,?,?,?,?,?)",
				new Object[] { arubamu, artist, date, company, style, id, cover });
		db.close();
	}
	
	public ArrayList<Album> findall(){
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<Album> albums = new ArrayList<Album>();
		Cursor cursor = db.rawQuery("select * from albums", null);
		while(cursor.moveToNext()){
			String album = cursor.getString(cursor.getColumnIndex("album"));
			String artist = cursor.getString(cursor.getColumnIndex("artist"));
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String company = cursor.getString(cursor.getColumnIndex("company"));
			String style = cursor.getString(cursor.getColumnIndex("style"));
			String id = cursor.getString(cursor.getColumnIndex("id"));
			int cover = cursor.getInt(cursor.getColumnIndex("cover"));
			Album a = new Album(album, artist, date, company, style, id, cover);
			albums.add(a);
		}
		cursor.close();
		db.close();
		return albums;
	}
}
