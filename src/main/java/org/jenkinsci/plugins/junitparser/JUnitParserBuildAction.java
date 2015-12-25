package org.jenkinsci.plugins.junitparser;

import org.jenkinsci.plugins.junitparser.model.TestSuite;
import org.jenkinsci.plugins.junitparser.parser.Summary;

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

    public Summary getSummary() { return summary; }
    
    public int getBuildNumber() {
        return this.build.number;
    }
    
    JUnitParserBuildAction(final TestSuite testSuite, final Summary summary, final AbstractBuild<?, ?> build)
    {
        this.testSuite = testSuite;
        this.summary = summary;
        this.build = build;
    }
}