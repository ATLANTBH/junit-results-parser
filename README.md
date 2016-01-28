AtlantBH Jenkins plugin: JUnit Results Parser
=======================
  
What is it?
----------------------

A Jenkins post-build plugin used for visual representation of JUnit test results adapted for functional testing.
Test results follow test suite/test case/test step pattern which is easier to follow when writing functional tests. For example: Test suite is smoke test, test case is the name of the smoke test and test steps is the list of steps that will be performed in this test case. One test suite has one or many test cases while one test case has one or many test steps. Conversion from JUnit format to suite/case/step pattern is done automatically in this plugin and then visualized in plugins output in post-build.


Requirements
-----------------------

-	Java 1.6 or greater

-	Maven 3.0 or greater


Build Instructions
-----------------------

-	Go to the top-level directory of the project and run:  
	```
	mvn package
	```
-	'target/*.hpi' file should be generated 


Installation Instructions
-----------------------
	 
-	*.hpi file can be uploaded to Jenkins and installed using the web UI

-	Alternatively, *.hpi file can be added to $JENKINS_HOME/plugins

-	Plugin can be tested by adding 'Publish JUnit test results' post-build action to a job and passing JUnit *.xml file


Licensing and legal issues
-----------------------
Copyright 2016 AtlantBH, [MIT License] (http://opensource.org/licenses/MIT)
  
  
