package com.Rain.musicsearch.domain;

import java.io.Serializable;

public class Artist implements Serializable{
	private String name,area,company,birth;
	private int photo;

	public Artist(String name, String area, String company, String birth,int photo) {
		super();
		this.name = name;
		this.area = area;
		this.company = company;
		this.birth = birth;
		this.photo = photo;
	}

	public int getPhoto() {
		return photo;
	}

	public String getName() {
		return name;
	}


	public String getArea() {
		return area;
	}

	public String getCompany() {
		return company;
	}


	public String getBirth() {
		return birth;
	}

	
	

}
