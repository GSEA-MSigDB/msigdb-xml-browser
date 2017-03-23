/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.actions;

import edu.mit.broad.msigdb_browser.genome.XLogger;

import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * Base action for actions that triggers an external process on a File.
 * For example, opening a file in Microsoft Excel.
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public abstract class ExtAction extends XAction {

    private String fPath;
    protected final Logger log = XLogger.getLogger(ExtAction.class);

    protected ExtAction(String id, String name, String description, Icon icon) {
        super(id, name, description, icon);
    }
    
    /**
     * @param file The File to run the External Action on
     */
    public void setPath(String path) {
        this.fPath = path;
    }

    public String getPath() {
        return fPath;
    }

}    // End ExtAction
