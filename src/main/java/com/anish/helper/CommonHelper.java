package com.anish.helper;

import com.anish.data.Test2Question;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class CommonHelper {
	
	@Autowired
	private Environment env;

	@Autowired
	HttpSession session;
	
	private String OS_WINDOWS = "windows";
	private String OS_MAC = "mac";

	private Gson gson = new Gson();
	
	public String getPathByOS(String pathValue) {
		String os = env.getProperty("laptop.os");
		if(OS_WINDOWS.equalsIgnoreCase(os)) {
			//String temp = StringUtils.replace(pathValue, "\\", "\\\\");
			//String finalVal = StringUtils.replace(temp, "/", "\\");
			return pathValue;
		} else if(OS_MAC.equalsIgnoreCase(os)) {
			String finalVal = StringUtils.replace(pathValue, "\\", "/");
			return finalVal;
		}
		return null;
	}
	public List<List<Test2Question>> addEmptyParts() {
		List<List<Test2Question>> parts = new ArrayList<>(3);
		IntStream.range(0, 3).forEach(i -> parts.add(new ArrayList<>()));
		return parts;
	}

	/*
	public void setTestTime(HttpSession session, String testId) {
		String testTimeKey = "sess_"+testId+"_testTime";
		if(session.getAttribute(testTimeKey) == null) {
			session.setAttribute(testTimeKey, LocalDateTime.now().toString());
		}
	}
	*/

	public void setQuestionTime(String testId) {
		String testTimeKey = "sess_"+testId+"_questionTime";
		String testTimeVal = LocalDateTime.now().toString();
		//HttpSession session = request.getSession(true);
		session.setAttribute(testTimeKey, testTimeVal);
		//if(session.getAttribute(testTimeKey) == null) {
		//	session.setAttribute(testTimeKey, LocalDateTime.now().toString());
		//}
	}
	public String getQuestionTime(Integer testId) {
		String testTimeKey = "sess_"+testId+"_questionTime";
		//HttpSession session = request.getSession();
		if(session.getAttribute(testTimeKey) != null) {
			return (String) session.getAttribute(testTimeKey);
		}
		return null;
	}
	/*
	String testTimeKey = "sess_"+testId+"_testTime";
				if(session.getAttribute(testTimeKey) != null) {
		String testTimeVal = (String) session.getAttribute(testTimeKey);
		item.setTestTime(testTimeVal);
	} else {
		item.setTestTime(LocalTime.now().toString());
		session.setAttribute(testTimeKey, LocalTime.now().toString());
	}
	String questionTimeKey = "sess_"+testId+"_questionTime";
				if(session.getAttribute(questionTimeKey) != null) {
		String testTimeVal = (String) session.getAttribute(questionTimeKey);
		item.setTestTime(testTimeVal);
	} else {
		item.setTestTime(LocalTime.now().toString());
		session.setAttribute(questionTimeKey, LocalTime.now().toString());
	}
				*/
}
