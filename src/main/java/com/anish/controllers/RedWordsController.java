package com.anish.controllers;

import com.anish.data.Word;
import com.anish.parsers.ReadWordSaxParserHandler;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
public class RedWordsController {
	
	//https://ttsmp3.com/
	@Autowired private Environment env;
		
	private Gson gson = new Gson();
	
	private String anish_name = "anish";
	private String ishant_name = "ishant";
	
	private String[] getNextWord(Integer fileNo) {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("audio/redwords/ishant/"+fileNo+".mp3");
		File fObj = new File(resource.getFile());
		String[] arr = fObj.getAbsolutePath().split("anish-project-new");
		String[] strArr = new String[2];
		strArr[0] = "file://"+arr[0]+"anish-project-new/src/main/resources/audio/"+fileNo+".mp3";
		strArr[1] = "file://"+arr[0]+"anish-project-new/src/main/resources/audio/"+fileNo+"_sentence.mp3";
		return strArr;
	}
	
	public void writeWordRecordToFile(String filePath, Word word) {
    	try {   
    		Path path = Path.of(filePath);
    		Files.writeString(path, "\n"+gson.toJson(word), StandardOpenOption.APPEND);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
	
	public void groupData(List<Word> wordList) {
		wordList.stream().forEach(word -> {
			word.setTotalWords(wordList.size());
		});
	}
	
	public List<Word> getReadWordDoneData(String filePath) {
		List<Word> lines = new ArrayList<Word>();
		Scanner myReader = null;
		String val = null;
    	try {
    		File myObj = new File(filePath);
    		myReader = new Scanner(myObj);
    		while (myReader.hasNextLine()) {
    			val = myReader.nextLine();
    			if(!StringUtils.isEmpty(val) && val.length()>0) {
        			lines.add(gson.fromJson(val, Word.class));
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
	
	@GetMapping(value = "/redwords/takeTest/{name}/{fileName}")
    public ModelAndView getTakeTest(@PathVariable("name") String name, @PathVariable("fileName") String fileName) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			List<Word> currentList = getRedWordsFile(fileName, name);
			String path = env.getProperty("destination.path");
			String doneFilePath = path+name+"/clean/"+fileName+"_done.txt";
			List<Word> doneList = getReadWordDoneData(doneFilePath);
			List<Integer> doneIds = new ArrayList<Integer>();
			if(doneList.size()>0) {
				for(Word wrd: doneList) {
					doneIds.add(wrd.getId());
				}	
			}
			List<Word> finalList = new ArrayList<Word>();
			for(Word wrd: currentList) {
				if(!doneIds.contains(wrd.getId())) {
					if(wrd.getSentence().equalsIgnoreCase("Nothing")) {
						wrd.setSentence("");
					}
					finalList.add(wrd);
				}
			}
			if(finalList.size()>0) {
				modelAndView.addObject("testName", finalList.get(0).getTestName());
				modelAndView.addObject("redWordsExists", "true");
				modelAndView.addObject("subList", finalList);
				/*
				if(finalList.size()==1) {
					modelAndView.addObject("subList1", finalList);
					modelAndView.addObject("subList2", new ArrayList<Word>());
				} else {
					List<Word> subList1 = finalList.subList(0, finalList.size()/2);
					List<Word> subList2 = finalList.subList((finalList.size()/2)+1, finalList.size());
					modelAndView.addObject("subList1", subList1);
					modelAndView.addObject("subList2", subList2);
				}
				*/
			}
			modelAndView.addObject("user_name_key", name);
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("read_word_test_new");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
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
	
	public Word getAllReadWordRecords(boolean next, Integer id, String spellValue, String name, String fileName) {
		String path = env.getProperty("destination.path");
		String doneFilePath = path+name+"/clean/"+fileName+"_done.txt";
		String xmlFilePath = "xmldata/"+fileName+".xml";
		List<Word> currentList = getRedWordsFile(xmlFilePath, name);
		if(next) {
			Optional<Word> weekOpt = currentList.stream().filter(x -> {
				if(x.getId()==id) {
					return true;
				}
				return false;
			}).findFirst();
			Word modifiedWord = weekOpt.get();
			modifiedWord.setRepeat(spellValue);
			writeWordRecordToFile(doneFilePath, modifiedWord);
		}
		List<Word> doneList = getReadWordDoneData(doneFilePath);
		Set<Integer> doneSet = doneList.stream().map(x -> {
			return x.getId();
		}).collect(Collectors.toSet());
		List<Word> finalCurrentList = currentList.stream()
				.filter(x -> {
					return !doneSet.contains(x.getId());
				}).collect(Collectors.toList());
		return finalCurrentList.get(0);
    }
	
	@GetMapping(value = "/redwords/doneword/{id}/{fileName}")
    public ModelAndView completeWord(@PathVariable("id") String id, @PathVariable("fileName") String fileName) {
		try {
			String path = env.getProperty("destination.path");
			String doneFilePath = path+ishant_name+"/clean/"+fileName+"_done.txt";
			List<Word> currentList = getRedWordsFile(fileName, ishant_name);
			Word myWord = null;
			for(Word wrd: currentList) {
				if(wrd.getId()==Integer.parseInt(id)) {
					myWord = wrd;
				}
			}
			writeWordRecordToFile(doneFilePath, myWord);
			return getTakeTest(ishant_name, fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return null;
    }
	
	@RequestMapping(value = "/redwords/submitWord", method = RequestMethod.POST)
    public ModelAndView completeWord(@ModelAttribute("word") Word word) {
		try {
			if(word.getIshantWrote()!=null) {
				List<Word> currentList = getRedWordsFile(String.valueOf(word.getFileNo()), word.getPersonName());
				Word myWord = null;
				if(currentList != null && currentList.size()>0) {
					for(Word w: currentList) {
						if(w.getId()==word.getId()) {
							myWord = w;
							myWord.setIshantWrote(word.getIshantWrote());
							break;
						}
					}	
				}
				if(myWord != null) {
					String path = env.getProperty("destination.path");
					String doneFilePath = path+ishant_name+"/clean/"+word.getFileNo()+"_done.txt";
					writeWordRecordToFile(doneFilePath, myWord);
					return getTestLink(word.getPersonName(), String.valueOf(word.getFileNo()));	
				}
			}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return null;
    }

	
	@GetMapping(value = "/redwords/getTestLink/{personName}/{fileName}")
	public ModelAndView getTestLink(@PathVariable("personName") String personName, @PathVariable("fileName") String fileName) {
		ModelAndView modelAndView = new ModelAndView("redword_test");
		try {
			Word word = getNextWord(fileName, personName);
			if(word != null) {
				modelAndView.addObject("word", word);
				modelAndView.addObject("isWord", Boolean.TRUE);
			}
			modelAndView.addObject("user_name_key", personName);
			modelAndView.setStatus(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
	}
	
	private Word getNextWord(String fileName, String personName) {
		List<Word> currentList = getRedWordsFile(fileName, personName);
		String path = env.getProperty("destination.path");
		String doneFilePath = path+personName+"/clean/"+fileName+"_done.txt";
		List<Word> doneList = getReadWordDoneData(doneFilePath);
		List<Integer> doneIds = new ArrayList<Integer>();
		if(doneList.size()>0) {
			for(Word wrd: doneList) {
				doneIds.add(wrd.getId());
			}	
		}
		List<Word> finalList = new ArrayList<Word>();
		for(Word wrd: currentList) {
			if(!doneIds.contains(wrd.getId())) {
				if(wrd.isHasSentence() && wrd.getSentence().equalsIgnoreCase("Nothing")) {
					wrd.setSentence("");
				}
				finalList.add(wrd);
			}
		}
		if(finalList.size()>0) {
			Word finalWord = finalList.get(0);
			finalWord.setWordsLeft(finalList.size());
			return finalWord;
		}
		return null;
	}
	
	@GetMapping(value = "/redwords/getResults/{personName}/{fileName}")
	public ModelAndView getResults(@PathVariable("personName") String personName, @PathVariable("fileName") String fileName) {
		ModelAndView modelAndView = new ModelAndView("redword_test");
		try {
			Word word = getNextWord(fileName, personName);
			if(word != null) {
				modelAndView.addObject("word", word);
				modelAndView.addObject("isWord", Boolean.TRUE);
			}
			modelAndView.addObject("user_name_key", personName);
			modelAndView.setStatus(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
	}
	
	@GetMapping(value = "/redwords/results/{personName}/{fileNo}")
	public ModelAndView getCurrentQuestions(@PathVariable("personName") String personName, @PathVariable("fileNo") String fileNo) {
		ModelAndView modelAndView = new ModelAndView("redwords_results");
		try {
			String path = env.getProperty("destination.path");
			String doneFilePath = path+personName+"/clean/"+fileNo+"_done.txt";
			List<Word> doneList = getReadWordDoneData(doneFilePath);
			AtomicInteger correctCnt = new AtomicInteger(0);
			AtomicInteger notAnsweredCnt = new AtomicInteger(0);
			AtomicInteger wrongCnt = new AtomicInteger(0);
			doneList.stream().forEach(w -> {
				if(w == null || StringUtils.isEmpty(w.getIshantWrote())) {
					w.setRightOrWrong("Not Answered");
					notAnsweredCnt.getAndIncrement();
				} else {
					String ishantWrote = w.getIshantWrote().trim();
					String actualName = w.getName().trim();
					if(ishantWrote.equalsIgnoreCase(actualName)) {
						w.setRightOrWrong("Correct");
						correctCnt.getAndIncrement();
					} else if(!ishantWrote.equalsIgnoreCase(actualName)) {
						w.setRightOrWrong("Wrong");
						wrongCnt.getAndIncrement();
					}
				}
			});
			if (doneList.size() > 0) {
				modelAndView.addObject("data", doneList);
				modelAndView.addObject("correctCnt", correctCnt.get());
				modelAndView.addObject("notAnsweredCnt", notAnsweredCnt.get());
				modelAndView.addObject("wrongCnt", wrongCnt.get());
			}
			modelAndView.addObject("user_name_key", personName);
			modelAndView.setStatus(HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
}
