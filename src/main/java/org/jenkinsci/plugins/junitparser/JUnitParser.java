package org.jenkinsci.plugins.junitparser;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.Builder;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import org.jenkinsci.plugins.junitparser.parser.Summary;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.w3c.dom.NodeList;

import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.List;

import org.jenkinsci.plugins.junitparser.model.TestCase;
import org.jenkinsci.plugins.junitparser.model.TestStep;
import org.jenkinsci.plugins.junitparser.parser.Parser;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link JUnitParser} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform} method will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class JUnitParser extends Recorder {

    private final String name;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public JUnitParser(String name) {
        this.name = name;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean perform(AbstractBuild<?,?> build, Launcher launcher, BuildListener listener) {
    	Parser parser = new Parser();
        Summary summary = new Summary();
		NodeList nodeList;
		try {
            List<String> fileLocations = Arrays.asList(name.split(","));
            for (String fileLocation : fileLocations) {
                nodeList = parser.getStartNode(fileLocation);
                parser.parseJUnitResults(nodeList);
                summary.addTestSuite(parser.getTestSuite());
            }
            parser.addTestCasesToTestSuite();
            summary.calculateSummaryResults();
			
			// BAKIR: Added for purposes of getting these data to front-end. Once implemented, delete it!			
			listener.getLogger().println("Test suite: " + parser.getTestSuite().getName());
			listener.getLogger().println("---------");

			for (TestCase testCase : parser.getTestSuite().getTestCases()) {
				listener.getLogger().println("Test case: " + testCase.getClassName());
				listener.getLogger().println("Test case failed: " + testCase.isFailed());
			    for (TestStep testStep : testCase.getTestSteps()) {
                    listener.getLogger().println("Test step: " + testStep.getName() + "..." + testStep.getTime());
                    for (Map.Entry<String, String> entry : testStep.getAssertionFailuresList().entrySet()) {
                        listener.getLogger().println("Message: " + entry.getKey());
                        listener.getLogger().println("Value: " + entry.getValue());
                    }
			    }
			    listener.getLogger().println("---------");
			}
	    	
	    	// Code added for implementing the buildAction screen
	    	JUnitParserBuildAction buildAction = new JUnitParserBuildAction(parser.getTestSuite(), summary, build);
	    	build.addAction(buildAction);
	    	
		} catch (Exception e) {
			listener.getLogger().println(e.getMessage());
			listener.getLogger().println(e.getStackTrace());
		}
		return true;
    }
    
    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }


    /**
     * Descriptor for {@link JUnitParser}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use <tt>transient</tt>.
         */

        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         *      <p>
         *      Note that returning {@link FormValidation#error(String)} does not
         *      prevent the form from being saved. It just means that a message
         *      will be displayed to the user. 
         */
        public FormValidation doCheckName(@QueryParameter String value)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error("Please set correct file location");
            if (value.length() > 0) {
                List<String> filesList = Arrays.asList(value.split(","));
                for (String file : filesList) {
                    File inputFile = new File(file);
                    if (!inputFile.isFile()) {
                      return FormValidation.error("Please set correct file location");
                    }
                }
            }
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Publish JUnit test results";
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }
    }

	@Override
	public BuildStepMonitor getRequiredMonitorService() {
		// TODO Auto-generated method stub
		return BuildStepMonitor.NONE;
	}
}

