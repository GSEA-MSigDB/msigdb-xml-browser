/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.api.frameworks.fiji;

import edu.mit.broad.msigdb_browser.xbench.core.Window;

import javax.swing.*;

/**
 * @author Aravind Subramanian
 */
// TODO: Review this class and the interface.  Unlikely they directly do anything useful.
public class JideWindow implements Window {

    private String fTitle;

    private JComponent fComp;

    /**
     * Class constructor
     *
     * @param comp
     */
    public JideWindow(final String title, final JComponent comp) {
        this.fComp = comp;
        this.fTitle = title;
    }

}
