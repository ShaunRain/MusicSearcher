package com.Rain.musicsearch.domain;

import java.io.Serializable;

public class Album implements Serializable{
	private String album, date, artist, company, style, id;
	private int cover;

	public Album(String album, String artist,String date,String company, String style,String id,
			int cover) {
		super();
		this.album = album;
		this.date = date;
		this.artist = artist;
		this.company = company;
		this.style = style;
		this.id = id;
		this.cover = cover;
	}

	public String getAlbum() {
		return album;
	}
	
	public String getDate() {
		return date;
	}

	public String getArtist() {
		return artist;
	}

	public String getCompany() {
		return company;
	}

	public String getStyle() {
		return style;
	}

	public int getCover() {
		return cover;
	}
	
	public String getId() {
		return id;
	}

}
