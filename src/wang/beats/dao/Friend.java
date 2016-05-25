package wang.beats.dao;

public class Friend {
	private int name;
	private int imgId;
	private String similar;
	private boolean isChecked;
	public boolean isChecked() {
		return isChecked;
	}
	public Friend( int imgId,int name,String similar) {
		super();
		this.name = name;
		this.imgId = imgId;
		this.similar = similar;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public String getSimilar() {
		return similar;
	}
	public void setSimilar(String similar) {
		this.similar = similar;
	}
}
