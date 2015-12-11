package org.jenkinsci.plugins.junitparser.model;

/*
 * This class is used for test steps information
 */
public class TestStep {
	
	private String name;
	private String time;
	private String failureMessage;
	
	public TestStep() {
		super();
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
