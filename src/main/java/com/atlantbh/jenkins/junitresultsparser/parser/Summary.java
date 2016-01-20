package com.atlantbh.jenkins.junitresultsparser.parser;

import com.atlantbh.jenkins.junitresultsparser.model.TestSuite;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by bjusufbe on 25.12.15..
 * Class used to aggregate summary results that will be shown on the output jenkins page
 */
public class Summary {

    private int totalSuccess;
    private int totalFailed;
    private int totalTests;
    private double totalExecutionTime;
    private ArrayList<TestSuite> testSuites = new ArrayList<TestSuite>();

    public Summary() {
        super();
    }

    public int getTotalSuccess() {
        return totalSuccess;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public double getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public void addTestSuite(TestSuite testSuite) {
        this.testSuites.add(testSuite);
    }

    public void calculateSummaryResults() {
        for (TestSuite testSuite : this.testSuites) {
            totalTests = totalTests + Integer.parseInt(testSuite.getTests());
            totalFailed = totalFailed + Integer.parseInt(testSuite.getFailures());
            totalExecutionTime = totalExecutionTime + Double.parseDouble(testSuite.getTime());
        }
        totalSuccess = totalTests - totalFailed;
    }

    public Double getSuccessRate() {
        return Double.parseDouble(new DecimalFormat("###.##").format((getTotalSuccess()/(double)getTotalTests())*100));
    }
}
