package com.anish.parsers;

import com.anish.data.Unit;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class UnitSaxParserHandler extends DefaultHandler{

	private List<Unit> unitList = null;
	private Unit unit = null;
	private StringBuilder data = null;
	
	private boolean id = false;
	private boolean questonName1 = false;
	private boolean sectonId = false;
	private boolean subSectonId = false;
	private boolean type = false;
	private boolean questonName1Answer = false;
	private boolean questonName2 = false;
	private boolean questonName2Answer = false;
	private boolean status = false;
	
	public UnitSaxParserHandler() { }
	
	
	public List<Unit> getQuestionList() {
		return unitList;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Unit")) {
			// create a new Question and put it in Map
			unit = new Unit();
			// initialize list
			if (unitList == null)
				unitList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("Id")) {
			id = true;
		} else if (qName.equalsIgnoreCase("SectonId")) {
			sectonId = true;
		} else if (qName.equalsIgnoreCase("SubSectonId")) {
			subSectonId = true;
		} else if (qName.equalsIgnoreCase("Type")) {
			type = true;
		} else if (qName.equalsIgnoreCase("QuestonName1")) {
			questonName1 = true;
		} else if (qName.equalsIgnoreCase("QuestonName1Anser")) {
			questonName1Answer = true;
		} else if (qName.equalsIgnoreCase("QuestonName2")) {
			questonName2 = true;
		} else if (qName.equalsIgnoreCase("QuestonName2Anser")) {
			questonName2Answer = true;
		} else if (qName.equalsIgnoreCase("Status")) {
			status = true;
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		try {
			if (id) {
				unit.setId(Integer.valueOf(data.toString()));
				id = false;
			} else if (sectonId) {
				unit.setSectonId(data.toString());
				sectonId = false;
			} else if (subSectonId) {
				unit.setSubSectonId(data.toString());
				subSectonId = false;
			} else if (type) {
				unit.setType(Integer.valueOf(data.toString()));
				type = false;
			} else if (questonName1) {
				unit.setQuestonName1(data.toString());
				questonName1 = false;
			} else if (questonName1Answer) {
				unit.setQuestonName1Answer(data.toString());
				questonName1Answer = false;
			} else if (questonName2) {
				unit.setQuestonName2(data.toString());
				questonName2 = false;
			} else if (questonName2Answer) {
				unit.setQuestonName2Answer(data.toString());
				questonName2Answer = false;
			} else if (status) {
				unit.setStatus(Integer.valueOf(data.toString()));
				status = false;
			} else if (qName.equalsIgnoreCase("Unit")) {
				// add Question object to list
				unitList.add(unit);
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
