package com.anish.data;

public class DoubleHalf {
	private Integer id;
	private String childName;
	private boolean isDouble;
	private Integer value;
	private String answer;
	private String result;
	private String doubleHalfAnswerTime;
	private String printValue;
	
	private Integer totalQuestions;
	
	public Integer getTotalQuestions() {
		return totalQuestions;
	}
	public void setTotalQuestions(Integer totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	public void setDouble(boolean isDouble) {
		this.isDouble = isDouble;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getDoubleHalfAnswerTime() {
		return doubleHalfAnswerTime;
	}
	public void setDoubleHalfAnswerTime(String doubleHalfAnswerTime) {
		this.doubleHalfAnswerTime = doubleHalfAnswerTime;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
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
	public boolean getIsDouble() {
		return isDouble;
	}
	public void setIsDouble(boolean isDouble) {
		this.isDouble = isDouble;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
	
	public String getPrintValue() {
		return printValue;
	}
	public void setPrintValue(String printValue) {
		this.printValue = printValue;
	}
	@Override
	public String toString() {
		return "DoubleHalf [id=" + id + ", childName=" + childName + ", isDouble=" + isDouble + ", value=" + value
				+ ", answer=" + answer + ", result=" + result + ", doubleHalfAnswerTime=" + doubleHalfAnswerTime
				+ ", printValue=" + printValue + ", totalQuestions=" + totalQuestions + "]";
	}

	
	
}
