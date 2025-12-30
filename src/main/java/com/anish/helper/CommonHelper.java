package com.anish.helper;

import com.anish.data.Test2Question;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class CommonHelper {
	
	@Autowired
	private Environment env;
	
	private String OS_WINDOWS = "windows";
	private String OS_MAC = "mac";
	
	private Gson gson = new Gson();
	
	public String getPathByOS(String pathValue) {
		String os = env.getProperty("laptop.os");
		if(OS_WINDOWS.equalsIgnoreCase(os)) {
			String temp = StringUtils.replace(pathValue, "\\", "\\\\");
			String finalVal = StringUtils.replace(temp, "/", "\\\\");
			return finalVal;
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
}
