package com.Rain.musicsearch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicSQLiteOpenHelper extends SQLiteOpenHelper {

	public MusicSQLiteOpenHelper(Context context) {

		super(context, "ipod.db", null, 1);
	}

	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table song (track varchar primary key,artist vachar(20), album vachar(20),"
				+ " time varchar(20), cover varchar(20),foreign key (album) references albums(album),"
				+ "foreign key (artist) references artists(name))");
		db.execSQL("create table albums (album varchar primary key,artist vachar(20), date varchar(20),"
				+ " company varchar(20), style varchar(20), id varchar(20), cover varchar(20),"
				+ "foreign key (artist) references artists(name)," 
				+ "foreign key (company) references companies(name))");
		db.execSQL("create table artists (name varchar primary key, area varchar(20), company varchar(20),"
				+ " birth varchar(20),photo varchar(20)," +
				"foreign key (company) references companies(name))");
		db.execSQL("create table companies (name varchar primary key,area varchar(20), found varchar(20)," +
				"brand varchar(20))");
		

		db.execSQL("create trigger fk_Delete before delete on albums for each row begin delete from song where album = old.album;" 
		+ "end;");
		
		db.execSQL("create trigger fk_Delete_artists before delete on artists for each row begin delete from albums where artist = old.name;"
		+ "end;");
		
		db.execSQL("create trigger fk_Delete_companies before delete on companies for each row begin delete from artists where company = old.name;"
		+ "end;");
		
		 

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
