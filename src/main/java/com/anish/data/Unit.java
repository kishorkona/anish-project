package com.anish.data;

public class Unit {
	private Integer id;
	private String sectonId;
	private String subSectonId;
	private Integer type;
	private String questonName1;
	private String questonName1Answer;
	private String questonName2;
	private String questonName2Answer;
	private Integer status;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getQuestonName1() {
		return questonName1;
	}
	public void setQuestonName1(String questonName1) {
		this.questonName1 = questonName1;
	}
	public String getQuestonName2() {
		return questonName2;
	}
	public void setQuestonName2(String questonName2) {
		this.questonName2 = questonName2;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getQuestonName1Answer() {
		return questonName1Answer;
	}
	public void setQuestonName1Answer(String questonName1Answer) {
		this.questonName1Answer = questonName1Answer;
	}
	public String getQuestonName2Answer() {
		return questonName2Answer;
	}
	public void setQuestonName2Answer(String questonName2Answer) {
		this.questonName2Answer = questonName2Answer;
	}
	
	public String getSectonId() {
		return sectonId;
	}
	public void setSectonId(String sectonId) {
		this.sectonId = sectonId;
	}
	public String getSubSectonId() {
		return subSectonId;
	}
	public void setSubSectonId(String subSectonId) {
		this.subSectonId = subSectonId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Unit [id=" + id + ", sectonId=" + sectonId + ", subSectonId=" + subSectonId + ", type=" + type
				+ ", questonName1=" + questonName1 + ", questonName1Answer=" + questonName1Answer + ", questonName2="
				+ questonName2 + ", questonName2Answer=" + questonName2Answer + ", status=" + status + "]";
	}

	

}