package com.anish.helper;

import com.anish.data.Test2Question;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class Test2AnishHelper {
	
	private String anish_name = "anish";
	private String ishant_name = "ishant";
	
	@Autowired
	private Environment env;
	
	private Gson gson = new Gson();
	
	public void setNewFeatures(ModelAndView modelAndView) {
		List<List<Test2Question>> parseTest201Xml = addEmptyParts();
		modelAndView.addObject("part1Questions", parseTest201Xml.get(0));
		modelAndView.addObject("part2Questions", parseTest201Xml.get(1));
		modelAndView.addObject("part3Questions", parseTest201Xml.get(2));
	}
	private List<List<Test2Question>> addEmptyParts() {
		List<List<Test2Question>> parts = new ArrayList<>(3);
		IntStream.range(0, 3).forEach(i -> parts.add(new ArrayList<>()));
		return parts;
	}
}