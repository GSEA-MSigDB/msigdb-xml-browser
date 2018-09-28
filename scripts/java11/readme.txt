=======================
MSigDB XML Browser Binary Distribution
=======================

Prerequisites:

Java 11 (http://openjdk.java.net).  This is bundled with our distributions.
Not compatible with Java 8, 9, 10.


Instructions:

1. Download and unzip the distribution file to a directory of your choice.

2. To start the MSigDB XML Browser execute the following from the command line,

     java --module-path=modules -Xmx4g @msigdb-xml-browser.args --patch-module=jide.common=lib/jide-components-3.7.4.jar:lib/jide-dock-3.7.4.jar:lib/jide-grids-3.7.4.jar  --module=org.gsea-msigdb.msigdb-xml-browser/xapps.browser.gsea.MSigDB_XML_Browser

Note that the command line has become more complex with Java 11 compared to Java 8.
Alternatively, you can start the MSigDB XML Browser with one of the following scripts; 
this is recommended.  Some of these may not be present depending on the distribution 
you downloaded.  You might have to make the script executable 
(chmod a+x msigdb-xml-browser.sh).


msigdb-xml-browser.bat       (for Windows)
msigdb-xml-browser.sh        (for Linux and macOS)
msigdb-xml-browser-hidpi.sh  (for Linux with HiDPI screens)
msigdb-xml-browser.command   (for macOS, double-click to start)

The bat and shell scripts are configured to start the MSigDB XML Browser
with 4GB of memory.  This is a reasonable default for most machines.  If
you are working with very large datasets you can increase the amount of
memory available to the MSigDB XML Browser by editing the first line of
the startup script.
Specifically change the value of the "-Xmx" parameter.  For example,
to start the MSigDB XML Browser with 8 GB of memory, change the value

   -Xmx4g

to

   -Xmx8g

