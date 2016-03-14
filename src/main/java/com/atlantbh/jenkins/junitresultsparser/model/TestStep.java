package com.atlantbh.jenkins.junitresultsparser.model;

import java.util.HashMap;
import java.util.Map;

/*
 * This class is used for test steps information
 */
public class TestStep {

    private String name;
    private String time;
    private Map<String, String> assertionFailuresList = new HashMap<String, String>();
    private Boolean skipped = false;

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

    public Map<String, String> getAssertionFailuresList() {
        return assertionFailuresList;
    }

    public void setAssertionFailures(String message, String value) {
        this.assertionFailuresList.put(message, value);
    }

    public Boolean isSkipped() {
        return skipped;
    }

    public void setSkipped(Boolean skipped) {
        this.skipped = skipped;
    }
}
