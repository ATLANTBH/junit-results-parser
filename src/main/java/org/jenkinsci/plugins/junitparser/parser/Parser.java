package org.jenkinsci.plugins.junitparser.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jenkinsci.plugins.junitparser.model.TestCase;
import org.jenkinsci.plugins.junitparser.model.TestStep;
import org.jenkinsci.plugins.junitparser.model.TestSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Parser {
	
    private TestSuite testSuite;
    private ArrayList<TestCase> testCases;
	
	public Parser() {
		super();
		testSuite = new TestSuite();
		testCases = new ArrayList<TestCase>();
	}
	
	public TestSuite getTestSuite() {
		return this.testSuite;
	}
	
	public NodeList getStartNode(String fileLocation) throws Exception {
		File fXmlFile = new File(fileLocation);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		NodeList nodeList = null;
		try {
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			nodeList = doc.getElementsByTagName("testsuite");
		} catch (ParserConfigurationException e) {
			e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			e.getMessage();
			e.printStackTrace();
		}
		
		if (nodeList == null) {
			throw new Exception("ERROR: Data not populated correctly! Please check your input file location and format");
		} else {
			return nodeList;
		}
	}
	
	public void addTestCasesToTestSuite() {
		for (TestCase tc : this.testCases) {
			this.testSuite.addTestCase(tc);
		}
	}
	
	public TestStep setFailureMessage(TestStep testStep, Element element) {
		NodeList childNodeList = element.getChildNodes();
	    for (int j = 0; j < childNodeList.getLength(); j++) {
	    	Node childNode = childNodeList.item(j);
	    	if (childNode.getNodeType() == Node.ELEMENT_NODE) {
	    		Element childElement = (Element) childNode;
	    		if (childNode.getNodeName() == "failure") {
	    			testStep.setFailureMessage(childElement.getAttribute("message"));
	    		}    		
	    	}
	    }
	    return testStep;
	}
	
	public void parseJUnitResults(NodeList nodeList) throws IOException, ParserConfigurationException {
			
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				
				Element element = (Element) node;
				if (node.getNodeName() == "testsuite") {
					this.testSuite.setName(element.getAttribute("name"));
					this.testSuite.setTests(element.getAttribute("tests"));
					this.testSuite.setFailures(element.getAttribute("failures"));
					this.testSuite.setTime(element.getAttribute("time"));	
				}

				if (node.getNodeName() == "testcase") {
					TestCase testCase = new TestCase();
					testCase.setClassName(element.getAttribute("classname"));
					TestStep testStep = new TestStep();
				    testStep.setName(element.getAttribute("name"));
				    testStep.setTime(element.getAttribute("time"));
					
					if (this.testCases.isEmpty()) {
						testStep = setFailureMessage(testStep, element);
					    testCase.addTestStep(testStep);
					    this.testCases.add(testCase);
					} else {
						boolean exists = false;
						for (TestCase tCase : this.testCases) {
	  			  		    if (tCase.getClassName().equals(testCase.getClassName())) {
								testStep = setFailureMessage(testStep, element);
							    tCase.addTestStep(testStep);
							    exists = true;
							    break;
							}
						}
						if (exists == false) {
							testStep = setFailureMessage(testStep, element);
						    testCase.addTestStep(testStep);
						    this.testCases.add(testCase);
						}
					}
				}				
			}
			
			if (node.hasChildNodes()) {
				parseJUnitResults(node.getChildNodes());
			}
		}
	}
}
