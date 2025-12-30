package com.anish.data;

public class BuildQuestion {
	private Integer id;
	private String subject;
	private String grade;
	private String sectionName;
	private String subSectionName;
	private String urlKey;
	private String resultsUrl;
	private String setValue;
	private Integer enabled;
	
	private String sectionId;
	private String subSectionId;
	private String url;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSubSectionId() {
		return subSectionId;
	}
	public void setSubSectionId(String subSectionId) {
		this.subSectionId = subSectionId;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
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
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getSubSectionName() {
		return subSectionName;
	}
	public void setSubSectionName(String subSectionName) {
		this.subSectionName = subSectionName;
	}
	public String getUrlKey() {
		return urlKey;
	}
	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}
	public String getResultsUrl() {
		return resultsUrl;
	}
	public void setResultsUrl(String resultsUrl) {
		this.resultsUrl = resultsUrl;
	}
	public String getSetValue() {
		return setValue;
	}
	public void setSetValue(String setValue) {
		this.setValue = setValue;
	}
	public Integer getEnabled() {
		return enabled;
	}
	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	@Override
	public String toString() {
		return "BuildQuestion [id=" + id + ", subject=" + subject + ", grade=" + grade + ", sectionName=" + sectionName
				+ ", subSectionName=" + subSectionName + ", urlKey=" + urlKey + ", resultsUrl=" + resultsUrl
				+ ", setValue=" + setValue + ", enabled=" + enabled + ", sectionId=" + sectionId + ", subSectionId="
				+ subSectionId + ", url=" + url + "]";
	}

}