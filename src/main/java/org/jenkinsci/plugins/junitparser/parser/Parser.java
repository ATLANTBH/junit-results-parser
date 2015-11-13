package org.jenkinsci.plugins.junitparser.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jenkinsci.plugins.junitparser.model.TestCase;
import org.jenkinsci.plugins.junitparser.model.TestSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Parser {
	
    private TestSuite testSuite;
	
	public Parser() {
		super();
		testSuite = new TestSuite();
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
			throw new Exception("Node list has not been populated!");
		} else {
			return nodeList;
		}
	}
	
	public void parseXml(NodeList nodeList) throws IOException, ParserConfigurationException {
			
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
				    testCase.setName(element.getAttribute("name"));
				    testCase.setTime(element.getAttribute("time"));

				    NodeList childNodeList = element.getChildNodes();
				    for (int j = 0; j < childNodeList.getLength(); j++) {
				    	Node childNode = childNodeList.item(j);
				    	if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				    		Element childElement = (Element) childNode;
				    		if (childNode.getNodeName() == "failure") {
				    			testCase.setFailureMessage(childElement.getAttribute("message"));
				    		}    		
				    	}
				    }
				    this.testSuite.addTestCase(testCase);				    
				}				
			}
			
			if (node.hasChildNodes()) {
				parseXml(node.getChildNodes());
			}
		}
	}
}
