package com.anish.controllers;

import com.anish.data.DoubleHalf;
import com.anish.data.TableData;
import com.anish.data.Word;
import com.anish.helper.CommonHelper;
import com.anish.helper.IXLHelper;
import com.anish.helper.RedWordsHelper;
import com.anish.helper.VocabularyHelper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
public class BuildDataRestController {

	private String anish_name = "anish";
	private String ishant_name = "ishant";

	@Autowired
	private Environment env;
	
	@Autowired
	private RedWordsHelper redWordsHelper;
	
	@Autowired
	private IXLHelper ixlHelper;
	
	@Autowired
	private VocabularyHelper vocabularyHelper;
	
	@Autowired private CommonHelper commonHelper;
	
	@Autowired private TableController tableController;
	
	private Gson gson = new Gson();
	public static String destPath = "ixl/";
	
	@GetMapping(value = "/buildXmls/ixl/{name}")
	public String buildIxlXmls(@PathVariable("name") String name) {
		String rtn = "failed";
		try {
			if(name.contentEquals(anish_name)) {
				return ixlHelper.buildIxlXml(name);
			} else if(name.contentEquals(ishant_name)) {
				return ixlHelper.buildIxlXml(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	@GetMapping(value = "/buildXmls/vocabulary/{name}")
	public String buildVocabularyXmls(@PathVariable("name") String name) {
		String rtn = "failed";
		try {
			if(name.contentEquals(anish_name)) {
				return vocabularyHelper.buildVocabularyXmlNew(name);
			} else if(name.contentEquals(ishant_name)) {
				return vocabularyHelper.buildVocabularyXmlNew(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	//@GetMapping(value = "/buildXmls/vocabulary/{name}")
	public String buildVocabularyByDateXmls(@PathVariable("name") String name) {
		String rtn = "failed";
		try {
			if(name.contentEquals(anish_name)) {
				return vocabularyHelper.buildVocabularyXml(name);
			} else if(name.contentEquals(ishant_name)) {
				return vocabularyHelper.buildVocabularyXml(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	@GetMapping(value = "/buildXmls/redwords")
	public String buildRedWordsXmls() {
		String rtn = "failed";
		try {
			List<Word> currentListTemp = redWordsHelper.getRedWordsFile();
			List<Word> currentList = currentListTemp.stream().map(wrd -> {
				if(wrd.getSentencePath()==null) {
					wrd.setHasSentence(false);
				} else {
					wrd.setHasSentence(true);
				}
				return wrd;
			}).collect(Collectors.toList());
			Map<String, Word> wordMap = new HashMap<String, Word>();
			for(Word wrd: currentList) {
				if(!wordMap.containsKey(wrd.getName())) {
					wordMap.put(wrd.getName().toUpperCase(), wrd);
				} else {
					throw new RuntimeException("Name Exists:"+gson.toJson(wrd));
				}
			}
			if(wordMap.size()>0) {
				String envValX = env.getProperty("ishant.redwords.splitFiles");
				String[] splitFilesArr = envValX.split("#");				
				int stCounter = 0;
				Map<String, AtomicInteger> idCounter = new HashMap<String, AtomicInteger>();
				for(int i=0;i<splitFilesArr.length;i++) {
					redWordsHelper.writeQuestonToFile("<ReadWords>\n", splitFilesArr[i], ishant_name, StandardOpenOption.CREATE);
					idCounter.put(splitFilesArr[i], new AtomicInteger(1));
				}
				for(String key: wordMap.keySet()) {
					String wordString = redWordsHelper.getWordString(wordMap.get(key), idCounter.get(splitFilesArr[stCounter]), splitFilesArr[stCounter]);
					redWordsHelper.writeQuestonToFile(wordString, splitFilesArr[stCounter], ishant_name);
					stCounter++;
					if(stCounter >= splitFilesArr.length) {
						stCounter = 0;
					}
				}
				for(int i=0;i<splitFilesArr.length;i++) {
					redWordsHelper.writeQuestonToFile("</ReadWords>", splitFilesArr[i], ishant_name, StandardOpenOption.APPEND);
				}
				rtn = "success";
			}
		} catch (Exception e) {
			System.out.println("Error [question="+e+"]");
			e.printStackTrace();
			throw e;
		}
		return rtn;
	}
	@GetMapping(value = "/tables/generateTables/{name}")
    public String generateTables(@PathVariable("name") String name) {
		try {
			String fromVal = env.getProperty(name+".table.from");
			String[] fromValArr = fromVal.split("#");
			
			String toVal = env.getProperty(name+".table.to");
			String[] toValArr = toVal.split("#");
			
			AtomicInteger cnt = new AtomicInteger(1);
			List<TableData> rslt = new ArrayList<TableData>();
			for(int i=Integer.parseInt(fromValArr[0]);i<=Integer.parseInt(fromValArr[1]);i++) {
				for(int j=Integer.parseInt(toValArr[0]);j<=Integer.parseInt(toValArr[1]);j++) {
					TableData tableData = new TableData();
					tableData.setId(cnt.getAndIncrement());
					tableData.setChildName(name);
					tableData.setValue(i+" Times "+j);
					rslt.add(tableData);
				}
			}
			if(rslt.size()>0) {
				writeQuestionStatusToFile(rslt, name);
			}
			return "success";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return "failed";
    }
	public void writeQuestionStatusToFile(List<TableData> finalList, String personName) {
    	try {   
    		String done_file = "table_source_file.txt";
    		String pathVal = env.getProperty("destination.path");
    		String doneFilePath = pathVal+personName+"/writeXml/"+personName+"_"+done_file;
    		Path path = Path.of(commonHelper.getPathByOS(doneFilePath));
    		Files.deleteIfExists(path);
    		boolean flag = true;
    		for(TableData q: finalList) {
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

	@GetMapping(value = "/tables/generateDouble/{name}")
    public String generateDouble(@PathVariable("name") String name) {
		try {
			String doubleVal = env.getProperty(name+".double");
			String[] doubleValArr = doubleVal.split("#");
			List<DoubleHalf> rslt = new ArrayList<DoubleHalf>();
			AtomicInteger cnt = new AtomicInteger(1);
			for(int i = Integer.parseInt(doubleValArr[0]); i<Integer.parseInt(doubleValArr[1]) ; i++) {
				DoubleHalf d = new DoubleHalf();
				d.setIsDouble(Boolean.TRUE);
				d.setValue(i);
				d.setChildName(name);
				d.setId(cnt.getAndIncrement());
				rslt.add(d);
			}
			String halfVal = env.getProperty(name+".half");
			String[] halfValArr = halfVal.split("#");
			for(int i = Integer.parseInt(halfValArr[0]); i<Integer.parseInt(halfValArr[1]) ; i++) {
				int rsltVal = i%2;
				if(rsltVal==0) {
					DoubleHalf d = new DoubleHalf();
					d.setIsDouble(Boolean.FALSE);
					d.setValue(i);
					d.setChildName(name);
					d.setId(cnt.getAndIncrement());
					rslt.add(d);	
				}
			}
			if(rslt.size()>0) {
				writeDoubleStatusToFile(rslt, name);
			}
			return "success";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return "failed";
    }
	public void writeDoubleStatusToFile(List<DoubleHalf> finalList, String personName) {
    	try {   
    		String done_file = "_double_source_file.txt";
    		String pathVal = env.getProperty("destination.path");
    		String doneFilePath = pathVal+personName+"/writeXml/"+personName+done_file;
    		Path path = Path.of(commonHelper.getPathByOS(doneFilePath));
    		Files.deleteIfExists(path);
    		boolean flag = true;
    		for(DoubleHalf q: finalList) {
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
	
	@GetMapping(value = "/tables/generateTableTest1/{name}")
    public String generateTableTest(@PathVariable("name") String name) {
		try {
			//https://blog.aspose.com/words/create-rich-word-documents-programmatically-in-java-using-java-word-api/
			List<TableData> tableData = tableController.getFullList(name);
			if(tableData != null && tableData.size()>0) {
				// Create a Document object
				//generateDocFile(tableData, name);
				
				
				return "file_generation_success";
			} else {
				return "file_generation_failed";
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return "file_generation_failed";
    }
	
	/*
	private void generateDocFile(List<TableData> data, String name) {
		try {
			//https://blog.aspose.com/words/create-rich-word-documents-programmatically-in-java-using-java-word-api/
			if(data != null && data.size()>0) {
				// Create a Document object
				Document doc = new Document();
				// Create a DocumentBuilder object
				DocumentBuilder builder = new DocumentBuilder(doc);
				// Create table
				Table table = builder.startTable();
				// Insert a cell
				builder.insertCell();
				table.autoFit(AutoFitBehavior.AUTO_FIT_TO_WINDOW);
				builder.getCellFormat().setVerticalAlignment(CellVerticalAlignment.CENTER);
				builder.write("This is Row 1 Cell 1");
				builder.insertCell();
				builder.write("This is Row 1 Cell 2");
				// End row
				builder.endRow();
				// start a next row and set its properties
				builder.getRowFormat().setHeight(100);
				builder.getRowFormat().setHeightRule(HeightRule.EXACTLY);
				builder.insertCell();
				builder.write("This is Row 2 Cell 1");
				builder.insertCell();
				builder.write("This is Row 2 Cell 2");
				builder.endRow();
				// End table
				builder.endTable();
				// Save the document
				String pathVal = env.getProperty("destination.path");
	    		String doneFilePath = pathVal+name+"/ixl_results/Rich Word Document.docx";
				doc.save(doneFilePath);
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	*/
	
}
