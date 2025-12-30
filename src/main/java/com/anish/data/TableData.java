package com.anish.data;

public class TableData {
	private Integer id;
	private String childName;
	private String value;
	private Integer answer;
	private String result;
	private String answerTime;
	
	private Integer totalQuestions;
	
	
	public TableData() {}
	
	public TableData(int id, String childName, String value) {
		this.id = id;
		this.childName = childName;
		this.value = value;
	}
	
	public Integer getTotalQuestions() {
		return totalQuestions;
	}
	public void setTotalQuestions(Integer totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getChildName() {
		return childName;
	}
	public void setChildName(String childName) {
		this.childName = childName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getAnswer() {
		return answer;
	}
	public void setAnswer(Integer answer) {
		this.answer = answer;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getAnswerTime() {
		return answerTime;
	}
	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}
	@Override
	public String toString() {
		return "TableData [id=" + id + ", childName=" + childName + ", value=" + value + ", answer=" + answer
				+ ", result=" + result + ", answerTime=" + answerTime + ", totalQuestions=" + totalQuestions + "]";
	}
	
	
}
