package com.Rain.musicsearch.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Rain.musicsearch.MusicSQLiteOpenHelper;
import com.Rain.musicsearch.domain.Artist;
import com.Rain.musicsearch.domain.Company;

public class CompanyDao {
	private MusicSQLiteOpenHelper helper;

	public CompanyDao(Context context) {
		helper = new MusicSQLiteOpenHelper(context);
	}

	public void create(String name, String area, String found,
			int brand) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into companies (name,area,found,brand) values (?,?,?,?)",
				new Object[] { name, area, found, brand });
		db.close();
	}

	/*
	 * 添加各种增删改查等方法
	 */
	public Company findBycompany(String kaisha) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Company company;
		Cursor cursor = db.rawQuery("select * from companies where name=?",
				new String[] { kaisha });
		boolean result = cursor.moveToNext();
		if (result) {

			String name = cursor.getString(cursor.getColumnIndex("name"));
			String area = cursor.getString(cursor.getColumnIndex("area"));
			String found = cursor.getString(cursor.getColumnIndex("found"));
			int brand = cursor.getInt(cursor.getColumnIndex("brand"));
			company = new Company(name, area, found, brand);
		} else {
			company = new Company(null, null, null, -1);
		}

		cursor.close();
		db.close();
		return company;
	}
	
	public String[] andArtist(String kaisha) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] artists = new String[100];
		if (kaisha != null) {
			int i = 0;
			Cursor cursor = db.rawQuery("select * from artists where company=?",
					new String[] { kaisha });
			while (cursor.moveToNext()) {
				String artist = cursor.getString(cursor.getColumnIndex("name"));
				artists[i] = artist;
				i++;
			}
			cursor.close();
		} else {
			artists = null;
		}
		db.close();

		return artists;
	}
	
	public String[] andAlbum(String kaisha) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] albums = new String[100];
		if (kaisha != null) {
			int i = 0;
			Cursor cursor = db.rawQuery("select * from albums where company=?",
					new String[] { kaisha });
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
	
	public void update(String kaisha, String newarea, String newfound) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("update companies set area=?,found=? where name=?",
				new Object[] { newarea, newfound, kaisha});
		db.close();
	}

	public void delete(String kaisha) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from companies where name = ?", new Object[] { kaisha });
		db.close();
	}

	public void add(String kaisha, String area, String found, int brand) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(
				"insert into companies (kashu,area,found,brand) values (?,?,?,?)",
				new Object[] { kaisha, area, found, brand});
		db.close();
	}
	
	public ArrayList<Company> findall(){
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<Company> companies = new ArrayList<Company>();
		Cursor cursor = db.rawQuery("select * from companies", null);
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String area = cursor.getString(cursor.getColumnIndex("area"));
			String found = cursor.getString(cursor.getColumnIndex("found"));
			int brand = cursor.getInt(cursor.getColumnIndex("brand"));
			Company a = new Company(name, area, found, brand);
			companies.add(a);
		}
		cursor.close();
		db.close();
		return companies;
	}
}
