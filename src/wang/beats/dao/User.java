package wang.beats.dao;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class User implements Serializable{
	private int img;
	private int name;
	private int count;
	private Boolean isChecked=false;
	public User(int img, int name, int count) {
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
	public int getName() {
		return name;
	}
	public void setName(int name) {
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
