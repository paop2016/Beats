package wang.beats.dao;

public class Position {
	
	private int name;
	private int count;
	private int newCount;
	public Position(int name, int count) {
		this(name, count, 0);
	}
	public Position(int name, int count, int newCount) {
		this.name = name;
		this.count = count;
		this.newCount = newCount;
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
	public int getNewCount() {
		return newCount;
	}
	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}
}
