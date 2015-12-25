package org.jenkinsci.plugins.junitparser.parser;

import org.jenkinsci.plugins.junitparser.model.TestSuite;
import java.util.ArrayList;

/**
 * Created by bjusufbe on 25.12.15..
 * Class used to aggregate summary results that will be shown on the output jenkins page
 */
public class Summary {

    private int totalSuccess;
    private int totalFailed;
    private int totalTests;
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

    public void addTestSuite(TestSuite testSuite) {
        this.testSuites.add(testSuite);
    }

    public void calculateSummaryResults() {
        for (TestSuite testSuite : this.testSuites) {
            totalTests = totalTests + Integer.parseInt(testSuite.getTests());
            totalFailed = totalFailed + Integer.parseInt(testSuite.getFailures());
        }
        totalSuccess = totalTests - totalFailed;
    }
}
