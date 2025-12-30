package com.anish.parsers;

import com.anish.data.Word;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadWordSaxParserHandler extends DefaultHandler{

	private List<Word> wordList = null;
	private Word word = null;
	private StringBuilder data = null;
	
	private boolean name;
	private boolean fileNo;
	private boolean sentence;
	private boolean hasSentence;
	private boolean combination;
	private boolean testName;
	private boolean wordPath;
	private boolean sentencePath;
	private boolean ishantWrote;
	private String personName;
	private AtomicInteger counter = new AtomicInteger(0);
	
	private Environment env;
	
	public ReadWordSaxParserHandler(Environment env, String personName) {
		this.env = env;
		this.personName = personName;
	}

	public List<Word> getWordList() {
		return wordList;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Word")) {
			word = new Word();
			word.setPersonName(personName);
			word.setId(counter.incrementAndGet());
			if (wordList == null)
				wordList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("Name")) {
			name = true;
		} else if (qName.equalsIgnoreCase("FileNo")) {
			fileNo = true;
		} else if (qName.equalsIgnoreCase("Sentence")) {
			sentence = true;
		} else if (qName.equalsIgnoreCase("Combination")) {
			combination = true;
		} else if (qName.equalsIgnoreCase("HasSentence")) {
			hasSentence = true;
		} else if (qName.equalsIgnoreCase("TestName")) {
			testName = true;
		} else if (qName.equalsIgnoreCase("WordPath")) {
			wordPath = true;
		} else if (qName.equalsIgnoreCase("SentencePath")) {
			sentencePath = true;
		} else if (qName.equalsIgnoreCase("IshantWrote")) {
			ishantWrote = true;
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (name) {
			word.setName(data.toString());
			name = false;
		} else if (fileNo) {
			String fileNoVal = data.toString();
			word.setFileNo(Integer.valueOf(fileNoVal));
			fileNo = false;
		} else if (sentence) {
			sentence = false;
			word.setSentence(data.toString());
		} else if (combination) {
			combination = false;
			word.setCombination(data.toString());
		} else if (testName) {
			testName = false;
			word.setTestName(data.toString());
		} else if (wordPath) {
			wordPath = false;
			//String pathVal = env.getProperty("destination.path");
			//String wrdPth = pathVal+"audio/redwords/ishant/"+data.toString()+".mp3";
			word.setWordPath(data.toString());
		} else if (sentencePath) {
			sentencePath = false;
			if(!StringUtils.isEmpty(data.toString())) {
				//String pathVal = env.getProperty("destination.path");
				//String wrdPth = pathVal+"audio/redwords/ishant/"+data.toString()+".mp3";
				word.setSentencePath(data.toString());
			}
		} else if (ishantWrote) {
			ishantWrote = false;
			word.setIshantWrote(data.toString());
		} else if (hasSentence) {
			word.setHasSentence(Boolean.valueOf(data.toString()));
			hasSentence = false;
		} else if (qName.equalsIgnoreCase("Word")) {
			
			wordList.add(word);
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
}
