package com.anish.parsers;

import com.anish.data.Test2Question;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class Test2QuestionSaxParserHandler extends DefaultHandler {

    private List<Test2Question> questionList = null;
    private Test2Question question = null;
    private StringBuilder data = null;

    private boolean id = false;
    private boolean person = false;
    private boolean qText = false;
    private boolean status = false;

    public List<Test2Question> getQuestionList() {
        return questionList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("Test2-01")) {
            question = new Test2Question();
            if (questionList == null) {
                questionList = new ArrayList<>();
            }
        } else if (qName.equalsIgnoreCase("Id")) {
            id = true;
        } else if (qName.equalsIgnoreCase("Person")) {
            person = true;
        } else if (qName.equalsIgnoreCase("Question")) {
            qText = true;
        } else if (qName.equalsIgnoreCase("Status")) {
            status = true;
        }
        data = new StringBuilder();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (id) {
            try { question.setId(Integer.parseInt(data.toString().trim())); } catch (NumberFormatException e) { question.setId(0); }
            id = false;
        } else if (person) {
            question.setPerson(data.toString().trim());
            person = false;
        } else if (qText) {
            question.setQuestion(data.toString().trim());
            qText = false;
        } else if (status) {
            try { question.setStatus(Integer.parseInt(data.toString().trim())); } catch (NumberFormatException e) { question.setStatus(0); }
            status = false;
        } else if (qName.equalsIgnoreCase("Test2-01")) {
            questionList.add(question);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }
}

