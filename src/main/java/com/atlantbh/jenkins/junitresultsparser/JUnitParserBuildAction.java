package com.atlantbh.jenkins.junitresultsparser;

import com.atlantbh.jenkins.junitresultsparser.parser.Summary;
import com.atlantbh.jenkins.junitresultsparser.model.TestSuite;

import hudson.model.AbstractBuild;
import hudson.model.Action;

public class JUnitParserBuildAction implements Action {
    private AbstractBuild<?, ?> build;
    private TestSuite testSuite;
    private Summary summary;

    @Override
    public String getIconFileName() {
        return "clipboard.png";
    }

    @Override
    public String getDisplayName() {
        return "JUnitParser Results page";
    }

    @Override
    public String getUrlName() {
        return "resultsPage";
    }

    public TestSuite getTestSuite() {
        return testSuite;
    }

    public Summary getSummary() {
        return summary;
    }

    public int getBuildNumber() {
        return this.build.number;
    }

    JUnitParserBuildAction(final TestSuite testSuite, final Summary summary, final AbstractBuild<?, ?> build) {
        this.testSuite = testSuite;
        this.summary = summary;
        this.build = build;
    }
}
