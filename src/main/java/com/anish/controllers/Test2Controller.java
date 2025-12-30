package com.anish.controllers;

import com.anish.data.DoubleHalf;
import com.anish.data.Unit;
import com.anish.helper.Test2AnishHelper;
import com.anish.helper.Test2IshantHelper;
import com.anish.parsers.UnitSaxParserHandler;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
public class Test2Controller {
	
	@Autowired private Environment env;

	@Autowired
	private Test2IshantHelper test2IshantHelper;

	@Autowired
	private Test2AnishHelper test2AnishHelper;

	private Gson gson = new Gson();
	
	private final String anish_name = "anish";
	private final String ishant_name = "ishant";

	@GetMapping(value = "/unit/{name}")
    public ModelAndView generateUnitTest(@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("print_test2");
		modelAndView.setStatus(HttpStatus.OK);
		modelAndView.addObject("user_name_key", name);
		modelAndView.addObject("user_name_key_caps", name.toUpperCase());
		try {
			long uniqueId = System.currentTimeMillis();
			
			int totalQuestions = buildUnitData(modelAndView, name);
			int cubeTotalQuestions = cubePrintableData(modelAndView, name);
			
			//List<AlzebraQuestion> alzebraQuestions = alzebraRestController.getCurrentXmlFileAsList(name, 1,"_alzebra_web.xml");
			switch (name) {
				case anish_name:
					test2AnishHelper.setNewFeatures(modelAndView);
					break;
				case ishant_name:
					test2IshantHelper.setNewFeatures(modelAndView);
					break;
			}
			modelAndView.addObject("dataExists", Boolean.TRUE);
			modelAndView.addObject("uniqueId", uniqueId);
			modelAndView.addObject("totalQuestions", (totalQuestions+cubeTotalQuestions));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return modelAndView;
    }
	
	private int buildUnitData(ModelAndView modelAndView, String name) {
		String sourceFile = "_unit_conversions.xml";
		List<Unit> finalList = getCurrentXmlFile(name, 1, sourceFile);
		Collections.shuffle(finalList);
		
		int size = finalList.size();
        int mid = size / 2; // if odd, second gets the extra
        List<Unit> first = new ArrayList<>(finalList.subList(0, mid));
        List<Unit> second = new ArrayList<>(finalList.subList(mid, size));

		modelAndView.addObject("unitData1", first);
		modelAndView.addObject("unitData2", second);
		return finalList.size();
	}
	
	private List<DoubleHalf> printCubes(String name) {
		List<DoubleHalf> rslt = new ArrayList<DoubleHalf>();
		try {
			String doubleVal = env.getProperty(name+".cube");
			String[] doubleValArr = doubleVal.split("#");
			AtomicInteger cnt = new AtomicInteger(1);
			for(int i = Integer.parseInt(doubleValArr[0]); i<Integer.parseInt(doubleValArr[1]) ; i++) {
				DoubleHalf d = new DoubleHalf();
				d.setIsDouble(Boolean.TRUE);
				d.setValue(i);
				d.setChildName(name);
				d.setId(cnt.getAndIncrement());
				rslt.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rslt;
	}
	
	private int cubePrintableData(ModelAndView modelAndView, String name) {
		try {
			List<DoubleHalf> tableData = printCubes(name);
			List<DoubleHalf> data1 = new ArrayList<DoubleHalf>();
			List<DoubleHalf> data2 = new ArrayList<DoubleHalf>();
			List<DoubleHalf> data3 = new ArrayList<DoubleHalf>();
			List<DoubleHalf> data4 = new ArrayList<DoubleHalf>();
			List<DoubleHalf> data5 = new ArrayList<DoubleHalf>();
			List<DoubleHalf> data6 = new ArrayList<DoubleHalf>();
			if(tableData != null && tableData.size()>0) {
				int counter = 1;
				modelAndView.addObject("cubeDataExists", Boolean.TRUE);
				for(int i=0;i<tableData.size();i++) {
					if(counter==1) {
						data1.add(tableData.get(i));
						counter++;
					} else if(counter==2) {
						data2.add(tableData.get(i));
						counter++;
					} else if(counter==3) {
						data3.add(tableData.get(i));
						counter++;
					} else if(counter==4) {
						data4.add(tableData.get(i));
						counter++;
					} else if(counter==5) {
						data5.add(tableData.get(i));
						counter++;
					} else if(counter==6) {
						data6.add(tableData.get(i));
						counter=1;
					}
				}
			} else {
				modelAndView.addObject("cubeDataExists", Boolean.FALSE);
			}
			modelAndView.addObject("cubeData1", data1);
			modelAndView.addObject("cubeData2", data2);
			modelAndView.addObject("cubeData3", data3);
			modelAndView.addObject("cubeData4", data4);
			modelAndView.addObject("cubeData5", data5);
			modelAndView.addObject("cubeData6", data6);
			return tableData.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<Unit> getCurrentXmlFile(String name, int status, String extName) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "xmldata/"+ name + extName;
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if (fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
				UnitSaxParserHandler handler = new UnitSaxParserHandler();
				saxParser.parse(fObj, handler);
				List<Unit> dataList = handler.getQuestionList();
				return dataList.stream().filter(u -> {
					return (u.getStatus()==1);
				}).collect(Collectors.toList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Unit>();
	}
	
}
