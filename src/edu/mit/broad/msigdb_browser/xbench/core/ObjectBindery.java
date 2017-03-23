/*
 * Copyright (c) 2003-2017 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package edu.mit.broad.msigdb_browser.xbench.core;

import edu.mit.broad.msigdb_browser.genome.parsers.ParserFactory;

import javax.swing.*;

/**
 * Factory of binders and xchoosers for objects.
 * <p/>
 * Binder -> something that attaches objects to a control such as a JComboBox
 * Chooser -> a UI that allows interactive choice of some object(s)
 *
 * @author Aravind Subramanian
 * @version %I%, %G%
 */
public class ObjectBindery {

    /**
     * Privatized class constructor
     * Only static methods.
     */
    private ObjectBindery() {
    }

    public static ComboBoxModel getModel(Class c) {
        return ParserFactory.getCache().createBoxModel(c);
    }
}    // End ObjectBindery


