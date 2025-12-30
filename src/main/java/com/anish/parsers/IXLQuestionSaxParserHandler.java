package com.anish.parsers;

import com.anish.data.Question;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IXLQuestionSaxParserHandler extends DefaultHandler{

	private List<Question> questionList = null;
	private Question question = null;
	private StringBuilder data = null;
	
	private boolean subject = false;
	private boolean grade = false;
	private boolean sectionId = false;
	private boolean sectionName = false;
	private boolean questionStatus = false;
	private boolean subSectionId = false;
	private boolean subSectionName = false;
	private boolean urlKey = false;
	private boolean resultsUrl = false;
	private boolean status = false;
	private boolean verified = false;
	private boolean questionId = false;
	private boolean questionName = false;
	private boolean id = false;
	private boolean setValue = false;
	private boolean wordProb = false;

	private AtomicInteger counter=null;
	
	public IXLQuestionSaxParserHandler() { }
	
	public IXLQuestionSaxParserHandler(AtomicInteger counter) {
		this.counter = counter;
	}
	
	public List<Question> getQuestionList() {
		return questionList;
	}
	//"https://www.ixl.com/analytics/questions-log#skill="+data.toString()+"&skillPlanSelected=false";
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Question")) {
			// create a new Question and put it in Map
			question = new Question();
			if(this.counter != null) {
				question.setId(counter.incrementAndGet());
			}
			// initialize list
			if (questionList == null)
				questionList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("Id")) {
			id = true;
		} else if (qName.equalsIgnoreCase("Subject")) {
			// set boolean values for fields, will be used in setting Employee variables
			subject = true;
		} else if (qName.equalsIgnoreCase("Grade")) {
			grade = true;
		} else if (qName.equalsIgnoreCase("SectionId")) {
			sectionId = true;
		} else if (qName.equalsIgnoreCase("SectionName")) {
			sectionName = true;
		} else if (qName.equalsIgnoreCase("Enabled")) {
			questionStatus = true;
		} else if (qName.equalsIgnoreCase("SubSectionId")) {
			subSectionId = true;
		} else if (qName.equalsIgnoreCase("SubSectionName")) {
			subSectionName = true;
		} else if (qName.equalsIgnoreCase("UrlKey")) {
			urlKey = true;
		} else if (qName.equalsIgnoreCase("ResultsUrl")) {
			resultsUrl = true;
		} else if (qName.equalsIgnoreCase("Status")) {
			status = true;
		} else if (qName.equalsIgnoreCase("Verified")) {
			verified = true;
		} else if (qName.equalsIgnoreCase("QuestionId")) {
			questionId = true;
		} else if (qName.equalsIgnoreCase("QuestionName")) {
			questionName = true;
		} else if (qName.equalsIgnoreCase("SetValue")) {
			setValue = true;
		} else if (qName.equalsIgnoreCase("WordProbleum")) {
			wordProb = true;
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		try {
			if (subject) {
				question.setSubject(data.toString().toLowerCase());
				subject = false;
			} else if (id) {
				question.setId(Integer.valueOf(data.toString()));
				id = false;
			}  else if (grade) {
				question.setGrade(data.toString());
				grade = false;
			} else if (sectionId) {
				question.setSectionId(data.toString());
				sectionId = false;
			} else if (sectionName) {
				question.setSectionName(data.toString());
				sectionName = false;
			} else if (questionStatus) {
				int questionStatusInt = Integer.valueOf(data.toString());
				question.setQuestionStatus(questionStatusInt);
				questionStatus = false;
			} else if (subSectionId) {
				question.setSubSectionId(data.toString());
				subSectionId = false;
			} else if (subSectionName) {
				question.setSubSectionName(data.toString());
				subSectionName = false;
			} else if (urlKey) {
				question.setUrlKey(data.toString());
				urlKey = false;
			} else if (status) {
				question.setStatus(data.toString());
				status = false;
			} else if (verified) {
				question.setVerified(data.toString());
				verified = false;
			} else if (resultsUrl) {
				question.setResultsUrl(data.toString());
				resultsUrl = false;
			} else if (questionId) {
				question.setQuestionId(Integer.parseInt(data.toString()));
				questionId = false;
			} else if (setValue) {
				question.setSetValue(data.toString());
				setValue = false;
			}  else if (questionName) {
				question.setQuestionName(data.toString());
				questionName = false;
			} else if (wordProb) {
				question.setWordProbleum(Boolean.valueOf(data.toString()));
				wordProb = false;
			} else if (qName.equalsIgnoreCase("Question")) {
				// add Question object to list
				questionList.add(question);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
}
