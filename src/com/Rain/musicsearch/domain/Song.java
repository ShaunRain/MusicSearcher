package com.Rain.musicsearch.domain;

import java.io.Serializable;

import android.widget.ImageView;

public class Song implements Serializable{
	private String track,artist,album,time;
	private int cover;


	public Song(String track, String artist, String album, String time, int cover) {
		super();
		this.track = track;
		this.artist = artist;
		this.album = album;
		this.time = time;
		this.cover = cover;
	}
	
	public String getTrack() {
		return track;
	}

	public String getArtist() {
		return artist;
	}

	public String getAlbum() {
		return album;
	}

	public String getTime() {
		return time;
	}
	
	public int getCover() {
		return cover;
	}

}
