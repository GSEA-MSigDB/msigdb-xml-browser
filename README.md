#  Introduction

The MSigDB XML Browser is a free utility for exploring the MSigDB XML file.  It is written in the Java(tm) language and was formerly part of the [GSEA Desktop](https://github.com/GSEA-MSigDB/gsea-desktop).  These have been split into separate programs to better facilitate the restructuring and future maintenance of both.   

See the [GSEA website](http://www.gsea-msigdb.org) for more details.  An earlier version of the User Guide for the GSEA Desktop can be found in the Documentation section.  This guide includes information on the MSigDB XML Browser that remains largely accurate; it is known as the "MSigDB Browser" in that document.

# License

The MSigDB XML Browser is made available under the terms of a BSD-style license, a copy of which is included in the distribution in the [LICENSE.txt](LICENSE.txt) file.  See that file for exact terms and conditions.

#  Latest Version
The latest binary release of this software can be obtained from the [Downloads page of the GSEA website](http://www.gsea-msigdb.org/gsea/downloads.jsp).

If you have any comments, suggestions or bugs to report, please see our [Contact page](http://www.gsea-msigdb.org/gsea/contact.jsp) for information on how to reach us.

# History and Acknowledgements

The **MSigDB XML Browser** was developed as a component of the **GSEA Desktop application version 1.0** by Aravind Subramanian as part of his PhD thesis.  The work was supported by the Broad Institute of MIT and Harvard and advised by Jill Mesirov, Pablo Tamayo, Vamsi Mootha, Sayan Mukherjee, Todd Golub and Eric Lander.

Joshua Gould (code) and Heidi Kuehn (docs) contributed greatly to **GSEA Desktop 2.0**.  There were additional code contributions by Michael Angelo, Chet Birger, Justin Guinney, Keith Ohm, and Michael Reich.  

**MSigDB XML Browser 1.0** is the open-source release.  Note that the package hierarchy and other organization has been separated from that of the GSEA Desktop in a somewhat artificial manner (by introducing intermediate packages with "browser" in the name) in order to keep the two code bases from overlapping.  There is a lot of duplication between the two but also a lot of change, so this separation allows the code to change independently.

David Eby was responsible for the open-source conversion and handles current maintenance and new feature development.  While David is listed on the initial commit to this public GitHub repository, original authorship is due to the individuals listed above regardless of the GitHub history metadata.

The initial GitHub commit roughly corresponds to the **MSigDB XML Browser version 1.0 Beta 1** release of March 31, 2017 with a few minor changes. The earlier code revision history is not available.

The GSEA project is currently a joint effort of the Broad Institute and the University of California San Diego, and funded by the National Cancer Institute of the National Institutes of Health (PI: JP Mesirov).

# Dependencies

GSEA Desktop is 100% Pure Java.  Java 8 is required for our pre-built binaries.  Builds against other versions of Java may be possible but are unsupported.  **Oracle Java is recommended as there are known issues when running with OpenJDK.**

See the [LICENSE-3RD-PARTY.txt](LICENSE-3RD-PARTY.txt) file for a full list of the MSigDB XML Browser library dependencies.  In our binary builds, all required 3rd party library code is bundled into the single self-contained MSigDB XML Browser jar file so that no additional downloads or installation are required. 

------
Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
