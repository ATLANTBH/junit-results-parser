AtlantBH Jenkins plugin: JUnit Results Parser
=======================
  
What is it?
----------------------

A Jenkins post-build plugin used for visual representation of JUnit test results adapted for functional testing.


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
Copyright 2016 AtlantBH
[http://opensource.org/licenses/MIT MIT License]
  
  
