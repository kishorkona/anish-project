package com.anish.data;

public class Word {
	
	private Integer id;
	private String name;
	private Integer fileNo;
	private String repeat;
	private Integer totalWords;
	private boolean hasSentence;
	private String sentence;
	private String combination;
	private String testName;
	private String wordPath;
	private String sentencePath;
	private String ishantWrote;
	
	private String personName;
	private Integer wordsLeft;
	private String rightOrWrong;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public Integer getTotalWords() {
		return totalWords;
	}
	public void setTotalWords(Integer totalWords) {
		this.totalWords = totalWords;
	}
	
	public boolean isHasSentence() {
		return hasSentence;
	}
	public void setHasSentence(boolean hasSentence) {
		this.hasSentence = hasSentence;
	}
	public Integer getFileNo() {
		return fileNo;
	}
	public void setFileNo(Integer fileNo) {
		this.fileNo = fileNo;
	}
	
	public String getSentence() {
		return sentence;
	}
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public String getCombination() {
		return combination;
	}
	public void setCombination(String combination) {
		this.combination = combination;
	}
	
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	
	public String getWordPath() {
		return wordPath;
	}
	public void setWordPath(String wordPath) {
		this.wordPath = wordPath;
	}
	public String getSentencePath() {
		return sentencePath;
	}
	public void setSentencePath(String sentencePath) {
		this.sentencePath = sentencePath;
	}
	public String getIshantWrote() {
		return ishantWrote;
	}
	public void setIshantWrote(String ishantWrote) {
		this.ishantWrote = ishantWrote;
	}
	public Integer getWordsLeft() {
		return wordsLeft;
	}
	public void setWordsLeft(Integer wordsLeft) {
		this.wordsLeft = wordsLeft;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getRightOrWrong() {
		return rightOrWrong;
	}
	public void setRightOrWrong(String rightOrWrong) {
		this.rightOrWrong = rightOrWrong;
	}
	@Override
	public String toString() {
		return "Word [id=" + id + ", name=" + name + ", fileNo=" + fileNo + ", repeat=" + repeat + ", totalWords="
				+ totalWords + ", hasSentence=" + hasSentence + ", sentence=" + sentence + ", combination="
				+ combination + ", testName=" + testName + ", wordPath=" + wordPath + ", sentencePath=" + sentencePath
				+ ", ishantWrote=" + ishantWrote + ", personName=" + personName + ", wordsLeft=" + wordsLeft
				+ ", rightOrWrong=" + rightOrWrong + "]";
	}
	
}