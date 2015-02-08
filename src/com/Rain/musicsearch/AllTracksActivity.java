package com.Rain.musicsearch;

import java.io.Serializable;
import java.util.ArrayList;
import com.Rain.musicsearch.domain.Song;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AllTracksActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alltracks);
		LinearLayout ll_track = (LinearLayout) findViewById(R.id.ll_track);
		
		Intent intent = getIntent();
		ArrayList<Song> songs = (ArrayList<Song>) intent.getSerializableExtra("songs");
		for(Song song : songs){
			String info = song.getTrack().toString();
			TextView tv = new TextView(this);
			tv.setTextSize(20);
			tv.setTextColor(Color.WHITE);
			tv.setText(info);
			ll_track.addView(tv);
		}
		
	}
}
