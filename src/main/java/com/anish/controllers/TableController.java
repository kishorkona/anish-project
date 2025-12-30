package com.anish.controllers;

import com.anish.data.TableData;
import com.anish.helper.CommonHelper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
public class TableController {
	
	@Autowired private Environment env;
	@Autowired private CommonHelper commonHelper;
	@Autowired private DoubleAndHalfController doubleAndHalfController;
		
	private Gson gson = new Gson();
	
	private String anish_name = "anish";
	private String ishant_name = "ishant";
	
	private String answer_correct = "Correct";
	private String answer_wrong = "Wrong";
	private String not_answered = "Not Answered";
	
	private String sourceFile = "_table_source_file.txt";
	private String doneFile = "table_done_file.txt";
	
	@GetMapping(value = "/tables/startTest/{name}")
    public ModelAndView getTakeTest(@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("table_view");
		modelAndView.setStatus(HttpStatus.OK);
		modelAndView.addObject("user_name_key", name);
		try {
			TableData tableData = getFinalList(name);

			if(tableData != null) {
				modelAndView.addObject("idDone", Boolean.FALSE);
				modelAndView.addObject("tableData", tableData);
			} else {
				modelAndView.addObject("idDone", Boolean.TRUE);
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	public TableData getFinalList(String name) {
		try {
			List<TableData> l1 = getTableSourceData(name);
			List<Integer> l2 = getTableSourceDoneFile(name).stream().map(x -> {
				return x.getId();
			}).collect(Collectors.toList());
			List<TableData> finalList = l1.stream()
					.filter(f1 -> {
						if(l2.contains(f1.getId())) {
							return false;
						}
						return true;
					})
					.collect(Collectors.toList());
			Map<Integer, TableData> fl1 = new HashMap<Integer, TableData>();
			finalList.stream().forEach(x -> {
				fl1.put(x.getId(), x);
			});
			if(fl1.size()>0) {
				Random rand = new Random();
			    int randomElement = fl1.keySet().stream().collect(Collectors.toList()).get(rand.nextInt(fl1.keySet().size()));
			    TableData finalObj = fl1.get(randomElement);
			    finalObj.setTotalQuestions(fl1.keySet().size());
			    return finalObj;
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return null;
    }
	
	public List<TableData> getFullList(String name) {
		List<TableData> resultList = new ArrayList<TableData>();
		try {
			List<TableData> l1 = getTableSourceData(name);
			List<Integer> l2 = getTableSourceDoneFile(name).stream().map(x -> {
				return x.getId();
			}).collect(Collectors.toList());
			List<TableData> finalList = l1.stream()
					.filter(f1 -> {
						if(l2.contains(f1.getId())) {
							return false;
						}
						return true;
					})
					.collect(Collectors.toList());
			Map<Integer, TableData> fl1 = new HashMap<Integer, TableData>();
			finalList.stream().forEach(x -> {
				fl1.put(x.getId(), x);
			});
			while(fl1.size()>0) {
				Random rand = new Random();
			    int randomElement = fl1.keySet().stream().collect(Collectors.toList()).get(rand.nextInt(fl1.keySet().size()));
			    TableData finalObj = fl1.get(randomElement);
			    finalObj.setTotalQuestions(fl1.keySet().size());
			    resultList.add(finalObj);
			    fl1.remove(randomElement);
			}
			return resultList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return null;
    }
	
	@RequestMapping(value = "/tables/submitTable", method = RequestMethod.POST)
    public ModelAndView completeWord(@ModelAttribute("tableData") TableData tableData) {
		ModelAndView modelAndView = new ModelAndView("table_view");
		modelAndView.setStatus(HttpStatus.OK);
		try {
			if(tableData.getAnswer()!=null) {
				List<TableData> finalList = getTableSourceData(tableData.getChildName());
				TableData obj = null;
				for(TableData w: finalList) {
					if(w.getValue().equals(tableData.getValue())) {
						obj = w;
						obj.setAnswer(tableData.getAnswer());
						obj.setAnswerTime(tableData.getAnswerTime());
						break;
					}
				}
				if(obj != null) {
					obj.setAnswer(tableData.getAnswer());
					obj.setAnswerTime(tableData.getAnswerTime());
					String val = obj.getValue();
					String[] valArr = val.split("Times");
					int ans = Integer.parseInt(valArr[0].trim()) * Integer.parseInt(valArr[1].trim());
					if(obj.getAnswer()==ans) {
						obj.setResult(answer_correct);
					} else {
						obj.setResult(answer_wrong);
					}
					String path = env.getProperty("destination.path");
					String doneFilePath = path+obj.getChildName()+"/clean/"+doneFile;
					writeWordRecordToFile(doneFilePath, obj);
					ModelAndView rslt = getTakeTest(tableData.getChildName());
					rslt.addObject("previousAnswer", obj.getAnswer());
					rslt.addObject("previousAnswerStatus", obj.getResult());
					rslt.addObject("previousValue", obj.getValue());
					rslt.addObject("previousAnswerFlag", Boolean.TRUE);
					return rslt;
				}
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return modelAndView;
    }
	
	public void writeWordRecordToFile(String filePath, TableData word) {
    	try {   
    		Path path = Path.of(commonHelper.getPathByOS(filePath));
    		Files.writeString(path, "\n"+gson.toJson(word), StandardOpenOption.APPEND);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
	
	public List<TableData> getTableSourceData(String name) {
		List<TableData> lines = new ArrayList<TableData>();
		Scanner myReader = null;
		String val = null;
    	try {
    		ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "xmldata/" + name+sourceFile;
			String finalPth = commonHelper.getPathByOS(filePath);
			System.out.println("file path To read at getTableSourceData="+finalPth);
			URL resource = classLoader.getResource(finalPth);
			File fObj = new File(resource.getFile());
    		myReader = new Scanner(fObj);
    		while (myReader.hasNextLine()) {
    			val = myReader.nextLine();
    			if(!StringUtils.isEmpty(val) && val.length()>0) {
        			lines.add(gson.fromJson(val, TableData.class));
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
    	buildSimpleMultiplication(name, "Times", lines);
    	return lines;
    }
	
	@GetMapping(value = "/tables/test/results/{name}")
    public ModelAndView getDoubleHalfResults(@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("tables_view_results");
		modelAndView.setStatus(HttpStatus.OK);
		modelAndView.addObject("user_name_key", name);
		try {
			List<TableData> completed = getTableSourceDoneFile(name);
			AtomicInteger totalCorrect = new AtomicInteger(0);
			AtomicInteger totalWrong = new AtomicInteger(0);
			AtomicInteger totalNotAnswered = new AtomicInteger(0);
			List<TableData> finalList = completed.stream().map(x -> {
				if(x.getResult().equalsIgnoreCase(not_answered)) {
					totalNotAnswered.getAndIncrement();
				} else {
					if(x.getResult().equalsIgnoreCase(answer_correct)) {
						totalCorrect.getAndIncrement();
					} else if(x.getResult().equalsIgnoreCase(answer_wrong)) {
						totalWrong.getAndIncrement();
					}
				}
				return x;
			}).collect(Collectors.toList());
			if(completed.size()>0) {
				modelAndView.addObject("data", finalList);
				modelAndView.addObject("totalCorrect", totalCorrect);
				modelAndView.addObject("totalWrong", totalWrong);
				modelAndView.addObject("totalNotAnswered", totalNotAnswered);
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	public List<TableData> getTableSourceDoneFile(String name) {
		String path = env.getProperty("destination.path");
		String doneFilePath = path+name+"/clean/"+doneFile;
		List<TableData> lines = new ArrayList<TableData>();
		Scanner myReader = null;
		String val = null;
    	try {
    		File myObj = new File(commonHelper.getPathByOS(doneFilePath));
    		myReader = new Scanner(myObj);
    		while (myReader.hasNextLine()) {
    			val = myReader.nextLine();
    			if(!StringUtils.isEmpty(val) && val.length()>0) {
        			lines.add(gson.fromJson(val, TableData.class));
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
    	return lines;
    }
	
	@GetMapping(value = "/tables/generateTableTest/{uniqNo}/{name}")
    public ModelAndView generateTableTest(@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("print_table_results");
		modelAndView.setStatus(HttpStatus.OK);
		modelAndView.addObject("user_name_key", name);
		modelAndView.addObject("user_name_key_caps", name.toUpperCase());
		try {
			long uniqueId = System.currentTimeMillis();
			List<TableData> tableData = generateTableTestList(name);
			
			int totalQuestionToAnswer = buildTablePrintData(tableData, modelAndView);
			List<TableData> practiceData = difficultTableFullList(name);
			buildTablePrintPracticeData(practiceData, modelAndView);
			long uniqueId1 = System.currentTimeMillis();
			modelAndView.addObject("uniqueId1", uniqueId1);
			modelAndView.addObject("dataExists", Boolean.TRUE);
			modelAndView.addObject("uniqueId", uniqueId);
			int totalDoubleQuestion = doubleAndHalfController.setDoublePrintableData(modelAndView, name);
			modelAndView.addObject("totalQuestions", totalQuestionToAnswer+totalDoubleQuestion);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return modelAndView;
    }
	
	private void buildTablePrintPracticeData(List<TableData> tableData, ModelAndView modelAndView) {
		try {
			int totalPracticeQuestions = tableData.size();

			List<TableData> practiceData1 = new ArrayList<TableData>();
			List<TableData> practiceData2 = new ArrayList<TableData>();
			List<TableData> practiceData3 = new ArrayList<TableData>();
			List<TableData> practiceData4 = new ArrayList<TableData>();
			List<TableData> practiceData5 = new ArrayList<TableData>();
			List<TableData> practiceData6 = new ArrayList<TableData>();
			
			int switchCounter = 1;
			for(TableData data: tableData) {
				if(switchCounter==1) {
					practiceData1.add(data);
					switchCounter = 2;
				} else if(switchCounter==2) {
					practiceData2.add(data);
					switchCounter = 3;
				} else if(switchCounter==3) {
					practiceData3.add(data);
					switchCounter = 4;
				} else if(switchCounter==4) {
					practiceData4.add(data);
					switchCounter = 5;
				} else if(switchCounter==5) {
					practiceData5.add(data);
					switchCounter = 6;
				} else if(switchCounter==6) {
					practiceData6.add(data);
					switchCounter = 1;
				}
			}
			modelAndView.addObject("practiceData1", practiceData1);
			modelAndView.addObject("practiceData2", practiceData2);
			modelAndView.addObject("practiceData3", practiceData3);
			modelAndView.addObject("practiceData4", practiceData4);
			modelAndView.addObject("practiceData5", practiceData5);
			modelAndView.addObject("practiceData6", practiceData6);
			modelAndView.addObject("totalPracticeQuestions", totalPracticeQuestions);
		} catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	private int buildTablePrintData(List<TableData> tableData, ModelAndView modelAndView) {
		try {
			List<TableData> testData = tableData.subList(0, 9);
			int totalQuestions = tableData.size();
			
			List<TableData> testData1 = new ArrayList<TableData>();
			List<TableData> testData2 = new ArrayList<TableData>();
			List<TableData> testData3 = new ArrayList<TableData>();
			testData1 = testData.subList(0, 3);
			testData2 = testData.subList(3, 6);
			testData3 = testData.subList(6, testData.size());
			modelAndView.addObject("testData1", testData1);
			modelAndView.addObject("testData2", testData2);
			modelAndView.addObject("testData3", testData3);
						
			List<TableData> data1 = new ArrayList<TableData>();
			List<TableData> data2 = new ArrayList<TableData>();
			List<TableData> data3 = new ArrayList<TableData>();
			List<TableData> data4 = new ArrayList<TableData>();
			List<TableData> data5 = new ArrayList<TableData>();
			List<TableData> data6 = new ArrayList<TableData>();
			int switchCounter = 1;
			for(TableData data: tableData) {
				if(switchCounter==1) {
					data1.add(data);
					switchCounter = 2;
				} else if(switchCounter==2) {
					data2.add(data);
					switchCounter = 3;
				} else if(switchCounter==3) {
					data3.add(data);
					switchCounter = 4;
				} else if(switchCounter==4) {
					data4.add(data);
					switchCounter = 5;
				} else if(switchCounter==5) {
					data5.add(data);
					switchCounter = 6;
				} else if(switchCounter==6) {
					data6.add(data);
					switchCounter = 1;
				}
			}
			modelAndView.addObject("data1", data1);
			modelAndView.addObject("data2", data2);
			modelAndView.addObject("data3", data3);
			modelAndView.addObject("data4", data4);
			modelAndView.addObject("data5", data5);
			modelAndView.addObject("data6", data6);
			return totalQuestions;
		} catch (Exception ex) {
            ex.printStackTrace();
        }
		return 0;
	}

	private void buildSimpleMultiplication(String name, String tns, List<TableData> completed) {
		Random rand = new Random();
		int sizeVal = completed.size();
		int randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, randomNumber+" "+tns+" 1"));
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, randomNumber+" "+tns+" 1"));
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, randomNumber+" "+tns+" 1"));
		
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, "1 "+tns+" "+randomNumber));
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, "1 "+tns+" "+randomNumber));
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, "1 "+tns+" "+randomNumber));
		
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, randomNumber+" "+tns+" 0"));
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, randomNumber+" "+tns+" 0"));
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, randomNumber+" "+tns+" 0"));
		
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, "0 "+tns+" "+randomNumber));
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, "0 "+tns+" "+randomNumber));
		randomNumber = rand.nextInt(20);
		completed.add(new TableData(++sizeVal, name, "0 "+tns+" "+randomNumber));    
	}
	
	public List<TableData> difficultTableFullList(String name) {
		List<TableData> resultList = new ArrayList<TableData>();
		try {
			String fromVal = env.getProperty(name+".table.practice.from");
			String[] fromValArr = fromVal.split("#");
			
			String toVal = env.getProperty(name+".table.practice.to");
			String[] toValArr = toVal.split("#");
			
			AtomicInteger cnt = new AtomicInteger(1);
			List<TableData> l1 = new ArrayList<TableData>();
			for(int i=0;i<fromValArr.length;i++) {
				int k = Integer.parseInt(fromValArr[i]);
				for(int j=Integer.parseInt(toValArr[0]);j<=Integer.parseInt(toValArr[1]);j++) {
					TableData tableData = new TableData();
					tableData.setId(cnt.getAndIncrement());
					tableData.setChildName(name);
					tableData.setValue(k+" X "+j+"=");
					l1.add(tableData);
				}
			}
			Map<Integer, TableData> fl1 = new HashMap<Integer, TableData>();
			l1.stream().forEach(x -> {
				fl1.put(x.getId(), x);
			});
			while(fl1.size()>0) {
				Random rand = new Random();
			    int randomElement = fl1.keySet().stream().collect(Collectors.toList()).get(rand.nextInt(fl1.keySet().size()));
			    TableData finalObj = fl1.get(randomElement);
			    finalObj.setTotalQuestions(fl1.keySet().size());
			    resultList.add(finalObj);
			    fl1.remove(randomElement);
			}
			return resultList;
        } catch (Exception ex) {
            throw ex;
        }
    }
	
	public List<TableData> generateTableTestList(String name) {
		List<TableData> resultList = new ArrayList<TableData>();
		try {
			String fromVal = env.getProperty(name+".table.from");
			String[] fromValArr = fromVal.split("#");
			
			String toVal = env.getProperty(name+".table.to");
			String[] toValArr = toVal.split("#");
			
			AtomicInteger cnt = new AtomicInteger(1);
			List<TableData> l1 = new ArrayList<TableData>();
			for(int i=Integer.parseInt(fromValArr[0]);i<=Integer.parseInt(fromValArr[1]);i++) {
				for(int j=Integer.parseInt(toValArr[0]);j<=Integer.parseInt(toValArr[1]);j++) {
					TableData tableData = new TableData();
					tableData.setId(cnt.getAndIncrement());
					tableData.setChildName(name);
					tableData.setValue(i+" X "+j+"=");
					l1.add(tableData);
				}
			}
			Map<Integer, TableData> fl1 = new HashMap<Integer, TableData>();
			l1.stream().forEach(x -> {
				fl1.put(x.getId(), x);
			});
			while(fl1.size()>0) {
				Random rand = new Random();
			    int randomElement = fl1.keySet().stream().collect(Collectors.toList()).get(rand.nextInt(fl1.keySet().size()));
			    TableData finalObj = fl1.get(randomElement);
			    finalObj.setTotalQuestions(fl1.keySet().size());
			    resultList.add(finalObj);
			    fl1.remove(randomElement);
			}
			return resultList;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
