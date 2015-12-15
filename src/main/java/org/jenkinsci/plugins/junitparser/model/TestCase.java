package org.jenkinsci.plugins.junitparser.model;

import java.util.ArrayList;
import java.util.Map;

/*
 * This class serves as container of test steps and gives additional info on test case level
 */
public class TestCase {
	private String className;
	private ArrayList<TestStep> testSteps = new ArrayList<TestStep>();
	private boolean failed;
	
	public TestCase() {
		super();
	}
	
	public ArrayList<TestStep> getTestSteps() {
		return testSteps;
	}
	
	public void addTestStep(TestStep testStep) {
        for (Map.Entry<String, String> entry : testStep.getAssertionFailuresList().entrySet()) {
            if (entry.getKey() != null) {
                if (!entry.getKey().isEmpty()) {
                    failed = true;
                }
            }
        }
		this.testSteps.add(testStep);
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public boolean isFailed() {
		return failed;
	}
}