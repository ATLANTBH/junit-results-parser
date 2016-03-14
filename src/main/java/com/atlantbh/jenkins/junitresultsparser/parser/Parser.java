package com.atlantbh.jenkins.junitresultsparser.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.atlantbh.jenkins.junitresultsparser.model.TestStep;
import com.atlantbh.jenkins.junitresultsparser.model.TestCase;
import com.atlantbh.jenkins.junitresultsparser.model.TestSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Parser {

    private TestSuite testSuite;
    private ArrayList<TestCase> testCases;

    private static final String testSuiteNodeName = "testsuite";
    private static final String failureNodeName = "failure";
    private static final String skippedNodeName = "skipped";
    private static final String failureMessageAttribute = "message";
    private static final String testSuiteNameAttribute = "name";
    private static final String testSuiteTestsAttribute = "tests";
    private static final String testSuiteFailuresAttribute = "failures";
    private static final String testSuiteTimeAttribute = "time";

    private static final String testCaseNodeName = "testcase";
    private static final String testCaseClassnameAttribute = "classname";
    private static final String testCaseNameAttribute = "name";
    private static final String testCaseTimeAttribute = "time";

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
            nodeList = doc.getElementsByTagName(testSuiteNodeName);
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

            if (childNode.getNodeName().equals(skippedNodeName)) {
                testStep.setSkipped(true);
                return testStep;
            }

            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) childNode;

                if (childNode.getNodeName().equals(failureNodeName)) {
                    testStep.setAssertionFailures(childElement.getAttribute(failureMessageAttribute), childElement.getTextContent());
                }
            }
        }
        return testStep;
    }

    public void parseJUnitResults(NodeList nodeList) throws Exception {

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                if (node.getNodeName().equals(testSuiteNodeName)) {
                    this.testSuite.setName(element.getAttribute(testSuiteNameAttribute));
                    try {
                        this.testSuite.setTests(Integer.parseInt(element.getAttribute(testSuiteTestsAttribute)));
                        this.testSuite.setFailures(Integer.parseInt(element.getAttribute(testSuiteFailuresAttribute)));
                        this.testSuite.setTime(Double.parseDouble(element.getAttribute(testSuiteTimeAttribute)));

                        if (element.getAttribute(skippedNodeName).isEmpty()) {
                            this.testSuite.setSkipped(0);
                        } else {
                            this.testSuite.setSkipped(Integer.parseInt(element.getAttribute(skippedNodeName)));
                        }
                    }
                    catch (NumberFormatException e) {
                        throw new Exception("ERROR: Not able to parse attribute: " + e.getMessage() + "! Please check your input file format");
                    }
                }

                if (node.getNodeName().equals(testCaseNodeName)) {
                    TestCase testCase = new TestCase();
                    testCase.setClassName(element.getAttribute(testCaseClassnameAttribute));
                    TestStep testStep = new TestStep();
                    testStep.setName(element.getAttribute(testCaseNameAttribute));
                    testStep.setTime(element.getAttribute(testCaseTimeAttribute));

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
                        if (!exists) {
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
