package com.anish.controllers;

import com.anish.data.Question;
import com.anish.data.Word;
import com.anish.data.XLData;
import com.anish.helper.CommonHelper;
import com.anish.helper.IXLHelper;
import com.anish.parsers.IXLQuestionSaxParserHandler;
import com.anish.parsers.ReadWordSaxParserHandler;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class IxlController {

	private List<Integer> anishGrade = Arrays.asList("4,5".split(",")).stream().map(x -> Integer.parseInt(x))
			.collect(Collectors.toList());
	private List<Integer> ishantGrade = Arrays.asList("1,2".split(",")).stream().map(x -> Integer.parseInt(x))
			.collect(Collectors.toList());
	private String anish_name = "anish";
	private String ishant_name = "ishant";
	
	private String easyFileName = "easyFileName_done.txt";
	private String needPracticeFileName = "needPracticeFileName_done.txt";
	private String needMorePracticeFileName = "needMorePracticeFileName_done.txt";
	private String doneFileName = "done.txt";

	@Autowired
	private Environment env;
	
	@Autowired
	private CommonHelper commonHelper;
	
	@Autowired private HttpServletRequest httpServletRequest;
	
	@Autowired
	private TableController tableController;

	@Autowired
	IXLHelper ixlHelper;
	
	private Gson gson = new Gson();
	private static String url = "https://www.ixl.com/";

	@GetMapping(value = "/")
	public ModelAndView homepage() {
		ModelAndView model = new ModelAndView("loginpage");
		return model;
	}
	
	@GetMapping(value = "/tests/all")
	public ModelAndView viewAllTests() {
		ModelAndView obj = new ModelAndView("all_topics", HttpStatus.OK);
		return obj;
	}

	@GetMapping(value = "/tests/view/{name}")
	public ModelAndView viewTests(@PathVariable("name") String name) {
		List<Question> dataList = getQuestions(name);
		ModelAndView obj = new ModelAndView("view_tests", HttpStatus.OK);
		if (name.equalsIgnoreCase(anish_name)) {
			obj.addObject("other_name", ishant_name.toUpperCase());
			obj.addObject("other_name_key", ishant_name);
			obj.addObject("user_name_key", anish_name);
		} else if (name.equalsIgnoreCase(ishant_name)) {
			obj.addObject("other_name", anish_name.toUpperCase());
			obj.addObject("other_name_key", anish_name);
			obj.addObject("user_name_key", ishant_name);
		}
		String val = env.getProperty("current.tests."+name);
		List<String> existingTests = Arrays.asList(val.split("#"));
		List<Question> finalDataList = dataList.stream().filter(x1 -> {
			if(existingTests.contains(x1.getQuestionId().toString())) {
				x1.setExistingTests(Boolean.TRUE);
			}
			return true;
		}).collect(Collectors.toList());
		obj.addObject("redWordsExists", "false");
		/*
		if(name.equalsIgnoreCase(ishant_name)) {
			List<Word> redWordsList = getRedWords(name);
			if(redWordsList.size()>0) {
				obj.addObject("redWords", redWordsList);
				obj.addObject("redWordsExists", "true");
			}	
		}
		*/
		//List<TableData> l1 = tableController.getTableSourceData(name);
		//List<TableData> l2 = tableController.getTableSourceDoneFile(name);
		obj.addObject("name", name.toUpperCase());
		obj.addObject("data", finalDataList);
		obj.addObject("tableQuestionLeft", 0);
		return obj;
	}
	
	private boolean resultFileDataExists(String filePath) {
		try {
			Path path = Path.of(commonHelper.getPathByOS(filePath));
			if(path.toFile().exists()) {
				BufferedReader br = new BufferedReader(new FileReader(path.toFile()));
				String st;
				boolean dataExists = Boolean.FALSE;
		        while ((st = br.readLine()) != null) {
		        	if(st != null && !"".equals(st)) {
		        		dataExists = Boolean.TRUE;
		        		break;
					}		      
		        }
				if(dataExists) {
					return Boolean.TRUE;
				}
			}
			return Boolean.FALSE;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	private boolean tablesFileResultFileDataExists(String name) {
		try {
			String pathVal = env.getProperty("destination.path");
			String doneFilePath = pathVal+name+"/clean/table_done_file.txt";
			Path path = Path.of(commonHelper.getPathByOS(doneFilePath));
			if(path.toFile().exists()) {
				BufferedReader br = new BufferedReader(new FileReader(path.toFile()));
				String st;
				boolean dataExists = Boolean.FALSE;
		        while ((st = br.readLine()) != null) {
		        	if(st != null && !"".equals(st)) {
		        		dataExists = Boolean.TRUE;
		        		break;
					}		      
		        }
				if(dataExists) {
					return Boolean.TRUE;
				}
			}
			return Boolean.FALSE;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	private boolean doublesFileResultFileDataExists() {
		try {
			String pathVal = env.getProperty("destination.path");
			String doneFilePath = pathVal+ishant_name+"/clean/double_half_done.txt";	
			Path path = Path.of(commonHelper.getPathByOS(doneFilePath));
			if(path.toFile().exists()) {
				BufferedReader br = new BufferedReader(new FileReader(path.toFile()));
				String st;
				boolean dataExists = Boolean.FALSE;
		        while ((st = br.readLine()) != null) {
		        	if(st != null && !"".equals(st)) {
		        		dataExists = Boolean.TRUE;
		        		break;
					}		      
		        }
				if(dataExists) {
					return Boolean.TRUE;
				}
			}
			return Boolean.FALSE;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	public boolean resultFileDataExists(String testId, String name) {
		try {
			String pathVal = env.getProperty("destination.path");
			String doneFilePath = pathVal+name+"/clean/"+testId+"_done.txt";
			if(resultFileDataExists(doneFilePath)) {
				return Boolean.TRUE;
			}
    		doneFilePath = pathVal+name+"/clean/"+testId+"_easyFileName_done.txt";
    		if(resultFileDataExists(doneFilePath)) {
				return Boolean.TRUE;
			}
    		doneFilePath = pathVal+name+"/clean/"+testId+"_needMorePracticeFileName_done.txt";
    		if(resultFileDataExists(doneFilePath)) {
				return Boolean.TRUE;
			}
    		doneFilePath = pathVal+name+"/clean/"+testId+"_needPracticeFileName_done.txt";
    		if(resultFileDataExists(doneFilePath)) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	@GetMapping(value = "/tests/results/{name}")
	public ModelAndView viewTestResults(@PathVariable("name") String name) {
		ModelAndView obj = new ModelAndView("view_test_results", HttpStatus.OK);
		String nameVal = env.getProperty(name+".datafiles");
		String[] nameValArr = nameVal.split("#");
		List<Question> dataList = new ArrayList<Question>();
		String val = env.getProperty("current.tests."+name);
		List<String> existingTests = Arrays.asList(val.split("#"));
		for(int i=0;i<nameValArr.length;i++) {
			Question q = new Question();
			q.setQuestionId(Integer.parseInt(nameValArr[i]));
			String testName = env.getProperty(name+".datafiles."+q.getQuestionId());
			q.setQuestionName(testName);
			q.setResultsClean(resultFileDataExists(String.valueOf(q.getQuestionId()), name));
			if(existingTests.contains(q.getQuestionId().toString())) {
				q.setExistingTests(Boolean.TRUE);
			}
			dataList.add(q);
		}
		List<Question> finalDataList = dataList.stream().filter(x1 -> {
			if (name.equalsIgnoreCase(anish_name)) {
				obj.addObject("other_name", ishant_name.toUpperCase());
				obj.addObject("other_name_key", ishant_name);
				obj.addObject("user_name_key", anish_name);
				obj.addObject("clean_tables_file", tablesFileResultFileDataExists(name));
			} else if (name.equalsIgnoreCase(ishant_name)) {
				obj.addObject("other_name", anish_name.toUpperCase());
				obj.addObject("other_name_key", anish_name);
				obj.addObject("user_name_key", ishant_name);
				obj.addObject("clean_double_file", doublesFileResultFileDataExists());
				obj.addObject("clean_tables_file", tablesFileResultFileDataExists(name));
			}
			return true;
		}).collect(Collectors.toList());
		if(name.equalsIgnoreCase(ishant_name)) {
			String wordVal = env.getProperty(name+".redwords.datafiles");
			String[] wordValArr = wordVal.split("#");
			List<Word> redWordsList = new ArrayList<Word>();
			for(int i=0;i<wordValArr.length;i++) {
				Word w = new Word();
				w.setFileNo(Integer.parseInt(wordValArr[i]));
				String testName = env.getProperty(name+".redwords.datafiles."+w.getFileNo());
				w.setTestName(testName);
				List<Word> completedList = getRedWordsCompletedFile(String.valueOf(w.getFileNo()));
				if(completedList.size()>0) {
					w.setTotalWords(completedList.size());
					redWordsList.add(w);
				}
			}
			if(redWordsList.size()>0) {
				obj.addObject("redWords", redWordsList);
				obj.addObject("redWordsExists", "true");
			}	
		}
		long uniqueId = System.currentTimeMillis();
		obj.addObject("uniqueId", uniqueId);
		obj.addObject("name", name.toUpperCase());
		obj.addObject("data", finalDataList);
		return obj;
	}

	public Map<Integer, XLData> getDataNew(String name) {
		Map<Integer, XLData> finalData = new HashMap<Integer, XLData>();
		String propValue = env.getProperty(name+".datafiles");
		String[] arr = propValue.split("#"); 
		for (int i = 0; i < arr.length; i++) {
			String val = env.getProperty(name+".datafiles."+arr[i]);
			XLData xlData = new XLData();
			xlData.setQuestionId(Integer.parseInt(arr[i]));
			xlData.setQuestionFileName(arr[i]+".xml");
			xlData.setQuestionName(val.trim());
			xlData.setQuestionCompletedFileName(arr[i]+"_done.txt");
			finalData.put(Integer.parseInt(arr[i]), xlData);
		}
		return finalData;
	}
	
	public List<Question> getQuestions(String name) {
		List<Question> dataList = new ArrayList<Question>();
		Map<Integer, XLData> finalData = getDataNew(name);
		finalData.values().forEach(xlData -> {
			try {
				List<Question> tempList = getAllRecordsNew(xlData, name);
				if (!tempList.isEmpty()) {
					List<Question> finalTempList = tempList.stream().filter(x -> {
						return true;
					}).collect(Collectors.toList());
					Question item = tempList.get(0);
					item.setTotalCurrentQuestions(finalTempList.size());
					item.setQuestionId(xlData.getQuestionId());
					item.setQuestionName(xlData.getQuestionName());
					dataList.add(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return dataList;
	}
	
	public List<Question> getAllRecordsNew(XLData xlData, String name) {
		List<Question> srcData = getQuestionListByStatusNew(xlData, 1);
		List<String> existingIds = getCompletedDataByName(xlData.getQuestionCompletedFileName(), name);
		List<Question> finalLines = new ArrayList<Question>();
		srcData.stream().forEach(x -> {
			if (!existingIds.contains(x.getSectionId() + "#" + x.getSubSectionId())) {
				finalLines.add(x);
			}
		});
		return finalLines;
	}
	
	public List<Question> getQuestionListByStatusNew(XLData xlData, int stauts) {
		return getCurrentXmlFile(xlData.getQuestionFileName(), "xmldata").stream().filter(x -> {
			if (x.getQuestionStatus().intValue() == stauts) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
	}
	public List<Question> getQuestionListByAll(XLData xlData) {
		return getCurrentXmlFile(xlData.getQuestionFileName(), "xmldata");
	}
	
	@GetMapping(value = "/tests/getTestLink/{testId}/{name}")
	public ModelAndView getTestLink(@PathVariable("testId") String testId, @PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);
			List<Question> finalLines = getAllRecords(val + "#" + valDone, name, testId);
			if(finalLines.size()>0) {
				Collections.shuffle(finalLines);
				Question item = finalLines.get(0);
				item.setTotalCurrentQuestions(finalLines.size());
				item.setQuestionId(Integer.valueOf(testId));
				String testIdVal = env.getProperty(name+".datafiles."+testId);
				item.setQuestionName(testIdVal.trim());
				item.setUserName(name);
				modelAndView.addObject("question", item);
			}
			/*
			Map<Integer, Question> linesMap = new HashMap<Integer, Question>();
			for(Question q: finalLines) {
				linesMap.put(q.getId(), q);
			}
			if (linesMap.size() > 0) {
				Random rand = new Random();
			    int randomElement = linesMap.keySet().stream().collect(Collectors.toList()).get(rand.nextInt(finalLines.size()));
			    Question item = linesMap.get(0);
				item.setTotalCurrentQuestions(finalLines.size());
				item.setQuestionId(Integer.valueOf(testId));
				String testIdVal = env.getProperty(name+".datafiles."+testId);
				item.setQuestionName(testIdVal.trim());
				item.setUserName(name);
				modelAndView.addObject("question", item);
			}
			*/
			modelAndView.addObject("user_name_key", name);
			modelAndView.addObject("testResetFlag", Boolean.TRUE);
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("ixl_test");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	public List<Question> getAllRecords(String fileName, String name, String testId) {
		String[] fileArr = fileName.split("#");
		List<Question> srcData = getQuestionListByStatus(fileName, 1, testId, name);
		List<String> existingIds = getCompletedDataByName(fileArr[1], name);
		List<Question> finalLines = new ArrayList<Question>();
		srcData.stream().forEach(x -> {
			if (!existingIds.contains(x.getSectionId() + "#" + x.getSubSectionId())) {
				finalLines.add(x);
			}
		});
		return finalLines;
	}
	
	public String getDoneFile(String testId) {
		return testId+"_done.txt";
	}
	
	public String getXmlFile(String testId) {
		return testId+".xml";
	}
	
	@RequestMapping(value = "/tests/nextQuestion", method = RequestMethod.POST)
	public ModelAndView nextQuestion(@ModelAttribute("question") Question questionReq) {
		ModelAndView modelAndView = new ModelAndView("ixl_test");
		try {
			String val = getXmlFile(String.valueOf(questionReq.getQuestionId()));
			String valDone = getDoneFile(String.valueOf(questionReq.getQuestionId()));
			Question question = getCurrentQuestionNew(val + "#" + valDone, questionReq.getSectionId(), questionReq.getSubSectionId(), questionReq.getUserName(), String.valueOf(questionReq.getQuestionId()));
			String testIdVal = env.getProperty(questionReq.getUserName()+".datafiles."+String.valueOf(question.getQuestionId()));
			question.setQuestionName(testIdVal.trim());
			writeRecordToFile(val, question, questionReq.getUserName());
			String selectFiles = env.getProperty("select.files");
			if(!"0".equalsIgnoreCase(selectFiles)) {
				writeQuestionTypeStatusToFile(val, question, questionReq.getUserName(), questionReq.getQuestionType());
			}

			List<Question> finalLines = getAllRecordsByQuestion(val + "#" + valDone, question, questionReq.getUserName(), String.valueOf(questionReq.getQuestionId()));
			if(finalLines.size()>0) {
				Collections.shuffle(finalLines);
				Question item = finalLines.get(0);			    
				item.setTotalCurrentQuestions(finalLines.size());
				item.setQuestionId(questionReq.getQuestionId());
				item.setQuestionName(testIdVal.trim());
				item.setUserName(questionReq.getUserName());
				modelAndView.addObject("question", item);
			}
			
			/*
			Map<Integer, Question> linesMap = new HashMap<Integer, Question>();
			for(Question q: finalLines) {
				linesMap.put(q.getId(), q);
			}
			if (linesMap.size() > 0) {
				Random rand = new Random();
			    int randomElement = linesMap.keySet().stream().collect(Collectors.toList()).get(rand.nextInt(finalLines.size()));
			    Question item = linesMap.get(randomElement);			    
				item.setTotalCurrentQuestions(finalLines.size());
				item.setQuestionId(questionReq.getQuestionId());
				item.setQuestionName(testIdVal.trim());
				item.setUserName(questionReq.getUserName());
				modelAndView.addObject("question", item);
			}
			*/
			modelAndView.addObject("user_name_key", questionReq.getUserName());
			modelAndView.setStatus(HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	public Question getCurrentQuestionNew(String fileName, String sectionId, String subSectionId, String name, String testId) {
		Question[] question = new Question[1];
		Question[] x = new Question[1];
		try {
			List<Question> srcData = getQuestionListByStatus(fileName, 1, testId, name);
			srcData.stream().forEach(x1 -> {
				x[0] = x1;
				if (x[0].getSectionId().equals(sectionId) && x[0].getSubSectionId().equals(subSectionId)) {
					question[0] = x[0];
				}
			});	
		} catch (Exception e) {
			System.out.println("question="+gson.toJson(x[0]));
			e.printStackTrace();
			throw e;
		}
		return question[0];
	}
	
	public List<Question> getAllRecordsByQuestion(String fileName, Question question, String name, String testId) {
		String[] fileArr = fileName.split("#");
		List<Question> srcData = getQuestionListByStatus(fileName, 1, testId, name);
		List<String> existingIds = getCompletedDataByName(fileArr[1], name);
		List<Question> finalLines = new ArrayList<Question>();
		srcData.stream().forEach(x -> {
			String key = x.getSectionId() + "#" + x.getSubSectionId();
			if (!existingIds.contains(key)) {
				finalLines.add(x);
			}
		});
		return finalLines;
	}
	
	@GetMapping(value = "/tests/disabled/{name}")
	public ModelAndView viewDisabledTests(@PathVariable("name") String name) {
		List<Question> dataList = getDisabledData(name);
		ModelAndView obj = new ModelAndView("view_disabled_tests", HttpStatus.OK);
		obj.addObject("user_name_key", name);
		obj.addObject("data", dataList);
		return obj;
	}
	
	public List<Question> getDisabledData(String name) {
		List<Question> dataList = new ArrayList<Question>();
		Map<Integer, XLData> finalData = getDataNew(name);
		finalData.values().forEach(x -> {
			try {
				Question question = new Question();
				List<Question> tempList = getQuestionListByStatusNew(x, 1);
				List<Question> tempComplexList = getQuestionListByStatusNew(x, 0);
				List<Question> tempCompletedList = getQuestionListByStatusNew(x, 2);
				List<Question> tempPracticeList = getQuestionListByStatusNew(x, 3);
				if (!tempComplexList.isEmpty()) {
					question = tempComplexList.get(0);
				} else if (!tempCompletedList.isEmpty()) {
					question = tempCompletedList.get(0);
				} else if (!tempList.isEmpty()) {
					question = tempList.get(0);
				}
				if (!tempComplexList.isEmpty()) {
					question.setTotalComplexQuestions(tempComplexList.size());
				}
				if (!tempCompletedList.isEmpty()) {
					question.setTotalCompletedQuestions(tempCompletedList.size());
				}
				if (!tempList.isEmpty()) {
					question.setTotalCurrentQuestions(tempList.size());
				}
				if (!tempPracticeList.isEmpty()) {
					question.setTotalPracticeQuestions(tempPracticeList.size());
				}
				question.setQuestionId(x.getQuestionId());
				question.setQuestionName(x.getQuestionName());
				dataList.add(question);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return dataList;
	}

	@GetMapping(value = "/tests/currentQuestions/{testId}/{personName}")
	public ModelAndView getCurrentQuestions(@PathVariable("testId") String testId, @PathVariable("personName") String personName) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 1, testId, personName);
			List<Question> finalLines = sortData.apply(tempLines);
			if (finalLines.size() > 0) {
				for(Question q: finalLines) {
					if(q.getResultsUrl()!=null && !q.getResultsUrl().equals("")) {
						q.setResultsAvailable(Boolean.TRUE);
						q.setResultsUrl("https://www.ixl.com/analytics/questions-log#skill="+q.getResultsUrl()+"&skillPlanSelected=false");
					}
				}
				modelAndView.addObject("data", finalLines);
			}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.addObject("name", personName.toUpperCase());
			modelAndView.setViewName("completed_questions");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	public List<Question> getQuestionListByStatus(String fileName, int stauts, String testId, String personName) {
		String[] fileArr = fileName.split("#");
		return getCurrentXmlFile(fileArr[0], "xmldata").stream().filter(x -> {
			x.setQuestionId(Integer.parseInt(testId));
			String testIdVal = env.getProperty(personName+".datafiles."+testId);
			x.setQuestionName(testIdVal);
			if (x.getQuestionStatus().intValue() == stauts) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
	}
	
	@GetMapping(value = "/tests/complexQuestions/{testId}/{personName}")
	public ModelAndView getComplexQuestions(@PathVariable("testId") String testId, @PathVariable("personName") String personName) {
		ModelAndView modelAndView = new ModelAndView("complex_questions", HttpStatus.OK);
		modelAndView.addObject("name", personName.toUpperCase());
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 0, testId, personName);
			List<Question> finalLines = sortData.apply(tempLines);
			if (finalLines.size() > 0) {
				for(Question q: finalLines) {
					if(q.getResultsUrl()!=null && !q.getResultsUrl().equals("")) {
						q.setResultsAvailable(Boolean.TRUE);
						q.setResultsUrl("https://www.ixl.com/analytics/questions-log#skill="+q.getResultsUrl()+"&skillPlanSelected=false");
					} else {
						q.setResultsAvailable(Boolean.FALSE);
					}
				}
				modelAndView.addObject("data", finalLines);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	@GetMapping(value = "/tests/completedQuestions/{testId}/{personName}")
	public ModelAndView getCompletedQuestionsRestCall(@PathVariable("testId") String testId, @PathVariable("personName") String personName) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 2, testId, personName);
			List<Question> finalLines = sortData.apply(tempLines);
			if (finalLines.size() > 0) {
				modelAndView.addObject("data", finalLines);
			}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.addObject("name", personName.toUpperCase());
			modelAndView.setViewName("completed_questions");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	@GetMapping(value = "/tests/practiceQuestions/{testId}/{personName}")
	public ModelAndView getPracticeQuestions(@PathVariable("testId") String testId, @PathVariable("personName") String personName) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 3, testId, personName);
			List<Question> finalLines = sortData.apply(tempLines);
			if (finalLines.size() > 0) {
				for(Question q: finalLines) {
					if(q.getResultsUrl()!=null && !q.getResultsUrl().equals("")) {
						q.setResultsAvailable(Boolean.TRUE);
						q.setResultsUrl("https://www.ixl.com/analytics/questions-log#skill="+q.getResultsUrl()+"&skillPlanSelected=false");
					} else {
						q.setResultsAvailable(Boolean.FALSE);
					}
				}
				modelAndView.addObject("data", finalLines);
			}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.addObject("name", personName.toUpperCase());
			modelAndView.setViewName("practice_questions");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	public List<String> getCompletedDataByName(String fileName, String name) {
		List<String> lines = new ArrayList<String>();
		Scanner myReader = null;
		String val = null;
		try {
			String path = env.getProperty("destination.path");
			String finalPath = path+name+"/clean/" + fileName;
			String yy = commonHelper.getPathByOS(finalPath);
			System.out.println("file path to read="+yy);
			File myObj = new File(yy);
			myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				val = myReader.nextLine();
				if (val != null && val.length() > 10) {
					Question q = gson.fromJson(val, Question.class);
					lines.add(q.getSectionId() + "#" + q.getSubSectionId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (myReader != null) {
					myReader.close();
				}
			} finally {
			}
		}
		return lines;
	}
	
	public List<Question> getCurrentXmlFile(String fileName, String folderName) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = folderName+"/" + fileName;
			//String finalFile = commonHelper.getPathByOS(filePath);
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if (fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
				IXLQuestionSaxParserHandler handler = new IXLQuestionSaxParserHandler();
				saxParser.parse(fObj, handler);
				List<Question> dataList = handler.getQuestionList();
				if (dataList.size() > 0) {
					return sortItems(dataList, dataList.size());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
	
	public List<Question> getCurrentXmlFileWithCounter(String fileName, String folderName) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = folderName+"/" + fileName;
			URL resource = classLoader.getResource(commonHelper.getPathByOS(filePath));
			File fObj = new File(resource.getFile());
			if (fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
				IXLQuestionSaxParserHandler handler = new IXLQuestionSaxParserHandler(new AtomicInteger(0));
				saxParser.parse(fObj, handler);
				List<Question> dataList = handler.getQuestionList();
				if (dataList.size() > 0) {
					return sortItems(dataList, dataList.size());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
	
	public void writeRecordToFile(String fileName, Question question, String name) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/clean/" + fileName.replace(".xml", "_done.txt");
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, "\n" + gson.toJson(question), StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void writeQuestionTypeStatusToFile(String fileName, Question question, String name, int questionType) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = null;
			if(questionType==1) {
				finalFilePath = path+name+"/clean/" + fileName.replace(".xml", "")+"_"+needPracticeFileName;
			} else if(questionType==2) {
				finalFilePath = path+name+"/clean/" + fileName.replace(".xml", "")+"_"+easyFileName;
			} else if(questionType==3) {
				finalFilePath = path+name+"/clean/" + fileName.replace(".xml", "")+"_"+needMorePracticeFileName;
			} else if(questionType==0) {
				finalFilePath = path+name+"/clean/" + fileName.replace(".xml", "")+"_"+doneFileName;
			}
			Path filePath = Path.of(finalFilePath);
			String data = question.getGrade()+"|"+question.getUrlKey();
			Files.writeString(filePath, "\n" + data, StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Function<List<Question>, List<Question>> sortData = (List<Question> data) -> {
		Map<String, List<Question>> groupBySubject = data.stream()
				.collect(Collectors.groupingBy(Question::getSectionId));
		List<String> sortedQuestions = groupBySubject.keySet().stream().sorted().collect(Collectors.toList());
		Map<String, List<Question>> finalListData = new HashMap<String, List<Question>>();
		for(String key: sortedQuestions) {
			finalListData.put(key, groupBySubject.get(key));
		}
		List<Question> finalLines = new ArrayList<Question>();
		finalListData.entrySet().stream().forEach(x -> {
			x.getValue().stream().forEach(y -> {
				finalLines.add(y);
			});
		});
		return finalLines;
	};
	
	private List<Question> sortItems(List<Question> dataList, Integer totalQuestions) {
		List<Question> finalItems = new ArrayList<Question>();
		List<Integer> excludeList = new ArrayList<Integer>();
		Map<String, List<Question>> groupBySubject = dataList.stream()
				.collect(Collectors.groupingBy(Question::getSectionId));
		boolean flag = true;
		while (flag) {
			for (Entry<String, List<Question>> entry : groupBySubject.entrySet()) {
				for (Question item : entry.getValue()) {
					if (finalItems.size() == dataList.size()) {
						flag = false;
						break;
					} else {
						if (!excludeList.contains(item.getId())) {
							// item.setTotalRows(totalQuestions);
							item.setUrl(url + getPathSubject(item.getSubject()).toLowerCase() + "/grade-"
									+ item.getGrade() + "/" + item.getUrlKey());
							finalItems.add(item);
							excludeList.add(item.getId());
							break;
						}
					}
				}
				if (finalItems.size() == dataList.size()) {
					flag = false;
					break;
				}
			}
		}
		return finalItems;
	}
	
	public String getPathSubject(String subject) {
		if (subject.equalsIgnoreCase("language arts")) {
			return "ela";
		} else if (subject.equalsIgnoreCase("math")) {
			return "math";
		}
		return null;
	}
	
	public List<Word> getRedWords(String personName) {
		List<Word> dataList = new ArrayList<Word>();
		String fileStr = env.getProperty("ishant.redwords.datafiles");
		String[] fileStrArr = fileStr.split("#");
		for(int i=0;i<fileStrArr.length;i++) {
			List<Word> currentList = finalRedWords(fileStrArr[i], personName);
			if(currentList != null && currentList.size()>0) {
				Word wrd = currentList.get(0);
				wrd.setTotalWords(currentList.size());
				dataList.add(wrd);
			}
		}
		return dataList;
	}
	
	public List<Word> getRedWordsFile(String fileName, String personName) {
		List<Word> dataList = new ArrayList<Word>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "xmldata/"+fileName+".xml";
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if(fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
				ReadWordSaxParserHandler handler = new ReadWordSaxParserHandler(env, personName);
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
	
	public List<Word> getRedWordsCompletedFile(String fileName) {
		List<Word> dataList = new ArrayList<Word>();
		Scanner myReader = null;
		String val = null;
		try {
			String path = env.getProperty("destination.path");
			String doneFilePath = path+ishant_name+"/clean/"+fileName+"_done.txt";
			File myObj = new File(doneFilePath);
			myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				val = myReader.nextLine();
				if (val != null && val.length() > 10) {
					Word q = gson.fromJson(val, Word.class);
					dataList.add(q);
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (myReader != null) {
					myReader.close();
				}
			} finally {
			}
		}
		return dataList;
	}
	
	public List<Word> finalRedWords(String fileName, String personName) {
		List<Word> dataList = new ArrayList<Word>();
		try {
			List<Word> completedRedWrods = getRedWordsCompletedFile(fileName);
			if(completedRedWrods != null && completedRedWrods.size()>0) {
				List<Integer> completedIds = new ArrayList<Integer>();
				for(Word wrd: completedRedWrods) {
					completedIds.add(wrd.getId());
				}
				List<Word> redWrods = getRedWordsFile(fileName, personName);
				for(Word wrd: redWrods) {
					if(!completedIds.contains(wrd.getId())) {
						dataList.add(wrd);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	public List<Question> getCompletedQuestions(String fileName, String name) {
		List<Question> lines = new ArrayList<Question>();
		Scanner myReader = null;
		String val = null;
		try {
			String path = env.getProperty("destination.path");
			String finalPath = path+name+"/clean/" + fileName;
			File myObj = new File(finalPath);
			myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				val = myReader.nextLine();
				if (val != null && val.length() > 10) {
					Question q = gson.fromJson(val, Question.class);
					lines.add(q);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (myReader != null) {
					myReader.close();
				}
			} finally {
			}
		}
		return lines;
	}
	public List<Question> getAllCompletedQuestions(String name) {
		List<Question> dataList = new ArrayList<Question>();
		String dataFilesVal = env.getProperty("anish.datafiles");
		String[] dataFilesArr = dataFilesVal.split("#");
		for(int i=0;i<=dataFilesArr.length;i++) {
			List<Question> tempList = getCompletedQuestions(dataFilesArr[i], name);
			if(tempList.size()>0) {
				Question finQuestion = tempList.get(0);
				finQuestion.setTotalCompletedQuestions(tempList.size());
			}
		}
		return dataList;
	}
	
	@GetMapping(value = "/ixl/test/results/{testId}/{name}")
	public ModelAndView getIxlTestResults(@PathVariable("testId") String testId, @PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("ixl_results_view");
		try {
			XLData xlData = new XLData();
			xlData.setQuestionFileName(testId+".xml");
			List<Question> dataList = getQuestionListByAll(xlData);
			List<Question> finalDataList = new ArrayList();
			for(Question q: dataList) {
				if(q.getQuestionStatus()!=0) {
					String fileName = testId+"_"+q.getGrade()+"_"+q.getSectionId()+"_"+q.getSubSectionId();
					Question qstn = getQuestionFromFile(name, fileName, testId);
					if(qstn != null) {
						q.setVerified(qstn.getVerified());
						q.setStatus(qstn.getStatus());
					} else {
						q.setVerified("0");
						q.setStatus("0");
					}
					if(q.getResultsUrl()!=null && !q.getResultsUrl().equals("")) {
						q.setResultsAvailable(Boolean.TRUE);
						q.setResultsUrl("https://www.ixl.com/analytics/questions-log#skill="+q.getResultsUrl()+"&skillPlanSelected=false");
					} else {
						q.setResultsAvailable(Boolean.FALSE);
					}
					q.setQuestionId(Integer.valueOf(testId));
					q.setQuestionName(fileName);
					finalDataList.add(q);
				} else {
					System.out.println("difficult question:"+q.toString());
				}
			}
			if (finalDataList.size() > 0) {
				modelAndView.addObject("data", finalDataList);
			}
			modelAndView.addObject("user_name_key", name);
			modelAndView.setStatus(HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	@GetMapping(value = "/ixl/test/resultsVerify/{testId}/{name}")
	public ModelAndView getIxlTestResultsVerify(@PathVariable("testId") String testId, @PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("ixl_results_view_verify");
		try {
			XLData xlData = new XLData();
			xlData.setQuestionFileName(testId+".xml");
			List<Question> dataList = getQuestionListByAll(xlData);
			List<Question> finalDataList = new ArrayList<Question>();
			for(Question q: dataList) {
				String fileName = testId+"_"+q.getGrade()+"_"+q.getSectionId()+"_"+q.getSubSectionId();
				Question qstn = getQuestionFromFile(name, fileName, testId);
				if(qstn == null && q.getQuestionStatus() != 0) {
					q.setVerified("0");
					q.setStatus("0");
					if(q.getResultsUrl()!=null && !q.getResultsUrl().equals("")) {
						q.setResultsAvailable(Boolean.TRUE);
						q.setResultsUrl("https://www.ixl.com/analytics/questions-log#skill="+q.getResultsUrl()+"&skillPlanSelected=false");
					} else {
						q.setResultsAvailable(Boolean.FALSE);
					}
					q.setQuestionId(Integer.valueOf(testId));
					q.setQuestionName(fileName);
					finalDataList.add(q);
				}
			}
			if (finalDataList.size() > 0) {
				modelAndView.addObject("data", finalDataList);
			}
			modelAndView.addObject("user_name_key", name);
			modelAndView.setStatus(HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}

	private void reCreateFile(String filePath) {
		try {
			Path path = Path.of(commonHelper.getPathByOS(filePath));
			System.out.println("delete file path="+path.toString());
			deleteIfExists(path);
			createAgain(path);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	private void deleteIfExists(Path path) {
		try {
			Files.deleteIfExists(path);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	private void createAgain(Path path) {
		try {
    		Files.writeString(path, "\n", StandardOpenOption.CREATE_NEW);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	@GetMapping(value = "/ixl/test/clean/{testId}/{name}")
	public ModelAndView cleanFolders(@PathVariable("testId") String testId, @PathVariable("name") String name) {
		boolean rslt = ixlHelper.deleteAndCreateFiles(testId, name);
		System.out.println("clean folders result="+rslt);
		if(rslt) {
			return viewTestResults(name);
		}
		throw new RuntimeException("error cleanFolders");
	}
	
	@GetMapping(value = "/ixl/test/cleanOther/{otherKey}/{name}")
	public ModelAndView cleanOtherFolders(@PathVariable("otherKey") String otherKey, @PathVariable("name") String name) {
		try {
			String pathVal = env.getProperty("destination.path");
			if(otherKey.equalsIgnoreCase("double")) {
				String doneFilePath = pathVal+name+"/clean/double_half_done.txt";
				reCreateFile(doneFilePath);
			} else if(otherKey.equalsIgnoreCase("table")) {
				String doneFilePath = pathVal+name+"/clean/table_done_file.txt";
				reCreateFile(doneFilePath);
			}			
			return viewTestResults(name);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	@GetMapping(value = "/ixl/updateResultStatus/{testId}/{name}/{statusId}/{id}")
	public ModelAndView updateResultStatus(@PathVariable("testId") String testId, @PathVariable("name") String name, 
			@PathVariable("statusId") String statusId, @PathVariable("id") String id) {
		try {
			XLData xlData = new XLData();
			xlData.setQuestionFileName(testId+".xml");
			List<Question> dataList = getQuestionListByAll(xlData);
			Question finalQuestion = null;
			for(Question q: dataList) {
				if(q.getId()==Integer.parseInt(id)) {
					finalQuestion = q;
					finalQuestion.setVerified("1");
					finalQuestion.setStatus(statusId);
					break;
				}
			}
			if(finalQuestion != null) {
				writeQuestionStatusToFile(finalQuestion, name, testId);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return getIxlTestResultsVerify(testId, name);
	}
	
	@GetMapping(value = "/ixl/updateResultStatusVerify/{testId}/{name}/{statusId}/{id}")
	public ModelAndView updateResultStatusVerify(@PathVariable("testId") String testId, @PathVariable("name") String name, 
			@PathVariable("statusId") String statusId, @PathVariable("id") String id) {
		try {
			XLData xlData = new XLData();
			xlData.setQuestionFileName(testId+".xml");
			List<Question> dataList = getQuestionListByAll(xlData);
			Question finalQuestion = null;
			for(Question q: dataList) {
				if(q.getId()==Integer.parseInt(id)) {
					finalQuestion = q;
					finalQuestion.setVerified("1");
					finalQuestion.setStatus(statusId);
					break;
				}
			}
			if(finalQuestion != null) {
				writeQuestionStatusToFile(finalQuestion, name, testId);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return getIxlTestResultsVerify(testId, name);
	}
	
	public void writeQuestionStatusToFile(Question question, String personName, String fileNo) {
    	try {   
    		String fileName = fileNo+"_"+question.getGrade()+"_"+question.getSectionId()+"_"+question.getSubSectionId();
    		String pathVal = env.getProperty("destination.path");
    		String doneFilePath = pathVal+personName+"/ixl_results/"+fileNo+"/"+fileName+".data";
    		Path path = Path.of(commonHelper.getPathByOS(doneFilePath));
    		Files.deleteIfExists(path);
    		Files.writeString(path, "\n"+gson.toJson(question), StandardOpenOption.CREATE_NEW);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
	/*
	public void writeQuestionStatusToFile(List<Question> finalList, String personName, String fileNo) {
    	try {   
    		String pathVal = env.getProperty("destination.path");
    		String doneFilePath = pathVal+personName+"/ixl_results/"+fileNo+"_done.txt";
    		Path path = Path.of(commonHelper.getPathByOS(doneFilePath));
    		Files.deleteIfExists(path);
    		boolean flag = true;
    		for(Question q: finalList) {
    			if(flag) {
    				Files.writeString(path, "\n"+gson.toJson(q), StandardOpenOption.CREATE_NEW);
    				flag = false;
    			} else {
    				Files.writeString(path, "\n"+gson.toJson(q), StandardOpenOption.APPEND);
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
	*/
	public Boolean questionStatusFileExists(String personName, String fileNo) {
    	try {   
    		String pathVal = env.getProperty("destination.path");
    		String doneFilePath = pathVal+personName+"/ixl_results/"+fileNo+".data";
    		Path path = Path.of(commonHelper.getPathByOS(doneFilePath));
    		return Boolean.valueOf(path.toFile().exists());
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	return null;
    }
	public Question readQuestionStatusFromFile(String name, String fileName) {
		Boolean questionStatusFlagFileExists = questionStatusFileExists(name, fileName);
		Question q = null;
		if(questionStatusFlagFileExists != null && questionStatusFlagFileExists.booleanValue()) {
			String path = env.getProperty("destination.path");
			String doneFilePath = path+name+"/ixl_results/"+fileName+"_done.data";
			Scanner myReader = null;
			String val = null;
	    	try {
	    		File myObj = new File(commonHelper.getPathByOS(doneFilePath));
	    		myReader = new Scanner(myObj);
	    		while (myReader.hasNextLine()) {
	    			val = myReader.nextLine();
	    			if(!StringUtils.isEmpty(val) && val.length()>0) {
	    				q = gson.fromJson(val, Question.class);
	    			}
	    		}
	    	} catch (Exception e) {
	    		System.out.println("Error at Row="+val);
				e.printStackTrace();
				return null;
			} finally {
				try {
					if(myReader!=null) {
						myReader.close();
					}
				} finally {}
			}
		}
    	return q;
    }
	
	public Question getQuestionFromFile(String name, String fileName, String finalFolderName) {
		String pathVal = env.getProperty("destination.path");
		String doneFilePath = pathVal+name+"/ixl_results/"+finalFolderName+"/"+fileName+".data";
		Path path = Path.of(commonHelper.getPathByOS(doneFilePath));
		if(path.toFile().exists()) {
			Question q = null;
			Scanner myReader = null;
			String val = null;
	    	try {
	    		File myObj = new File(commonHelper.getPathByOS(doneFilePath));
	    		myReader = new Scanner(myObj);
	    		while (myReader.hasNextLine()) {
	    			val = myReader.nextLine();
	    			if(!StringUtils.isEmpty(val) && val.length()>0) {
	    				q = gson.fromJson(val, Question.class);
	    			}
	    		}
	    	} catch (Exception e) {
	    		System.out.println("Error at Row="+val);
				e.printStackTrace();
				return null;
			} finally {
				try {
					if(myReader!=null) {
						myReader.close();
					}
				} finally {}
			}
	    	return q;
		} else {
			return null;
		}
    }
	
	@GetMapping(value = "/sets/tests/view/{name}")
	public ModelAndView viewSetTests(@PathVariable("name") String name) {
		List<Question> dataList = getSetQuestions(name);
		ModelAndView obj = new ModelAndView("sets_view_tests", HttpStatus.OK);
		if (name.equalsIgnoreCase(anish_name)) {
			obj.addObject("other_name", ishant_name.toUpperCase());
			obj.addObject("other_name_key", ishant_name);
			obj.addObject("user_name_key", anish_name);
		} else if (name.equalsIgnoreCase(ishant_name)) {
			obj.addObject("other_name", anish_name.toUpperCase());
			obj.addObject("other_name_key", anish_name);
			obj.addObject("user_name_key", ishant_name);
		}
		obj.addObject("data", dataList);
		return obj;
	}
	
	private Map<String, Integer> getAllSets(String name) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for(int i=1;i<=20;i++) {
			String val = env.getProperty(name+".set."+i);
			if(val != null) {
				map.put(val, i);
			}
		}
		return map;
	}
	public List<Question> getSetQuestions(String name) {
		List<Question> dataList = new ArrayList<Question>();
		
		List<Question> tempList = new ArrayList<Question>();
		getDataFilesForCurrentGrade(name).stream().forEach(fileNo -> {
			List<Question> myQuestions = getCurrentXmlFile(fileNo+".xml", "xmldata").stream().filter(x -> {
				if (x.getQuestionStatus().intValue() == 1) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			tempList.addAll(myQuestions);
		});
		Map<String, Integer> allSets = getAllSets(name);
		allSets.entrySet().stream().forEach(entry -> {
			Question item = new Question();
			List<Question> tempList1 = tempList.stream().filter(x -> {
				if (x.getSetValue().contains(String.valueOf(entry.getValue()))) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			item.setQuestionName(entry.getKey());
			item.setQuestionId(entry.getValue());
			item.setTotalCurrentQuestions(tempList1.size());
			dataList.add(item);
		});
		return dataList;
	}
	
	public List<Question> getSetAllQuestions(String name) {
		List<Question> dataList = new ArrayList<Question>();
		getDataFilesForCurrentGrade(name).stream().forEach(fileNo -> {
			List<Question> myQuestions = getCurrentXmlFile(fileNo+".xml", "xmldata").stream().filter(x -> {
				if (x.getQuestionStatus().intValue() == 1) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			dataList.addAll(myQuestions);
		});
		return dataList;
	}
	
	private List<String> getDataFilesForCurrentGrade(String name) {
		if(anish_name.equals(name)) {
			String filesVal = env.getProperty("anish.datafiles");
			return Arrays.asList(filesVal.split("#"));
		} else {
			String filesVal = env.getProperty("ishant.datafiles");
			return Arrays.asList(filesVal.split("#"));
		}
	}
	public Map<Integer, XLData> getSetDataNew(String name) {
		Map<Integer, XLData> finalData = new HashMap<Integer, XLData>();
		String propValue = env.getProperty(name+".datafiles");
		String[] arr = propValue.split("#"); 
		for (int i = 0; i < arr.length; i++) {
			String val = env.getProperty(name+".datafiles."+arr[i]);
			XLData xlData = new XLData();
			xlData.setQuestionId(Integer.parseInt(arr[i]));
			xlData.setQuestionFileName(arr[i]+".xml");
			xlData.setQuestionName(val.trim());
			xlData.setQuestionCompletedFileName(arr[i]+"_done.txt");
			finalData.put(Integer.parseInt(arr[i]), xlData);
		}
		return finalData;
	}
	@GetMapping(value = "/sets/tests/getTestLink/{testId}/{name}")
	public ModelAndView getSetsTestLink(@PathVariable("testId") String testId, @PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			List<Question> qList = getSetQuestionsExisting(name, testId);
			List<Question> doneList = getSetDoneQuestionsExisting(name, testId);
			List<String> doneList1 = doneList.stream().map(x -> {
				return x.getSectionId()+"#"+x.getSubSectionId();
			}).collect(Collectors.toList());
			List<Question> finalList = qList.stream().filter(x -> {
				String key = x.getSectionId()+"#"+x.getSubSectionId();
				if(!doneList1.contains(key)) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			
			if (finalList.size() > 0) {
				List ls = getUniqueQuestion(finalList, Boolean.TRUE);
				if(ls.size()>0) {
					Question item = (Question) ls.get(0);
					item.setTotalCurrentQuestions(finalList.size());
					item.setQuestionId(Integer.valueOf(testId));
					String testIdVal = env.getProperty(name+".set."+testId);
					item.setQuestionName(testIdVal.trim());
					item.setUserName(name);
					modelAndView.addObject("question", item);
					modelAndView.addObject("dataExists", Boolean.TRUE);
				} else {
					modelAndView.addObject("dataExists", Boolean.FALSE);
				}
			}
			modelAndView.addObject("user_name_key", name);
			modelAndView.addObject("testResetFlag", Boolean.TRUE);
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("sets_ixl_test");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	private List getUniqueQuestion(List<Question> qList, boolean onlyQuestion) {
		List ls = new ArrayList();
		if(qList.size()>0) {
			Random rand = new Random();
			int c = rand.nextInt(qList.size());
			List<Question> tmp = new ArrayList<Question>();
			Question qtn = null;
			for(int i=0;i<qList.size();i++) {
				if(i==c) {
					if(onlyQuestion) {
						qtn = qList.get(i);
						break;
					} else {
						qtn = qList.get(i);
					}
				} else {
					tmp.add(qList.get(i));
				}
			}
			ls.add(qtn);
			ls.add(tmp);
		}
		return ls;
	}
	
	public List<Question> getSetQuestionsExisting(String name, String setId) {	
		List<Question> tempList = new ArrayList<Question>();
		getDataFilesForCurrentGrade(name).stream().forEach(fileNo -> {
			List<Question> myQuestions = getCurrentXmlFile(fileNo+".xml", "xmldata").stream().filter(x -> {
				if (x.getQuestionStatus().intValue() == 1 && x.getSetValue().contains(setId)) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			tempList.addAll(myQuestions);
		});
		return tempList;
	}
	
	@RequestMapping(value = "/sets/tests/nextQuestion", method = RequestMethod.POST)
	public ModelAndView setsNextQuestion(@ModelAttribute("question") Question questionReq) {
		try {
			String val = getXmlFile(String.valueOf(questionReq.getQuestionId()));
			List<Question> qList = getSetQuestionsExisting(questionReq.getUserName(), String.valueOf(questionReq.getQuestionId()));
			Question question = null;
			for(Question q: qList) {
				if(q.getGrade().equals(questionReq.getGrade()) 
						&& q.getSectionId().equals(questionReq.getSectionId()) 
						&& q.getSubSectionId().equals(questionReq.getSubSectionId())) {
					question = q;
					break;
				}
			}
			if(question == null) {
				throw new RuntimeException("got null for this question="+questionReq.toString());
			}
			writeSetRecordToFile(val, question, questionReq.getUserName());
			return getSetsTestLink(String.valueOf(questionReq.getQuestionId()), questionReq.getUserName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public void writeSetRecordToFile(String fileName, Question question, String name) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/set_clean/" + fileName.replace(".xml", "_done.txt");
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, "\n" + gson.toJson(question), StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public List<Question> getSetDoneQuestionsExisting(String name, String fileName) {
		List<Question> lines = new ArrayList<Question>();
		Scanner myReader = null;
		String val = null;
		try {
			String path = env.getProperty("destination.path");
			String finalPath = path+name+"/set_clean/" + fileName+"_done.txt";
			File myObj = new File(commonHelper.getPathByOS(finalPath));
			myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				val = myReader.nextLine();
				if (val != null && val.length() > 10) {
					Question q = gson.fromJson(val, Question.class);
					lines.add(q);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (myReader != null) {
					myReader.close();
				}
			} finally {
			}
		}
		return lines;
	}
	
	@GetMapping(value = "/sets/tests/results/{name}")
	public ModelAndView viewSetsTestResults(@PathVariable("name") String name) {
		ModelAndView obj = new ModelAndView("sets_view_test_results", HttpStatus.OK);
		List<Question> dataList = getSetQuestions(name);
		List<Question> finalDataList = dataList.stream().filter(x1 -> {
			x1.setResultsClean(setFileDataExists(String.valueOf(x1.getQuestionId()), name));
			if (name.equalsIgnoreCase(anish_name)) {
				obj.addObject("other_name", ishant_name.toUpperCase());
				obj.addObject("other_name_key", ishant_name);
				obj.addObject("user_name_key", anish_name);
			} else if (name.equalsIgnoreCase(ishant_name)) {
				obj.addObject("other_name", anish_name.toUpperCase());
				obj.addObject("other_name_key", anish_name);
				obj.addObject("user_name_key", ishant_name);
			}
			return true;
		}).collect(Collectors.toList());
		obj.addObject("name", name.toUpperCase());
		obj.addObject("data", finalDataList);
		return obj;
	}
	@GetMapping(value = "/sets/test/results/{testId}/{name}")
	public ModelAndView getSetTestResults(@PathVariable("testId") String testId, @PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("sets_ixl_results_view");
		try {
			XLData xlData = new XLData();
			xlData.setQuestionFileName(testId+".xml");
			List<Question> dataList = getSetAllQuestions(name);
			List<Question> finalList = dataList.stream().filter(x -> {
				if(x.getSetValue() != null && x.getSetValue().contains(testId)) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			List<Question> finalDataList = new ArrayList();
			for(Question q: finalList) {
				if(q.getQuestionStatus()!=0) {
					String fileName = testId+"_"+q.getGrade()+"_"+q.getSectionId()+"_"+q.getSubSectionId();
					Question qstn = getSetQuestionFromFile(name, fileName, testId);
					if(qstn != null) {
						q.setVerified(qstn.getVerified());
						q.setStatus(qstn.getStatus());
					} else {
						q.setVerified("0");
						q.setStatus("0");
					}
					if(q.getResultsUrl()!=null && !q.getResultsUrl().equals("")) {
						q.setResultsAvailable(Boolean.TRUE);
						q.setResultsUrl("https://www.ixl.com/analytics/questions-log#skill="+q.getResultsUrl()+"&skillPlanSelected=false");
					} else {
						q.setResultsAvailable(Boolean.FALSE);
					}
					q.setQuestionId(Integer.valueOf(testId));
					q.setQuestionName(fileName);
					finalDataList.add(q);
				} else {
					System.out.println("difficult question:"+q.toString());
				}
			}
			if (finalDataList.size() > 0) {
				modelAndView.addObject("data", finalDataList);
			}
			modelAndView.addObject("user_name_key", name);
			modelAndView.setStatus(HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	@GetMapping(value = "/sets/test/resultsVerify/{testId}/{name}")
	public ModelAndView getSetTestResultsVerify(@PathVariable("testId") String testId, @PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("sets_ixl_results_view_verify");
		try {
			XLData xlData = new XLData();
			xlData.setQuestionFileName(testId+".xml");
			List<Question> dataList = getSetAllQuestions(name);
			List<Question> finalList = dataList.stream().filter(x -> {
				if(x.getSetValue() != null && x.getSetValue().contains(testId)) {
					return true;
				}
				return false;
			}).collect(Collectors.toList());	
			List<Question> finalDataList = new ArrayList<Question>();
			for(Question q: finalList) {
				String fileName = testId+"_"+q.getGrade()+"_"+q.getSectionId()+"_"+q.getSubSectionId();
				Question qstn = getSetQuestionFromFile(name, fileName, testId);
				if(qstn == null && q.getQuestionStatus() != 0) {
					q.setVerified("0");
					q.setStatus("0");
					if(q.getResultsUrl()!=null && !q.getResultsUrl().equals("")) {
						q.setResultsAvailable(Boolean.TRUE);
						q.setResultsUrl("https://www.ixl.com/analytics/questions-log#skill="+q.getResultsUrl()+"&skillPlanSelected=false");
					} else {
						q.setResultsAvailable(Boolean.FALSE);
					}
					q.setQuestionId(Integer.valueOf(testId));
					q.setQuestionName(fileName);
					finalDataList.add(q);
				}
			}
			if (finalDataList.size() > 0) {
				modelAndView.addObject("data", finalDataList);
			}
			modelAndView.addObject("user_name_key", name);
			modelAndView.setStatus(HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	@GetMapping(value = "/sets/updateResultStatus/{testId}/{name}/{statusId}/{id}")
	public ModelAndView updateSetResultStatus(@PathVariable("testId") String testId, @PathVariable("name") String name, 
			@PathVariable("statusId") String statusId, @PathVariable("id") String id) {
		try {
			XLData xlData = new XLData();
			xlData.setQuestionFileName(testId+".xml");
			List<Question> dataList = getSetAllQuestions(name);
			Question finalQuestion = null;
			for(Question q: dataList) {
				if(q.getId()==Integer.parseInt(id)) {
					finalQuestion = q;
					finalQuestion.setVerified("1");
					finalQuestion.setStatus(statusId);
					break;
				}
			}
			if(finalQuestion != null) {
				writeSetQuestionStatusToFile(finalQuestion, name, testId);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return getSetTestResultsVerify(testId, name);
	}
	public Question getSetQuestionFromFile(String name, String fileName, String finalFolderName) {
		String pathVal = env.getProperty("destination.path");
		String doneFilePath = pathVal+name+"/set_results/"+finalFolderName+"/"+fileName+".data";
		Path path = Path.of(commonHelper.getPathByOS(doneFilePath));
		if(path.toFile().exists()) {
			Question q = null;
			Scanner myReader = null;
			String val = null;
	    	try {
	    		File myObj = new File(commonHelper.getPathByOS(doneFilePath));
	    		myReader = new Scanner(myObj);
	    		while (myReader.hasNextLine()) {
	    			val = myReader.nextLine();
	    			if(!StringUtils.isEmpty(val) && val.length()>0) {
	    				q = gson.fromJson(val, Question.class);
	    			}
	    		}
	    	} catch (Exception e) {
	    		System.out.println("Error at Row="+val);
				e.printStackTrace();
				return null;
			} finally {
				try {
					if(myReader!=null) {
						myReader.close();
					}
				} finally {}
			}
	    	return q;
		} else {
			return null;
		}
    }
	public void writeSetQuestionStatusToFile(Question question, String personName, String fileNo) {
    	try {   
    		String fileName = fileNo+"_"+question.getGrade()+"_"+question.getSectionId()+"_"+question.getSubSectionId();
    		String pathVal = env.getProperty("destination.path");
    		String doneFilePath = pathVal+personName+"/set_results/"+fileNo+"/"+fileName+".data";
    		Path path = Path.of(commonHelper.getPathByOS(doneFilePath));
    		Files.deleteIfExists(path);
    		Files.writeString(path, "\n"+gson.toJson(question), StandardOpenOption.CREATE_NEW);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
	public boolean setFileDataExists(String testId, String name) {
		try {
			String pathVal = env.getProperty("destination.path");
			String doneFilePath = pathVal+name+"/set_clean/"+testId+"_done.txt";
			if(resultFileDataExists(doneFilePath)) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	@GetMapping(value = "/sets/test/clean/{testId}/{name}")
	public ModelAndView setCleanFolders(@PathVariable("testId") String testId, @PathVariable("name") String name) {
		try {
			String pathVal = env.getProperty("destination.path");
			String doneFilePath = pathVal+name+"/set_clean/"+testId+"_done.txt";
			reCreateFile(doneFilePath);
			return viewSetsTestResults(name);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	@GetMapping(value = "/admin/tasks")
	public ModelAndView runAdminTests() {
		ModelAndView obj = new ModelAndView("run_admin_tasks", HttpStatus.OK);
		return obj;
	}
}