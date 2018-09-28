/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.core.api;

import javax.swing.*;

/**
 * This is like xtools.Tool
 * except that it is run from a visualization in a UI.
 * <p/>
 * HAS TO BE HERE ELSE JUNIT BARFS
 * <p/>
 *
 * @author Aravind Subramanian
 */
public interface VTool extends Action {

    public String getTitle();

    public VTool.Runnable getRunnable();

    public JComponent getComponent();

    public JComponent getInitFocusedComponent();

    public boolean isRequiredAllSet();

    public String getHelpURL();

    // The reason didnt re-use object.runnable is so that this can throw an exception
    // just like with tool execs the exception is best handled at a higher level
    public interface Runnable {

        public void run() throws Exception;

    }

} // End class ModalAction
