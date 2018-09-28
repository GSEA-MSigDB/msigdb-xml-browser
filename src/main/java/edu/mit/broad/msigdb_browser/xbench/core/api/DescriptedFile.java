/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.core.api;

import java.io.File;

/**
 * @author Aravind Subramanian
 */
//TODO: Almost certainly is not used.  Remove if possible.
// Proved this for the Desktop; just need to do the same here.
public class DescriptedFile extends File {

    public File file;
    public String desc;

    /**
     * Class constructor
     *
     * @param file
     * @param desc
     */
    public DescriptedFile(File file, String desc) {
        super(file.toURI());
        this.file = file;
        this.desc = desc;
    }

    public String toString() {
        return file.toString();
    }

} // End class DescriptedFile
