package com.anish.data;

public class AlzebraQuestion {
	private Integer id;
	private String subject;
	private Integer status;
	private Integer hasImage;
	private Integer rowHight;
	private String imageName;
	private Integer imageWidth;
	private Integer imageHeight;
	
	
	public Integer getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}
	public Integer getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getHasImage() {
		return hasImage;
	}
	public void setHasImage(Integer hasImage) {
		this.hasImage = hasImage;
	}
	public Integer getRowHight() {
		return rowHight;
	}
	public void setRowHight(Integer rowHight) {
		this.rowHight = rowHight;
	}
	
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	@Override
	public String toString() {
		return "AlzebraQuestion [id=" + id + ", subject=" + subject + ", status=" + status + ", hasImage=" + hasImage
				+ ", rowHight=" + rowHight + ", imageName=" + imageName + ", imageWidth=" + imageWidth
				+ ", imageHeight=" + imageHeight + "]";
	}
}
