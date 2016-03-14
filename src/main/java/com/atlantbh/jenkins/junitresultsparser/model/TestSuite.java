package com.atlantbh.jenkins.junitresultsparser.model;

import java.util.ArrayList;

/*
 * This class serves as container of test cases and gives additional info on test suite level
 */
public class TestSuite {

    private String name;
    private Integer tests;
    private Integer failures;
    private Integer skipped;
    private Double time;
    private ArrayList<TestCase> testCases = new ArrayList<TestCase>();

    public TestSuite() {
        super();
    }

    public ArrayList<TestCase> getTestCases() {
        return testCases;
    }

    public void addTestCase(TestCase testCase) {
        this.testCases.add(testCase);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTests() {
        return tests;
    }

    public void setTests(Integer tests) {
        this.tests = tests;
    }

    public Integer getFailures() {
        return failures;
    }

    public void setFailures(Integer failures) {
        this.failures = failures;
    }

    public Integer getSkipped() {
        return skipped;
    }

    public void setSkipped(Integer skipped) {
        this.skipped = skipped;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
