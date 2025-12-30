package com.anish.controllers;

import com.anish.data.DoubleHalf;
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
public class DoubleAndHalfController {
	
	@Autowired private Environment env;
	@Autowired private CommonHelper commonHelper;
	private String sourceFile = "_double_source_file.txt";
		
	private Gson gson = new Gson();
	
	private String answer_correct = "Correct";
	private String answer_wrong = "Wrong";
	private String not_answered = "Not Answered";
	
	private String anish_name = "anish";
	private String ishant_name = "ishant";
	private String done_file = "double_half_done.txt";
	
	@GetMapping(value = "/doubleHalf/showQuestions/{name}")
    public ModelAndView getTakeTest(@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("double_half_view");
		modelAndView.setStatus(HttpStatus.OK);
		modelAndView.addObject("user_name_key", name);
		try {
			List<DoubleHalf> list = getDoubleSourceData(name);
			List<DoubleHalf> completed = getDoubleHalfDoneData(name);
			List<DoubleHalf> finalList = null;
			if(completed!=null && completed.size()>0) {
				finalList = list.stream()
						.filter(x -> {
							return !checkRecordExists(completed, x.getValue(), x.getIsDouble());
						})
						.collect(Collectors.toList());
			} else {
				finalList = list;
			}
			Map<Integer, DoubleHalf> fl1 = new HashMap<Integer, DoubleHalf>();
			finalList.stream().forEach(x -> {
				fl1.put(x.getId(), x);
			});
			if(fl1.size()>0) {
				Random rand = new Random();
			    int randomElement = fl1.keySet().stream().collect(Collectors.toList()).get(rand.nextInt(fl1.keySet().size()));
			    DoubleHalf finalObj = fl1.get(randomElement);
			    finalObj.setTotalQuestions(fl1.keySet().size());
			    modelAndView.addObject("idDone", Boolean.FALSE);
				modelAndView.addObject("doubleHalf", finalObj);
				modelAndView.addObject("doubleHalfTotal", fl1.size());
			} else {
				modelAndView.addObject("idDone", Boolean.TRUE);
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	public List<DoubleHalf> getDoubleSourceData(String name) {
		List<DoubleHalf> lines = new ArrayList<DoubleHalf>();
		Scanner myReader = null;
		String val = null;
    	try {
    		ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "xmldata/" + name+sourceFile;
			URL resource = classLoader.getResource(commonHelper.getPathByOS(filePath));
			File fObj = new File(resource.getFile());
    		myReader = new Scanner(fObj);
    		while (myReader.hasNextLine()) {
    			val = myReader.nextLine();
    			if(!StringUtils.isEmpty(val) && val.length()>0) {
        			lines.add(gson.fromJson(val, DoubleHalf.class));
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
	public List<DoubleHalf> getDoubleHalfDoneData(String name) {
		String path = env.getProperty("destination.path");
		String doneFilePath = path+name+"/clean/"+done_file;
		List<DoubleHalf> lines = new ArrayList<DoubleHalf>();
		Scanner myReader = null;
		String val = null;
    	try {
    		File myObj = new File(commonHelper.getPathByOS(doneFilePath));
    		myReader = new Scanner(myObj);
    		while (myReader.hasNextLine()) {
    			val = myReader.nextLine();
    			if(val !=null && !"".equals(val) && val.length()>0) {
        			lines.add(gson.fromJson(val, DoubleHalf.class));
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
	@RequestMapping(value = "/doubleHalf/submitDoubleHalf", method = RequestMethod.POST)
    public ModelAndView completeWord(@ModelAttribute("doubleHalf") DoubleHalf doubleHalf) {
		ModelAndView modelAndView = new ModelAndView("double_half_view");
		modelAndView.setStatus(HttpStatus.OK);
		try {
			if(doubleHalf.getAnswer()!=null) {
				List<DoubleHalf> finalList = getDoubleSourceData(doubleHalf.getChildName());
				DoubleHalf doubleHalfVal = null;
				for(DoubleHalf w: finalList) {
					if(w.getIsDouble()==doubleHalf.getIsDouble() && w.getValue().equals(doubleHalf.getValue())) {
						doubleHalfVal = w;
						doubleHalfVal.setAnswer(doubleHalf.getAnswer());
						doubleHalfVal.setDoubleHalfAnswerTime(doubleHalf.getDoubleHalfAnswerTime());
						if(doubleHalfVal.getIsDouble()) {
							int doubleVal = Integer.valueOf(doubleHalfVal.getValue());
							doubleVal = doubleVal+ doubleVal;
							if(doubleVal == Integer.valueOf(doubleHalfVal.getAnswer())) {
								doubleHalfVal.setResult(answer_correct);
							} else {
								doubleHalfVal.setResult(answer_wrong);
							}
						} else {
							int halfVal = doubleHalfVal.getValue()/2;
							if(Integer.valueOf(doubleHalfVal.getAnswer()) == halfVal) {
								doubleHalfVal.setResult(answer_correct);
							} else {
								doubleHalfVal.setResult(answer_wrong);
							}
						}
						break;
					}
				}
				if(doubleHalfVal != null) {
					String path = env.getProperty("destination.path");
					String doneFilePath = path+doubleHalf.getChildName()+"/clean/"+done_file;
					writeWordRecordToFile(doneFilePath, doubleHalf);
					ModelAndView rslt = getTakeTest(doubleHalfVal.getChildName());
					rslt.addObject("previousAnswer", doubleHalfVal.getAnswer());
					rslt.addObject("previousAnswerStatus", doubleHalfVal.getResult());
					rslt.addObject("previousValue", doubleHalfVal.getValue());
					rslt.addObject("previousAnswerFlag", Boolean.TRUE);
					if(doubleHalfVal.getIsDouble()) {
						rslt.addObject("previousIsDouble", Boolean.TRUE);
					} else {
						rslt.addObject("previousIsDouble", Boolean.FALSE);
					}
					String y = "ddd";
					return rslt;	
				}
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return modelAndView;
    }
	
	public void writeWordRecordToFile(String filePath, DoubleHalf word) {
    	try {   
    		Path path = Path.of(commonHelper.getPathByOS(filePath));
    		Files.writeString(path, "\n"+gson.toJson(word), StandardOpenOption.APPEND);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
	
	public boolean checkRecordExists(List<DoubleHalf> completedList, Integer value, boolean isDouble) {
		boolean rtnFlag = Boolean.FALSE;
		for(DoubleHalf x: completedList) {
			if(x.getIsDouble()==isDouble && x.getValue()== value) {
				rtnFlag = Boolean.TRUE;
				break;
			}
		}
		return rtnFlag;
	}
	
	@GetMapping(value = "/doubleHalf/test/results/{name}")
    public ModelAndView getDoubleHalfResults(@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("double_half_view_results");
		modelAndView.setStatus(HttpStatus.OK);
		modelAndView.addObject("user_name_key", name);
		try {
			List<DoubleHalf> completed = getDoubleHalfDoneData(name);
			if(completed.size()>0) {
				modelAndView.addObject("data", completed);
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	private List<DoubleHalf> arrangeDoubleData(List<DoubleHalf> list) {
		List<DoubleHalf> finalList = new ArrayList<DoubleHalf>();
		Map<Integer, DoubleHalf> fl1 = new HashMap<Integer, DoubleHalf>();
		list.stream().forEach(x -> {
			fl1.put(x.getId(), x);
		});
		while(fl1.size()>0) {
			Random rand = new Random();
		    int randomElement = fl1.keySet().stream().collect(Collectors.toList()).get(rand.nextInt(fl1.keySet().size()));
		    DoubleHalf finalObj = fl1.get(randomElement);
		    finalObj.setTotalQuestions(fl1.keySet().size());
		    finalList.add(finalObj);
		    fl1.remove(randomElement);
		}
		return finalList;
	}
	
	@GetMapping(value = "/double/generateDoubleTest/{name}")
    public ModelAndView generateTableTest(@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView("print_double_results");
		modelAndView.setStatus(HttpStatus.OK);
		modelAndView.addObject("user_name_key", name);
		modelAndView.addObject("user_name_key_caps", name.toUpperCase());
		try {
			long uniqueId = System.currentTimeMillis();
			setDoublePrintableData(modelAndView, name);
			modelAndView.addObject("uniqueId", uniqueId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return modelAndView;
    }
	
	private List<DoubleHalf> printDoubles(String name) {
		List<DoubleHalf> rslt = new ArrayList<DoubleHalf>();
		try {
			String doubleVal = env.getProperty(name+".double");
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
	
	public int setDoublePrintableData(ModelAndView modelAndView, String name) {
		try {
			List<DoubleHalf> list = printDoubles(name);
			List<DoubleHalf> tableData = list.stream().map(x -> {
				if(x.getIsDouble()) {
					x.setPrintValue("D-"+x.getValue()+"=");
				} else {
					x.setPrintValue("H-"+x.getValue()+"=");
				}
				return x;
			}).collect(Collectors.toList());
			
			List<DoubleHalf> data1 = new ArrayList<DoubleHalf>();
			List<DoubleHalf> data2 = new ArrayList<DoubleHalf>();
			List<DoubleHalf> data3 = new ArrayList<DoubleHalf>();
			List<DoubleHalf> data4 = new ArrayList<DoubleHalf>();
			List<DoubleHalf> data5 = new ArrayList<DoubleHalf>();
			List<DoubleHalf> data6 = new ArrayList<DoubleHalf>();
			if(tableData != null && tableData.size()>0) {
				int counter = 1;
				modelAndView.addObject("doubleDataExists", Boolean.TRUE);
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
				modelAndView.addObject("doubleDataExists", Boolean.FALSE);
			}
			modelAndView.addObject("doubleData1", data1);
			modelAndView.addObject("doubleData2", data2);
			modelAndView.addObject("doubleData3", data3);
			modelAndView.addObject("doubleData4", data4);
			modelAndView.addObject("doubleData5", data5);
			modelAndView.addObject("doubleData6", data6);
			return tableData.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}
