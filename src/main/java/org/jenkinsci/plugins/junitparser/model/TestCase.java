package org.jenkinsci.plugins.junitparser.model;

/*
 * This class will be used as list of test steps where each test step which as same className
 * will belong to the same test case
 * name is test step name while className is test case name
 */
public class TestCase {
	
	private String className;
	private String name;
	private String time;
	private String failureMessage;
	
	public TestCase() {
		super();
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getFailureMessage() {
		return failureMessage;
	}
	
	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}
}
