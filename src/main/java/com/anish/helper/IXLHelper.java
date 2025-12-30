package com.anish.helper;

import com.anish.data.BuildQuestion;
import com.anish.parsers.BuildIXLQuestionSaxParserHandler;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class IXLHelper {

	String[] arr = {"_done.txt", "_easyFileName_done.txt","_needMorePracticeFileName_done.txt","_needPracticeFileName_done.txt"};
	
	@Autowired
	private Environment env;
	
	@Autowired
	private CommonHelper commonHelper;
	
	private static String url = "https://www.ixl.com/";
	
	private Gson gson = new Gson();
	
	public String buildIxlXml(String name) {
		String rtn = "failed";
		try {
			String value = env.getProperty(name+".buildXmls");
			String[] valArr = value.split("#");
			Map<String, BuildQuestion> qMap = new HashMap<String, BuildQuestion>();
			for(int i=0;i<valArr.length;i++) {
				List<BuildQuestion> qList = buildGetCurrentXmlFileWithCounter(valArr[i]+".xml", "buildXml");
				if(qList.size()>0) {
					qList.stream().forEach(q -> {
						String val = null;
						try {
							if(q.getSubSectionId()==null) {
								throw new RuntimeException("SubSectionId Already Exists:"+val);
							}
							val = q.getGrade()+"#"+q.getSectionId()+"#"+q.getSubSectionId();
							boolean hasKey = qMap.containsKey(val);
							if(!hasKey) {
								qMap.put(val, q);
							} else {
								throw new RuntimeException("Already Exists:"+val);
							}
						} catch (Exception e) {
							System.out.println("Error [val="+val+"][question="+gson.toJson(q)+"]");
							e.printStackTrace();
							throw e;
						}
					});
				}
			}
			if(qMap.size()>0) {
				List<BuildQuestion> mommyReviewList = new ArrayList<BuildQuestion>();
				List<BuildQuestion> finalList = new ArrayList<BuildQuestion>();
				qMap.entrySet().forEach(entry -> {
					if(entry.getValue().getEnabled()!=1) {
						mommyReviewList.add(entry.getValue());
					} else {
						finalList.add(entry.getValue());
					}
				});
				Collections.shuffle(finalList);
				String envValX = env.getProperty(name+".splitFiles");
				String[] splitFilesArr = envValX.split("#");
				int chunkSize = finalList.size()/splitFilesArr.length;
				List<List<BuildQuestion>> result = splitList(finalList, chunkSize);
				
				// Validate Total Questions
				int totalQuestions = finalList.size();
				int consolidatedList = 0;
				for(int i=0;i<result.size();i++) {
					consolidatedList = consolidatedList+result.get(i).size();
				};
				if(totalQuestions!=consolidatedList) {
					throw new Exception("Issue total questions is not equal to sorted list totalQuestions="+totalQuestions+",consolidatedList="+consolidatedList);
				}
				// End Validate Total Questions
				int evaluateFile = 0;
				for(int i=0;i<splitFilesArr.length;i++) {
					String fileName = env.getProperty(name+".datafiles."+splitFilesArr[i]);
					List<BuildQuestion> writeList = result.get(i);
					writeQuestonToFile(writeList, splitFilesArr[i], fileName, name);
					if(mommyReviewList.size()>0) {
						evaluateFile = Integer.parseInt(splitFilesArr[i]);
					}
				}
				if(mommyReviewList.size()>0) {
					evaluateFile = evaluateFile+1;
					String fileName = env.getProperty(name+".datafiles."+evaluateFile);
					writeQuestonToFile(mommyReviewList, String.valueOf(evaluateFile), fileName, name);
				}
				rtn = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	public <T> List<List<T>> splitList(List<T> originalList, int chunkSize) {
        if (originalList == null || originalList.isEmpty() || chunkSize <= 0) {
            return new ArrayList<>(); // Return an empty list for invalid input
        }

        List<List<T>> sublists = new ArrayList<>();
        int listSize = originalList.size();

        for (int i = 0; i < listSize; i += chunkSize) {
            // Calculate the end index for the sublist
            int endIndex = Math.min(i + chunkSize, listSize);
            
            // Create a new ArrayList from the sublist view to ensure it's a separate copy
            sublists.add(new ArrayList<>(originalList.subList(i, endIndex)));
        }

        return sublists;
    }
	
	public void writeQuestonToFile(List<BuildQuestion> writeList, String fileNo, String fileName, String name) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/writeXml/" + fileNo+".xml";
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, "<Questions>\n", StandardOpenOption.CREATE);
			
			AtomicInteger counter = new AtomicInteger(1);
			writeList.stream().forEach(q -> {
				String questionStr = getQuestionStringNew(q, counter, fileNo, fileName);
				try {
					Files.writeString(filePath, questionStr, StandardOpenOption.APPEND);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			Files.writeString(filePath, "</Questions>", StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<BuildQuestion> buildGetCurrentXmlFileWithCounter(String fileName, String folderName) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = folderName+"/" + fileName;
			URL resource = classLoader.getResource(commonHelper.getPathByOS(filePath));
			File fObj = new File(resource.getFile());
			if (fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
				BuildIXLQuestionSaxParserHandler handler = new BuildIXLQuestionSaxParserHandler(new AtomicInteger(0));
				saxParser.parse(fObj, handler);
				List<BuildQuestion> dataList = handler.getQuestionList();
				if (dataList.size() > 0) {
					return buildSortItems(dataList, dataList.size());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
	
	private List<BuildQuestion> buildSortItems(List<BuildQuestion> dataList, Integer totalQuestions) {
		List<BuildQuestion> finalItems = new ArrayList<BuildQuestion>();
		List<Integer> excludeList = new ArrayList<Integer>();
		Map<String, List<BuildQuestion>> groupBySubject = dataList.stream()
				.collect(Collectors.groupingBy(BuildQuestion::getSectionId));
		boolean flag = true;
		while (flag) {
			for (Entry<String, List<BuildQuestion>> entry : groupBySubject.entrySet()) {
				for (BuildQuestion item : entry.getValue()) {
					if (finalItems.size() == dataList.size()) {
						flag = false;
						break;
					} else {
						if (!excludeList.contains(item.getId())) {
							// item.setTotalRows(totalQuestions);
							item.setUrl(url + getPathSubject(item.getSubject()).toLowerCase() + "/grade-"+ item.getGrade() + "/" + item.getUrlKey());
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
	
	/*
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
	*/
	private String getQuestionStringNew(BuildQuestion q, AtomicInteger counter, String fileNo, String fileName) {
		StringBuffer sb = new StringBuffer();
		sb.append("\t<Question>\n");
			sb.append("\t\t<Id>"+counter.getAndIncrement()+"</Id>\n");
			sb.append("\t\t<Subject>"+q.getSubject()+"</Subject>\n");
			sb.append("\t\t<Grade>"+q.getGrade()+"</Grade>\n");
			sb.append("\t\t<SectionId>"+q.getSectionId()+"</SectionId>\n");
			sb.append("\t\t<SectionName>"+q.getSectionName()+"</SectionName>\n");
			sb.append("\t\t<Enabled>"+q.getEnabled()+"</Enabled>\n");
			sb.append("\t\t<SubSectionId>"+q.getSubSectionId()+"</SubSectionId>\n");
			sb.append("\t\t<SubSectionName>"+q.getSubSectionName()+"</SubSectionName>\n");
			sb.append("\t\t<UrlKey>"+q.getUrlKey()+"</UrlKey>\n");
			if(q.getResultsUrl()!=null) {
				sb.append("\t\t<ResultsUrl>"+q.getResultsUrl()+"</ResultsUrl>\n");
			} else {
				sb.append("\t\t<ResultsUrl/>\n");
			}
			sb.append("\t\t<Verified>0</Verified>\n");
			sb.append("\t\t<Status>0</Status>\n");
			/*
			if(q.getStatus()!=null) {
				sb.append("\t\t<Status>"+q.getStatus()+"</Status>\n");
			} else {
				sb.append("\t\t<Status/>\n");
			}
			if(q.getVerified()!=null) {
				sb.append("\t\t<Verified>"+q.getVerified()+"</Verified>\n");
			} else {
				sb.append("\t\t<Verified/>\n");
			}
			*/
			sb.append("\t\t<QuestionId>"+fileNo+"</QuestionId>\n");
			if(q.getSetValue() != null) {
				sb.append("\t\t<SetValue>"+q.getSetValue()+"</SetValue>\n");
			} else {
				sb.append("\t\t<SetValue>0</SetValue>\n");
			}
			sb.append("\t\t<QuestionName>"+fileName+"</QuestionName>\n");
		sb.append("\t</Question>\n");
		return sb.toString();
	}

	public String getPathSubject(String subject) {
		if (subject.equalsIgnoreCase("language arts")) {
			return "ela";
		} else if (subject.equalsIgnoreCase("math")) {
			return "math";
		}
		return null;
	}

	public boolean deleteAndCreateFiles(String testId, String name) {
		String pathVal = env.getProperty("destination.path");
		for(String fileDetails : arr) {
			String doneFilePath = pathVal+name+"/clean/"+testId+fileDetails;
			boolean flag = deleteAndCreateFile(doneFilePath);
			if(!flag) {
				return false;
			}
		}
		return true;
	}

	public boolean deleteAndCreateFile(String pathStr) {
		try {
			Path path = Path.of(pathStr);
			if(Files.exists(path)) {
				Files.delete(path);
			}
			Files.createFile(path);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
