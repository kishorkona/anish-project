package com.anish.helper;

import com.anish.data.Word;
import com.anish.parsers.ReadWordSaxParserHandler;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RedWordsHelper {
	
	private String anish_name = "anish";
	private String ishant_name = "ishant";
	
	@Autowired
	private Environment env;
	
	private Gson gson = new Gson();
	
	public List<Word> getRedWordsFile() {
		List<Word> dataList = new ArrayList<Word>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "read_words.xml";
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if(fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
				ReadWordSaxParserHandler handler = new ReadWordSaxParserHandler(env, ishant_name);
		        saxParser.parse(fObj, handler);
		        dataList = handler.getWordList();
				if(dataList.size()>0) {
					groupData(dataList);
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	public void groupData(List<Word> wordList) {
		wordList.stream().forEach(word -> {
			word.setTotalWords(wordList.size());
		});
	}
	
	public String getWordString(Word word, AtomicInteger counter, String testFileName) {
		String tstName = env.getProperty("ishant.redwords.datafiles."+testFileName);
		StringBuffer sb = new StringBuffer();
		int no = counter.getAndIncrement();
		sb.append("\t<Word>\n");
			sb.append("\t\t<Id>"+no+"</Id>\n");
			sb.append("\t\t<FileNo>"+testFileName+"</FileNo>\n");
			sb.append("\t\t<Name>"+word.getName()+"</Name>\n");
			sb.append("\t\t<HasSentence>"+word.isHasSentence()+"</HasSentence>\n");
			sb.append("\t\t<TestName>"+tstName+"</TestName>\n");
			sb.append("\t\t<Sentence>"+word.getSentence()+"</Sentence>\n");
			sb.append("\t\t<Combination>"+word.getCombination()+"</Combination>\n");
			sb.append("\t\t<WordPath>"+word.getWordPath()+"</WordPath>\n");
			sb.append("\t\t<SentencePath>"+word.getSentencePath()+"</SentencePath>\n");
			sb.append("\t\t<IshantWrote>"+word.getIshantWrote()+"</IshantWrote>\n");
		sb.append("\t</Word>\n");
		return sb.toString();
	}
	
	public void writeQuestonToFile(String xmlData, String fileName, String name) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/writeXml/" + fileName+".xml";
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, xmlData, StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeQuestonToFile(String value, String fileName, String name, StandardOpenOption option) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/writeXml/" + fileName+".xml";
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, value, option);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}