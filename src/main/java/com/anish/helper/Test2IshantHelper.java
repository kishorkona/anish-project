package com.anish.helper;

import com.anish.data.Test2Question;
import com.anish.parsers.Test2QuestionSaxParserHandler;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class Test2IshantHelper {
	
	private String anish_name = "anish";
	private String ishant_name = "ishant";
	String filePath = "xmldata/test2_01.xml";
	
	@Autowired
	private Environment env;

	@Autowired
	CommonHelper commonHelper;
	
	private Gson gson = new Gson();
	
	public void setNewFeatures(ModelAndView modelAndView) {
		List<List<Test2Question>> parts = parseTest201Xml();
		// Always safe: parts guaranteed size 3
		modelAndView.addObject("part1Questions", parts.get(0));
		modelAndView.addObject("part2Questions", parts.get(1));
		modelAndView.addObject("part3Questions", parts.get(2));
	}

	public List<List<Test2Question>> parseTest201Xml() {
		// Always maintain exactly 3 parts
		List<List<Test2Question>> parts = createEmptyParts();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			URL resource = classLoader.getResource(filePath);
			if(resource == null) {
				return parts; // return 3 empty lists if file not found
			}
			File fileObj = new File(resource.getFile());
			if(fileObj.isFile() && fileObj.exists()) {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				Test2QuestionSaxParserHandler handler = new Test2QuestionSaxParserHandler();
				saxParser.parse(fileObj, handler);
				List<Test2Question> dataList = handler.getQuestionList();
				if(dataList != null && !dataList.isEmpty()) {
					List<Test2Question> results = dataList.stream().filter(x -> {
						if(x.getPerson().equalsIgnoreCase(ishant_name) && x.getStatus() == 1) {
							return true;
						} else {
							return false;
						}
					}).collect(Collectors.toList());
					Collections.shuffle(results);
					if(results.size() >= 3) {
						int total = results.size();
						int base = total / 3;
						int rem = total % 3;
						int start = 0;
						for(int i=0;i<3;i++) {
							int chunkSize = base + (i < rem ? 1 : 0);
							int to = Math.min(start + chunkSize, total);
							parts.set(i, new ArrayList<>(results.subList(start, to)));
							start = to;
						}
					} else {
						// Put all available results in first part only (results may be empty)
						parts.set(0, new ArrayList<>(results));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parts;
	}

	private List<List<Test2Question>> createEmptyParts() {
		List<List<Test2Question>> empty = new ArrayList<>(3);
		for(int i=0;i<3;i++) {
			empty.add(new ArrayList<>());
		}
		return empty;
	}

}