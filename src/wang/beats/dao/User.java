package wang.beats.dao;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class User implements Serializable{
	private int img;
	private String name;
	private int count;
	private Boolean isChecked=false;
	public User(int img, String name, int count) {
		this.img = img;
		this.name = name;
		this.count = count;
	}
	public int getImg() {
		return img;
	}
	public void setImg(int img) {
		this.img = img;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Boolean getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
}
