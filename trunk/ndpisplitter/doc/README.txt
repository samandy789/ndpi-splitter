This is the Ndpi Splitter application. It depends on the ndpi-reader and intersect-common projects.

Build targets of note:
	- all    compile, checkstyle, unit tests and coverage report
	- dist   build the distributable 
	
The user instructions contains information on external packages that are required to be installed on your system.
This software only runs on Windows due to the dependency on the Hamamatsu SDK which is a Windows DLL

Dependencies:
	- Java 6 or higher
	- jna for accessing the dll
	- swt for user interface
	- Hamamatsu SDK DLL
	- Java image IO and Java advanced imaging installed in the JRE
