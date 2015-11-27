package org.jenkinsci.plugins.junitparser;

import org.jenkinsci.plugins.junitparser.model.TestSuite;

import hudson.model.AbstractBuild;
import hudson.model.Action;

public class JUnitParserBuildAction implements Action {
	private AbstractBuild<?, ?> build;
	private TestSuite testSuite;
	private String message = "Poz.";
	
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
    
    public int getBuildNumber() {
        return this.build.number;
    }
    
    public String getMessage() {
    	return message;
    }
    
    JUnitParserBuildAction(final TestSuite testSuite, final AbstractBuild<?, ?> build)
    {
        this.testSuite = testSuite;
        this.build = build;
    }
}
