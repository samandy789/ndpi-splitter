One-time installation steps:
  * Install JAI into your JRE by running ndpisplitter/install/jai_imageio-1_1-lib-windows-i586-jre.exe


Build steps:

Because this uses SWT, it has to be built differently for different operating systems. 
Currently only 32-bit windows and mac (cocoa) are supported. More can easily be added, 
however it will only work properly on windows because the NDPI SDK is a windows-only DLL.

To build the distributable for windows:
 > ant dist
 
Or for mac, 
 > ant -Dtarget-os=mac dist
 
This creates a "dist" folder will all the artefacts you need. You can copy the contents of the dist folder
elsewhere and the application should run ok. There will be a runnable jar in ndpisplitter/build/ant/dist/NdpiSplitter.jar. 
You should be able to double click to run the app. 

To run checkstyle, tests etc, use the "all" or "hudson-all" targets.