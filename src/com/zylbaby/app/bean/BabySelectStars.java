package com.zylbaby.app.bean;

public class BabySelectStars {
	
	private int id;
	private int stars;
	public int getStars() {
		return stars;
	}
	public void setStars(int stars) {
		this.stars = stars;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public BabySelectStars(int id,int stars){
		
		setId(id);
		setStars(stars);
	}

}
