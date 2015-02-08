package com.Rain.musicsearch.domain;

import java.io.Serializable;

public class Company implements Serializable{
	private String name, area, found;
	private int brand;

	public Company(String name, String area, String found, int brand) {
		super();
		this.name = name;
		this.area = area;
		this.found = found;
		this.brand = brand;
	}

	public String getName() {
		return name;
	}

	public String getArea() {
		return area;
	}

	public String getFound() {
		return found;
	}

	public int getBrand() {
		return brand;
	}

}
