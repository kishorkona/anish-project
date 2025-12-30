package com.anish.parsers;

import com.anish.data.BuildQuestion;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BuildIXLQuestionSaxParserHandler extends DefaultHandler{

	private List<BuildQuestion> questionList = null;
	private BuildQuestion question = null;
	private StringBuilder data = null;
	
	private boolean subject = false;
	private boolean grade = false;
	private boolean sectionName = false;
	private boolean subSectionName = false;
	private boolean urlKey = false;
	private boolean resultsUrl = false;
	private boolean setValue = false;
	private boolean enabled = false;
	
	private AtomicInteger counter=null;
	
	public BuildIXLQuestionSaxParserHandler() { }
	
	public BuildIXLQuestionSaxParserHandler(AtomicInteger counter) {
		this.counter = counter;
	}
	
	public List<BuildQuestion> getQuestionList() {
		return questionList;
	}
	//"https://www.ixl.com/analytics/questions-log#skill="+data.toString()+"&skillPlanSelected=false";
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("BuildQuestion")) {
			// create a new Question and put it in Map
			question = new BuildQuestion();
			if(this.counter != null) {
				question.setId(counter.incrementAndGet());
			}
			// initialize list
			if (questionList == null) {
				questionList = new ArrayList<>();
			}
		} else if (qName.equalsIgnoreCase("Subject")) {
			// set boolean values for fields, will be used in setting Employee variables
			subject = true;
		} else if (qName.equalsIgnoreCase("Grade")) {
			// set boolean values for fields, will be used in setting Employee variables
			grade = true;
		} else if (qName.equalsIgnoreCase("SectionName")) {
			sectionName = true;
		} else if (qName.equalsIgnoreCase("SubSectionName")) {
			subSectionName = true;
		} else if (qName.equalsIgnoreCase("UrlKey")) {
			urlKey = true;
		} else if (qName.equalsIgnoreCase("ResultsUrl")) {
			resultsUrl = true;
		} else if (qName.equalsIgnoreCase("SetValue")) {
			setValue = true;
		} else if (qName.equalsIgnoreCase("Enabled")) {
			enabled = true;
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		try {
			if (subject) {
				question.setSubject(data.toString());
				subject = false;
			} else if (grade) {
				question.setGrade(data.toString());
				grade = false;
			} else if (sectionName) {
				question.setSectionName(data.toString());
				sectionName = false;
			} else if (subSectionName) {
				String subSecName = data.toString();
				int firstSpaceIndex = subSecName.indexOf(' ');
				String section = subSecName.substring(0, firstSpaceIndex);
				String[] secArr = section.split("\\.");				
				question.setSubSectionName(subSecName);
				if(secArr.length==2) {
					question.setSectionId(secArr[0]);
					question.setSubSectionId(secArr[1]);
				} else if(secArr.length==1) {
					question.setSectionId(secArr[0]);
				}
				subSectionName = false;
			} else if (urlKey) {
				question.setUrlKey(data.toString());
				urlKey = false;
			} else if (resultsUrl) {
				question.setResultsUrl(data.toString());
				resultsUrl = false;
			} else if (setValue) {
				question.setSetValue(data.toString());
				setValue = false;
			} else if (enabled) {
				question.setEnabled(Integer.valueOf(data.toString()));
				enabled = false;
			} else if (qName.equalsIgnoreCase("BuildQuestion")) {
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
