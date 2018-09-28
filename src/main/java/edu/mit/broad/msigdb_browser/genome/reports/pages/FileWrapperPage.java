/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.genome.reports.pages;

import java.io.File;

public class FileWrapperPage implements Page {

    private File fFile;

    /**
     * Class constructor
     * @param name
     */
    public FileWrapperPage(File file) {
        this.fFile = file;
    }

    public File getFile() {
        return fFile;
    }

} // End class FileWrapperPage
