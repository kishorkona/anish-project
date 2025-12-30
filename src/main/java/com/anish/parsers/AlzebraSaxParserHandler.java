package com.anish.parsers;

import com.anish.data.AlzebraQuestion;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class AlzebraSaxParserHandler extends DefaultHandler{

	private List<AlzebraQuestion> questionList = null;
	private AlzebraQuestion question = null;
	private StringBuilder data = null;
	
	private boolean id = false;
	private boolean subject = false;
	private boolean status = false;
	private boolean hasImage = false;
	private boolean rowHight = false;
	private boolean imageName = false;
	private boolean imageWidth = false;
	private boolean imageHeight = false;

	public AlzebraSaxParserHandler() { }
	
	public List<AlzebraQuestion> getQuestionList() {
		return questionList;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Question")) {
			// create a new Question and put it in Map
			question = new AlzebraQuestion();
			// initialize list
			if (questionList == null)
				questionList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("Id")) {
			id = true;
		} else if (qName.equalsIgnoreCase("Subject")) {
			subject = true;
		} else if (qName.equalsIgnoreCase("Status")) {
			status = true;
		} else if (qName.equalsIgnoreCase("hasImage")) {
			hasImage = true;
		} else if (qName.equalsIgnoreCase("RowHight")) {
			rowHight = true;
		} else if (qName.equalsIgnoreCase("ImageName")) {
			imageName = true;
		} else if (qName.equalsIgnoreCase("ImageWidth")) {
			imageWidth = true;
		} else if (qName.equalsIgnoreCase("ImageHeight")) {
			imageHeight = true;
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		try {
			if (subject) {
				question.setSubject(data.toString());
				subject = false;
			} else if (id) {
				question.setId(Integer.valueOf(data.toString()));
				id = false;
			} else if (status) {
				question.setStatus(Integer.valueOf(data.toString()));
				status = false;
			} else if (hasImage) {
				question.setHasImage(Integer.valueOf(data.toString()));
				hasImage = false;
			} else if (rowHight) {
				question.setRowHight(Integer.valueOf(data.toString()));
				rowHight = false;
			} else if (imageName) {
				question.setImageName(data.toString());
				imageName = false;
			} else if (imageWidth) {
				question.setImageWidth(Integer.valueOf(data.toString()));
				imageWidth = false;
			}  else if (imageHeight) {
				question.setImageHeight(Integer.valueOf(data.toString()));
				imageHeight = false;
			} else if (qName.equalsIgnoreCase("Question")) {
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
