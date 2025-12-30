package com.anish.data;

public class Question {
	private String subject;
	private String grade;
	private String sectionId;
	private String sectionName;
	private Integer questionStatus;
	private String subSectionId;
	private String subSectionValue;
	private String subSectionName;
	private String resultsUrl;
	private boolean resultsAvailable;
	private String status;
	private String verified;
	private String questionTime;
	
	private Integer totalCurrentQuestions;
	private Integer totalComplexQuestions;
	private Integer totalCompletedQuestions;
	private Integer totalPracticeQuestions;
	private Integer id;
	private String url;
	private String urlKey;
	private String userName;

	private int questionType;
	private Integer questionId;
	private String questionName;
	
	private boolean resultsClean;
	private boolean existingTests;
	private String setValue;
	private boolean wordProbleum = Boolean.FALSE;
	
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getQuestionTime() {
		return questionTime;
	}
	public void setQuestionTime(String questionTime) {
		this.questionTime = questionTime;
	}
	public boolean isResultsAvailable() {
		return resultsAvailable;
	}
	public void setResultsAvailable(boolean resultsAvailable) {
		this.resultsAvailable = resultsAvailable;
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
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public Integer getQuestionStatus() {
		return questionStatus;
	}
	public void setQuestionStatus(Integer questionStatus) {
		this.questionStatus = questionStatus;
	}
	public String getSubSectionId() {
		return subSectionId;
	}
	public void setSubSectionId(String subSectionId) {
		this.subSectionId = subSectionId;
	}
	public String getSubSectionName() {
		return subSectionName;
	}
	public void setSubSectionName(String subSectionName) {
		this.subSectionName = subSectionName;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isResultsClean() {
		return resultsClean;
	}
	public void setResultsClean(boolean resultsClean) {
		this.resultsClean = resultsClean;
	}
	public Integer getTotalPracticeQuestions() {
		return totalPracticeQuestions;
	}
	public void setTotalPracticeQuestions(Integer totalPracticeQuestions) {
		this.totalPracticeQuestions = totalPracticeQuestions;
	}
	public Integer getTotalCurrentQuestions() {
		return totalCurrentQuestions;
	}
	public void setTotalCurrentQuestions(Integer totalCurrentQuestions) {
		this.totalCurrentQuestions = totalCurrentQuestions;
	}
	public Integer getTotalComplexQuestions() {
		return totalComplexQuestions;
	}
	public void setTotalComplexQuestions(Integer totalComplexQuestions) {
		this.totalComplexQuestions = totalComplexQuestions;
	}
	public Integer getTotalCompletedQuestions() {
		return totalCompletedQuestions;
	}
	public void setTotalCompletedQuestions(Integer totalCompletedQuestions) {
		this.totalCompletedQuestions = totalCompletedQuestions;
	}
	
	public String getSubSectionValue() {
		return subSectionValue;
	}
	public void setSubSectionValue(String subSectionValue) {
		this.subSectionValue = subSectionValue;
	}
	
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVerified() {
		return verified;
	}
	public void setVerified(String verified) {
		this.verified = verified;
	}
	
	public boolean isExistingTests() {
		return existingTests;
	}
	public void setExistingTests(boolean existingTests) {
		this.existingTests = existingTests;
	}
	
	
	public boolean isWordProbleum() {
		return wordProbleum;
	}
	public void setWordProbleum(boolean wordProbleum) {
		this.wordProbleum = wordProbleum;
	}
	public String getSetValue() {
		return setValue;
	}
	public void setSetValue(String setValue) {
		this.setValue = setValue;
	}
	@Override
	public String toString() {
		return "Question [subject=" + subject + ", grade=" + grade + ", sectionId=" + sectionId + ", sectionName="
				+ sectionName + ", questionStatus=" + questionStatus + ", subSectionId=" + subSectionId
				+ ", subSectionValue=" + subSectionValue + ", subSectionName=" + subSectionName + ", resultsUrl="
				+ resultsUrl + ", resultsAvailable=" + resultsAvailable + ", status=" + status + ", verified="
				+ verified + ", questionTime=" + questionTime + ", totalCurrentQuestions=" + totalCurrentQuestions
				+ ", totalComplexQuestions=" + totalComplexQuestions + ", totalCompletedQuestions="
				+ totalCompletedQuestions + ", totalPracticeQuestions=" + totalPracticeQuestions + ", id=" + id
				+ ", url=" + url + ", urlKey=" + urlKey + ", userName=" + userName + ", questionType=" + questionType
				+ ", questionId=" + questionId + ", questionName=" + questionName + ", resultsClean=" + resultsClean
				+ ", existingTests=" + existingTests + ", setValue=" + setValue + ", wordProbleum=" + wordProbleum
				+ "]";
	}
	
	

}